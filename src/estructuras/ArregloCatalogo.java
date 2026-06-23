package estructuras;
import modelo.Producto;

public class ArregloCatalogo {
    private Producto[] catalogo;
    private int cantidad;

    // Constructor:
    public ArregloCatalogo(int maxSize) {
        this.catalogo = new Producto[maxSize];
        this.cantidad = 0;
    }

    // Método de inserción de producto:
    public void insertarProducto(Producto nProducto) {
        if (cantidad < catalogo.length) {
            catalogo[cantidad] = nProducto;
            cantidad++;
            System.out.println("Producto insertado exitosamente.");
        } else {
            System.out.println("El producto no puede insertarse. Catálogo lleno");
        }
    }

    // Método para mostrar datos del arreglo:
    public void mostrarArreglo() {
        if (cantidad == 0) {
            System.out.println("No tiene productos registrados.");
            return;
    }

        for (int i = 0; i < cantidad; i++) {
            System.out.println((i + 1) + ". " + catalogo[i]);
        }
    }
}
