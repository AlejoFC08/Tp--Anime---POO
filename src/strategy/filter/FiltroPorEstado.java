package strategy.filter;

import model.Anime;
import model.enums.EstadoAnime;

public class FiltroPorEstado implements FiltroAnime {
    private EstadoAnime estadoBuscado;

    public FiltroPorEstado(EstadoAnime estadoBuscado) {
        this.estadoBuscado = estadoBuscado;
    }

    @Override
    public boolean cumple(Anime anime) {
        return anime.getEstado() == estadoBuscado;
    }
}