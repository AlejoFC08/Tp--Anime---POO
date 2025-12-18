package strategy.sort;
import model.Anime;
import java.util.Comparator;

public class OrdenadorPorAnio implements OrdenadorAnime {
    @Override
    public Comparator<Anime> comparator() {
        return Comparator.comparingInt(Anime::getAnio).reversed();
    }
    @Override
    public String toString() { return "Año (Más nuevos primero)"; }
}