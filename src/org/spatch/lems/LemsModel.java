package org.spatch.lems;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
 
import org.lemsml.jlems.core.expression.ParseError;
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.sim.LemsProcess;
import org.lemsml.jlems.core.sim.Sim;
import org.lemsml.jlems.core.type.Component;
import org.lemsml.jlems.core.type.ComponentType;
import org.lemsml.jlems.core.type.Dimension;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.io.util.FileUtil;
import org.lemsml.jlems.io.util.JUtil;
import org.spatch.models.ModelRoot;

public class LemsModel {

	Lems lems;
	
	
	HashMap<ComponentType, LemsComponentType> typeWrapperHM = new HashMap<ComponentType, LemsComponentType>();
	HashMap<Component, LemsComponent> componentWrapperHM = new HashMap<Component, LemsComponent>();
	
	
	static LemsModel singleInstance;
	
	
	HashMap<String, String> defaultUnits;
	
	ArrayList<LemsComponentType> slctA = null;
	
	String srcfnm = "";
	
	
	public LemsModel(String s) {
		srcfnm = s;
		defaultUnits = new HashMap<String, String>();
		defaultUnits.put("time", "ms");
		defaultUnits.put("voltage", "mV");
		defaultUnits.put("current", "nA");
		defaultUnits.put("none", "");
		
		
		try {
			String srcText = JUtil.getRelativeResource(ModelRoot.class, srcfnm);
			
			LemsProcess sim = new Sim(srcText);
			sim.readModel();
	
			lems = sim.getLems();
		
			if (singleInstance == null) {
				singleInstance = this;
			} else {
				E.error("only one LemsModels should be created");
			}
			
		} catch (Exception ex) {
			E.error("cant read model: " + ex);
			ex.printStackTrace();
		}
	}

 
	public static LemsModel instance() {
		return singleInstance;
	}
	
	
	
	public ArrayList<LemsComponentType> getStandaloneLemsComponentTypes() {
		if (slctA == null) {
			slctA = new ArrayList<LemsComponentType>();
			for (ComponentType ct : lems.getComponentTypes()) {
				if (ct.standalone) {
					LemsComponentType lct = getLemsComponentType(ct); 
					
					if (ct.isExtension()) {
						
					} else {
						slctA.add(lct);
					}
				}
			}
			
		}
		return slctA;
	}
	
	
	
	public ArrayList<Component> getComponents(String typename) throws ContentError {
		 return lems.getAllByType(typename);
		
	}


	public String[] getSubtypeNames(LemsComponentType lct) throws ContentError {
		String[] ret = lems.getSubtypeNames(lct.getName());
		return ret;
	}


	public void addComponent(LemsComponent lc) throws ContentError, ParseError {
		E.error("don't think this is the right thing to do... - see addComponent");
		Component cpt = lc.getComponent();
		lems.addComponent(cpt);
		cpt.resolve(lems, null);
	}
	
	public void removeComponent(LemsComponent lc) {
		Component cpt = lc.getComponent();
		E.missing();
		// lems.removeComponent(cpt);
	}
	
	
	public LemsComponent createComponent(String typeName, String label, String name) throws ContentError, ParseError {
		ComponentType ct = lems.getComponentTypeByName(typeName);
		return createComponent(ct, label, name, true);
	}
		
	
	public LemsComponent wrapLemsComponent(Component cpt) {
		return getLemsComponent(cpt, false);
	}
	
	public LemsComponent getLemsComponent(Component cpt) {
		return getLemsComponent(cpt, true);	
	}
	
	public LemsComponent getLemsComponent(Component cpt, boolean warn) {
		LemsComponent ret = null;
		if (componentWrapperHM.containsKey(cpt)) {
			ret = componentWrapperHM.get(cpt);
		} else {
			// if we're just wrapping for the first time, its fine.
			// otherwise, warn if wrapping something that should already be wrapped
			if (warn) {
				E.warning("no lems cpt matching " + cpt + "?");
			}
			ret = new LemsComponent(cpt);
			componentWrapperHM.put(cpt, ret);
		}
		return ret;
	}
	
	
	public LemsComponent lemsDumyCopyComponent(Component template) {
		LemsComponent ret = null;
		try {
			ret = addDummyComponent(template.getComponentType());
		} catch (Exception ex) {
			E.error("Problem creating new component from template " + template);
			ex.printStackTrace();
		}
		return ret;
	}
	
	
	
	
	public LemsComponent lemsCopyComponent(Component template) {
		LemsComponent ret = null;
		try {
			ret = addComponent(template.getComponentType());
		} catch (Exception ex) {
			E.error("Problem creating new component from template " + template);
			ex.printStackTrace();
		}
		return ret;
	}
	 
	
	
	public LemsComponent createComponent(ComponentType ct, String id, String name, boolean real) throws ContentError, ParseError {	
		Component cpt = new Component();
		cpt.setID(id);
		cpt.setDeclaredName(name);
		// cpt.addAttribute(new XMLAttribute("fullName", name));
		cpt.setType(ct);
		
		if (real) {
			lems.addComponent(cpt);
			cpt.resolve(lems, null, false, true);
		}
		
		LemsComponent ret = new LemsComponent(cpt, real);
		
		// real is false for dummy components that have temp ids and aren't added to lems
		// this is used for echoing drag behavior on the component bar before the component appears
		// on the assembly canvas
		if (real) {
			componentWrapperHM.put(cpt, ret);
		}
		// new Exception().printStackTrace();
	//	E.info("made new cpt " + cpt);
		
		return ret;
	}

	public LemsComponent addComponent(ComponentType ct) throws ContentError, ParseError {
		String tnm = ct.getName();
		int ext = getNewSuffix(tnm);
 		String label = tnm + "-" + ext;
		LemsComponent ret = createComponent(ct, label, label, true);
		return ret;
	}
	
	// this is used for the temp components on te components bar - don't add them to lems, and 
	// don't give new names.
	public LemsComponent addDummyComponent(ComponentType ct) throws ContentError, ParseError {
		String tnm = ct.getName();
		String label = tnm;
		LemsComponent ret = createComponent(ct, label, label, false);
		return ret;
	}
	 

	public String getUnitSymbol(Dimension dimension) {
		String sd = dimension.getName();
		String ret = "";
		if (defaultUnits.containsKey(sd)) {
			ret = (defaultUnits.get(sd));
		} else {
			E.warning("no default units for " + sd);
		}
		return ret;
	}


	public int getNewSuffix(String tnm) throws ContentError {
		String rt = tnm;
		if (rt == null) {
			rt = "component";
		}
		int ret = 1;
		while (true) {
			String cand = rt + "-" + ret;
			if (!lems.hasComponent(cand)) {
				break;
			}
			ret += 1;
		}
		return ret;
	}


	public Lems getLems() {
		return lems;
	}


	public ArrayList<Component> getComponents(LemsComponentType lct) throws ContentError {
		ComponentType ct = lct.getComponentType();
		ArrayList<Component>  ret = lems.getAllByType(ct);	
		return ret;
	}


	public LemsComponentType getLemsComponentType(ComponentType type) {
		LemsComponentType ret = null;
		if (typeWrapperHM.containsKey(type)) {
			ret = typeWrapperHM.get(type);
		} else {
			ret = new LemsComponentType(type);
			typeWrapperHM.put(type, ret);
		}
		return ret;
	} 
	
	
}
