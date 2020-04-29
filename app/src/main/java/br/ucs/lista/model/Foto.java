package br.ucs.lista.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Foto {

    private Integer id;
    private String titulo;
    private String desc;
    private String path;

    public Foto(String titulo) {
        this.titulo = titulo;
    }
}
