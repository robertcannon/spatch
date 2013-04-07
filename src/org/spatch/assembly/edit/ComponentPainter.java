package org.spatch.assembly.edit;

import java.util.ArrayList;
 
import org.lemsml.jlems.viz.plot.IntPosition;
import org.lemsml.jlems.viz.plot.Painter;
import org.lemsml.jlems.viz.plot.Position;
import org.lemsml.jlems.viz.plot.XYLocation;
import org.spatch.assembly.base.AssemblyComponent;
import org.spatch.assembly.base.ComponentPlug;
import org.spatch.assembly.base.Socket;
import org.spatch.assembly.base.SpatchAssembly;
import org.spatch.assembly.base.SpecialStrings;
import org.spatch.drawing.DrawingPainter;

public class ComponentPainter {

	PlugAndSocketPainter plugAndSocketPainter;
	DrawingPainter drawingPainter;

	public ComponentPainter() {
		plugAndSocketPainter = new PlugAndSocketPainter();
		drawingPainter = new DrawingPainter();

	}

	public boolean isOnCanvas(Painter p, AssemblyComponent cpt) {

		boolean ret = true;

		if (cpt.hasIntPosition()) {
			IntPosition ipos = cpt.getIntPosition();
			cpt.setAbsolutePosition(p.wop(ipos));
		}

		ret = p.isOnCanvas(cpt.getAbsolutePosition());

		return ret;
	}

	public void instruct(Painter p, AssemblyComponent cpt,
			ShapePainter shapePainter) {
		int dstat = cpt.getDisplayState();
		// boolean paintPortLabels = (dstat == 1);

		if (cpt.hasIntPosition()) {
			IntPosition ipos = cpt.getIntPosition();
			cpt.setPosition(p.wop(ipos));
		}

		double cscl = cpt.getSize();

		Position pabs = cpt.getAbsolutePosition();

		if (cpt.substructureVisible()) {
			SpatchAssembly ass = cpt.getSubstructure();
			if (ass == null) {
				// TODO - link to popup model chooser;
			 
			} else {
				int ncpt = ass.size();
				if (ncpt == 0) {

				} else if (ncpt == 1) {
					// ADHOC - special case? - hack for refs to externs?
					AssemblyComponent subcpt = ass.getComponent(0);
					instructSubcomponent(p, subcpt, pabs, cscl);

				} else {
					drawingPainter.instruct(p, cpt.getIcon(), pabs, cscl);

					for (Socket skt : ass.getSockets()) {
						XYLocation xyl = skt.getXYLocation();
						double xpos = xyl.getX();
						double ypos = xyl.getY();

						p.setColorWhite();
						p.drawIntCircle(pabs.getX() + cscl * xpos, pabs.getY()
								+ cscl * ypos, 3);
					}
				}
			}

		} else {
			drawingPainter.instruct(p, cpt.getIcon(), pabs, cscl);

			// if (cpt instanceof RefComponent) {
			// E.info("drawing ref cpt at " + pabs + " " + cscl + " " +
			// cpt.getIcon());
			// }
		}

		if (dstat >= 0) {
			String cid = cpt.getID();
			if (cid != null && !cid.equals(SpecialStrings.NONE_STRING) && cid.length() > 0) {
				p.setColorBlack();
				p.drawXCenteredYTopAlignedLabel(cid, pabs.getX(), pabs.getY() - cscl);
			}
		}

		ArrayList<ComponentPlug> plugs = cpt.getPlugs();
		if (plugs != null) {
			for (ComponentPlug plug : plugs) {
				plugAndSocketPainter.instructPlug(p, plug, cscl);
			}
		}
		
		
		// TODO - shouldn't need this: put sockets in the vector icon
		ArrayList<Socket> sockets = cpt.getSockets();
		if (sockets != null) {
			for (Socket skt : sockets) {
				plugAndSocketPainter.instructSocket(p, skt, cscl, true);
			}
		}
	}

	public void paintLabels(Painter p, AssemblyComponent cpt) {
		// sockets are now part of the icon,
		double cscl = cpt.getSize();
		ArrayList<Socket> sockets = cpt.getSockets();
		if (sockets != null) {
			p.setColorDarkGray();
			for (Socket skt : sockets) {
				plugAndSocketPainter.instructSocketLabel(p, skt, cscl);
			}
		}

		ArrayList<ComponentPlug> plugs = cpt.getPlugs();
		if (plugs != null) {
			for (ComponentPlug plg : plugs) {
				plugAndSocketPainter.instructPlugLabel(p, plg, cscl);
			}
		}
	}

	private void instructSubcomponent(Painter p, AssemblyComponent subcpt,
			Position ppar, double scale) {

		drawingPainter.instruct(p, subcpt.getIcon(), ppar, scale);

		ArrayList<Socket> sockets = subcpt.getSockets();
		if (sockets != null) {
			for (Socket socket : sockets) {

				XYLocation xyl = socket.getRelativeXYLocation();
				double xpos = xyl.getX();
				double ypos = xyl.getY();

				p.setColorGreen();
				p.fillIntCircle(ppar.getX() + scale * xpos, ppar.getY() + scale
						* ypos, 3);
			}
		}
	}

}
