//imports for network communication
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class Server {
    final String LOCAL_HOST = "127.0.0.1";
    final int PORT = 5000;

    ServerSocket serverSocket;//server socket for connection
    //ArrayList<ClientHandler> connections = new ArrayList<ClientHandler>();
    int clientCounter = 0;
    ArrayList <ServerHandler> connections = new ArrayList<ServerHandler>();
    String player1Input;

    public static void main(String[] args) {
        Server server = new Server();
    }

    public Server() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Waiting for connections");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Server-Client connection made");
                clientCounter++;
                System.out.println("Client #"+clientCounter);
                ServerHandler serverHandler = new ServerHandler(clientSocket, clientCounter);
                connections.add(serverHandler);
                Thread thread = new Thread (serverHandler);
                thread.run();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // public void go() {

    // }

    class ServerHandler extends Thread{
        private Socket socket;
        private DataInputStream in;
        BufferedReader input;
        private DataOutputStream output;

        private int playerID;
        //create a socket with the local IP address (try-catch required) and wait for connection request

        public ServerHandler(Socket s, int id){
//            this.socket = s;
//            this.playerID = id;
            socket = s;
            playerID = id;

            try{
//                input = new DataInputStream(socket.getInputStream());
                in = new DataInputStream(socket.getInputStream());
                input = new BufferedReader(new InputStreamReader(in));
                output = new DataOutputStream(socket.getOutputStream());
                System.out.println("input and output stream connected");
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        public void run(){
            try{
                System.out.println("before write int");
                output.writeInt(playerID);
                output.flush();
                System.out.println("after flush");
                /* need to modify this while loop for chess */
                while (true){ //true
//                    if (playerID ==1){
//                        player1Input= input.readLine();
//                        System.out.println();
//                        connections.get(0).sendMove("a","b");
//                    }
//                    if (playerID==32323){ // if smth happens or put it as a while loop condition
//                        break;
//                    }
//58:37
                break;
                }
//                for (int i = 0; i<connections.size(); i++){
//                    connections.get(i).closeConnection();
//                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        public void sendMove(String startPos, String endPos){
            try{
                output.writeChars(startPos+" "+endPos);
                output.flush();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        public void closeConnection(){
            try{
                socket.close();
                System.out.println("Socket closed.");
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

}

//------------------------------------------------------------------------------
