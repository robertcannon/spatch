package org.spatch.assembly.base;

import org.lemsml.jlems.viz.plot.XYLocation;
 
 
public abstract class HandleSet {


   public abstract void locate(XYLocation xyl, double sz);

   public abstract RegionHandle[] getHandles();

}
