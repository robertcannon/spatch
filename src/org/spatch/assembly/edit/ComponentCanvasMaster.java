package org.spatch.assembly.edit;
 

import java.util.ArrayList;
 
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.viz.plot.Box;
import org.lemsml.jlems.viz.plot.BuildPaintInstructor;
import org.lemsml.jlems.viz.plot.Builder;
import org.lemsml.jlems.viz.plot.IntPosition;
import org.lemsml.jlems.viz.plot.Mouse;
import org.lemsml.jlems.viz.plot.Painter;
import org.lemsml.jlems.viz.plot.PickListener;
import org.lemsml.jlems.viz.plot.Pickable;
import org.lemsml.jlems.viz.plot.Position;
import org.lemsml.jlems.viz.plot.Repaintable;
import org.spatch.WrapError;
import org.spatch.assembly.base.AssemblyComponent;
import org.spatch.assembly.base.Cable;
import org.spatch.assembly.base.SpatchAssemblySpec;
import org.spatch.drawing.Shape;


public class ComponentCanvasMaster implements BuildPaintInstructor, PickListener {

    
   ArrayList<Cable> sourceCables;
   ArrayList<AssemblyComponent> sourceComponents;
   ArrayList<Shape> sourceShapes;
   
   CablePainter cablePainter;
   ComponentPainter componentPainter;
   ShapePainter shapePainter;

   Cable draggedCable;
   Cable draggedCablePeer;

   
   AssemblyComponent draggedComponent;
   AssemblyComponent draggedComponentPeer;

   Shape draggedShape;
   Shape draggedShapePeer;

   AssemblyCanvasMaster acMaster;
   InfoReceiver infoReceiver;

   
   Repaintable rbCanvas;
   
   
   double xmax;
   boolean folded;
   double worldXRange = 1;
   
   double newComponentScaleFactor = 1.0;


   public ComponentCanvasMaster(Repaintable rble) {
      rbCanvas = rble;
      Cable c1 = new Cable();
      c1.setPosition(new Position(2, -1.8));
      sourceCables = new ArrayList<Cable>();
      sourceCables.add(c1);
      
      cablePainter = new CablePainter();
      componentPainter = new ComponentPainter();
      shapePainter = new ShapePainter();

      sourceComponents = new ArrayList<AssemblyComponent>();
      sourceShapes = new ArrayList<Shape>();
   }

   
   
   public void setInfoReceiver(InfoReceiver ir) {
      infoReceiver = ir;
   }
   
   public void setNewComponentScaleFactor(double d) {
      newComponentScaleFactor = d;
   }


   public void setComponentSpecs(ArrayList<SpatchAssemblySpec> cptSpecs) throws WrapError {
      sourceComponents.clear();
      int icp = 0;
      for (SpatchAssemblySpec cspec : cptSpecs) {
         AssemblyComponent cpt = cspec.newDummyComponent();
         cpt.setID("");
         cpt.setDisplayState(-1);
         sourceComponents.add(cpt);

         Position cpos = new Position(2. + 3. * icp, -1.8);
         cpt.setPosition(cpos);
         cpt.setSize(1.0);
         cpt.initPickable();
         icp += 1;
      }
      xmax = 1000.;
   }


   public void setShapes(ArrayList<Shape> arl) {
      sourceShapes.clear();
      int ncpt = sourceCables.size() + sourceComponents.size();

      int ishp = 0;
      for (Shape shp : arl) {
         Position cpos = new Position(2. + 3. * (ncpt + ishp), -1.8);
         sourceShapes.add(shp);
         shp.select();
         shp.setPosition(cpos);
         shp.initPickable();
         ishp += 1;
      }
      xmax = 1000.;
   }


   private void foldSources(double xm) {
      xmax = xm;
      int row = 0;
      int col = sourceCables.size();
      folded = false;
      for (AssemblyComponent cpt : sourceComponents) {
         if (3 * col + 4 > xmax) {
            row += 1;
            col = 0;
            folded = true;
         }
         Position cpos = new Position(2 + 3 * col, -1.8 - 3 * row);
         cpt.setPosition(cpos);
         col += 1;
      }

      for (Shape shp : sourceShapes) {
         if (3 * col + 4 > xmax) {
            row += 1;
            col = 0;
            folded = true;
         }
         Position cpos = new Position(2 + 3 * col, -1.8 - 3 * row);
         shp.setPosition(cpos);
         col += 1;
      }
   }



   public void setAssemblyCanvasMaster(AssemblyCanvasMaster acm) {
      acMaster = acm;
   }


   // for buildpaint instructor
   public boolean antialias() {
      return true;
   }


   /*
    * public boolean reallyRepaint() { return (draggedComponent == null ||
    * componentPainter.isOnCanvas(p, draggedComponent)); }
    */

   
   

   public void instruct(Painter p, Builder b) {
      p.setPixelScalingFromTop(0.05); // one off call?
      p.setAntialias(true);
      
      double xm = p.getWorldCanvasWidth();
      worldXRange = xm;
      
      // E.info("considering fold " + xmax + " " + xm);
      if ((folded && xm > xmax + 3) || (xm < xmax - 3)) {
         foldSources(xm);
      }
      
      if (sourceCables != null) {
         instructCables(p, b);
      }
      
      if (sourceComponents != null) {
         instructComponents(p, b);
      }
      if (sourceShapes != null) {
         instructShapes(p, b);
      }
   }

