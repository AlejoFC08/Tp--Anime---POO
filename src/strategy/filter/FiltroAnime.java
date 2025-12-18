package strategy.filter;
import model.Anime;

/**
 * Interfaz funcional para el patrón Strategy (Filtros).
 * Define una condición que un Anime debe cumplir.
 */
@FunctionalInterface
public interface FiltroAnime {
    boolean cumple(Anime anime);
}