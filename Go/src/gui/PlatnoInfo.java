package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
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
	private static Map<String, Integer> mozneTezavnosti;
	
	JComboBox<String> igralecB;
	JComboBox<String> igralecC;
	JComboBox<Integer> sirina;
	JComboBox<Integer> visina;
	JComboBox<String> tezavnost;
	JButton novaIgra;
	
	public PlatnoInfo(JFrame frame) {
		this.frame = frame;
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		JLabel beli = new JLabel("Beli igralec:");
		beli.setAlignmentX(Component.LEFT_ALIGNMENT);
	    add(beli);
	    
	    String[] igralci = {"Človek","Računalnik"};
	    igralecB = new JComboBox<String>(igralci);
	    igralecB.addActionListener(this);
	    igralecB.setAlignmentX(Component.LEFT_ALIGNMENT);
	    add(igralecB);
	    dodajRazmik();
	    
	    JLabel crni = new JLabel("Črni igralec:");
	    crni.setAlignmentX(Component.LEFT_ALIGNMENT);
	    add(crni);
	    
	    igralecC = new JComboBox<String>(igralci);
	    igralecC.addActionListener(this);
	    igralecC.setAlignmentX(Component.LEFT_ALIGNMENT);
	    add(igralecC);
	    dodajRazmik();
	    
	    Integer[] stevilke = {5,6,7,8,9,10,11,12,13,14,15,16,17,18,19};
	    JLabel napisSirina = new JLabel("Širina:");
	    napisSirina.setAlignmentX(Component.LEFT_ALIGNMENT);
	    add(napisSirina);
	    
	    sirina = new JComboBox<Integer>(stevilke);
	    sirina.setSelectedItem(9);
	    sirina.addActionListener(this);
	    sirina.setAlignmentX(Component.LEFT_ALIGNMENT);
	    add(sirina);
	    dodajRazmik();
	    
	    JLabel napisVisina = new JLabel("Višina:");
	    napisVisina.setAlignmentX(Component.LEFT_ALIGNMENT);
	    add(napisVisina);
	    
	    visina = new JComboBox<Integer>(stevilke);
	    visina.setSelectedItem(9);
	    visina.addActionListener(this);
	    visina.setAlignmentX(Component.LEFT_ALIGNMENT);
	    add(visina);
	    dodajRazmik();
	    
	    JLabel napisTezavnost = new JLabel("Težavnost (čas za računalnikovo potezo):");
	    napisTezavnost.setAlignmentX(Component.LEFT_ALIGNMENT);
	    add(napisTezavnost);
	    
	    mozneTezavnosti = new HashMap<String, Integer>();
	    mozneTezavnosti.put("Naključne poteze", 10);
	    mozneTezavnosti.put("Lahko", 500);
	    mozneTezavnosti.put("Precej lahko", 1000);
	    mozneTezavnosti.put("Srednje", 5000);
	    mozneTezavnosti.put("Precej težko", 7500);
	    mozneTezavnosti.put("Težko", 10000);
	    String[] tezavnostiArray = {
	    		"Naključne poteze",
	    	    "Lahko",
	    	    "Precej lahko",
	    	    "Srednje",
	    	    "Precej težko",
	    	    "Težko"
	    };	    
	    tezavnost = new JComboBox<String>(tezavnostiArray);
	    tezavnost.setSelectedItem("Srednje");
	    tezavnost.addActionListener(this);
	    tezavnost.setAlignmentX(Component.LEFT_ALIGNMENT);
	    add(tezavnost);
	    dodajRazmik();
	    
	    novaIgra = new JButton("Prični igro");
	    novaIgra.addActionListener(this);
	    novaIgra.setAlignmentX(CENTER_ALIGNMENT);
	    
	    JPanel gumb = new JPanel();
	    gumb.setAlignmentX(LEFT_ALIGNMENT);
	    gumb.add(novaIgra);
	    add(gumb);
	}
	
	private void dodajRazmik() {
		add(Box.createRigidArea(new Dimension(0, 7)));
	}
	
	
	@Override
    public void actionPerformed(ActionEvent e) {
		if (e.getSource() == novaIgra) {
			String igralecBeli = (String) igralecB.getSelectedItem();
			String igralecCrni = (String) igralecC.getSelectedItem();
			int s = (int) sirina.getSelectedItem();
			int v = (int) visina.getSelectedItem();
			String izbranaTezavnost = (String) tezavnost.getSelectedItem();
			
			int trajanje = mozneTezavnosti.get(izbranaTezavnost);
			Vodja.trajanje = trajanje;
			
			if (igralecBeli == "Človek" && igralecCrni == "Človek") {
				Vodja.vrstiIgralcev =
        		new EnumMap<Igra.BarvaIgralca, VrstaIgralca>(Igra.BarvaIgralca.class);
				Vodja.vrstiIgralcev.put(Igra.BarvaIgralca.BELI, VrstaIgralca.CLOVEK);
				Vodja.vrstiIgralcev.put(Igra.BarvaIgralca.CRNI, VrstaIgralca.CLOVEK);
				Vodja.ustvariNovoIgro(s, v);
			}
			else if (igralecBeli == "Računalnik" && igralecCrni == "Človek") {
	            Vodja.vrstiIgralcev =
	            		new EnumMap<Igra.BarvaIgralca, VrstaIgralca>(Igra.BarvaIgralca.class);
	            Vodja.vrstiIgralcev.put(Igra.BarvaIgralca.CRNI, VrstaIgralca.CLOVEK);
	            Vodja.vrstiIgralcev.put(Igra.BarvaIgralca.BELI, VrstaIgralca.RACUNALNIK);
	            Vodja.ustvariNovoIgro(s, v);
			}
			else if (igralecBeli == "Človek" && igralecCrni == "Računalnik") { 
	            Vodja.vrstiIgralcev =
	            		new EnumMap<Igra.BarvaIgralca, VrstaIgralca>(Igra.BarvaIgralca.class);
	            Vodja.vrstiIgralcev.put(Igra.BarvaIgralca.CRNI, VrstaIgralca.RACUNALNIK);
	            Vodja.vrstiIgralcev.put(Igra.BarvaIgralca.BELI, VrstaIgralca.CLOVEK);
	            Vodja.ustvariNovoIgro(s, v);
			}
			else if (igralecBeli == "Računalnik" && igralecCrni == "Računalnik") { 
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
