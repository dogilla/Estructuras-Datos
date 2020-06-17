package mx.unam.ciencias.edd.proyecto3.dibujos;

import java.text.DecimalFormat;

import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.proyecto3.reporte.ReporteHtml.Entrada;

/* clase que representa el svg de una gráfica de pastel */
public class Pastel extends SVGEstructura {

    /* Lista con las palabras más utilizadas */
    private Lista<Entrada> elementos;
    /* total de palabras en el archivo de texto */
    private int total;
    /* forma de acortar los decimales */
    private static DecimalFormat df = new DecimalFormat("0.00");


    /**
     * Contructor de la clase que construye un gráfico
     * de pastel
     * @param elementos etiquetas de los elementos
     * @param total total de elementos
     */
    public Pastel(Lista<Entrada> elementos, int total){
        this.total = total;
        this.elementos = elementos;
    }

    /**
     * método que dibuja la gráfica de pastel y barras de la clase
     * @return elemento.toString() dibujo en svg
     */
    @Override
    public String dibuja(){
        return creaGraficos();
    }

    /**
     * Calcula el angulo necesario para gráficar un porcentaje
     * especifico y regresa su reprentacion en cadena.
     * @param porcentaje porcentaje de la gráfica a dibujar
     */
    private String Porcentaje(Double porcentaje){
        return df.format((porcentaje * 31.4)/ 100);
    }

    /**
     * método que crea una cadena con el texto svg que crea una
     * gráfica de barras y otra de pastel para el texto del archivo
     * de texto
     * @return cadena con svg de ambas gráficas
     */
    private String creaGraficos(){
        int rebanadas;
        rebanadas = (elementos.getElementos() > 10) ? 10 : 4;
        Lista<Entrada> graficables = new Lista<Entrada>();
        /* numero de palabras entre las mas usadas */
        int masUsadas = 0;
        for(Entrada e : elementos){
            if(rebanadas == 0)
                break;
            masUsadas += e.contador;
            graficables.agrega(e);
            rebanadas--;
        }
        /* porcentaje que representas las mas usadas del total */
        Double porcentajeMasUsadas = (double) ((100 * masUsadas) / total);
        Double porcentajeMenosUsadas = 100 - porcentajeMasUsadas;
        String s = "        <svg height='20' width='20' viewBox='0 0 20 20' style='height:250px; width: 250px;'>; \n" +
            "            <circle r='10' cx='10' cy='10' fill='#EEE8AA' /> \n";
        int i = 2;
        double p = 100 - porcentajeMenosUsadas;
        for(Entrada graficable : graficables){
            s+= creaRebanada(p,colores[i]);
            p -= calculaPorcentaje(graficable.contador);
            i++;
        }
        s += "        </svg> \n";
        s += listadoPastel(graficables);
        s += creaBarras(graficables);
        return s;
    }

    /**
     * método que genera el texto informativo de la gráfica de pastel.
     * @param l lista con las entradas de la grafica de pastel
     * @return texto html con la información de la grafica de pastel
     */
    private String listadoPastel(Lista<Entrada> l){
        String info = "        <ul style='list-style: none;'> \n";
        double usado = 0.0;
        for(Entrada graficable : l){
            double p = calculaPorcentaje(graficable.contador);
            usado += p;
            info += "            <li>" + graficable.palabra + " : " 
            + df.format(p) +"%</li> \n";
        }
        info += "            <li>"+ "Otras palabras: " 
            + df.format(100.0 - usado)  + "%</li> ";
        info += "        </ul> \n";
        return info;
    }

    /**
     * metodo que crea el texto svg de la grafica de barras.
     * @param graficables lista con entradas que hay que gráficar
     * @return cadena con svg de la gráfica de barras
     */
    private String creaBarras(Lista<Entrada> graficables){
        String barras = "        <h1> Grafica de barras </h1> \n" +
            "        <figure> \n"+
            "          <svg class='chart' width='420' height='500' aria-labelledby='title' role='img'> \n";
        int ejey = 5;
        for(Entrada graficable : graficables){
            barras += creaBarra(graficable, ejey);
            ejey += 25;
        }
        barras += "          </svg> \n" +
                "        </figure> \n";
        return barras;
    }

    /**
     * crea una sola barra individual del grafico de barras
     * @param svg entrada con la informacion para graficar la barra
     * @param y eje y sobre el dibujo para graficar.
     * @return texto html de la barra 
     */
    private String creaBarra(Entrada svg, int y){
        int espacio = svg.contador * 8;
        String barra = "            <g class='bar'> \n" +
            "              <rect width='" + espacio + 
            "' height='20' y='"+ (y+5) +"'></rect> \n"+
            "            <text x='"+ (espacio+10) + "' y='"+ (y+12) +"' dy='.35em'>"
            + svg.palabra + "</text> \n" +
            "            </g> \n";
        return barra; 
    }

    /**
     * Calcula el porcentaje de un numero de palabras con
     * respecto al total de palabras
     * @param repeticiones veces que se repite la palabra
     * @return porcentaje
     */
    private Double calculaPorcentaje(int repeticiones){
        return (double) (repeticiones*100)/total;
    }

    /**
     * Crea el svg de una rebanada del grafico de pastel
     * @param porcentaje porcentaje del total que ocupa la rebanada
     * @param color color del que se va a colorear
     * @return cadena svg
     */
    private String creaRebanada(Double porcentaje, String color){
        String s = "            <circle r='5' cx='10' cy='10' fill='transparent' \n" +
            "                stroke='" + color +  "' \n "+
            "                stroke-width='10' \n" +
            "                stroke-dasharray='" + Porcentaje(porcentaje) + " 31.4'/> \n";
        return s;
    }

}