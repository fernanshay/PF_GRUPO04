package gui;

import estructuras.PilaDevoluciones;
import modelo.DatosPrecargados;
import modelo.Producto;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;

// Panel Swing para administrar la pila de devoluciones.
public class PanelDevoluciones extends JPanel {
    private final PilaDevoluciones pilaDevoluciones;
    private final DefaultTableModel modeloTabla;
    private final JTable tabla;
    private final JTextField campoCodigo;
    private final JTextField campoNombre;
    private final JTextField campoPrecio;
    private final JTextField campoStock;
    private final JLabel lblEstado;
    private boolean ignorarCambioSeleccion;

    // Crea el panel y carga devoluciones de ejemplo.
    public PanelDevoluciones() {
        pilaDevoluciones = new PilaDevoluciones();
        ignorarCambioSeleccion = false;
        setLayout(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(new GridLayout(2, 4, 8, 8));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Registrar devolución"));

        campoCodigo = new JTextField();
        campoNombre = new JTextField();
        campoPrecio = new JTextField();
        campoStock = new JTextField("1");

        panelFormulario.add(new JLabel("Código:"));
        panelFormulario.add(new JLabel("Producto:"));
        panelFormulario.add(new JLabel("Precio:"));
        panelFormulario.add(new JLabel("Stock:"));
        panelFormulario.add(campoCodigo);
        panelFormulario.add(campoNombre);
        panelFormulario.add(campoPrecio);
        panelFormulario.add(campoStock);

        JPanel panelAcciones = new JPanel(new GridLayout(1, 3, 8, 8));
        JButton btnApilar = new JButton("Apilar devolución");
        JButton btnDesapilar = new JButton("Procesar tope");
        JButton btnLimpiar = new JButton("Limpiar");
        panelAcciones.add(btnApilar);
        panelAcciones.add(btnDesapilar);
        panelAcciones.add(btnLimpiar);

        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.add(panelFormulario, BorderLayout.CENTER);
        panelSuperior.add(panelAcciones, BorderLayout.SOUTH);

        String[] columnas = { "Código", "Producto", "Precio", "Stock" };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(this::cargarSeleccionEnFormulario);

        JScrollPane scroll = new JScrollPane(tabla);

        lblEstado = new JLabel("Estado: sin devoluciones registradas", SwingConstants.LEFT);
        lblEstado.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(lblEstado, BorderLayout.SOUTH);

        btnApilar.addActionListener(e -> registrarDevolucion());
        btnDesapilar.addActionListener(e -> procesarTope());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        cargarDevolucionesPrecargadas();
        actualizarVista();
    }

    // Registra una nueva devolución en la pila.
    private void registrarDevolucion() {
        String codigo = campoCodigo.getText().trim();
        String nombre = campoNombre.getText().trim();
        String precioTexto = campoPrecio.getText().trim();
        String stockTexto = campoStock.getText().trim();

        try {
            double precio = Double.parseDouble(precioTexto);
            int stock = Integer.parseInt(stockTexto);

            if (codigo.isEmpty() || nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Código y producto no pueden estar vacíos.",
                        "Datos inválidos",
                        JOptionPane.ERROR_MESSAGE);
            } else if (precio < 0 || stock <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Precio debe ser mayor o igual a 0 y stock mayor que 0.",
                        "Datos inválidos",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                Producto producto = new Producto(codigo, nombre, precio, stock);
                pilaDevoluciones.apilar(producto);
                actualizarVista();
                limpiarCampos();
                JOptionPane.showMessageDialog(this,
                        "Devolución registrada correctamente.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Precio y stock deben ser numéricos.",
                    "Error de datos",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Procesa la devolución del tope de la pila.
    private void procesarTope() {
        Producto producto = pilaDevoluciones.desapilar();

        if (producto == null) {
            JOptionPane.showMessageDialog(this,
                    "No hay devoluciones pendientes por procesar.",
                    "Pila vacía",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            actualizarVista();
            limpiarCampos();
            JOptionPane.showMessageDialog(this,
                    "Devolución procesada: " + producto.getNombre() + ".");
        }
    }

    // Actualiza la tabla y el texto de estado.
    private void actualizarVista() {
        ignorarCambioSeleccion = true;
        tabla.clearSelection();
        ignorarCambioSeleccion = false;

        modeloTabla.setRowCount(0);

        Object[][] filas = pilaDevoluciones.obtenerFilas();

        for (Object[] fila : filas) {
            modeloTabla.addRow(fila);
        }

        lblEstado.setText(pilaDevoluciones.describirTope()
                + " | devoluciones pendientes: " + pilaDevoluciones.contarDevoluciones());
    }

    // Limpia los campos del formulario.
    private void limpiarCampos() {
        campoCodigo.setText("");
        campoNombre.setText("");
        campoPrecio.setText("");
        campoStock.setText("1");
    }

    // Copia la fila seleccionada al formulario.
    private void cargarSeleccionEnFormulario(ListSelectionEvent evento) {
        if (!evento.getValueIsAdjusting() && !ignorarCambioSeleccion) {
            int fila = tabla.getSelectedRow();

            if (fila >= 0) {
                campoCodigo.setText(modeloTabla.getValueAt(fila, 0).toString());
                campoNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
                campoPrecio.setText(modeloTabla.getValueAt(fila, 2).toString());
                campoStock.setText(modeloTabla.getValueAt(fila, 3).toString());
            }
        }
    }

    // Carga elementos iniciales para mostrar la pila desde el inicio.
    private void cargarDevolucionesPrecargadas() {
        Producto[] productos = DatosPrecargados.obtenerProductosBase();

        for (Producto producto : productos) {
            pilaDevoluciones.apilar(producto);
        }
    }
}
