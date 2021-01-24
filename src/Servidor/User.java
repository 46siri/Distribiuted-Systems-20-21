package Servidor;

import java.util.ArrayList;

public class User {
    private String nome;
    private Boolean infetado;


    public User(){
        this.nome = "";
        this.infetado= false;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getInfetado() {
        return infetado;
    }

    public void setInfetado(Boolean infetado) {
        this.infetado = infetado;
    }
}
