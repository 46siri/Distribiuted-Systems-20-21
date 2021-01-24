package Servidor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Localizacao {
    private final int aresta;
    private List<String> [][] mapa;
    private Map<String, ArrayList<String>> contactos;
    private int numUtilizadores;
    private int numInfetados;
    private Map<String,String> userDistrict;
    private Map<String,String> userPassword;
    private RWLock lock ;

    public Localizacao(int aresta){

        this.aresta = aresta;
        this.mapa = new ArrayList [aresta][aresta];
        for(int i = 0; i<aresta;i++){
            for(int j = 0 ; j < aresta; j++){
               this.mapa[i][j] = new ArrayList<>();
            }
        }
        this.contactos = new HashMap();
        userDistrict = new HashMap<>();
        userPassword = new HashMap<>();
        this.lock = new RWLock();
    }


    public int getAresta(){
        return  this.aresta;
    }

    public List<String>[][] getMapa() {
        return mapa;
    }
    public int getNumUtilizadores() {
        try{
            lock.readLock();
        return this.numUtilizadores;
        }finally {
            lock.readUnlock();
        }

    }

    public void entradaUtilizador() {
        ++this.numUtilizadores;
    }

    public int getNumInfetados() {
        return this.numInfetados;
    }
    public int getNumUsersLocalizacao(int x, int y) {
        try {
            return this.mapa[x][y].size();
        } catch (Exception var4) {
            return 0;
        }
    }

    public void deslocacaoUtilizador() {
        try{
            lock.writeLock();
            ++this.numUtilizadores;
        }finally {
            lock.writeUnlock();
        }

    }
    public String moveTo (String user, int x, int y){
        // descobrir e retirar da antiga localização
        int oldX = -1, oldY = -1;
        boolean primeiraEntrada = true;
        String rep = "ficou,acabou";
        for (int row = 0; row < aresta; row++) {
            for (int col = 0; col < aresta; col++) {
                if(mapa[row][col] != null && mapa[row][col].contains(user)){
                    System.out.println(row + col);
                    if( row != x || col != y){
                        primeiraEntrada = false;
                        oldX = row; oldY = col;
                        mapa[row][col].remove(user);
                        System.out.println(mapa[row][col].size());
                        // notificar caso a localização fique vazia
                        if(mapa[oldX][oldY].isEmpty()) rep = "vazia," + String.valueOf(oldX) + "," + String.valueOf(oldY) + ",";
                        else rep ="saiu," + String.valueOf(oldX) + "," + String.valueOf(oldY) + ",";
                        break;}
                    else break;
                }
            }
        }
        if(primeiraEntrada) entradaUtilizador();
        // mover para nova localização
        if(mapa[x][y] == null) mapa[x][y] = new ArrayList<>();
        if(!mapa[x][y].contains(user)){
            this.atualizarContactos(user, x, y);
            mapa[x][y].add(user);
            if(primeiraEntrada)
                rep ="entrou,acabou";
        }
        // notificar entrada em localização

        return rep;
    }
    private void atualizarContactos(String user, int x, int y) {
        List<String> temp = mapa[x][y];
        ArrayList<String> contactosUser = contactos.get(user);
        for(String elem : temp){
            if(contactosUser!=null && !contactosUser.contains(elem)){
                ArrayList<String> userlist = contactos.get(user);
                userlist.add(elem);
                contactos.put(user,userlist);
            }else{
                contactos.put(user,new ArrayList<>());
                ArrayList<String> userlist = contactos.get(user);
                userlist.add(elem);
                contactos.put(user,userlist);
            }
            if(contactos.get(elem)!= null && !contactos.get(elem).contains(user)){
                ArrayList<String> elemlist = contactos.get(elem);
                elemlist.add(user);
                contactos.put(elem,elemlist);
            }else{
                contactos.put(elem,new ArrayList<>());
                ArrayList<String> elemlist = contactos.get(elem);
                elemlist.add(user);
                contactos.put(elem,elemlist);
            }
        }
    }

    public String addInfetado(String infetado) {
        try {
            lock.writeLock();
            ++this.numInfetados;
            String rep = "";
            if (this.contactos != null && this.contactos.get(infetado) != null) {
                ArrayList<String> list = this.contactos.get(infetado);
                rep = (String) list.stream().map(String::valueOf).collect(Collectors.joining(","));
            }
            return rep;
        }finally {
            lock.writeUnlock();
        }


    }




    public String registar(String nome, String pass){
        if(userPassword.containsKey(nome)){
            return "ja_existe_nome";
        }
        else{
            this.userPassword.put(nome,pass);
            return "ok";
        }
    }

    public String login(String nome, String pass) {
        if (userPassword.containsKey(nome)) {
            String password = this.userPassword.get(nome);
            if (password.equals(pass))
                return "ok";
            else if (password.equals("userInfected##")) {
                return "User Bloqueado. Mantenha as normas da DGS e continue em isolamento.";
            } else if (this.userPassword.get(nome) == null) {
                return "invalid_username";
            } else
                return "invalid_password";
        }
        else{
            return "utilizador_inexistente";
        }
    }


    public int utilizadoresPosicao(int x, int y){
        return mapa[x][y].size();
    }

    public void changePassword(String nome, String novaPass){
        this.userPassword.put(nome,novaPass);
    }

  /*  public void adicionaNovaLocalizacao(String user, int x, int y) {
        this.mapa[x][y].add(user);
        atualizarContactos(user,x,y);

    }

    public String mudarLocalizacao(String nome, int x, int y){
        Boolean encontrou = false;
        System.out.println(aresta);
        for(int i = 0; i < aresta || encontrou ; i++){
            for(int j = 0; j < aresta || encontrou ; j++){
                if(mapa[i][j]!=null && mapa[i][j].contains(nome)) {
                    System.out.println(i + ","+ j);
                    encontrou = true;
                    if (x == i && y == j) {
                        System.out.println(x+","+i+"-"+y+","+j);
                        System.out.println("ola");
                        return "mesmaPosicao";
                    } else {
                        mapa[i][j].remove(nome);
                        mapa[x][y].add(nome);
                        atualizarContactos(nome, x, y);
                    }
                }
            }
        }
        System.out.println("ole");
        return "ok";
    }
    public void atualizaHistorial(String nome, int x, int y){
        ArrayList<String> temp = new ArrayList<>();
        ArrayList<String> temp2 = new ArrayList<>();
        if(mapa[x][y].size()>0) {
            temp = mapa[x][y];
        }
        if(this.contactos.containsKey(nome) && this.contactos.get(nome)!=null && this.contactos.size()>0){
            temp2 = contactos.get(nome);
            for(int i = 0 ; i< temp.size();i++){
                temp.add(temp2.get(i));
            }
        }
        contactos.put(nome,temp);


    }*/

}
