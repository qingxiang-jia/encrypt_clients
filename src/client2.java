import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

// client2 listens to port2 for incoming file
public class client2
{
    boolean shouldRun;
    public void run(int port2)
    {
        try
        {
            ServerSocketChannel c22s = ServerSocketChannel.open();
            c22s.socket().bind(new InetSocketAddress(port2));
            c22s.configureBlocking(false);

            Selector selector = Selector.open();
            SelectionKey fromS = c22s.register(selector, SelectionKey.OP_READ);

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
                    if (key.isReadable() && key == fromS)
                        validation(key.channel());
                }
                iter.remove(); // this client is served, remove it from the set
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    // order: byte[] ePwd, byte[] cipherText, byte[] eHash
    private void validation(SelectableChannel selectableChannel)
    {
        try
        {
            SocketChannel toClient1 = ((ServerSocketChannel) selectableChannel).accept();
            ObjectInputStream ois = new ObjectInputStream(toClient1.socket().getInputStream());
            Cargo bundle = (Cargo) ois.readObject();
            System.out.printf("ePwd size: %d   cipherText size: %d   eHash size: %d\n", bundle.ePwd.length, bundle.cipherText.length, bundle.eHash.length);
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }


}
