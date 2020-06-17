package mx.unam.ciencias.edd.proyecto3;

import java.io.File;

import mx.unam.ciencias.edd.Diccionario;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.proyecto3.reporte.Archivo;
import mx.unam.ciencias.edd.proyecto3.reporte.Linea;
import mx.unam.ciencias.edd.proyecto3.reporte.ReporteHtml;

public class Main {
    public static void main(String[] args) throws Exception {
        /* lista que guarda todos los archivos de texto que nos proporcionan */
        Lista<Archivo> entradas = new Lista<Archivo>();
        /* longitud de los argumentos de la linea de comandos */
        int len = args.length;
        /* nombre del directorio donde vamos a guardar los reportes */
        String directorio = null;
        /* diccionario que nos ayuda a contar las palabras del texto */
        Diccionario<String, Integer> contador;
        /* objeto que nos permite crear un reporte Html */
        ReporteHtml reporte = new ReporteHtml();

        if (len < 2) {
            System.out.println("Faltan parametros");
        }

        /* lee la linea de comandos */
        for (int i = 0; i < len; i++) {
            if (args[i].equals("-o")) {
                directorio = args[i + 1];
                i++;
                continue;
            }
            entradas.agrega(new Archivo(args[i]));
        }

        /* camino al nuevo directorio */
        String dirPath = null;

        if (directorio == null) {
            System.out.print("No se proporciono el nombre del directorio");
            /* se crea el nuevo directorio si no exite */
        } else {
            File dir = new File(directorio);
            boolean existe = dir.exists();
            if (!existe) {
                File file = new File(directorio);
                boolean creado = file.mkdir();
                if (!creado) {
                    System.out.print("Error al crear el directorio " + directorio);
                }
            }
            dirPath = System.getProperty("user.dir") + "/" + directorio;
            System.out.print(dirPath);
        }

        if(dirPath == null){
            System.out.print("Error en el directorio");
        }

        /* hacemos el conteo de palabras por archivo */
        for (Archivo a : entradas) {
            a.normalizaTexto();
            contador = new Diccionario<String, Integer>();
            /* por lineas */
            for (Linea renglon : a.texto) {
                /* por palabras */
                for (String palabra : renglon.palabras){
                    if(contador.contiene(palabra))
                        contador.agrega(palabra, contador.get(palabra)+1);
                    else
                        contador.agrega(palabra, 1);
                }     
            }
            a.EscribeArchivo(reporte.creaReporte(contador), dirPath);
        }


    }
}
