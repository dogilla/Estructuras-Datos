package mx.unam.ciencias.edd.proyecto3.reporte;

import java.text.Collator;
import java.text.Normalizer;

/* Clase que representan lineas de texto*/
public class Linea implements Comparable<Linea>{

    /* texto completo de la linea */
    String texto;
    /* arreglo con las palabras de la linea */
    public String[] palabras;

    /**
     *  Contructor de la clase linea
     * @param texto contenido de la linea de texto
     */
    public Linea(String texto){
        this.texto = texto;
    }

    /* 
     * Normaliza el texto de la linea y la separa por palabras
     * en un arreglo.
     */
    public void setPalabras(){
        this.texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
        this.texto = texto.replaceAll("[^\\p{ASCII}]", "");
        palabras = texto.toLowerCase().trim().split(" ");
    }

    /* Representacion en cadena de la linea */
    @Override
    public String toString(){
        return texto;
    }

    /* Método que vuelve nuestras Lineas comparables */
    @Override
    public int compareTo(Linea otra){
        Collator myCollator = Collator.getInstance();
        return myCollator.compare(texto.toLowerCase(), 
                                otra.toString().toLowerCase());
    }    

}