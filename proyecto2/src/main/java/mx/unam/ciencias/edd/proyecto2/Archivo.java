package mx.unam.ciencias.edd.proyecto2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import mx.unam.ciencias.edd.Lista;

import java.io.FileNotFoundException;


/* Clase que representa un archivo de texto */
public class Archivo{
    /* nombre del archivo de texto para leer*/
    String lectura;
    /* nombre del archivo de texto para escribir*/
    String escritura;
    /* texto del archivo */
    Lista<String> texto;
    /* lista de numeros */
    Lista<Integer> numeros;
    /* buffer para leer del archivo */
    private BufferedReader br;
    /* booleano para saber si leer de la entrada estandar */
    private boolean estandar;
    /* tipo de estructura del archivo */
    String tipo;


    /* constructor de la clase archivo */
    public Archivo(){
        this.escritura = null;
        this.lectura = null;
        this.estandar = false;
        this.texto = new Lista<String>();
        this.numeros = new Lista<Integer>();
    }

    /* inicia y la lectura desde un archivo de texto o entrada estandar */
    public void lectura(String archivo, boolean bandera){
        /* en este caso leemos de la entrada estandar */
        if(bandera){
            br = new BufferedReader(new InputStreamReader(System.in));
        /* En este otro de un archivo */
        } else {
            lectura = archivo;
            try {
                this.br = new BufferedReader(new FileReader(new File(lectura)));
            } catch(FileNotFoundException e){
                
                System.out.print("No se puedo encontrar el archivo: " 
                                + lectura + "\n");
                Anuncio();
                
            }
        }
        /* empezamos a pasar las cadenas a una estructura y graficar */
        try{
            boolean primero = true;
            String linea = "";
            String[] dividido;
            while ((linea = br.readLine()) != null) {
                //ignoramos las lineas con "#"
                linea = linea.trim();
                if(linea.equals("")){
                    continue;
                }
                if(linea.charAt(0) == '#'){
                    continue;
                }
                    
                // caso de la primera linea
                if(primero){
                    dividido = linea.split(" ");
                    if(dividido.length == 1){
                        tipo = dividido[0];
                        primero = false;
                        continue;
                    }
                    int i = linea.indexOf(" ");
                    tipo = linea.substring(0, i);
                    linea = linea.substring(i).trim();
                    primero = false;
                }
                dividido = linea.split(" ");
                
                for(String numero : dividido)
                    numeros.agrega(Integer.parseInt(numero));
            }
            br.close();
        }catch(IOException e2){
            System.out.print("Error al tratar de leer del archivo " 
                            + lectura + "\n");
        }
    }

  
    /*  nombre de archivo, contenido pues el contenido */
    public void EscribeArchivo(String s, String contenido){
        escritura = s + ".svg";
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

    public void Anuncio(){
        String[] a = new String[5];
        a[0] = "Error en la entrada \n Entradas validas: " + "\n";
        a[1] = "java -jar target/proyecto2.jar ";
        a[2] = "ArbolRojinegro.txt \n";
        a[3] = "ArbolOrdenado.txt \n";
        a[4] = "MonticuloMinimo.txt \n";
        System.out.print(a[0]);
        for(int i = 2; i <= 4; i++){
            System.out.print(a[1] + a[i]);
        }
        a[2] = "Considerando que los archivos .txt se encuentran en el directorio actual";
        System.out.print(a[2]);
        System.exit(1);

    }
    

}