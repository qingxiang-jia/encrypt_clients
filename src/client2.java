import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.security.PublicKey;
import java.util.Arrays;
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
            SelectionKey fromS = c22s.register(selector, SelectionKey.OP_ACCEPT);

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
                    if (key == fromS)
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
        SocketChannel toClient1 = null;
        ObjectInputStream ois = null;
        try
        {
            toClient1 = ((ServerSocketChannel) selectableChannel).accept();
            ois = new ObjectInputStream(toClient1.socket().getInputStream());
            Cargo bundle = (Cargo) ois.readObject();
            System.out.printf("ePwd size: %d   cipherText size: %d   eHash size: %d\n", bundle.ePwd.length, bundle.cipherText.length, bundle.eHash.length);

            PublicKey c1p = (PublicKey) Util.deserialize("c1p.ser");
            byte[] hash = Crypto.decryptRSA(bundle.eHash, c1p);
            byte[] pwd = Crypto.decryptRSA(bundle.ePwd, c1p);
            byte[] file = Crypto.decryptAES(pwd, bundle.cipherText);
            byte[] dHash = null;
            if (file != null)
                dHash = Crypto.getHash(file);
            if (dHash != null && Arrays.equals(hash, dHash))
            {
                System.out.println("Verification Passed");
                Util.writeFile(file, "client2data");
            }
            else
                System.out.println("Verification Failed");

        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (ois != null)
                    ois.close();
                if (toClient1 != null)
                    toClient1.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args)
    {
        client2 c2 = new client2();
        c2.shouldRun = true;
        c2.run(Integer.parseInt(args[0]));
    }


}
