package estructuras;

import modelo.NodoCola;
import modelo.Producto;

import java.util.ArrayList;
import java.util.List;

// Cola priorizada para el módulo de despacho.
public class cCola {
    private NodoCola inicio;

    // Inicializa la cola vacía.
    public cCola() {
        this.inicio = null;
    }

    // Indica si la cola no tiene pedidos.
    public boolean estaVacia() {
        return inicio == null;
    }

    // Inserta un pedido respetando la prioridad: 1 es la más alta.
    public boolean encolar(Producto producto, int cantidad, int prioridad) {
        boolean insertado = false;

        if (producto != null && producto.getCodigo() != null && cantidad > 0 && prioridad > 0
                && !existeProducto(producto.getCodigo())) {
            Producto copia = new Producto(producto.getCodigo(), producto.getNombre(), producto.getPrecio(),
                    producto.getStock());
            NodoCola nuevo = new NodoCola(copia, cantidad, prioridad);

            if (estaVacia()) {
                inicio = nuevo;
            } else if (prioridad < inicio.getPrioridad()) { // NOTA: Evaluar prioridad. Puede que lo mejor sea
                                                            // considerar solo el principio de la cola: First In - First
                                                            // Out
                nuevo.setSgte(inicio);
                inicio = nuevo;
            } else {
                NodoCola actual = inicio;

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

    // Verifica si un producto con el mismo código ya está en la cola.
    public boolean existeProducto(String codigo) {
        boolean existe = false;
        NodoCola actual = inicio;

        while (actual != null && !existe) {
            Producto producto = actual.getProducto();

            if (producto != null && producto.getCodigo() != null && producto.getCodigo().equals(codigo)) {
                existe = true;
            } else {
                actual = actual.getSgte();
            }
        }

        return existe;
    }

    // Elimina y devuelve el pedido del frente.
    public NodoCola desencolar() {
        NodoCola eliminado = null;

        if (!estaVacia()) {
            eliminado = inicio;
            inicio = inicio.getSgte();
            eliminado.setSgte(null);
        }

        return eliminado;
    }

    // Devuelve el pedido del frente sin removerlo.
    public NodoCola acceso() {
        return inicio;
    }

    // Cuenta los pedidos en cola.
    public int contarElementos() {
        int cantidad = 0;
        NodoCola actual = inicio;

        while (actual != null) {
            cantidad++;
            actual = actual.getSgte();
        }

        return cantidad;
    }

    // Devuelve las filas listas para una tabla Swing.
    public Object[][] obtenerFilas() {
        List<Object[]> filas = new ArrayList<>();
        NodoCola actual = inicio;

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
}