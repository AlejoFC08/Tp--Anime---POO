package model;

import model.enums.EstadoAnime;
import exception.ValidacionException;

public class AnimeSerie extends Anime {
    private int capitulos;

    public AnimeSerie(String titulo, int anio, String estudio, EstadoAnime estado, int capitulos) {
        super(titulo, anio, estudio, estado);
        this.capitulos = capitulos;
    }

    @Override
    public void validar() throws ValidacionException {
        super.validar(); // Llama a la validación del padre
        if (capitulos <= 0) {
            throw new ValidacionException("La cantidad de capítulos debe ser mayor a 0.");
        }
    }

    public int getCapitulos() { return capitulos; }
}