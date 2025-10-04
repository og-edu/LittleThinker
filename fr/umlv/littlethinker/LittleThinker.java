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
    static final int TAILLE_PILE = 20;
    VideoRam vr = null;
	
	/**
	 * Initialise le processeur, la mémoire et le terminal dans deux JSplitPane
	 */
	public LittleThinker(){
        super("Z6 - simulateur de microprocesseur");

        vr = new VideoRam(8,3,MEMORY_LENGTH, TAILLE_PILE);
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
        // Ecriture dans la video ram
/*        int colonne = 0;   // lettre Z
        int caractere = 8;
        vr.setOctet(caractere * 0 + colonne,0b00000000);
        vr.setOctet(caractere * 1 + colonne, 126);
        vr.setOctet(caractere * 2 + colonne,6);
        vr.setOctet(caractere * 3 + colonne,12);
        vr.setOctet(caractere * 4 + colonne,48);
        vr.setOctet(caractere * 5 + colonne,96);
        vr.setOctet(caractere * 6 + colonne,126);
        vr.setOctet(caractere * 7 + colonne,0);
        colonne ++; // lettre 6
        vr.setOctet(caractere * 0 + colonne,0b00000000);
        vr.setOctet(caractere * 1 + colonne, 126);
        vr.setOctet(caractere * 2 + colonne,66);
        vr.setOctet(caractere * 3 + colonne,64);
        vr.setOctet(caractere * 4 + colonne,126);
        vr.setOctet(caractere * 5 + colonne,66);
        vr.setOctet(caractere * 6 + colonne,126);
        vr.setOctet(caractere * 7 + colonne,0);*/
        // Ecriture dans la mEmoire
        int colonne = 44;   // lettre Z
        int caractere = 8;
        mem.setValue(caractere * 0 + colonne,"0");
        mem.setValue(caractere * 1 + colonne,"126");
        mem.setValue(caractere * 2 + colonne,"6");
        mem.setValue(caractere * 3 + colonne,"12");
        mem.setValue(caractere * 4 + colonne,"48");
        mem.setValue(caractere * 5 + colonne,"96");
        mem.setValue(caractere * 6 + colonne,"126");
        mem.setValue(caractere * 7 + colonne,"0");
        colonne ++; // lettre 6
        mem.setValue(caractere * 0 + colonne,"0");
        mem.setValue(caractere * 1 + colonne,"126");
        mem.setValue(caractere * 2 + colonne,"66");
        mem.setValue(caractere * 3 + colonne,"64");
        mem.setValue(caractere * 4 + colonne,"126");
        mem.setValue(caractere * 5 + colonne,"66");
        mem.setValue(caractere * 6 + colonne,"126");
        mem.setValue(caractere * 7 + colonne,"0");
        int adresse = 191;
        boolean result = false;
//        for (adresse = 50; adresse <256; adresse++) {
//             result = vr.faitPartieDeRamVideo(adresse);
//            System.out.printf("adresse = %d, resultat=%s%n",adresse,result);
//        }

        ///  TESTS

    }
}