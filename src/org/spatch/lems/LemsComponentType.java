package org.spatch.lems;

import java.util.ArrayList;
import java.util.HashMap;
 
import org.lemsml.jlems.core.sim.ContentError;
import org.lemsml.jlems.core.type.ComponentReference;
import org.lemsml.jlems.core.type.ComponentType;
import org.lemsml.jlems.core.type.EventPort;
import org.lemsml.jlems.core.type.Lems;
import org.lemsml.jlems.core.type.visualization.DrawingConnector;
import org.lemsml.jlems.core.type.visualization.LinkSourceConnector;
import org.lemsml.jlems.core.type.visualization.LinkTargetConnector;
import org.lemsml.jlems.core.type.visualization.Visualization;

import org.lemsml.jlems.core.xml.XMLElement;
import org.lemsml.jlems.viz.plot.Position;
 
import org.spatch.WrapError;
import org.spatch.assembly.base.AssemblyComponent;
import org.spatch.assembly.base.ConnectionFlavor;
import org.spatch.assembly.base.SocketSpec;
import org.spatch.assembly.base.SpatchAssemblySpec;
import org.spatch.assembly.edit.AssemblyLabel;
import org.spatch.assembly.edit.IconSpec;
import org.spatch.drawing.DrawingImporter;
import org.spatch.drawing.DrawingReader;
import org.spatch.drawing.FixedDrawingComponent;
import org.spatch.drawing.Shape;
import org.spatch.drawing.VectorIcon;

public class LemsComponentType implements SpatchAssemblySpec {

	ComponentType componentType;
	
	double hWeight = Double.NaN; 
	
	ArrayList<AssemblyLabel> alA = new ArrayList<AssemblyLabel>();
	ArrayList<IconSpec> iconSpecA = new ArrayList<IconSpec>();
	
	ArrayList<SocketSpec> ssA = null;
	
	VectorIcon vectorIcon;
	
	public LemsComponentType(ComponentType ct) {
		componentType = ct;
	}
	
	 
	
	public String toString() {
		return componentType.getName();
	}

	 
	public String getInfo() {
		String stp = componentType.getName();
		String ret = "<html>";
		if (stp != null) {
			ret += "<h2>" + stp + "</h2>\n";
		}
		
		ret += componentType.getAbout();
		return ret;
	}

	public ComponentType getComponentType() {
		return componentType;
	}



	public String getName() {
		return componentType.getName();
	}
	 
	public String getAbout() {
		return componentType.getAbout();
	}



	public boolean hasTextParameter(String nm) throws ContentError {
		boolean ret = false;
		if (componentType.getTexts().hasName(nm)) {
			ret = true;
		}  
		return ret;
	}



	public double getHierarchyWeight(Lems lems) {
		if (Double.isNaN(hWeight)) {
			hWeight = componentType.getHierarchyWeight(lems);
		}
		return hWeight;
	}



	@Override
	public ArrayList<AssemblyLabel> getAssemblyLabels() {
		return alA;
	}



	@Override
	public ArrayList<IconSpec> getIconSpecs() {
		return iconSpecA;
	}



	@Override
	public double[] getInitialXRange() {
		double[] rx = {-1., 1.};
		return rx;
	}



	@Override
	public double[] getInitialYRange() {
		double[] rx = {-1., 1.};
		return rx;
	}



