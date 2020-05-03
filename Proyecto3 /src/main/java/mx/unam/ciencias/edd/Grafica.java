package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador() {
            iterador = vertices.iterator();
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            return iterador.next().get();
        }
    }

    /* Clase interna privada para vértices. */
    private class Vertice implements VerticeGrafica<T> {

        /* El elemento del vértice. */
        public T elemento;
        /* El color del vértice. */
        public Color color;
        /* La lista de vecinos del vértice. */
        public Lista<Vertice> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            this.elemento = elemento;
            this.vecinos = new Lista<Vertice>();
            this.color = Color.NINGUNO;
        }

        /* Regresa el elemento del vértice. */
        @Override public T get() {
            return elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            return vecinos.getLongitud();
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
            return color;
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return vecinos;
        }
    }

    /* Vértices. */
    private Lista<Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        vertices = new Lista<Vertice>();
        aristas = 0;
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        return vertices.getLongitud();
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        return aristas;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento es <code>null</code> o ya
     *         había sido agregado a la gráfica.
     */
    @Override public void agrega(T elemento) {
        Vertice nuevo = new Vertice(elemento);
        if(elemento == null || this.contiene(elemento))
            throw new IllegalArgumentException("No se puede agregar null a una grafica");
        vertices.agrega(nuevo);
    }

    /* Método privado que busca un vertice en la gráfica dado un elemento */
    private Vertice busca(T elemento){
        for(Vertice vertice : vertices){
            if(vertice.elemento.equals(elemento))
                return vertice;
        }
        return null;
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica. El peso de la arista que conecte a los elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {
        Vertice v1 = busca(a);
        Vertice v2 = busca(b);
        if(v1 == null || v2 == null)
            throw new NoSuchElementException();
        if(v2.vecinos.contiene(v1) || a == b)
            throw new IllegalArgumentException();
        v1.vecinos.agrega(v2);
        v2.vecinos.agrega(v1);
        aristas++;

    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
        Vertice v1 = busca(a);
        Vertice v2 = busca(b);
        if(v1 == null || v2 == null)
            throw new NoSuchElementException();
        if(v1.equals(v2) || !sonVecinos(a, b))
            throw new IllegalArgumentException();
        v1.vecinos.elimina(v2); 
        v2.vecinos.elimina(v1);
        aristas--;
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <code>true</code> si el elemento está contenido en la gráfica,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        for(Vertice vertice : vertices){
            if(vertice.elemento.equals(elemento))
                return true;
        }
        return false;
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
        Vertice eliminable = busca(elemento);
        if(eliminable == null)
            throw new NoSuchElementException();
        for(Vertice vecino: eliminable.vecinos)
            desconecta(vecino.elemento, eliminable.elemento);
        vertices.elimina(eliminable);
    }

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <code>true</code> si a y b son vecinos, <code>false</code> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
        Vertice v1 = busca(a), v2 = busca(b);
        if(v1 == null || v2 == null)
            throw new NoSuchElementException();
        boolean ab, ba;
        ab = ba = false;
        for(Vertice vecinoDeA : v1.vecinos){
            if(vecinoDeA.equals(v2)){
                ab = true;
                break;
            }
        }
        for(Vertice vecinoDeB : v2.vecinos){
            if(vecinoDeB.equals(v1)){
                ba = true;
                break;
            }
        }
        return ab && ba;
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        Vertice vertice = busca(elemento);
        if (vertice == null) 
            throw new NoSuchElementException();
        else 
            return vertice;
    }

    /**
     * Define el color del vértice recibido.
     * @param vertice el vértice al que queremos definirle el color.
     * @param color el nuevo color del vértice.
     * @throws IllegalArgumentException si el vértice no es válido.
     */
    public void setColor(VerticeGrafica<T> vertice, Color color) {
        if (vertice == null || vertice.getClass() != Vertice.class) 
            throw new IllegalArgumentException();
        Vertice v = (Vertice)vertice;
        if(!vertices.contiene(v))
          throw new IllegalArgumentException();
        v.color = color;
    }

    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {
        if(vertices.esVacia() || vertices.getElementos() == 1)
            return true;
        for(Vertice coloreado : vertices)
            coloreado.color = Color.NEGRO;
        Cola<Vertice> colilla = new Cola<Vertice>();
        Vertice vertice = vertices.getPrimero(), actual;
        colilla.mete(vertice);
        while(!colilla.esVacia()){
            actual = colilla.saca();
            //coloreamos al sacar para no colorear dos veces 
            // a lo pendejo
            actual.color = Color.ROJO;
            for (Vertice vecino : actual.vecinos) {
                //si no es rojo lo metemos
                if (vecino.color.equals(Color.NEGRO)) {
                    colilla.mete(vecino);
                }
            }
        }
        for(Vertice v: vertices){
            if(v.color == Color.NEGRO)
                return false;
        }
        return true;
    }

    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
        for(Vertice vertice: vertices)
            accion.actua(vertice);
    }
    
    /* Método que recorre la gráfica en bfs o dfs segun la estructura */
    private void recorreGrafica(AccionVerticeGrafica<T> accion, 
    MeteSaca<Vertice> estructura, T elemento){
        Vertice vertice = busca(elemento), actual;
        for(Vertice v : vertices){
            v.color = Color.NINGUNO;
        }
        estructura.mete(vertice);
        vertice.color = Color.NEGRO;
        while(!estructura.esVacia()) {
            actual = estructura.saca();
            accion.actua(actual);
            for (Vertice vecino: actual.vecinos) {
                if (vecino.color == Color.NINGUNO) {
                    estructura.mete(vecino);
                    vecino.color = Color.NEGRO;
                }
            }
        }
        for(Vertice v: vertices){
            v.color = Color.NINGUNO;
        }
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
        Cola<Vertice> colilla = new Cola<Vertice>();
        recorreGrafica(accion, colilla, elemento);
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
        Pila<Vertice> pililla = new Pila<Vertice>();
        recorreGrafica(accion, pililla,elemento);
    }

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return vertices.esVacia();
    }

    /**
     * Limpia la gráfica de vértices y aristas, dejándola vacía.
     */
    @Override public void limpia() {
        vertices.limpia();
        aristas = 0;
    }

    /**
     * Regresa una representación en cadena de la gráfica.
     * @return una representación en cadena de la gráfica.
     */
    @Override public String toString(){
        for(Vertice v : vertices)
            v.color = Color.ROJO;
        String s, aristas = s = "{";
        for(Vertice vertice : vertices){
            s += vertice.get() + ", ";
            for(Vertice vecino : vertice.vecinos){
                if(vecino.color == Color.ROJO)
                    aristas += "(" + vertice.get() + ", " + vecino.get() + "), ";
                vertice.color = Color.NINGUNO;
            }
        }
        return String.format("%s}, %s}",s,aristas);
    }


    /**
     * Nos dice si la gráfica es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la gráfica es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") 
        Grafica<T> grafica = (Grafica<T>)objeto;
        if(grafica.aristas != aristas)
            return false;
        for(Vertice vertice : vertices){
            if(!grafica.contiene(vertice.get()))
                return false;
            for(Vertice vecino : vertice.vecinos){
                if(!grafica.sonVecinos(vertice.elemento, vecino.elemento))
                    return false;
            }
        }
        return true;
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
