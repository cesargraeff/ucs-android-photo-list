package br.ucs.lista.model;

import java.io.Serializable;

public class Foto implements Serializable {

    private Integer id;
    private String titulo;
    private String desc;
    private String path;

    public Foto(Integer id, String titulo, String desc, String path) {
        this.id = id;
        this.titulo = titulo;
        this.desc = desc;
        this.path = path;
    }

    public Foto(String path) {
        this.path = path;
        this.titulo = path;
    }

    public Foto(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
