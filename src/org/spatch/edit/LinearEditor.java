package org.spatch.edit;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
 
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.type.Child;
import org.lemsml.jlems.core.type.Children;
import org.lemsml.jlems.core.type.ComponentReference;
import org.lemsml.jlems.core.type.ComponentRelative;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.core.type.Parameter;
import org.spatch.lems.LemsComponent;
import org.spatch.lems.LemsModel;

public class LinearEditor extends ComponentEditor implements ActionListener, KeyListener {

	JTextField labelField;
	JTextField nameField;
	
	JButton saveButton;
	JButton cancelButton;
	
	JTextArea descTF;
 
	JPanel panel = null;
	
	HashMap<String, JTextField> fieldHM = new HashMap<String, JTextField>();
	HashMap<String, String> unitsHM = new HashMap<String, String>();
	
	 
	public LinearEditor(LemsComponent lc, Lems lems) {
		super(lc, lems);
	}
	
	
	
	public JPanel getPanel() {
		if (panel == null) {
			buildPanel();
		}
		return panel;
	}
 

	private void buildPanel() {
		 panel = new JPanel();  
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	
		descTF = new JTextArea(8, 60);
		descTF.setFont(new Font("Serif", Font.PLAIN, 12));
		descTF.setLineWrap(true);
		descTF.setWrapStyleWord(true);
		JScrollPane areaScrollPane = new JScrollPane(descTF);
		areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setPreferredSize(new Dimension(300, 80));
	 
		/*
		JPanel dp = new JPanel();
		dp.setBorder(new EmptyBorder(2, 10, 20, 10));
		dp.setLayout(new BorderLayout(10, 2));
		dp.add(BorderLayout.WEST, new JLabel("Description: "));
		
		dp.add(BorderLayout.CENTER, areaScrollPane);
		panel.add(dp);
		*/
		
		JPanel ppars = new JPanel(new SpringLayout());
		
	 	JLabel lab = new JLabel("Short name:");
		ppars.add(lab);
	 
		labelField = new JTextField(10);
		ppars.add(leftFlow(labelField));
		labelField.setText(cpt.getID()) ; 
		labelField.addKeyListener(this);
	 
		
		 JLabel nameLabel = new JLabel("Full name:");
		 ppars.add(nameLabel);
		 
		nameField = new JTextField(20);
		nameField.setText(cpt.getName());
		nameField.addKeyListener(this);
		ppars.add(leftFlow(nameField));
	 	 
		 JLabel descLabel = new JLabel("Description:");
		 ppars.add(descLabel);
		 ppars.add(areaScrollPane);
	 	
		
		int nrow = 0;
		for (Parameter param : type.getParameters()) {
			JLabel jl = new JLabel(param.getName());
			ppars.add(jl);
			
			JTextField jtf = new JTextField();
			jtf.addKeyListener(this);
			jtf.setColumns(12);
			fieldHM.put(param.getName(), jtf);
		 	
			String su = LemsModel.instance().getUnitSymbol(param.getDimension());
			JLabel ujl = new JLabel(su);
			unitsHM.put(param.getName(), su);
			ppars.add(leftFlow(jtf, ujl));
			
			nrow += 1;
		}
		
		for (ComponentReference cr : type.getComponentReferences()) {
			JLabel jl = new JLabel(cr.getName());
			ppars.add(jl);
			
			JComboBox jcb = makeComponentRefChoice(cr);
			ppars.add(jcb);
			nrow += 1;
		}
		
		
		for (Child child : type.getChilds()) {
			JLabel jl = new JLabel(child.getName());
			ppars.add(jl);
			ChildEditor childEditor = new ChildEditor(cpt, child, myLems);
			ppars.add(childEditor.getPanel());
			nrow += 1;
		}
		
		for (Children children : type.getChildrens()) {
			JLabel jl = new JLabel(children.getName());
			ppars.add(jl);
			ChildrenEditor childrenEditor = new ChildrenEditor(cpt, children, myLems);
			ppars.add(childrenEditor.getPanel());
			nrow += 1;
		}
		
		SpringUtilities.makeCompactGrid(ppars, nrow + 3, 2, 4, 4, 4, 4);
		
	
		 
		panel.add(centerFlow(ppars));
		
		saveButton = new JButton("Save changes");
		saveButton.setEnabled(false);
		saveButton.addActionListener(this);
		saveButton.setActionCommand("save");
		
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		cancelButton.setActionCommand("cancel");
		
		JPanel bpan = new JPanel(new FlowLayout(FlowLayout.CENTER));
		bpan.add(saveButton);
		bpan.add(cancelButton);
		
		panel.add(centerFlow(bpan));
	 
	}


	private void saveChanges() {
		cpt.clear();
		
		cpt.setID(labelField.getText().replace(" ", "_"));
		cpt.setDeclaredName(nameField.getText());
		
		for (String s : fieldHM.keySet()) {	
			cpt.setParameter(s, fieldHM.get(s).getText() + " " + unitsHM.get(s));
		}
		try {
			cpt.resolve(LemsModel.instance().getLems(), null);
			saveButton.setEnabled(false);

		} catch (Exception ex) {
			System.out.println("problems... " + ex);
		}
		reportSaved();
	
	}
	
	
	
	public void cancelChanges() {
		reportCancelled();
	}
	
	
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("save")) {
			saveChanges();
		
		} else if (cmd.equals("cancel")) {
			cancelChanges();
			
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
		saveButton.setEnabled(true);
	}
	
	private JComboBox  makeComponentRefChoice(ComponentReference cr) {
		JComboBox ret = new JComboBox();
		ArrayList<ComponentRelative> refVals = myLems.getComponentReferenceTargets(cpt, cr);
		for (ComponentRelative rel : refVals) {
			ret.addItem(rel);
		}
		
		return ret;
	}
	
	
	
}
