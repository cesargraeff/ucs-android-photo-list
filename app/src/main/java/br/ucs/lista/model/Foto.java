package br.ucs.lista.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Foto implements Serializable {

    private Integer id;
    private String titulo;
    private String desc;
    private String path;

    public Foto(String path) {
        this.path = path;
    }
}
