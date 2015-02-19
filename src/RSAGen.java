//Qingxiang Jia
import java.io.*;
import java.security.*;

/**
 * Generates RSA keys.
 */
public class RSAGen
{
    /**
     * Generates key pair.
     * @param fn File name of the key pair
     */
    public void gen(String fn)
    {
        KeyPair pair = genKeyPair();
        deserializeKeyPair(fn, pair);
    }

    /**
     * Deserialize the keys.
     * @param fn    File name of which key will be stored
     * @param pair  Key pair
     */
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

    /**
     * Generates 2048-bit RSA key pair.
     * @return  Key pair
     */
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

    // quick test: I used this to generate RSA keys
    public static void main(String[] args) throws Exception
    {
        RSAGen gen = new RSAGen();
        gen.gen("c1");

        // verification
        FileInputStream fin;
        ObjectInputStream oin;

        fin = new FileInputStream("c1i.ser");
        oin = new ObjectInputStream(fin);
        PrivateKey privateKey = (PrivateKey) oin.readObject();
        oin.close();
        fin.close();
        System.out.println(privateKey.toString());

        fin = new FileInputStream("c1p.ser");
        oin = new ObjectInputStream(fin);
        PublicKey publicKey = (PublicKey) oin.readObject();
        oin.close();
        fin.close();
        System.out.println(publicKey.toString());
    }
}
