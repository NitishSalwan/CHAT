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
        this.activeClients = activeClients;
    }

    private CentralServer()
    {
        activeClients = new ArrayList<>();
    }

    public synchronized static CentralServer getInstance()
    {
        if(centralServerInstance==null)
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
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                CentralServerMessage tempMessage;

                try
                {
                    while((tempMessage=(CentralServerMessage)ois.readObject())!=null)
                        {
                            processMessage(tempMessage,socket);
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

    private void processMessage(CentralServerMessage message,Socket socket)
    {
        if(message.getType().equals("register"))
        {
            centralServerInstance.getActiveClients().add(message.getEntityDetail());
            sendAck(message,"registrationDone",socket);


        }
        else if(message.getType().equals("unregister"))
        {
            centralServerInstance.getActiveClients().remove(message.getEntityDetail());
            sendAck(message,"registrationCancel",socket);
        }
        else if(message.getType().equals("getclientslist"))
        {
            sendActiveClientsList(message,getActiveClients(),socket);
        }
        else {
            throw new UnsupportedOperationException("No method with corresponding message");
        }
    }




    private void sendAck(CentralServerMessage message,String action,Socket socket) {
        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.println(action);
            writer.flush();
            writer.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }



    private void sendActiveClientsList(CentralServerMessage message,List<ClientDetail> activeClients,Socket socket) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            for(ClientDetail clientDetail : getActiveClients())
            {
                out.writeObject(clientDetail);
            }
            out.close();
            socket.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}





