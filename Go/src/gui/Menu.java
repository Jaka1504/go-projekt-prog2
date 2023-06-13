package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import logika.Igra;
import vodja.Vodja;
import vodja.Vodja.VrstaIgralca;

public class Menu extends JMenuBar implements ActionListener{
	
	JButton novaIgra;
	JComboBox<String> igralecB;
	JComboBox<String> igralecČ;
	JComboBox<Integer> sirina;
	JComboBox<Integer> visina;
	
	public Menu() {
		super();
	    novaIgra = new JButton("Ustvari novo igro");
	    novaIgra.addActionListener(this);
	    add(novaIgra);
	    JLabel beli = new JLabel("Beli igralec:");
	    add(beli);
	    String[] igralci = {"Človek","Računalnik"};
	    igralecB = new JComboBox<String>(igralci);
	    igralecB.addActionListener(this);
	    add(igralecB);
	    JLabel črni = new JLabel("Črni igralec:");
	    add(črni);
	    igralecČ = new JComboBox<String>(igralci);
	    igralecČ.addActionListener(this);
	    add(igralecČ);
	    Integer[] stevilke = {5,6,7,8,9,10,11,12,13,14,15,16};
	    JLabel napisSirina = new JLabel("Širina:");
	    add(napisSirina);
	    sirina = new JComboBox<Integer>(stevilke);
	    sirina.setSelectedItem(9);
	    sirina.addActionListener(this);
	    add(sirina);
	    JLabel napisVisina = new JLabel("Višina:");
	    add(napisVisina);
	    visina = new JComboBox<Integer>(stevilke);
	    visina.setSelectedItem(9);
	    visina.addActionListener(this);
	    add(visina);
	}
	
	@Override
    public void actionPerformed(ActionEvent e) {
		if (e.getSource() == novaIgra) {
			String igralecBeli = (String) igralecB.getSelectedItem();
			String igralecČrni = (String) igralecČ.getSelectedItem();
			int s = (int) sirina.getSelectedItem();
			int v = (int) visina.getSelectedItem();
			if (igralecBeli == "Človek" && igralecČrni == "Človek") {
				Vodja.vrstiIgralcev =
        		new EnumMap<Igra.BarvaIgralca, VrstaIgralca>(Igra.BarvaIgralca.class);
				Vodja.vrstiIgralcev.put(Igra.BarvaIgralca.BELI, VrstaIgralca.CLOVEK);
				Vodja.vrstiIgralcev.put(Igra.BarvaIgralca.CRNI, VrstaIgralca.CLOVEK);
				Vodja.ustvariNovoIgro(s, v);
			}
			else if (igralecBeli == "Računalnik" && igralecČrni == "Človek") {
	            Vodja.vrstiIgralcev =
	            		new EnumMap<Igra.BarvaIgralca, VrstaIgralca>(Igra.BarvaIgralca.class);
	            Vodja.vrstiIgralcev.put(Igra.BarvaIgralca.CRNI, VrstaIgralca.CLOVEK);
	            Vodja.vrstiIgralcev.put(Igra.BarvaIgralca.BELI, VrstaIgralca.RACUNALNIK);
	            Vodja.ustvariNovoIgro(s, v);
			}
			else if (igralecBeli == "Človek" && igralecČrni == "Računalnik") { 
	            Vodja.vrstiIgralcev =
	            		new EnumMap<Igra.BarvaIgralca, VrstaIgralca>(Igra.BarvaIgralca.class);
	            Vodja.vrstiIgralcev.put(Igra.BarvaIgralca.CRNI, VrstaIgralca.RACUNALNIK);
	            Vodja.vrstiIgralcev.put(Igra.BarvaIgralca.BELI, VrstaIgralca.CLOVEK);
	            Vodja.ustvariNovoIgro(s, v);
			}
			else if (igralecBeli == "Računalnik" && igralecČrni == "Računalnik") { 
	            Vodja.vrstiIgralcev =
	            		new EnumMap<Igra.BarvaIgralca, VrstaIgralca>(Igra.BarvaIgralca.class);
	            Vodja.vrstiIgralcev.put(Igra.BarvaIgralca.BELI, VrstaIgralca.RACUNALNIK);
	            Vodja.vrstiIgralcev.put(Igra.BarvaIgralca.CRNI, VrstaIgralca.RACUNALNIK);
	            Vodja.ustvariNovoIgro(s, v);
			}
        }
	}
}
