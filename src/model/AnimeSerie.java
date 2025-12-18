package model;

import model.enums.EstadoAnime;
import exception.ValidacionException;

/**
 * Representa una Serie de Animé. Extiende la clase base Anime.
 * Agrega el atributo de cantidad de capítulos.
 */
public class AnimeSerie extends Anime {
    private int capitulos;

    public AnimeSerie(String titulo, int anio, String estudio, EstadoAnime estado, int capitulos) {
        super(titulo, anio, estudio, estado);
        this.capitulos = capitulos;
    }

    /**
     * Sobrescribe la validación para verificar también los capítulos.
     */
    @Override
    public void validar() throws ValidacionException {
        super.validar(); // Llama a la validación del padre primero
        if (capitulos <= 0) {
            throw new ValidacionException("La cantidad de capítulos debe ser mayor a 0.");
        }
    }

    public int getCapitulos() { return capitulos; }
}