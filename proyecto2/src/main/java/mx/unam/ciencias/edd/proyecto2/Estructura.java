package mx.unam.ciencias.edd.proyecto2;

public abstract class Estructura{

   public final String fin = "</svg>";

   /* método que nos devuleve el inicio de todo archivo SVG */
   public String inicio(int x, int y){
      return String.format("<?xml version='1.0' encoding='UTF-8' ?>  \n" +
      "<svg width='%d' height='%d'> \n", x, y);
   }


   /* regresa SVG de una linea */
   public String linea(double x1, double y1, double x2, double y2){
		return "	<line x1='" + x1 + "' y1='" + y1 +"' x2='"+x2+"' y2='"+y2+"' stroke='black' stroke-width='2' /> " + "\n";
   }

   /* regresa SVG de un texto */
	public String texto (String texto, double x, double y, String extra) {
		return "	<text x='"+ x +"' y='"+ y +"' font-size='20' "+ extra +">"+ texto +"</text>" + "\n";
	}

   /* regresa SVG de un numero*/
	public String numero (int n, double x, double y, String extra) {
		return "	<text x='"+ x +"' y='"+ y +"' font-size='25' "+ extra +">"+ n +"</text>" + "\n";
	}

   /* regresa SVG de un rectangulo*/
	public String rectangulo (double base, double altura, double x, double y) {
		return "	<rect x='"+ x +"' y='"+ y +"' width='"+ base +"' height='"+ altura +"' stroke='black' stroke-width='2.2' fill='white'/>" + "\n";
	}

   /* regresa SVG de un circulo*/
	public String circulo (double radio, double x, double y, String color) {
		String color_s = "";
		if (!color.equals("")) {
			color_s="fill='"+ color +"'";
		}
		return "	<circle cx='"+ x +"' cy='"+ y +"' r='"+ radio +"' "+ color_s +" stroke='black' stroke-width='2'/>" + "\n";
	}


	public String rectanguloConNumero (int n, double x, double y, int base, int altura) {
		return this.rectangulo(base, altura, x, y) + this.numero(n, x+10, y+20,"");
	}

	public String circuloConNumero (int n, double x, double y, int radio, String color, String colorLetra) {
      return this.circulo(radio, x, y, color) + this.numero(n, x, y+5, "text-anchor='middle' fill='"+ colorLetra +"'");
   }

   /* metodo que devuelve el texto svg de la estructura */
   public abstract String dibuja();
}
