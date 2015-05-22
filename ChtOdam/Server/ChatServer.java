import java.io.*;
import java.net.*; 
import java.util.*;


public class ChatServer {

    private static final String USAGE = "Usage: java ChatServer";

    //Port numarası tanımlanır.
    private static final int PORT_NUMBER = 8008;

    //Client'lara yollanacak mesajların listesi
    private List<PrintWriter> clients;

    //Server olusturma
    public ChatServer() {
        clients = new LinkedList<PrintWriter>();
    }

    //Serverı baslatma
    public void start() {
        System.out.println("ChatOdam basladı "
                           + PORT_NUMBER + "!"); 
        try {
            ServerSocket s = new ServerSocket(PORT_NUMBER); 
            for (;;) {
                Socket incoming = s.accept(); 
                new ClientHandler(incoming).start(); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("ChatOdam durdu"); 
    }

    //Yeni bir client ekleme
    private void addClient(PrintWriter out) {
        synchronized(clients) {
            clients.add(out);
        }
    }

    //Client silme
    private void removeClient(PrintWriter out) {
        synchronized(clients) {
            clients.remove(out);
        }
    }

    //Tüm client'lara broadcast olarak gonderme
    private void broadcast(String msg) {
        for (PrintWriter out: clients) {
            out.println(msg);
            out.flush();
        }
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            System.out.println(USAGE);
            System.exit(-1);
        }
        new ChatServer().start();
    }

    //Gelen mesaji alip client'lara broadcast yollama
    private class ClientHandler extends Thread {

        //Client mesajlarini okuma
        private Socket incoming; 

        
        public ClientHandler(Socket incoming) {
            this.incoming = incoming;
        }

        //Mesaji alip broadcast olarak client'lara gonderme
        public void run() {
            PrintWriter out = null;
            try {
                out = new PrintWriter(
                        new OutputStreamWriter(incoming.getOutputStream()));
                
                //Yeni gelen client'a mesaj gonderme
                ChatServer.this.addClient(out);

                out.print("ChatOdamaHosgeldiniz");
                out.println("byeilecikisyapabilirsiniz"); 
                out.flush();

                BufferedReader in 
                    = new BufferedReader(
                        new InputStreamReader(incoming.getInputStream())); 
                for (;;) {
                    String msg = in.readLine(); 
                    if (msg == null) {
                        break; 
                    } else {
                        if (msg.trim().equals("BYE")) 
                            break; 
                        System.out.println("Received: " + msg);
                        // broadcast mesaj alma
                        ChatServer.this.broadcast(msg);
                    }
                }
                incoming.close(); 
                ChatServer.this.removeClient(out);
            } catch (Exception e) {
                if (out != null) {
                    ChatServer.this.removeClient(out);
                }
                e.printStackTrace(); 
            }
        }
    }
}
