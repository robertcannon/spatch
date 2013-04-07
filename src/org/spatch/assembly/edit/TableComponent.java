package org.spatch.assembly.edit;

import org.lemsml.jlems.viz.plot.Position;
import org.spatch.assembly.base.AssemblyComponent;
import org.spatch.assembly.base.Connection;
import org.spatch.drawing.VectorIcon;

public abstract class TableComponent extends AssemblyComponent {

	public abstract AssemblyComponent makeDummyCopy();
	
	public abstract AssemblyComponent makeCopy();

	public abstract AssemblyComponent makeScaledCopy(double newComponentScaleFactor);

	public abstract String[] getMenuOptions();

	@Override
	public abstract Connection[] getConnectionsFromPeer();

	@Override
	public abstract VectorIcon getIcon();

	@Override
	public abstract boolean worldSized();

	@Override
	public abstract String getTag();

	@Override
	public abstract void syncPositionToPeer(Position p);

	public abstract boolean isMenuSettable();

}
