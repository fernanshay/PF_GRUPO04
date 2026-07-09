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
    public boolean insertarProducto(Producto nProducto) {
        String codigo = normalizarCodigo(nProducto.getCodigo());
        boolean insertado = false;

        if (codigo != null && existeProducto(codigo) == false && cantidad < catalogo.length) { // Conndiciones: Verifica que el código no esté vacío, no se repita y que aún exista espacio disponible en catalogo
            nProducto.setCodigo(codigo);
            catalogo[cantidad] = nProducto;
            cantidad++;
            insertado = true;
        }
        return insertado;
    }

    public boolean existeProducto(String codigo) {
        int posicion = buscarPosicionProducto(codigo);
        boolean existe = posicion != -1;
        return existe;
    }

    private String normalizarCodigo(String codigo) {
        String codigoNormalizado = null;

        if (codigo == null) {
            codigoNormalizado = null;
        } else {
            codigoNormalizado = codigo.trim();
            if (codigoNormalizado.isEmpty()) {
                codigoNormalizado = null;
            }
        }

        return codigoNormalizado;
    }

    private int buscarPosicionProducto(String codigo) {
        String codigoNormalizado = normalizarCodigo(codigo);
        int posicion = -1;

        if (codigoNormalizado != null) {
            for (int i = 0; i < cantidad; i++) {
                if (catalogo[i].getCodigo().equals(codigoNormalizado)) {
                    posicion = i;
                    break;
                }
            }
        }

        return posicion;
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

    public Producto[] obtenerTodos() {
        Producto[] copia = new Producto[cantidad];
        for (int i = 0; i < cantidad; i++) {
            copia[i] = catalogo[i];
        }
        return copia;
    }

    public boolean eliminarProducto(String codigo) {
        int posicion = buscarPosicionProducto(codigo);
        boolean eliminado = false;

        // Si se encuentra, reordenamos el arreglo
        if (posicion != -1) {
            for (int i = posicion; i < cantidad - 1; i++) {
                catalogo[i] = catalogo[i + 1];
            }
            catalogo[cantidad - 1] = null;
            cantidad--;
            eliminado = true;
        }

        return eliminado;
    }

    public boolean actualizarProducto(String codigo, double nuevoPrecio, int nuevoStock) {
        int posicion = buscarPosicionProducto(codigo);
        boolean actualizado = false;

        // Si lo encontramos, actualizamos sus datos
        if (posicion != -1) {
            catalogo[posicion].setPrecio(nuevoPrecio);
            catalogo[posicion].setStock(nuevoStock);
            actualizado = true;
        }

        return actualizado;
    }
}
