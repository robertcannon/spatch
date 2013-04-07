package org.spatch.assembly.base;

import java.util.ArrayList;

import org.spatch.drawing.Shape;
 


public interface SpatchAssembly {
  
	public ArrayList<? extends AssemblyComponent>  getComponents();

	public int size();

	public AssemblyComponent getComponent(int i);

	public ArrayList<Socket> getSockets();

	public void deleteComponent(AssemblyComponent obj);

	public void addCable(Cable cbl);

	public void addShape(Shape newDragShape);

	public String getSizeUnit();

	public ArrayList<Shape> getShapes();
	

	public ArrayList<Cable> getCables();

	public void addObserver(AssemblyObserver ao);

	public void deleteCable(Cable obj);

	public void addComponent(AssemblyComponent cpt);

	public void deleteShape(Shape parent);

	public SpatchAssemblySpec getAssemblySpec();

	public void removeObserver(AssemblyObserver ao);

	public double getTypicalSize();

}
