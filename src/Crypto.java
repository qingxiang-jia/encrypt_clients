import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;

/**
 * Utility class that handles all encryption/decryption related operations.
 */
public class Crypto
{
    /**
     * Wrapper to encrypt file using password (AES).
     * @param pwd   Password, can be number, letter, and , . / < > ? ; : ’ " [ ] { } \ | ! @ # $ % ˆ & * ( ) - _ = +
     * @param file  File to be encrypted
     * @return  Encrypted byte array
     */
    public static byte[] encryptAES(byte[] pwd, byte[] file) { return AES(pwd, file, 1); }

    /**
     * Wrapper to decrypt file using password (AES)
     * @param pwd   Password, can be number, letter, and , . / < > ? ; : ’ " [ ] { } \ | ! @ # $ % ˆ & * ( ) - _ = +
     * @param file  File to be decrypted
     * @return  Decrypted byte array
     */
    public static byte[] decryptAES(byte[] pwd, byte[] file) { return AES(pwd, file, 2); }

    /**
     * The actual AES encryption/decryption procedure.
     * @param pwd   Password
     * @param file  File to be encrypted/decrypted
     * @param mode  1 = encryption, 2 = encryption
     * @return  encrypted/decrypted byte array
     */
    private static byte[] AES(byte[] pwd, byte[] file, int mode)
    {
        try
        {
            Cipher AESCipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); // cannot use PKCS7 with AES in Java
            SecretKey AESKey = new SecretKeySpec(pwd, "AES"); // set up key using password
            AESCipher.init(mode, AESKey, new IvParameterSpec(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0})); // IV, as TA suggested, hardcoded
            return AESCipher.doFinal(file); // return the encrypted/decrypted byte array
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
            System.out.println("The file to be en/decrypted has a size not of multiple of 16. The file is tampered!");
        } catch (BadPaddingException e)
        {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Wrapper to encrypt a file using private/public key.
     * @param file  File to be encrypted
     * @param key   Private/public key
     * @return  Encrypted byte array
     */
    public static byte[] encryptRSA(byte[] file, Key key) { return RSA(file, key, 1); }

    /**
     * Wrapper to decrypt a file using private/public key
     * @param file  File to be decrypted
     * @param key   Private/public key
     * @return  Decrypted byte array
     */
    public static byte[] decryptRSA(byte[] file, Key key) { return RSA(file, key, 2); }

    /**
     * Actual procedure that encrypts/decrypts a file using private/public key.
     * @param content   File to be encrypted/decrypted
     * @param key       Private/public key
     * @param mode      1 = encryption, 2 = decryption
     * @return
     */
    private static byte[] RSA(byte[] content, Key key, int mode)
    {
        try
        {
            Cipher RSACipher = Cipher.getInstance("RSA"); // singleton RSA cipher
            RSACipher.init(mode, key);  // initialize cipher
            return RSACipher.doFinal(content); // return the encrypted/decrypted file
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

    /**
     * Returns hash of a file.
     * @param file  File to be hashed
     * @return      Hash of the file
     */
    public static byte[] getHash(byte[] file)
    {
        try
        {
            MessageDigest hasher = MessageDigest.getInstance("SHA-256"); // homework requirement
            hasher.update(file);
            return hasher.digest();
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
