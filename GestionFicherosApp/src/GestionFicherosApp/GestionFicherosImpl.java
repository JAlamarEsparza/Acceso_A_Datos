package GestionFicherosApp;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import gestionficheros.FormatoVistas;
import gestionficheros.GestionFicheros;
import gestionficheros.GestionFicherosException;
import gestionficheros.TipoOrden;

//	Sergi Pastor Llopis

public class GestionFicherosImpl implements GestionFicheros {
	private File carpetaDeTrabajo = null;
	private Object[][] contenido;
	private int filas = 0;
	private int columnas = 3;
	private FormatoVistas formatoVistas = FormatoVistas.NOMBRES;
	private TipoOrden ordenado = TipoOrden.DESORDENADO;
		
	public GestionFicherosImpl() {
		carpetaDeTrabajo = File.listRoots()[0];
		actualiza();
	}

	private void actualiza() {
		String[] ficheros = carpetaDeTrabajo.list(); // obtener los nombres
		// calcular el número de filas necesario
		filas = ficheros.length / columnas;
		if (filas * columnas < ficheros.length) {
			filas++; // si hay resto necesitamos una fila más
		}

		// dimensionar la matriz contenido según los resultados

		contenido = new String[filas][columnas];
		// Rellenar contenido con los nombres obtenidos
		for (int i = 0; i < columnas; i++) {
			for (int j = 0; j < filas; j++) {
				int ind = j * columnas + i;
				if (ind < ficheros.length) {
					contenido[j][i] = ficheros[ind];
				} else {
					contenido[j][i] = "";
				}
			}
		}
	}

	@Override
	public void arriba() {
		if (carpetaDeTrabajo.getParentFile() != null) {
			carpetaDeTrabajo = carpetaDeTrabajo.getParentFile();
			actualiza();
		}
	}

