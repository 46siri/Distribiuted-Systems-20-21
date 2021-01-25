/*package Cliente;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {

        Socket sc = new Socket("127.0.0.1", 65000);

        Logs l = new Logs();
        Thread t = new Thread(new Menu(sc,l));
        Thread t1 = new Thread(new User(sc,l));
        t.start();
        t1.start();

    }
}*/

package Cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Classe responsável por ligar um cliente ao servidor.
 */

public class Client {
    private static String myname;
    private static String mypass;

    public static boolean validXY(String x, String y) {
        try {
            if (Integer.parseInt(x) <= 20 && Integer.parseInt(x) >= 0 && Integer.parseInt(y) <= 20 && Integer.parseInt(y) >= 0)
                return false;
        } catch (Exception e) {
            return true;
        }
        return true;
    }

    public static void main(String[] dab) throws IOException {
        Socket replyer = new Socket("127.0.0.1", 65000);

        try {
            PrintWriter out = new PrintWriter(replyer.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(replyer.getInputStream()));
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

            boolean aux = true;
            while (aux) {
                System.out.println("Hello there");
                System.out.println("1-login 2-registar 3-quit");
                String option = input.readLine();
                String username;
                String password;
                switch (option) {
                    case "1" -> {
                        System.out.println("Inserir :");
                        username = input.readLine();
                        myname = username;
                        System.out.println("Inserir password:");
                        password = input.readLine();
                        mypass = password;
                        int sucesso = login(username, password, in, out);
                        if (sucesso == 1) {
                            Socket s = new Socket("127.0.0.1", 65000);
                            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                            menu(br, s, in, out, input);
                        } else if (sucesso == 2) {
                            menuAdmin(in, out, input);
                        }
                    }
                    case "2" -> {
                        System.out.println("Inserir :");
                        username = input.readLine();
                        System.out.println("Inserir password:");
                        password = input.readLine();
                        registar(username, password, out, in);
                    }
                    case "3" -> aux = false;
                }
            }

            input.close();
            in.close();
            out.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        replyer.close();
    }


    /**
     * Método para fazer um novo registo.
     * @param username username do novo utilizador.
     * @param password password do novo utilizador.
     * @param requester pedido feito do cliente.
     * @param in resposta enviada do servidor.
     * @throws IOException
     */
    //Nota um request tem de ser seguido de um reply
    private static void registar(String username, String password, PrintWriter requester, BufferedReader in) throws IOException {

            String args = "registar," + username + "," + password;
            requester.println(args);
            String reply = in.readLine();
            if (reply.equals("ok")) {
                System.out.println("Foi registado com sucesso");
            } else
                System.out.println(reply);

    }
    /**
     * Método para fazer o login.
     * @param username username do utilizador previamente registado.
     * @param password password do utilizador previamente registado.
     * @param requester pedido feito do cliente.
     * @param in resposta enviada do servidor.
     * @throws IOException
     */

    private static int login(String username, String password, BufferedReader in, PrintWriter requester) throws IOException {
        String args = "login," + username + "," + password;
        requester.println(args);
        String reply = in.readLine();

        if (reply.equals("ok")) {
            System.out.println("Login feito com sucesso");
            return 1;
        }else if(reply.equals("admin")){
            System.out.println("Bem vindo admin");
            return 2;
        }else {
            System.out.println(reply);
            return 0;
        }
    }

    /**
     * Método para ler a decisão do cliente em cada opção do menu.
     * @param in resposta enviada do Servidor.
     * @param requester pedido feito do Cliente.
     * @param input input.
     * @throws IOException
     */

    private static void menu(BufferedReader br, Socket s , BufferedReader in, PrintWriter requester, BufferedReader input) throws IOException {
        //BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        Notificao notificao = new Notificao(br,s);
        Thread t = new Thread(notificao);
        t.start();
        boolean aux = true;
        String x, y;
        String args;
        String reply;
        while (aux) {
            System.out.println("1-Change my Localization\n2-Where To Go safely?\n3-I am Infected\n4-Add My Localization\n5-Change My Password\n0-Close");
            String option = input.readLine();
            switch (option) {
                case "0":
                    aux = false;
                    args = "quit," + myname;
                    requester.println(args);
                    reply = in.readLine();
                    System.out.println(reply);
                    break;
                case "1":
                    x = y = "-1";
                    while (validXY(x, y)) {
                        System.out.println("Escreva as suas coordenadas. apenas serão consideradas validas se o valor se encontrar entre 0 e 20.");
                        System.out.println("Inserir coordenada x:");
                        x = input.readLine();
                        System.out.println("Inserir coordenada y:");
                        y = input.readLine();
                    }
                    args = "localizacao" + "," + myname + "," + x + "," + y;
                    requester.println(args);
                    reply = in.readLine();
                    if (reply.equals("ok")) {
                        System.out.println("A sua posição foi atualizada");
                    } else if (reply.equals("mesmaPosicao")) {
                        System.out.println("A nova posicao pedida é já onde se encontra!");
                    }
                    else
                        System.out.println("Erro Sistema??");
                    break;
                case "2":
                    x = y = "-1";
                    while (validXY(x, y)) {
                        System.out.println("Escreva as suas coordenadas. apenas serão consideradas validas se o valor se encontrar entre 0 e 20.");
                        System.out.println("Inserir coordenada x:");
                        x = input.readLine();
                        System.out.println("Inserir coordenada y:");
                        y = input.readLine();
                    }
                    args = "infoLocalizacao" + "," + x + "," + y;
                    requester.println(args);
                    reply = in.readLine();
                    System.out.println("Existem " + reply + " utilizadores nessa localização");
                    break;

                case "3":
                    System.out.println("Quer confirmar que está infetado?\n Pressione 'S' se sim");
                    String confirmation = input.readLine();
                    if (confirmation.equals("s") || confirmation.equals("S")) {
                        args = "infetado" + "," + myname + "," + mypass;
                        requester.println(args);
                        reply = in.readLine();
                        System.out.println(reply);
                        aux = false;
                    }
                    break;
                case "4":
                    x = y = "-1";
                    while (validXY(x, y)) {
                        System.out.println("Escreva as suas coordenadas. apenas serão consideradas validas se o valor se encontrar entre 0 e 20.");
                        System.out.println("Inserir coordenada x:");
                        x = input.readLine();
                        System.out.println("Inserir coordenada y:");
                        y = input.readLine();
                    }
                    args = "minhaLocalizacao" + "," + myname + "," + x + "," + y;
                    requester.println(args);
                    reply = in.readLine();
                    System.out.println(reply);
                    break;

                case "5":
                    System.out.println("Digite a sua nova password:");
                    String novaPass = input.readLine();
                    args = "mudaPass" + "," + myname + "," + novaPass;
                    requester.println(args);
                    reply = in.readLine();
                    System.out.println(reply);
                    break;

                default:
                    break;

            }
        }
    }
    public static void menuAdmin(BufferedReader in, PrintWriter pw, BufferedReader input) throws IOException {
        boolean aux = true;
        while(aux){
            System.out.println("1-Descarregar Mapa\n0-Quit");
            String option = input.readLine();
            switch (option){
                case "0":
                    aux = false;
                    String args = "quit," + myname;
                    pw.println(args);
                    String reply = in.readLine();
                    System.out.println(reply);
                    break;
                case "1":
                    System.out.println("Crie um ficheiro de destino do mapa e digite o seu input!");
                    String path = input.readLine();
                    pw.println("mapa" + "," + path);
                    if(in.readLine().equals("1")){
                        System.out.println("Mapa descarregado verifique o seu ficheiro");
                    }else{
                        System.out.println("Um erro aconteceu");
                    }
                    break;
                default:
                    break;

            }
        }
    }

}
