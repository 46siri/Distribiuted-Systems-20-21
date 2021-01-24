package Servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) throws IOException {
        Localizacao local = new Localizacao(21);
        System.out.println("Covid-19 ALARM");
        ServerSocket socket = new ServerSocket(65000);
        while (true) {
            Socket conn = socket.accept();
            new Thread(new ControladorUser(conn, local)).start();
            System.out.println("User Connected");
        }
    }


    public static class ControladorUser implements Runnable {

        private Socket replyer;
        private Localizacao local;


        public ControladorUser(Socket c, Localizacao local) {
            replyer = c;
            this.local = local;
        }

        public void run() {

            try {
                PrintWriter out = new PrintWriter(replyer.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(replyer.getInputStream()));
                //ciclo while = com opções login e regist. se login bem suc aux = false
                //se login bem guardar user na string user
                boolean aux = true;
                while (aux) {
                    String user = "";
                    String pass = "";
                    String option = in.readLine();
                    String conteudo[] = option.split(",");
                    System.out.println(option + "-------");
                    String ans = "";
                    switch (conteudo[0]) {
                        case "registar":
                            user = conteudo[1];
                            pass = conteudo[2];
                            ans = local.registar(user, pass);
                            System.out.println(ans);
                            out.println(ans);
                            break;
                        case "login":
                            user = conteudo[1];
                            pass = conteudo[2];
                            ans = local.login(user, pass);
                            if (ans.equals("ok")) {
                                aux = false;
                                out.println(ans);
                            } else {
                                out.println(ans);
                            }
                            break;
                    }
                }
                    aux = true;
                String ans = "";
                    while (aux) {
                        String option = in.readLine();
                        System.out.println(option);

                        String[] arrOfStr = option.split(",");
                        String request = arrOfStr[0];
                        switch (request) {
                            case "localizacao":
                                //ans = local.mudarLocalizacao(arrOfStr[1], Integer.parseInt(arrOfStr[2]), Integer.parseInt(arrOfStr[3]));
                                System.out.println(arrOfStr[1] + "," + arrOfStr[2] + "," + arrOfStr[3]);
                                ans = local.moveTo(arrOfStr[1], Integer.parseInt(arrOfStr[2]), Integer.parseInt(arrOfStr[3]));
                                System.out.println(ans);
                                out.println("ok");
                                break;
                            case "minhaLocalizacao":
                                local.moveTo(arrOfStr[1], Integer.parseInt(arrOfStr[2]), Integer.parseInt(arrOfStr[3]));
                                //local.adicionaNovaLocalizacao(arrOfStr[1],Integer.parseInt(arrOfStr[2]),Integer.parseInt(arrOfStr[3]));
                                out.println("Localização adiconada");
                                break;
                            case "infoLocalizacao":
                                int i = local.getNumUsersLocalizacao(Integer.parseInt(arrOfStr[1]),Integer.parseInt(arrOfStr[2]));
                                out.println(i);
                                break;
                            case "infetado":
                                local.addInfetado(arrOfStr[1]);
                                local.changePassword(arrOfStr[1],"userInfected##");
                                out.println("Faça quarentena e um desejo de melhoras");
                                break;
                            case "mudaPass":
                                local.changePassword(arrOfStr[1],arrOfStr[2]);
                                out.println("ok");
                                break;
                            case "quit":
                                out.println("good bye!");
                                aux = false;
                                break;
                        }
                    }
                } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
            }
    }


