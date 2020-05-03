package mx.unam.ciencias.edd.proyecto2;

import java.lang.StringBuilder;

import mx.unam.ciencias.edd.ArbolAVL;
import mx.unam.ciencias.edd.ArbolBinario;
import mx.unam.ciencias.edd.ArbolBinarioCompleto;
import mx.unam.ciencias.edd.ArbolBinarioOrdenado;
import mx.unam.ciencias.edd.ArbolRojinegro;
import mx.unam.ciencias.edd.Color;
import mx.unam.ciencias.edd.ComparableIndexable;
import mx.unam.ciencias.edd.Grafica;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.MonticuloMinimo;
import mx.unam.ciencias.edd.Pila;
import mx.unam.ciencias.edd.ValorIndexable;
import mx.unam.ciencias.edd.VerticeArbolBinario;


public class EstructuraFactory {

    /* objeto para guardar string */
    public StringBuilder sb;

    /* Constructor de EstructuraFactory*/
    EstructuraFactory(){
        sb = new StringBuilder();
    }

    /* clase que representa una gráfica */
    private class Graph extends Estructura {

        /* grafica interna */
        private Grafica<Integer> g;

        private String vertices;

        private String aristas;

        /* constructor de nuestra grafica de enteros */
        Graph(Lista<Integer> l){
            g = new Grafica<Integer>();
            boolean par = false;
            aristas = inicio(500, 500);
            vertices = "";
            int anterior = Integer.MIN_VALUE;
            for(int n : l){
                if(par){
                    if(n == anterior){
                        if(g.contiene(n)){
                            g.elimina(n);
                            g.agrega(n);
                        } else
                            g.agrega(n);
                    }
                    else {
                        if(!g.contiene(n))
                            g.agrega(n);
                        if(! g.contiene(anterior))
                            g.agrega(anterior);
                        g.conecta(n, anterior);
                    }
                    par = false;
                } else {
                    anterior = n;
                    par = true;
                }
            }    
        }

        /* Método que dibuja los vertices */
        private void dibujaVertices(){
            int elementos = g.getElementos();
            double x = 250, y = 30;
            int i = 0;
            // angulo de inclinacion para dibujar los vertices en circulo
            double angulo;
            for(int n : g) {
                /*ya no me dio tiempo de dibujar apropiadamente las gráficas. 
                Ni modos, en la vida a veces se gana y a veces se piede 
                y hoy me tocó perder. u,_u */
                angulo = i++ * (elementos / 360);
                x = 20 * Math.cos(Math.toRadians(angulo));
                y = 20 * Math.sin(Math.toRadians(angulo));
                vertices += circuloConNumero(n, x + 10, y + 10, 20, "black", "white");
            }

        }

        /* metodo que dibuja el SVG de la grafica */
        @Override
        public String dibuja(){
            dibujaVertices();
            return aristas + vertices + fin;
        }
    }

    /* Clase interna que representa una lista  o una cola*/
    private class List extends Estructura {

        Lista<Integer> lista;
        boolean cola;
        int elementos;
        
        /* constructor de nuestra lista */
        public List(Lista<Integer> l, boolean cola){
            this.lista = l;
            this.cola = cola;
            this.elementos = l.getElementos();
        }

        /* metodo que dibuja el SVG de una lista o cola */
        @Override
        public String dibuja(){
            int ejex = 15, ejey = 15;
            String s = inicio(80 * elementos, 60), simbolo = cola ? "→" : "⇄";
            for(int numero : lista){
                s += rectanguloConNumero(numero, ejex, ejey, 30, 30);
                ejex += 30;
                s += texto(simbolo, ejex + 5, ejey + 15, "font-weight='bold'");
                ejex += 30;
            }
            ejex += 15;
            return s + fin;
        }
    }

    /* Clase interna que representa una pila */
    private class Stack extends Estructura { 
        private Pila<Integer> p;
        private int elementos;

        Stack(Lista<Integer> lista){
            this.p = new Pila<Integer>();
            elementos = lista.getElementos();
            for(int entero : lista)
                p.mete(entero);         
        }

        /* metodo que dibuja el SVG de una pila*/
        @Override
        public String dibuja(){
            int base = 30, altura = 20, margen = elementos*20 + 30;
            int mitad = (margen/2) - 15, ejey = 15; 
            String s = inicio(margen, margen);
            while(!p.esVacia()){
                int e = p.saca();
                s += rectanguloConNumero(e, mitad, ejey, base, altura);
                ejey += 20;
            }
            return s + fin;
        }
    }

    /* clase que representa un estructura con forma de arbol */
    private class BinaryTree extends Estructura {

        private ArbolBinario<Integer> arbol;
        private String s;
        private String lineas;
        private String balances;
        private boolean avl;
        private boolean rn;

        BinaryTree(Lista<Integer> l, String estructura){
            this.avl = false;
            this.rn = false;
            this.arbol = tipoDeArbol(l, estructura);
            this.lineas = "";
            this.balances = "";
            s = "";
        }
        
