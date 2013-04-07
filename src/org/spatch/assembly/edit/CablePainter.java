package org.spatch.assembly.edit;

import java.awt.Color;

import org.lemsml.jlems.viz.plot.IntPosition;
import org.lemsml.jlems.viz.plot.Painter;
import org.lemsml.jlems.viz.plot.Position;
import org.spatch.assembly.base.Cable;
import org.spatch.assembly.base.CablePlug;
import org.spatch.assembly.base.ConnectionFlavor;
 



public class CablePainter {
  

   public CablePainter() {
     
   }



   public boolean isOnCanvas(Painter p, Cable cbl) {

      boolean ret = true;

      if (cbl.hasIntPosition()) {
         IntPosition ipos = cbl.getIntPosition();
         cbl.setAbsolutePosition(p.wop(ipos));
      }

      ret = p.isOnCanvas(cbl.getAbsolutePosition());

      return ret;
   }



   public void instruct(Painter p, Cable cbl) {
 
      if (cbl.hasIntPosition()) {
         IntPosition ipos = cbl.getIntPosition();
         cbl.setPosition(p.wop(ipos));
      }
 

      Position pabs = cbl.getAbsolutePosition();
     
      CablePlug[] cpa = cbl.getPlugs();
      Position pa = cpa[0].getAbsolutePosition();
      Position pb = cpa[1].getAbsolutePosition();
      
      
      
      ConnectionFlavor cf = cbl.getConnectionFlavor();
      
      if (cf == null) {
         p.setColorBlack();
      //   p.drawLine(pa, pabs);
      //   p.drawLine(pb, pabs);
         p.drawCable(pa, pabs, pb, Color.black, 2, true);
         
      } else {
         p.setColor(cf.getColor().getColor()); // REFAC 
         p.drawCable(pa, pabs, pb, cf.getColor().getColor(), 2, true);
      }
   }
 

}
