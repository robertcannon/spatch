package org.spatch.assembly.edit;

import org.lemsml.jlems.viz.plot.Position;
 
public interface ContextMenu {

   
   public void setContext(String[] options, SelectionActor h);

   public void showAt(Position absolutePosition);
   
   
}
