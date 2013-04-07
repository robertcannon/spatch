package org.spatch.assembly.base;

import org.lemsml.jlems.viz.plot.AbsLocated;
import org.lemsml.jlems.viz.plot.Position;
import org.lemsml.jlems.viz.plot.XYLocation;
 



public class WorkpieceSocket implements Socket, AbsLocated {


   private String id;
   private ConnectionFlavor flavor;

   private XYLocation xyloc;

     private Position p_pos;

     private Object r_parent;


     // REFAC - how does this relate to AttachmentPointSocket? 
     
   public WorkpieceSocket(AttachmentPoint ap) {
      xyloc = new BaseXYLocation(ap.getXYLocation());
      flavor = ap.getFlavor();
      id = ap.getID();
      p_pos = new Position(xyloc);
   }

   public void setParent(Object p) {
      r_parent = p;
   }

   public Object getParent() {
      return r_parent;
   }

   public String toString() {
      return ("wkp socket id=" + id + ", flav=" + flavor + ", pos=" + xyloc);
   }


   public String getParentID() {
      return "workpiece";
   }

   public XYLocation getXYLocation() {
      return xyloc;
   }

   public XYLocation getRelativeXYLocation() {
      return p_pos;
   }

   public Position getAbsolutePosition() {
      return p_pos;
   }


   public boolean isExternal() {
      return true; // POSERR
   }
   
   // POSERR
   public boolean isInput() {
      return true;
   }
   
   public boolean isOutput() {
      return true;
   }
   

   public String getID() {
      return id;
   }

   public ConnectionFlavor getFlavor() {
      return flavor;
   }

   public void parentMoved() {

   }
   
   public boolean isConnected() {
	   // TODO
	   return false;
   }

@Override
public void addConnection(Plug p) {
	// TODO Auto-generated method stub
	
}

@Override
public void removeConnection(Plug p) {
	// TODO Auto-generated method stub
	
}

}
