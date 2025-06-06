package fr.umlv.littlethinker;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

/**
 * Classe qui créé la fenetre principale du programme
 */
@SuppressWarnings("serial")
public class LittleThinker extends JFrame{
	private final Processor proc;
	private final Memory mem;
	private final Terminal term;
	static final int MEMORY_LENGTH = 51;
	
	/**
	 * Initialise le processeur, la mémoire et le terminal dans deux JSplitPane
	 */
	public LittleThinker(){
        super("LittleThinker");

        mem = new Memory(MEMORY_LENGTH);
        term = new Terminal();
        proc = new Processor(mem, term);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);

        JSplitPane splitV = new JSplitPane(JSplitPane.VERTICAL_SPLIT, proc, new JScrollPane(term));
//        add(splitV);
        VideoRam vr = new VideoRam(8,3);
        JSplitPane splitVr = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitV, new JScrollPane(vr));
        add(splitVr);

        JSplitPane splitH = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, splitVr, new JScrollPane(mem));
        add(splitH);

        setVisible(true);
        splitV.setDividerLocation(0.5);
        splitH.setDividerLocation(0.75);
        // TODO : remove tests
        ///  TESTS
        vr.setOctet(0,255);
        vr.setOctet(1,1);
        vr.setOctet(7,128+32+8+2);
        vr.setOctet(8,255);
        vr.setOctet(191,0b10000001);


        ///  TESTS

    }
}