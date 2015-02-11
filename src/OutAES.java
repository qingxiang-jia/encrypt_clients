import javax.crypto.spec.IvParameterSpec;

public class OutAES
{
    byte[] content; // cipher text or plain text
    IvParameterSpec iv; // initialization vector for CBC mode
    public OutAES(byte[] content, IvParameterSpec iv)
    {
        this.content = content;
        this.iv = iv;
    }
}
