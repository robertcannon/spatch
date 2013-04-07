package org.spatch.lems;

import java.util.ArrayList;
import java.util.HashMap;

public class LemsComponentList {

	String name;
	
	ArrayList<LemsComponentList> lists = new ArrayList<LemsComponentList>();

	ArrayList<LemsComponent> items = new ArrayList<LemsComponent>();
	
	String groupAtt;
	
	HashMap<String, LemsComponentList> listHM = new HashMap<String, LemsComponentList>();
	
	LemsComponentList parent;
	
	LEMSTree lemsTree;
	
	public LemsComponentList(String s, LemsComponentList par) {
		name = s;
		parent = par;
	}
	
	public LemsComponentList getParent() {
		return parent;
	}
	
	
	public void setName(String s) {
		name = s;
	}
	
	public String toString() {
		return name;
	}
	
	public void add(LemsComponent t) {
		items.add(t);
		t.setParent(this);
	}
	
	public void add(LemsComponentList l) {
		lists.add(l);
		l.setParent(this);
	}
	
 
	
	private void setParent(LemsComponentList cl) {
		parent = cl;
	}

	public int size() {
		return lists.size() + items.size();
	}

	public Object getChild(int index) {
		Object ret = null;
		if (index >= lists.size()) {
			ret = items.get(index - lists.size());
		} else {
			ret = lists.get(index);
		}
		return ret;
	}

	public int getIndexOfChild(Object child) {
		int ret = -1;
		if (items.contains(child)) {
			ret = lists.size() + items.indexOf(child);
		} else if (lists.contains(child)) {
			ret = lists.indexOf(child);
		} else {
			System.out.println("error - cant get index");
		}
		return ret;
	}

	
	public void addAll(ArrayList<LemsComponent> components) {
		items.addAll(components);
		for (LemsComponent lc : components) {
			lc.setParent(this);
		}
	}

	public void groupBy(String att) {
		groupAtt = att;
		ArrayList<LemsComponent> wk = new ArrayList<LemsComponent>();
		wk.addAll(items);
		lists = new ArrayList<LemsComponentList>();
		items = new ArrayList<LemsComponent>();
		
		
		
		for (LemsComponent c : wk) {
			addGrouped(c);
			 
		}
	}
	
	public LemsComponentList addGrouped(LemsComponent cpt) {
		LemsComponentList ret = this;
		if (groupAtt == null) {
			add(cpt);
		} else {
			String tp = cpt.getTextParam(groupAtt);
			if (tp != null && tp.length() > 0) {
				if (listHM.containsKey(tp)) {
					ret = listHM.get(tp);
					ret.add(cpt);
				} else {
					LemsComponentList cl = new LemsComponentList(tp, this);
					ret = cl;
					listHM.put(tp, cl);
					cl.add(cpt);
					lists.add(cl);
				}
				
			} else {
				items.add(cpt);
				ret = this;
			}
		}
		return ret;
	}


	public Object[] getPathObjects() {
		 ArrayList<Object> obs = new ArrayList<Object>();
		 LemsComponentList wk = this;
		 while (wk != null) {
			 obs.add(wk);
			 wk = wk.getParent();
		 }
		 int n = obs.size();
		 Object[] ret = new Object[n];
		 for (int i = 0; i < n; i++) {
			 ret[i] = obs.get(n-i-1);
		 }
		 return ret;
	}

	public void setTree(LEMSTree ltree) {
		lemsTree = ltree;
	}

	public void reportChange(LemsComponent src) {
		if (lemsTree != null) {
			lemsTree.cptChanged(src);
		} else {
			parent.reportChange(src);
		}
	}

	public void remove(LemsComponent cpt) {
		items.remove(cpt);
	}

	public LemsComponent getFirst() {
		 LemsComponent ret = null;
		 if (items.size() > 0) {
			 ret = items.get(0);
		 }
		 return ret;
	}
	

	



}
