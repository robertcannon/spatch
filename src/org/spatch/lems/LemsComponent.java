package org.spatch.lems;

import java.util.ArrayList;
 
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.type.Component;
import org.lemsml.jlems.viz.plot.Position;
import org.spatch.assembly.base.AssemblyComponent;
import org.spatch.assembly.base.AssemblyObserver;
import org.spatch.assembly.base.Cable;
import org.spatch.assembly.base.ComponentSocket;
import org.spatch.assembly.base.Connection;
import org.spatch.assembly.base.Socket;
import org.spatch.assembly.base.SocketSpec;
import org.spatch.assembly.base.SpatchAssembly;
import org.spatch.assembly.base.SpatchAssemblySpec;
import org.spatch.assembly.edit.TableComponent;
import org.spatch.drawing.Shape;
import org.spatch.drawing.VectorIcon;

public class LemsComponent extends TableComponent implements SpatchAssembly {

	ArrayList<AssemblyObserver> aoAL = new ArrayList<AssemblyObserver>();
	
	
	Component component;
	
	LemsComponentList parent;
	
	LemsComponentType lctype = null;
	
	ArrayList<Shape> shapes = new ArrayList<Shape>();
	
	ArrayList<Cable> cables = new ArrayList<Cable>();
	
	ArrayList<LemsComponent> lemsComponents = new ArrayList<LemsComponent>();
	
	VectorIcon vectorIcon;
	
	
	public LemsComponent(Component c) {
		this(c, true);
	}
	
	public LemsComponent(Component c, boolean real) {
		component = c;
		
		// Circle circ = Circle.defaultCircle();
		// shapes.add(circ.makeShape());
		
		if (real) {
			syncFromLems();
		}
	}
	
	
	public void syncFromLems() {
		ArrayList<Component> acpt = component.getAllChildren();
		lemsComponents.clear();
		for (Component c : acpt) {
			lemsComponents.add(LemsModel.instance().getLemsComponent(c));
		}
	}
	
	
	
	public void setParent(LemsComponentList cl) {
		parent = cl;
	}
	
	
	public LemsComponentType getLemsComponentType() {
		if (lctype == null) {
			lctype = LemsModel.instance().getLemsComponentType(component.getComponentType());
		}
		return lctype;
	}
	
	public String toString() {
		return component.getID();
	}

	public String getTextParam(String att) {
		return component.getTextParam(att);
	}
	
	public String getInfo() {
		String stp = component.getID();
		String snm = component.getName();
		if (snm != null) {
			stp += ": " + snm;
		}
		String ret = "<html>";
		if (stp != null) {
			ret += "<h2>" + stp + "</h2>\n";
		}
		
		ret += component.getAbout();
		return ret;
	}

	public Component getComponent() {
		return component;
	}
	
	public void reportChange() {
		parent.reportChange(this);
	}

	public LemsComponentList getParentList() {
		return parent;
	}

	public String getID() {
		return component.getID();
	}

	public boolean isDiagram() {
		boolean ret = false;
		// TODO tmp!!
		if (component.getComponentType().getName().equals("Setup")) {
			ret = true;
		}
		return ret;
	}
	
	
	

	@Override
	public void addCable(Cable cbl) {
		cables.add(cbl);
	}
 
	@Override
	public void addComponent(AssemblyComponent cpt) {
		if (cpt instanceof LemsComponent) {
			lemsComponents.add((LemsComponent)cpt);
		} else {
			E.missing();	
		}
	}

	@Override
	public void deleteComponent(AssemblyComponent cpt) {
		if (cpt instanceof LemsComponent) {
			LemsComponent lc = (LemsComponent)cpt;
			lc.delete();
			lemsComponents.remove(lc);
		} else {
			E.missing();	
		}
	}
	 
	
	private void delete() {
		E.missing();
	}


	@Override
	public void addShape(Shape newDragShape) {
		shapes.add(newDragShape);
	}

	@Override
	public void deleteCable(Cable cbl) {
		cables.remove(cbl);
	}


	@Override
	public void deleteShape(Shape shp) {
		shapes.remove(shp);
	}

	@Override
	public SpatchAssemblySpec getAssemblySpec() {
		return getLemsComponentType();
	}

