package ui;

import model.Anime;
import model.AnimeSerie;
import model.AnimePelicula;
import model.ListaPersonalizada;
import model.enums.EstadoAnime;
import model.enums.Genero;
import service.controller.AnimeController;
import service.EstadisticaService;
import exception.ValidacionException;

// Imports de Strategies (ESTO ES LO QUE TE FALTABA)
import strategy.filter.*;
import strategy.sort.*;
import strategy.recommend.Recomendador;
import strategy.recommend.RecomendadorAleatorio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AnimeMainFrame extends JFrame {
    private AnimeController controller;

    // Componentes de la UI
    private JTabbedPane tabbedPane;
    private JTable tablaAnimes;
    private DefaultTableModel tableModel;
    private Anime animeEnEdicion = null; // Controla si estamos editando

    // Campos del formulario
    private JTextField txtTitulo, txtAnio, txtEstudio, txtExtra;
    private JComboBox<EstadoAnime> cmbEstado;
    private JComboBox<String> cmbTipo;

    public AnimeMainFrame(AnimeController controller) {
        this.controller = controller;
        configurarVentana();
        inicializarComponentes();
        setVisible(true);
    }

    private void configurarVentana() {
        setTitle("Sistema de Clasificación de Animé");
        setSize(900, 650); // Un poco más grande para que quepan las pestañas
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void inicializarComponentes() {
        tabbedPane = new JTabbedPane();

        // Pestaña 1: Carga
        JPanel panelCarga = crearPanelCarga();
        tabbedPane.addTab("Registrar Animé", panelCarga);

        // Pestaña 2: Listado (Catálogo)
        JPanel panelListado = crearPanelListado();
        tabbedPane.addTab("Catálogo", panelListado);

        // Pestaña 3: Búsqueda
        JPanel panelBusqueda = crearPanelBusqueda();
        tabbedPane.addTab("Búsqueda Avanzada", panelBusqueda);

        // Pestaña 4: Mis Listas (NUEVO)
        JPanel panelListas = crearPanelListas();
        tabbedPane.addTab("Mis Listas", panelListas);

        // Evento: Actualizar tabla al cambiar de pestaña
        tabbedPane.addChangeListener(e -> {
            if (tabbedPane.getSelectedIndex() == 1) { // Catálogo
                actualizarTabla();
            }
        });

        add(tabbedPane);
    }

    // =========================================================
    //                PESTAÑA 1: CARGA / EDICIÓN
    // =========================================================
    private JPanel crearPanelCarga() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Tipo
        panel.add(new JLabel("Tipo de Animé:"));
        cmbTipo = new JComboBox<>(new String[]{"Serie", "Película"});
        panel.add(cmbTipo);

        // Título
        panel.add(new JLabel("Título:"));
        txtTitulo = new JTextField();
        panel.add(txtTitulo);

        // Año
        panel.add(new JLabel("Año:"));
        txtAnio = new JTextField();
        panel.add(txtAnio);

        // Estudio
        panel.add(new JLabel("Estudio:"));
        txtEstudio = new JTextField();
        panel.add(txtEstudio);

        // Estado
        panel.add(new JLabel("Estado:"));
        cmbEstado = new JComboBox<>(EstadoAnime.values());
        panel.add(cmbEstado);

        // Dato Variable
        JLabel lblExtra = new JLabel("Cantidad de Capítulos:");
        panel.add(lblExtra);
        txtExtra = new JTextField();
        panel.add(txtExtra);

        // Evento cambio de tipo
        cmbTipo.addActionListener(e -> {
            if ("Serie".equals(cmbTipo.getSelectedItem())) {
                lblExtra.setText("Cantidad de Capítulos:");
            } else {
                lblExtra.setText("Duración (minutos):");
            }
        });

        // Botón Guardar
        JButton btnGuardar = new JButton("Registrar / Guardar");
        btnGuardar.addActionListener(e -> accionGuardar());
        panel.add(new JLabel(""));
        panel.add(btnGuardar);

        return panel;
    }

    private void accionGuardar() {
        try {
            String titulo = txtTitulo.getText();
            int anio = Integer.parseInt(txtAnio.getText());
            String estudio = txtEstudio.getText();
            EstadoAnime estado = (EstadoAnime) cmbEstado.getSelectedItem();
            int extra = Integer.parseInt(txtExtra.getText());

            if (animeEnEdicion == null) {
                // Modo CREAR
                if ("Serie".equals(cmbTipo.getSelectedItem())) {
                    controller.registrarSerie(titulo, anio, estudio, estado, extra);
                } else {
                    controller.registrarPelicula(titulo, anio, estudio, estado, extra);
                }
                JOptionPane.showMessageDialog(this, "¡Registrado con éxito!");
            } else {
                // Modo EDITAR (Borrar viejo y crear nuevo)
                controller.eliminar(animeEnEdicion);
                if ("Serie".equals(cmbTipo.getSelectedItem())) {
                    controller.registrarSerie(titulo, anio, estudio, estado, extra);
                } else {
                    controller.registrarPelicula(titulo, anio, estudio, estado, extra);
                }
                animeEnEdicion = null;
                txtTitulo.setEditable(true);
                JOptionPane.showMessageDialog(this, "¡Modificado con éxito!");
            }
            limpiarFormulario();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Año y Cantidad deben ser números.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void limpiarFormulario() {
        txtTitulo.setText("");
        txtAnio.setText("");
        txtEstudio.setText("");
        txtExtra.setText("");
        animeEnEdicion = null;
        txtTitulo.setEditable(true);
    }

    // =========================================================
    //                PESTAÑA 2: CATÁLOGO (Listado)
    // =========================================================
    private JPanel crearPanelListado() {
        JPanel panel = new JPanel(new BorderLayout());

        // --- Panel Herramientas (Norte) ---
        JPanel panelHerramientas = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Ordenar
        panelHerramientas.add(new JLabel("Ordenar por:"));
        JComboBox<OrdenadorAnime> cmbOrden = new JComboBox<>(new OrdenadorAnime[]{
                new OrdenadorPorTitulo(),
                new OrdenadorPorAnio(),
                new OrdenadorPorCalificacion()
        });
        cmbOrden.addActionListener(e -> {
            OrdenadorAnime ordenador = (OrdenadorAnime) cmbOrden.getSelectedItem();
            List<Anime> lista = controller.getTodos();
            lista.sort(ordenador.comparator());
            llenarTabla(lista);
        });
        panelHerramientas.add(cmbOrden);

        // Estadísticas
        JButton btnStats = new JButton("Ver Estadísticas");
        btnStats.addActionListener(e -> mostrarEstadisticas());
        panelHerramientas.add(btnStats);

        // Recomendar
        JButton btnRecomendar = new JButton("¡Recomiéndame algo!");
        btnRecomendar.addActionListener(e -> mostrarRecomendacion());
        panelHerramientas.add(btnRecomendar);

        panel.add(panelHerramientas, BorderLayout.NORTH);

        // --- Tabla (Centro) ---
        String[] columnas = {"Título", "Año", "Estudio", "Estado", "Calif.", "Tipo"};
        tableModel = new DefaultTableModel(columnas, 0);
        tablaAnimes = new JTable(tableModel);
        panel.add(new JScrollPane(tablaAnimes), BorderLayout.CENTER);

        // --- Botones Acción (Sur) ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnEditar = new JButton("Editar Seleccionado");
        btnEditar.addActionListener(e -> accionEditar());
        panelBotones.add(btnEditar);

        JButton btnAgregarLista = new JButton("Agregar a Lista...");
        btnAgregarLista.addActionListener(e -> accionAgregarALista());
        panelBotones.add(btnAgregarLista);

        JButton btnEliminar = new JButton("Eliminar Seleccionado");
        btnEliminar.setBackground(new Color(255, 100, 100));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.addActionListener(e -> accionEliminar());
        panelBotones.add(btnEliminar);

        JButton btnRefrescar = new JButton("Refrescar");
        btnRefrescar.addActionListener(e -> actualizarTabla());
        panelBotones.add(btnRefrescar);

        panel.add(panelBotones, BorderLayout.SOUTH);
        return panel;
    }

    private void actualizarTabla() {
        llenarTabla(controller.getTodos());
    }

    private void llenarTabla(List<Anime> lista) {
        tableModel.setRowCount(0);
        for (Anime a : lista) {
            String tipo = (a instanceof AnimeSerie) ? "Serie" : "Película";
            tableModel.addRow(new Object[]{
                    a.getTitulo(), a.getAnio(), a.getEstudio(), a.getEstado(),
                    (a.getCalificacion() > 0 ? a.getCalificacion() : "-"), tipo
            });
        }
    }

    private void accionEliminar() {
        int fila = tablaAnimes.getSelectedRow();
        if (fila == -1) return;
        String titulo = (String) tableModel.getValueAt(fila, 0);

        if (JOptionPane.showConfirmDialog(this, "¿Borrar '" + titulo + "'?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            Anime a = controller.buscarPorTitulo(titulo);
            if (a != null) {
                controller.eliminar(a);
                actualizarTabla();
            }
        }
    }

    private void accionEditar() {
        int fila = tablaAnimes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un animé.");
            return;
        }
        String titulo = (String) tableModel.getValueAt(fila, 0);
        Anime anime = controller.buscarPorTitulo(titulo);
        if (anime != null) {
            txtTitulo.setText(anime.getTitulo());
            txtTitulo.setEditable(false);
            txtAnio.setText(String.valueOf(anime.getAnio()));
            txtEstudio.setText(anime.getEstudio());
            cmbEstado.setSelectedItem(anime.getEstado());
            if (anime instanceof AnimeSerie) {
                cmbTipo.setSelectedItem("Serie");
                txtExtra.setText(String.valueOf(((AnimeSerie) anime).getCapitulos()));
            } else {
                cmbTipo.setSelectedItem("Película");
                txtExtra.setText(String.valueOf(((AnimePelicula) anime).getDuracionMinutos()));
            }
            animeEnEdicion = anime;
            tabbedPane.setSelectedIndex(0);
            JOptionPane.showMessageDialog(this, "Datos cargados. Modifica y guarda.");
        }
    }

    // =========================================================
    //                PESTAÑA 3: BÚSQUEDA
    // =========================================================
    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel panelFiltros = new JPanel(new GridLayout(5, 2, 5, 5));
        panelFiltros.setBorder(BorderFactory.createTitledBorder("Filtros"));

        JTextField txtB_Titulo = new JTextField();
        JComboBox<Genero> cmbB_Genero = new JComboBox<>(Genero.values());
        cmbB_Genero.insertItemAt(null, 0); cmbB_Genero.setSelectedIndex(0);
        JComboBox<EstadoAnime> cmbB_Estado = new JComboBox<>(EstadoAnime.values());
        cmbB_Estado.insertItemAt(null, 0); cmbB_Estado.setSelectedIndex(0);
        JComboBox<Integer> cmbB_Calif = new JComboBox<>(new Integer[]{1,2,3,4,5});
        cmbB_Calif.insertItemAt(null, 0); cmbB_Calif.setSelectedIndex(0);

        panelFiltros.add(new JLabel("Título:")); panelFiltros.add(txtB_Titulo);
        panelFiltros.add(new JLabel("Género:")); panelFiltros.add(cmbB_Genero);
        panelFiltros.add(new JLabel("Estado:")); panelFiltros.add(cmbB_Estado);
        panelFiltros.add(new JLabel("Calif. Mínima:")); panelFiltros.add(cmbB_Calif);

        JButton btnBuscar = new JButton("Buscar");
        panelFiltros.add(new JLabel("")); panelFiltros.add(btnBuscar);
        panel.add(panelFiltros, BorderLayout.NORTH);

        DefaultTableModel modRes = new DefaultTableModel(new String[]{"Título", "Estado", "Calif"}, 0);
        panel.add(new JScrollPane(new JTable(modRes)), BorderLayout.CENTER);

        btnBuscar.addActionListener(e -> {
            FiltroAnime f = a -> true;
            if (!txtB_Titulo.getText().isEmpty()) f = new FiltroAND(f, new FiltroPorTitulo(txtB_Titulo.getText()));
            if (cmbB_Genero.getSelectedItem() != null) f = new FiltroAND(f, new FiltroPorGenero((Genero)cmbB_Genero.getSelectedItem()));
            if (cmbB_Estado.getSelectedItem() != null) f = new FiltroAND(f, new FiltroPorEstado((EstadoAnime)cmbB_Estado.getSelectedItem()));
            if (cmbB_Calif.getSelectedItem() != null) f = new FiltroAND(f, new FiltroPorCalificacion((Integer)cmbB_Calif.getSelectedItem()));

            List<Anime> res = controller.buscar(f);
            modRes.setRowCount(0);
            for(Anime a : res) modRes.addRow(new Object[]{a.getTitulo(), a.getEstado(), a.getCalificacion()});
            if(res.isEmpty()) JOptionPane.showMessageDialog(this, "Sin resultados.");
        });
        return panel;
    }

    // =========================================================
    //                PESTAÑA 4: MIS LISTAS
    // =========================================================
    private JPanel crearPanelListas() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel panelCrear = new JPanel(new FlowLayout());
        JTextField txtNombre = new JTextField(15);
        JButton btnCrear = new JButton("Crear Lista");
        JComboBox<String> cmbListas = new JComboBox<>();
        DefaultListModel<String> listModel = new DefaultListModel<>();

        panelCrear.add(new JLabel("Nombre:")); panelCrear.add(txtNombre); panelCrear.add(btnCrear);
        panel.add(panelCrear, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.add(cmbListas, BorderLayout.NORTH);
        panelCentro.add(new JScrollPane(new JList<>(listModel)), BorderLayout.CENTER);
        panel.add(panelCentro, BorderLayout.CENTER);

        btnCrear.addActionListener(e -> {
            try {
                controller.crearLista(txtNombre.getText());
                cmbListas.addItem(txtNombre.getText());
                txtNombre.setText("");
                JOptionPane.showMessageDialog(this, "Lista creada.");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });

        cmbListas.addActionListener(e -> {
            listModel.clear();
            String nombre = (String) cmbListas.getSelectedItem();
            if(nombre == null) return;
            for(ListaPersonalizada l : controller.getListas()) {
                if(l.getNombre().equals(nombre)) {
                    for(Anime a : l.getAnimes()) listModel.addElement(a.toString());
                }
            }
        });

        // Cargar combo al inicio
        tabbedPane.addChangeListener(e -> {
            if(tabbedPane.getSelectedIndex() == 3) {
                cmbListas.removeAllItems();
                controller.getListas().forEach(l -> cmbListas.addItem(l.getNombre()));
            }
        });
        return panel;
    }

    // =========================================================
    //                OTROS MÉTODOS AUXILIARES
    // =========================================================
    private void mostrarEstadisticas() {
        EstadisticaService s = new EstadisticaService();
        List<Anime> l = controller.getTodos();
        JOptionPane.showMessageDialog(this,
                "Promedio: " + s.calcularPromedioGlobal(l) + "\nTop Género: " + s.generoMasFrecuente(l));
    }

    private void mostrarRecomendacion() {
        Recomendador r = new RecomendadorAleatorio();
        List<Anime> l = controller.getTodos();
        if(l.isEmpty()) { JOptionPane.showMessageDialog(this, "Catálogo vacío"); return; }
        List<Anime> rec = r.recomendar(l, 1);
        if(!rec.isEmpty()) JOptionPane.showMessageDialog(this, "Te recomiendo: " + rec.get(0).getTitulo());
    }

    private void accionAgregarALista() {
        int fila = tablaAnimes.getSelectedRow();
        if (fila == -1) return;
        String titulo = (String) tableModel.getValueAt(fila, 0);
        Anime anime = controller.buscarPorTitulo(titulo);

        List<ListaPersonalizada> listas = controller.getListas();
        if(listas.isEmpty()) { JOptionPane.showMessageDialog(this, "Crea una lista primero en la pestaña 'Mis Listas'"); return; }

        String[] nombres = listas.stream().map(ListaPersonalizada::getNombre).toArray(String[]::new);
        String sel = (String) JOptionPane.showInputDialog(this, "Elige lista:", "Agregar", JOptionPane.QUESTION_MESSAGE, null, nombres, nombres[0]);
        if(sel != null) {
            controller.agregarAnimeALista(sel, anime);
            JOptionPane.showMessageDialog(this, "Agregado.");
        }
    }
}