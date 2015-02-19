//Qingxiang Jia
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

/**
 * client2 listens to port2 for incoming file, decrypts the file, checks for validity
 */
public class client2 implements InputCheck
{
    boolean shouldRun; // set to true to run
    public void run(int port2)
    {
        try
        {
            ServerSocketChannel c22s = ServerSocketChannel.open(); // channel connects client2 to server (c22s)
            c22s.socket().bind(new InetSocketAddress(port2)); // client2 listens on port2
            c22s.configureBlocking(false); // use selector, so non-blocking

            Selector selector = Selector.open();
            SelectionKey fromS = c22s.register(selector, SelectionKey.OP_ACCEPT); // accept data from server

            while (shouldRun) // client2 also acts like a server
            {
                int readyChannels = selector.select(); // select channels that are ready
                if (readyChannels == 0) // no one contacts, keep looping
                    continue;

                Set<SelectionKey> selectedKeys = selector.selectedKeys(); // get a set of client to serve
                Iterator<SelectionKey> iter = selectedKeys.iterator();
                while (iter.hasNext()) // serve each client one after another
                {
                    SelectionKey key = iter.next();
                    System.out.println("got data from server"); // server is the client
                    if (key == fromS)
                        validation(key.channel()); // if is from server, validate the packet
                }
                iter.remove(); // this client is served, remove it from the set
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Validates the contents from server.
     * @param selectableChannel Channel connects server and client2
     */
    // order: byte[] ePwd, byte[] cipherText, byte[] eHash
    private void validation(SelectableChannel selectableChannel)
    {
        SocketChannel toServer = null; // channel connects server and client2
        ObjectInputStream ois = null;
        try
        {
            toServer = ((ServerSocketChannel) selectableChannel).accept();
            ois = new ObjectInputStream(toServer.socket().getInputStream()); // get data from server
            Cargo bundle = (Cargo) ois.readObject();
            System.out.printf("ePwd size: %d   cipherText size: %d   eHash size: %d\n", bundle.ePwd.length, bundle.cipherText.length, bundle.eHash.length);

            PublicKey c1p = (PublicKey) Util.deserialize("c1p.ser"); // client2 has client1's public key
            byte[] hash = Crypto.decryptRSA(bundle.eHash, c1p); // get the hash of the decrypted file
            byte[] pwd = Crypto.decryptRSA(bundle.ePwd, c1p); // get the decrypted password
            byte[] file = Crypto.decryptAES(pwd, bundle.cipherText); // get the decrypted file
            byte[] dHash = null; // decrypted hash of the file
            if (file != null)
                dHash = Crypto.getHash(file); // if file swapped, file will be null since decryption failed
            if (dHash != null && Arrays.equals(hash, dHash)) // if dHash == hash, file is not swapped
            {
                System.out.println("Verification Passed");
                Util.writeFile(file, "client2data");
            }
            else
                System.out.println("Verification Failed");

        } catch (IOException e)
        {
            System.out.println("Either ServerSocketChannel failed to accept; or ObjectInputStream failed to get stream, or failed to read out the object.");
        } catch (ClassNotFoundException e)
        {
            System.out.println("ObjectInputStream did read the data, but there is no corresponding class found.");
        } finally
        {
            try
            {
                if (ois != null)
                    ois.close();
                if (toServer != null)
                    toServer.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void runWithInputCheck(String[] args)
    {
        if (args.length != 1)
        {
            System.out.println("Number of parameters should be 1.");
            System.out.println("usage: client2 <port number>");
            System.exit(0);
        } else
        {
            try
            {
                int port = Integer.parseInt(args[0]);
                if (port <= 0)
                    throw new NumberFormatException();
                run(port);
            } catch (NumberFormatException e)
            {
                System.out.println("Port number is invalid.");
            } finally
            {
                System.out.println("usage: client2 <port number>");
            }
        }
    }

    public static void main(String[] args)
    {
        client2 c2 = new client2();
        c2.shouldRun = true;
//        c2.run(Integer.parseInt(args[0]));
        c2.runWithInputCheck(args);
    }
}
