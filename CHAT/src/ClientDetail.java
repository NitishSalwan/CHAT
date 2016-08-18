import java.io.Serializable;

/**
 * Created by Nitish on 8/17/2016.
 */
public class ClientDetail implements Serializable{

    private String ipAddress;
    private int listeningPort;

    ClientDetail(String ipAddress,int port)
    {
        setipAddress(ipAddress);
        setlisteningPort(port);
    }

    public String getipAddress() {
        return ipAddress;
    }

    public void setipAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getlisteningPort() {
        return listeningPort;
    }

    public void setlisteningPort(int listeningPort) {
        this.listeningPort = listeningPort;
    }
}
