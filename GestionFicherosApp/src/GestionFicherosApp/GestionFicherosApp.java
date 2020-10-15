package GestionFicherosApp;

import gestionficheros.MainGUI;

//	Sergi Pastor Llopis

public class GestionFicherosApp {

	public static void main(String[] args) {

		GestionFicherosImpl getFicherosImpl = new GestionFicherosImpl();
		new MainGUI(getFicherosImpl).setVisible(true);
	}
}