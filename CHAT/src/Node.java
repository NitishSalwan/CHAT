import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;


/**
 * Created by Nitish on 8/17/2016.
 */
public class Node {

    private String myIpAddress;
    private int myListeningPort;
    private List<ClientDetail> myPeersList;
    private List<ClientDetail> activeClients;

    Node(InetSocketAddress centralServerAddress,int port) throws Exception {
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
        register(centralServerAddress);



    }

    private void register(InetSocketAddress centralServerAddress)
    {
        try
        {
            Socket socket = new Socket();
            socket.connect(centralServerAddress, 10000);
            CentralServerMessage message = new CentralServerMessage("register " + myIpAddress + " " + myListeningPort);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(message);

            captureActiveClientsList();

            out.close();
            socket.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


    }

    private void captureActiveClientsList()
    {
        try {
            ServerSocket serverSocket = new ServerSocket(myListeningPort);
            Socket socket = serverSocket.accept();
            ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());
            List<ClientDetail> tempList;
            try
            {
                while((tempList=(List<ClientDetail>)ois.readObject())!=null)
                {
                    activeClients=tempList;
                }
                socket.close();
                ois.close();
                serverSocket.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
        catch(Exception e)
        {

        }
    }


    public static void main(String[] args) {
        System.out.print("hello");
    }

}
