package modelo;

// Contiene un Producto, la prioridad y el puntero 'siguiente'

public class NodoCola {
    private Producto producto;
    private cNodo sgte;

    // Constructor:
    public cNodo(Producto producto) {
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

    public cNodo getSgte() {
        return sgte;
    }

    public void setSgte(cNodo sgte) {
        this.sgte = sgte;
    }    
}
