package org.spatch.drawing;


import java.awt.Color;
 

public class VectorIcon extends  FixedDrawing {

   public String ref;

   BasicTouchTime touchTime;


   public VectorIcon() {
      super();
      touchTime = new BasicTouchTime();
   }


   public VectorIcon copy() {
      VectorIcon ret = new VectorIcon();
      for (FixedDrawingComponent fdc : items) {
         ret.add(fdc.copy());
      }
      return ret;
   }


   public BasicTouchTime getTouchTime() {
      return touchTime;
   }

   public void baseInit() {
      add(new FDSquare(0., 0., 0.6, 0.6));
   }


   public boolean hasRef() {
      return (ref != null);
   }

   public String getRef() {
         return ref;
   }


   public static VectorIcon errorIcon() {
      VectorIcon vir = new VectorIcon();
      vir.add(FDSquare.defaultSquare());
      return vir;
     }


   public void addSubIcon(VectorIcon icon, double x, double y, double scl) {
       add(new IconWrapper(icon, x, y, scl));
   }


   public static VectorIcon makeErrorIcon() {
      VectorIcon vir = new VectorIcon();
      FDDisc d = new FDDisc();
      d.setSize(0.8, 0.8);
      d.setFillColor(Color.red);
      vir.add(d);

      FDRectangle r = new FDRectangle(0.0, 0.3, 0.1, 0.5);
      r.setFillColor(Color.white);
      vir.add(r);

      FDRectangle rd = new FDRectangle(0.0, -0.5, 0.1, 0.1);
      rd.setFillColor(Color.white);
      vir.add(rd);
      return vir;
   }


   public static VectorIcon makeRefIcon() {
      VectorIcon vir = new VectorIcon();
    
      FDRectangle r = new FDRectangle(0.0, 0.0, 0.8, 0.8);
      r.setClosed();
      r.setLineColor(Color.black);
      r.setLineWidth(1.);
      vir.add(r);
      return vir;

   }
   
   
   
   public static VectorIcon makePendingIcon() {
      VectorIcon vir = new VectorIcon();
      FDDisc d = new FDDisc();
      d.setSize(0.8, 0.8);
      d.setFillColor(Color.gray);
      vir.add(d);

      return vir;
   }



   

}
