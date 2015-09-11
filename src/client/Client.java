package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pagh
 */
public class Client extends Observable implements Runnable
{

    Socket socket;
    private int port;
    private InetAddress serverAddress;
    private Scanner scan;
    private BufferedReader input;
    private PrintWriter output;
    private boolean running = true;
    private String name;
    private ArrayList list;
    private List<Observer> obsList = new ArrayList();
    private String message;
    private Client client = this;

    public void connect(String address, int port) throws IOException
    {
        this.port = port;

        serverAddress = InetAddress.getByName(address);
        //HUSK AT ÆNDRE NEDENSTÅENDE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        socket = new Socket(address, port);
        scan = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);  //Set to true, to get auto flush behaviour
        run();
    }

    @Override
    public void run()
    {
        Thread t = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    while (running)
                    {

                        message = scan.nextLine();

                        System.out.println("ER DER NOGET I NEXT I CLIENT ");
                        for (Observer observer : obsList)
                        {
                            observer.update(client, message);
                        }
                        setChanged();
                        notifyObservers(message);

                    }

                } catch (NoSuchElementException e)
                {
                    System.err.println("User disconnected" + "(IP:" + serverAddress + ")");
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, e);
                }
            }

        });

        t.start();
    }

    public void registerObserver(Observer o)
    {
        obsList.add(o);
    }

    public ArrayList recieve()
    {
        try
        {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String msg = input.readLine();
            scan = new Scanner(msg);
            String a;

            if (scan.hasNext())
            {
                scan = new Scanner(msg);
                scan.useDelimiter("#");

                if (scan.next().equals("USERLIST"))
                {
                    list = new ArrayList();
                    scan.useDelimiter(",");
                    while (scan.hasNext())
                    {
                        a = scan.next();
                        list.add(a);
                    }
                    return list;
                } else
                {
                    while (scan.hasNext())
                    {
                        a = scan.next();
                        list.add(scan.next());
                        list.add(scan.next());
                        return list;
                    }
                }

            }
        } catch (IOException ex)
        {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
