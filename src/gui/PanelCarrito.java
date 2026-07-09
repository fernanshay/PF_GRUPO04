package gui;

import estructuras.cLES;
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

// Panel Swing para agregar y gestionar productos del carrito.
public class PanelCarrito extends JPanel {
    private final cLES carrito;
    private final DefaultTableModel modeloTabla;
    private final JTable tabla;
    private final JTextField campoCodigo;
    private final JTextField campoNombre;
    private final JTextField campoPrecio;
    private final JTextField campoCantidad;
    private final JLabel lblTotal;
    private boolean ignorarCambioSeleccion;

    // Crea un carrito nuevo si no se recibe uno externo.
    public PanelCarrito() {
        this(new cLES());
    }

    // Construye la vista usando un carrito compartido.
    public PanelCarrito(cLES carrito) {
        this.carrito = carrito;
        this.ignorarCambioSeleccion = false;
        setLayout(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(new GridLayout(2, 4, 8, 8));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Agregar producto al carrito"));

        campoCodigo = new JTextField();
        campoNombre = new JTextField();
        campoPrecio = new JTextField();
        campoCantidad = new JTextField("1");

        panelFormulario.add(new JLabel("Código:"));
        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(new JLabel("Precio:"));
        panelFormulario.add(new JLabel("Cantidad:"));
        panelFormulario.add(campoCodigo);
        panelFormulario.add(campoNombre);
        panelFormulario.add(campoPrecio);
        panelFormulario.add(campoCantidad);

        JPanel panelAcciones = new JPanel(new GridLayout(1, 4, 8, 8));
        JButton btnAgregar = new JButton("Agregar");
        JButton btnActualizarCantidad = new JButton("Actualizar cantidad");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");

        panelAcciones.add(btnAgregar);
        panelAcciones.add(btnActualizarCantidad);
        panelAcciones.add(btnEliminar);
        panelAcciones.add(btnLimpiar);

        JPanel panelNorte = new JPanel(new BorderLayout(10, 10));
        panelNorte.add(panelFormulario, BorderLayout.CENTER);
        panelNorte.add(panelAcciones, BorderLayout.SOUTH);

        String[] columnas = { "Código", "Nombre", "Precio", "Cantidad", "Subtotal" };
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

        lblTotal = new JLabel("Importe total: S/ 0.00", SwingConstants.RIGHT);
        lblTotal.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        add(panelNorte, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(lblTotal, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> agregarProducto());
        btnActualizarCantidad.addActionListener(e -> actualizarCantidadProducto());
        btnEliminar.addActionListener(e -> eliminarProductoSeleccionado());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        cargarDatosPrecargados();
        actualizarTabla();
    }

    // Agrega un producto con su cantidad al carrito.
    private void agregarProducto() {
        String codigo = campoCodigo.getText().trim();
        String nombre = campoNombre.getText().trim();
        String precioTexto = campoPrecio.getText().trim();
        String cantidadTexto = campoCantidad.getText().trim();

        try {
            double precio = Double.parseDouble(precioTexto);
            int cantidad = Integer.parseInt(cantidadTexto);

            if (codigo.isEmpty() || nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Código y nombre no pueden estar vacíos.",
                        "Datos inválidos",
                        JOptionPane.ERROR_MESSAGE);
            } else if (precio < 0 || cantidad <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Precio debe ser mayor o igual a 0 y cantidad mayor que 0.",
                        "Datos inválidos",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                Producto producto = new Producto(codigo, nombre, precio, 0);
                boolean agregado = carrito.insertarAlCarrito(producto, cantidad);

                if (agregado) {
                    actualizarTabla();
                    limpiarFormulario();
                    JOptionPane.showMessageDialog(this,
                            "Producto agregado al carrito.");
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo agregar el producto.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Precio y cantidad deben ser numéricos.",
                    "Error de datos",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Actualiza la cantidad del item seleccionado.
    private void actualizarCantidadProducto() {
        String codigo = obtenerCodigoSeleccionadoOTeclado();
        String cantidadTexto = campoCantidad.getText().trim();

        if (codigo == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un producto o escribe su código.",
                    "Sin selección",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                int cantidad = Integer.parseInt(cantidadTexto);
                boolean actualizado = carrito.actualizarCantidad(codigo, cantidad);

                if (actualizado) {
                    actualizarTabla();
                    limpiarFormulario();
                    JOptionPane.showMessageDialog(this,
                            "Cantidad actualizada.");
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No se encontró el producto para actualizar.",
                            "Producto no encontrado",
                            JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "La cantidad debe ser numérica.",
                        "Error de datos",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Elimina el item seleccionado del carrito.
    private void eliminarProductoSeleccionado() {
        String codigo = obtenerCodigoSeleccionadoOTeclado();

        if (codigo == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un producto de la tabla o escribe su código.",
                    "Sin selección",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            boolean eliminado = carrito.eliminarProducto(codigo);

            if (eliminado) {
                actualizarTabla();
                limpiarFormulario();
                JOptionPane.showMessageDialog(this,
                        "Producto eliminado del carrito.");
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se encontró el producto para eliminar.",
                        "Producto no encontrado",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    // Refresca la tabla y el total visible.
    private void actualizarTabla() {
        Object[][] filas = carrito.obtenerFilas();

        modeloTabla.setRowCount(0);

        for (Object[] fila : filas) {
            modeloTabla.addRow(fila);
        }

        lblTotal.setText(String.format(java.util.Locale.US,
                "Importe total: S/ %.2f",
                carrito.calcularImporteTotal()));
    }

    // Limpia los campos y la selección.
    private void limpiarFormulario() {
        ignorarCambioSeleccion = true;
        tabla.clearSelection();
        ignorarCambioSeleccion = false;
        campoCodigo.setText("");
        campoNombre.setText("");
        campoPrecio.setText("");
        campoCantidad.setText("1");
    }

    // Copia la fila elegida al formulario.
    private void cargarSeleccionEnFormulario(ListSelectionEvent evento) {
        if (!evento.getValueIsAdjusting() && !ignorarCambioSeleccion) {
            int fila = tabla.getSelectedRow();

            if (fila >= 0) {
                campoCodigo.setText(modeloTabla.getValueAt(fila, 0).toString());
                campoNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
                campoPrecio.setText(modeloTabla.getValueAt(fila, 2).toString());
                campoCantidad.setText(modeloTabla.getValueAt(fila, 3).toString());
            }
        }
    }

    // Obtiene el código escrito o el de la fila seleccionada.
    private String obtenerCodigoSeleccionadoOTeclado() {
        String codigo = campoCodigo.getText().trim();

        if (codigo.isEmpty() && tabla.getSelectedRow() >= 0) {
            codigo = modeloTabla.getValueAt(tabla.getSelectedRow(), 0).toString();
        }

        if (codigo.isEmpty()) {
            codigo = null;
        }

        return codigo;
    }

    // Carga productos de ejemplo en el carrito con cantidad inicial de 1.
    private void cargarDatosPrecargados() {
        Producto[] productos = DatosPrecargados.obtenerProductosBase();

        for (Producto producto : productos) {
            carrito.insertarAlCarrito(producto, 1);
        }
    }
}
