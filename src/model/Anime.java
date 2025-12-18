package model;

import model.enums.EstadoAnime;
import model.enums.Genero;
import exception.ValidacionException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

// Implementa Serializable para poder guardarse en archivo binario fácilmente
public abstract class Anime implements Serializable {
    private static final long serialVersionUID = 1L;

    private String titulo;
    private int anio;
    private String estudio;
    private int calificacion; // 1 a 5
    private EstadoAnime estado;
    private Set<Genero> generos;

    public Anime(String titulo, int anio, String estudio, EstadoAnime estado) {
        this.titulo = titulo;
        this.anio = anio;
        this.estudio = estudio;
        this.estado = estado;
        this.calificacion = 0; // 0 indica "sin calificar"
        this.generos = new HashSet<>();
    }

    // Método plantilla para validación (GRASP Expert)
    public void validar() throws ValidacionException {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new ValidacionException("El título no puede estar vacío.");
        }
        if (anio < 1950 || anio > 2030) {
            throw new ValidacionException("El año debe estar entre 1950 y 2030.");
        }
        if (calificacion != 0 && (calificacion < 1 || calificacion > 5)) {
            throw new ValidacionException("La calificación debe estar entre 1 y 5.");
        }
    }

    // Getters y Setters con lógica
    public void setCalificacion(int calificacion) throws ValidacionException {
        if (calificacion < 1 || calificacion > 5) {
            throw new ValidacionException("La calificación debe ser entre 1 y 5.");
        }
        this.calificacion = calificacion;
    }

    public void agregarGenero(Genero g) {
        this.generos.add(g);
    }

    public String getTitulo() { return titulo; }
    public int getAnio() { return anio; }
    public String getEstudio() { return estudio; }
    public EstadoAnime getEstado() { return estado; }
    public int getCalificacion() { return calificacion; }
    public Set<Genero> getGeneros() { return generos; }

    @Override
    public String toString() {
        return titulo + " (" + anio + ")";
    }
}