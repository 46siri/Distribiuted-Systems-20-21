package Cliente;

import java.io.BufferedReader;
import java.net.Socket;

public class Notificao implements Runnable {
    BufferedReader conn;
    Socket socket;
    public Notificao(BufferedReader c, Socket s) {
        conn = c;
        socket = s;
    }

    public void run() {
        try {
            String r;
            while(socket.isBound()) {
                System.out.println("qqqqqqqqqq");
                r = conn.readLine();
                System.out.println("wwwwwwwwwwwww");


                while (!r.isEmpty()) {
                    System.out.println(r);
                    r = conn.readLine();
                }
            }
        }
        catch (Exception e){}
    }
}
