package estructuras;
import modelo.Producto;
import modelo.cNodo;

public class cLES {
    private cNodo inicio;

    // Constructor:
    public cLES() {
        this.inicio = null;
    }

    // Método de inserción:
    public void insertarAlCarrito(Producto nProducto) {
        cNodo nuevo = new cNodo(nProducto);
        if (inicio == null) {
            inicio = nuevo;
        } else {
            cNodo p = inicio;
            while (p.getSgte() != null) {
                p = p.getSgte();
            }
            p.setSgte(nuevo);
        }
        System.out.println("Producto agregado al carrito exitosamente.");
    }

    // Método para mostrar carrito:
    public void mostrarCarrito() {
        if (inicio == null) {
            System.out.println("El carrito está vacío");
            return;
        }

        cNodo p = inicio;
        System.out.println("Carrito de compras:");
        while (p != null) {
            System.out.println(p.getProducto().toString());
            p = p.getSgte();
        }
    }

    // Método de eliminación
    public void eliminarProducto(String codigo) {
        if (inicio == null) {
            System.out.println("El carrito está vacío.");
            return;
        }

        // Caso 1: Producto es el 1ro de la lista:
        if (inicio.getProducto().getCodigo().equals(codigo)) {
            inicio = inicio.getSgte();
            System.out.println("Producto elimado del carrito");
            return;
        }

        // Caso 2: Producto está en el medio o al final:
        cNodo p = inicio;
        cNodo q = null;
        while (p != null && !p.getProducto().getCodigo().equals(codigo)) {
            q = p;
            p = p.getSgte();
        }

        if (p != null) {
            q.setSgte(q.getSgte());
            System.out.println("Producto elimado del carrito");
        } else {
            System.out.println("El producto no está en el carrito");
        }
    }

    // Método de actualización de datos (por ahora con precio):
    public void actualizarPrecio(String codigo, double nuevoPrecio) {
        cNodo p = inicio;

        while (p != null) {
            if (p.getProducto().getCodigo().equals(codigo)) {
                p.getProducto().setPrecio(nuevoPrecio);
                System.out.println("Precio del producto actualizado exitosamente");
                return;
            }
            p = p.getSgte();
        }
        System.out.println("Producto no encontrado para actualizar precio");
    }
}