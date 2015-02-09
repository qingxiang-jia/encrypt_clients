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
        try
        {
            Socket sock = new Socket(serverIP, portNum);
            ObjectOutputStream meToDest = new ObjectOutputStream(sock.getOutputStream());
            meToDest.writeObject(bundle);
            meToDest.close();
            sock.close();
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
