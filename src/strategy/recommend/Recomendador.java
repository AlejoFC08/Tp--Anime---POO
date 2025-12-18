package strategy.recommend;
import model.Anime;
import java.util.List;

public interface Recomendador {
    List<Anime> recomendar(List<Anime> base, int n);
}