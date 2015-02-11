import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

public class Crypto
{
    public static byte[] encryptAES(byte[] pwd, byte[] file)
    {
        return AES(pwd, file, 1);
    }

    public static byte[] decryptAES(byte[] pwd, byte[] file)
    {
        return AES(pwd, file, 2);
    }

    private static byte[] AES(byte[] pwd, byte[] file, int mode)
    {
        try
        {
            Cipher AESCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKey AESKey = new SecretKeySpec(pwd, "AES");
            AESCipher.init(mode, AESKey, new IvParameterSpec(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
            return AESCipher.doFinal(file);
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
        } catch (InvalidAlgorithmParameterException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] encryptRSA(byte[] file, Key key)
    {
        return RSA(file, key, 1);
    }

    public static byte[] decryptRSA(byte[] file, Key key)
    {
        return RSA(file, key, 2);
    }

    private static byte[] RSA(byte[] content, Key key, int mode)
    {
        try
        {
            Cipher RSACipher = Cipher.getInstance("RSA");
            RSACipher.init(mode, key);
            return RSACipher.doFinal(content);
        }  catch (NoSuchAlgorithmException e)
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
