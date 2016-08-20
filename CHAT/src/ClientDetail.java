import java.io.Serializable;

/**
 * Created by Nitish on 8/17/2016.
 */
public class ClientDetail implements Serializable{

    @Override
    public int hashCode() {
        return (getIpAddress().hashCode() + getListeningPort());
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if((obj == null) || (obj.getClass() != this.getClass()))
            return false;

        ClientDetail test = (ClientDetail)obj;
        return ipAddress.equals(test.ipAddress) && listeningPort==test.listeningPort;
    }

    private String ipAddress;
    private int listeningPort;

    ClientDetail(String ipAddress,int port)
    {
        setIpAddress(ipAddress);
        setListeningPort(port);
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getListeningPort() {
        return listeningPort;
    }

    public void setListeningPort(int listeningPort) {
        this.listeningPort = listeningPort;
    }
}
