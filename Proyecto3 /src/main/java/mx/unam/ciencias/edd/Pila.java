package mx.unam.ciencias.edd;

/**
 * Clase para pilas genéricas.
 */
public class Pila<T> extends MeteSaca<T> {

    /**
     * Regresa una representación en cadena de la pila.
     * @return una representación en cadena de la pila.
     */
    @Override public String toString() {
      if(esVacia())
          return "";
      if(cabeza.siguiente==null){
          return ""+cabeza.elemento+"\n";
      }

      String a = "";
      Nodo b = cabeza;
      while(b!=null){
          a+=b.elemento+"\n";
          b=b.siguiente;
      }
      return a;
    }

    /**
     * Agrega un elemento al tope de la pila.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void mete(T elemento) {
      if(elemento == null)
        throw new IllegalArgumentException("No se pueder mete un elemento " +
                                           "nulo a la pila");
      Nodo n = new Nodo(elemento);
      if (this.esVacia()) {
        cabeza = rabo = n;
      } else {
        n.siguiente = cabeza;
        cabeza = n;
      }
    }
}
