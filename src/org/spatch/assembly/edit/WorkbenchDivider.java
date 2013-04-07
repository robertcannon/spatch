
package org.spatch.assembly.edit;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

class WorkbenchDivider extends BasicSplitPaneDivider {
   static final long serialVersionUID = 1001;
   
   
   Color cbg;
   Color cline;

   WorkbenchDivider(BasicSplitPaneUI bspui, Color c) {
      super(bspui);
      setBg(c);
      setBorder(new EmptyBorder(0, 0, 0, 0));
   }


   public void paint(Graphics g) {
      if (cbg == null) {
    	  setBg(new Color(40, 40, 40));
      }
      int w = getWidth();
      g.setColor(cbg);
      g.fillRect(0, 0, getWidth(), getHeight());
      
      g.setColor(cbg.brighter());
      g.drawLine(0, 0, w, 0);
      g.setColor(cbg);
      g.drawLine(0, 1, w, 1);
      g.setColor(cbg.darker());
      g.drawLine(0, 2, w, 2);
   }


   public void setBg(Color c) {
      cbg = c;
      cline = c.brighter();
   }

}

