package org.spatch;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
 
import org.lemsml.jlems.core.sim.ContentError;
import org.spatch.edit.ComponentSelectorEditor;
import org.spatch.lems.HierarchyComparator;
import org.spatch.lems.LemsComponentType;
import org.spatch.lems.LemsModel;
import org.spatch.sim.RecordingPanel;

 
public class Spatch {

	LemsModel lemsModel;
 
	
	public Spatch() {
	}
	
	
	public void init() throws ContentError {
	
		
		lemsModel = new LemsModel("spatch.xml");
//		lemsModel = new LemsModel("example1.xml");
		
		 
		try {
		//	   PlasticLookAndFeel.setPlasticTheme(new DesertBlue());
		//	UIManager.setLookAndFeel(new PlasticXPLookAndFeel());
	     UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		 
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		
		JFrame frame = new JFrame();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600, 400);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar); 
	
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.add(new JMenuItem("Open"));
		fileMenu.add(new JMenuItem("Save"));
		fileMenu.add(new JMenuItem("Save as"));
		menuBar.add(fileMenu);
		
		
		
		JTabbedPane jTabbedPane = new JTabbedPane();
		
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		
		frame.getContentPane().add(mainPanel);
		
		
		mainPanel.add(jTabbedPane, BorderLayout.CENTER);
		
		 
		
		ArrayList<LemsComponentType> slctA = lemsModel.getStandaloneLemsComponentTypes();

		
		Collections.sort(slctA, new HierarchyComparator(lemsModel));
		
		boolean first = true;
		for (LemsComponentType lct : slctA) {
			ComponentSelectorEditor se = new ComponentSelectorEditor(lct, lemsModel, first);
			jTabbedPane.add(lct.getName(), se.getPanel());
			first = false;
		}
		
		
		
		JPanel prec = new RecordingPanel();
		jTabbedPane.add("Recording", prec);
		
	
         frame.setMinimumSize(new Dimension(400, 200));
         frame.setPreferredSize(new Dimension(800, 600));

		
		frame.pack();
		frame.setVisible(true);
	}
	

	
	public static void main(String[] argv) {
		try {
			Spatch spatch = new Spatch();
			spatch.init();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
}
