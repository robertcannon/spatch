package org.spatch.assembly.base;

import org.lemsml.jlems.viz.plot.Position;
 
 


public class CablePlug extends BasePlug {

   private Cable p_cable;
   
  
   public CablePlug(Cable cbl, String sid, Position prel) {
      super(cbl, sid);
      
      p_cable = cbl;
     
      setRelativeAttachment(new Position(0., 0.));

      setRelativePosition(prel);
   
   }
 

   public ConnectionFlavor getFlavor() {
       ConnectionFlavor ret = null;
       Socket skt = getSocket();
       if (skt != null) {
          ret = skt.getFlavor();
       }
       return ret;
   }


   public Cable getCable() {
      return p_cable;
   }




 

}
