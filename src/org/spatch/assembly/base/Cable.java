package org.spatch.assembly.base;

import java.util.HashMap;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.viz.plot.PickablePoint;
import org.lemsml.jlems.viz.plot.Position;
 

public class Cable extends SubLocated implements RowPeer {

	public String id;

	private boolean updatePickable = true;
	
	@SuppressWarnings("unused")
	private HandleSet handleSet;

	private CablePlug[] plugs;

	private PickablePoint p_pickablePoint;

	@SuppressWarnings("unused")
	private Assembly r_parent;

	private ConnectionFlavor conFlavor;

	public Cable() {
		super(null);
		silentSetSize(1.0);
		plugs = new CablePlug[2];
		plugs[0] = new CablePlug(this, "A", new Position(-1.2, -0.3));
		plugs[1] = new CablePlug(this, "B", new Position(1.2, -0.3));
		plugs[0].setModeSmart();
		plugs[1].setModeSmart();
		
		plugs[0].setPeer(plugs[1]);
		plugs[1].setPeer(plugs[0]);
		
		conFlavor = null;
	}

	public ConnectionFlavor getConnectionFlavor() {
		return conFlavor;
	}

	public void setID(String s) {
		id = s;
	}

	public String getID() {
		return id;
	}

	public void setParent(Assembly a) {
		r_parent = a;
	}

	public void reconnect(HashMap<String, AssemblyComponent> hm) {
		Connection[] ca = new Connection[0]; // sgetConnectionsFromPeer();
		conFlavor = null;

		if (ca != null) {
			for (Connection con : ca) {
				String st = con.getTargetID();
				String ss = con.getSocketID();
				if (st == null) {

				} else if (st.equals("workpiece")) {
					E.missing("cant connect to workpiece yet");
					// see TableComponent for reconectToWorkpiece

				} else if (hm.containsKey(st)) {
					AssemblyComponent tgt = hm.get(st);

					CablePlug from = getPlug(con.getPlugID());

					Socket skt = tgt.getSocket(ss);
					if (from != null && skt != null) {
						from.connectTo(skt);

						if (conFlavor == null) {
							conFlavor = skt.getFlavor();
						}

					}

				} else {
					E.error("cant reconnect to " + st + " no such cpt");
				}
			}
		}
	}

	private CablePlug getPlug(String pid) {
		CablePlug ret = null;
		for (int i = 0; i < plugs.length; i++) {
			if (plugs[i].getID().equals(pid)) {
				ret = plugs[i];
				break;
			}
		}
		if (ret == null) {
			E.error("component cant find plug " + pid + " in " + this);
		}
		return ret;
	}

	public CablePlug[] getPlugs() {
		return plugs;
	}

	public void quietSetPosition(Position pos) {
		setAbsolutePosition(pos);
		updatePickable = true;
	}

	public void setPosition(Position pos) {
		quietSetPosition(pos);
		notifyDependentsOfMove();
	}

	public void notifyDependentsOfMove() {
		if (plugs != null) {
			for (int i = 0; i < plugs.length; i++) {
				plugs[i].parentMoved();
			}
		}
		// syncPositionToPeer(getRelativePosition());

	}

	public PickablePoint getHandlePoint() {
		if (p_pickablePoint == null) {
			p_pickablePoint = new PickablePoint(getAbsolutePosition(), this);
		}

		if (updatePickable) {
			p_pickablePoint.moveTo(getAbsolutePosition());
			updatePickable = false;
		}

		return p_pickablePoint;
	}

	public void flavorize() {
		conFlavor = null;
		for (Plug p : getPlugs()) {
			ConnectionFlavor pf = p.getFlavor();

			if (pf == null) {
				// fine
			} else if (conFlavor == null) {
				conFlavor = pf;

			} else if (conFlavor.matches(pf)) {
				// as it should be;
			} else {
				E.error("misconnected cable: " + conFlavor + " matched with " + pf);
			}
		}

	}

	public boolean isVirgin() {
		return (conFlavor == null);
	}

	public boolean flavorMatches(Socket skt) {
		boolean ret = true;
		if (conFlavor != null) {
			if (conFlavor.matches(skt.getFlavor())) {
				ret = true;
			} else {
				ret = false;
			}
		}
		return ret;
	}

	public void disconnect() {
		for (Plug p : getPlugs()) {
			p.disconnect();
		}
		conFlavor = null;
	}

	public void notifyObservers() {
		// TODO Auto-generated method stub
	}

	public Cable makeCopy() {
		Cable cbl = new Cable();
		cbl.setRelativePosition(getRelativePosition());
		cbl.setSize(getSize());
		return cbl;
	}

	public Cable makeScaledCopy(double fac) {
		Cable cbl = makeCopy();
		cbl.setSize(getSize() * fac);
		return cbl;
	}

	@Override
	public void initFromRow(Row table) {
		// TODO Auto-generated method stub

	}

	@Override
	public void attachRow(Row table) {
		// TODO Auto-generated method stub

	}

	@Override
	public Row getRow() {
		// TODO Auto-generated method stub
		return null;
	}

}
