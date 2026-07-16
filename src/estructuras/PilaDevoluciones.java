package estructuras;

import modelo.Producto;
import modelo.cPila;

import java.util.ArrayList;
import java.util.List;

public class PilaDevoluciones {
    private cPila tope;

    // Constructor:
    public PilaDevoluciones() {
        this.tope = null;
    }

    // Método apilar
    public void apilar(Producto producto) {
        cPila nuevo = new cPila(producto);
        if (tope == null) {
            tope = nuevo;
        } else {
            nuevo.setSgte(tope);
            tope = nuevo;
        }
    }

    // Método desapilar
    public Producto desapilar() {
        Producto producto = null;

        if (tope != null) {
            producto = tope.getProducto();
            tope = tope.getSgte();
        }
        return producto;
    }

    // Método accesar:
    public Producto accesar() {
        Producto producto = null;

        if (tope != null) {
           producto = tope.getProducto();
        }
        return producto;
    }

    // Devuelve una representación tabular de la pila, desde el tope hacia abajo.
    public Object[][] obtenerFilas() {
    List<Object[]> filas = new ArrayList<>();
    cPila actual = tope;

    while (actual != null) {
        Producto producto = actual.getProducto();

        String codigo;
        String nombre;
        double precio;
        int stock;

        if (producto != null) {
            codigo = producto.getCodigo();
            nombre = producto.getNombre();
            precio = producto.getPrecio();
            stock = producto.getStock();
        } else {
            codigo = "";
            nombre = "";
            precio = 0.0;
            stock = 0;
        }

        filas.add(new Object[] { codigo, nombre, precio, stock });

        actual = actual.getSgte();
    }

    return filas.toArray(new Object[0][]);
}

    // Indica cuántas devoluciones están pendientes.
    public int contarDevoluciones() {
        int cantidad = 0;
        cPila actual = tope;

        while (actual != null) {
            cantidad++;
            actual = actual.getSgte();
        }

        return cantidad;
    }

    // Describe el tope actual para la interfaz gráfica.
    public String describirTope() {
        String texto = "No hay devoluciones pendientes.";

        if (tope != null && tope.getProducto() != null) {
            texto = "Tope actual: " + tope.getProducto().toString();
        }

        return texto;
    }

    // Método pilaVacia:
    public boolean pilaVacia() {
        return tope == null;
    }

    // Método mostrarPila:
    public void mostrarPila() {
        cPila p = tope;
        while (p != null) {
            p = p.getSgte();
        }
    }
}
