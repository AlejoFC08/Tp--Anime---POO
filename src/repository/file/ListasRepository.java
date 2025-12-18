package repository.file;

import model.ListaPersonalizada;
import exception.PersistenciaException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ListasRepository {
    private static final String FILE_NAME = "listas_usuario.dat";

    public void guardar(List<ListaPersonalizada> listas) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(listas);
        } catch (IOException e) {
            throw new PersistenciaException("Error al guardar listas.", e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<ListaPersonalizada> cargar() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<ListaPersonalizada>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
}