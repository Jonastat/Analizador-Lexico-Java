package Analizador;

import java.util.ArrayList;

/**
 * @author Jonathan Pacalagua Morales,
 * Estudiante de Ingeniería de Sistemas y Computación,
 * Universidad del Quindío.
 */
public class AnalizadorLexico {

	private String codigoFuente;
	private ArrayList<Token> listaTokens;
	private char caracterActual;
	private int posicionActual, filaActual, columnaActual;
	private char finCodigo;

	public AnalizadorLexico(String codigoFuente) {
		this.codigoFuente = codigoFuente;
		this.listaTokens = new ArrayList<Token>();
		this.caracterActual = codigoFuente.charAt(posicionActual);
		this.finCodigo = 0;
	}

	/**
	 * Método que analiza el código fuente.
	 */
	public void analizar() {

		while (caracterActual != finCodigo) {

//			System.out.println(posicionActual);
			if (caracterActual == ' ' || caracterActual == '\t' || caracterActual == '\n') {
				obtenerSgteCaracter();
				continue;
			}
			if (esIdentificadorPalabraReservada())
				continue;
			if (esOperadorLogicoRelacional())
				continue;
			if (esNumerico())
				continue;
			if (esCadenaCaracteres())
				continue;
			if (esOperadorAritIncreDecreAsig())
				continue;
			if (esParentesis())
				continue;
			if (esLlave())
				continue;
			if (esFinSentencia())
				continue;
			if (esSeparador())
				continue;
			if (esHexadecimal())
				continue;
			if (esComentario())
				continue;

			listaTokens.add(new Token("" + caracterActual, Categoria.DESCONOCIDO, filaActual, columnaActual));
			obtenerSgteCaracter();

		}

	}
	
	/**
	 * Método que valida si una palabra es un número hexadecimal, la palabra debe empezar con el caracter ‼ (alt + 19) y 
	 * debe estar concatenado a cualquier caracter del conjunto de sintáxis de un 
	 * número hexadecimal S={0, 1, 2, 3, 4, 5, 6, 7, 8, 9, A, B, C, D, E, F}, ((‼)uS)(S)*.
	 * @return true or false
	 */
	public boolean esHexadecimal() {
		
		if (caracterActual == '‼') {
			String palabra = "";
			int fila = filaActual;
			int columna = columnaActual;

			palabra += caracterActual;
			obtenerSgteCaracter();
			if (Character.isDigit(caracterActual) || caracterActual == 'A' || caracterActual == 'B' || caracterActual == 'C' || caracterActual == 'D'
					|| caracterActual == 'E' || caracterActual == 'F') {
				palabra += caracterActual;
				obtenerSgteCaracter();
				if (Character.isDigit(caracterActual)) {
					palabra += caracterActual;
					obtenerSgteCaracter();
					while (Character.isDigit(caracterActual) || caracterActual == 'A' || caracterActual == 'B' || 
							caracterActual == 'C' || caracterActual == 'D' || caracterActual == 'E' || caracterActual == 'F') {
						palabra += caracterActual;
						obtenerSgteCaracter();
					}
					listaTokens.add(new Token(palabra, Categoria.HEXADECIMAL, fila, columna));
					return true;
				}
			}
			listaTokens.add(new Token(palabra, Categoria.DESCONOCIDO, fila, columna));
			return true;
		}
		return false;
	}

	/**
	 * Método que valida si una palabra es un número decimal, la palabra debe empezar con un dígito y 
	 * debe estar concatenado con cualquier cantidad de dígitos, si no hay un punto (.) de por 
	 * medio en la palabra lo categoriza como ENTERO (D)(D)*, de lo contrario REAL (D)(D)*(.)(D)(D)*.
	 * @return true or false
	 */
	public boolean esNumerico() {

		if (Character.isDigit(caracterActual)) {

			String palabra = "";
			int fila = filaActual;
			int columna = columnaActual;

			palabra += caracterActual;
			obtenerSgteCaracter();

			while (Character.isDigit(caracterActual)) {
				palabra += caracterActual;
				obtenerSgteCaracter();
			}
			if (caracterActual == '.') {
				palabra+=caracterActual;
				obtenerSgteCaracter();
				if (Character.isDigit(caracterActual)) {
					palabra += caracterActual;
					obtenerSgteCaracter();
					while (Character.isDigit(caracterActual)) {
						palabra += caracterActual;
						obtenerSgteCaracter();
					}
					listaTokens.add(new Token(palabra, Categoria.REAL, fila, columna));
					return true;
				}
			}

			listaTokens.add(new Token(palabra, Categoria.ENTERO, fila, columna));
			return true;
		}
		return false;
	}

