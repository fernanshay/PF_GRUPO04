package estructuras;

// Inserción y búsquedas recursivas por String
// Trabajar con precios*

import modelo.NodoArbol;
import modelo.Producto;

// Inserción y búsquedas recursivas por String (nombre del producto)

public class cABB {
    private NodoArbol raiz, nuevo, p;

    public void insertaNodo(Producto producto) {
        nuevo = new NodoArbol(producto);
        if (raiz == null)
            raiz = nuevo;
        else {
            p = raiz;
            insertaNodoR(p, nuevo);
        }
    }

    public void insertaNodoR(NodoArbol p, NodoArbol nuevo) {
        if (p == null)
            p = nuevo;
        else {
            if (nuevo.getValor().compareTo(p.getValor()) < 0) {
                if (p.getIzq() == null)
                    p.setIzq(nuevo);
                else
                    insertaNodoR(p.getIzq(), nuevo);
            } else {
                if (p.getDer() == null)
                    p.setDer(nuevo);
                else
                    insertaNodoR(p.getDer(), nuevo);
            }
        }
    }

    // Búsqueda Secuencial: recorre todos los nodos uno por uno (no aprovecha el orden del árbol)
    public Producto busquedaSecuencial(String nombre) {
        return busSecR(raiz, nombre.toUpperCase());
    }

    private Producto busSecR(NodoArbol p, String clave) {
        Producto encontrado = null;
        if (p != null) {
            encontrado = busSecR(p.getIzq(), clave);
            if (encontrado == null) {
                if (p.getValor().equals(clave))
                    encontrado = p.getProducto();
                else
                    encontrado = busSecR(p.getDer(), clave);
            }
        }
        return encontrado;
    }

    // Búsqueda Indexada: aprovecha el orden del árbol (izquierda/derecha según compareTo)
    public Producto busquedaIndexada(String nombre) {
        return busIdxR(raiz, nombre.toUpperCase());
    }

    private Producto busIdxR(NodoArbol p, String clave) {
        Producto encontrado = null;
        if (p != null) {
            int cmp = clave.compareTo(p.getValor());
            if (cmp == 0)
                encontrado = p.getProducto();
            else if (cmp < 0)
                encontrado = busIdxR(p.getIzq(), clave);
            else
                encontrado = busIdxR(p.getDer(), clave);
        }
        return encontrado;
    }

    // Elimina el nodo cuyo producto tiene el nombre indicado
    public boolean eliminaNodo(String nombre) {
        boolean eliminado = busquedaIndexada(nombre) != null;
        if (eliminado)
            raiz = eliminaNodoR(raiz, nombre.toUpperCase());
        return eliminado;
    }

    private NodoArbol eliminaNodoR(NodoArbol p, String clave) {
        if (p != null) {
            int cmp = clave.compareTo(p.getValor());
            if (cmp < 0) {
                p.setIzq(eliminaNodoR(p.getIzq(), clave));
            } else if (cmp > 0) {
                p.setDer(eliminaNodoR(p.getDer(), clave));
            } else {
                if (p.getIzq() == null && p.getDer() == null) {
                    p = null;
                } else if (p.getIzq() == null) {
                    p = p.getDer();
                } else if (p.getDer() == null) {
                    p = p.getIzq();
                } else {
                    NodoArbol sucesor = minimoNodo(p.getDer());
                    p.setProducto(sucesor.getProducto());
                    p.setDer(eliminaNodoR(p.getDer(), sucesor.getValor()));
                }
            }
        }
        return p;
    }

    private NodoArbol minimoNodo(NodoArbol p) {
        NodoArbol actual = p;
        while (actual.getIzq() != null)
            actual = actual.getIzq();
        return actual;
    }

    public String muestra(int tipo) {
        String cadena = "";
        switch (tipo) {
            case 1: cadena = preOrden(raiz);  break;
            case 2: cadena = inOrden(raiz);   break;
            case 3: cadena = postOrden(raiz); break;
        }
        return cadena;
    }

    public String preOrden(NodoArbol p) {
        String cadena = "";
        if (p != null) {
            cadena = cadena + p.getProducto() + " - ";
            cadena = cadena + preOrden(p.getIzq());
            cadena = cadena + preOrden(p.getDer());
        }
        return cadena;
    }

    public String inOrden(NodoArbol p) {
        String cadena = "";
        if (p != null) {
            cadena = cadena + inOrden(p.getIzq());
            cadena = cadena + p.getProducto() + " - ";
            cadena = cadena + inOrden(p.getDer());
        }
        return cadena;
    }

    public String postOrden(NodoArbol p) {
        String cadena = "";
        if (p != null) {
            cadena = cadena + postOrden(p.getIzq());
            cadena = cadena + postOrden(p.getDer());
            cadena = cadena + p.getProducto() + " - ";
        }
        return cadena;
    }

    public boolean estaVacio() {
        return raiz == null;
    }
}
