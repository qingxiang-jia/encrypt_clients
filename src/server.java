//Qingxiang Jia
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
public class server implements InputCheck
{
    boolean shouldRun; // set to true to run
    byte[] serverData; // in untrusted mode, use this data to tamper the packet from client1

    /**
     * Using selector so that it has ability to handle multiple clients (not required)
     *
     * @param port1     Port number for client1 to contact
     * @param port2     Port number of client2
     * @param client2IP IP address of client2
     * @param mode      'u' = untrusted, 't' = trusted
     */
    public void runServer(int port1, int port2, String client2IP, char mode)
    {
        ServerSocketChannel s2c1 = null;
        Selector selector = null;
        try
        {
            s2c1 = ServerSocketChannel.open(); // open a server-to-client SocketChannel
            s2c1.socket().bind(new InetSocketAddress(port1)); // listens on port1 for client1 to contact
            s2c1.configureBlocking(false); // must be in non-blocking mode since using selector

            selector = Selector.open();
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
            System.out.println("Either ServerSocketChannel failed to open, or to bind, or Selector failed to open.");
        } finally // close channels, sockets, selector
        {
            try
            {
                if (selector != null)
                    selector.close();
                if (s2c1 != null)
                    s2c1.close();
            } catch (IOException e)
            {
                System.out.println("Failed to close ServerSocketChannel and/or Selector.");
            }
        }
    }

    /**
     * Validates inpute parameter before server starts.
     * @param args Commandline input
     */
    public void runWithInputCheck(String[] args)
    {
        int port1, port2;
        String client2IP;
        char mode;
        boolean parameterOK = false;
        try
        {
            port1 = Integer.parseInt(args[0]);
            port2 = Integer.parseInt(args[1]);
            client2IP = args[2];
            mode = args[3].charAt(0);
            parameterOK = true;
            if (port1 > 0 && port2 > 0 && (mode == 't' || mode == 'u') && (client2IP != null))
                runServer(port1, port2, client2IP, mode);
            else
            {
                parameterOK = false;
                if (port1 <= 0 || port2 <= 0)
                    System.out.println("Port number is invalid.");
                if (mode != 't' && mode != 'u')
                    System.out.println("mode has to be t or u.");
            }
        }
        catch (NumberFormatException e)
        {
            System.out.println("Port number needs to be integers.");
        }
        catch (IndexOutOfBoundsException e)
        {
            System.out.println("Number of parameters should be 4.");
        }
        finally
        {
            if (!parameterOK)
                System.out.println("usage: server <port listen to client1> <port client2 listens> <client2 IP> <mode>");
        }
    }

    /**
     * In untrusted mode, swap ciphertext with serverData, and then forwards to client2; otherwise, just forwards to client2.
     *
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
                this.serverData = Util.readFile("serverdata");
                System.out.println("maliciously swapped file");
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
        serv.runWithInputCheck(args);
    }

}