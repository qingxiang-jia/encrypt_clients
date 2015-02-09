import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;

public class Crypto
{
    public static byte[] encryptAES(byte[] pwd, byte[] file)
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

    public static byte[] encryptRSA(byte[] file, PrivateKey privateKey)
    {// file here is the plain text hash
        try
        {
            Cipher RSAcipher = Cipher.getInstance("RSA");
            RSAcipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return RSAcipher.doFinal(file);
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
        return null;
    }

    public static byte[] getHash(byte[] file)
    {
        try
        {
            MessageDigest hasher = MessageDigest.getInstance("SHA-256");
            hasher.update(file);
            return hasher.digest();
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
