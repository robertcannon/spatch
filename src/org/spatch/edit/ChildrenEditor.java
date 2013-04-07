package org.spatch.edit;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.lemsml.jlems.core.type.Children;
import org.lemsml.jlems.core.type.Component;
import org.lemsml.jlems.core.type.ComponentType;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.core.type.Child;

import org.spatch.lems.LemsComponent;
import org.spatch.lems.LemsComponentType;

public class ChildrenEditor {

	
	
	public ChildrenEditor(Component cpt, Children ch, Lems l) {
		
	}
	
	public JPanel getPanel() {
		JPanel ret = new JPanel();
		ret.setLayout(new FlowLayout());
		ret.add(new JLabel("Children editor - not implemented"));
		return ret;
	}
	
	
}
