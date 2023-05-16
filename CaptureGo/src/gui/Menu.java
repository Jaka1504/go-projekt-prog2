package gui;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

public class Menu extends JMenuBar {
	
	public Menu() {
		super();
		JMenu menuIgralec = new JMenu("Lastnosti igralca");	// naredi meni za lastnosti igralca
	    JMenu menuVmesnik = new JMenu("Lastnosti grafičnega vmesnika");	// naredi meni za lastnosti grafičnega vmesnika
	    
	    JMenu igralec = new JMenu("Kdo igra"); //naredimo sub-menu za igralca
	    JRadioButtonMenuItem menuItem1 = new JRadioButtonMenuItem("Človek");
	    igralec.add(menuItem1);
	    JRadioButtonMenuItem menuItem2 = new JRadioButtonMenuItem("Računalnik");
	    igralec.add(menuItem2);
	    menuIgralec.add(igralec);
	    menuIgralec.addSeparator(); // line to separate different items types
	    JRadioButtonMenuItem menuItem5 = new JRadioButtonMenuItem("Algoritem");
	    menuIgralec.add(menuItem5);
	    
	    JMenuItem menuItem3 = new JMenuItem("Barve žetonov");
	    menuVmesnik.add(menuItem3);
	    JMenuItem menuItem4 = new JMenuItem("Barva plošče");
	    menuVmesnik.add(menuItem4);
	    
	    add(menuIgralec); 
	    add(menuVmesnik);
		
		
	}

}
