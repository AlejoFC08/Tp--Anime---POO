package ui;

import repository.file.AnimeRepository;
import repository.file.FileAnimeRepository;
import service.AnimeService;
import service.controller.AnimeController;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Ejecutar en el hilo de despacho de eventos de Swing (Buena práctica)
        SwingUtilities.invokeLater(() -> {
            try {
                // 1. Inicializar Repositorio (Persistencia)
                AnimeRepository repositorio = new FileAnimeRepository();

                // 2. Inicializar Servicio (Lógica de Negocio)
                AnimeService servicio = new AnimeService(repositorio);

                // 3. Inicializar Controlador (Coordinador)
                AnimeController controller = new AnimeController(servicio);

                // 4. Iniciar la UI (Vista)
                new AnimeMainFrame(controller);

            } catch (Exception e) {
                e.printStackTrace();
                // Opcional: Mostrar error fatal en un popup si falla el inicio
            }
        });
    }
}