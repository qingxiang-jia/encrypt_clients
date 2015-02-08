import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

public class server
{
    Boolean shouldRun;
    Boolean isTrust;
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
                    {
                        serveClient1();
                    }
                }
                iter.remove(); // this client is served, remove it from the set
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void serveClient1()
    {

    }
}
