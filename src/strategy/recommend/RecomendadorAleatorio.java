package strategy.recommend;
import model.Anime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Estrategia simple de recomendación: elige animes al azar del catálogo.
 */
public class RecomendadorAleatorio implements Recomendador {
    @Override
    public List<Anime> recomendar(List<Anime> base, int n) {
        List<Anime> copia = new ArrayList<>(base);
        Collections.shuffle(copia); // Mezcla aleatoriamente
        int cantidad = Math.min(n, copia.size());
        return copia.subList(0, cantidad);
    }
}