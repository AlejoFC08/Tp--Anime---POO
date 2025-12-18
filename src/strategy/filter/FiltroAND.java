package strategy.filter;
import model.Anime;

/**
 * Filtro compuesto que une dos filtros mediante lógica AND.
 * Permite encadenar múltiples criterios de búsqueda.
 */
public class FiltroAND implements FiltroAnime {
    private FiltroAnime f1;
    private FiltroAnime f2;

    public FiltroAND(FiltroAnime f1, FiltroAnime f2) {
        this.f1 = f1;
        this.f2 = f2;
    }

    @Override
    public boolean cumple(Anime anime) {
        return f1.cumple(anime) && f2.cumple(anime);
    }
}