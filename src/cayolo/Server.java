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
        for(String user : users.keySet())
        {
            onlineMsg += user + ",";
            
        }
        for(ClientHandler ch : users.values())
        {
            ch.sendUserList(onlineMsg);
        }
//        out.println("USERLIST#" + clients.toString());
    }



}
