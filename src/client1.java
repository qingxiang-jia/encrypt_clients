import java.nio.charset.Charset;
import java.security.PrivateKey;

/**
 * client1 encrypts file (args[1]) using password (args[0]). client1 sends file to server (arg[2])
 * at port args[3]. The private key is args[4].
 */
public class client1 implements InputCheck
{
    /**
     * Encrypts file. Sends it to server.
     * @param pwd       Password
     * @param fileName  File (to be sent) name
     * @param serverIP  IP address
     * @param port      Port number that server listens to
     * @param c1i       Path of private key
     */
    public void execMission(String pwd, String fileName, String serverIP, String port, String c1i)
    {
        PrivateKey privateKey = (PrivateKey) Util.deserialize(c1i); // get private key
        Net net = new Net(); // helper function that handles network related operation
        byte[] file = Util.readFile(fileName); // read in file, could be simpler, but CLIC machine runs Java 1.6
        byte[] cipherText = Crypto.encryptAES(pwd.getBytes(Charset.forName("UTF-8")), file); // encrypt file using password
//        Util.writeFile(cipherText, "serverdataSameSize");
        byte[] hash = Crypto.getHash(file); // get hash of the file (before encryption)
        byte[] eHash = Crypto.encryptRSA(hash, privateKey); // using private key to sign the file (encrypt hash)
        byte[] ePwd = Crypto.encryptRSA(pwd.getBytes(Charset.forName("UTF-8")), privateKey); // using private key to encrypt password
        System.out.printf("size of file: %d   size of cipher text: %d\n", file.length, cipherText.length);
        System.out.printf("size of hash: %d\n", hash.length);
        System.out.printf("size of ehash: %d\n", eHash.length);
        System.out.printf("size of ePwd: %d\n", ePwd.length);
        net.sendBundle(new Cargo(ePwd, cipherText, eHash), serverIP, port); // send encrypted password, ciphertext, and signature
        System.out.println("sent");
    }

    public void runWithInputCheck(String[] args)
    {
        String pwd, fileName, serverIP, port, c1i;
        boolean parameterOK = false;
        try
        {
            pwd = args[0];
            fileName = args[1];
            serverIP = args[2];
            port = args[3];
            int lookForException = Integer.parseInt(port);
            c1i = args[4];
            parameterOK = true;
            execMission(pwd, fileName, serverIP, port, c1i);
        } catch (IndexOutOfBoundsException e)
        {
            System.out.println("Number of parameters should be 5.");
        } catch (NumberFormatException e)
        {
            System.out.println("Port number is invalid.");
            parameterOK = false;
        }
        finally
        {
            if (!parameterOK)
                System.out.println("usage: client1 <password> <path to file to be sent> <server IP> <port number> <path to private key>");
        }
    }


    public static void main(String[] args)
    {
        client1 c1 = new client1();
        // c1.execMission("1234567887654321", "liukanshan.jpg", "10.0.0.1", "8087", "c1i.ser");
//        c1.execMission(args[0], args[1], args[2], args[3], args[4]);
        c1.runWithInputCheck(args);
    }

}
