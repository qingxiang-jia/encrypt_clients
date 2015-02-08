import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;

public class client1
{
    public void execMission(String pwd, String fileName, String serverIP, String port, String RSA)
    {

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
        FileInputStream fileInputStream = null;
        byte[] bFile = new byte[(int) file.length()];
        try
        {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return bFile;
    }

}
