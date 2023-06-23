package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import logika.Igra;
import logika.Igra.BarvaIgralca;
import logika.Rezultat;
import vodja.Vodja;

@SuppressWarnings("serial")
public class PlatnoRezultati extends JPanel implements ActionListener {
	
	protected JFrame frame;
	protected Igra igra;
	protected Color barvaCrnih;
	protected Color barvaBelih;
	protected JButton ponovi;
	protected JButton zapri;
	
	
	public PlatnoRezultati(JFrame frame, Igra igra, Color barvaCrnih, Color barvaBelih) {
		this.frame = frame;
		this.igra = igra;
		this.barvaCrnih = barvaCrnih;
		this.barvaBelih = barvaBelih;

		// ============== TOCKE ==================
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
		
		JPanel prikazTock = new JPanel();
		prikazTock.setLayout(new BoxLayout(prikazTock, BoxLayout.X_AXIS));
		
		JPanel crni = new PlatnoRezultatEnega(BarvaIgralca.CRNI, igra, barvaCrnih);
		JPanel beli = new PlatnoRezultatEnega(BarvaIgralca.BELI, igra, barvaBelih);	
		
		prikazTock.add(crni);
		prikazTock.add(beli);
		
		add(prikazTock);
		
		// ============= GUMBI =================
		
		JPanel gumbi = new JPanel();
		gumbi.setLayout(new BoxLayout(gumbi, BoxLayout.X_AXIS));
		ponovi = new JButton("PONOVI IGRO");
		zapri = new JButton("ZAPRI");
		ponovi.addActionListener(this);
		zapri.addActionListener(this);
		
		gumbi.add(Box.createGlue());
		gumbi.add(Box.createGlue());
		gumbi.add(ponovi);
		gumbi.add(Box.createGlue());
		gumbi.add(zapri);
		gumbi.add(Box.createGlue());
		gumbi.add(Box.createGlue());
		add(gumbi);
	}
	
	/*
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawRect(naslovC.getX(), naslovC.getY(), naslovC.getWidth(), naslovC.getHeight());
	}
	*/
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ponovi) {
			Vodja.ustvariNovoIgro(igra.sirina(), igra.visina());
		}
		frame.dispose();
	}
}
