package org.spatch.assembly.base;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.viz.plot.AbsLocated;
import org.lemsml.jlems.viz.plot.PickablePoint;
import org.lemsml.jlems.viz.plot.Position;
 
  
  
 

public abstract class BasePlug extends SubLocated implements Plug {
 

   private PickablePoint p_pickablePoint;

   private Socket p_socket;

   private Connection r_connection;

   private int p_mode = Plug.ANY;
   
   private int p_currentMode = Plug.ANY;

   private Plug p_peer;
   
   private String p_id;
   
   public BasePlug(SubLocated prnt, String sid) {
      super(prnt);
      p_id = sid;
      p_socket = null;
   }

   
   public String getID() {
	   return p_id;
   }
   
   public void setModeSmart() {
	   p_mode = Plug.SMART;
   }
   
   public void setModeInput() {
      p_mode = INPUT;
   }
   
   public void setModeOutput() {
      p_mode = OUTPUT;
   }
  
   public boolean isInput() {
      boolean ret = false;
      if (p_mode == Plug.OUTPUT || p_currentMode == Plug.OUTPUT) {
    	  ret = false;
      } else {
    	  ret = true;
      }
    return ret;  
   }
   
   
   
   public boolean isOutput() {
    	boolean ret = false;
    	if (p_mode == Plug.INPUT || p_currentMode == Plug.INPUT) {
    		ret = false;
    	} else {
    		ret = true;
    	}
    	return ret;
   }
  
   
   public boolean directionMatches(Socket skt) {
      boolean ret = false;
      if (isInput() && skt.isOutput()) {
         ret = true;
      } else if (isOutput() && skt.isInput()) {
         ret = true;
      }
      return ret;
   }
   
   
   public PickablePoint getPickablePoint() {
      if (p_pickablePoint == null) {
         p_pickablePoint = new PickablePoint(this);
      }
      return p_pickablePoint;
   }


   public void connectTo(Socket skt) {
      p_socket = skt;
      skt.addConnection(this);
      
      if (skt.isInput()) {
    	  p_currentMode = Plug.OUTPUT;
      } else {
    	  p_currentMode = Plug.INPUT;
      }
      if (p_peer != null) {
    	  p_peer.updateFromPeer();
      }
      
      
      if (skt instanceof AbsLocated) {
         setLock((AbsLocated)skt);
      } else {
         E.warning("set socket in plug but socket is not abs located");
      }

      if (r_connection == null) {
    	  r_connection = new Connection(p_id);
      }
      r_connection.connectTo(skt); // skt.getParentID(), skt.getID(), skt.getParent());
   }

   public void disconnect() {
      
	  if (p_socket != null) {
		  p_currentMode = Plug.ANY;
		  if (p_peer != null) {
			  p_peer.updateFromPeer();
		  }
		  p_socket.removeConnection(this);
		  release();
      }
      if (r_connection != null) {
         r_connection.disconnect();
      }
   }

   public void release() {
      p_socket = null;
      releaseLock();
   }


   public Socket getSocket() {
      return p_socket;
   }


   public boolean isFree() {
      return (p_socket == null);
   }


   public boolean isAttached() {
      return (p_socket != null);
   }


   public void reReference() {
      p_socket = null;
   }



   public void setConnection(Connection connection) {
      r_connection = connection;
      
   }
   
   
   public void syncLocation() {
      if (r_connection != null) {
         Position pos = getAbsolutePosition();
         r_connection.setTargetPosition(pos.getX(), pos.getY());
      }
   }


   public void setPeer(Plug p) {
	   p_peer = p;
   }
   
   public int getCurrentMode() {
	   return p_currentMode;
   }
   
   
   public void updateFromPeer() {
	   int pcm = p_peer.getCurrentMode();
	   if (pcm == Plug.ANY) {
		   if (p_socket == null) {
			   // peer no longer connected, and no local socket, so back to any
			   p_currentMode = Plug.ANY;
		   }
		   
		   // nothing to do;
	   } else if (pcm == Plug.OUTPUT) {
		   if (p_currentMode == Plug.OUTPUT) {
			   E.error("self and peer both output?");
		   } else {
			   p_currentMode = Plug.INPUT;
		   }
		   
	   } else if (pcm == Plug.INPUT) {
		   if (p_currentMode == Plug.INPUT) {
			   E.error("self and peer both input?");
		   } else {
			   p_currentMode = Plug.OUTPUT;
		   }
	   }
   }

}
