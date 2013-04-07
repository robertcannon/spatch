package org.spatch.assembly.base;
  
import java.util.ArrayList;
import java.util.HashMap;
 
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.viz.plot.PickableRegion;
import org.lemsml.jlems.viz.plot.Position;
import org.lemsml.jlems.viz.plot.XYLocation;
import org.spatch.assembly.edit.InfoReceiver;
import org.spatch.drawing.SegmentLine;
import org.spatch.drawing.VectorIcon;


public abstract class AssemblyComponent extends SubLocated {

   public String id;

   public ArrayList<ComponentPlug> plugs;

   public ArrayList<Socket> sockets;

   protected PickableRegion p_pickableRegion;

   private boolean updatePickable = true;

   private int displayState;

   private boolean showSubstructure;
   

   private HandleSet handleSet;

   private ArrayList<IconWatcher> iconWatchers;
   
   
   public AssemblyComponent() {
      super(null);
      silentSetSize(0.15);
      displayState = 0;
      iconWatchers = new ArrayList<IconWatcher>();
   }

   public void syncRegionTag() {
      if (p_pickableRegion != null) {
            p_pickableRegion.setRegionTag(getTag());
      }
   }


   public void setSubstructureView(boolean b) {
      showSubstructure = b;
   }

   public boolean substructureVisible() {
      return showSubstructure;
   }
 

   public abstract Connection[] getConnectionsFromPeer();

   public abstract VectorIcon getIcon();

   public abstract boolean worldSized();



 //  public abstract void exportInfo(InfoReceiver infoReceiver);

   public abstract String getTag();

   public abstract void syncPositionToPeer(Position p);


    public void setID(String s) {
       id = s;
    }

    public String getID() {
       return id;
    }


   public void setComponentSize(double d) {
      super.silentSetSize(d);
      if (p_pickableRegion != null) {
         initPickable();
      }
   }
 


   public Position getFirstPlugPosition() {
      Position ret = null;
      if (plugs.size() > 0) {
         ret = plugs.get(0).getAbsolutePosition();
      } else {
         E.error("cant get first plug pos - no plugs");
      }
      return ret;
   }


   public void setFirstPlugPosition(double x, double y) {
         if (plugs.size() > 0) {
            plugs.get(0).setAbsolutePosition(new Position(x, y));
            E.info("set plug pos " + x + " " + y);
         } else {
            E.error("cant set plug pos - no plugs");
         }
   }
   

   public void reconnect(HashMap<String, AssemblyComponent> hm) {
      Connection[] ca = getConnectionsFromPeer();
      if (ca != null) {
         for (Connection con : ca) {
            String st = con.getTargetID();
            String ss = con.getSocketID();
            
            if (st == null) {
               ComponentPlug from = getPlug(con.getPlugID());
               from.setXYLocation(new Position(con.getXTarget(), con.getYTarget()));
               from.syncLocation();
            
             
            } else if (st.equals("workpiece")) {
               E.missing("cant connect to workpiece yet");

            } else if (hm.containsKey(st)) {
               AssemblyComponent tgt = hm.get(st);
               ComponentPlug from = getPlug(con.getPlugID());
               Socket skt = tgt.getSocket(ss);
               if (from != null && skt != null) {
                  from.connectTo(skt);
               }

            } else {
               E.error("cant reconnect to " + st + " no such cpt");
            }
         }
      }
   }



   public void reconnectToWorkpiece(Object obj) {
      if (obj instanceof PointAttachable) {

         HashMap<String, AttachmentPoint> hm = new HashMap<String, AttachmentPoint>();
         for (AttachmentPoint ap : ((PointAttachable)obj).getAttachmentPoints()) {
            hm.put(ap.getID(), ap);
         }

         Connection[] ca = getConnectionsFromPeer();

         if (ca != null) {
            for (Connection con : ca) {
               String sid = con.getTargetID();

               if (sid == null) {

               } else if (sid.equals("workpiece")) {

                  String sto = con.getSocketID();

                  if (hm.containsKey(sto)) {
                     // POSERR what about the editors socket store - does it
                     // matter?
                     ComponentPlug cp = getPlug(con.getPlugID());

                     cp.connectTo(new WorkpieceSocket(hm.get(sto)));

                  } else {
                     E.error("not such point in workpiece " + sto);
                  }
               }
            }
         }
      } else {
         E.error("cant reconnect to " + obj);
      }
   }