	@Override
	public ArrayList<Cable> getCables() {
		return cables;
	}

	@Override
	public AssemblyComponent getComponent(int i) {
		E.missing();
		return null;
	}

	@Override
	public ArrayList<? extends AssemblyComponent> getComponents() {
		return lemsComponents;
	}
	


	@Override
	public ArrayList<Shape> getShapes() {
		return shapes;
	}

	@Override
	public String getSizeUnit() {
		// just used for labels on axes?
		return "";
	}		 

	 
	public ArrayList<Socket> getSockets() {
		 if (sockets == null) {
			 sockets = new ArrayList<Socket>();
			 ArrayList<SocketSpec> ass = getLemsComponentType().getSocketSpecs();
			 for (SocketSpec ss : ass) {
				 ComponentSocket cs = new ComponentSocket(ss, this);
				 sockets.add(cs);
			 }
		 }
		 return sockets;
	}

	@Override
	public double getTypicalSize() {
		return 1.0;
	}

	@Override
	public int size() {
		return 100;
	}

	public void addObserver(AssemblyObserver ao) {
		 aoAL.add(ao);
		
	}

	@Override
	public void removeObserver(AssemblyObserver ao) {
		aoAL.remove(ao);
	}
	
	
	
	
	
	
	

	@Override
	public Connection[] getConnectionsFromPeer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public VectorIcon getIcon() {
		if (vectorIcon == null) {
			VectorIcon typeIcon = getLemsComponentType().getVectorIcon();
			vectorIcon = typeIcon.copy();
			// TODO if colorable, set part of icon to show own color, or if have own icon, use that
		}
		return vectorIcon;
	}

	@Override
	public String[] getMenuOptions() {
		String[] opts = {"option1", "option2", "option3"};
		return opts;
	}

	@Override
	public String getTag() {
		return component.getID();
	}

	@Override
	public boolean isMenuSettable() {
		return true;
	}

	  public AssemblyComponent makeDummyCopy() {
	      LemsComponent cpt = LemsModel.instance().lemsDumyCopyComponent(component);  
	      if (cpt != null) {
	    	  cpt.setRelativePosition(getRelativePosition());
	    	  cpt.setSize(getSize());
//	    	  cpt.initPickable();
	      }
	      return cpt;
	   }

	
	  public AssemblyComponent makeCopy() {
	      LemsComponent cpt = LemsModel.instance().lemsCopyComponent(component);  
	      if (cpt != null) {
	    	  cpt.setRelativePosition(getRelativePosition());
	    	  cpt.setSize(getSize());

	    	  cpt.initPickable();
	      }
	      return cpt;
	   }


	   public AssemblyComponent makeScaledCopy(double fac) {
	      LemsComponent cpt = LemsModel.instance().lemsCopyComponent(component);  
	      if (cpt != null) {
	    	  cpt.setRelativePosition(getRelativePosition());
	    	  cpt.setSize(getSize() * fac);

	    	  cpt.initPickable();
	      }
	      return cpt;
	   }

 

	@Override
	public void syncPositionToPeer(Position p) {
		component.setPosition(p.getX(), p.getY());
	}

