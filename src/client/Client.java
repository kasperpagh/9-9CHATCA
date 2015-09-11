/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pagh
 */
public class Client extends Thread
{

    Socket socket;
    private int port;
    private InetAddress serverAddress;
    private Scanner input;
    private PrintWriter output;
    private boolean running = true;
    private String name;

    public void connect(String address, int port) throws IOException
    {
        this.port = port;

        serverAddress = InetAddress.getByName(address);
        //HUSK AT ÆNDRE NEDENSTÅENDE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        socket = new Socket(address, port);
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);  //Set to true, to get auto flush behaviour
        if (running)
        {
            start();
        }

    }
    
    public void recieve()
    {
        
    }
    
    

    public void send(String msg)
    {
        output.println(msg);
    }

    public void killMePls()
    {
        try
        {
            running = false;
            socket.close();
//            this.interrupt();
        } catch (IOException ex)
        {
            System.err.println("The reason you see this, is becourse your socket likes to throw IOexceptions better than it likes to close();");
        }
    }
    
    public String getUserName()
    {
        return name;
    }
    
    public void setUserName(String usrName)
    {
        name = usrName;
    }
}
