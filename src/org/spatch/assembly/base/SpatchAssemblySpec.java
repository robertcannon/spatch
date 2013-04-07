package org.spatch.assembly.base;

import java.util.ArrayList;

import org.spatch.WrapError;
import org.spatch.assembly.edit.AssemblyLabel;
import org.spatch.assembly.edit.IconSpec;
import org.spatch.drawing.Shape;

public interface SpatchAssemblySpec {

	public AssemblyComponent newComponent() throws WrapError;
	 
	public AssemblyComponent newDummyComponent() throws WrapError;
	
	public ArrayList<Shape> getShapeExemplars();

	public double[] getInitialXRange();
	
	public double[] getInitialYRange();
	

	public ArrayList<SpatchAssemblySpec> getSetMemberSpecs();

	public ArrayList<AssemblyLabel> getAssemblyLabels();

	public boolean hasAssemblyLabels();

	public ArrayList<IconSpec> getIconSpecs();

	public boolean hasIconSpecs();

}
