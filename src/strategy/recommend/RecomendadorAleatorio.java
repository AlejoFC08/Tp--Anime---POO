package strategy.recommend;
import model.Anime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecomendadorAleatorio implements Recomendador {
    @Override
    public List<Anime> recomendar(List<Anime> base, int n) {
        List<Anime> copia = new ArrayList<>(base);
        Collections.shuffle(copia);
        int cantidad = Math.min(n, copia.size());
        return copia.subList(0, cantidad);
    }
}