	@Override
	public void creaCarpeta(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo, arg0);		
		
//		2 Excepciones, si el archivo existe y si se puede crear
		if (!carpetaDeTrabajo.canWrite()) {
			throw new GestionFicherosException("ERROR - No existe la carpeta padre");
		}else if(!carpetaDeTrabajo.exists()) {
			throw new GestionFicherosException("ERROR - No tienes permisos");
		//Accion
		}else {
			file.mkdir();
			actualiza();
		}
	}

	@Override
	public void creaFichero(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo, arg0);
		//Probar a crear fichero
		try {
			file.createNewFile();
			actualiza();
		//Si no funciona, que revise y escoja la excepcion adecuada
		} catch (IOException e) {
			if (!carpetaDeTrabajo.canWrite()) {
				throw new GestionFicherosException("ERROR - No tienes permisos");
			}else if(!carpetaDeTrabajo.exists()) {
				throw new GestionFicherosException("ERROR - No existe la carpeta padre");
			}

		}
		
	}

	@Override
	public void elimina(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo, arg0);
		//3 Excepciones, si se puede eliminar, si existe la carpeta padre y si ya existia el archivo.
		if (!carpetaDeTrabajo.canWrite()) {
			throw new GestionFicherosException("ERROR - No tienes permisos");
		}else if(!carpetaDeTrabajo.exists()) {
			throw new GestionFicherosException("ERROR - No existe la carpeta padre");
		}else if(!file.exists()) {
			throw new GestionFicherosException("ERROR - No existe el archivo");
		//Si no se lanzan excepciones, se elimina
		}else {
			file.delete();
			actualiza();
		}
		
	}

	@Override
	public void entraA(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo, arg0);
		if (!file.exists()) throw new GestionFicherosException(file + " - No es una ruta");
		if (!file.isDirectory()) throw new GestionFicherosException(file.getName() + "  - No es un directorio");
		if (!file.canRead()) throw new GestionFicherosException("No tienes permisos de lectura");

		carpetaDeTrabajo = file;
		actualiza();
	}

	@Override
	public int getColumnas() {
		return columnas;
	}

	@Override
	public Object[][] getContenido() {
		return contenido;
	}

	@Override
	public String getDireccionCarpeta() {
		return carpetaDeTrabajo.getAbsolutePath();
	}

	@Override
	public String getEspacioDisponibleCarpetaTrabajo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEspacioTotalCarpetaTrabajo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getFilas() {
		// TODO Auto-generated method stub
		return filas;
	}

	@Override
	public FormatoVistas getFormatoContenido() {
		return formatoVistas;
	}

	@Override
	public String getInformacion(String arg0) throws GestionFicherosException {
		File file = new File(carpetaDeTrabajo, arg0);		
		
//		2 Excepciones, si el archivo existe y si se puede leer
		if (!file.exists()) throw new GestionFicherosException("ERROR - EL ARCHIVO NO EXISTE");
		if (!file.canRead()) throw new GestionFicherosException("ERROR - NO SE PUEDE LEER EL ARCHIVO");
		
		StringBuilder sb = new StringBuilder();
		SimpleDateFormat date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		
		
		sb.append("--------------------INFORMACION DEL FICHERO-----------------" + "\n\n");
		
//		He usado "if cortos" en lugar de la estructura normal de un if. Un ejemplo: [   file.isDirectory() ? "Si" : "No"  ]
//		El valor de la interrogacion es "Si" y el de los puntos "No".
//		Si el Archivo es un directorio,  Pasara al "Si" si el valor es true. Si el valor fuera false, pasaria al "No".
		
//		Nombre
		sb.append("Nombre: " + file.getName() + "\n\n");
		
//		Tipo		
		sb.append("Tipo: " + (file.isDirectory() ? "Directorio\n\n" : "Fichero\n\n"));
		
//		Ubicacion
		sb.append("Ubicacion: " + file.getAbsolutePath() + "\n\n");
		
//		Fecha modificacion con el SimpleDateFormat
		sb.append("Ultima modificacion: " + date.format(file.lastModified()) + "\n\n");
		
//		Oculto	
		sb.append("Esta oculto? - " + (file.isHidden() ? "SI\n\n" : "NO\n\n"));

//		Tamanyo fichero en formato Bytes
		sb.append(file.isFile() ? ("Tamanyo del fichero: " + file.length() / 8 + " bytes" + "\n") : "");
		
//		3 tipos de espacio y su espacio + cantidad elementos
		if (file.isDirectory()) {
			sb.append(file.getName() + " - Contiene: " + file.list().length + " elementos" + "\n");
			sb.append("Espacio libre: " + (file.getFreeSpace() / 8 / 1048576) + " MB" + "\n");
			sb.append("Espacio disponible: " + (file.getUsableSpace() / 8 / 1048576) + " MB" + "\n");
			sb.append("Espacio total: " + (file.getTotalSpace() / 8 / 1048576) + " MB" + "\n\n");
		}
		
		String cadena = sb.toString();
		return cadena;
	}

	@Override
	public boolean getMostrarOcultos() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getNombreCarpeta() {
		return carpetaDeTrabajo.getName();	
	}

	@Override
	public TipoOrden getOrdenado() {
		return ordenado;
	}

	@Override
	public String[] getTituloColumnas() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getUltimaModificacion(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String nomRaiz(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int numRaices() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void renombra(String arg0, String arg1) throws GestionFicherosException {
		File d = new File(carpetaDeTrabajo, arg0);
		//Si se tiene permisos y si la carpeta padre existe
		if (!carpetaDeTrabajo.canWrite()) {
			throw new GestionFicherosException("ERROR - No tienes permisos");
		}else if(!carpetaDeTrabajo.exists()) {
			throw new GestionFicherosException("ERROR - No existe la carpeta padre");
		//Si no da error, renombrará el archivo
		}else {
			File f = new File(carpetaDeTrabajo, arg1);
			d.renameTo(f);
			actualiza();
		}
		
	}

	@Override
	public boolean sePuedeEjecutar(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sePuedeEscribir(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sePuedeLeer(String arg0) throws GestionFicherosException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setColumnas(int arg0) {
		columnas = arg0;
		
	}

	@Override
	public void setDirCarpeta(String arg0) throws GestionFicherosException {
		File file = new File(arg0);
		
		if (!file.exists()) {
			throw new GestionFicherosException(file + " - No es una ruta");
		}
		if (!file.isDirectory()) {
			throw new GestionFicherosException(file.getName() + "  - No es un directorio");
		}
		if (!file.canRead()) {
			throw new GestionFicherosException("No tienes permisos de lectura");
		}
		
		carpetaDeTrabajo = file;
		actualiza();
		
	}

	@Override
	public void setFormatoContenido(FormatoVistas arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setMostrarOcultos(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOrdenado(TipoOrden arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSePuedeEjecutar(String arg0, boolean arg1) throws GestionFicherosException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSePuedeEscribir(String arg0, boolean arg1) throws GestionFicherosException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSePuedeLeer(String arg0, boolean arg1) throws GestionFicherosException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUltimaModificacion(String arg0, long arg1) throws GestionFicherosException {
		// TODO Auto-generated method stub
		
	}
}