package mx.unam.ciencias.edd.proyecto3.dibujos;

import mx.unam.ciencias.edd.ArbolAVL;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.VerticeArbolBinario;
import mx.unam.ciencias.edd.proyecto3.reporte.ReporteHtml.Entrada;

/* clase que representa el svg de un arbol AVL */
public class SVGAVLTree extends SVGEstructura {
    /* arbol rojinegro para dibujar */
    private ArbolAVL<String> arbolavl;
    /* Cadena que guarda el SVG que dibuja los balances */
    private String balances;
    /* Cadena que guarda el texto SVG que dibuja vertices*/
    private String vertices;
    /* Cadena que guarda el texto SVG que dibuja las aristas*/
    private String lineas;

    /**
     * Constructor para la clase 
     * @param palabras lista de palabras que van a formar el arbol
     */
    public SVGAVLTree(Lista<Entrada> palabras){
        this.arbolavl = new ArbolAVL<String>();
        for(Entrada entrada : palabras)
            arbolavl.agrega(entrada.palabra);
    }
    /**
     * Constructor único para la clase que gráfica un arbol AVL.
     * @param palabras lista de palabras que van a conformar
     * el arbol AVL.
     */
    @Override
    public String dibuja(){
        int ejex = 250, ejey = 30;
        VerticeArbolBinario<String> v = arbolavl.raiz();
        if(v != null){
            dibujaRecursivo(v, ejex, ejey);
            dibujaLineas(v, ejex, ejey);
        }
        return inicio(500, 500) + lineas + vertices + balances + fin;
    }

    /**
     * Dibuja recursivamente los vertices de un arbol avl.
    * @param v vertice de inicio para dibujar
    * @param x eje x en el plano donde se va a dibujar el vertice
    * @param y eje y en el plano donde se va a dibujar el vertice
    */
    public void dibujaRecursivo(VerticeArbolBinario<String> v, int x, int y){
        if(v.hayIzquierdo())
            dibujaRecursivo(v.izquierdo(), x+45, y+40);
        if(v.hayDerecho())
            dibujaRecursivo(v.derecho(), x-45, y+40);
        String balance = Integer.toString(v.altura()) + "/";
        int e = getAltura(v.izquierdo()) - getAltura(v.derecho());
        balance += Integer.toString(e);
        balances += texto(balance, x-15, y-15, "");

        vertices += circuloConTexto(v.get(), x, y, 15,colores[0], colores[1]);
    }
    
    /**
     * saca la altura del vertice avl
     * @param vertice vertice de arbol avl
     * @return altura del vertice
     */
    private int getAltura(VerticeArbolBinario<String> vertice){
        return vertice == null ? -1 : vertice.altura();
    }

    /* dibuja las lineas que unen los vertices del arbol */
    public void dibujaLineas(VerticeArbolBinario<String> v, int x, int y){
        if(v.hayIzquierdo()){
            lineas += linea(x,y,x+45,y+40);
            dibujaLineas(v.izquierdo(), x+45, y+40);
        }
        if(v.hayDerecho()){
            lineas += linea(x,y,x-45,y+40);
            dibujaLineas(v.derecho(), x-45, y+40);
        }    
    }


}