import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class server
{
    Boolean shouldRun;
    Boolean isTrust;
    byte[] serverData;
    public void run(int port1, int port2, char mode)
    {
        try
        {
            ServerSocketChannel s2c1 = ServerSocketChannel.open();
            ServerSocketChannel s2c2 = ServerSocketChannel.open();
            s2c1.socket().bind(new InetSocketAddress(port1));
            s2c2.socket().bind(new InetSocketAddress(port2));
            s2c1.configureBlocking(false);
            s2c2.configureBlocking(false);

            Selector selector = Selector.open();
            SelectionKey fromC1 = s2c1.register(selector, SelectionKey.OP_READ);

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
                    if (key.isReadable() && key == fromC1)
                        serveClient1(key.channel(), s2c2);
                }
                iter.remove(); // this client is served, remove it from the set
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void serveClient1(SelectableChannel selectableChannel, ServerSocketChannel s2c2)
    {
        try
        {
            SocketChannel toClient1 = ((ServerSocketChannel) selectableChannel).accept();
            ObjectInputStream ois = new ObjectInputStream(toClient1.socket().getInputStream());
            Cargo bundle = (Cargo) ois.readObject();
            if (!isTrust)
                bundle.cipherText = this.serverData; // malicious server
            sendBundle(bundle, s2c2); // pass the packet to client2
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    // order: byte[] ePwd, byte[] cipherText, byte[] eHash
    private void sendBundle(Cargo bundle, ServerSocketChannel s2c2)
    {
        try
        {
            SocketChannel socketChannel = s2c2.accept();
            ObjectOutputStream c2s = new ObjectOutputStream(socketChannel.socket().getOutputStream());
            c2s.writeObject(bundle);
            c2s.close();
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
