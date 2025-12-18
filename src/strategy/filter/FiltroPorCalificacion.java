package strategy.filter;

import model.Anime;

public class FiltroPorCalificacion implements FiltroAnime {
    private int calificacionMinima;

    public FiltroPorCalificacion(int calificacionMinima) {
        this.calificacionMinima = calificacionMinima;
    }

    @Override
    public boolean cumple(Anime anime) {
        return anime.getCalificacion() >= calificacionMinima;
    }
}