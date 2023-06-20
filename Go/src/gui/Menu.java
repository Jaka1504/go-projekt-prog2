package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import logika.Igra;
import vodja.Vodja;
import vodja.Vodja.VrstaIgralca;


@SuppressWarnings("serial")
public class Menu extends JMenuBar implements ActionListener{
	
	JButton novaIgra;
	JMenu barveIgralcev;
	JMenuItem crniZetoni;
	JMenuItem beliZetoni;
	protected Igra igra;
	
	
	public Menu() {
		super();
	    novaIgra = new JButton("Ustvari novo igro");
	    novaIgra.addActionListener(this);
	    add(novaIgra);
	    
	    barveIgralcev = new JMenu("Spremeni barve žetonov");
	    crniZetoni = new JMenuItem("Spremeni barvo črnih žetonov");
	    crniZetoni.addActionListener(this);
	    beliZetoni = new JMenuItem("Spremeni barvo belih žetonov");
	    beliZetoni.addActionListener(this);
	    barveIgralcev.add(crniZetoni);
	    barveIgralcev.add(beliZetoni);
	    
	    add(barveIgralcev);
	}
	
	
	public void nastaviIgro(Igra igra) {
		this.igra = igra;
		
	}
	
	@Override
    public void actionPerformed(ActionEvent e) {
		if (e.getSource() == novaIgra) {
			JFrame infoOkno = new JFrame(); 
			infoOkno.setSize(250,300);
			infoOkno.setTitle("Nova igra");
			infoOkno.setResizable(false);
			PlatnoInfo infoPlatno = new PlatnoInfo(infoOkno);
			infoOkno.add(infoPlatno);
			infoOkno.setVisible(true);	
		}
		if (e.getSource() == crniZetoni) {
			JColorChooser izbiraBarveCrni = new JColorChooser();
			Color barva = JColorChooser.showDialog(null, "Spremeni barvo črnih žetonov", Color.BLACK);
			this.igra.nastaviBarvoCrnih(barva);
		}
		if (e.getSource() == beliZetoni) {
			JColorChooser izbiraBarveBeli = new JColorChooser();
			Color barva = JColorChooser.showDialog(null, "Spremeni barvo belih žetonov", Color.WHITE);
			this.igra.nastaviBarvoBelih(barva);
		}
	} 

}
