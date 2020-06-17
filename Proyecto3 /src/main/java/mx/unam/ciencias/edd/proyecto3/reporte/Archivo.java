package mx.unam.ciencias.edd.proyecto3.reporte;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import mx.unam.ciencias.edd.Lista;

import java.io.FileNotFoundException;


/* Clase que representa un archivo de texto */
public class Archivo{
    /* nombre del archivo de texto para leer*/
    String lectura;
    /* nombre del archivo de texto para escribir*/
    String escritura;
    /* texto del archivo */
    public Lista<Linea> texto;
    /* buffer para leer del archivo */
    private BufferedReader br;
    /* tipo de estructura del archivo */
    String nombre;


    /**
     * Contructor del archivo de texto
     * @param lectura nombre del archivo donde se leen las palabras
     */
    public Archivo(String lectura){
        this.lectura = lectura;
        this.texto = new Lista<Linea>();
        setNombre();
        if(lectura != null){ 
            try {
                this.br = new BufferedReader(new FileReader(new File(lectura)));
            } catch(FileNotFoundException e){
                System.out.print("No se pudo encontrar el archivo: " 
                                + lectura + "\n");
            }
        }
        this.leeArchivo();
    }

    /*
     * Le pone nombre al archivo de salida
     */
    private void setNombre(){
        int punto = lectura.indexOf(".");
        if(punto != -1){
            this.nombre = lectura.substring(0 , punto);
        }

    }

    /*
     * Lee del archivo de texto y guarda su contenido.
     */
    private void leeArchivo(){
        try{
            String linea;
            while ((linea = br.readLine()) != null) {
                texto.agrega(new Linea(linea));
            }
            br.close();
        }catch(IOException e2){
            System.out.print("Error al tratar de leer del archivo " 
                            + lectura + "\n");
        }
    }

    /*
     * Remueve los caracteres especiales del archivo y pone las 
     * condiciones para que se pueda contar las palabras
     */
    public void normalizaTexto(){
        for(Linea l : texto){
            l.setPalabras();
        }
    }

   /**
    * metodo que crea un archivo de texto nuevo
    * @param nombre nombre para el nuevo archivo
    * @param contenido contenido de texto del archivo
    */
    public void EscribeArchivo(String contenido, String dirPath){
        escritura = dirPath + "/" + nombre + ".html";
        try {
            File archivo = new File(escritura);
            archivo.createNewFile();
            FileWriter writer = new FileWriter(escritura);
            writer.write(contenido);
            writer.close();
        }catch(IOException e3){
            System.out.print("Error al escribir en el archivo");
            System.exit(1);
        }
    }

    

}