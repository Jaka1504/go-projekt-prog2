package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import logika.Igra;
import vodja.Vodja;
import vodja.Vodja.VrstaIgralca;

public class Menu extends JMenuBar implements ActionListener{
	
	JMenuItem igraClovekRacunalnik;
	JMenuItem igraRacunalnikClovek;
	JMenuItem igraClovekClovek;
	JMenuItem igraRacunalnikRacunalnik;
	
	public Menu() {
		super();
		JMenu menuIgra = new JMenu("Ustvari novo igro");	// naredi meni za novo igro
	    JMenu menuGrafika = new JMenu("Grafika");	// naredi meni za lastnosti grafičnega vmesnika
	    
	    //sub-menu
	    JMenu igraCloRac = new JMenu("Človek - Računalnik"); 
	    igraClovekRacunalnik = new JMenuItem("Začne Človek");
	    igraCloRac.add(igraClovekRacunalnik);
	    igraClovekRacunalnik.addActionListener(this);
	    igraRacunalnikClovek = new JMenuItem("Začne Računalnik");
	    igraCloRac.add(igraRacunalnikClovek);
	    igraRacunalnikClovek.addActionListener(this);
	    menuIgra.add(igraCloRac);
	    
	    igraClovekClovek = new JMenuItem("Človek - Človek");
	    menuIgra.add(igraClovekClovek);
	    igraClovekClovek.addActionListener(this);
	    igraRacunalnikRacunalnik = new JMenuItem("Računalnik - Računalnik");
	    menuIgra.add(igraRacunalnikRacunalnik);
	    igraRacunalnikRacunalnik.addActionListener(this);
	    
	    //grafika
	    JMenuItem menuItem3 = new JMenuItem("Barve žetonov");
	    menuGrafika.add(menuItem3);
	    JMenuItem menuItem4 = new JMenuItem("...");
	    menuGrafika.add(menuItem4);
	    
	    add(menuIgra); 
	   add(menuGrafika);
	}
	
	@Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == igraClovekRacunalnik) {
            Vodja.vrstiIgralcev =
            		new EnumMap<Igra.BarvaIgralca, VrstaIgralca>(Igra.BarvaIgralca.class);
            Vodja.vrstiIgralcev.put(Igra.BarvaIgralca.CRNI, VrstaIgralca.CLOVEK);
            Vodja.vrstiIgralcev.put(Igra.BarvaIgralca.BELI, VrstaIgralca.RACUNALNIK);
            Vodja.ustvariNovoIgro();
        } else if (e.getSource() == igraRacunalnikClovek) { 
            Vodja.vrstiIgralcev =
            		new EnumMap<Igra.BarvaIgralca, VrstaIgralca>(Igra.BarvaIgralca.class);
            Vodja.vrstiIgralcev.put(Igra.BarvaIgralca.CRNI, VrstaIgralca.RACUNALNIK);
            Vodja.vrstiIgralcev.put(Igra.BarvaIgralca.BELI, VrstaIgralca.CLOVEK);
            Vodja.ustvariNovoIgro();
        } else if (e.getSource() == igraClovekClovek) { 
            Vodja.vrstiIgralcev =
            		new EnumMap<Igra.BarvaIgralca, VrstaIgralca>(Igra.BarvaIgralca.class);
            Vodja.vrstiIgralcev.put(Igra.BarvaIgralca.BELI, VrstaIgralca.CLOVEK);
            Vodja.vrstiIgralcev.put(Igra.BarvaIgralca.CRNI, VrstaIgralca.CLOVEK);
            Vodja.ustvariNovoIgro();
        } else if (e.getSource() == igraRacunalnikRacunalnik) { 
            Vodja.vrstiIgralcev =
            		new EnumMap<Igra.BarvaIgralca, VrstaIgralca>(Igra.BarvaIgralca.class);
            Vodja.vrstiIgralcev.put(Igra.BarvaIgralca.BELI, VrstaIgralca.RACUNALNIK);
            Vodja.vrstiIgralcev.put(Igra.BarvaIgralca.CRNI, VrstaIgralca.RACUNALNIK);
            Vodja.ustvariNovoIgro();
        }
	}

}
