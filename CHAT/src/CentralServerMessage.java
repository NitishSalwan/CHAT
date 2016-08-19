/**
 * Created by Nitish on 8/17/2016.
 */
public class CentralServerMessage {

    private String type;
    private ClientDetail entityDetail;

    @Override
    public int hashCode() {
        return entityDetail.hashCode() + type.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if((obj == null) || (obj.getClass() != this.getClass()))
            return false;

        CentralServerMessage test = (CentralServerMessage)obj;
        return type.equals(test.type) && entityDetail.equals(test.entityDetail);
    }

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
