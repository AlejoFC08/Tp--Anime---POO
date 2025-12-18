package repository.file;

import model.Anime;
import java.util.List;

public interface AnimeRepository {
    void guardar(List<Anime> animes);
    List<Anime> cargar();
}