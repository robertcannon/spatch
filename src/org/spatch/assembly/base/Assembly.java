package org.spatch.assembly.base;

import java.util.ArrayList;

public interface Assembly {

   public ArrayList<? extends Object> getComponents();
 
   public ArrayList<Socket> getSockets();
   
   public String getSizeUnit();
   
   public double getTypicalSize();
   
}
