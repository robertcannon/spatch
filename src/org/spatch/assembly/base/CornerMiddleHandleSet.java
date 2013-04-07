package org.spatch.assembly.base;

import org.lemsml.jlems.viz.plot.XYLocation;
 
 

public class CornerMiddleHandleSet extends HandleSet {

   RegionHandle[] handles;


   public CornerMiddleHandleSet(Object obj) {
      handles = new RegionHandle[8];
      handles[0] = new RegionHandle(RegionHandle.TOP_RIGHT, obj);
      handles[1] = new RegionHandle(RegionHandle.TOP_LEFT, obj);
      handles[2] = new RegionHandle(RegionHandle.BOTTOM_LEFT, obj);
      handles[3] = new RegionHandle(RegionHandle.BOTTOM_RIGHT, obj);

      handles[4] = new RegionHandle(RegionHandle.MIDDLE_RIGHT, obj);
      handles[5] = new RegionHandle(RegionHandle.MIDDLE_LEFT, obj);
      handles[6] = new RegionHandle(RegionHandle.MIDDLE_TOP, obj);
      handles[7] = new RegionHandle(RegionHandle.MIDDLE_BOTTOM, obj);
   }


   public RegionHandle[] getHandles() {
      return handles;
   }


   public void locate(XYLocation xyl, double sz) {
      double x = xyl.getX();
      double y = xyl.getY();
      // corners anticlockwise from top right;
      handles[0].setPosition(x + sz, y + sz);
      handles[1].setPosition(x - sz, y + sz);
      handles[2].setPosition(x - sz, y - sz);
      handles[3].setPosition(x + sz, y - sz);

      // middle rt and left;
      handles[4].setPosition(x + sz, y);
      handles[5].setPosition(x - sz, y);

      // top and bottom;
      handles[6].setPosition(x, y + sz);
      handles[7].setPosition(x, y - sz);
   }

}
