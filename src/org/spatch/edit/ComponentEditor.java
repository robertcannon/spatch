package org.spatch.edit;

import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
 
import org.lemsml.jlems.core.type.Component;
import org.lemsml.jlems.core.type.ComponentType;
import org.lemsml.jlems.core.type.Lems;
import org.spatch.lems.LemsComponent;
import org.spatch.lems.LemsComponentType;

public class ComponentEditor {

	protected LemsComponent lcpt;
	protected LemsComponentType lcptType;
	
	protected Component cpt;
	protected ComponentType type;
	protected EditListener editListener = null;

	
	Lems myLems;
	
	
	public ComponentEditor(LemsComponent lc, Lems lems) {
		lcpt = lc;
		myLems = lems;
		
		cpt = lc.getComponent();
		type = cpt.getComponentType();
		lcptType = lc.getLemsComponentType();
	}

	
	public LemsComponent getLemsComponent() {
		return lcpt;
	}
	
	
	public LemsComponentType getLemsComponentType() {
		return lcptType;
	}
	
	public void setEditListener(EditListener el) {
		editListener = el;
	}

	
	

	public JPanel centerFlow(JComponent p) {
		return wrapFlow(p, FlowLayout.CENTER);
	}
	
	public JPanel rightFlow(JComponent p) {
		return wrapFlow(p, FlowLayout.RIGHT);
	}
	
	public JPanel leftFlow(JComponent p) {
		return wrapFlow(p, FlowLayout.LEFT);
	}
	
	public JPanel topFlow(JComponent p) {
		return wrapFlow(p, FlowLayout.LEADING);
	}
	
	private JPanel wrapFlow(JComponent p, int pos) {
		JPanel ret = new JPanel();
		ret.setLayout(new FlowLayout(pos, 0, 0));
		ret.add(p);
		return ret;
	}
	
	private JPanel wrapFlow(JComponent p1, JComponent p2, int pos) {
		JPanel ret = new JPanel();
		ret.setLayout(new FlowLayout(pos, 0, 0));
		ret.add(p1);
		ret.add(p2);
		return ret;
	}
	
	
	public JPanel leftFlow(JComponent p1, JComponent p2) {
		return wrapFlow(p1, p2, FlowLayout.LEFT);
	}
	
	protected void reportSaved() {
		if (editListener != null) {
			editListener.editSaved();
		}
	}
	
	protected void reportCancelled() {
		if (editListener != null) {
			editListener.editCancelled();
		}
	}
}
