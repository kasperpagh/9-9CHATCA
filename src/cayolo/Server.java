/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cayolo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pagh
 */
public class Server extends Thread
{

    private boolean running = true;
    private ArrayList clients;
    private HashMap<String, ClientHandler> users;
//    private static final Properties properties = utils.Utils.initProperties("server.properties");
    Scanner scan;
    String inMsg;
    BufferedReader in;
    PrintWriter out;
    Socket s;
    String userName;
    Scanner checkUserName;
    boolean pendingUserName;
    Scanner msgPPL;

    public Server()
    {
        this.clients = new ArrayList();
        this.users = new HashMap();
        this.pendingUserName = true;

    }

    public static void main(String[] args)
    {
//        int port = Integer.parseInt(properties.getProperty("port"));
//        String ip = properties.getProperty("server-Ip");
//        String logFile = properties.getProperty("logFile");
//        Logger.getLogger(Server.class.getName()).log(Level.INFO, "Sever started");
        new Server().connect();

    }

    public void connect()
    {

        String ip = "localhost";
        int port = 4321;
        try
        {
            ServerSocket ss = new ServerSocket();
            ss.bind(new InetSocketAddress(ip, port));
            while (running)
            {
                //Here we make a uniqe user in the form of a clientHandler and adds it to an arraylist
                s = ss.accept();
//                Logger.getLogger(Server.class.getName()).log(Level.INFO, "forbundet til klient");
                ClientHandler ch = new ClientHandler(s, this);

//                out.println("Connected!");
//                clients.add(ch);
//                out.println("Der er pt: " + clients.size() + " klient(er) tilsluttet!");
                new Thread(ch).start();
//                addUser(ch);

            }
        } catch (IOException e)
        {
            System.err.println("fanget i catch");

        }
    }

    public void addUser(String username, ClientHandler ch)
    {
        users.put(username, ch);
        clients.add(username);
    }

    public void stopUser(String userName, ClientHandler ch)
    {
        System.out.println("I SERVER STOP");
        System.out.println(users.toString());
        users.remove(userName, ch);
        
        System.out.println(users.toString());

    }

    public synchronized void userList()
    {
        String onlineMsg = "USERLIST#";

        for (String user : users.keySet())
        {
            onlineMsg += user + ",";

        }
        for (ClientHandler ch : users.values())
        {
            ch.sendUserList(onlineMsg);
        }
//        out.println("USERLIST#" + clients.toString());
    }

    public void sendMessage(String message, ArrayList<String> recipients)
    {
        System.out.println("bubberllama: " + recipients.toString());

        if (recipients.size() == 1 && !recipients.get(0).equals("*"))
        {
            ClientHandler ch = users.get(recipients.get(0));
            ch.message(message);
        } else if (recipients.get(0).equals("*"))
        {
            System.out.println("her er field 0 i array: " + recipients.get(0));
            for (ClientHandler ch : users.values())
            {
                ch.message(message);
            }
        } else
        {
            for (String recipient : recipients)
            {
                ClientHandler ch = users.get(recipient);
                ch.message(message);
            }
        }
    }
}
