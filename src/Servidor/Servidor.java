package Servidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Servidor responsável por responder aos pedidos do Cliente.
 */

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
    /**
     * Classe responsável por intrepertar os pedidos feitos por um cliente ao servidor.
     */

    public static class ControladorUser implements Runnable {

        private Socket replyer;
        private Localizacao local;
       // private ServerSocket ss;


        public ControladorUser(Socket c, Localizacao local) {
            replyer = c;
            this.local = local;
            //this.ss=ss;
        }
        /**
         * Método run que é executado pela thread.
         */
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
                    System.out.println(option);
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
                            System.out.println(user);
                            System.out.println(pass);
                            ans = local.login(user, pass );
                            if (ans.equals("ok")) {
                                //Socket not = ss.accept();
                                local.getNotifcacoes().put(user,out);
                                aux = false;
                                out.println(ans);
                            } else if(ans.equals("admin")){
                                out.println(ans);
                                aux = false;
                            }
                            else {
                                out.println(ans);
                            }
                            break;
                    }
                }
                    aux = true;
                String ans = "";
                    while (aux) {
                        System.out.println("olaaaaa");
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
                                local.adicionaHistoricoUsers(arrOfStr[1], Integer.parseInt(arrOfStr[2]), Integer.parseInt(arrOfStr[3]));
                                out.println("ok");
                                break;
                            case "minhaLocalizacao":
                                local.moveTo(arrOfStr[1], Integer.parseInt(arrOfStr[2]), Integer.parseInt(arrOfStr[3]));
                                local.adicionaHistoricoUsers(arrOfStr[1], Integer.parseInt(arrOfStr[2]), Integer.parseInt(arrOfStr[3]));
                                //local.adicionaNovaLocalizacao(arrOfStr[1],Integer.parseInt(arrOfStr[2]),Integer.parseInt(arrOfStr[3]));
                                out.println("Localização adiconada");
                                break;
                            case "infoLocalizacao":
                                int i = local.getNumUsersLocalizacao(Integer.parseInt(arrOfStr[1]),Integer.parseInt(arrOfStr[2]));
                                out.println(i);
                                break;
                            case "infetado":
                                List<PrintWriter> notifica = new ArrayList<>();
                                notifica = local.notifica(arrOfStr[1]);
                                if(notifica!=null) {
                                    for (int iterator = 0; iterator < notifica.size(); iterator++) {
                                        System.out.println("olaaaaaaaaa");
                                       // PrintWriter pw = new PrintWriter(notifica.get(iterator).getOutputStream(), true);
                                        System.out.println("tttttttttttttttt");
                                        notifica.get(iterator).println("Uma pessoa que você cruzou ficou infetado, faça isolamento!");
                                    }
                                }
                                local.addInfetado(arrOfStr[1]);
                                local.changePassword(arrOfStr[1],"userInfected##");
                                local.adicionaHistoricoInfecoes(arrOfStr[1]);
                                out.println("Faça quarentena e um desejo de melhoras");
                                break;
                            case "mudaPass":
                                local.changePassword(arrOfStr[1],arrOfStr[2]);
                                out.println("ok");
                                break;
                            case "mapa":
                                System.out.println("coise");
                                int check = local.escreveMapa(arrOfStr[1]);
                                out.println(check);
                            case "quit":
                                out.println("good bye!");
                                if(!arrOfStr[1].equals("admin"))
                                local.getNotifcacoes().remove(arrOfStr[1]);
                                aux = false;
                                break;
                        }
                    }
                    out.close();
                    in.close();
                } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
            }
    }


