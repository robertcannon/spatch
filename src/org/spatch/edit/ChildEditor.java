package org.spatch.edit;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.lemsml.jlems.core.type.Component;
import org.lemsml.jlems.core.type.ComponentType;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.core.type.Child;

import org.spatch.lems.LemsComponent;
import org.spatch.lems.LemsComponentType;

public class ChildEditor {

	
	public ChildEditor(Component cpt, Child ch, Lems l) {
		
	}
	
	public JPanel getPanel() {
		JPanel ret = new JPanel();
		ret.setLayout(new FlowLayout());
		ret.add(new JLabel("Child editor - not implemented"));
		return ret;
	}
	
	
}
