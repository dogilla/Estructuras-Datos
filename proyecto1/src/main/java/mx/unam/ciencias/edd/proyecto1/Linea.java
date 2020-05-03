package mx.unam.ciencias.edd.proyecto1;

import mx.unam.ciencias.edd.*;
import java.text.Collator;

/* Clase que representan lineas de texto*/
public class Linea implements Comparable<Linea>{

    String texto;

    /*Constructor de la clase */
    public Linea(String texto){
        this.texto = texto;
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
