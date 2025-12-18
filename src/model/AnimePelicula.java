package model;

import model.enums.EstadoAnime;
import exception.ValidacionException;

/**
 * Representa una Película de Animé. Extiende la clase base Anime.
 * Agrega el atributo de duración en minutos.
 */
public class AnimePelicula extends Anime {
    private int duracionMinutos;

    public AnimePelicula(String titulo, int anio, String estudio, EstadoAnime estado, int duracionMinutos) {
        super(titulo, anio, estudio, estado);
        this.duracionMinutos = duracionMinutos;
    }

    /**
     * Sobrescribe la validación para verificar la duración.
     */
    @Override
    public void validar() throws ValidacionException {
        super.validar();
        if (duracionMinutos <= 0) {
            throw new ValidacionException("La duración debe ser mayor a 0 minutos.");
        }
    }

    public int getDuracionMinutos() { return duracionMinutos; }
}