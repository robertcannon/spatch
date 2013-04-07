package org.spatch.assembly.edit;

import java.awt.Color;

import org.spatch.WrapError;
 

public interface Workbench {

   void setPreferredSize(int prefw, int prefh);

   void setBg(Color bgc);

   void setSunkenBorder(Color bgc);

 //  void setModeController(ModeController modeController);

 //  void setInfoReceiver(InfoReceiver infoReceiver);

   void setCanvasColor(Color canvasColor);

   void setShelfColor(Color shelfColor);

   void setAssembly(Object ass) throws WrapError;

}
