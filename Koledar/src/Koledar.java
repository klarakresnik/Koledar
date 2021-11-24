import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import gui.Window;

public class Koledar {
	
//	Metoda odpre novo prazno okno
	public static void main(String[] args) throws IOException {
		
//		prebremo datoteka, da dobimo posebne datume za praznike
		BufferedReader vhod = new BufferedReader(new FileReader("prazniki.txt"));
		
		String[] prazniki = new String[16];
		int i = 0;
		while (vhod.ready()) {
			String line = vhod.readLine().trim();
			prazniki[i] = line;
			i++;
		} vhod.close();
		
		Window okno = new Window();
		okno.canvas.prazniki = prazniki;
		okno.pack();
		okno.setVisible(true);
	}
}