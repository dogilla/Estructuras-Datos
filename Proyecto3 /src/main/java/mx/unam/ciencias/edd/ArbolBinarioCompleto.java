package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Cola para recorrer los vértices en BFS. */
        private Cola<Vertice> cola;

        /* Inicializa al iterador. */
        public Iterador() {
            /* creamos una cola y metemos la raiz si no es vacia*/
            this.cola = new Cola<Vertice>();
            if(raiz != null){
                cola.mete(raiz);
            }
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return !cola.esVacia();
        }

        /* Regresa el siguiente elemento en orden BFS. */
        @Override public T next() {
            if(cola.esVacia())
                throw new NoSuchElementException();
            /* como es una arbol completo, si hay derecho debe haber izquierdo */
            Vertice actual = cola.saca();
            if(actual.hayDerecho()){
                cola.mete(actual.izquierdo);
                cola.mete(actual.derecho);
            } else if (actual.hayIzquierdo()) {
                cola.mete(actual.izquierdo);
            }
            return actual.elemento;
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioCompleto() { super(); }

    /**
     * Construye un árbol binario completo a partir de una colección. El árbol
     * binario completo tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario completo.
     */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
     * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        if (elemento == null)
            throw new IllegalArgumentException();
        Vertice nuevo = nuevoVertice(elemento);
        elementos++;
        if(raiz == null){
            raiz = nuevo;
            return;
        }        
        Cola<ArbolBinario<T>.Vertice> colilla = new Cola<>();
        colilla.mete(raiz);
        while(!colilla.esVacia()){
            Vertice actual = colilla.saca();
            if(!actual.hayIzquierdo()){
                actual.izquierdo = nuevo;
                nuevo.padre = actual;
                return;
            }
            if(!actual.hayDerecho()){
                actual.derecho = nuevo;
                nuevo.padre = actual;
                return;
            }
            colilla.mete(actual.izquierdo);
            colilla.mete(actual.derecho);
        }
    }

    public Vertice bfsElemento(){
        if(raiz != null){
            Cola<ArbolBinario<T>.Vertice> colilla = new Cola<>();
            colilla.mete(raiz);
            while(!colilla.esVacia()){
                Vertice actual = colilla.saca();
                if(actual.izquierdo != null)
                    colilla.mete(actual.izquierdo);
                if(actual.derecho != null)
                    colilla.mete(actual.derecho);
                if(colilla.esVacia()){
                    return actual;
                }
            }
        }
        return null;      
    }

    private boolean esHijoIzquierdo(Vertice vertice){
        if(!vertice.hayPadre())
            return false;
        return vertice.padre.izquierdo == vertice;        
    }

    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Vertice ultimoAgregado = bfsElemento();
        if (elemento == null || ultimoAgregado == null)
            return;
        //Variable booleana que me servira para indicar cuando el elemento se elimino.
        boolean eliminado = false;
        //Variable que apunta a la raiz
        Vertice vertice = raiz;
        //Auxiliar para tener el penultimo elemento, su funcion es que sobre
        //cada iteracion hace que apunte al vertice en turno, para de esta
        //forma en la ultima iteracion tener el penultimo elemento.
        Vertice nuevoUltimo = null;
        Cola<ArbolBinario<T>.Vertice> cola = new Cola<>();
        //Metemos la raiz a la cola
        cola.mete(vertice);
        while (!cola.esVacia()) {
            vertice = cola.saca();
            //Vemos si el elemento es igual, en caso de serlo solo
            //intercambiamos el contenido del actual con el ultimoAgregado
            if (vertice.get().equals(elemento) && !eliminado) {
                vertice.elemento = ultimoAgregado.elemento;
                eliminado = true;
                elementos--;
            }
            //Validacion para obtener el penultimo elemento en la penultima iteracion.
            if (vertice != ultimoAgregado)
                nuevoUltimo = vertice;
            //Si ya fue eliminado y va en la ultima iteracion.
            if (eliminado && vertice == ultimoAgregado) {
                //Elimina si hay un solo elemento.
                if (!vertice.hayPadre())
                    raiz = ultimoAgregado = null;
                else {
                    //Si el arbol tiene mas de un elemento.
                    ultimoAgregado = nuevoUltimo;
                    if (esHijoIzquierdo(vertice))
                        vertice.padre.izquierdo = null;
                    else
                        vertice.padre.derecho = null;
                }
            }
            //En el caso de que no lo haya encontrado, mete a sus hijos.
            //En la cola cuando de le pasa un elemento nulo solo hace un return.
            if(vertice.izquierdo != null)
                cola.mete(vertice.izquierdo);
            if(vertice.derecho != null)
                cola.mete(vertice.derecho);
        }
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol binario completo
     * siempre es ⌊log<sub>2</sub><em>n</em>⌋.
     * @return la altura del árbol.
     */
    @Override public int altura() {
        if(this.esVacia())
            return -1;
        return (int)Math.floor(Math.log(elementos) / Math.log(2));
    }

    /**
     * Realiza un recorrido BFS en el árbol, ejecutando la acción recibida en
     * cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void bfs(AccionVerticeArbolBinario<T> accion) {
        if(raiz != null){
            Cola<ArbolBinario<T>.Vertice> colilla = new Cola<>();
            colilla.mete(raiz);
            while(!colilla.esVacia()){
                Vertice actual = colilla.mira();
                if(actual.izquierdo != null)
                    colilla.mete(actual.izquierdo);
                if(actual.derecho != null)
                    colilla.mete(actual.derecho);
                accion.actua(colilla.saca());
            }
        }
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
