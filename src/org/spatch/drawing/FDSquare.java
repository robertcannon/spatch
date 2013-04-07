package org.spatch.drawing;


import java.awt.Color;



public class FDSquare extends FDRectangle {


   public FDSquare() {
      super();
   }
   
   public FDSquare(double d, double e, double f, double g) {
      super(d, e, f, g);
   }

   
  public static FDSquare defaultSquare() {
      FDSquare d = new FDSquare();
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
