package org.spatch.assembly.base;

import org.lemsml.jlems.viz.plot.PickablePoint;
import org.lemsml.jlems.viz.plot.Position;
 
 

public class RegionHandle {

   public static final int[][] TOP_RIGHT = {{-6, 0, 0}, {0, 0, 6}, {1, 1}};
   public static final int[][] TOP_LEFT = {{0, 0, 6}, {6, 0, 0}, {-1, 1}};
   public static final int[][] BOTTOM_LEFT = {{0, 0, 6}, {-6, 0, 0}, {-1, -1}};
   public static final int[][] BOTTOM_RIGHT = {{-6, 0, 0}, {0, 0, -6}, {1, -1}};

   public static final int[][] MIDDLE_RIGHT = {{0, 0}, {-6, 6}, {1, 0}};
   public static final int[][] MIDDLE_LEFT = {{0, 0}, {-6, 6}, {-1, 0}};
   
   public static final int[][] MIDDLE_TOP = {{-6, 6}, {0, 0}, {0, 1}};
   public static final int[][] MIDDLE_BOTTOM = {{-6, 6}, {0, 0}, {0, -1}};


   int[] xpts;
   int[] ypts;
   int[] action;
   
   double xpos;
   double ypos;

   PickablePoint pickablePoint;

   Object peer;

   Position manipPosition;
   double manipSize;
   
   
   public RegionHandle(int[][] pts, Object obj) {
      peer = obj;

      xpts = pts[0];
      ypts = pts[1];
      action = pts[2];
      pickablePoint = new PickablePoint(this);
   }


   public void setPosition(double x, double y) {
      xpos = x;
      ypos = y;
      pickablePoint.setPosition(xpos, ypos);
   }


   public double getX() {
      return xpos;
   }

   public double getY() {
      return ypos;
   }

   public int[] getXPts() {
      return xpts;
   }

   public int[] getYPts() {
      return ypts;
   }


   public PickablePoint getPickablePoint() {
       return pickablePoint;
   }


   public void manipPositionSize(Position downPosition, double downSize, Position pos) {
      double xl = downPosition.getX() - downSize;
      double xr = downPosition.getX() + downSize;
      
      double yb = downPosition.getY() - downSize;
      double yt = downPosition.getY() + downSize;
      
      double xp = pos.getX();
      double yp = pos.getY();
      
      manipSize = downSize;
      
      double m = 0.1 * downSize;
     
      if (action[0] == 1) {
            xr = xp;
            if (xr < xl + m) {
               xr = xl + m;
            }
      }
      
      if (action[0] == -1) {
            xl = xp;
            if (xl > xr - m) {
               xl = xr - m;
            }
      }
         
      if (action[1] == 1) {
         yt = yp;
         if (yt < yb + m) {
            yt = yb + m;
         }
      }
   
      if (action[1] == -1) {
         yb = yp;
         if (yb > yt - m) {
            yb = yt - m;
         }
      }
       
      if (action[0] == 0) {
         manipSize = (yt - yb) / 2.;
         
      } else if (action[1] == 0) {
         manipSize = (xr - xl) / 2.;

      } else {
         manipSize = ((yt - yb)  + (xr - xl)) / 4.;
      }

      
         manipPosition = new Position(downPosition);
         if (action[0] == 1) {
            manipPosition.setX(xl + manipSize);
            
         } else if (action[0] == -1) {
            manipPosition.setX(xr - manipSize);
         }
         
         if (action[1] == 1) {
            manipPosition.setY(yb + manipSize);
            
         } else if (action[1] == -1) {
            manipPosition.setY(yt - manipSize);
         }
     
      }
   

   

   public Position getManipPosition() {
      return manipPosition;
   }


   public double getManipSize() {
      return manipSize;
   }

}
