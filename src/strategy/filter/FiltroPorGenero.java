package strategy.filter;

import model.Anime;
import model.enums.Genero;

public class FiltroPorGenero implements FiltroAnime {
    private Genero generoBuscado;

    public FiltroPorGenero(Genero generoBuscado) {
        this.generoBuscado = generoBuscado;
    }

    @Override
    public boolean cumple(Anime anime) {
        return anime.getGeneros().contains(generoBuscado);
    }
}