   private ComponentPlug getPlug(String pid) {
      ComponentPlug ret = null;
      if (plugs != null) {
         for (ComponentPlug plug : plugs) {
            if (plug.getID().equals(pid)) {
               ret = plug;
               break;
            }
         }
      }
      if (ret == null) {
         E.error("component cant find plug " + pid + " in " + this);
      }
      return ret;
   }



   Socket getSocket(String pid) {
      Socket ret = null;
      if (sockets != null) {
         for (Socket socket : sockets) {
            if (socket.getID().equals(pid)) {
               ret = socket;
                  break;
            }
         }
      }
      if (ret == null) {
         E.error("component cant find Socket " + pid + " in " + this);
      }
      return ret;
   }



   public boolean hasPlugs() {
      return (plugs != null && plugs.size() > 0);
   }


   public void setPlugs(ComponentPlug[] pa) {
      plugs = new ArrayList<ComponentPlug>();
      for (ComponentPlug p : pa) {
    	  plugs.add(p);
      }
   }


   public ArrayList<ComponentPlug> getPlugs() {
      return plugs;
   }



   public void setSockets(Socket[] sa) {
	   sockets = new ArrayList<Socket>();
	   if (sa != null) {
         for (Socket s : sockets) {
        	 sockets.add(s);
         }
      }
   }

   public void setSockets(ArrayList<Socket> soca) {
      setSockets(soca.toArray(new Socket[soca.size()]));
   }
   

   public ArrayList<Socket> getSockets() {
      return sockets;
   }


   public void quietSetPosition(Position pos) {
      setAbsolutePosition(pos);
      updatePickable = true;
   }


   public void setPosition(Position pos) {
      quietSetPosition(pos);
      notifyDependentsOfMove();
   }


   public void notifyDependentsOfMove() {
      if (plugs != null) {
         for (ComponentPlug plug : plugs) {
            plug.parentMoved();
         }
      }

      if (sockets != null) {
         for (Socket s : sockets) {
            s.parentMoved();
         }
      }

      syncPositionToPeer(getRelativePosition());


   }



   public PickableRegion getBoundaryRegion() {
      if (p_pickableRegion == null) {
         initPickable();
      }

      if (updatePickable) {
         p_pickableRegion.moveTo(getAbsolutePosition());
         p_pickableRegion.scaleTo(getSize());
         
         updatePickable = false;
      }
      return p_pickableRegion;
   }


   public void initPickable() {

      SegmentLine pl = null;
 
         VectorIcon dr = getIcon();
         if (dr == null) {
            pl = SegmentLine.unitBox();

         } else {
            pl = dr.getBoundary();
         }
     

      pl.scaleBy(getSize());

      Position pos = getAbsolutePosition();
      double xp = 0.;
      double yp = 0.;

      if (pos.isValid()) {
         xp = pos.getX();
         yp = pos.getY();
      }

      pl.shift(xp, yp);

      p_pickableRegion = new PickableRegion(pl.getXPts(), pl.getYPts(), this);
      p_pickableRegion.setReferencePoint(xp, yp);


      syncRegionTag();
   }



   public void cycleDisplay() {
      displayState = (displayState + 1) % 2;
      E.info("set display state " + displayState);
   }

   
   public void setDisplayState(int ids) {
      displayState = ids;
   }

   public int getDisplayState() {
      return displayState;
   }


   public void disconnect() {
      for (Plug p : getPlugs()) {
         p.disconnect();
      }
   }


   public void notifyObservers() {
      // TODO Auto-generated method stub

   }



   public RegionHandle[] getBoundaryHandles() {
      if (handleSet == null) {
         handleSet = new CornerMiddleHandleSet(this);
      }
      XYLocation xyl = getXYLocation();
      double sz = getSize();
      handleSet.locate(xyl, sz);
      return handleSet.getHandles();
   }

   
   public void addIconWatcher(IconWatcher iw) {
      iconWatchers.add(iw);
   }
   
   public void removeIconWatcher(IconWatcher iw) {
      iconWatchers.remove(iw);
   }
   
   public void reportIconChange() {
      for (IconWatcher iw : iconWatchers) {
         iw.iconChangedIn(this);
      } 
      if (iconWatchers == null || iconWatchers.size() == 0) {
        // E.info("no watchers of " + this);
      }
   }

public SpatchAssembly getSubstructure() {
	// TODO Auto-generated method stub
	return null;
}

public void exportInfo(InfoReceiver infoReceiver) {
	// TODO Auto-generated method stub
	
}
 
}
