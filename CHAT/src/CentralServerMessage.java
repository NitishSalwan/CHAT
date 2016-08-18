/**
 * Created by Nitish on 8/17/2016.
 */
public class CentralServerMessage {

    private String type;
    private ClientDetail entityDetail;

    CentralServerMessage(String message)
    {
        String[] splitMessage=message.split("+s//");
        setType(splitMessage[0]);
        entityDetail.setipAddress(splitMessage[1]);
        entityDetail.setlisteningPort(Integer.parseInt(splitMessage[2]));
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setEntityIpAddress(String ipAddress) {
        entityDetail.setipAddress(ipAddress);
    }

    public ClientDetail getEntityDetail(){
        return entityDetail;
    }

    public void setEntityListeningPort(int port){
        entityDetail.setlisteningPort(port);
    }

}
