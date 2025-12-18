package service;

import model.Anime;
import model.AnimeSerie;
import model.AnimePelicula;
import model.ListaPersonalizada;
import model.enums.EstadoAnime;
import model.enums.Genero;
import repository.file.AnimeRepository;
import repository.file.ListasRepository;
import exception.ValidacionException;
import strategy.filter.FiltroAnime;
import strategy.sort.OrdenadorAnime;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AnimeService {
    private AnimeRepository repositorio;
    private ListasRepository repoListas;

    private List<Anime> catalogoEnMemoria;
    private List<ListaPersonalizada> listasEnMemoria;

    public AnimeService(AnimeRepository repositorio) {
        this.repositorio = repositorio;
        this.catalogoEnMemoria = repositorio.cargar();

        // --- LOGICA DE DATOS DE PRUEBA ---
        // Si la lista está vacía, cargamos los animes automáticos
        if (this.catalogoEnMemoria.isEmpty()) {
            cargarDatosDePrueba();
        }

        this.repoListas = new ListasRepository();
        this.listasEnMemoria = repoListas.cargar();
    }

    private void cargarDatosDePrueba() {
        try {
            // 1. Dragon Ball Z
            registrarSerie("Dragon Ball Z", 1989, "Toei Animation", EstadoAnime.FINALIZADO, 291);
            agregarDetalles("Dragon Ball Z", 5, Genero.SHONEN, Genero.ACCION);

            // 2. El Viaje de Chihiro
            registrarPelicula("El Viaje de Chihiro", 2001, "Studio Ghibli", EstadoAnime.FINALIZADO, 125);
            agregarDetalles("El Viaje de Chihiro", 5, Genero.FANTASIA, Genero.DRAMA);

            // 3. One Piece
            registrarSerie("One Piece", 1999, "Toei Animation", EstadoAnime.VIENDO, 1000);
            agregarDetalles("One Piece", 5, Genero.SHONEN, Genero.FANTASIA);

            // 4. Evangelion
            registrarSerie("Neon Genesis Evangelion", 1995, "Gainax", EstadoAnime.FINALIZADO, 26);
            agregarDetalles("Neon Genesis Evangelion", 5, Genero.MECHA, Genero.DRAMA);

            // 5. Akira
            registrarPelicula("Akira", 1988, "Tokyo Movie Shinsha", EstadoAnime.FINALIZADO, 124);
            agregarDetalles("Akira", 4, Genero.SEINEN, Genero.ACCION);

            System.out.println("--- Datos de prueba cargados correctamente ---");

        } catch (Exception e) {
            System.err.println("Error cargando datos de prueba: " + e.getMessage());
        }
    }

    private void agregarDetalles(String titulo, int calif, Genero... generos) {
        Anime a = buscarPorTitulo(titulo);
        if (a != null) {
            try {
                if (calif > 0) a.setCalificacion(calif);
            } catch (Exception e) {}
            for (Genero g : generos) a.agregarGenero(g);
        }
        repositorio.guardar(catalogoEnMemoria);
    }

    // --- MÉTODOS CRUD ---

    public void registrarSerie(String titulo, int anio, String estudio, EstadoAnime estado, int capitulos) throws ValidacionException {
        validarTituloUnico(titulo);
        AnimeSerie serie = new AnimeSerie(titulo, anio, estudio, estado, capitulos);
        serie.validar();
        catalogoEnMemoria.add(serie);
        repositorio.guardar(catalogoEnMemoria);
    }

    public void registrarPelicula(String titulo, int anio, String estudio, EstadoAnime estado, int duracion) throws ValidacionException {
        validarTituloUnico(titulo);
        AnimePelicula pelicula = new AnimePelicula(titulo, anio, estudio, estado, duracion);
        pelicula.validar();
        catalogoEnMemoria.add(pelicula);
        repositorio.guardar(catalogoEnMemoria);
    }

    private void validarTituloUnico(String titulo) throws ValidacionException {
        boolean existe = catalogoEnMemoria.stream()
                .anyMatch(a -> a.getTitulo().equalsIgnoreCase(titulo));
        if (existe) {
            throw new ValidacionException("Ya existe un animé con el título: " + titulo);
        }
    }

    public void eliminar(Anime anime) {
        catalogoEnMemoria.remove(anime);
        repositorio.guardar(catalogoEnMemoria);
    }

    public void actualizarAnime(Anime anime) {
        repositorio.guardar(catalogoEnMemoria);
    }

    public List<Anime> getTodos() {
        return new ArrayList<>(catalogoEnMemoria);
    }

    public List<Anime> buscar(FiltroAnime filtro) {
        return catalogoEnMemoria.stream().filter(filtro::cumple).collect(Collectors.toList());
    }

    public List<Anime> ordenar(OrdenadorAnime ordenador) {
        List<Anime> lista = new ArrayList<>(catalogoEnMemoria);
        lista.sort(ordenador.comparator());
        return lista;
    }

    public Anime buscarPorTitulo(String titulo) {
        return catalogoEnMemoria.stream()
                .filter(a -> a.getTitulo().equalsIgnoreCase(titulo))
                .findFirst().orElse(null);
    }

    // --- LISTAS PERSONALIZADAS ---

    public void crearLista(String nombre) throws ValidacionException {
        if (nombre == null || nombre.trim().isEmpty()) throw new ValidacionException("Nombre vacío.");
        if (listasEnMemoria.stream().anyMatch(l -> l.getNombre().equalsIgnoreCase(nombre)))
            throw new ValidacionException("Nombre duplicado.");

        listasEnMemoria.add(new ListaPersonalizada(nombre));
        repoListas.guardar(listasEnMemoria);
    }

    public List<ListaPersonalizada> getListas() {
        return new ArrayList<>(listasEnMemoria);
    }

    public void agregarAnimeALista(String nombreLista, Anime anime) {
        for (ListaPersonalizada lista : listasEnMemoria) {
            if (lista.getNombre().equals(nombreLista)) {
                lista.agregarAnime(anime);
                repoListas.guardar(listasEnMemoria);
                return;
            }
        }
    }
}