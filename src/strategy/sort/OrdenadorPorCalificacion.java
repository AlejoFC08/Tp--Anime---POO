package strategy.sort;
import model.Anime;
import java.util.Comparator;

public class OrdenadorPorCalificacion implements OrdenadorAnime {
    @Override
    public Comparator<Anime> comparator() {
        return Comparator.comparingInt(Anime::getCalificacion).reversed();
    }
    @Override
    public String toString() { return "Calificación (Mejores primero)"; }
}