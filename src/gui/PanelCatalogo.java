package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import estructuras.ArregloCatalogo;
import modelo.DatosPrecargados;
import modelo.Producto;

// Panel Swing para administrar el catálogo de productos.
public class PanelCatalogo extends JPanel {

    // Datos del catálogo y de la tabla visible.
    private ArregloCatalogo catalogo;
    private DefaultTableModel modeloTabla;
    private JTable tabla;

    // Campos de entrada del formulario.
    private JTextField campoCodigo;
    private JTextField campoNombre;
    private JTextField campoPrecio;
    private JTextField campoStock;
    // Botones de acción del panel.
    private JButton btnEliminar;
    private JButton btnActualizar;
    // Guarda el código del producto seleccionado.
    private String codigoSeleccionado = null; // guarda qué producto se está editando
    // Evita cambios de selección al limpiar el formulario.
    private boolean ignorarCambioSeleccion = false;

    // Crea el panel y prepara la tabla y el formulario.
    public PanelCatalogo() {
        catalogo = new ArregloCatalogo(50); // tamaño máximo del catálogo
        setLayout(new BorderLayout());

        // --- Panel superior: formulario de entrada ---
        JPanel panelFormulario = new JPanel();
        campoCodigo = new JTextField(8);
        campoNombre = new JTextField(15);
        campoPrecio = new JTextField(8);
        campoStock = new JTextField(5);
        JButton btnAgregar = new JButton("Agregar producto");

        panelFormulario.add(new JLabel("Código:"));
        panelFormulario.add(campoCodigo);
        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(campoNombre);
        panelFormulario.add(new JLabel("Precio:"));
        panelFormulario.add(campoPrecio);
        panelFormulario.add(new JLabel("Stock:"));
        panelFormulario.add(campoStock);
        panelFormulario.add(btnAgregar);

        // --- Panel central: la tabla ---
        String[] columnas = { "Código", "Nombre", "Precio", "Stock" };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modeloTabla);
        JScrollPane scroll = new JScrollPane(tabla);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !ignorarCambioSeleccion) {
                cargarProductoEnFormulario();
            }
        });

        add(panelFormulario, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // --- Evento del botón ---
        btnAgregar.addActionListener(e -> agregarProducto());

        btnEliminar = new JButton("Eliminar seleccionado");
        panelFormulario.add(btnEliminar);

        btnEliminar.addActionListener(e -> eliminarProductoSeleccionado());

        btnActualizar = new JButton("Actualizar");
        panelFormulario.add(btnActualizar);

        btnActualizar.addActionListener(e -> actualizarProductoSeleccionado());

        cargarDatosPrecargados();
        actualizarTabla();
    }

    // Agrega un producto nuevo al catálogo.
    private void agregarProducto() {
        try {
            String codigo = campoCodigo.getText().trim();
            String nombre = campoNombre.getText();
            double precio = Double.parseDouble(campoPrecio.getText());
            int stock = Integer.parseInt(campoStock.getText());
            boolean agregarProducto = true;
            String mensaje = null;
            String titulo = null;
            int tipoMensaje = JOptionPane.INFORMATION_MESSAGE;

            if (codigo.isEmpty()) {
                agregarProducto = false;
                mensaje = "El código no puede estar vacío.";
                titulo = "Código inválido";
                tipoMensaje = JOptionPane.ERROR_MESSAGE;
            }

            if (agregarProducto && catalogo.existeProducto(codigo)) {
                agregarProducto = false;
                mensaje = "Ya existe un producto con ese código.";
                titulo = "Código duplicado";
                tipoMensaje = JOptionPane.ERROR_MESSAGE;
            }

            boolean operacionExitosa = false;

            if (agregarProducto) {
                Producto nuevo = new Producto(codigo, nombre, precio, stock);
                operacionExitosa = catalogo.insertarProducto(nuevo);

                if (operacionExitosa) {
                    actualizarTabla();
                    limpiarCampos();
                    mensaje = "Producto insertado exitosamente.";
                    tipoMensaje = JOptionPane.INFORMATION_MESSAGE;
                } else {
                    mensaje = "No se pudo insertar el producto. Catálogo lleno.";
                    titulo = "Error";
                    tipoMensaje = JOptionPane.ERROR_MESSAGE;
                }
            } else {
                if (titulo == null) {
                    titulo = "Error";
                }
            }

            JOptionPane.showMessageDialog(this, mensaje, titulo, tipoMensaje);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Precio y Stock deben ser numéricos.",
                    "Error de datos",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Recarga la tabla con los datos actuales.
    private void actualizarTabla() {
        modeloTabla.setRowCount(0); // limpia la tabla
        for (Producto p : catalogo.obtenerTodos()) {
            modeloTabla.addRow(new Object[] {
                    p.getCodigo(), p.getNombre(), p.getPrecio(), p.getStock()
            });
        }
    }

    // Limpia los campos del formulario.
    private void limpiarCampos() {
        campoCodigo.setText("");
        campoNombre.setText("");
        campoPrecio.setText("");
        campoStock.setText("");
    }

    // Elimina los productos seleccionados de la tabla.
    private void eliminarProductoSeleccionado() {
        int[] filasSeleccionadas = tabla.getSelectedRows();

        if (filasSeleccionadas.length == 0) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona al menos un producto de la tabla.",
                    "Sin selección",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            int eliminados = 0;
            int noEncontrados = 0;

            for (int i = 0; i < filasSeleccionadas.length; i++) {
                int fila = filasSeleccionadas[i];
                String codigo = (String) modeloTabla.getValueAt(fila, 0);
                boolean operacionExitosa = catalogo.eliminarProducto(codigo);

                if (operacionExitosa) {
                    eliminados++;
                } else {
                    noEncontrados++;
                }
            }

            actualizarTabla();
            restaurarFormulario();

            if (eliminados > 0 && noEncontrados == 0) {
                JOptionPane.showMessageDialog(this,
                        eliminados + " producto(s) eliminado(s) exitosamente.");
            } else if (eliminados > 0) {
                JOptionPane.showMessageDialog(this,
                        eliminados + " producto(s) eliminado(s) exitosamente. "
                                + noEncontrados + " no se encontraron en el catálogo.",
                        "Eliminación parcial",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo eliminar el producto porque ya no existe en el catálogo.",
                        "Producto no encontrado",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    // Copia el producto elegido al formulario.
    private void cargarProductoEnFormulario() {
        int fila = tabla.getSelectedRow();

        if (fila != -1) {
            campoCodigo.setText((String) modeloTabla.getValueAt(fila, 0));
            campoNombre.setText((String) modeloTabla.getValueAt(fila, 1));
            campoPrecio.setText(modeloTabla.getValueAt(fila, 2).toString());
            campoStock.setText(modeloTabla.getValueAt(fila, 3).toString());

            campoCodigo.setEditable(false);
            campoNombre.setEditable(false);

            codigoSeleccionado = (String) modeloTabla.getValueAt(fila, 0);
        } else {
            restaurarFormulario();
        }
    }

    // Actualiza el precio y el stock del producto seleccionado.
    private void actualizarProductoSeleccionado() {
        boolean operacionExitosa = false;

        if (codigoSeleccionado == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un producto de la tabla primero.",
                    "Sin selección",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                double nuevoPrecio = Double.parseDouble(campoPrecio.getText());
                int nuevoStock = Integer.parseInt(campoStock.getText());

                operacionExitosa = catalogo.actualizarProducto(codigoSeleccionado, nuevoPrecio, nuevoStock);

                if (operacionExitosa) {
                    actualizarTabla();
                    limpiarCampos();
                    restaurarFormulario();
                    JOptionPane.showMessageDialog(this, "Producto actualizado exitosamente.");
                } else if (!catalogo.existeProducto(codigoSeleccionado)) {
                    restaurarFormulario();
                    JOptionPane.showMessageDialog(this,
                            "No se pudo actualizar porque el producto ya no existe en el catálogo.",
                            "Producto no encontrado",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "No se pudo actualizar el producto.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Precio y Stock deben ser numéricos.",
                        "Error de datos",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Restaura el formulario y limpia la selección.
    private void restaurarFormulario() {
        ignorarCambioSeleccion = true;
        tabla.clearSelection();
        ignorarCambioSeleccion = false;
        limpiarCampos();
        campoCodigo.setEditable(true);
        campoNombre.setEditable(true);
        codigoSeleccionado = null;
    }

    // Carga productos de ejemplo
    private void cargarDatosPrecargados() {
        Producto[] productos = DatosPrecargados.obtenerProductosBase();

        for (Producto producto : productos) {
            catalogo.insertarProducto(producto);
        }
    }
}