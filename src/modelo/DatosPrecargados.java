package modelo;

// Fuente única de datos de ejemplo para las pantallas del proyecto.
public final class DatosPrecargados {
    private DatosPrecargados() {
    }

    // Devuelve una copia de los productos base para que cada panel use sus propios
    // objetos.
    public static Producto[] obtenerProductosBase() {
        return new Producto[] {
                new Producto("P001", "Arroz", 3.50, 100),
                new Producto("P002", "Leche Gloria", 4.20, 80),
                new Producto("P003", "Aceite Primor", 8.90, 50),
                new Producto("P004", "Fideos Don Vittorio", 2.80, 120),
                new Producto("P005", "Jabón Bolívar", 2.10, 60),
                new Producto("P006", "Yogurt Laive", 5.50, 40),
                new Producto("P007", "Detergente Ariel", 12.90, 30)
        };
    }
}