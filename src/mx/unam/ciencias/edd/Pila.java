package mx.unam.ciencias.edd;

/**
 * Clase para pilas genéricas.
 */
public class Pila<T> extends MeteSaca<T> {

    /**
     * Agrega un elemento al tope de la pila.
     * @param elemento el elemento a agregar.
     */
    @Override public void mete(T elemento) {
        Nodo<T> nodo = new Nodo<T>(elemento);
        if (rabo == null) {
            cabeza = rabo = nodo;
        } else {
            nodo.siguiente = cabeza;
            cabeza = nodo;
        }
        elementos++;
    }
    
    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
        return toStringR(cabeza);
    }
    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
     private String toStringR(Nodo<T> t) {
        if(t==null)
            return "";
            
        return t.elemento.toString()+toStringR(t.siguiente);
        
    }


}
