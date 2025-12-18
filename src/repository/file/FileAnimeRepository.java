package repository.file;

import model.Anime;
import exception.PersistenciaException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
            return new ArrayList<>(); // Si no existe, devuelve lista vacía
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Anime>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // Si el archivo está corrupto o hay error, podrías devolver vacía o lanzar error
            // Por simplicidad, devolvemos vacía para no bloquear la app, pero logueamos
            System.err.println("Error cargando archivo, se iniciará catálogo vacío: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}