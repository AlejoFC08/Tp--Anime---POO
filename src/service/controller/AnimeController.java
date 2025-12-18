package service.controller;

import model.Anime;
import model.enums.EstadoAnime;
import model.enums.Genero;
import service.AnimeService;
import exception.ValidacionException;
import strategy.filter.FiltroAnime;
import strategy.sort.OrdenadorAnime;
import model.ListaPersonalizada;

import java.util.List;

public class AnimeController {
    private AnimeService service;

    public AnimeController(AnimeService service) {
        this.service = service;
    }

    // --- Métodos que la Vista necesita invocar ---

    public void registrarSerie(String titulo, int anio, String estudio, EstadoAnime estado, int capitulos) throws ValidacionException {
        service.registrarSerie(titulo, anio, estudio, estado, capitulos);
    }

    public void registrarPelicula(String titulo, int anio, String estudio, EstadoAnime estado, int duracion) throws ValidacionException {
        service.registrarPelicula(titulo, anio, estudio, estado, duracion);
    }

    public List<Anime> getTodos() {
        return service.getTodos();
    }

    public List<Anime> buscar(FiltroAnime filtro) {
        return service.buscar(filtro);
    }

    public void eliminar(Anime anime) {
        service.eliminar(anime);
    }

    public Anime buscarPorTitulo(String titulo) {
        return service.buscarPorTitulo(titulo);
    }

    public void actualizar(Anime anime) {
        service.actualizarAnime(anime);
    }

    public void crearLista(String nombre) throws ValidacionException {
        service.crearLista(nombre);
    }

    public List<ListaPersonalizada> getListas() {
        return service.getListas();
    }

    public void agregarAnimeALista(String nombreLista, Anime anime) {
        service.agregarAnimeALista(nombreLista, anime);
    }

}

