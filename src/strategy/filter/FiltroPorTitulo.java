package strategy.filter;

import model.Anime;

public class FiltroPorTitulo implements FiltroAnime {
    private String palabraClave;

    public FiltroPorTitulo(String palabraClave) {
        this.palabraClave = (palabraClave == null) ? "" : palabraClave.toLowerCase();
    }

    @Override
    public boolean cumple(Anime anime) {
        if (anime.getTitulo() == null) return false;
        return anime.getTitulo().toLowerCase().contains(palabraClave);
    }
}