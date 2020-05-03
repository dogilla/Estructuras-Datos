package mx.unam.ciencias.edd.proyecto2;


public class Main {
    public static void main(String[] args) throws Exception {
        /* longitud de la linea de comandos */
        int len = args.length;
        Archivo archivo = new Archivo();

        EstructuraFactory ef = new EstructuraFactory();
        /* caso donde no ha parametros */
        if(len == 0){
            archivo.lectura("", true);
            archivo.EscribeArchivo(archivo.tipo, ef.getSVG(archivo.tipo, archivo.numeros));
            System.exit(1);
        }
        /* caso donde hay que leer de un archivo */
        if(len == 1){
            archivo.lectura(args[0], false);
            archivo.EscribeArchivo(archivo.tipo, ef.getSVG(archivo.tipo, archivo.numeros));
            System.exit(1);
        } else {
            archivo.Anuncio();
        }
    }
}
