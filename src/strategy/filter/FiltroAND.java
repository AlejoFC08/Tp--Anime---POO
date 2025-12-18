package strategy.filter;

import model.Anime;

public class FiltroAND implements FiltroAnime {
    private FiltroAnime filtro1;
    private FiltroAnime filtro2;

    public FiltroAND(FiltroAnime filtro1, FiltroAnime filtro2) {
        this.filtro1 = filtro1;
        this.filtro2 = filtro2;
    }

    @Override
    public boolean cumple(Anime anime) {
        return filtro1.cumple(anime) && filtro2.cumple(anime);
    }
}