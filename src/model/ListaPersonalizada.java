package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa una lista creada por el usuario (ej: "Favoritos", "Para ver después").
 * Contiene una lista de referencias a Animes.
 */
public class ListaPersonalizada implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nombre;
    private List<Anime> animes;

    public ListaPersonalizada(String nombre) {
        this.nombre = nombre;
        this.animes = new ArrayList<>();
    }

    public void agregarAnime(Anime anime) {
        // Evita duplicados en la misma lista
        if (!animes.contains(anime)) {
            animes.add(anime);
        }
    }

    public void quitarAnime(Anime anime) {
        animes.remove(anime);
    }

    public List<Anime> getAnimes() {
        return animes;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre + " (" + animes.size() + " animes)";
    }
}