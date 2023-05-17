package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import vodja.Vodja;

@SuppressWarnings("serial")
public class Okno extends JFrame implements ActionListener {
	
	protected Platno platno;
	
	public Okno() {
		super();
		setTitle("Capture Go");
		platno = new Platno(800, 800);	 	// ustvari v pomnilniku
		add(platno);				// doda platno na okno
		
		//menu
		JMenuBar menu= new JMenuBar();
		
		JMenu menuIgra = new JMenu("Ustvari novo igro");	// naredi meni za novo igro
	    JMenu menuGrafika = new JMenu("Grafika");	// naredi meni za lastnosti grafičnega vmesnika
	    
	    //sub-menu
	    JMenu igraCloRac = new JMenu("Človek - Računalnik"); 
	    JMenuItem igraClovekRacunalnik = new JMenuItem("Začne Človek");
	    igraCloRac.add(igraClovekRacunalnik);
	    igraClovekRacunalnik.addActionListener(this);
	    JMenuItem igraRacunalnikClovek = new JMenuItem("Začne Računalnik");
	    igraCloRac.add(igraRacunalnikClovek);
	    igraClovekRacunalnik.addActionListener(this);
	    menuIgra.add(igraCloRac);
	    
	    JMenuItem igraClovekClovek = new JMenuItem("Človek - Človek");
	    menuIgra.add(igraClovekClovek);
	    igraClovekClovek.addActionListener(this);
	    JMenuItem igraRacunalnikRacunalnik = new JMenuItem("Računalnik - Računalnik");
	    menuIgra.add(igraRacunalnikRacunalnik);
	    igraRacunalnikRacunalnik.addActionListener(this);
	    
	    //grafika
	    JMenuItem menuItem3 = new JMenuItem("Barve žetonov");
	    menuGrafika.add(menuItem3);
	    JMenuItem menuItem4 = new JMenuItem("...");
	    menuGrafika.add(menuItem4);
	    
	    menu.add(menuIgra); 
	    menu.add(menuGrafika);
		
	    this.setJMenuBar(menu);  //doda meni na okno
	    
		// da se program ustavi ko zapremo okno
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	public Platno platno() {
		return platno;
	}
	
	
}
