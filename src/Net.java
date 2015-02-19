//Qingxiang Jia
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Utility class that handles all network related operation
 */
public class Net
{
    /**
     * A wrapper to sends the bundle.
     * @param bundle    Bundle to be sent
     * @param serverIP  IP address of the server
     * @param portNum   Port number the server listens on
     */
    public void sendBundle(Cargo bundle, String serverIP, String portNum)
    {
        int port = Integer.parseInt(portNum);
        sendBundle(bundle, serverIP, port);
    }

    /**
     * Sends the bundle.
     * @param bundle    Bundle to be sent
     * @param serverIP  IP of the server
     * @param portNum   Port number the server listens on
     */
    public void sendBundle(Cargo bundle, String serverIP, int portNum)
    {
        Socket sock = null;
        ObjectOutputStream meToDest = null; // sends the object via ObjectOutputStream
        try // create a socket to the server and send the bundle
        {
            sock = new Socket(serverIP, portNum);
            meToDest = new ObjectOutputStream(sock.getOutputStream());
            meToDest.writeObject(bundle);
            meToDest.close();
            sock.close();
            System.out.println("Sent");
        } catch (UnknownHostException e)
        {
            System.out.println("The host is unknown.");
        } catch (IOException e)
        {
            System.out.println("Server is down.");
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
                System.out.println("Failed to close the stream or socket.");
            }
        }
    }
}
