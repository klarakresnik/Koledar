package gui;

import java.awt.event.*;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.util.HashMap;

import logika.logika;

@SuppressWarnings("serial")
public class Window extends JFrame implements ActionListener, ItemListener {
	
	public Canvas canvas;
	
	private static String[] months; // v pomoč pri pretverjanju mesecov iz stevilk v nize 
	private static HashMap<String, Integer> monthsToNumber; // in obratno
	
	private static JTextField vpisanDatum;
	private static JButton potrdi; // z gumbom si bomo pomagali pri branju besedilnega polja
	private static JComboBox<String> mesec, leto;
	private static JLabel trenutnoPrikazan;
	
	public Window() {
		
		setTitle("Koledar");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
//		 vse elemente bomo dodajali tej plosci
		JPanel glavnaPlosca = new JPanel(); 
		glavnaPlosca.setLayout(new BoxLayout(glavnaPlosca, BoxLayout.Y_AXIS));
		this.add(glavnaPlosca);
		
//		 plosca v kateri bo polje za besedilo, gumb in dva combo box-a
		JPanel p1 = new JPanel(); 
	
		Integer[] datum = logika.danasnjiDatum();
		
		vpisanDatum = new JTextField("", 10);
		
		potrdi = new JButton("Potrdi");
		potrdi.addActionListener(this);
		
		p1.add(vpisanDatum);
		p1.add(potrdi);
		
		months = new String[] {"Januar", "Februar", "Marec", "April",
				"Maj", "Junij", "Julij", "Avgust", "September", 
				"Oktober", "November", "December"};
		
//		 v combo box-u bomo prikazovali leta do 1990
		String[] years = new String[datum[2] - 1990];
		for (int i = datum[2]; i > 1990; i--) {
			years[datum[2] - i] = String.valueOf(i);
		}
		
		mesec = new JComboBox<String>(months);
		leto = new JComboBox<String>(years);
		
		mesec.setEditable(true);
		leto.setEditable(true);
		
		mesec.setSelectedItem(months[datum[1] - 1]);
		leto.setSelectedItem(datum[2]);
		
		mesec.addItemListener(this);
		leto.addItemListener(this);
		
		p1.add(mesec);
		p1.add(leto);
		
		glavnaPlosca.add(p1);
		
//		plosca za oznako trenutno prikazanega meseca
		JPanel p2 = new JPanel();

		trenutnoPrikazan = new JLabel(months[datum[1] - 1] + " " + datum[2], 10);
		
		p2.add(trenutnoPrikazan);
		glavnaPlosca.add(p2);
		
		Canvas.year = datum[2];
		Canvas.month = datum[1];
		canvas = new Canvas(300, 300);
		glavnaPlosca.add(canvas);

		monthsToNumber = new HashMap<String, Integer>();
		monthsToNumber.put("Januar", 1);
		monthsToNumber.put("Februar", 2);
		monthsToNumber.put("Marec", 3);
		monthsToNumber.put("April", 4);
		monthsToNumber.put("Maj", 5);
		monthsToNumber.put("Junij", 6);
		monthsToNumber.put("Julij", 7);
		monthsToNumber.put("Avgust", 8);
		monthsToNumber.put("September", 9);
		monthsToNumber.put("Oktober", 10);
		monthsToNumber.put("November", 11);
		monthsToNumber.put("December", 12);
	}
	
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == mesec) {
			int month = monthsToNumber.get(mesec.getSelectedItem());
			Canvas.month = month;
			trenutnoPrikazan.setText(months[month - 1] + " " + Canvas.year);
		} else if (e.getSource() == leto) {
			int year = Integer.parseInt((String) leto.getSelectedItem());
			Canvas.year = year;
			trenutnoPrikazan.setText(months[Canvas.month - 1] + " " + year);
		}
		canvas.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == potrdi) {
			if (vpisanDatum.getText().matches("\\d{1,2}\\.\\d{1,2}\\.\\d{4}")) {
				int day = 0;
				int month = 0;
				int year = 0;
				String[] vnos = vpisanDatum.getText().split("\\.");
				try {
					day = Integer.parseInt(vnos[0]);
					month = Integer.parseInt(vnos[1]);
					year = Integer.parseInt(vnos[2]);
				} catch (Exception NumberFormatException) {
					JOptionPane.showMessageDialog(null, 
							"Vnesite datum v obliki dd.mm.yyyy.",
							"Obvestilo", JOptionPane.INFORMATION_MESSAGE);
				}
				if (year > 0 && month > 0 && day > 0 &&
						(month <= 12) &&
						(day <= logika.steviloDniVMesecu(year, month)) &&
						year <= logika.danasnjiDatum()[2]) {					
					Canvas.month = month;
					Canvas.year = year;
					trenutnoPrikazan.setText(months[month - 1] + " " + year);
					canvas.repaint();
				} else {
					JOptionPane.showMessageDialog(null, 
							"Vnesite veljaven datum.",
							"Obvestilo", JOptionPane.INFORMATION_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(null, 
						"Vnesite datum v obliki dd.mm.yyyy.",
						"Obvestilo", JOptionPane.INFORMATION_MESSAGE);
			}
        }	
	}
}
