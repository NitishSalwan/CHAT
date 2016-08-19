import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nitish on 8/17/2016.
 */
public class CentralServer implements Serializable {

    private List<ClientDetail> activeClients;
    private static CentralServer centralServerInstance=null;
    private static int ListeningPort;
    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader reader;



    public List<ClientDetail> getActiveClients() {
        return activeClients;
    }

    public void setActiveClients(List<ClientDetail> activeClients) {
        CentralServer.activeClients = activeClients;
    }

    private CentralServer()
    {
        activeClients = new ArrayList<>();
    }

    public static CentralServer getInstance()
    {
        if(activeClients==null)
        {
            centralServerInstance = new CentralServer();
        }

        return centralServerInstance;
    }

    void startServer()
    {
        try
        {
            serverSocket = new ServerSocket(ListeningPort);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        while (true)
        {
            try
            {
                socket = serverSocket.accept();
                ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());

                CentralServerMessage tempMessage;

                try
                {
                    while((tempMessage=(CentralServerMessage)ois.readObject())!=null)
                        {
                            processMessage(tempMessage);
                        }
                }
                catch (Exception e)
                {
                        e.printStackTrace();
                }

            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    private void processMessage(CentralServerMessage message)
    {
        if(message.getType().equals("register"))
        {
            centralServerInstance.getActiveClients().add(message.getEntityDetail());
            //sendAck(message,"registration done");
            sendActiveClientsList(message,getActiveClients());

        }
        else if(message.getType().equals("unregister"))
        {
            centralServerInstance.getActiveClients().remove(message.getEntityDetail());
            //sendAck(message,"registration cancel");
        }
        else {
            throw new UnsupportedOperationException("No method with corresponding message");
        }
    }



    /*
    private void sendAck(CentralServerMessage message,String action) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(message.getEntityDetail().getipAddress(), message.getEntityDetail().getlisteningPort()), 10000);
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.println(action);
            writer.flush();
            writer.close();
            socket.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    */


    private void sendActiveClientsList(CentralServerMessage message,List<ClientDetail> activeClients) {
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(message.getEntityDetail().getipAddress(), message.getEntityDetail().getlisteningPort()), 10000);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(getActiveClients());
            out.close();
            socket.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}



}