	@Override
	public boolean worldSized() {
		return true;
	}

	
	
	
	
	
/*	
	 
	 
	   public TableComponent(TableSpec tsp) {
	      super();

	      p_spec = tsp;
	      setPlugs(p_spec.makePlugArray(this));
	      setSockets(p_spec.makeSocketArray(this));

//	      setAbsolutePosition(new Position(0., 0.));
	      initPickable();
	   }

	   public TableSpec getSpec() {
	      return p_spec;
	   }

	   public Connection[] getConnectionsFromPeer() {
	      return  r_table.getConnections();
	   }

	   public void exportInfo(InfoReceiver infoReceiver) {
	   p_spec.exportInfo(infoReceiver);
	   }

	   public VectorIcon getIcon() { 
	      if (cachedIcon == null) {
	          
	         if (r_table != null) {
	            if (r_table.hasLocalIcon()) {
	               cachedIcon = r_table.getLocalIcon();    
	               
	               
	            } else if (r_table.isMenuSettable()) {
	               cachedIcon = r_table.makeVectorIcon();
	            
	            }
	         }
	         
	         if (cachedIcon == null) {
	            cachedIcon = p_spec.getVectorIcon();
	            if (cachedIcon == null) {
	               E.error("table spec returned null icon?");
	            }
	         }
	      }
	       return cachedIcon;
	   }

	   public boolean worldSized() {
	      return p_spec.worldSized();
	   }

	   public void syncPositionToPeer(Position p) {
	      if (r_table != null) {
	         r_table.setPosition(getRelativePosition());
	         r_table.setSize(getSize());
	      }
	   }

	   public String getTag() {
	      String ret = null;
	      if (r_table == null) {
	         ret = p_spec.getTag();
	      } else {
	         ret = r_table.getTag();
	      }
	      return ret;
	   }


	   // ADHOC -
	   public String getID() {
	      String sid = null;
	      if (r_table != null) {
	         sid = r_table.getID();
	      } else {
	         sid = SpecialStrings.NONE_STRING;
	      }
	      return sid;
	      }


	   public Component makeCopy() {
	      Component cpt = p_spec.newComponent();
	      cpt.setRelativePosition(getRelativePosition());
	      cpt.setSize(getSize());

	      cpt.initPickable();
	      return cpt;
	   }


	   public Component makeScaledCopy(double fac) {
	      Component cpt = p_spec.newComponent();
	      cpt.setRelativePosition(getRelativePosition());
	      cpt.setSize(getSize() * fac);

	      cpt.initPickable();
	      return cpt;
	   }



	   public void attachTable(org.catacomb.interlish.structure.Table tbl) {
	      setTable((TDMTable)tbl);
	      syncSpaceToTable();
	      syncConnectionsFromTable();
	      syncRegionTag();
	      iconChangedIn(null);
	   }

	   public void initFromTable(org.catacomb.interlish.structure.Table tbl) {
	      setTable((TDMTable)tbl);
	      syncSpaceFromTable();
	      syncConnectionsFromTable();
	      syncRegionTag();
	      iconChangedIn(null);
	   }

	   private void setTable(TDMTable tbl) {
	      if (r_table != null) {
	         r_table.removeIconWatcher(this);
	      }
	      r_table = tbl;
	      r_table.addIconWatcher(this);
	   }

	   private void syncSpaceFromTable() {
	      TDMTable tbl = r_table;

	      // copy current values out of table.
	      // in case setting one prompts a reexport of others to the table. 
	      // REFAC - all sync methods should do this enforce somehow?
	      double sz = tbl.getSize();
	      Position pos = tbl.getPosition().copy();
	      
	      setComponentSize(sz);
	      setPosition(pos);

	      setSubstructure(tbl.getAssembly());
	   }





	   private void syncSpaceToTable() {
	      TDMTable tbl = r_table;

	    //
	      E.info("syncing space to table ");
	      
	      tbl.setPosition(getRelativePosition());
	      tbl.setSize(getSize());
	   }

	   private void syncConnectionsFromTable() {
	       TDMTable tbl = r_table;

	      if (plugs != null && plugs.length > 0) {
	         Connection[] ca = tbl.getConnections();
	         if (ca.length != plugs.length) {
	            E.error("plug array missmatch " + ca.length + " " + plugs.length);
	         }
	         for (int ip = 0; ip < plugs.length; ip++) {
	            if (ca[ip] == null) {
	               E.error("null connection from table? " + ip + " " + tbl);
	            }
	            plugs[ip].setConnection(ca[ip]);
	         }
	      }
	   }
	   

	   public boolean isMenuSettable() {
	      boolean ret = false;
	      if (r_table != null && r_table.isMenuSettable()) {
	         ret = true;
	      }
	      return ret;
	   }
	   
	   public String[] getMenuOptions() {
	      String[] ret = null;
	      if (r_table != null) {
	         ret = r_table.getMenuSettableOptions();
	      }
	      return ret;
	   }
	   
	   
	   public boolean hasTable() {
	      return (r_table != null);
	   }

	   public TDMTable getTable() {
	      return r_table;
	   }

	   public void iconChangedIn(Object src) {
	      cachedIcon = null;
	      reportIconChange();
	   }

	}
	*/
	 
}
