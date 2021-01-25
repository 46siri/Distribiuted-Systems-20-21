package Servidor;

import java.io.PrintWriter;
import java.net.Socket;

public class Notificador {
    private Socket socket;
    private PrintWriter out;

    public Notificador(Socket conn, PrintWriter o){

        socket=conn;
        out=o;
    }






    public Socket getSocket(){return socket;}

    public PrintWriter getPrintwriter(){return out;}
}
