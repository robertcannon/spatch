package org.spatch.assembly.edit;
 
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.viz.plot.Painter;
import org.lemsml.jlems.viz.plot.Position;
import org.spatch.drawing.Shape;

 


public class ShapePainter {

 


   public boolean isOnCanvas(Painter p, Shape shp) {

      boolean ret = true;

      if (shp.hasIntPosition()) {
         shp.setPosition(p.wop(shp.getIntPosition()));
      }

      ret = p.isOnCanvas(shp.getPosition());
      return ret;
   }

   
   
   public void instruct(Painter p, Shape shp, Position cpos, double scale) {
      
      E.info("drawing a shape at " + cpos.getX() + "   scale " + scale);
      
      double dcn = shp.getCurviness();
      if (dcn < 0.001) {
         drawAngularShape(p, shp, cpos.getX(), cpos.getY(), scale);

      } else if (dcn > 0.999) {
         drawRoundShape(p, shp, cpos.getX(), cpos.getY(), scale);

      } else {
         E.shortWarning("need intermediate smoothness");
         drawAngularShape(p, shp, cpos.getX(), cpos.getY(), scale);
      }
   }
   


   public void instruct(Painter p, Shape shp) {
      if (shp.hasIntPosition()) {
         shp.setPosition(p.wop(shp.getIntPosition()));
      }

      double dcn = shp.getCurviness();
      if (dcn < 0.001) {
         drawAngularShape(p, shp);

      } else if (dcn > 0.999) {
         drawRoundShape(p, shp);

      } else {
         E.missing("need intermediate smoothness");
         drawAngularShape(p, shp);
      }
   }


   private void drawRoundShape(Painter p, Shape shp) {
      double[] xp = shp.getXPts();
      double[] yp = shp.getYPts();

      double w = shp.getLineWidth();

      if (shp.isRectangular()) {
         double xc = 0.5 * (xp[0] + xp[2]);
         double yc = 0.5 * (yp[0] + yp[2]);
         double rx = Math.abs(xp[0] - xc);
         double ry = Math.abs(yp[0] - yc);
      
          if (shp.isFilled()) {
             p.fillCenteredOval(xc, yc, rx, ry, shp.getFillColor());
          }
         if (w > 0.) {
             p.drawCenteredOval(xc, yc, rx, ry, shp.getLineColor(), w, true);
         }
      } else {
       E.missing();  
       
      }
   }



   private void drawAngularShape(Painter p, Shape shp) {
      double[] xp = shp.getXPts();
      double[] yp = shp.getYPts();

      double w = shp.getLineWidth();

      if (shp.isFilled()) {
         p.fillPolygon(xp, yp, xp.length, shp.getFillColor());
      }

      if (w > 0.) {
         if (shp.isClosed()) {
            p.drawPolygon(xp, yp, xp.length, shp.getLineColor(), w, true);
         } else {
            p.drawPolyline(xp, yp, xp.length, shp.getLineColor(), w, true);
         }
      }

   }

   
   private double[] offsetScaled(double[] va, double c, double scl) {
      int n = va.length;
      double[] ret = new double[n];
      for (int i = 0; i < n; i++) {
         ret[i] = c + scl * va[i];
      }
      return ret;
   }
   
   
   private void drawRoundShape(Painter p, Shape shp, double xcen, double ycen, double scl) {
      double[] xp = offsetScaled(shp.getXPts(), xcen, scl);
      double[] yp = offsetScaled(shp.getYPts(), ycen, scl);

      double w = shp.getLineWidth();

      if (shp.isRectangular()) {
         double xc = 0.5 * (xp[0] + xp[2]);
         double yc = 0.5 * (yp[0] + yp[2]);
         double rx = Math.abs(xp[0] - xc);
         double ry = Math.abs(yp[0] - yc);
      
          if (shp.isFilled()) {
             p.fillCenteredOval(xc, yc, rx, ry, shp.getFillColor());
          }
         if (w > 0.) {
             p.drawCenteredOval(xc, yc, rx, ry, shp.getLineColor(), w, true);
         }
      } else {
       E.missing();  
       
      }
   }



   private void drawAngularShape(Painter p, Shape shp, double xcen, double ycen, double scl) {
    
      double[] xp = offsetScaled(shp.getXPts(), xcen, scl);
      double[] yp = offsetScaled(shp.getYPts(), ycen, scl);
    
      double w = shp.getLineWidth();

      if (shp.isFilled()) {
         p.fillPolygon(xp, yp, xp.length, shp.getFillColor());
      }

      if (w > 0.) {
         if (shp.isClosed()) {
            p.drawPolygon(xp, yp, xp.length, shp.getLineColor(), w, true);
         } else {
            p.drawPolyline(xp, yp, xp.length, shp.getLineColor(), w, true);
         }
      }

   }


}
