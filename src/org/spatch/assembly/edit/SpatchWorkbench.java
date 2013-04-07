package org.spatch.assembly.edit;
 


import java.awt.BorderLayout;
import java.awt.Color;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.viz.plot.BasePanel;
import org.lemsml.jlems.viz.plot.ColorUtil;
import org.lemsml.jlems.viz.plot.PickWorldCanvas;
import org.spatch.WrapError;
import org.spatch.assembly.base.SpatchAssembly;
import org.spatch.assembly.base.SpatchAssemblySpec;
 

public class SpatchWorkbench extends BasePanel implements Workbench {

   static final long serialVersionUID = 1001;


   PickWorldCanvas componentCanvas;
   PickWorldCanvas assemblyCanvas;


   ComponentCanvasMaster ccMaster;
   AssemblyCanvasMaster acMaster;

   WorkbenchSplitPanel absp;

   SpatchAssemblySpec domain;

   InfoReceiver infoReceiver;


   public SpatchWorkbench(int w, int h) {
      super();


      componentCanvas = new PickWorldCanvas(true, w, 60);
      componentCanvas.setBg("#e8e8ea");
      componentCanvas.setNoGrid();
  

      assemblyCanvas = new PickWorldCanvas(true, w, h - 60);
      assemblyCanvas.setFixedAspectRatio(1.0);
      assemblyCanvas.setYRange(-0.2, 1.2);
      assemblyCanvas.setXRange(-0.2, 1.2);
      assemblyCanvas.setBg("#ededed");

      assemblyCanvas.setOnGridAxes();
      // TODO
      //      assemblyCanvas.setShowGrid(false);


      absp = new WorkbenchSplitPanel(componentCanvas, assemblyCanvas);
      absp.setResizeWeight(0.0);
      absp.setBg(new Color(0xc0, 0xc0, 0xc0));
      absp.setDividerSize(3);

      setLayout(new BorderLayout());
      add("Center", absp);


      ccMaster = new ComponentCanvasMaster(componentCanvas);
      componentCanvas.attach(ccMaster);

      ContextMenu contextMenu = new GraphContextMenu(assemblyCanvas);

      acMaster = new AssemblyCanvasMaster(assemblyCanvas, contextMenu);
      assemblyCanvas.attach(acMaster);
      ccMaster.setAssemblyCanvasMaster(acMaster);
   }



   public void setPreferredSize(int w, int h) {
	   // assemblyCanvas.setPreferredSize(w, h-60);
       componentCanvas.setPreferredHeight(60);
   }

   

   public void setModeController(ModeController mc) {
    //  mc.addModeSettable(assemblyCanvas);
    //  mc.addModeSettable(componentCanvas);
   }




   public SpatchAssemblySpec getDomain() {
      return domain;
   }


   public void setAssembly(Object obj) throws WrapError {
	   SpatchAssembly tdma = (SpatchAssembly)obj;
      setAssembly(tdma);
   }

   public void setAssembly(SpatchAssembly ass) throws WrapError {
      if (ass != null) {

         assemblyCanvas.setXAxisLabel(ass.getSizeUnit());

         domain = ass.getAssemblySpec();
         ccMaster.setComponentSpecs(domain.getSetMemberSpecs());
         ccMaster.setShapes(domain.getShapeExemplars());


         double[] xr = domain.getInitialXRange();
         double[] yr = domain.getInitialYRange();

         if (xr != null & yr != null) {
            assemblyCanvas.ensureCovers(xr, yr);
         }

       }
      acMaster.setAssembly(ass);

      // cc has 2n components of unit size, n ~ 6, hence 0.15 (ac is unit scale)
      ccMaster.setNewComponentScaleFactor(ass.getTypicalSize() * 0.15);

      componentCanvas.repaint();
      assemblyCanvas.repaint();
   }



   public void setComponentEditor(ComponentEditor ce) {
      acMaster.setComponentEditor(ce);
   }



   public void setInfoReceiver(InfoReceiver ir) {
      infoReceiver = ir;
      ccMaster.setInfoReceiver(infoReceiver);
      acMaster.setInfoReceiver(infoReceiver);

   }


   public InfoReceiver getInfoReceiver() {
      return infoReceiver;
   }


   public void showInfo(String title, String txt) {
      if (infoReceiver != null) {
         infoReceiver.receiveInfo(title, txt);
      } else {
         E.warning("no info receiver in workbench?");
      }
   }

   /*
   public void setBg(Color c) {
      absp.setBg(c);
      super.setBg(c);
   }
*/

   public void setCanvasColor(Color color) {
      assemblyCanvas.setBg(color);

   }


   public void setShelfColor(Color color) {
      componentCanvas.setBg(color);
      absp.setBg(ColorUtil.verySlightlyBrighter(color));
   }


   // ADHOC
   public void setSunkenBorder(Color c) {
    // setBorder(BorderUtil.makeEtchedBorder(c));

   }

}
