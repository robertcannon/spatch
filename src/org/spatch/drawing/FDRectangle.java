package org.spatch.drawing;
 
import java.awt.Color;

import org.lemsml.jlems.viz.plot.Painter;
 



public class FDRectangle extends FixedDrawingComponent {

 
   public double x;
   public double y;
   public double rx;
   public double ry;


   public FDRectangle() {
   }


   public FDRectangle(double[] xywh) {
	   this(xywh[0], xywh[1], xywh[2]/2, xywh[3]/2);
   }
   
   
   public FDRectangle(double x1, double y1, double rx1, double ry1) {
      x = x1;
      y = y1;
      rx = rx1;
      ry = ry1;
      setColor(Color.black);
      setClosed();
      setFillColor(Color.orange);
   }



public void setSize(double sx, double sy) {
      rx = sx;
      ry = sy;
   }
   
   public void instruct(Painter p, double offx, double offy, double scale) {
	   double xdr = offx + scale * x;
	   double ydr = offy + scale * y;
	   double xw = scale * rx;
	   double yw = scale * ry;
	   
	   double cr = scale * cornerRadius;
	   
	   if (cr > 0.) {
		   if (isFilled()) {
			   p.drawFilledRoundedRectangle(xdr, ydr, xw, yw, cr, getFillColor(), getColor(), getWidth(), widthIsPixels());
		   } else {
			   p.drawRoundedRectangle(xdr, ydr, xw, yw, cr, getColor(), getWidth(), widthIsPixels());
		   }
		   
	   } else {
		   if (isFilled()) {
			   p.drawFilledRectangle(xdr, ydr, xw, yw, getFillColor(), getColor(), getWidth(), widthIsPixels());
		   } else {
			   p.drawRectangle(xdr, ydr, xw, yw, getColor(), getWidth(), widthIsPixels());
		   }
	   }
   }



   public void applyToShape(Shape shp) {
      shp.setCurviness(0.0);
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
