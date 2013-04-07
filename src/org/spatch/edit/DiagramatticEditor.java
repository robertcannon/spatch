package org.spatch.edit;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JTextField;
 
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.type.Lems;
import org.spatch.WrapError;
import org.spatch.assembly.edit.SpatchWorkbench;
import org.spatch.lems.LemsComponent;
import org.spatch.lems.LemsComponentType;
 

public class DiagramatticEditor extends ComponentEditor implements ActionListener, KeyListener {

 
	JPanel panel;
	
	LemsComponentType ass;
	SpatchWorkbench workbench;
 
	HashMap<String, JTextField> fieldHM = new HashMap<String, JTextField>();
	HashMap<String, String> unitsHM = new HashMap<String, String>();
	
	
	public DiagramatticEditor(LemsComponent lc, Lems lems) {
		super(lc, lems);
	}
	
	
	
	public JPanel getPanel() throws WrapError {
		if (workbench == null) {
			buildPanel();
		}
		return panel;
	}
 
	private void buildPanel() throws WrapError {
		 panel = new JPanel();  
		 
		panel.setLayout(new BorderLayout());
		
		workbench = new SpatchWorkbench(600, 400);
		panel.add(BorderLayout.CENTER, workbench);
		 
	 
		workbench.setAssembly(getLemsComponent());
	}

 
	
	public void cancelChanges() {
		
		if (editListener != null) {
			editListener.editCancelled();
		}
	}
	
	
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("save")) {
		 
		
		} else if (cmd.equals("cancel")) {
		 
			
		} else {
		 E.error("unrecognized: " + cmd);
		}
	}


	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	public void keyTyped(KeyEvent e) {
		 
	}
	
 
	
	
	
}