	/**
	 * Método que valida si una palabra es un operador lógico (!)u(&&)u(||) 
	 * o relacional (!=)u(==)u(<)u(>)u(<=)u(>=).
	 * @return true or false
	 */
	public boolean esOperadorLogicoRelacional() {

		if (caracterActual == '!') {

			String palabra = "";
			int fila = filaActual;
			int columna = columnaActual;

			palabra += caracterActual;
			obtenerSgteCaracter();

			if (caracterActual == '=') {
				palabra += caracterActual;
				obtenerSgteCaracter();
				listaTokens.add(new Token(palabra, Categoria.OPERADOR_RELACIONAL, fila, columna));
				return true;
			}
			
			listaTokens.add(new Token(palabra, Categoria.OPERADOR_LOGICO, fila, columna));
			return true;

		}

		if (caracterActual == '=') {

			String palabra = "";
			int fila = filaActual;
			int columna = columnaActual;

			palabra += caracterActual;
			obtenerSgteCaracter();

			if (caracterActual == '=') {
				palabra += caracterActual;
				obtenerSgteCaracter();
				listaTokens.add(new Token(palabra, Categoria.OPERADOR_RELACIONAL, fila, columna));
				return true;
			}
			
			listaTokens.add(new Token(palabra, Categoria.OPERADOR_DE_ASIGNACION, fila, columna));
			return true;

		}

		if (caracterActual == '<' || caracterActual == '>') {

			String palabra = "";
			int fila = filaActual;
			int columna = columnaActual;

			palabra += caracterActual;
			obtenerSgteCaracter();

			if (caracterActual == '=') {
				palabra += caracterActual;
				obtenerSgteCaracter();
				listaTokens.add(new Token(palabra, Categoria.OPERADOR_RELACIONAL, fila, columna));
				return true;
			}
			
			listaTokens.add(new Token(palabra, Categoria.OPERADOR_RELACIONAL, fila, columna));
			return true;

		}

		if (caracterActual == '&') {

			String palabra = "";
			int fila = filaActual;
			int columna = columnaActual;

			palabra += caracterActual;
			obtenerSgteCaracter();

			if (caracterActual == '&') {
				palabra += caracterActual;
				obtenerSgteCaracter();
				listaTokens.add(new Token(palabra, Categoria.OPERADOR_LOGICO, fila, columna));
				return true;
			}

		}

		if (caracterActual == '|') {

			String palabra = "";
			int fila = filaActual;
			int columna = columnaActual;

			palabra += caracterActual;
			obtenerSgteCaracter();

			if (caracterActual == '|') {
				palabra += caracterActual;
				obtenerSgteCaracter();
				listaTokens.add(new Token(palabra, Categoria.OPERADOR_LOGICO, fila, columna));
				return true;
			}

		}

		return false;

	}

	/**
	 * Método que valida si una palabra es un identificador ($u_uL)($u_uLuD)* o palabra reservada del lenguaje 
	 * (Entero)u(Real)u(Para)u(Mientras)u(Private)u(Public)u(Paquete)u(Importar)u(Clase)u(Return)u(Break).
	 * @return true or false
	 */
	public boolean esIdentificadorPalabraReservada() {

		if (Character.isLetter(caracterActual) || caracterActual == '$' || caracterActual == '_') {

			String palabra = "";
			int fila = filaActual;
			int columna = columnaActual;

			palabra += caracterActual;
			obtenerSgteCaracter();

			while (Character.isLetter(caracterActual) || caracterActual == '$' || caracterActual == '_'
					|| Character.isDigit(caracterActual)) {

				palabra += caracterActual;
				obtenerSgteCaracter();

			}
			if (palabraReservada(palabra)) {
				listaTokens.add(new Token(palabra, Categoria.PALABRA_RESERVADA, fila, columna));
				return true;
			} else {

				listaTokens.add(new Token(palabra, Categoria.IDENTIFICADOR, fila, columna));
				return true;
			}

		}

		return false;

	}

