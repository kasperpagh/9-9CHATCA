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
import shared.ProtocolStrings;

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
    private List<Observer> observersList = new ArrayList();
    private Client ec = this;
    String name;

    public void connect(String address, int port) throws UnknownHostException, IOException
    {
        this.port = port;
        serverAddress = InetAddress.getByName(address);
        socket = new Socket(serverAddress, port);
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);  //Set to true, to get auto flush behaviour
        run();
    }

    public void registerObserver(Observer o)
    {
        observersList.add(o);
    }

    public void setUserName(String usrName)
    {
        name = usrName;
    }

    public String getUserName()
    {
        return name;
    }

    public void send(String msg)
    {
        output.println(msg);
    }

    public void stopClient() throws IOException
    {
        output.println(ProtocolStrings.STOP);
    }

}
