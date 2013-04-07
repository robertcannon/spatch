package org.spatch.assembly.edit;
 
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.viz.plot.Box;
import org.lemsml.jlems.viz.plot.Position;
import org.lemsml.jlems.viz.plot.Size;
import org.lemsml.jlems.viz.plot.XYLocation;
import org.spatch.assembly.base.ConnectionFlavor;
import org.spatch.assembly.base.Plug;
import org.spatch.assembly.base.Socket;
 



public class SocketSet {

   LocatedItemSet allItems;

   
   Box boundingBox;

   final static int nbox = 20;
   LocatedItemSet[][] boxes;


   boolean newItem;


   public SocketSet(ConnectionFlavor flavor) {
      allItems = new LocatedItemSet();
      newItem = false;
   }



   public void clear() {
      allItems.clear();

      if (boxes != null) {
	 for (int i = 0; i < nbox; i++) {
	    for (int j = 0; j < nbox; j++) {
	       boxes[i][j].clear();
	    }
	 }
      }
      newItem = false;
   }




   public void add(Socket skt) {
      XYLocation xyloc = skt.getXYLocation();

      allItems.add(xyloc, skt);

      newItem = true;
   }






   public Socket getInRange(Plug plug, Size sz) {
      Socket ret = null;

      if (allItems.size() > 20) {
	  E.missing("Socket set needs more code for box lookup");
      } 
      
      LocatedItem locit = allItems.getWithinRange(plug.getXYLocation(), sz);

      if (locit != null) {
	 ret = (Socket)(locit.getRef()); 
      }
      return ret;
   }




   public Socket boxGetInRange(Plug plug, Size sz) {

      if (boxes == null || newItem) {
	 buildSocketMap();
	 newItem = false;
      }

      XYLocation ppos = plug.getXYLocation();

      double ax = ppos.getX();
      double ay = ppos.getY();

      int ix = boundingBox.getXSubdivision(ax);
      int iy = boundingBox.getYSubdivision(ay);

      LocatedItemSet liset = boxes[ix][iy];
      
      LocatedItem locit = liset.getWithinRange(ppos, sz);

      Socket ret = (Socket)(locit.getRef()); // usually null;
      return ret;

   }



   public void buildSocketMap() {

      boundingBox = allItems.getBoundingBox();
      boundingBox.enlarge(0.001);
      boundingBox.subdivide(nbox);
	
      if (boxes == null) {
	 boxes = new LocatedItemSet[nbox][nbox];
      }
      for (int i = 0; i < nbox; i++) {
	 for (int j = 0; j < nbox; j++) {
	    if (boxes[i][j] == null) {
	       boxes[i][j] = new LocatedItemSet();
	    }
	    boxes[i][j].clear();
	 }
      }
      
      int nitem = allItems.size();
      LocatedItem[] lita = allItems.getItems();

      for (int i = 0; i < nitem; i++) {
	 LocatedItem li = lita[i];
	 Position pos = li.getPosition();
	 double xli = pos.getX();
	 double yli = pos.getY();

	 int ix = boundingBox.getXSubdivision(xli);
	 int iy = boundingBox.getYSubdivision(yli);

	 boxes[ix][iy].addItem(li);
      }
   }





}
