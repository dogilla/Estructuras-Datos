package mx.unam.ciencias.edd.proyecto3.dibujos;

import mx.unam.ciencias.edd.ArbolRojinegro;
import mx.unam.ciencias.edd.Color;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.VerticeArbolBinario;
import mx.unam.ciencias.edd.proyecto3.reporte.ReporteHtml.Entrada;

public class SVGBlackRedTree extends SVGEstructura {
    /* arbol rojinegro al que hay que dibujar*/
    private ArbolRojinegro<String> arbolrn;
    /* cadena que guarda el texto SVG que dibuja vertices*/
    private String vertices;
    /* cadena que guarda el texto SVG que dibuja las aristas*/
    private String lineas;

    /**
     * Constructor único para la clase que gráfica un arbol 
     * rojinegro.
     * @param palabras lista de palabras que van a conformar
     * el arbol rojinegro
     */
    public SVGBlackRedTree(Lista<Entrada> palabras){
        this.arbolrn = new ArbolRojinegro<String>();
        for(Entrada entrada : palabras)
            arbolrn.agrega(entrada.palabra);
    }

    /**
     * Método que crea la cadena que representa el dibujo svg
     * del arbol rojinegro de la clase
     * @return la cadena svg
     */
    @Override
    public String dibuja(){
        int ejex = 250, ejey = 30;
        VerticeArbolBinario<String> v = arbolrn.raiz();
        if(v != null){
            dibujaRecursivo(v, ejex, ejey);
            dibujaLineas(v, ejex, ejey);
        }
        return inicio(500, 500) + lineas + vertices + fin;
    }

    /**
     * Método que dibuja recursivamente los vertices del arbol rojinegro
     * @param v vértice de inicio del dibujo, es el primero en dibujarse
     * @param x coordenada de inicio sobre el eje x
     * @param y coordenada de inicio sobre el eje y
     */
    public void dibujaRecursivo(VerticeArbolBinario<String> v, int x, int y){
        if(v.hayIzquierdo())
            dibujaRecursivo(v.izquierdo(), x+45, y+40);
        if(v.hayDerecho())
            dibujaRecursivo(v.derecho(), x-45, y+40);
        // colores para el fondo y letra de un vertice
        String fondo = colores[1], letra = colores[0];
        if (arbolrn.getColor(v) == Color.ROJO)
            fondo = colores[2];
        if (arbolrn.getColor(v) == Color.NEGRO){
            fondo = colores[0];
            letra = colores[1];
        }
        vertices += circuloConTexto(v.get(), x, y, 15,fondo, letra);
    }

    /**
     * Método que dibuja recursivamente las aristas del arbol rojinegro
     * @param v vértice de inicio del dibujo, es el primero en dibujarse
     * @param x coordenada de inicio sobre el eje x
     * @param y coordenada de inicio sobre el eje y
     */
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