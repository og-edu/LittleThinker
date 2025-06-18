package fr.umlv.littlethinker;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import java.awt.*;

/**
 * Classe qui créé la fenetre principale du programme
 */
@SuppressWarnings("serial")
public class LittleThinker extends JFrame{
	private final Processor proc;
	private final Memory mem;
	private final Terminal term;
	static final int MEMORY_LENGTH = 256;
    VideoRam vr = null;
	
	/**
	 * Initialise le processeur, la mémoire et le terminal dans deux JSplitPane
	 */
	public LittleThinker(){
        super("LittleThinker");

        vr = new VideoRam(8,3,MEMORY_LENGTH);
        //vr.setMinimumSize(new Dimension(500,1000));
        mem = new Memory(MEMORY_LENGTH,vr);
        term = new Terminal();
        //term.setPreferredSize(new Dimension(100,100));
        proc = new Processor(mem, term);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080);

        JSplitPane splitV = new JSplitPane(JSplitPane.VERTICAL_SPLIT, proc, new JScrollPane(term));
        splitV.setMinimumSize(new Dimension(500,600));
        JSplitPane splitVr = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitV, new JScrollPane(vr));
        add(splitVr);

        JSplitPane splitH = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitVr, new JScrollPane(mem));
        add(splitH);

        setVisible(true);
        splitV.setDividerLocation(0.5);
        splitH.setDividerLocation(0.75);
        // TODO : remove tests
        ///  TESTS
        vr.setOctet(00,0b00000000);
        vr.setOctet(8, 0b00011000);
        vr.setOctet(16,0b00100100);
        vr.setOctet(24,0b00100100);
        vr.setOctet(32,0b01111110);
        vr.setOctet(40,0b01000010);
        vr.setOctet(48,0b01000010);
        int adresse = 191;
        boolean result = false;
//        for (adresse = 50; adresse <256; adresse++) {
//             result = vr.faitPartieDeRamVideo(adresse);
//            System.out.printf("adresse = %d, resultat=%s%n",adresse,result);
//        }

        ///  TESTS

    }
}