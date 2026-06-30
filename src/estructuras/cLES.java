package estructuras;

import modelo.Producto;
import modelo.NodoLista;

import java.util.ArrayList;
import java.util.List;

// Lista enlazada simple que guarda el carrito de compras.
public class cLES {
    private NodoLista inicio;

    // Inicializa el carrito vacío.
    public cLES() {
        this.inicio = null;
    }

    // Agrega un producto al final o suma cantidad si ya existe.
    public boolean insertarAlCarrito(Producto nProducto, int cantidad) {
        boolean insertado = false;

        if (nProducto != null && cantidad > 0) {
            String codigo = normalizarCodigo(nProducto.getCodigo());

            if (codigo != null) {
                NodoLista existente = buscarNodo(codigo);

                if (existente != null) {
                    existente.setCantidad(existente.getCantidad() + cantidad);
                    insertado = true;
                } else {
                    Producto copia = new Producto(codigo, nProducto.getNombre(), nProducto.getPrecio(),
                            nProducto.getStock());
                    NodoLista nuevo = new NodoLista(copia, cantidad);

                    if (inicio == null) {
                        inicio = nuevo;
                    } else {
                        NodoLista p = inicio;

                        while (p.getSgte() != null) {
                            p = p.getSgte();
                        }

                        p.setSgte(nuevo);
                    }

                    insertado = true;
                }
            }
        }

        return insertado;
    }

    // Elimina un item por código.
    public boolean eliminarProducto(String codigo) {
        boolean eliminado = false;
        String codigoNormalizado = normalizarCodigo(codigo);

        if (codigoNormalizado != null && inicio != null) {
            if (inicio.getProducto().getCodigo().equals(codigoNormalizado)) {
                inicio = inicio.getSgte();
                eliminado = true;
            } else {
                NodoLista p = inicio;
                NodoLista anterior = null;

                while (p != null && !p.getProducto().getCodigo().equals(codigoNormalizado)) {
                    anterior = p;
                    p = p.getSgte();
                }

                if (p != null && anterior != null) {
                    anterior.setSgte(p.getSgte());
                    eliminado = true;
                }
            }
        }

        return eliminado;
    }

    // Cambia la cantidad de un item o lo elimina si queda en cero.
    public boolean actualizarCantidad(String codigo, int nuevaCantidad) {
        boolean actualizado = false;
        String codigoNormalizado = normalizarCodigo(codigo);

        if (codigoNormalizado != null) {
            NodoLista nodo = buscarNodo(codigoNormalizado);

            if (nodo != null) {
                if (nuevaCantidad > 0) {
                    nodo.setCantidad(nuevaCantidad);
                    actualizado = true;
                } else {
                    actualizado = eliminarProducto(codigoNormalizado);
                }
            }
        }

        return actualizado;
    }

    // Verifica si un código ya está en el carrito.
    public boolean existeProducto(String codigo) {
        boolean existe = buscarNodo(codigo) != null;
        return existe;
    }

    // Indica si el carrito no tiene elementos.
    public boolean estaVacio() {
        boolean vacio = inicio == null;
        return vacio;
    }

    // Cuenta los items del carrito.
    public int contarProductos() {
        int cantidad = 0;
        NodoLista p = inicio;

        while (p != null) {
            cantidad++;
            p = p.getSgte();
        }

        return cantidad;
    }

    // Suma los subtotales de todos los items.
    public double calcularImporteTotal() {
        double total = 0.0;
        NodoLista p = inicio;

        while (p != null) {
            total += p.getSubtotal();
            p = p.getSgte();
        }

        return total;
    }

    // Devuelve los datos listos para llenar una tabla Swing.
    public Object[][] obtenerFilas() {
        List<Object[]> filas = new ArrayList<>();
        NodoLista p = inicio;

        while (p != null) {
            filas.add(new Object[] {
                    p.getProducto().getCodigo(),
                    p.getProducto().getNombre(),
                    p.getProducto().getPrecio(),
                    p.getCantidad(),
                    p.getSubtotal()
            });
            p = p.getSgte();
        }

        return filas.toArray(new Object[0][]);
    }

    // Imprime el carrito en consola para pruebas simples.
    public void mostrarCarrito() {
        NodoLista p = inicio;

        if (p == null) {
            System.out.println("El carrito está vacío.");
        } else {
            System.out.println("Carrito de compras:");

            while (p != null) {
                System.out.println(p.getProducto().toString() + " x" + p.getCantidad());
                p = p.getSgte();
            }
        }
    }

    // Busca un nodo por código normalizado.
    private NodoLista buscarNodo(String codigo) {
        NodoLista encontrado = null;
        String codigoNormalizado = normalizarCodigo(codigo);
        NodoLista p = inicio;

        if (codigoNormalizado != null) {
            while (p != null && encontrado == null) {
                if (p.getProducto().getCodigo().equals(codigoNormalizado)) {
                    encontrado = p;
                } else {
                    p = p.getSgte();
                }
            }
        }

        return encontrado;
    }

    // Limpia espacios y valida un código.
    private String normalizarCodigo(String codigo) {
        String codigoNormalizado = null;

        if (codigo != null) {
            codigoNormalizado = codigo.trim();

            if (codigoNormalizado.isEmpty()) {
                codigoNormalizado = null;
            }
        }

        return codigoNormalizado;
    }
}