package org.spatch.assembly.edit;

 
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

import org.lemsml.jlems.viz.plot.BasePanel;

public class WorkbenchSplitPanel extends BasePanel {
   static final long serialVersionUID = 1001;
   
   JSplitPane jSplitPane;

   Color cbg;



   
   public WorkbenchSplitPanel(BasePanel c1, BasePanel c2) {

      super();

      cbg = (Color.black);

      boolean CONTINUOUS_LAYOUT = true;

      setLayout(new GridLayout(1, 1, 0, 0));
      jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, 
				  CONTINUOUS_LAYOUT, c1, c2);

      
      jSplitPane.setUI(new WorkbenchSplitPanelUI(cbg));

      jSplitPane.setBorder(new EmptyBorder(0, 0, 0, 0));
      jSplitPane.setDividerSize(3);
      jSplitPane.setDividerLocation(60);
      add(jSplitPane);
   }


   
   public void setBg(Color c) {
      cbg = c;
      jSplitPane.setBackground(c);
      jSplitPane.setUI(new WorkbenchSplitPanelUI(cbg));
      jSplitPane.setDividerSize(3);
   }


   public void setDividerSize(int n) {
      jSplitPane.setDividerSize(n);
   }


   public void setResizeWeight(double d) {
      jSplitPane.setResizeWeight(d);
   }



   public void applyLAF() {
      // should be done after adding components 
      //       cspui.setOwnDivider();
   }




   
}


