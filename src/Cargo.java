import java.io.Serializable;

/**
 * Bundle of encrypted password, ciphertext, and encrypted hash of the file
 */
public class Cargo implements Serializable
{
    byte[] ePwd, cipherText, eHash;
    public Cargo(byte[] ePwd, byte[] cipherText, byte[] eHash)
    {
        this.ePwd = ePwd;
        this.cipherText = cipherText;
        this.eHash = eHash;
    }
}
