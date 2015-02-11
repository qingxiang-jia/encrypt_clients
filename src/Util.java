import java.io.*;

public class Util
{
    public static byte[] readFile(String fileName)
    {
        File file = new File(fileName);
        FileInputStream stream = null;
        byte[] fileInBytes = new byte[(int) file.length()];
        try
        {
            stream = new FileInputStream(file);
            int count = stream.read(fileInBytes);
            System.out.println("Read in " + count + " bytes");
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (stream != null)
                    stream.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return fileInBytes;
    }

    public static void writeFile(byte[] data, String fileName)
    {
        File file = new File(fileName);
        FileOutputStream stream = null;
        try
        {
            stream = new FileOutputStream(file);
            stream.write(data);
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (stream != null)
                    stream.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static Object deserialize(String fileName)
    {
        FileInputStream fin = null;
        ObjectInputStream oin = null;
        try
        {
            fin = new FileInputStream(fileName);
            oin = new ObjectInputStream(fin);
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
        } finally
        {
            try
            {
                if (oin != null)
                    oin.close();
                if (fin != null)
                    fin.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }
}
