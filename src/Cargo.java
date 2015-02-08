import java.io.Serializable;

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
