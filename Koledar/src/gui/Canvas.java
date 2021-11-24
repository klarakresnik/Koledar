package gui;

import javax.swing.JPanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

import logika.logika;

@SuppressWarnings("serial")
public class Canvas extends JPanel{
	
	public String[] prazniki;
	
	public static int day;
	public static int month;
	public static int year;
	
	private Color barvaNedelja;
	private Color barvaPraznik;
	private Color barvaPrvaVrstica; // prva vrstica je glava tabele, vanjo zapisemo imena dnevov
	
	private double sirinaCrteRazmerje;
	private double prvaVrsticaRazmerje;
	
	public Canvas(int sirina, int visina) {
		this.setPreferredSize(new Dimension(sirina,visina));
		this.setBackground(new Color(255, 236, 179));
		this.barvaNedelja = new Color(0, 134, 179);
		this.barvaPraznik = new Color(179, 224, 255);
		this.barvaPrvaVrstica = new Color(255, 153, 0);
		
		this.sirinaCrteRazmerje = 0.004;
		this.prvaVrsticaRazmerje = 0.1;
	}
		
		
	private double columnWidth() {
		return (getWidth() - sirinaCrteRazmerje * getHeight()) / (7);
	}
	
	private double rowHeight() {
		double prvaVrstica = getHeight() * prvaVrsticaRazmerje;
		int steviloVrstic = logika.steviloTednovVMesecu(year, month);
		return (getHeight() - sirinaCrteRazmerje * getHeight() - prvaVrstica) / (steviloVrstic);
	}
		
	@Override
	protected void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		double w = columnWidth();
		double h = rowHeight();
		double sirinaCrte = sirinaCrteRazmerje * getHeight();
		double prvaVrstica = getHeight() * prvaVrsticaRazmerje;
		
		int firstDayInMonth = logika.danVTednu(year, month, 1);
		int weeksInMonth = logika.steviloTednovVMesecu(year, month);
		int daysInMonth = logika.steviloDniVMesecu(year, month);
		
//		obarvamo poligone posebnih dni
//		prva vrstica
		g2d.setColor(barvaPrvaVrstica);
		g2d.fillPolygon(new int[] {0, getWidth(), getWidth(), 0}, new int[] {0, 0, (int) prvaVrstica, (int) prvaVrstica}, 4);
		
//		nedelje
		g2d.setColor(barvaNedelja);
		int[] x_poly = new int[] {(int)(w * 6 + sirinaCrte / 2), 
				(int)(w * 7 + sirinaCrte/ 2),
				(int)(w * 7 + sirinaCrte/ 2),
				(int)(w * 6 + sirinaCrte / 2)};
		int[] y_poly = new int[] {0, 0, getHeight(), getHeight()};
		g2d.fillPolygon(x_poly, y_poly, 4);
		
//		prazniki
		g2d.setColor(barvaPraznik);
		// line je oblike "dd.mm.yyyy,t", kjer t pomeni tip praznika (ponavlja -> 1, ne ponavlja -> 0)
		for (String line : prazniki) {
			String[] dmyt = line.strip().split(",");
			int d = Integer.parseInt(dmyt[0].split("\\.")[0]);
			int m = Integer.parseInt(dmyt[0].split("\\.")[1]);
			int y = Integer.parseInt(dmyt[0].split("\\.")[2]);
			int tip = Integer.parseInt(dmyt[1]);
			if (tip == 0) {
				if (y == year && m == month) {
					Polygon p = vrniPoligon(year, month, d, w, h, prvaVrstica, sirinaCrte);
					g2d.fillPolygon(p);
				}
			} else if (m == month) {
					Polygon p = vrniPoligon(year, month, d, w, h, prvaVrstica, sirinaCrte);
					g2d.fillPolygon(p);
			}
		}
		
//		risemo mrezo in zapisujemo
		g2d.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke((float) sirinaCrte));
		
		Font f = new Font("SansSerif", Font.PLAIN, (int) (setFontSize(prvaVrstica, w)));
		g2d.setFont(f);
		
		String[] dnevi = new String[] {"PON", "TOR", "SRE", "ČET", "PET", "SOB", "NED"};
		
//		risemo stolpce
		for (int i = 0; i < 9; i++) {
			g2d.drawLine((int)(w * i + sirinaCrte/ 2), 0, (int)(w * i + sirinaCrte / 2), getHeight());
//			izpis dni v prvi vrstici
			if (i < 7) g2d.drawString(dnevi[i], (int) (2 * sirinaCrte + i * w),(int) (prvaVrstica - 2 * sirinaCrte));
		}
		
//		risemo vrstice
		for (int i = 0; i < weeksInMonth + 2; i++) {
			if (i == 0) g2d.drawLine(0, 0, getWidth(), 0);
			g2d.drawLine(0, (int)(h * i + sirinaCrte / 2 + prvaVrstica), getWidth(), (int)(h * i + sirinaCrte / 2 + prvaVrstica));
		}
		
//		zapisemo stevilko dneva v posamezen kvadratek
		for (int i = 1; i <= daysInMonth; i++) {
			g2d.drawString(String.valueOf(i), 
				(int) (w * ((i + firstDayInMonth - 2) % 7) + 2 * sirinaCrte), 
				(int) (h * Math.floor((i + firstDayInMonth - 2) / 7 + 1) - 2 * sirinaCrte + prvaVrstica));
		}
	}
	
//	vrne poligon, ki predstavlja pravokotnik na koledarju za podan dan 
	private Polygon vrniPoligon(int year, int month, int day, double w, double h, double prvaVrstica, double sirinaCrte) {
		int stolpec = logika.danVTednu(year, month, day);
		int vrstica = logika.tedenVMesecu(year, month, day);
		int[] x_poly = new int[] {(int) ((stolpec - 1) * w + sirinaCrte / 2), 
				(int) (stolpec * w + sirinaCrte / 2),
				(int) (stolpec * w + sirinaCrte / 2),
				(int) ((stolpec - 1) * w + sirinaCrte / 2)};
		int[] y_poly = new int[] {(int) ((vrstica - 1) * h + sirinaCrte / 2 + prvaVrstica), 
				(int) ((vrstica - 1) * h + sirinaCrte / 2 + prvaVrstica), 
				(int) ((vrstica) * h + sirinaCrte / 2 + prvaVrstica), 
				(int) ((vrstica) * h + sirinaCrte / 2 + prvaVrstica)};
		Polygon p = new Polygon(x_poly, y_poly, 4);
		return p;
	}
	
//	priredi velikost pisave glede na velikost celotnega platna
	private double setFontSize(double h, double w) {
		if (w < 65) return w * 0.4;
		else return h * 0.7;
	}
}
