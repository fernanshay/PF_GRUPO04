package modelo;

// Nodo de una cola priorizada de despacho.
public class NodoCola {
    private Producto producto;
    private int cantidad;
    private int prioridad;
    private NodoCola sgte;

    // Constructor.
    public NodoCola(Producto producto, int cantidad, int prioridad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.prioridad = prioridad;
        this.sgte = null;
    }

    // Getters y setters.
    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public NodoCola getSgte() {
        return sgte;
    }

    public void setSgte(NodoCola sgte) {
        this.sgte = sgte;
    }

    public double getSubtotal() {
        double subtotal = 0.0;

        if (producto != null) {
            subtotal = producto.getPrecio() * cantidad;
        }

        return subtotal;
    }
}
