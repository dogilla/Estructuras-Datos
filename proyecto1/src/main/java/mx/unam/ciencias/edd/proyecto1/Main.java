package mx.unam.ciencias.edd.proyecto1;

import mx.unam.ciencias.edd.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;


public class Main {

    public static void main(String[] args) throws Exception {
        /* lista que guarda todos los archivos de texto que ordenamos */
        Lista<Archivo> entradas = new Lista<Archivo>();
        /* archivo de salida en caso necesario  */
        Archivo salida = null;
        /* id del archivo de salida en caso necesario */
        String output = "";
        /* booleanos que nos indican si las banderas -r y -o fueron invocados*/
        boolean reversa, salidaArch;
        reversa = salidaArch = false;
        /* longitud de la linea de comandos */
        int len = args.length;

        /* En este caso hay que leer de la entrada estandar */
        if(len == 0){
            BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
            Archivo standar = new Archivo(null);
            String n;
            try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
				while ((n = br.readLine()) != null)
                    standar.texto.agrega(new Linea(n));
            } catch (IOException e0){
                System.out.print("Error al leer de la entrada estandar");
            }
            standar.imprime();
            System.exit(1);
        }

        /* si llegamos aqui, quiere decir que no vamos a leer de la entrada
           estandar. Entonces se lee uno a uno los parametros que el usuario 
           le pasa por la linea de comandos */
        for(int i = 0; i < len; i++){
            if(args[i].equals("-r")){
                reversa = true;
                continue;
            }
            if(args[i].equals("-o")) { 
                output = args[i+1];
                salidaArch = true;
                i++;
                continue;
            }
            entradas.agrega(new Archivo(args[i]));
        }

        /* si se invoco la bandera -o se crea un nuevo archivo con el id
            que el usuario eligió */
        if(salidaArch)
            creaArchivo(output);

        /* se ordena cada archivo en tiempo O(n log n) y se imprime 
           el resultado en pantalla */
        for(Archivo a: entradas){
            if(reversa)
                a.reversa = true;
            if(salidaArch)
                a.escribeEnArchivo(output);
            else
                a.imprime();
        }
    }

    public static void creaArchivo(String id) throws IOException{
        File file = new File(id);
    }

}
