
package org.spatch.assembly.edit;

import org.lemsml.jlems.viz.plot.Box;
import org.lemsml.jlems.viz.plot.Position;
import org.lemsml.jlems.viz.plot.Size;
import org.lemsml.jlems.viz.plot.XYLocation;
 

public class LocatedItemSet {

   int nitem;
   LocatedItem[] items;


   public LocatedItemSet() {
      items = new LocatedItem[10];
   }


   public void add(XYLocation p, Object ref) {
      if (nitem < items.length && items[nitem] != null) {
	 items[nitem++].set(p, ref);
      } else {
	 addItem(new LocatedItem(p, ref));
      }
   }



   public void addItem(LocatedItem lit) {

      if (nitem >= items.length) {
	 LocatedItem[] lia = new LocatedItem[2 * nitem];
	 System.arraycopy(items, 0, lia, 0, nitem);
	 items = lia;
      }

      items[nitem++] = lit;
   }


   public void clear() {
      nitem = 0;
   }

   
   public int size() {
      return nitem;
   }

   public LocatedItem[] getItems() {
      return items;
   }


   public Box getBoundingBox() {
      Box ret = null;
      if (nitem > 0) {
	 ret = new Box(items[0].getPosition());
	 for (int i = 2; i < nitem; i++) {
	    ret.extendTo(items[i].getPosition());
	 }
      } 
      return ret;
   }



   public LocatedItem getWithinRange(XYLocation pos, Size sz) {
      double x = pos.getX();
      double y = pos.getY();
      double dx = 8 * sz.getWidth(); // ADHOC
      double dy = 8 * sz.getHeight();
      

      LocatedItem ret = null;
      for (int i = 0; i < nitem; i++) {
	 LocatedItem lit = items[i];
	 Position pit = lit.getPosition();
	 double xit = pit.getX();
	 double yit = pit.getY();

	 if (Math.abs(x - xit) < dx && Math.abs(y - yit) < dy) {
	    ret = lit;
	    break;
	 }
      }
      return ret;
   }


}
