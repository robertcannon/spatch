package org.spatch.assembly.base;

import org.lemsml.jlems.viz.plot.XYLocation;
 
public interface Plug {


   XYLocation getXYLocation();

   void setXYLocation(XYLocation xyl);

   void connectTo(Socket skt);

   void disconnect();

   String getID();

   ConnectionFlavor getFlavor();

   boolean isFree();

   Socket getSocket();

   public void syncLocation();

   
   public final static int ANY = 0;
   public final static int INPUT = 1;
   public final static int OUTPUT = 2;
   public final static int SMART = 3;
   
   boolean directionMatches(Socket skt);

   int getCurrentMode();

   void updateFromPeer();
   
}
