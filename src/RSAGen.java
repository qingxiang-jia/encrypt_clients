import java.io.*;
import java.security.*;

public class RSAGen
{
    public void gen(String fn)
    {
        KeyPair pair = genKeyPair();
        deserializeKeyPair(fn, pair);
    }

    private void deserializeKeyPair(String fn, KeyPair pair)
    {
        try
        {
            FileOutputStream fout;
            ObjectOutputStream oout;

            fout = new FileOutputStream(fn+"p.ser");
            oout = new ObjectOutputStream(fout);
            oout.writeObject(pair.getPublic());
            oout.close();
            fout.close();

            fout = new FileOutputStream(fn+"i.ser");
            oout = new ObjectOutputStream(fout);
            oout.writeObject(pair.getPrivate());
            oout.close();
            fout.close();
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
    public static void main(String[] args) throws Exception
    {
        RSAGen gen = new RSAGen();
        gen.gen("c2");

        // verification
        FileInputStream fin;
        ObjectInputStream oin;

        fin = new FileInputStream("c2i.ser");
        oin = new ObjectInputStream(fin);
        PrivateKey privateKey = (PrivateKey) oin.readObject();
        oin.close();
        fin.close();
        System.out.println(privateKey.toString());

        fin = new FileInputStream("c2p.ser");
        oin = new ObjectInputStream(fin);
        PublicKey publicKey = (PublicKey) oin.readObject();
        oin.close();
        fin.close();
        System.out.println(publicKey.toString());
    }
}
