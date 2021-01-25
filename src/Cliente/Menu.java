package Cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Menu {
    private BufferedReader in;
    private BufferedReader input;
    private PrintWriter replyer;
    private String myname;
    private String mypass;

    public Menu(BufferedReader in , PrintWriter replyer, BufferedReader input, String myname, String mypass){
        this.in=in;
        this.input = input;
        this.replyer = replyer;
        this.myname=myname;
        this.mypass=mypass;
    }

    public static boolean validXY(String x, String y) {
        try {
            if (Integer.parseInt(x) <= 20 && Integer.parseInt(x) >= 0 && Integer.parseInt(y) <= 20 && Integer.parseInt(y) >= 0)
                return true;
        } catch (Exception e) {
            return false;
        }
        return false;
    }


    public void menu(BufferedReader in, PrintWriter requester, BufferedReader input) throws IOException {

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
                    while (!validXY(x, y)) {
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
                    while (!validXY(x, y)) {
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
                    System.out.println("Quer confirmar que está infetado?\n Pressione 'Y' se sim");
                    String confirmation = input.readLine();
                    if (confirmation.equals("y") || confirmation.equals("Y")) {
                        args = "infetado" + "," + myname + "," + mypass;
                        requester.println(args);
                        //receber possível notificação de alteração
                        reply = in.readLine();
                        System.out.println(reply);
                        aux = false;
                    }
                    break;
                case "4":
                    x = y = "-1";
                    while (!validXY(x, y)) {
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
}
