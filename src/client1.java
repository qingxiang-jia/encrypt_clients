import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;

public class client1
{
    public void execMission(String pwd, String fileName, String serverIP, String port, String RSA)
    {
        byte[] file = readFile(fileName);
        byte[] cipherText = enryptAES(pwd.getBytes(Charset.forName("UTF-8")), file);
        System.out.printf("size of file: %d   size of cipher text: %d\n", file.length, cipherText.length);
    }

    private byte[] enryptAES(byte[] pwd, byte[] file)
    {
        byte[] cipherText = null;
        try
        {
            Cipher AESCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKey AESKey = new SecretKeySpec(pwd, "AES");
            AESCipher.init(Cipher.ENCRYPT_MODE, AESKey);
            cipherText = AESCipher.doFinal(file);
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        } catch (NoSuchPaddingException e)
        {
            e.printStackTrace();
        } catch (InvalidKeyException e)
        {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e)
        {
            e.printStackTrace();
        } catch (BadPaddingException e)
        {
            e.printStackTrace();
        }
        return cipherText;
    }

    private byte[] readFile(String filename)
    {
        File file = new File(filename);
        FileInputStream stream = null;
        byte[] fileInBytes = new byte[(int) file.length()];
        try
        {
            stream = new FileInputStream(file);
            int count = stream.read(fileInBytes);
            System.out.println("Read in " + count + " bytes");
            stream.close();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return fileInBytes;
    }

    // quick test
    public static void main(String[] args)
    {
        client1 c1 = new client1();
        c1.execMission("1234567887654321", "liukanshan.jpg", "10.0.0.1", "8087", "RSA");
    }

}
