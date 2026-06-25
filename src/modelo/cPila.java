package modelo;

// (Contiene un Producto y el puntero siguiente)
public class cPila {
    private Producto producto;
    private cPila sgte;

    // Constructor:
    public cPila(Producto producto) {
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

    public cPila getSgte() {
        return sgte;
    }

    public void setSgte(cPila sgte) {
        this.sgte = sgte;
    }
}
