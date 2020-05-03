package mx.unam.ciencias.edd.proyecto1;

import mx.unam.ciencias.edd.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;


/* Clase que representa un archivo de texto */
public class Archivo{
    /* nombre del archivo de texto */
    String archivo;
    /* booleano que nos dice si se va a imprimir en reversa */
    boolean reversa;
    /* texto del archivo */
    Lista<Linea> texto; 
    /* buffer para leer del archivo */
    BufferedReader br;

    /* constructor de la clase archivo */
    public Archivo(String archivo){
        this.archivo = archivo;
        this.reversa = false;
        this.texto = new Lista<Linea>();
        if(archivo != null){ 
            try {
                this.br = new BufferedReader(new FileReader(new File(archivo)));
            } catch(FileNotFoundException e){
                System.out.print("No se puedo encontrar el archivo: " 
                                + archivo + "\n");
            }
        }
    }

    /* lee un archivo y agrega su contenido a la variable global texto */
    private void leeArchivo() {
        try{
            String linea;
            while ((linea = br.readLine()) != null) {
                texto.agrega(new Linea(linea));
            }
            br.close();
        }catch(IOException e2){
            System.out.print("Error al tratar de leer del archivo " 
                            + archivo + "\n");
        }
    }

    /* imprime el texto del archivo en la terminal */
    public void imprime(){
        if(archivo != null)
            leeArchivo();
        if(reversa)
            imprimeTextoReversa();
        else
            imprimeTexto();
    }

    /* imprime el texto en reversa en la terminal */
    private void imprimeTextoReversa(){
        for(Linea l: Lista.mergeSort(texto).reversa()){
            System.out.print(l.toString() + "\n");
        }
    }

    /* imprime el texto en la terminal*/
    private void imprimeTexto(){
        for(Linea l: Lista.mergeSort(texto))
            System.out.print(l.toString() + "\n");
    }
    
    /* escribe el texto en un archivo ya existente */
    public void escribeEnArchivo(String file){
        try{
            leeArchivo();
            FileWriter escritor = new FileWriter(file);
            for(Linea l : reversa ? Lista.mergeSort(texto).reversa() : 
            Lista.mergeSort(texto)) {
                escritor.write(l.toString() + "\n");
            }
            escritor.close();
        }catch(IOException e3){
            System.out.print("Error al tratar de escribir en el archivo "
                            + archivo + "\n");
        }
    }

}
