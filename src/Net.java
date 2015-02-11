import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Net
{
    public void sendBundle(Cargo bundle, String serverIP, String portNum)
    {
        int port = Integer.parseInt(portNum);
        sendBundle(bundle, serverIP, port);
    }

    public void sendBundle(Cargo bundle, String serverIP, int portNum)
    {
        Socket sock = null;
        ObjectOutputStream meToDest = null;
        try
        {
            sock = new Socket(serverIP, portNum);
            meToDest = new ObjectOutputStream(sock.getOutputStream());
            meToDest.writeObject(bundle);
            meToDest.close();
            sock.close();
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (meToDest != null)
                    meToDest.close();
                if (sock != null)
                    sock.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
