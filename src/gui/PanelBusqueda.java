package gui;
// Formulario Swing para el ABB

import javax.swing.*;
import java.awt.*;

import estructuras.cABB;
import modelo.DatosPrecargados;
import modelo.Producto;

// Panel Swing para administrar el árbol ABB de productos (ordenado por nombre).
public class PanelBusqueda extends JPanel {

    // El árbol ABB.
    private cABB arbol;

    // Campos de entrada del formulario.
    private JTextField campoCodigo;
    private JTextField campoNombre;
    private JTextField campoPrecio;
    private JTextField campoStock;

    // Campo para buscar por nombre.
    private JTextField campoBuscar;

    // Área donde se muestra el árbol y los resultados.
    private JTextArea areaResultado;

    // Contador para generar código automático si se deja vacío.
    private int contadorCodigo = 1;

    // Crea el panel y prepara el formulario, los botones y el área de resultados.
    public PanelBusqueda() {
        arbol = new cABB();
        setLayout(new BorderLayout());

        // --- Panel superior: formulario de entrada ---
        JPanel panelFormulario = new JPanel();
        campoCodigo = new JTextField(8);
        campoNombre = new JTextField(15);
        campoPrecio = new JTextField(8);
        campoStock = new JTextField(5);
        JButton btnInsertar = new JButton("Insertar Nodo");

        panelFormulario.add(new JLabel("Código:"));
        panelFormulario.add(campoCodigo);
        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(campoNombre);
        panelFormulario.add(new JLabel("Precio:"));
        panelFormulario.add(campoPrecio);
        panelFormulario.add(new JLabel("Stock:"));
        panelFormulario.add(campoStock);
        panelFormulario.add(btnInsertar);

        // --- Panel de búsquedas y otras operaciones ---
        JPanel panelOperaciones = new JPanel();
        campoBuscar = new JTextField(12);
        JButton btnBuscarSecuencial = new JButton("Búsqueda Secuencial");
        JButton btnBuscarIndexada = new JButton("Búsqueda Indexada");
        JButton btnEliminar = new JButton("Eliminar Nodo");
        JButton btnMostrarArbol = new JButton("Mostrar Árbol (Pre/In/Post)");

        panelOperaciones.add(new JLabel("Buscar por nombre:"));
        panelOperaciones.add(campoBuscar);
        panelOperaciones.add(btnBuscarSecuencial);
        panelOperaciones.add(btnBuscarIndexada);
        panelOperaciones.add(btnEliminar);
        panelOperaciones.add(btnMostrarArbol);

        JPanel panelSuperior = new JPanel(new GridLayout(2, 1));
        panelSuperior.add(panelFormulario);
        panelSuperior.add(panelOperaciones);

        // --- Panel central: área de texto con el resultado / recorridos ---
        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        areaResultado.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaResultado.setLineWrap(true);
        areaResultado.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(areaResultado);

        add(panelSuperior, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        // --- Eventos de los botones ---
        btnInsertar.addActionListener(e -> insertarProducto());
        btnBuscarSecuencial.addActionListener(e -> buscarSecuencial());
        btnBuscarIndexada.addActionListener(e -> buscarIndexada());
        btnEliminar.addActionListener(e -> eliminarProducto());
        btnMostrarArbol.addActionListener(e -> mostrarArbol());

        // Carga productos de ejemplo para la exposición.
        cargarDatosPrecargados();
        mostrarArbol();
    }

    // Inserta un nuevo producto (nodo) en el árbol.
    private void insertarProducto() {
        try {
            String nombre = campoNombre.getText().trim();
            double precio = Double.parseDouble(campoPrecio.getText());
            int stock = Integer.parseInt(campoStock.getText());

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "El nombre no puede estar vacío.",
                        "Nombre inválido",
                        JOptionPane.ERROR_MESSAGE);
            } else if (arbol.busquedaIndexada(nombre) != null) {
                JOptionPane.showMessageDialog(this,
                        "Ya existe un producto con ese nombre en el árbol.",
                        "Nombre duplicado",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                String codigo = campoCodigo.getText().trim();
                if (codigo.isEmpty()) {
                    codigo = "P" + String.format("%03d", contadorCodigo);
                    contadorCodigo++;
                }

                arbol.insertaNodo(new Producto(codigo, nombre, precio, stock));
                limpiarCampos();
                mostrarArbol();
                JOptionPane.showMessageDialog(this, "Producto insertado exitosamente en el árbol.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Precio y Stock deben ser numéricos.",
                    "Error de datos",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Elimina un nodo del árbol según el nombre ingresado.
    private void eliminarProducto() {
        String nombre = campoNombre.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Escribe el nombre del producto a eliminar.",
                    "Sin nombre",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            boolean eliminado = arbol.eliminaNodo(nombre);

            if (eliminado) {
                limpiarCampos();
                mostrarArbol();
                JOptionPane.showMessageDialog(this, "Producto eliminado exitosamente del árbol.");
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se encontró un producto con ese nombre en el árbol.",
                        "Producto no encontrado",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    // Búsqueda Secuencial: recorre todos los nodos uno por uno.
    private void buscarSecuencial() {
        String nombre = campoBuscar.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Escribe un nombre para buscar.",
                    "Sin nombre",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            long inicio = System.nanoTime();
            Producto encontrado = arbol.busquedaSecuencial(nombre);
            long fin = System.nanoTime();

            String resultado = "=== BÚSQUEDA SECUENCIAL ===\n"
                    + "Recorre todos los nodos del árbol uno por uno hasta encontrarlo.\n\n";

            if (encontrado != null) {
                resultado += "Encontrado: " + encontrado + "\n";
            } else {
                resultado += "No se encontró ningún producto con el nombre \"" + nombre + "\".\n";
            }
            resultado += "Tiempo de búsqueda: " + (fin - inicio) + " ns";

            areaResultado.setText(resultado);
        }
    }

    // Búsqueda Indexada: aprovecha la estructura del ABB (izquierda/derecha).
    private void buscarIndexada() {
        String nombre = campoBuscar.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Escribe un nombre para buscar.",
                    "Sin nombre",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            long inicio = System.nanoTime();
            Producto encontrado = arbol.busquedaIndexada(nombre);
            long fin = System.nanoTime();

            String resultado = "=== BÚSQUEDA INDEXADA (ABB) ===\n"
                    + "Usa el orden del árbol: va a la izquierda o derecha según el nombre.\n\n";

            if (encontrado != null) {
                resultado += "Encontrado: " + encontrado + "\n";
            } else {
                resultado += "No se encontró ningún producto con el nombre \"" + nombre + "\".\n";
            }
            resultado += "Tiempo de búsqueda: " + (fin - inicio) + " ns";

            areaResultado.setText(resultado);
        }
    }

    // Muestra el árbol completo con los 3 recorridos: PreOrden, InOrden, PostOrden.
    private void mostrarArbol() {
        if (arbol.estaVacio()) {
            areaResultado.setText("El árbol está vacío.");
        } else {
            String texto = "=== RECORRIDOS DEL ÁRBOL ABB ===\n\n"
                    + "PreOrden:\n" + arbol.muestra(1) + "\n\n"// Modificar título. Por ejemplo: "Raíz, izq., der. (Pre-orden)"
                    + "InOrden:\n" + arbol.muestra(2) + "\n\n" // Modificar título. Por ejemplo: "De menor a mayor (In-orden)"
                    + "PostOrden:\n" + arbol.muestra(3); // Modificar título: Por ejemplo: "Izq., der., raíz (Porst-orden)"
            areaResultado.setText(texto);
        }
    }

    // Limpia los campos del formulario.
    private void limpiarCampos() {
        campoCodigo.setText("");
        campoNombre.setText("");
        campoPrecio.setText("");
        campoStock.setText("");
    }

    // Carga productos de ejemplo para que el árbol no inicie vacío.
    private void cargarDatosPrecargados() {
        Producto[] productos = DatosPrecargados.obtenerProductosBase();

        for (Producto producto : productos) {
            arbol.insertaNodo(producto);
        }

        contadorCodigo = productos.length + 1;
    }
}
