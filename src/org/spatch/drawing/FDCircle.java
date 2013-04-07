package org.spatch.drawing;


import java.awt.Color;



public class FDCircle extends FDOval {

   
   public FDCircle() {
      super();
   }
   
   public FDCircle(double[] xywh) {
	   super(xywh);
   }

   
   public FDCircle(double x, double y, double diam) {
	   super(x, y, diam, diam);
   }
   
public static FDCircle defaultCircle() {
       FDCircle d = new FDCircle();
       d.rx = 1.0;
       d.ry = 1.0;
       d.setColor(Color.red);
       d.setFilled();
       d.setFillColor(Color.blue);
       return d;
    }
  
    
    
    public void applySymmetryToShape(Shape shp) {
       shp.setSymmetry(ShapeSymmetry.SQUARE);
    }


}
