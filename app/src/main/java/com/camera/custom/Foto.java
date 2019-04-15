package com.camera.custom;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Foto {

    @Id
    private Long id;

    private String caminho;

    private int pesquisa;

    @Generated(hash = 1282187533)
    public Foto(Long id, String caminho, int pesquisa) {
        this.id = id;
        this.caminho = caminho;
        this.pesquisa = pesquisa;
    }

    @Generated(hash = 483995547)
    public Foto() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCaminho() {
        return this.caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public int getPesquisa() {
        return this.pesquisa;
    }

    public void setPesquisa(int pesquisa) {
        this.pesquisa = pesquisa;
    }

}
