package modelo;

// (Contiene un Producto y el puntero siguiente)
public class NodoPila {
    private Producto producto;
    private NodoPila sgte;

    // Constructor:
    public NodoPila(Producto producto) {
        this.producto = producto;
        this.sgte = null;
    }

    // Getters y Setters:
    public Producto getProducto() {
        return producto;
    }
    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    public NodoPila getSgte() {
        return sgte;
    }
    public void setSgte(NodoPila sgte) {
        this.sgte = sgte;
    }
}
