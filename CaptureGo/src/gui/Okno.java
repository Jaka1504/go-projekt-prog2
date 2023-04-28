package gui;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Okno extends JFrame {
	
	protected Platno platno;
	
	public Okno() {
		super();
		setTitle("Capture Go");
		platno = new Platno(800, 800);	 	// ustvari v pomnilniku
		add(platno); 						// doda platno na okno
		// da se program ustavi ko zapremo okno
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	public Platno platno() {
		return platno;
	}
	
}
