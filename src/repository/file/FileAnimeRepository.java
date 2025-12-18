package repository.file;

import model.Anime;
import exception.PersistenciaException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del repositorio que guarda los datos en un archivo binario (.dat).
 * Usa Serialización de Java.
 */
public class FileAnimeRepository implements AnimeRepository {
    private static final String FILE_NAME = "catalogo_anime.dat";

    @Override
    public void guardar(List<Anime> animes) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(animes);
        } catch (IOException e) {
            throw new PersistenciaException("Error al guardar el archivo de catálogo.", e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Anime> cargar() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return new ArrayList<>(); // Si no existe, inicia lista vacía
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Anime>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error leyendo archivo: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}