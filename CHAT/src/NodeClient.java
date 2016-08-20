import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Nitish on 8/17/2016.
 */
public class NodeClient {

    private static NodeClient client=null;

    private NodeClient()
    {

    }

    public synchronized static NodeClient getInstance()
    {
        if(client == null)
        {
            client=new NodeClient();
        }

        return client;

    }


    public void request(ClientDetail peer,Node node) throws Exception {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(peer.getIpAddress(),peer.getListeningPort()),10000);
        PrintWriter writer =new PrintWriter(socket.getOutputStream());
        writer.println("wanttochat " +  Node.getMyIpAddress() + " " + Node.getMyListeningPort());
        writer.flush();
        writer.close();

        isRequestAccepted(socket);

        socket.close();
    }

    private boolean isRequestAccepted(Socket socket)
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String messageStr;
        try {
            while ((messageStr = reader.readLine()) != null) {
                if(messageStr.equals("sure"))
                {
                    return true;
                }
            }
        }
        catch(Exception e)
        {

        }
        return false;
    }

    public void sendReply()
    {


    }

}
