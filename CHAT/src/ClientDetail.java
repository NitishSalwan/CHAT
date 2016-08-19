import java.io.Serializable;

/**
 * Created by Nitish on 8/17/2016.
 */
public class ClientDetail implements Serializable{

    @Override
    public int hashCode() {
        return (getipAddress().hashCode() + getlisteningPort());
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
