package estructuras;

// Inserción y búsquedas recursivas por String
// Trabajar con precios*

public class cABB {
    private cNodo raiz,nuevo,p;
    
    public void insertaNodo(int valor){
        nuevo= new cNodo(valor);
        if(raiz == null)
            raiz = nuevo;
        else{
            p = raiz;
            insertaNodoR(p, nuevo);
        }
    }
    public void insertaNodoR(cNodo p, cNodo nuevo){
        if (p == null)
            p = nuevo;
        else{
            if(nuevo.getValor() < p.getValor()){
                if(p.getIzq() == null)
                    p.setIzq(nuevo);
                else
                    insertaNodoR(p.getIzq(),nuevo);
            }
            else{
                if(p.getDer() == null)
                    p.setDer(nuevo);
                else
                    insertaNodoR(p.getDer(), nuevo);
            }
        }
    }
    public String muestra(int tipo){
        String cadena="";
        switch(tipo){
            case 1:cadena=preOrden(raiz);break;
            case 2:cadena=inOrden(raiz);break;
            case 3:cadena=postOrden(raiz);break;
        }
        return cadena;
    }
    public String preOrden(cNodo p){
        String cadena="";
        if(p != null){
            cadena = cadena + p.getValor()+" - ";
            cadena = cadena + preOrden(p.getIzq());
            cadena = cadena + preOrden(p.getDer());
        }
        return cadena;
    }
    public String inOrden(cNodo p){
        String cadena="";
        if(p != null){
            cadena = cadena + inOrden(p.getIzq());
            cadena = cadena + p.getValor()+" - ";
            cadena = cadena + inOrden(p.getDer());
        }
        return cadena;
    }    
    public String postOrden(cNodo p){
        String cadena="";
        if(p != null){
            cadena = cadena + postOrden(p.getIzq());
            cadena = cadena + postOrden(p.getDer());
            cadena = cadena + p.getValor()+" - ";            
        }
        return cadena;
    }     
}
