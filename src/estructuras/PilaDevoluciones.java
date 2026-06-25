package estructuras;

import modelo.Producto;
import modelo.cPila;

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
        System.out.println("Devolución registrada: " + producto.getNombre());
    }

    // Método desapilar
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

    // Método accesar: 
    public Producto accesar() {
        if (tope == null) {
            System.out.println("No hay devoluciones pendientes.");
            return null;
        }
        return tope.getProducto();
    }

    // Método pilaVacia: 
    public boolean pilaVacia() {
        return tope == null;
    }

    // Método mostrarPila: 
    public void mostrarPila() {
        if (tope == null) {
            System.out.println("No hay devoluciones registradas.");
            return;
        }
        cPila p = tope;
        System.out.println("Devoluciones pendientes (última primero):");
        while (p != null) {
            System.out.println(p.getProducto().toString());
            p = p.getSgte();
        }
    }
}
