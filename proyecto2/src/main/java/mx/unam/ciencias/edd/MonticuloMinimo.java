package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para montículos mínimos (<i>min heaps</i>).
 */
public class MonticuloMinimo<T extends ComparableIndexable<T>>
    implements Coleccion<T>, MonticuloDijkstra<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Índice del iterador. */
        private int indice;

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return indice < elementos;
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            if (hasNext())
                return arbol[indice++];
            throw new NoSuchElementException();
        }
    }

    /* Clase estática privada para adaptadores. */
    private static class Adaptador<T  extends Comparable<T>>
        implements ComparableIndexable<Adaptador<T>> {

        /* El elemento. */
        private T elemento;
        /* El índice. */
        private int indice;

        /* Crea un nuevo comparable indexable. */
        public Adaptador(T elemento) {
            this.elemento = elemento;
            this.indice = -1;
        }

        /* Regresa el índice. */
        @Override public int getIndice() {
            return indice;
        }

        /* Define el índice. */
        @Override public void setIndice(int indice) {
            this.indice = indice;
        }

        /* Compara un adaptador con otro. */
        @Override public int compareTo(Adaptador<T> adaptador) {
            return elemento.compareTo(adaptador.elemento);
        }
    }

    /* El número de elementos en el arreglo. */
    private int elementos;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arbol;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] nuevoArreglo(int n) {
        return (T[])(new ComparableIndexable[n]);
    }

    /**
     * Constructor sin parámetros. Es más eficiente usar {@link
     * #MonticuloMinimo(Coleccion)} o {@link #MonticuloMinimo(Iterable,int)},
     * pero se ofrece este constructor por completez.
     */
    public MonticuloMinimo() {
        arbol = nuevoArreglo(100);      
    }

    /**
     * Constructor para montículo mínimo que recibe una colección. Es más barato
     * construir un montículo con todos sus elementos de antemano (tiempo
     * <i>O</i>(<i>n</i>)), que el insertándolos uno por uno (tiempo
     * <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param coleccion la colección a partir de la cuál queremos construir el
     *                  montículo.
     */
    public MonticuloMinimo(Coleccion<T> coleccion) {
        this(coleccion, coleccion.getElementos());
    }

    /**
     * Constructor para montículo mínimo que recibe un iterable y el número de
     * elementos en el mismo. Es más barato construir un montículo con todos sus
     * elementos de antemano (tiempo <i>O</i>(<i>n</i>)), que el insertándolos
     * uno por uno (tiempo <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param iterable el iterable a partir de la cuál queremos construir el
     *                 montículo.
     * @param n el número de elementos en el iterable.
     */
    public MonticuloMinimo(Iterable<T> iterable, int n) {
        int i = 0;
        arbol = nuevoArreglo(n);
        elementos = n;
        for(T elemento : iterable){
            arbol[i] = elemento;
            elemento.setIndice(i);
            i++;
        }
        for(int indice = ((n/2)-1); indice >= 0; indice--){
            acomodaAbajo(indice);
        }
    }


    private void acomodaAbajo(int x) {
        int minimo = x;
        int i = x * 2 + 1, d = x * 2 + 2;
        boolean ii = !(i < 0 ||  i >= elementos);
        boolean dd = !(d < 0 ||  d >= elementos);
        if (ii  && arbol[i].compareTo(arbol[minimo]) < 0)
            minimo = i;
        if (dd  && arbol[d].compareTo(arbol[minimo]) < 0)
            minimo = d;
        if(minimo != x){
            swap(minimo, x);
            acomodaAbajo(minimo);
        }
    }
    


    private void acomodaArriba(int x) {
        if (x < 0 || x >= elementos)
          return;
        int p = (x - 1) / 2;
        if (p < 0)
          return;
        T v = arbol[x];
        T padre = arbol[p];
        if (padre.compareTo(v) > 0) {
          swap(x, p);
          acomodaArriba(p);
        }
      }

    private void swap(int i, int j){
        T aux = arbol[i];
        arbol[i] = arbol[j];
        arbol[i].setIndice(i);
        arbol[j] = aux;
        arbol[j].setIndice(j); 
    }


    /**
     * Agrega un nuevo elemento en el montículo.
     * @param elemento el elemento a agregar en el montículo.
     */
    @Override public void agrega(T elemento) {
        if(elementos == arbol.length){
            T nuevo[] = nuevoArreglo(arbol.length * 2);
            for(int i = 0;i < elementos; i++){
                nuevo[i] = arbol[i];
            }
            arbol = nuevo;
        }
        arbol[elementos] = elemento;
        elemento.setIndice(elementos);
        elementos++;
        acomodaArriba(elementos-1);
    }

    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    @Override public T elimina() {
        if(esVacia())
            throw new IllegalStateException();
        T raiz = arbol[0];
        swap(0, elementos-1);
        elementos--;
        acomodaAbajo(0);
        raiz.setIndice(-1);
        return raiz;
    }

    /**
     * Elimina un elemento del montículo.
     * @param elemento a eliminar del montículo.
     */
    @Override public void elimina(T elemento) {
        int i = elemento.getIndice();
        if(i < 0 || i >= elementos)
            return;
        T e = arbol[i];
        swap(i, elementos-1);
        elementos--;
        arbol[elementos] = null;
        reordena(arbol[i]);
        e.setIndice(-1);
    }

    /**
     * Nos dice si un elemento está contenido en el montículo.
     * @param elemento el elemento que queremos saber si está contenido.
     * @return <code>true</code> si el elemento está contenido,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        int i = elemento.getIndice();
        if(i < 0 || i >= elementos)
            return false;
        return arbol[i].compareTo(elemento) == 0 ? true : false;
    }

    /**
     * Nos dice si el montículo es vacío.
     * @return <code>true</code> si ya no hay elementos en el montículo,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean esVacia() {
        return elementos == 0;
    }

    /**
     * Limpia el montículo de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        elementos = 0;
    }

   /**
     * Reordena un elemento en el árbol.
     * @param elemento el elemento que hay que reordenar.
     */
    @Override public void reordena(T elemento) {
        if(elemento == null)
            return;
        // segun el libro de canek esto funciona
        acomodaAbajo(elemento.getIndice());
        acomodaArriba(elemento.getIndice());
    }

    /**
     * Regresa el número de elementos en el montículo mínimo.
     * @return el número de elementos en el montículo mínimo.
     */
    @Override public int getElementos() {
        return elementos;
    }

    /**
     * Regresa el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @param i el índice del elemento que queremos, en <em>in-order</em>.
     * @return el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @throws NoSuchElementException si i es menor que cero, o mayor o igual
     *         que el número de elementos.
     */
    @Override public T get(int i) {
        if(i < 0 || i >= elementos)
            throw new NoSuchElementException();
        return arbol[i];
    }

    /**
     * Regresa una representación en cadena del montículo mínimo.
     * @return una representación en cadena del montículo mínimo.
     */
    @Override public String toString() {
        String s = "";
        for(T elemento : arbol){
            s += String.format("%s, ", elemento.toString());
        }
        return s;
    }

    /**
     * Nos dice si el montículo mínimo es igual al objeto recibido.
     * @param objeto el objeto con el que queremos comparar el montículo mínimo.
     * @return <code>true</code> si el objeto recibido es un montículo mínimo
     *         igual al que llama el método; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") MonticuloMinimo<T> monticulo =
            (MonticuloMinimo<T>)objeto;
            int i = 0;
            if(monticulo.getElementos() != elementos)
                return false;
            for(T elemento : arbol){
                if(!elemento.equals(monticulo.get(i++)))
                    return false;
            }
            return true;
    }

    /**
     * Regresa un iterador para iterar el montículo mínimo. El montículo se
     * itera en orden BFS.
     * @return un iterador para iterar el montículo mínimo.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Ordena la colección usando HeapSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param coleccion la colección a ordenar.
     * @return una lista ordenada con los elementos de la colección.
     */
    public static <T extends Comparable<T>> Lista<T> heapSort(Coleccion<T> coleccion) {
        Lista<Adaptador<T>> lista = new Lista<Adaptador<T>>();
        for(T elemento : coleccion){
            lista.agrega(new Adaptador<T>(elemento));
        }
        Lista<T> lesta = new Lista<>();
        MonticuloMinimo<Adaptador<T>> heap = new MonticuloMinimo<Adaptador<T>>(lista);
        while(!heap.esVacia())
            lesta.agrega(heap.elimina().elemento);
        return lesta;
    }
}
