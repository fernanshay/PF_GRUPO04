package estructuras;

import modelo.Producto;
import modelo.NodoPila;

public class PilaDevoluciones {
    private NodoPila tope;

    // Constructor:
    public PilaDevoluciones() {
        this.tope = null;
    }

    // Método apilar (push): registra el ingreso de una devolución al almacén
    public void apilar(Producto producto) {
        NodoPila nuevo = new NodoPila(producto);
        if (tope == null) {
            tope = nuevo;
        } else {
            nuevo.setSgte(tope);
            tope = nuevo;
        }
        System.out.println("Devolución registrada: " + producto.getNombre());
    }

    // Método desapilar (pop): procesa el último artículo devuelto (LIFO)
    public Producto desapilar() {
        if (tope == null) {
            System.out.println("No hay devoluciones pendientes.");
            return null;
        }
        Producto producto = tope.getProducto();
        tope = tope.getSgte();
        System.out.println("Procesando devolución: " + producto.getNombre());
        return producto;
    }

    // Método accesar: consulta el último artículo devuelto sin retirarlo
    public Producto accesar() {
        if (tope == null) {
            System.out.println("No hay devoluciones pendientes.");
            return null;
        }
        return tope.getProducto();
    }

    // Método pilaVacia: verifica si hay devoluciones pendientes
    public boolean pilaVacia() {
        return tope == null;
    }

    // Método mostrarPila: lista todas las devoluciones pendientes de revisar
    public void mostrarPila() {
        if (tope == null) {
            System.out.println("No hay devoluciones registradas.");
            return;
        }
        NodoPila p = tope;
        System.out.println("Devoluciones pendientes (última primero):");
        while (p != null) {
            System.out.println(p.getProducto().toString());
            p = p.getSgte();
        }
    }
}
