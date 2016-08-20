import java.io.*;
import java.net.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;


/**
 * Created by Nitish on 8/17/2016.
 */
public class Node {

    private static String myIpAddress;
    private static int myListeningPort;
    private List<ClientDetail> myPeersList;
    private List<ClientDetail> activeClients;
    private NodeClient client;
    private MiniServer miniServer;

    public static String getMyIpAddress() {
        return myIpAddress;
    }

    public static void setMyIpAddress(String myIpAddress) {
        Node.myIpAddress = myIpAddress;
    }

    public static int getMyListeningPort() {
        return myListeningPort;
    }

    public static void setMyListeningPort(int myListeningPort) {
        Node.myListeningPort = myListeningPort;
    }

    public List<ClientDetail> getMyPeersList() {
        return myPeersList;
    }

    public void setMyPeersList(List<ClientDetail> myPeersList) {
        this.myPeersList = myPeersList;
    }

    public List<ClientDetail> getActiveClients() {
        return activeClients;
    }

    public void setActiveClients(List<ClientDetail> activeClients) {
        this.activeClients = activeClients;
    }

    public NodeClient getClient() {
        return client;
    }

    public void setClient(NodeClient client) {
        this.client = client;
    }

    public MiniServer getMiniServer() {
        return miniServer;
    }

    public void setMiniServer(MiniServer miniServer) {
        this.miniServer = miniServer;
    }

    Node(InetSocketAddress centralServerAddress, int port) throws Exception {
        //InetAddress localHost = InetAddress.getLocalHost();
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netint : Collections.list(nets))
        {
            Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
            for (InetAddress inetAddress : Collections.list(inetAddresses))
            {
                if (!(inetAddress.toString().contains("127.0.0.1")))
                {
                    myIpAddress = inetAddress.toString();
                }
            }
        }
        myListeningPort = port;
        if(register(centralServerAddress))
        {
            captureActiveClientsList(centralServerAddress);
            client = NodeClient.getInstance();
            miniServer=MiniServer.getInstance();
        }
        else
        {
            System.out.println("Server doesn't not Exist");
            System.exit(0);
        }




    }

    public boolean unRegister(InetSocketAddress centralServerAddress)
    {
        try
        {
            Socket socket = new Socket();
            socket.connect(centralServerAddress, 10000);
            CentralServerMessage message = new CentralServerMessage("unregister " + myIpAddress + " " + myListeningPort);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(message);

            boolean registered = isUnRegistered(socket);


            out.close();
            socket.close();
            return registered;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    private boolean isUnRegistered(Socket socket) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String messageStr;
        try {
            while ((messageStr = reader.readLine()) != null) {
                if(messageStr.equals("registrationCancel"))
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


    private boolean register(InetSocketAddress centralServerAddress)
    {
        try
        {
            Socket socket = new Socket();
            socket.connect(centralServerAddress, 10000);
            CentralServerMessage message = new CentralServerMessage("register " + myIpAddress + " " + myListeningPort);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(message);

            boolean registered = isRegistered(socket);


            out.close();
            socket.close();
            return registered;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return false;

    }

    private boolean isRegistered(Socket socket) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String messageStr;
        try {
            while ((messageStr = reader.readLine()) != null) {
                if(messageStr.equals("registrationDone"))
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

    private void captureActiveClientsList(InetSocketAddress centralServerAddress) {
        try {
            Socket socket = new Socket();
            socket.connect(centralServerAddress, 10000);
            CentralServerMessage message = new CentralServerMessage("getclientslist " + myIpAddress + " " + myListeningPort);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(message);


            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ClientDetail client;
            try {
                while ((client = (ClientDetail) ois.readObject()) != null) {
                    activeClients.add(client);
                }
                socket.close();
                ois.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {

        }

    }

    public void sendReply()
    {

        client.sendReply();


    }




    public static void main(String[] args) throws Exception{
        System.out.print("hello");
        Node nodeInstance = new Node(new InetSocketAddress("127.0.0.1",1000),345);
    }

}
