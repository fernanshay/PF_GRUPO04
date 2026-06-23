package modelo;

public class Producto {
    private String codigo;
    private String nombre;
    private double precio;
    private int stock;

    // Constructor:
    public Producto(String codigo, String nombre, double precio, int stock) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    // Getters y Setters:
    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    // Print producto como texto legible:
    @Override
    public String toString() {
        return "[" + codigo + "]" + nombre + " - S/" + precio + " (Stock: " + stock + ")";
    }
}