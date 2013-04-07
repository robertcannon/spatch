package org.spatch.assembly.edit;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;


final class WorkbenchSplitPanelUI extends BasicSplitPaneUI {

   Color cbg;
   
   

   WorkbenchSplitPanelUI() {
      this(Color.black);
   }
   
   
   WorkbenchSplitPanelUI(Color c) {
      super();
      cbg = c;
   }
   
   public static ComponentUI createUI(JComponent jcomponent) {
         return (new WorkbenchSplitPanelUI());
   }

   public BasicSplitPaneDivider createDefaultDivider() {
      return (new WorkbenchDivider(this, cbg));
   }
   
   
   public void paint(Graphics graphics, JComponent jcomponent) {

   }
   
   
   protected void uninstallDefaults() {
   }
   
}

