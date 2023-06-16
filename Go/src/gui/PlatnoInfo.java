package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import logika.Igra;
import vodja.Vodja;
import vodja.Vodja.VrstaIgralca;

@SuppressWarnings("serial")
public class PlatnoInfo extends JPanel implements ActionListener{

	private JFrame frame;
	
	JComboBox<String> igralecB;
	JComboBox<String> igralecČ;
	JComboBox<Integer> sirina;
	JComboBox<Integer> visina;
	JButton novaIgra;
	
	public PlatnoInfo(JFrame frame) {
		this.frame = frame;
		setPreferredSize(new Dimension(100, 100));
		
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
	    
	    novaIgra = new JButton("Prični igro");
	    novaIgra.addActionListener(this);
	    this.add(novaIgra);
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
		frame.dispose();
        }
	}

}
