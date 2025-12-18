package strategy.sort;
import model.Anime;
import java.util.Comparator;

public class OrdenadorPorTitulo implements OrdenadorAnime {
    @Override
    public Comparator<Anime> comparator() {
        return Comparator.comparing(a -> a.getTitulo().toLowerCase());
    }
    @Override
    public String toString() { return "Título (A-Z)"; }
}