	/**
	 * Método que valida si una palabra es una cadena de caracteres S={Ascii}, (")(S)*(").
	 * @return true or false
	 */
	public boolean esCadenaCaracteres() {

		if (caracterActual == '\"') {

			String palabra = "";
			int fila = filaActual;
			int columna = columnaActual;

			palabra += caracterActual;
			obtenerSgteCaracter();

			while (caracterActual != '\"') {

				palabra += caracterActual;
				obtenerSgteCaracter();
				if (caracterActual==finCodigo) {
					if (caracterActual != '\"') {
						listaTokens.add(new Token(palabra, Categoria.CADENA_CARACTERES_SIN_CERRAR, fila, columna));
						return true;
					}
				}
			}
			palabra += caracterActual;
			obtenerSgteCaracter();
			listaTokens.add(new Token(palabra, Categoria.CADENA_CARACTERES, fila, columna));
			return true;

		}

		return false;

	}
	
	/**
	 * Método que valida si una palabra es un paréntesis de apertura o cierre (()u()).
	 * @return true or false
	 */
	public boolean esParentesis() {

		if (caracterActual == '(' || caracterActual == ')') {

			String palabra = "";
			int fila = filaActual;
			int columna = columnaActual;

			palabra += caracterActual;
			obtenerSgteCaracter();

			listaTokens.add(new Token(palabra, Categoria.PARENTESIS_APERTURA_CIERRE, fila, columna));
			return true;

		}

		return false;

	}
	
	/**
	 * Método que valida si una palabra es una llave de apertura o cierre ({)u(}).
	 * @return true or false
	 */
	public boolean esLlave() {

		if (caracterActual == '{' || caracterActual == '}') {

			String palabra = "";
			int fila = filaActual;
			int columna = columnaActual;

			palabra += caracterActual;
			obtenerSgteCaracter();

			listaTokens.add(new Token(palabra, Categoria.LLAVE_APERTURA_CIERRE, fila, columna));
			return true;

		}

		return false;

	}
	
	/**
	 * Método que valida si una palabra es un fin de sentencia (;).
	 * @return true or false
	 */
	public boolean esFinSentencia() {

		if (caracterActual == ';') {

			String palabra = "";
			int fila = filaActual;
			int columna = columnaActual;

			palabra += caracterActual;
			obtenerSgteCaracter();

			listaTokens.add(new Token(palabra, Categoria.TERMINAL_FIN_SENTENCIA, fila, columna));
			return true;

		}

		return false;

	}
	
	/**
	 * Método que valida si una palabra es un separador (,).
	 * @return true or false
	 */
	public boolean esSeparador() {

		if (caracterActual == ',') {

			String palabra = "";
			int fila = filaActual;
			int columna = columnaActual;

			palabra += caracterActual;
			obtenerSgteCaracter();

			listaTokens.add(new Token(palabra, Categoria.SEPARADOR, fila, columna));
			return true;

		}

		return false;

	}
	
	/**
	 * Método que valida si una palabra (línea de código) es un comentario S={Ascii}, (#)(S)*(\n).
	 * @return true or false
	 */
	public boolean esComentario() {

		if (caracterActual == '#') {

			String palabra = "";
			int fila = filaActual;
			int columna = columnaActual;

			palabra += caracterActual;
			obtenerSgteCaracter();

			while (fila == filaActual) {

				palabra += caracterActual;
				obtenerSgteCaracter();

			}
			
			listaTokens.add(new Token(palabra, Categoria.COMENTARIO, fila, columna));
			return true;

		}

		return false;

	}

