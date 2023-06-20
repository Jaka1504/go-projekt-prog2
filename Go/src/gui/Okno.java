package gui;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Okno extends JFrame {
	
	protected Platno platno;
	protected Menu menu;
	
	public Okno() {
		super();
		setTitle("Capture Go");
		platno = new Platno(800, 800);	 	// ustvari v pomnilniku
		add(platno);				// doda platno na okno
		
		//menu
		menu= new Menu();
		
	    this.setJMenuBar(menu);  //doda meni na okno
	    
		// da se program ustavi ko zapremo okno
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	public Platno platno() {
		return platno;
	}
	
	public Menu menu() {
		return menu;
	}
	
	
}
