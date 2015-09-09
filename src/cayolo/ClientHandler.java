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
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author pagh
 */
public class ClientHandler extends Thread
{

    private Socket s;
    private Server ser;
    private String nameInput;
    Scanner checkUserName;
    BufferedReader in;
    PrintWriter out;
    boolean pendingUserName;
    private String inMsg;
    private Scanner scan;
    private Scanner msgPPL;
    

    public ClientHandler(Socket s, Server ser)
    {
        this.s = s;
        this.ser = ser;
        pendingUserName = true;
    }

    @Override
    public void run()
    {
        try
        {
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(), true);

            while (pendingUserName)
            {
//                out.println("Please enter a username like this: USER#'your_name'");
                nameInput = in.readLine();
                checkUserName = new Scanner(nameInput);
                checkUserName.useDelimiter("#");
                while (checkUserName.hasNext())
                {
                    String a = checkUserName.next();
                    String userName = checkUserName.next();

                    if (a.equals("USER"))
                    {
                        ser.addUser(userName, this);
                        nameInput = userName;
                        pendingUserName = false;
                    }
                }

            }

        } catch (IOException ex)
        {
            System.err.println("Der er knas i addUser funktionen!");
        }

        ser.userList();
        sendMessage();
    }

    public void sendUserList(String list)
    {
        out.println(list);
    }



    public synchronized void sendMessage()
    {
        try
        {
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(), true);
            String first = "";
            String middle = "";
            String last = "";
            inMsg = in.readLine();
            scan = new Scanner(inMsg);
            scan.useDelimiter("#");
            String msgName = "";
            while (scan.hasNext())
            {
                first = scan.next();
//                middle = scan.next();
//                last = scan.next();

                switch (first)
                {
                    case "STOP":
                        stopClient();
                        break;
                    case "MSG":
                        
                        break;
                }
            }
        } catch (IOException ex)
        {
            System.err.println("Knas i sendMsg");
        }

    }

    public void message(String middle, String last)
    {
        
    }
    public void stopClient() 
    {
        try{
        ser.stopUser(nameInput, this);
        ser.userList();
        System.out.println("jeg er ikke lukket endnu");
        s.close();
       
        System.out.println("Jeg er lukket" +  s.isClosed());
        }
        catch(IOException ex)
        {
            System.err.println("JEG HAR FANGET EX I STOP CLIENT");
        }
    }

}
