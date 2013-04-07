package org.spatch.assembly.base;

import org.lemsml.jlems.viz.plot.XYLocation;
 


public interface Socket {


   XYLocation getXYLocation();

   XYLocation getRelativeXYLocation();

   String getID();

   ConnectionFlavor getFlavor();

   String getParentID();

   Object getParent();

   void parentMoved();
   
   boolean isExternal();

   boolean isInput();
   
   boolean isOutput();

   boolean isConnected();

   void addConnection(Plug p);
   
   void removeConnection(Plug p);
   
   
}
