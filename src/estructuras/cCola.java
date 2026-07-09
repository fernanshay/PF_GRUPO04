package estructuras;

import modelo.NodoCola;
import modelo.Producto;

import java.util.ArrayList;
import java.util.List;

// Cola priorizada para el módulo de despacho.
public class cCola {
    private NodoCola frente;

    // Inicializa la cola vacía.
    public cCola() {
        this.frente = null;
    }

    // Indica si la cola no tiene pedidos.
    public boolean estaVacia() {
        return frente == null;
    }

    // Inserta un pedido respetando la prioridad: 1 es la más alta.
    public boolean encolar(Producto producto, int cantidad, int prioridad) {
        boolean insertado = false;

        if (producto != null && cantidad > 0 && prioridad > 0) {
            Producto copia = new Producto(producto.getCodigo(), producto.getNombre(), producto.getPrecio(),
                    producto.getStock());
            NodoCola nuevo = new NodoCola(copia, cantidad, prioridad);

            if (estaVacia()) {
                frente = nuevo;
            } else if (prioridad < frente.getPrioridad()) {
                nuevo.setSgte(frente);
                frente = nuevo;
            } else {
                NodoCola actual = frente;

                while (actual.getSgte() != null && actual.getSgte().getPrioridad() <= prioridad) {
                    actual = actual.getSgte();
                }

                nuevo.setSgte(actual.getSgte());
                actual.setSgte(nuevo);
            }

            insertado = true;
        }

        return insertado;
    }

    // Elimina y devuelve el pedido del frente.
    public NodoCola desencolar() {
        NodoCola eliminado = null;

        if (!estaVacia()) {
            eliminado = frente;
            frente = frente.getSgte();
            eliminado.setSgte(null);
        }

        return eliminado;
    }

    // Devuelve el pedido del frente sin removerlo.
    public NodoCola acceso() {
        return frente;
    }

    // Cuenta los pedidos en cola.
    public int contarElementos() {
        int cantidad = 0;
        NodoCola actual = frente;

        while (actual != null) {
            cantidad++;
            actual = actual.getSgte();
        }

        return cantidad;
    }

    // Devuelve las filas listas para una tabla Swing.
    public Object[][] obtenerFilas() {
        List<Object[]> filas = new ArrayList<>();
        NodoCola actual = frente;

        while (actual != null) {
            Producto producto = actual.getProducto();
            filas.add(new Object[] {
                    actual.getPrioridad(),
                    producto != null ? producto.getCodigo() : "",
                    producto != null ? producto.getNombre() : "",
                    producto != null ? producto.getPrecio() : 0.0,
                    actual.getCantidad(),
                    actual.getSubtotal()
            });
            actual = actual.getSgte();
        }

        return filas.toArray(new Object[0][]);
    }

    // Devuelve un texto legible con el contenido de la cola.
    public String mostrarCola() {
        StringBuilder texto = new StringBuilder();
        NodoCola actual = frente;

        if (actual == null) {
            texto.append("No hay pedidos en despacho.");
        } else {
            while (actual != null) {
                Producto producto = actual.getProducto();
                texto.append("[Prioridad ").append(actual.getPrioridad()).append("] ");

                if (producto != null) {
                    texto.append(producto.getCodigo()).append(" - ").append(producto.getNombre());
                }

                texto.append(" x").append(actual.getCantidad())
                        .append(" => S/ ")
                        .append(String.format(java.util.Locale.US, "%.2f", actual.getSubtotal()))
                        .append(System.lineSeparator());
                actual = actual.getSgte();
            }
        }

        return texto.toString();
    }
}