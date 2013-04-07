package org.spatch.drawing;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.viz.plot.Painter;
import org.lemsml.jlems.viz.plot.Position;
 


public class DrawingPainter {


   public DrawingPainter() {

   }



   public void instruct(Painter p, FixedDrawing dr, Position pos, double scale) {
      instruct(p, dr, pos.getX(), pos.getY(), scale);
   }



   public void instruct(Painter p, FixedDrawing dr, double cx, double cy, double scale) {
      if (dr == null) {
         E.warning("null drawing in painter.instruct");

      } else {
         dr.instruct(p, cx, cy, scale);
      }
   }





}
