package org.spatch.lems;

import java.util.Comparator;

import org.lemsml.jlems.core.type.Lems;
 
 
public class HierarchyComparator implements Comparator<LemsComponentType> {
	LemsModel lemsModel;
	Lems lems; 
	
	public HierarchyComparator(LemsModel lm) {
		lemsModel = lm;
		lems = lm.getLems();
	}
	
	
	public int compare(LemsComponentType o1, LemsComponentType o2) {
		double v1 = o1.getHierarchyWeight(lems);
		double v2 = o2.getHierarchyWeight(lems);
		return (v1 < v2 ? 1 : (v1 > v2 ? -1 : 0));
	}
 	
	
}
