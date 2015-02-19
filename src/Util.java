import java.io.*;

/**
 * Utility class than handles all IOs.
 */
public class Util
{
    /**
     * Reads in a file, and return its byte array representation.
     * Java 1.7+ built-in class can handle this but CLIC machines have only JVM 1.6.
     * @param fileName  The file to be read
     * @return  Byte array that contains all bytes of the file.
     */
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
            System.out.println("File "+fileName+" not found.");
        } catch (IOException e)
        {
            System.out.println("Cannot read " + fileName + ".");
        } finally
        {
            try
            {
                if (stream != null)
                    stream.close();
            } catch (IOException e)
            {
                System.out.println("Failed to close stream.");
            }
        }
        return fileInBytes;
    }

    /**
     * Writes data into a file.
     * @param data      Byte array contains data
     * @param fileName  File name
     */
    public static void writeFile(byte[] data, String fileName)
    {
        File file = new File(fileName);
        FileOutputStream stream = null;
        try
        {
            stream = new FileOutputStream(file);
            stream.write(data);
            System.out.println(data.length + " written");
        } catch (FileNotFoundException e)
        {
            System.out.println("File "+fileName+" not found.");
        } catch (IOException e)
        {
            System.out.println("Failed to write data to file " + fileName);
        } finally
        {
            try
            {
                if (stream != null)
                    stream.close();
            } catch (IOException e)
            {
                System.out.println("Failed to close stream.");
            }
        }
    }

    /**
     * Deserializes a file.
     * @param fileName  Path to the file
     * @return  An object that just been deserialized
     */
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
        } catch (FileNotFoundException e)
        {
            System.out.println("File "+fileName+" not found.");
        } catch (IOException e)
        {
            System.out.println("Failed to create ObjectInputStream during deserialization.");
        } catch (ClassNotFoundException e)
        {
            System.out.println("Cannot find a class for data acquired.");
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
                System.out.println("Failed to close stream.");
            }
        }
        return null;
    }
}
