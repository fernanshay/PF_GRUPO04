package gui;

import estructuras.cCola;
import modelo.DatosPrecargados;
import modelo.NodoCola;
import modelo.Producto;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;

// Panel Swing para administrar la cola priorizada de despacho.
public class PanelDespacho extends JPanel {
    private final cCola colaDespacho;
    private final DefaultTableModel modeloTabla;
    private final JTable tabla;
    private final JTextField campoCodigo;
    private final JTextField campoNombre;
    private final JTextField campoPrecio;
    private final JTextField campoCantidad;
    private final JComboBox<String> comboPrioridad;
    private final JLabel lblEstado;

    // Crea el panel y carga algunos pedidos de ejemplo.
    public PanelDespacho() {
        colaDespacho = new cCola();
        setLayout(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(new GridLayout(2, 4, 8, 8));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Registrar pedido para despacho"));

        campoCodigo = new JTextField();
        campoNombre = new JTextField();
        campoPrecio = new JTextField();
        campoCantidad = new JTextField("1");

        panelFormulario.add(new JLabel("Código:"));
        panelFormulario.add(new JLabel("Producto:"));
        panelFormulario.add(new JLabel("Precio:"));
        panelFormulario.add(new JLabel("Cantidad:"));
        panelFormulario.add(campoCodigo);
        panelFormulario.add(campoNombre);
        panelFormulario.add(campoPrecio);
        panelFormulario.add(campoCantidad);

        JPanel panelPrioridad = new JPanel(new GridLayout(1, 2, 8, 8));
        panelPrioridad.setBorder(BorderFactory.createTitledBorder("Prioridad de envío"));
        comboPrioridad = new JComboBox<>(new String[] { "1 - Express", "2 - Normal", "3 - Económico" });
        panelPrioridad.add(new JLabel("Nivel:"));
        panelPrioridad.add(comboPrioridad);

        JPanel panelAcciones = new JPanel(new GridLayout(1, 3, 8, 8));
        JButton btnRegistrar = new JButton("Encolar");
        JButton btnAtender = new JButton("Atender siguiente");
        JButton btnLimpiar = new JButton("Limpiar");
        panelAcciones.add(btnRegistrar);
        panelAcciones.add(btnAtender);
        panelAcciones.add(btnLimpiar);

        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));
        panelSuperior.add(panelFormulario, BorderLayout.CENTER);
        panelSuperior.add(panelPrioridad, BorderLayout.NORTH);
        panelSuperior.add(panelAcciones, BorderLayout.SOUTH);

        String[] columnas = { "Prioridad", "Código", "Producto", "Precio", "Cantidad", "Subtotal" };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tabla);

        lblEstado = new JLabel("Estado: cola vacía", SwingConstants.LEFT);
        lblEstado.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(lblEstado, BorderLayout.SOUTH);

        btnRegistrar.addActionListener(e -> encolarPedido());
        btnAtender.addActionListener(e -> atenderSiguiente());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        cargarPedidosPrecargados();
        actualizarVista();
    }

    // Inserta un pedido en la cola según la prioridad seleccionada.
    private void encolarPedido() {
        String codigo = campoCodigo.getText().trim();
        String nombre = campoNombre.getText().trim();
        String precioTexto = campoPrecio.getText().trim();
        String cantidadTexto = campoCantidad.getText().trim();
        int prioridad = comboPrioridad.getSelectedIndex() + 1;

        try {
            double precio = Double.parseDouble(precioTexto);
            int cantidad = Integer.parseInt(cantidadTexto);

            if (codigo.isEmpty() || nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Código y producto no pueden estar vacíos.",
                        "Datos inválidos",
                        JOptionPane.ERROR_MESSAGE);
            } else if (precio < 0 || cantidad <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Precio debe ser mayor o igual a 0 y cantidad mayor que 0.",
                        "Datos inválidos",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                Producto producto = new Producto(codigo, nombre, precio, 0);
                boolean insertado = colaDespacho.encolar(producto, cantidad, prioridad);

                if (insertado) {
                    actualizarVista();
                    limpiarCampos();
                    JOptionPane.showMessageDialog(this,
                            "Pedido agregado a la cola de despacho.");
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo agregar el pedido.",
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

    // Atiende el pedido que está al frente de la cola.
    private void atenderSiguiente() {
        NodoCola pedido = colaDespacho.desencolar();

        if (pedido == null) {
            JOptionPane.showMessageDialog(this,
                    "No hay pedidos pendientes por atender.",
                    "Cola vacía",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            actualizarVista();
            limpiarCampos();
            Producto producto = pedido.getProducto();
            String nombre = producto != null ? producto.getNombre() : "pedido";
            JOptionPane.showMessageDialog(this,
                    "Pedido atendido: " + nombre + ".");
        }
    }

    // Actualiza la tabla y el estado visible.
    private void actualizarVista() {
        modeloTabla.setRowCount(0);

        Object[][] filas = colaDespacho.obtenerFilas();

        for (Object[] fila : filas) {
            modeloTabla.addRow(fila);
        }

        NodoCola frente = colaDespacho.acceso();
        String estado = "Estado: cola vacía";

        if (frente != null && frente.getProducto() != null) {
            estado = "Estado: frente -> " + frente.getProducto().getNombre()
                    + " | prioridad " + frente.getPrioridad()
                    + " | pedidos pendientes: " + colaDespacho.contarElementos();
        }

        lblEstado.setText(estado);
    }

    // Limpia los campos del formulario.
    private void limpiarCampos() {
        campoCodigo.setText("");
        campoNombre.setText("");
        campoPrecio.setText("");
        campoCantidad.setText("1");
        comboPrioridad.setSelectedIndex(0);
    }

    // Carga pedidos de ejemplo para mostrar la cola desde el inicio.
    private void cargarPedidosPrecargados() {
        Producto[] productos = DatosPrecargados.obtenerProductosBase();

        for (int i = 0; i < productos.length; i++) {
            int prioridad = (i % 3) + 1;
            colaDespacho.encolar(productos[i], 1, prioridad);
        }
    }
}
