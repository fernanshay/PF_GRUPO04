package estructuras;

// Lógica de ordenamiento por prioridad al insertar

public class cCola {
    private int cola[];
    private int first, last, size;

    public cCola(int n) {
        cola = new int[n];
        size = n;
        colaVacia();
    }

    public void colaVacia() {
        first = -1; last = -1;
    }

    public void incluirElemento(int valor) {
        if (last < size - 1) {
            last++;
            cola[last] = valor;

            if (first == -1) {
                first = 0;
            }
        }
    }

    public int eliminaElemento() {
        int valor = 0;

        if (first >= 0) {
            valor = cola[first];
            first++;

            if (first > last) {
                colaVacia();
            }
        }
        return valor;
    }
    
    public int acceso() {
        int valor = 0;

        if (first >= 0) {
            valor = cola[first];

            if (first > last) {
                colaVacia();
            }
        }
        return valor;
    }

    public String recorreArreglo() {
        String cadena =  "";

        if (first >= 0) {
            for (int i = first; i < last; i++) {
                cadena += cola[i] + " - ";
            }
        }
        return cadena;
    }
}   