        /* Método que transfora nuestro arbol de busqueda segun su tipo */
        private ArbolBinario<Integer> tipoDeArbol(Lista<Integer> l,String estructura){
            ArbolBinario<Integer> a;
            switch(estructura){
                case "arbolavl":
                    a = new ArbolAVL<Integer>();
                    break;
                case "arbolordenado":
                    a = new ArbolBinarioOrdenado<Integer>();;
                    break;
                case "arbolrojinegro":
                    a = new ArbolRojinegro<Integer>();
                    break;
                case "arbolcompleto":
                    a = new ArbolBinarioCompleto<Integer>();
                default:
                    a = new ArbolBinarioCompleto<Integer>();
            }

            if(estructura.equals("arbolrojinegro"))
                rn = true;

            if(estructura.equals("arbolavl"))
                avl = true;

            for(int entero : l){
                a.agrega(entero);
            }
            return a;

        }

        /* Método que dibuja un arbol binario de busqueda */
        @Override
        public String dibuja(){
            int ejex = 250, ejey = 30;
            VerticeArbolBinario<Integer> v = arbol.raiz();
            if(v != null){
                dibujaRecursivo(v, ejex, ejey);
                dibujaLineas(v, ejex, ejey);
            }
            return inicio(500, 500) + lineas + s + balances + fin;
        }

        /* Método recursivo que dibuja los vertices de un arbol de busqueda */
        public void dibujaRecursivo(VerticeArbolBinario<Integer> v, int x, int y){
            if(v.hayIzquierdo())
                dibujaRecursivo(v.izquierdo(), x+45, y+40);
            if(v.hayDerecho())
                dibujaRecursivo(v.derecho(), x-45, y+40);
            String fondo = "white";
            String letra = "black";
            if(rn){
                ArbolRojinegro<Integer> aa = (ArbolRojinegro<Integer>) arbol;
                if (aa.getColor(v) == Color.ROJO)
                    fondo = "red";
                if (aa.getColor(v) == Color.NEGRO){
                    fondo = "black";
                    letra = "white";
                }
            }

            if(avl){
                String balance = Integer.toString(v.altura()) + "/";
                int e = getAltura(v.izquierdo()) - getAltura(v.derecho());
                balance += Integer.toString(e);
                balances += texto(balance, x-15, y-15, "");
            }

            s += circuloConNumero(v.get(), x, y, 15,fondo, letra);
        }

        /* metodo que saca la altura en el caso de vertice de AVL */
        private int getAltura(VerticeArbolBinario<Integer> vertice){
            return vertice == null ? -1 : vertice.altura();
        }

        /* dibuja las lineas que unen los vertices del arbol */
        public void dibujaLineas(VerticeArbolBinario<Integer> v, int x, int y){
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


    /* clase que representa un monticulo */
    private class Heap extends Estructura{
        private MonticuloMinimo<ValorIndexable<Integer>> m;

        private BinaryTree arbol;

        Heap(Lista<Integer> l) {
            creaHeapDibujable(l);
        }
        
        /* crea el heap y lo pasa a un arbol completo */
        private void creaHeapDibujable(Lista<Integer> numeros){
            Lista<ValorIndexable<Integer>> elementos = new Lista<>();
            int j = 0;
            for(int numero : numeros)
                elementos.agrega(new ValorIndexable<Integer>(j++,numero));
            this.m = new MonticuloMinimo<>(elementos);
            Lista<Integer> listafinal = new Lista<>();
            for(int i = 0; i < m.getElementos(); i++)
                listafinal.agregaFinal(m.get(i).getElemento());
            this.arbol = new BinaryTree(listafinal,"arbolcompleto" );
        }

        /* Método que dibuja un monticulo */
        @Override
        public String dibuja(){
            return arbol.dibuja();
        }
    }

    /* Método que elige una estructura según la entrada */
    public Estructura getEstructura(String estructura, Lista<Integer> numeros){
        estructura = estructura.toLowerCase().trim();
        switch(estructura){
            case "arbolrojinegro":
            case "arbolavl":
            case "arbolordenado":
            case "arbolbinario":
                return new BinaryTree(numeros, estructura);
            case "grafica":
                return new Graph(numeros);
            case "pila":
                return new Stack(numeros);
            case "lista":
                return new List(numeros, false);
            case "cola":
                return new List(numeros, true);
            case "monticulominimo":
                return new Heap(numeros);
            default:
                Anuncio();
                return new BinaryTree(numeros, estructura);
        }
    }

    public void Anuncio(){
        String[] a = new String[5];
        a[0] = "Error en la entrada \n Entradas validas: " + "\n";
        a[1] = "java -jar target/proyecto2.jar ";
        a[2] = "ArbolRojinegro.txt \n";
        a[3] = "ArbolOrdenado.txt \n";
        a[4] = "MonticuloMinimo.txt \n";
        System.out.print(a[0]);
        for(int i = 2; i <= 4; i++){
            System.out.print(a[1] + a [i]);
        }
        a[2] = "Considerando que los archivos .txt se encuentran en el directorio actual";
        System.out.print(a[2]);
        System.exit(1);
    }

    public String getSVG(String e, Lista<Integer> l){
        return getEstructura(e,l).dibuja();
    }
}