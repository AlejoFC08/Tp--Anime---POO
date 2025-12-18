package strategy.sort;

import model.Anime;
import java.util.Comparator;

public interface OrdenadorAnime {
    Comparator<Anime> comparator();
}