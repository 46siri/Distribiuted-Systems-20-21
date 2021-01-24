package Servidor;

import java.util.ArrayList;
import java.util.HashMap;

public class Data {
    private ArrayList<User> utilizadores;
    private int nr_users;
    private int nr_infetados;

    public Data(){
        this.nr_users=0;
        this.nr_infetados=0;
        this.utilizadores = new ArrayList<>();
    }

    public int getNr_users() {
        return nr_users;
    }

    public void setNr_users(int nr_users) {
        this.nr_users = nr_users;
    }

    public int getNr_infetados() {
        return nr_infetados;
    }

    public void setNr_infetados(int nr_infetados) {
        this.nr_infetados = nr_infetados;
    }


    public void adicionaInfectados(){
        this.nr_infetados++;
    }

    public void adicionaUsers(User user){
        this.nr_users++;
        this.utilizadores.add(user);
    }

    public ArrayList<User> getUtilizadores() {
        return utilizadores;
    }

    public void setUtilizadores(ArrayList<User> utilizadores) {
        this.utilizadores = utilizadores;
    }
}
