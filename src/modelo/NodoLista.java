package modelo;

// Nodo simple para cada item del carrito.
public class NodoLista {
    private Producto producto;
    private int cantidad;
    private NodoLista sgte;

    // Crea un nodo con producto y cantidad inicial.
    public NodoLista(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.sgte = null;
    }

    // Devuelve el producto guardado.
    public Producto getProducto() {
        return producto;
    }

    // Reemplaza el producto del nodo.
    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    // Devuelve la cantidad del item.
    public int getCantidad() {
        return cantidad;
    }

    // Cambia la cantidad del item.
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    // Devuelve el siguiente nodo.
    public NodoLista getSgte() {
        return sgte;
    }

    // Enlaza el siguiente nodo.
    public void setSgte(NodoLista sgte) {
        this.sgte = sgte;
    }

    // Calcula el subtotal del item.
    public double getSubtotal() {
        double subtotal = 0.0;

        if (producto != null) {
            subtotal = producto.getPrecio() * cantidad;
        }

        return subtotal;
    }
}