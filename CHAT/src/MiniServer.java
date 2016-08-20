import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Nitish on 8/17/2016.
 */
public class MiniServer {

        private static MiniServer miniServerInstance = null;
        private Node node;


        private MiniServer()
        {

        }

        public synchronized static MiniServer getInstance()
        {
            if(miniServerInstance == null)
            {
                miniServerInstance = new MiniServer();
            }

            return miniServerInstance;
        }


        void acceptConnections()
        {
            try {
                ServerSocket serverSocket = new ServerSocket();

                //BufferedReader reader = new BufferedReader((new InputStreamReader(socket.getInputStream())));
                while(true)
                {
                    Socket socket = serverSocket.accept();
                    new Thread(new RequestHandler(socket,miniServerInstance)).start();
                }
            }
            catch(Exception e)
            {

            }
        }

       class RequestHandler implements Runnable
       {
           Socket socket;
           MiniServer miniServerInstance=null;
           BufferedReader reader;


           RequestHandler(Socket socket,MiniServer miniServerInstance) throws Exception {
               this.socket=socket;
               this.miniServerInstance = miniServerInstance;
               reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

           }

           public void run()
           {
               String tempMessage;
               try {
                   while((tempMessage = reader.readLine())!=null)
                   {
                       processMessage(tempMessage,socket);
                   }
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }

           private void processMessage(String message,Socket socket) throws Exception {

               String[] tempMessageSplit = message.split("+s//");
               if(tempMessageSplit[0].equals("wanttochat"))
               {
                   PrintWriter writer =new PrintWriter(socket.getOutputStream());
                   writer.println("sure");
                   writer.flush();
                   writer.close();
               }
           }

       }



}
