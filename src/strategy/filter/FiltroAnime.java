package strategy.filter;

import model.Anime;

@FunctionalInterface
public interface FiltroAnime {
    boolean cumple(Anime anime);
}

