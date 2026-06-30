package modelo;

// Contiene un Producto, hijoIzquierdo e hijoDerecho

public class NodoArbol {
        private Producto producto;
    private NodoArbol der, izq;
 
    public NodoArbol(Producto producto) {
        this.producto = producto;
    }
 
    public Producto getProducto() {
        return producto;
    }
    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    public NodoArbol getDer() {
        return der;
    }
    public void setDer(NodoArbol der) {
        this.der = der;
    }
    public NodoArbol getIzq() {
        return izq;
    }
    public void setIzq(NodoArbol izq) {
        this.izq = izq;
    }
 
    // Clave de comparación del árbol: nombre del producto en mayúsculas
    public String getValor() {
        return producto.getNombre().toUpperCase();
    }
}
