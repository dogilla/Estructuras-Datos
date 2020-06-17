package mx.unam.ciencias.edd.proyecto3.dibujos;

public abstract class SVGEstructura{

   /* etiqueta para cerrar el archivo svg.s */
   public final String fin = "</svg>";

   /* colores disponibles */
   public String[] colores = {"black","white","red", "blue", "pink", "yellow",
		"#DAA520", "#ADFF2F", "#CD5C5C", "#4B0082", "#800000", "#40E0D0",
		"#00FFFF", "#2E8B57", "#8FBC8F", "#FFDEAD"};

   /**
	* Método que genera las primeras lineas del archivo SVG.
	* @param x tamaño a lo ancho que va a tener todo el dibujo (eje x)
	* @param y tamaño a lo largo que va a tener todo el dibujo (eje y)
	* @return
    */
   public String inicio(int x, int y){
      return String.format("<?xml version='1.0' encoding='UTF-8' ?>  \n" +
      "<svg width='%d' height='%d'> \n", x, y);
   }

   /**
	* Regresa la expresion svg de una linea
	* @param x1 eje x del inicio de la linea
	* @param y1 eje y del inicio de la linea
	* @param x2 eje x del final de la linea
	* @param y2 eje y del final de la linea
	* @return
    */
   public String linea(double x1, double y1, double x2, double y2){
		return "	<line x1='" + x1 + "' y1='" + y1 +"' x2='"+x2+"' y2='"+y2+"' stroke='black' stroke-width='2' /> " + "\n";
   }

   /**
	* Regresa la expresion svg de un texto
	* @param texto el contenido del texto
	* @param x eje x de la posición del texto
	* @param y eje y de la posición del texto
	* @param extra contenido extra del texto, el color por ejemplo.
	* @return expesion svg
    */
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

	public String rectanguloConTexto(String texto, double x, double y, int base, int altura) {
		return rectangulo(base, altura, x, y);
	}

	public String circuloConNumero (int n, double x, double y, int radio, String color, String colorLetra) {
	  return this.circulo(radio, x, y, color) + this.numero(n, x, y+5, "text-anchor='middle' fill='"+ colorLetra +"'");
	}

	public String circuloConTexto(String s, double x, double y, int radio, String color, String colorLetra){
		return this.circulo(radio, x, y, color) + texto(s, x, y+5, colorLetra);
	}

   /* metodo que devuelve el texto svg de la estructura */
   public abstract String dibuja();
}
