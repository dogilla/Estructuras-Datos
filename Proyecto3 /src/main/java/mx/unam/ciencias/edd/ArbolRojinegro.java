package mx.unam.ciencias.edd;



/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 * <li>Todos los vértices son NEGROS o ROJOS.</li>
 * <li>La raíz es NEGRA.</li>
 * <li>Todas las hojas (<code>null</code>) son NEGRAS (al igual que la
 * raíz).</li>
 * <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 * <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 * mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros se autobalancean.
 */
public class ArbolRojinegro<T extends Comparable<T>> extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeRojinegro extends Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * 
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
            super(elemento);
            color = Color.NINGUNO;
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        public String toString() {
            String c = "";
            if(color == Color.ROJO)
                c = "R";
            if(color == Color.NEGRO)
                c = "N";
            return String.format("%s{%s}",c, elemento.toString());
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked")
                VerticeRojinegro vertice = (VerticeRojinegro)objeto;
            return super.equals(vertice) && vertice.color == this.color;
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolRojinegro() { super(); }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolRojinegro(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeRojinegro(elemento);
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        VerticeRojinegro v = (VerticeRojinegro)vertice;
        return v.color;
    }

    private VerticeRojinegro castVertice(VerticeArbolBinario<T> vertice){
        return (ArbolRojinegro<T>.VerticeRojinegro) vertice;
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);
        VerticeRojinegro vertice =  castVertice(ultimoAgregado);
        vertice.color = Color.ROJO;
        agregaB(vertice);
    }

    /* Método que saca el hermano del padre del vertice */
    private VerticeRojinegro sacaTio(VerticeRojinegro vertice){
        if(vertice.padre == null) 
            return null;
        return sacaHermano(castVertice(vertice.padre));
    }

    /* saca al otro hijo del padre del vertice */
    private VerticeRojinegro sacaHermano(VerticeRojinegro vertice){
        if(vertice.padre == null) 
            return null;
        if(vertice.padre.izquierdo != null && vertice.padre.derecho != null){
            VerticeRojinegro surdo = castVertice(vertice.padre.izquierdo);
            VerticeRojinegro diestro = castVertice(vertice.padre.derecho);
            if(surdo.equals(vertice))
                return diestro;
            else
                return surdo;
        }
        return null;
          
    }

    /* método que nos dice si dos vertices estan cruzado, es decir, uno es
       un hijo izquierdo y el otro derecho. */
    private boolean estanCruzados(VerticeRojinegro v,VerticeRojinegro p){
        return (esIzquierdo(v) && esDerecho(p)) ^
            (esDerecho(v) && esIzquierdo(p)); 
    }

    /* nos dice si un vertice es hijo izquierdo */
    private boolean esIzquierdo(VerticeRojinegro vertice){
        VerticeRojinegro papa =  castVertice(vertice.padre);
        if(papa.izquierdo != null && vertice != null){
            VerticeRojinegro i = castVertice(papa.izquierdo);
            return i.equals(vertice);
        }
        return false;
    }

    /* nos dice si un vertice es hijo derecho */
    private boolean esDerecho(VerticeRojinegro vertice){
        VerticeRojinegro papa = castVertice(vertice.padre);
        if(papa.derecho != null && vertice != null){
            VerticeRojinegro d = castVertice(papa.derecho);
            return d.equals(vertice);
        }
        return false;
    }
    
    /* método privado que nos permite hacer el rebalanceo y coloracion
        del arbol una vez que hayamos agregado un vertice */
    private void agregaB(VerticeRojinegro vertice){
        /* Caso 1 */
        if(vertice.padre == null){
            vertice.color = Color.NEGRO;
            return;
        }
        /* caso 2 */
        VerticeRojinegro papa = castVertice(vertice.padre);
        if(papa.color == Color.NEGRO){
            return;
        }
        /* caso 3 */
        VerticeRojinegro abuelo = castVertice(papa.padre);
        VerticeRojinegro tio = sacaTio(vertice);
        if(tio != null && tio.color == Color.ROJO){
            papa.color = Color.NEGRO;
            tio.color = Color.NEGRO;
            abuelo.color = Color.ROJO;
            agregaB(abuelo);
            return;
        }
        /* caso 4 */
        if(estanCruzados(vertice, papa)){
            if(esIzquierdo(papa))
                super.giraIzquierda(papa);
            else    
                super.giraDerecha(papa);
            VerticeRojinegro swap = vertice;
            vertice = papa;
            papa = swap;            
        }
        /* caso 5 */
        papa.color = Color.NEGRO;
        abuelo.color = Color.ROJO;
        if(esIzquierdo(vertice))
            super.giraDerecha(abuelo);
        else
            super.giraIzquierda(abuelo);
    }

    /* Método que nos dice si un vertice es negro, este metodo
        es necesario en el rebalance para que las hojas vacias
        (que en teoria son negras) no nos lancen un NullPointerException */
    private boolean esNegro(VerticeRojinegro vertice){
        if(vertice == null)
            return true;
        return vertice.color == Color.NEGRO;
    }

    /* Método que nos dice si un vertice es rojo, este metodo
        es necesario en el rebalance para que las hojas vacias
        (que no son rojas) no nos lancen un NullPointerException */
    private boolean esRojo(VerticeRojinegro vertice){
        return !esNegro(vertice);
    }

    /* recibe un vertice de color negro y rebalancea el arbol despues
    de eliminar un elemento */
    public void rebalenceaColores(VerticeRojinegro vertice){
        /* caso 1 */
        if(vertice.padre == null)
            return;
        /* caso 2 */
        VerticeRojinegro papa = castVertice(vertice.padre);
        VerticeRojinegro hermano = sacaHermano(vertice);
        if(hermano.color == Color.ROJO){
            papa.color = Color.ROJO;
            hermano.color = Color.NEGRO;
            if(esIzquierdo(vertice))
                super.giraIzquierda(papa);
            else
                super.giraDerecha(papa);
            papa = castVertice(vertice.padre);
            hermano = sacaHermano(vertice);
        }
        /* caso 3 */
        VerticeRojinegro hi = castVertice(hermano.izquierdo);
        VerticeRojinegro hd = castVertice(hermano.derecho);
        if(esNegro(papa) && esNegro(hermano) && esNegro(hi) && esNegro(hd)){
            hermano.color = Color.ROJO;
            rebalenceaColores(papa);
            return;
        }
        /* caso 4 */
        if(esRojo(papa) && esNegro(hermano)
        && esNegro(hi) && esNegro(hd)){
            hermano.color = Color.ROJO;
            papa.color = Color.NEGRO;
            return;
        }
        /* caso 5 */
        if((esIzquierdo(vertice) && esRojo(hi) && esNegro(hd)) ||
            esDerecho(vertice) && esNegro(hi) && esRojo(hd)){
            hermano.color = Color.ROJO;
            if(esRojo(hi))
                hi.color = Color.NEGRO;
            else
                hd.color = Color.NEGRO;

            if(esIzquierdo(vertice))
                super.giraDerecha(hermano);
            else
                super.giraIzquierda(hermano);
        }
        /* caso 6 */
        hermano = sacaHermano(vertice);
        hi = castVertice(hermano.izquierdo);
        hd = castVertice(hermano.derecho);
        hermano.color = papa.color;
        papa.color = Color.NEGRO;
        if (this.esIzquierdo(vertice)) {
            hd.color = Color.NEGRO;
        } else {
            hi.color = Color.NEGRO;
        }
        if (this.esIzquierdo(vertice)) {
            super.giraIzquierda(papa);
        } else {
            super.giraDerecha(papa);
        }

    }
    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        VerticeRojinegro fantasma = null;
        VerticeRojinegro eliminado =  castVertice(busca(elemento));
        if(eliminado == null)
            return;
        elementos--;
		if(eliminado.hayIzquierdo())
            eliminado = castVertice(intercambiaEliminable(eliminado));
		if(eliminado.izquierdo == null && eliminado.derecho == null){ 
                fantasma = castVertice(this.nuevoVertice(null));
                fantasma.color = Color.NEGRO;
                eliminado.izquierdo = fantasma;
                fantasma.padre = eliminado;
        }
        VerticeRojinegro hijo = eliminado.hayIzquierdo() ? 
                                castVertice(eliminado.izquierdo) :
                                castVertice(eliminado.derecho);
        super.eliminaVertice(eliminado);
        if(!(esNegro(eliminado) != esNegro(hijo))){
            hijo.color = Color.NEGRO;
            rebalenceaColores(hijo);
        } else {
            hijo.color = Color.NEGRO;
        }
        //eliminamos fantasma
        if(hijo.elemento == null){
            eliminaHoja(hijo);
        }       
    }

    private void eliminaHoja(VerticeRojinegro eliminar) {
        if (this.raiz == eliminar) {
            this.raiz = null;
            this.ultimoAgregado = null;
        } else if (esIzquierdo(eliminar)) {
            eliminar.padre.izquierdo = null;
        } else {
            eliminar.padre.derecho = null;
        }
    }



    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }
}