	/**
	 * Método que valida si una palabra es un operador aritmético (+)u(-)u(*)u(/)u(%), 
	 * de incremento (++), decremento (--) o de asignación (=)u(+=)u(-=)u(*=)u(/=)u(%=).
	 * @return true or false
	 */
	public boolean esOperadorAritIncreDecreAsig() {
		
		if (caracterActual == '*' || caracterActual == '/' || caracterActual == '%') {

			String palabra = "";
			int fila = filaActual;
			int columna = columnaActual;

			palabra += caracterActual;
			obtenerSgteCaracter();
			
			if (caracterActual == '=') {
				palabra += caracterActual;
				obtenerSgteCaracter();
				listaTokens.add(new Token(palabra, Categoria.OPERADOR_DE_ASIGNACION, fila, columna));
				return true;
			}

			listaTokens.add(new Token(palabra, Categoria.OPERADOR_ARTIMETICO, fila, columna));
			return true;
		}

		if (caracterActual == '+') {

			String palabra = "";
			int fila = filaActual;
			int columna = columnaActual;

			palabra += caracterActual;
			obtenerSgteCaracter();
			
			if (caracterActual == '=') {
				palabra += caracterActual;
				obtenerSgteCaracter();
				listaTokens.add(new Token(palabra, Categoria.OPERADOR_DE_ASIGNACION, fila, columna));
				return true;
			}
			
			if (caracterActual == '+') {
				palabra += caracterActual;
				obtenerSgteCaracter();
				listaTokens.add(new Token(palabra, Categoria.OPERADOR_DE_INCREMENTO_DECREMENTO, fila, columna));
				return true;
			}
			listaTokens.add(new Token(palabra, Categoria.OPERADOR_ARTIMETICO, fila, columna));
			return true;
		}
		
		if (caracterActual == '-') {

			String palabra = "";
			int fila = filaActual;
			int columna = columnaActual;

			palabra += caracterActual;
			obtenerSgteCaracter();
			
			if (caracterActual == '=') {
				palabra += caracterActual;
				obtenerSgteCaracter();
				listaTokens.add(new Token(palabra, Categoria.OPERADOR_DE_ASIGNACION, fila, columna));
				return true;
			}
			
			if (caracterActual == '-') {
				palabra += caracterActual;
				obtenerSgteCaracter();
				listaTokens.add(new Token(palabra, Categoria.OPERADOR_DE_INCREMENTO_DECREMENTO, fila, columna));
				return true;
			}
			
			listaTokens.add(new Token(palabra, Categoria.OPERADOR_ARTIMETICO, fila, columna));
			return true;
		}
		if (caracterActual == '=') {

			String palabra = "";
			int fila = filaActual;
			int columna = columnaActual;

			palabra += caracterActual;
			obtenerSgteCaracter();
	
			listaTokens.add(new Token(palabra, Categoria.OPERADOR_DE_ASIGNACION, fila, columna));
			return true;

		}

		return false;

	}

	/**
	 * Método que obtiene el siguiente caracter de una palabra.
	 * @return true or false
	 */
	public void obtenerSgteCaracter() {
		posicionActual++;
		if (posicionActual < codigoFuente.length()) {

			if (caracterActual == '\n') {
				filaActual++;
				columnaActual = 0;
			} else {
				columnaActual++;
			}

			caracterActual = codigoFuente.charAt(posicionActual);
		} else {
			caracterActual = finCodigo;
		}

	}


	/**
	 * Método que compara si una palabra que entra como parámetro es una de las palabras reservadas del lenguaje, 
	 * la palabra debe coincidir exactamente en cuanto a mayúsculas y minúsculas ("Entero", "Real", 
	 * "Para", "Mientras", "Private", "Public", "Paquete", "Importar", "Clase", "Return", "Break").
	 * @param String cadena
	 * @return boolean true or false
	 */
	public static boolean palabraReservada(String cadena) {
		if (cadena.equals("Entero"))
			return true;
		else if (cadena.equals("Real"))
			return true;
		else if (cadena.equals("Para"))
			return true;
		else if (cadena.equals("Mientras"))
			return true;
		else if (cadena.equals("Private"))
			return true;
		else if (cadena.equals("Public"))
			return true;
		else if (cadena.equals("Paquete"))
			return true;
		else if (cadena.equals("Importar"))
			return true;
		else if (cadena.equals("Clase"))
			return true;
		else if (cadena.equals("Return"))
			return true;
		else if (cadena.equals("Break"))
			return true;
		else
			return false;
	}

	public ArrayList<Token> getListaTokens() {
		return listaTokens;
	}

}
