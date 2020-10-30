	package model;

import java.util.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class GestionDatos {

	public GestionDatos() {

	}

	public static BufferedReader abrirFicheros (String fichero1) throws FileNotFoundException {
		FileReader leerArchivo = new FileReader(fichero1);
		BufferedReader leerBuffer = new BufferedReader(leerArchivo);
		return leerBuffer;
	}
	
	public static void cerrarFicheros (BufferedReader buffer1) throws IOException {
		buffer1.close();
	}
	
	public static boolean compararContenido(String fichero1, String fichero) throws IOException {

		BufferedReader[] leerBuffer = new BufferedReader[2];
		leerBuffer[0] = abrirFicheros(fichero1);
		leerBuffer[1] = abrirFicheros(fichero);
		
		String texto = leerBuffer[0].readLine();
		String texto2 = leerBuffer[1].readLine();
		
		while(texto != null && texto2 != null) {
			if(!texto.equals(texto2)) {
				return false;
			} else {
				texto = leerBuffer[0].readLine();
				texto2 = leerBuffer[1].readLine();
			}
		}
		cerrarFicheros(leerBuffer[0]);
		cerrarFicheros(leerBuffer[1]);
		
		return true;
	}
	
	public static int buscarPalabra (String fichero1, String palabra, boolean primera_aparicion) throws IOException {

		BufferedReader[] leerBuffer = new BufferedReader[1];
		leerBuffer[0] = abrirFicheros(fichero1);
		String texto = leerBuffer[0].readLine();
		
		int line = 1, finalLine = -1;
		
		List<String> palabras = new ArrayList<String>();
		
		if(primera_aparicion == true) {
			while(texto != null) {
				palabras = Arrays.asList(texto.split(" "));
				if(palabras.contains(palabra)) {
					return line;
				}
				texto = leerBuffer[0].readLine();
				line++;
			}
		} else {
			while(texto != null) {
				palabras = Arrays.asList(texto.split(" "));
				if(palabras.contains(palabra)) {
					return line;
				}
				texto = leerBuffer[0].readLine();
				line++;
			}
			return finalLine;
		}
		cerrarFicheros(leerBuffer[0]);
		return -1;
	}	

}
