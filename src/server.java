import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class server
{
    boolean shouldRun;
    byte[] serverData;

    public void runServer(int port1, int port2, String client2IP, char mode)
    {
        try
        {
            ServerSocketChannel s2c1 = ServerSocketChannel.open();
            s2c1.socket().bind(new InetSocketAddress(port1));
            s2c1.configureBlocking(false);

            Selector selector = Selector.open();
            SelectionKey fromC1 = s2c1.register(selector, SelectionKey.OP_ACCEPT);

            while (shouldRun)
            {
                int readyChannels = selector.select();
                if (readyChannels == 0) // no one contacts, keep looping
                    continue;

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();
                while (iter.hasNext()) // serve each client one after another
                {
                    SelectionKey key = iter.next();
                    System.out.println("got data from client1");
                    if (key == fromC1)
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

    private void serveClient1(SelectableChannel selectableChannel, String client2IP, int port2, char mode)
    {
        try
        {
            SocketChannel toClient1 = ((ServerSocketChannel) selectableChannel).accept();
            ObjectInputStream ois = new ObjectInputStream(toClient1.socket().getInputStream());
            Cargo bundle = (Cargo) ois.readObject();
            if (mode == 'u')
                bundle.cipherText = this.serverData; // malicious server
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