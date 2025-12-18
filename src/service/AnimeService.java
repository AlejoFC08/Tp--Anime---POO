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

/**
 * Servicio principal que maneja la lógica de negocio.
 * Se encarga de coordinar los repositorios y aplicar reglas de negocio.
 */
public class AnimeService {
    private AnimeRepository repositorio;
    private ListasRepository repoListas;
    
    // Listas mantenidas en memoria principal para acceso rápido
    private List<Anime> catalogoEnMemoria;
    private List<ListaPersonalizada> listasEnMemoria;

    public AnimeService(AnimeRepository repositorio) {
        this.repositorio = repositorio;
        this.catalogoEnMemoria = repositorio.cargar();
        
        // --- SEED DATA (Datos de prueba automáticos) ---
        // Si no hay datos guardados, cargamos animes famosos para probar
        if (this.catalogoEnMemoria.isEmpty()) {
            cargarDatosDePrueba();
        }
        
        this.repoListas = new ListasRepository();
        this.listasEnMemoria = repoListas.cargar();
    }

    /**
     * Carga un set inicial de animes si el catálogo está vacío.
     */
    private void cargarDatosDePrueba() {
        try {
            registrarSerie("Dragon Ball Z", 1989, "Toei Animation", EstadoAnime.FINALIZADO, 291);
            agregarDetalles("Dragon Ball Z", 5, Genero.SHONEN, Genero.ACCION);

            registrarPelicula("El Viaje de Chihiro", 2001, "Studio Ghibli", EstadoAnime.FINALIZADO, 125);
            agregarDetalles("El Viaje de Chihiro", 5, Genero.FANTASIA, Genero.DRAMA);

            registrarSerie("One Piece", 1999, "Toei Animation", EstadoAnime.VIENDO, 1000);
            agregarDetalles("One Piece", 5, Genero.SHONEN, Genero.FANTASIA);
            
            // ... (Se pueden agregar más aquí) ...
            
            System.out.println("--- Datos de prueba cargados correctamente ---");
        } catch (Exception e) {
            System.err.println("Error cargando datos de prueba: " + e.getMessage());
        }
    }

    // Método auxiliar para completar datos de prueba
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

    // ==========================================
    //       MÉTODOS CRUD (Crear, Leer, Borrar)
    // ==========================================

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
        // Como trabajamos por referencia, el objeto ya está modificado en memoria.
        // Solo hace falta guardar el estado actual en el archivo.
        repositorio.guardar(catalogoEnMemoria);
    }

    // ==========================================
    //       BÚSQUEDA Y ORDENAMIENTO
    // ==========================================

    public List<Anime> getTodos() {
        return new ArrayList<>(catalogoEnMemoria);
    }

    /**
     * Aplica un patrón Strategy de filtrado.
     */
    public List<Anime> buscar(FiltroAnime filtro) {
        return catalogoEnMemoria.stream()
                .filter(filtro::cumple)
                .collect(Collectors.toList());
    }

    /**
     * Aplica un patrón Strategy de ordenamiento.
     */
    public List<Anime> ordenar(OrdenadorAnime ordenador) {
        List<Anime> lista = new ArrayList<>(catalogoEnMemoria);
        lista.sort(ordenador.comparator());
        return lista;
    }

    public Anime buscarPorTitulo(String titulo) {
        return catalogoEnMemoria.stream()
                .filter(a -> a.getTitulo().equalsIgnoreCase(titulo))
                .findFirst()
                .orElse(null);
    }

    // ==========================================
    //       GESTIÓN DE LISTAS DE USUARIO
    // ==========================================

    public void crearLista(String nombre) throws ValidacionException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ValidacionException("El nombre de la lista no puede estar vacío.");
        }
        if (listasEnMemoria.stream().anyMatch(l -> l.getNombre().equalsIgnoreCase(nombre))) {
            throw new ValidacionException("Ya existe una lista con ese nombre.");
        }
        
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
                repoListas.guardar(listasEnMemoria); // Persistir cambios
                return;
            }
        }
    }
}