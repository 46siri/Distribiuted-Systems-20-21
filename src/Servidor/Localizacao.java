package Servidor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Classe Localizacao responsável por guardar o mapa das localizações dos utilizadores .
 */
public class Localizacao {
    private Map<String, PrintWriter> notifcacoes;
    private final int aresta;
    private List<String> [][] mapa;
    private List<String> [][] mapaHistoricoUtilizadores;
    private List<String> [][] mapaHistoricoInfecoes;
    private Map<String, ArrayList<String>> contactos;
    private int numInfetados;
    private Map<String,String> userPassword;
    private RWLock lock ;



    /**
     Construtor da class Localizacao.
     @param aresta aresta indicada pelo utilizador para definir a sua localização no mapa.
     */

    public Localizacao(int aresta){
        this.notifcacoes = new HashMap<>();
        this.aresta = aresta;
        this.mapa = new ArrayList [aresta][aresta];
        for(int i = 0; i<aresta;i++){
            for(int j = 0 ; j < aresta; j++){
               this.mapa[i][j] = new ArrayList<>();
            }
        }
        this.mapaHistoricoUtilizadores = new ArrayList [aresta][aresta];
        for(int i = 0; i<aresta;i++){
            for(int j = 0 ; j < aresta; j++){
                this.mapaHistoricoUtilizadores[i][j] = new ArrayList<>();
            }
        }
        this.mapaHistoricoInfecoes = new ArrayList [aresta][aresta];
        for(int i = 0; i<aresta;i++){
            for(int j = 0 ; j < aresta; j++){
                this.mapaHistoricoInfecoes[i][j] = new ArrayList<>();
            }
        }
        this.contactos = new HashMap();
        userPassword = new HashMap<>();
        this.lock = new RWLock();
    }


    /**
     Método get para o número de utilizadores infetados.
     @return int com o número de Utilizadores infetados.
     */
    public int getNumInfetados() {
        return this.numInfetados;
    }
    /**
     Método get para o número de utilizadores numa dada localização.
     @param x posição x (
     @return int com o número de Utilizadores numa dada localização.
     */
    public int getNumUsersLocalizacao(int x, int y) {
        try {lock.readLock();
            return this.mapa[x][y].size();
        } catch (Exception e) {
            return 0;
        }finally {
            lock.readUnlock();
        }
    }
    public int getNumUsersHistorico(int x, int y) {
        try {lock.readLock();
            return this.mapaHistoricoUtilizadores[x][y].size();
        } catch (Exception e) {
            return 0;
        }finally {
            lock.readUnlock();
        }
    }
    public int getNumUsersInfecao(int x, int y) {
        try {lock.readLock();
            return this.mapaHistoricoInfecoes[x][y].size();
        } catch (Exception e) {
            return 0;
        }finally {
            lock.readUnlock();
        }
    }


    public void adicionaHistoricoUsers(String user, int x, int y){
        try{lock.writeLock();
        if (!mapaHistoricoUtilizadores[x][y].contains(user)){
            this.mapaHistoricoUtilizadores[x][y].add(user);
        }
    }finally {
            lock.writeUnlock();
        }
        }

    public void adicionaHistoricoInfecoes(String user){
        try{
        lock.writeLock();
        for(int i = 0; i < aresta ; i++){
            for(int j = 0; j < aresta ; j++){
                if(mapa[i][j].contains(user)){
                    if (!mapaHistoricoInfecoes[i][j].contains(user)){
                        this.mapaHistoricoInfecoes[i][j].add(user);
                    }
                }
            }
        }
    }finally {
            lock.writeUnlock();
        }
        }



    public void moveTo (String user, int x, int y){
        try{lock.writeLock();
        for (int row = 0; row < aresta; row++) {
            for (int col = 0; col < aresta; col++) {
                if(mapa[row][col] != null && mapa[row][col].contains(user)){
                    System.out.println(row + col);
                    if( row != x || col != y){
                        mapa[row][col].remove(user);
                        System.out.println(mapa[row][col].size());
                    }
                    break;
                }
            }
        }
        // mover para nova localização
        if(mapa[x][y] == null) mapa[x][y] = new ArrayList<>();
        if(!mapa[x][y].contains(user)){
            this.atualizarContactos(user, x, y);
            mapa[x][y].add(user);
        }
    }finally {
            lock.writeUnlock();
        }
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
            try {
                lock.writeLock();
                this.userPassword.put(nome, pass);
                return "ok";
            }finally {
                lock.writeUnlock();
            }
        }
    }

    public String login(String nome, String pass) {
        if(nome.equals("admin") && pass.equals("admin")){
            System.out.println("olaaaaa");
            return "admin";
            }
        if (userPassword.containsKey(nome)) {
            String password = this.userPassword.get(nome);
            try{lock.readLock();
            if (password.equals(pass)) {
                return "ok";
            }
            else if (password.equals("userInfected##")) {
                return "User Bloqueado. Mantenha as normas da DGS e continue em isolamento.";
            } else if (this.userPassword.get(nome) == null) {
                return "invalid_username";
            } else
                return "invalid_password";
        }finally {
                lock.readUnlock();
            }
            }
        else{
            return "utilizador_inexistente";
        }
    }

    public void changePassword(String nome, String novaPass){
        try{
            lock.writeLock();
        this.userPassword.put(nome,novaPass);
    }finally {
            lock.writeUnlock();
        }
        }

    public Map<String, PrintWriter> getNotifcacoes() {
        return notifcacoes;
    }

    public List<PrintWriter> notifica(String user){
        List<String> temp;
        List<PrintWriter> temp2 = new ArrayList<>();
        if(contactos.get(user)!=null) {
            temp = this.contactos.get(user);
            for (String s : temp) {
                temp2.add(this.notifcacoes.get(s));
            }
            return temp2;
        }
        return null;
    }

    public int escreveMapa(String path){
        try {
            lock.readLock();
            System.out.println("oooooooooo");
            System.out.println("uuuuuuuuuu");
            FileWriter myWriter = new FileWriter(path);
            for(int i =0 ; i< aresta;i++){
                for (int j = 0 ; j < aresta; j++){
                    myWriter.write(getNumUsersHistorico(i,j)+ "_" + getNumUsersInfecao(i,j) + "||");
            }
                myWriter.write("\n");
        }
            myWriter.close();
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }finally {
            lock.readUnlock();
        }
    }

}
