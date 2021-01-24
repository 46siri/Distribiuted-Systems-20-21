/*package Servidor;

import java.util.ArrayList;
import java.util.HashMap;

public class Mapa {
    private int aresta;
    private Data data;
    private Data[][] infomapa;
    private HashMap<String, ArrayList<User>> historicoContactos;

    public Mapa(int aresta){
        this.aresta = aresta;
        this.data = new Data();
        this.infomapa = new Data[aresta][aresta];
        this.historicoContactos = new HashMap<>();
    }
    //utilizadores por localizacao
    public int utilizadoresLocalizacao(int x, int y){
        return infomapa[x][y].getNr_users();
    }
    //mover sitio
    public void move_to(String user,int x, int y){
        User u = new User();
        u.setNome(user);

        if(historicoContactos.get(u) == null){
            ArrayList<> temp = new ArrayList();
        }

        for(int i = 0 ; i<aresta;i++){
            for (int j = 0 ; j < aresta ; j++){
                if(infomapa[i][j].getUtilizadores().contains(u)){

                    ArrayList<> temp = new ArrayList();
                    temp = historicoContactos.get(u);
                    for(int k =0 ; k < infomapa[i][j].getUtilizadores().size();k++){


                    }
                }
            }
        }
    }

}
*/