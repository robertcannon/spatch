
package org.spatch.assembly.edit;
 

import java.awt.Color;
 
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.viz.plot.Painter;
import org.lemsml.jlems.viz.plot.Position;
import org.lemsml.jlems.viz.plot.XYLocation;
import org.spatch.assembly.base.ComponentPlug;
import org.spatch.assembly.base.PlugSpec;
import org.spatch.assembly.base.Socket;
import org.spatch.drawing.DrawingPainter;
import org.spatch.drawing.VectorIcon;
import org.spatch.drawing.VectorIconed;


public class PlugAndSocketPainter {

   DrawingPainter drawingPainter;


   public PlugAndSocketPainter() {
      drawingPainter = new DrawingPainter();
   }



   public void instructPlug(Painter p, ComponentPlug plug, double cptScl) {

      PlugSpec ps = plug.getSpec();

      Position pos0 = plug.getAbsoluteAttachmentPosition();
      Position pos1 = plug.getAbsolutePosition();

      

      Color c = ps.getCableColor();
    
      //Color c = Color.black;
      //if (sc != null) {
      //   c = sc.getColor();
     // }
      
      p.setColor(c);
      p.drawLine(pos0, pos1);

      if (ps == null) {
         p.setColor(Color.yellow);
         p.drawCenteredOval(pos1, 4, 4);
      } else {
         VectorIcon vi = ps.getVectorIcon();
         if (vi == null) {
            p.setColor(Color.red);
            p.drawCenteredOval(pos1, 4, 4);
         } else {

            drawingPainter.instruct(p, vi, pos1, 0.1 * cptScl);
         }
      }

      /*
      if (lab) {
         p.drawLeftAlignedLabel(plug.getID(), pos1.getX(), pos1.getY());
      }
      */
   }



   public void instructSocket(Painter p, Socket socket, double cptScl, boolean lab) {

      // no longer needed - sockets are part of the parent icon
      
      XYLocation xyl = socket.getXYLocation();
      double xpos = xyl.getX();
      double ypos = xyl.getY();


//      TODO someone should cache image;

       VectorIcon vir = null;
      if (socket instanceof VectorIconed) {
         vir = ((VectorIconed)socket).getVectorIcon();
         if (vir == null) {
            E.warning("vid skt with null icon?");
         }
      }

      Position pos = new Position(xpos, ypos);

      if (vir == null) {
    	  Color cfill = Color.white;
    	  if (socket.isConnected()) {
    		  cfill = Color.black;
    	  }
    	  
    	  p.setColor(Color.blue);
          p.fillExactCenteredOval(pos, 3, 3, cfill, Color.blue, 1.);
          
      } else {
       //  drawingPainter.instruct(p, vir, pos, 0.1 * cptScl);
      }
   }

   
   public void instructPlugLabel(Painter p, ComponentPlug plug, double cptScl) {
      
      Position pos1 = plug.getAbsolutePosition();

      p.drawLeftAlignedLabel(plug.getID(), pos1.getX(), pos1.getY());
   }
    

   public void instructSocketLabel(Painter p, Socket socket, double cptScl) {

      XYLocation xyl = socket.getXYLocation();
      double xpos = xyl.getX();
      double ypos = xyl.getY();
     
          XYLocation xyr = socket.getRelativeXYLocation();

         String sid = socket.getID();
         double x = xyr.getX();
         double y = xyr.getY();

         if (x > 0 && x > Math.abs(y)) {
            p.drawLeftAlignedLabel(sid, xpos, ypos);

         } else if (x < 0 && x < Math.abs(y)) {
            p.drawRightAlignedLabel(sid, xpos, ypos);

         } else if (y > 0) {
            p.drawXCenteredYBottomAlignedLabel(sid, xpos, ypos);

         } else {
            p.drawXCenteredYTopAlignedLabel(sid, xpos, ypos);
         }
      }

    
}
