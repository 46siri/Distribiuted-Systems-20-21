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
                r = conn.readLine();

                while (!r.isEmpty()) {

                    System.out.println(r);
                    r = conn.readLine();
                }
            }
        }
        catch (Exception e){}
    }
}
