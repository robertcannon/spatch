package org.spatch.assembly.base;

import org.lemsml.jlems.viz.plot.Position;
 
public class Connection {

	   String plugID;
	   String targetID;
	   String socketID;
	   Object target;
	   
	   String reconTargetID;
	   String reconSocketID;
	
	   // these apply if ther eis no target socket - when the connection is to
	   // a physical position
	   double xtarget;
	   double ytarget;
	   
	
	public void connectTo(Socket skt) { 
		connectTo(skt.getParentID(), skt.getID(), skt.getParent());
	} 

	 
	  
	   public Connection(String id) {
	      plugID = id;
	   }
	   
	   
	   public void connectTo(String stg, String ssk, Object tgt) {
	      targetID = stg;
	      socketID = ssk;
	      target = tgt;
	   }
	   
	   public double getXTarget() {
	      return xtarget;
	   }
	   
	   public double getYTarget() {
	      return ytarget;
	   }
	   
	   public void disconnect() {
	      targetID = null;
	      socketID = null;
	   }
	   
	   
	   public String getPlugID() {
	      return plugID;
	   }
	   
	   public String getTargetID() {
	      return targetID;
	   }
	   
	   public String getSocketID() { 
	      return socketID;
	   }


	   public void setReconnectData(String to, String on, String sx, String sy) {
	      socketID = to;
	      targetID = on;
	      if (sx != null) {
	         xtarget = Double.parseDouble(sx);
	      }
	      if (sy != null) {
	         ytarget = Double.parseDouble(sy);
	      }
	   }

	   public void setTargetPosition(double x, double y) {
	      xtarget = x;
	      ytarget = y;
	   }
	   

	   public Position getTargetPosition() {
	      return new Position(xtarget, ytarget);
	   }
	   
	   
	   public boolean isConnected() {
	      return (targetID != null);
	   }


	   public Object getTarget() {
	      return target;
	   }
	     
	
	 
	 

}
