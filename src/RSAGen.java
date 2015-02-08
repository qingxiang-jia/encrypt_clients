import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class RSAGen
{
    public void gen(String fn)
    {
        KeyPair pair = genKeyPair();
        writeKeyPair(fn, pair);
    }

    private void writeKeyPair(String fn, KeyPair pair)
    {
        try
        {
            FileOutputStream stream1 = new FileOutputStream(fn+"_pub");
            stream1.write(pair.getPublic().getEncoded());
            stream1.close();
            FileOutputStream stream2 = new FileOutputStream(fn+"_pri");
            stream2.write(pair.getPublic().getEncoded());
            stream2.close();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private KeyPair genKeyPair()
    {
        KeyPair pair = null;
        try
        {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            pair = keyPairGenerator.genKeyPair();
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return pair;
    }

    // quick test
    public static void main(String[] args)
    {
        RSAGen gen = new RSAGen();
        gen.gen("c2");
    }
}
