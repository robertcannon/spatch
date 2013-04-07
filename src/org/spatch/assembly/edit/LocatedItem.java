

package org.spatch.assembly.edit;

import org.lemsml.jlems.viz.plot.Position;
import org.lemsml.jlems.viz.plot.XYLocation;
 



public class LocatedItem {

   Position pos;

   Object ref;


   public LocatedItem() {
      pos = new Position();
   }

   public LocatedItem(XYLocation p, Object obj) {
      this();
      set(p, obj);
   }


   public void set(XYLocation ps, Object obj) {
      pos.set(ps);
      ref = obj;
   }

   
   public Position getPosition() {
      return pos;
   }


   public Object getRef() {
      return ref;
   }


}
