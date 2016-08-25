import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Nitish on 8/17/2016.
 */
public class MiniServer implements Runnable
{

        private static MiniServer miniServerInstance = null;
        private Node node;
        private boolean isServerUp;
        private ServerSocket serverSocket;

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public boolean isServerUp() {
        return isServerUp;
    }

    public void setServerUp(boolean serverUp) {
        isServerUp = serverUp;
    }

    private MiniServer(Node node)
        {
            this.node=node;
            try{
                serverSocket = new ServerSocket();
                isServerUp = true;
            }
            catch(Exception e)
            {
                isServerUp = false;
            }
        }

    public synchronized static MiniServer getInstance()
        {
            if(miniServerInstance == null)
            {
                miniServerInstance = new MiniServer();
            }
            return miniServerInstance;
        }


    public void run ()
        {
                while(true)
                {

                    Socket socket = null;
                    try {
                        socket = serverSocket.accept();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    BufferedReader reader = null;
                    try
                    {
                        reader = new BufferedReader((new InputStreamReader(socket.getInputStream())));
                    String tempMessage;
                    while((tempMessage = reader.readLine())!=null)
                    {

                        String[] tempMessageSplit = tempMessage.split("+s//");
                        if(tempMessageSplit[0].equals("wanttochat"))
                        {

                            if(checkResources())
                            {
                                sendAvailability("sure",socket);
                                new Thread(new ChatHandler(socket)).start();

                            }
                            else
                            {
                                sendAvailability("notavailable",socket);
                            }

                        }
                    }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }




        }


    private void sendAvailability(String message,Socket socket) throws Exception
    {

            PrintWriter writer =new PrintWriter(socket.getOutputStream());
            writer.println("sure");
            writer.flush();
            writer.close();

    }

        private boolean checkResources()
        {

        }





}
