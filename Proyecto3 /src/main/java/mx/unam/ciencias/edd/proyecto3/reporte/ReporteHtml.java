package mx.unam.ciencias.edd.proyecto3.reporte;

import mx.unam.ciencias.edd.Diccionario;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.proyecto3.dibujos.Pastel;
import mx.unam.ciencias.edd.proyecto3.dibujos.SVGAVLTree;
import mx.unam.ciencias.edd.proyecto3.dibujos.SVGBlackRedTree;

import java.lang.StringBuilder;
import java.util.Iterator;


public class ReporteHtml {

    /* clase privada que representa la entrada de un elemento del
     * diccionario, es necesaria para ordenar el numero de apariciones
     * y el orden de las palabras.
     */
    public class Entrada implements Comparable<Entrada>{
        /*palabra de la entrada */
        public String palabra;
        /*numero de veces que aparece la palabra*/
        public int contador;

        /**
         * Contructor para entrada de diccionario
         * @param palabra palabra de la entrada
         * @param contador numero de veces que aparece
         */
        public Entrada(String palabra, int contador){
            this.palabra = palabra;
            this.contador = contador;
        }

        /*
         * Compara la entrada con otro objeto de la misma clase
         * regresa 1 si el contador es mayor, -1 si es menos o
         * 0 si ambos son iguales.
         * @param e entrada con la cual se va a comparar
         */
        @Override
        public int compareTo(Entrada e){
            return (this.contador > e.contador) ? 1 : (this.contador > e.contador) ? -1 : 0 ;
        }

        @Override
        /*
         * Sobrecarga en metodo toString() 
        */
        public String toString(){
            return palabra + " : " + contador;
        }
    }

    /*  stringbuilder para mejorar el rendimiento de cadenas*/
    private StringBuilder sb;
    /* diccionario con el conteo de palabras para el reporte */
    private Diccionario<String, Integer> dic;
    /* total de palabras */
    int total;

    public ReporteHtml(){
        this.sb = new StringBuilder();
        sb.append("<!DOCTYPE html>" + "\n" + "<html lang='en'>" +"\n");
        total = 0;
    }

    /**
     * Crea un reporte en html a partir del conteo de palabras
     * de un archivo de texto
     * @param conteo diccionario que contiene el conteo.
     */
    public String creaReporte(Diccionario<String, Integer> conteo){
        Head();
        this.dic = conteo;
        Lista<Entrada> entradas = creaLista();
        Body(entradas);
        Pastel graficoPastel = new Pastel(entradas,total);
        int c = 0;
        Lista<Entrada> masUsadas = new Lista<Entrada>();
        for(Entrada a : entradas){
            if(c == 15)
                break;
            masUsadas.agregaFinal(a);
        }
        SVGBlackRedTree brt = new SVGBlackRedTree(masUsadas);
        SVGAVLTree avl = new SVGAVLTree(masUsadas);
        sb.append(graficoPastel.dibuja());
        sb.append(brt.dibuja());
        sb.append(avl.dibuja());
        sb.append("    </body>"+"\n");
        return sb.toString();
    }


    /**
     * Crea el header del documento html, pareciera muy rebuscado
     * hacer esto pero fue muy fácil con el el editor VSC
     */
    public void Head(){
        /* fue aquí cuando descubri que basta copiar y pegar html y dejar
            que visual studio hiciera lo demás. */
        sb.append("    <head>" + "\n"+
        "        <meta charset='utf-8'>"+ "\n"+
        "        <title> Reporte </title>" + "\n"+
        "        <style>"+"\n"+
        "        body{"+"\n"+
        "            background-color: rgb(31, 27, 42);"+"\n"+
        "            text-align: center;"+"\n"+
        "        }"+"\n"+
        "        h1{"+"\n"+
        "            font-family: Verdana, Geneva, sans-serif;"+"\n"+
        "            color:rgb(246, 157, 74);"+"\n"+
        "            font-size: -webkit-xxx-large;"+"\n"+
        "        }"+"\n"+
        "        p, ul, h2{"+"\n"+
        "            color: whitesmoke;"+"\n"+
        "            font-family: Helvetica, sans-serif;"+"\n"+
        "        }"+"\n"+
        "        .bar { \n" +
        "           fill: #E6E6FA; \n" +
        "           height: 21px; \n" +
        "           transition: fill .3s ease;"+
        "           cursor: pointer; \n" +
        "           font-family: Helvetica, sans-serif; \n"+
        "           text { \n"+
        "               fill: #555; \n"+
        "           } \n"+
        "        } \n"+
        "        .chart:hover, \n"+
        "        .chart:focus { \n"+
        "           .bar { \n"+
        "               fill: #aaa; \n"+
        "           } \n"+
        "        } \n"+
        "        .bar:hover, \n"+
        "        .bar:focus { \n"+
        "        fill: rgb(187, 255, 0) !important; \n"+      
        "        text { \n" +   
        "            fill: rgb(137, 235, 73); \n"+
        "        } \n"+
        "        } \n");
        sb.append("        </style>"+"\n");
        sb.append("    </head>" + "\n");
    }



    /**
     * crea el cuerpo del archivo html
     * @param entradas entradas para generar el reporte
     */
    public void Body(Lista<Entrada> entradas){
        sb.append("    <body>"+"\n"+
        "        <h1>Reporte final</h1>"+"\n"+
        "        <p> Las palabras utilizadas en el archivo son:  <p>"+"\n"+
        "        <ul style='list-style: none;'     >"   +"\n");
        for(Entrada e : entradas){
            sb.append("            <li> "+ e.toString() + " </li>"+"\n");
        }
        sb.append("        </ul>"+"\n"+
        "        <h2> El total de palabras fue: " + total + "</h2>" + "\n"+
        "        <h1>Grafica de pastel</h1>" + "\n");
    }
    
    /**
     * lista que crea y ordena las palabras del archivo
     * @return lista con las palabras ordenadas segun
     * el numero de apariciones en el texto.
     */
    private Lista<Entrada> creaLista(){
        Lista<Entrada> lista = new Lista<Entrada>();
        int v = 0;
        Iterator iteradorLlaves = dic.iteradorLlaves();
        while(iteradorLlaves.hasNext()){
            String llave = (String) iteradorLlaves.next();
            int apariciones = dic.get(llave);
            total += apariciones;
            lista.agrega(new Entrada(llave, apariciones));
        }
        return Lista.mergeSort(lista).reversa();
    }


    
}













