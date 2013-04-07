
package org.spatch.assembly.base;

import org.lemsml.jlems.viz.plot.Position;
 

// this is attachment points on extended structures, like adding a probe to a point on a neuron model
// an AttachmentPoint is just something that is Flavoured, IDd and XYLocated
// for connections between components, use the ComponentSocket

public class AttachmentPointSocket extends SubLocated implements Socket {


   private final AttachmentPoint aPoint;


/*
   public AttachmentPointSocket(AttachmentPoint ap, Component cpt) {
      super(cpt);
      aPoint = ap;
      setRelativePosition(new Position(ap.getXYLocation()));
   }
*/

   public AttachmentPointSocket(AttachmentPoint ap, AssemblyComponent cpt,
            double xnorm, double ynorm) {
      super(cpt);
      aPoint = ap;
      setRelativePosition(new Position(xnorm, ynorm));
   }


   public boolean isExternal() {
      return true;
   }

   
   // POSERR
   public boolean isInput() {
      return true;
   }
   
   public boolean isOutput() {
      return true;
   }
   

   public String getID() {
        return aPoint.getID();
   }


   public String getParentID() {
      return ((AssemblyComponent)getParent()).getID();
   }

   public AssemblyComponent getComponent() {
      return (AssemblyComponent)(getParent());
   }

 
   public boolean isConnected() {
	   // TODO
	   return false;
   }

   public ConnectionFlavor getFlavor() {
      return aPoint.getFlavor();
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
