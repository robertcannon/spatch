package org.spatch.lems;

import java.util.ArrayList;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.type.Component;
 
public class LEMSTree implements TreeModel {

	
	LemsComponentList root;
	
	
	ArrayList<TreeModelListener> listeners = new ArrayList<TreeModelListener>();
	
	ArrayList<Component> allComponents;
	
	
	public LEMSTree(String name) {
		root = new LemsComponentList(name, null);	
	}
 
	
	public LEMSTree(String nm, ArrayList<Component> components) {
		this(nm);
		LemsModel model = LemsModel.instance();
		allComponents = components;
 		
		ArrayList<LemsComponent> lcts = new ArrayList<LemsComponent>();
		for (Component c : components) {
			lcts.add(model.wrapLemsComponent(c));
		}
		
		root.addAll(lcts);
		root.setTree(this);
	}
 

	public LemsComponent getFirst() {
		return root.getFirst();
	}
	
	
	
	public void addTreeModelListener(TreeModelListener l) {
		listeners.add(l);
	}

	 
	public Object getChild(Object parent, int index) {
		return ((LemsComponentList)parent).getChild(index);
	}

	 
	public int getChildCount(Object parent) {
		return ((LemsComponentList)parent).size();
	}

	 
	public int getIndexOfChild(Object parent, Object child) {
		return ((LemsComponentList)parent).getIndexOfChild(child);
	}

	public Object getRoot() {
		return root;
	}

	public boolean isLeaf(Object node) {
		boolean ret = false;
		if (node instanceof LemsComponentList) {
			ret = false;
		} else {
			ret = true;
		}
		return ret;
	}

	public void removeTreeModelListener(TreeModelListener l) {
		listeners.remove(l);
	}

	public void valueForPathChanged(TreePath path, Object newValue) {
		// TODO Auto-generated method stub
	}


	public void groupBy(String string) {
		root.groupBy(string);
	}



	public void addAndSelect(LemsComponent lc) {
		LemsComponentList cl = root.addGrouped(lc);
		
		int inew = cl.getIndexOfChild(lc);
		int[] inds = {inew};
		Object[] obs = {lc};
		
		TreeModelEvent tme = new TreeModelEvent(this, cl.getPathObjects(), inds, obs);
		
		for (TreeModelListener tml : listeners) {
			tml.treeNodesInserted(tme);
		}
		
	}


	public void removeComponent(LemsComponent cpt) {
		LemsComponentList cl = cpt.getParentList();
		int ich = cl.getIndexOfChild(cpt);
		int[] inds = {ich};
		Object[] obs = {cpt};
		cl.remove(cpt);
		
		E.info("reporting remove " + ich + " " + cpt + " " + cl);
		TreeModelEvent tme = new TreeModelEvent(this, cl.getPathObjects(), inds, obs);
		for (TreeModelListener tml : listeners) {
			tml.treeNodesRemoved(tme);
		}
	}

	
	
	public void cptChanged(LemsComponent src) {
		LemsComponentList cl = src.getParentList();
		int ich = cl.getIndexOfChild(src);
		int[] inds = {ich};
		Object[] obs = {src};
		
		TreeModelEvent tme = new TreeModelEvent(this, cl.getPathObjects(), inds, obs);
		
		for (TreeModelListener tml : listeners) {
			tml.treeNodesChanged(tme);
		}
	}
	
	
	
}