	@Override
	public ArrayList<SpatchAssemblySpec> getSetMemberSpecs() {
		ArrayList<SpatchAssemblySpec> ret = new ArrayList<SpatchAssemblySpec>();
		try {
			ArrayList<ComponentType> cctAL = componentType.getChildCompTypes();
			for (ComponentType ct : cctAL) {
				ret.add(LemsModel.instance().getLemsComponentType(ct));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ret;
	}



	@Override
	public ArrayList<Shape> getShapeExemplars() {
		ArrayList<Shape> ret=  new ArrayList<Shape>();
		/*
		Circle c = Circle.defaultCircle();
		Triangle t = new Triangle(-0.5, -0.5, 0., 0.4, 0.5, -0.5);
		
		ret.add(c.makeShape());
		ret.add(t.makeShape());
		*/
		
		return ret;
	}



	@Override
	public boolean hasAssemblyLabels() {
		return (alA.size() > 0);
	}



	@Override
	public boolean hasIconSpecs() {
		return (iconSpecA.size() > 0);
	}



	@Override
	public AssemblyComponent newComponent() throws WrapError {
		LemsComponent ret = null;
		try {
			ret = LemsModel.instance().addComponent(componentType);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new WrapError("", ex);
		}
		return ret;
	}
	
	@Override
	public AssemblyComponent newDummyComponent() throws WrapError {
		LemsComponent ret = null;
		try {
			ret = LemsModel.instance().addDummyComponent(componentType);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new WrapError("", ex);
		}
		return ret;
	}


	public VectorIcon getVectorIcon() {
		if (vectorIcon == null) {
			Visualization v = componentType.getVisualization();
			if (v != null) {
				DrawingImporter di = new DrawingImporter(v);
				
				vectorIcon = di.makeVectorIcon();
			}
			
		}
		if (vectorIcon != null && vectorIcon.nonTrivial()) {
		// OK
		} else {
			vectorIcon = VectorIcon.makePendingIcon();
		}
	
		return vectorIcon;
	}

	
	
	private VectorIcon parseMetaIcon() {		
		VectorIcon ret = null;
		XMLElement xe = componentType.getContextMeta("viz");
		if (xe != null) {
			ret = new VectorIcon();
			DrawingReader sr = new DrawingReader();
			for (XMLElement xec : xe.getElements()) {
				FixedDrawingComponent dc = sr.importDrawing(xec);
				if (dc != null) {
					ret.add(dc);
				}
			}
		}
		return ret;
	}
	
	
	
	public ArrayList<SocketSpec> getSocketSpecs() {
		if (ssA == null) {
			ssA = new ArrayList<SocketSpec>();
			 
			ConnectionFlavor cf = new ConnectionFlavor(componentType.getName());

			 HashMap<String, LinkSourceConnector> srcHM = new HashMap<String, LinkSourceConnector>();

			Visualization v = componentType.getVisualization();
			if (v != null) {
				for (DrawingConnector dc : v.drawingConnectors) {
					if (dc instanceof LinkSourceConnector) {
						LinkSourceConnector lsc = (LinkSourceConnector)dc;
						srcHM.put(lsc.componentReference, lsc);
					}
			
					if (dc instanceof LinkTargetConnector) {
						LinkTargetConnector ltc = (LinkTargetConnector)dc;
						if (ltc.visible) {
							ssA.add(new SocketSpec("ref", new Position(ltc.x, ltc.y), SocketSpec.IN, cf));
						}
					
					}
				}
					
			
				ArrayList<ComponentReference> locrefs = componentType.getLocalComponentReferences();
			
				int nl = locrefs.size();
				if (nl > 0) {
					double dx = 1. / nl;
					double xo = -0.5 * nl * dx;

					int il = 0;
					for (ComponentReference link : locrefs) {
						String tt = link.getTargetType();
						ConnectionFlavor cfl = new ConnectionFlavor(tt);

						double xp = xo + dx * il;
						double yp = 1.0;
						if (srcHM.containsKey(link.getName())) {
							LinkSourceConnector lsc = srcHM.get(link.getName());
							xp = lsc.x;
							yp = lsc.y;
						
						} else {
							il += 1;
						}
					
						Position pl = new Position(xp, yp);
						SocketSpec ss = new SocketSpec(tt, pl, SocketSpec.OUT, cfl);
						ssA.add(ss);
					}
				}
			}
			
			int nin = 0;
			int nout = 0;
			for (EventPort ep : componentType.getEventPorts()) {
				
				if (ep.isDirectionIn()) {
					ConnectionFlavor cfl = new ConnectionFlavor("event in");
					Position pl = new Position(-1.0, 0.0 + 0.2 * nin);
					SocketSpec ss = new SocketSpec(ep.getName(), pl, SocketSpec.IN, cfl);
					ssA.add(ss);
					nin += 1;
					
				} else if (ep.isDirectionOut()) {
					ConnectionFlavor cfl = new ConnectionFlavor("event out");
					Position pl = new Position(1.0, 0.0 + 0.2 * nout);
					SocketSpec ss = new SocketSpec(ep.getName(), pl, SocketSpec.OUT, cfl);
					ssA.add(ss);
					nout += 1;
				}
			}
			
		}
		return ssA;
	}

	
	
	
	
	
	public ArrayList<SocketSpec> getSocketSpecsFromXMLMeta() {
		if (ssA == null) {
			ssA = new ArrayList<SocketSpec>();
			 
			ConnectionFlavor cf = new ConnectionFlavor(componentType.getName());

			HashMap<String, XMLElement> srcHM = new HashMap<String, XMLElement>();
			
			double xin = 0.;
			double yin = 0;
			boolean showTargetSocket = true;
			
			
			XMLElement xe = componentType.getContextMeta("viz");
			if (xe != null && xe.hasElement("LinkTargetConnector")) {
				XMLElement xlt = xe.getElement("LinkTargetConnector");
				xin = xlt.getDouble("x");
				yin = xlt.getDouble("y");
				boolean b = xlt.getBoolean("visible", true);
				if (!b) {
					showTargetSocket = false;
				}
			}
			if (showTargetSocket) {
				ssA.add(new SocketSpec("ref", new Position(xin, yin), SocketSpec.IN, cf));
			}
			
			if (xe != null && xe.hasElement("LinkSourceConnector")) {
				ArrayList<XMLElement> xel = xe.getElements("LinkSourceConnector");
				for (XMLElement x : xel) {
					srcHM.put(x.getAttribute("link"), x);
				}
			}
			
			ArrayList<ComponentReference> locrefs = componentType.getLocalComponentReferences();
			
			int nl = locrefs.size();
			if (nl > 0) {
				double dx = 1. / nl;
				double xo = -0.5 * nl * dx;

				int il = 0;
				for (ComponentReference link : locrefs) {
					String tt = link.getTargetType();
					ConnectionFlavor cfl = new ConnectionFlavor(tt);

					double xp = xo + dx * il;
					double yp = 1.0;
					if (srcHM.containsKey(link.getName())) {
						XMLElement elt = srcHM.get(link.getName());
						xp = elt.getDouble("x");
						yp = elt.getDouble("y");
						
					} else {
						il += 1;
					}
					
					Position pl = new Position(xp, yp);
					SocketSpec ss = new SocketSpec(tt, pl, SocketSpec.OUT, cfl);
					ssA.add(ss);
				}
				
			}
			
			int nin = 0;
			int nout = 0;
			for (EventPort ep : componentType.getEventPorts()) {
				
				if (ep.isDirectionIn()) {
					ConnectionFlavor cfl = new ConnectionFlavor("event in");
					Position pl = new Position(-1.0, 0.0 + 0.2 * nin);
					SocketSpec ss = new SocketSpec(ep.getName(), pl, SocketSpec.IN, cfl);
					ssA.add(ss);
					nin += 1;
					
				} else if (ep.isDirectionOut()) {
					ConnectionFlavor cfl = new ConnectionFlavor("event out");
					Position pl = new Position(1.0, 0.0 + 0.2 * nout);
					SocketSpec ss = new SocketSpec(ep.getName(), pl, SocketSpec.OUT, cfl);
					ssA.add(ss);
					nout += 1;
				}
			}
			
		}
		return ssA;
	}
	 
	
}
