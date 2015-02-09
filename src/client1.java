import java.nio.charset.Charset;
import java.security.PrivateKey;

public class client1
{
    public void execMission(String pwd, String fileName, String serverIP, String port, String RSA)
    {
        PrivateKey privateKey = (PrivateKey) Util.deserialize("c1i.ser");
        byte[] file = Util.readFile(fileName);
        byte[] cipherText = Crypto.encryptAES(pwd.getBytes(Charset.forName("UTF-8")), file);
        byte[] hash = Crypto.getHash(file);
        byte[] eHash = Crypto.encryptRSA(hash, privateKey);
        byte[] ePwd = Crypto.encryptRSA(pwd.getBytes(Charset.forName("UTF-8")), privateKey);
        System.out.printf("size of file: %d   size of cipher text: %d\n", file.length, cipherText.length);
        System.out.printf("size of hash: %d\n", hash.length);
        System.out.printf("size of ehash: %d\n", eHash.length);
        System.out.printf("size of ePwd: %d\n", ePwd.length);
    }

    // quick test
    public static void main(String[] args)
    {
        client1 c1 = new client1();
        c1.execMission("1234567887654321", "liukanshan.jpg", "10.0.0.1", "8087", "RSA");
    }

}
