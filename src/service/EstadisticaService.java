package service;

import model.Anime;
import model.enums.EstadoAnime;
import model.enums.Genero;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EstadisticaService {

    // RF5: Promedio de calificaciones global
    public double calcularPromedioGlobal(List<Anime> animes) {
        return animes.stream()
                .filter(a -> a.getCalificacion() > 0)
                .mapToInt(Anime::getCalificacion)
                .average()
                .orElse(0.0);
    }

    // RF5: Cantidad de animé por estado
    public Map<EstadoAnime, Long> cantidadPorEstado(List<Anime> animes) {
        return animes.stream()
                .collect(Collectors.groupingBy(Anime::getEstado, Collectors.counting()));
    }

    // RF5: Top género más frecuente
    public String generoMasFrecuente(List<Anime> animes) {
        return animes.stream()
                .flatMap(a -> a.getGeneros().stream())
                .collect(Collectors.groupingBy(g -> g, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> entry.getKey().name())
                .orElse("Sin datos");
    }
}