
package org.spatch.assembly.base;

import java.util.ArrayList;
 

public class ComponentSocket extends SubLocated implements Socket {


   private final SocketSpec p_spec;

   ArrayList<Plug> connectedPlugs = new ArrayList<Plug>();

   boolean connected = false;
   
   public ComponentSocket(SocketSpec ss, AssemblyComponent cpt) {
      super(cpt);
      p_spec = ss;
      setRelativePosition(p_spec.getPosition());
   }


   public boolean isExternal() {
      return p_spec.isExternal();
   }
   
   public String getID() {
      return p_spec.getID();
   }

  
   public boolean isInput() {
      return p_spec.doesIn();
   }
   
   public boolean isOutput() {
      return p_spec.doesOut();
   }
   
   
   
   public String getParentID() {
      return ((AssemblyComponent)getParent()).getID();
   }

   public AssemblyComponent getComponent() {
      return (AssemblyComponent)(getParent());
   }


   public SocketSpec getSpec() {
      return p_spec;
   }

   
   public ConnectionFlavor getFlavor() {
      return p_spec.getFlavor();
   }

   public boolean isConnected() {
	   return connected;
   }
   
   public boolean checkConnected() {
	   boolean ret = false;
	   if (connectedPlugs.size() > 0) {
		   ret = true;
	   }
	   return ret;
   }
 
   public void addConnection(Plug p) {
	   connectedPlugs.add(p);
	   checkLast();
   }


   @Override
   public void removeConnection(Plug p) {
	   connectedPlugs.remove(p);
	   checkLast();
   }

   private void checkLast() {
	   if (connectedPlugs.size() > 0) {
		   connected = true;
	   } else {
		   connected = false;
	   }
   }

}
