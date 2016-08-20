import java.io.*;
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
        if(register(centralServerAddress))
        {
            captureActiveClientsList(centralServerAddress));
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

    private void captureActiveClientsList(InetSocketAddress centralServerAddress)
    {
        try {
            Socket socket = new Socket();
            socket.connect(centralServerAddress, 10000);
            CentralServerMessage message = new CentralServerMessage("getclientslist " + myIpAddress + " " + myListeningPort);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(message);


            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ClientDetail client;
            try
            {
                while((client=(ClientDetail)ois.readObject())!=null)
                {
                    activeClients.add(client);
                }
                socket.close();
                ois.close();
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
