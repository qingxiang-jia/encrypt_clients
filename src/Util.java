import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Util
{
    public static byte[] readFile(String filename)
    {
        File file = new File(filename);
        FileInputStream stream = null;
        byte[] fileInBytes = new byte[(int) file.length()];
        try
        {
            stream = new FileInputStream(file);
            int count = stream.read(fileInBytes);
            System.out.println("Read in " + count + " bytes");
            stream.close();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return fileInBytes;
    }

    public static Object deserialize(String fileName)
    {
        try
        {
            FileInputStream fin = new FileInputStream(fileName);
            ObjectInputStream oin = new ObjectInputStream(fin);
            Object obj = oin.readObject();
            oin.close();
            fin.close();
            return obj;
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
