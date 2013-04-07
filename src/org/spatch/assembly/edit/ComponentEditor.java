package org.spatch.assembly.edit;

import org.spatch.assembly.base.AssemblyComponent;
import org.spatch.drawing.Shape;
 

public interface ComponentEditor {

   void showComponent(AssemblyComponent cpt);

   void editComponent(AssemblyComponent cpt);

   void showShape(Shape shp);

   void editShape(Shape shp);


}
