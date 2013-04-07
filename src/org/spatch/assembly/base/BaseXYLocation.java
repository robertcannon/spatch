
package org.spatch.assembly.base;

import org.lemsml.jlems.viz.plot.XYLocation;
 
 

public class BaseXYLocation implements XYLocation {

   double xpos;
   double ypos;


   public BaseXYLocation(XYLocation xyl) {
      xpos = xyl.getX();
      ypos = xyl.getY();
   }

   public BaseXYLocation(double x, double y) {
      xpos = x;
      ypos = y;
   }


   public String toString() {
      return "(xyloc: " + xpos + ", " + ypos + ")";
   }

   public double getX() {
      return xpos;
   }

   public double getY() {
      return ypos;
   }

}
