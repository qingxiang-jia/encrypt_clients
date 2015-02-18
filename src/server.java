import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * Server that takes a file from client1 and forwards to client2
 * Can be malicious in untrusted mode (args[3])
 */
public class server
{
    boolean shouldRun; // set to true to run
    byte[] serverData; // in untrusted mode, use this data to tamper the packet from client1

    /**
     * Using selector so that it has ability to handle multiple clients (not required)
     * @param port1     Port number for client1 to contact
     * @param port2     Port number of client2
     * @param client2IP IP address of client2
     * @param mode      'u' = untrusted, 't' = trusted
     */
    public void runServer(int port1, int port2, String client2IP, char mode)
    {
        try
        {
            ServerSocketChannel s2c1 = ServerSocketChannel.open(); // open a server-to-client SocketChannel
            s2c1.socket().bind(new InetSocketAddress(port1)); // listens on port1 for client1 to contact
            s2c1.configureBlocking(false); // must be in non-blocking mode since using selector

            Selector selector = Selector.open();
            SelectionKey fromC1 = s2c1.register(selector, SelectionKey.OP_ACCEPT); // select when accepted

            while (shouldRun) // waiting for client to contact
            {
                int readyChannels = selector.select(); // a client has contacted
                if (readyChannels == 0) // no one contacts, keep looping
                    continue;

                Set<SelectionKey> selectedKeys = selector.selectedKeys(); // a set of clients to serve
                Iterator<SelectionKey> iter = selectedKeys.iterator();
                while (iter.hasNext()) // serve each client one after another
                {
                    SelectionKey key = iter.next();
                    System.out.println("got data from client1"); // in this project, only client1 will be here
                    if (key == fromC1) // is client1
                        serveClient1(key.channel(), client2IP, port2, mode);
                }
                iter.remove(); // this client is served, remove it from the set
            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * In untrusted mode, swap ciphertext with serverData, and then forwards to client2; otherwise, just forwards to client2.
     * @param selectableChannel Channel that connects client1 and server
     * @param client2IP         IP address of client2
     * @param port2             Port that client2 is running on
     * @param mode              'u' = untrusted, 't' = trusted
     */
    private void serveClient1(SelectableChannel selectableChannel, String client2IP, int port2, char mode)
    {
        try
        {
            SocketChannel toClient1 = ((ServerSocketChannel) selectableChannel).accept(); // channel that connects to client1
            ObjectInputStream ois = new ObjectInputStream(toClient1.socket().getInputStream()); // read from client1
            Cargo bundle = (Cargo) ois.readObject();
            if (mode == 'u') // swap file if in untrusted mode
            {
                this.serverData = Util.readFile("serverdataSameSize");
                bundle.cipherText = this.serverData; // malicious server
            }
            Net net = new Net();
            net.sendBundle(bundle, client2IP, port2); // pass the packet to client2
            System.out.println("data sent to client2");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    // quick test
    public static void main(String[] args)
    {
        server serv = new server();
        serv.shouldRun = true;
//        serv.runServer(8087, 8088, "127.0.0.1", 't');
        serv.runServer(Integer.parseInt(args[0]), Integer.parseInt(args[1]), args[2], args[3].charAt(0));
    }


}