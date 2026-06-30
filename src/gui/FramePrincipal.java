/**
 * Clase principal donde se inicializa la ventana (frame) principal del programa
 */
package gui;

import javax.swing.*;

import estructuras.cLES;

// Ventana principal que organiza todos los módulos por pestañas.
public class FramePrincipal extends JFrame {
    // Crea el frame y comparte el carrito con su pestaña.
    public FramePrincipal() {
        cLES carrito = new cLES();

        setTitle("Súper Mass - Gestión de Inventario");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);

        JTabbedPane pestanas = new JTabbedPane();

        pestanas.addTab("Catálogo", new PanelCatalogo());
        pestanas.addTab("Carrito", new PanelCarrito(carrito));
        pestanas.addTab("Despacho", new PanelDespacho());
        pestanas.addTab("Devoluciones", new PanelDevoluciones());
        pestanas.addTab("Búsqueda", new PanelBusqueda());

        add(pestanas);
    }

    // Punto de entrada de la aplicación.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FramePrincipal ventana = new FramePrincipal();
            ventana.setVisible(true);
        });
    }
}