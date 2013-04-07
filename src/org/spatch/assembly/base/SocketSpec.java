package org.spatch.assembly.base;

import org.lemsml.jlems.viz.plot.Position;
 

public class SocketSpec {

	Position p;
	String id;
	public final static int IN = 1;
	public final static int OUT = 2;
	public final static int INOUT = 3;
	int direction;	
	ConnectionFlavor connectionFlavor;
	
	
	public SocketSpec(String sid, Position p0, int dir, ConnectionFlavor cf) {
		id = sid;
		p = new Position(p0);
		direction = dir;
		connectionFlavor = cf;
	}
	
	
	public Position getPosition() {
		return p;
	}

	public boolean isExternal() {
		return false;
	}

	public String getID() {
		return id;
	}

	public boolean doesIn() {
		return (direction == IN || direction == INOUT);
	}

	public boolean doesOut() {
		return (direction == OUT || direction == INOUT);
	}

	public ConnectionFlavor getFlavor() {
		return connectionFlavor;
	}

}
