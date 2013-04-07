package org.spatch.drawing;

import org.lemsml.jlems.viz.plot.Painter;
 


public class FDOval extends FixedDrawingComponent {



   public double x;
   public double y;
   public double rx;
   public double ry;
 
   public FDOval() {
	   super();
   }
   
   public FDOval(double ax, double ay, double aw, double ah) {
	   x = ax;
	   y = ay;
	   rx = aw / 2;
	   ry = ah / 2;
   }
   
   public FDOval(double[] xywh) {
      super();
      x = xywh[0];
      y = xywh[1];
      rx = xywh[2] / 2.;
      ry = xywh[3] / 2.;
   }
   
   
   public void setSize(double sx, double sy) {
      rx = sx;
      ry = sy;
   }
   
   
   
   
   public void instruct(Painter p, double offx, double offy, double scale) {
      double w = getWidth();
      if (isFilled()) {
         p.drawFilledOval(offx + scale * x, offy + scale * y, 
                scale * rx, scale * ry, getFillColor(), getColor(),
                w, widthIsPixels());
      } else if (w > 0.5){
         p.drawOval(offx + scale * x, offy + scale * y, 
               scale * rx, scale * ry, getColor(), w, widthIsPixels());

      }
   }


   public void applyToShape(Shape shp) {
      shp.setCurviness(1.0);
      applySymmetryToShape(shp);

      double[] xpts = { x - rx, x - rx, x + rx, x + rx };
      double[] ypts = { y - ry, y + ry, y + ry, y - ry };
      shp.setXpts(xpts);
      shp.setYpts(ypts);
   }


   public void applySymmetryToShape(Shape shp) {
      shp.setSymmetry(ShapeSymmetry.RECTANGLE);
   }

}
