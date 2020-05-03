package mx.unam.ciencias.edd;

/**
 * Clase para colas genéricas.
 */
public class Cola<T> extends MeteSaca<T> {

    /**
     * Regresa una representación en cadena de la cola.
     * @return una representación en cadena de la cola.
     */
    @Override public String toString() {
        if(esVacia())
          return "";
        Nodo n = cabeza;
        String s = "";
        while(n != null) {
          s += n.elemento.toString();
          s += ",";
          n = n.siguiente;
        }
        return s;
    }

    /**
     * Agrega un elemento al final de la cola.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void mete(T elemento) {
      if (elemento == null)
        throw new IllegalArgumentException("El elemento a agregar " +
                                            "en la cola no puede ser null.");
      Nodo nuevo = new Nodo(elemento);
      if (esVacia()) {
        cabeza = rabo = nuevo;
      } else {
        rabo.siguiente = nuevo;
        rabo = rabo.siguiente;
      }
    }
}
