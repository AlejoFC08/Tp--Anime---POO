package strategy.sort;
import model.Anime;
import java.util.Comparator;

/**
 * Interfaz para el patrón Strategy (Ordenamiento).
 * Devuelve un Comparator para ordenar la lista de Animes.
 */
public interface OrdenadorAnime {
    Comparator<Anime> comparator();
}