   private void instructCables(Painter p, Builder b) {
      for (Cable cbl : sourceCables) {
         cablePainter.instruct(p, cbl);
         b.addPickablePoint(cbl.getHandlePoint());
      }

      if (draggedCable != null) {
         cablePainter.instruct(p, draggedCable);
         IntPosition ipos = p.pow(draggedCable.getAbsolutePosition());
         ipos.shift(0, -1 * p.getCanvasHeight() - 5);
         draggedCablePeer.setIntPosition(ipos);

         if (acMaster != null) {
            acMaster.newCableDragged(draggedCablePeer);
         }
      }
   }


   private void instructComponents(Painter p, Builder b) {
      for (AssemblyComponent cpt : sourceComponents) {
         componentPainter.instruct(p, cpt, null);
         b.addPickableRegion(cpt.getBoundaryRegion());
      }
    
      if (draggedComponent != null) { 	  
    	  componentPainter.instruct(p, draggedComponent, null);
       
         // integer drag location relative to bottom left is fetched from
         // painter and cached here for draging onto an adjacent canvas -
         // mucky... generalize?? ADHOC
         IntPosition ipos = p.pow(draggedComponent.getAbsolutePosition());
         ipos.shift(0, -1 * p.getCanvasHeight() - 5);

         draggedComponentPeer.setIntPosition(ipos);

         if (acMaster != null) {
            acMaster.newComponentDragged(draggedComponentPeer);
         }
      }
   }



   private void instructShapes(Painter p, Builder b) {
      for (Shape shp : sourceShapes) {
         shapePainter.instruct(p, shp);
         b.addPickableRegion(shp.getBoundaryRegion());
      }

      if (draggedShape != null) {
         shapePainter.instruct(p, draggedShape);
     
         IntPosition ipos = p.pow(draggedShape.getPosition());
         ipos.shift(0, -1 * p.getCanvasHeight() - 5);

         draggedShapePeer.setIntPosition(ipos);


         if (acMaster != null) {
            acMaster.newShapeDragged(draggedShapePeer);
         }
      }
   }


   public void backgroundPressed(int i, int ix, int iy) {
      
   }

  
   public void pickHovered(Pickable pbl) {
      rbCanvas.requestRepaint(); // ADHOC - only reason for holding a ref to the canvas;
   }
   
   
   public void pickPressed(Pickable pbl, int button, int ix, int iy) {

	   System.out.println("CC pp " + pbl);
	   
	   
	   draggedComponent = null;
      draggedShape = null;
      draggedCable = null;
      
       if (button == Mouse.LEFT) {

         Object obj = pbl.getRef();
         if (obj instanceof TableComponent) {
            TableComponent refComponent = (TableComponent)obj;
            draggedComponent = refComponent.makeDummyCopy();
 
            draggedComponentPeer = refComponent.makeScaledCopy(newComponentScaleFactor);



         } else if (obj instanceof Shape) {
            Shape refShape = (Shape)obj;
            draggedShape = refShape.makeCopy();
            draggedShape.select();
            draggedShapePeer = refShape.makeCopy();
            if (acMaster != null) {
               double fscale = acMaster.getWorldXRange() / getWorldXRange();
               draggedShapePeer.rescale(fscale);
            }
            
            draggedShapePeer.select();

            
         } else if (obj instanceof Cable) {
             Cable refCable = (Cable)obj;
             draggedCable = refCable.makeCopy();
             draggedCablePeer = refCable.makeScaledCopy(newComponentScaleFactor);
             
         } else {
            E.missing(" cant drag " + obj);
         }


      } else if (button == Mouse.RIGHT) {
         Object obj = pbl.getRef();
         if (obj instanceof AssemblyComponent) {
            AssemblyComponent cpt = (AssemblyComponent)obj;

            cpt.exportInfo(infoReceiver);

         }
      }

   }


// REFAC - shorter?
   public void pickDragged(Pickable pbl, Position dragPos, int button, int ix, int iy) {
 	   
      if (draggedComponent != null) {
         draggedComponent.setPosition(dragPos);

      } else if (draggedShape != null) {
         draggedShape.setPosition(dragPos);
      
      } else if (draggedCable != null) {
         draggedCable.setPosition(dragPos);
           
      } else {
         E.error("unused pick drag? ");
      }

   }


   public void pickReleased(Pickable pbl, int button) {
      if (acMaster != null) {
         if (draggedComponent != null) {
            acMaster.newComponentReleased();

         } else if (draggedShape != null) {
            acMaster.newShapeReleased();

         } else if (draggedCable != null) {
            acMaster.newCableReleased();
         }
      }
      draggedComponent = null;
      draggedShape = null;
      draggedCable = null;
   }


   public void pickEnteredTrash(Pickable pbl) {
   }


   public void pickLeftTrash(Pickable pbl) {
   }


   public void pickTrashed(Pickable pbl) {
   }


   public void trashPressed() {
      // TODO Auto-generated method stub
      
   }

   public double getWorldXRange() {
      return worldXRange;
   }



@Override
public Box getLimitBox(Painter p) {
	E.missing();
	// TODO Auto-generated method stub
	return null;
}


}
