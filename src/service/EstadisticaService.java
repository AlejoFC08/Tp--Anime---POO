package service;

import model.Anime;
import model.enums.EstadoAnime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servicio especializado en cálculos estadísticos sobre el catálogo.
 * Utiliza Java Streams para procesar datos eficientemente.
 */
public class EstadisticaService {

    // Calcula el promedio de calificación de todos los animes puntuados
    public double calcularPromedioGlobal(List<Anime> animes) {
        return animes.stream()
                .filter(a -> a.getCalificacion() > 0)
                .mapToInt(Anime::getCalificacion)
                .average()
                .orElse(0.0);
    }

    // Cuenta cuántos animes hay de cada Estado (Viendo, Finalizado, etc.)
    public Map<EstadoAnime, Long> cantidadPorEstado(List<Anime> animes) {
        return animes.stream()
                .collect(Collectors.groupingBy(Anime::getEstado, Collectors.counting()));
    }

    // Encuentra el género que más se repite en el catálogo
    public String generoMasFrecuente(List<Anime> animes) {
        return animes.stream()
                .flatMap(a -> a.getGeneros().stream()) // Aplanar lista de listas de géneros
                .collect(Collectors.groupingBy(g -> g, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> entry.getKey().name())
                .orElse("Sin datos");
    }
}