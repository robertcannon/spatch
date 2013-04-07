package org.spatch.assembly.edit;
  
import java.util.ArrayList;
 
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.viz.plot.Box;
import org.lemsml.jlems.viz.plot.BuildPaintInstructor;
import org.lemsml.jlems.viz.plot.BuildPaintMaster;
import org.lemsml.jlems.viz.plot.Builder;
import org.lemsml.jlems.viz.plot.Mouse;
import org.lemsml.jlems.viz.plot.Painter;
import org.lemsml.jlems.viz.plot.PickListener;
import org.lemsml.jlems.viz.plot.Pickable;
import org.lemsml.jlems.viz.plot.Position;
import org.lemsml.jlems.viz.plot.Repaintable;
import org.spatch.assembly.base.AssemblyComponent;
import org.spatch.assembly.base.AssemblyObserver;
import org.spatch.assembly.base.Cable;
import org.spatch.assembly.base.CablePlug;
import org.spatch.assembly.base.ComponentPlug;
import org.spatch.assembly.base.Plug;
import org.spatch.assembly.base.RegionHandle;
import org.spatch.assembly.base.Socket;
import org.spatch.assembly.base.SpatchAssembly;
import org.spatch.assembly.base.SpatchAssemblySpec;
import org.spatch.drawing.Shape;
import org.spatch.drawing.ShapePoint;
 


public class AssemblyCanvasMaster implements BuildPaintInstructor, PickListener, AssemblyObserver {

   SocketStore socketStore;

   ComponentPainter componentPainter;
   ShapePainter shapePainter;
   CablePainter cablePainter;


   Position downPosition;
   double downSize;

   AssemblyComponent dragCpt;
     

   AssemblyComponent focusCpt;
   Shape focusShape;
   
   Object shownItem;
   Object hoverItem;
   
   Plug focusPlug;

   AssemblyComponent newDragCpt;
   Shape newDragShape;
   Cable newDragCbl;
   Cable justFixedCbl;
   
   
   Repaintable rbCanvas;
   ContextMenu contextMenu;
   
   boolean keepDragged;
   boolean dragInTrash;
   boolean deleteMode;
   
   boolean tryWorkpiecePainting;
   BuildPaintMaster workpieceInstructor;

   SpatchAssembly assembly;

   ComponentEditor componentEditor;

   InfoReceiver infoReceiver;

   Position pressPosition;
   boolean nearPress;
   boolean doneDisconnect;


   String sizeUnit;
   // slightly odd - don't know this till we've painted
   // (have no ref access to own canvas). Shouldn't need range ever?
   double worldXRange = 1;

   public AssemblyCanvasMaster(Repaintable rb, ContextMenu cm) {
      componentPainter = new ComponentPainter();
      shapePainter = new ShapePainter();
      cablePainter = new CablePainter();
      
      socketStore = new SocketStore();
      rbCanvas = rb;
      contextMenu = cm;
      tryWorkpiecePainting = true;
      deleteMode = false;
   }



   public void setAssembly(SpatchAssembly ass) {
      if (assembly != null) {
         assembly.removeObserver(this);
          
      }
      assembly = ass;
      socketStore.setAssembly(ass);
      assembly.addObserver(this);

      sizeUnit = assembly.getSizeUnit();
     
      
      
      tryWorkpiecePainting = true;
   }


   public SpatchAssembly getAssembly() {
      return assembly;
   }

   public double getWorldXRange() {
      return worldXRange;
   }
   
   public void repaintCanvas() {
      rbCanvas.requestRepaint();
   }
   

   public void setComponentEditor(ComponentEditor ce) {
      componentEditor = ce;
   }



   // for buildpaint instructor
   public boolean antialias() {
      return true;
   }


   public void newComponentDragged(AssemblyComponent cpt) {
      keepDragged = false;

      if (cpt == focusCpt) {
         // new cpt already been fixed;

      } else if (newDragCpt == null) {
         newDragCpt = cpt;
      }
      if (cpt.hasValidPosition()) {
         checkConnect(cpt);
      }
      rbCanvas.requestRepaint();
   }


   public void newCableDragged(Cable cbl) {
      keepDragged = false;

      newDragCbl = cbl;
      if (cbl.hasValidPosition()) {
         checkConnect(cbl);
      }
      rbCanvas.requestRepaint();
   }



   public void assemblyChange() {
     //  E.info("ac master got assembly change");
      rbCanvas.requestRepaint();
   }


   public void newShapeDragged(Shape shp) {

      keepDragged = false;
      if (newDragShape == null) {
         newDragShape = shp;
      }

      rbCanvas.requestRepaint();
   }



   public void newComponentReleased() {
      if (keepDragged && newDragCpt != null) {
         fixNewDragComponent(newDragCpt);
         newDragCpt = null;
      }
   }


   public void newCableReleased() {
      if (keepDragged && newDragCbl != null && newDragCbl != justFixedCbl) {
         justFixedCbl = newDragCbl;
         fixNewDragCable(newDragCbl);
         newDragCbl = null;
         justFixedCbl = null;
      }
   }


   private void fixNewDragComponent(AssemblyComponent cpt) {
      if (assembly != null) {
         assembly.addComponent(cpt);
         socketStore.newComponent(cpt);
         editComponent(cpt);
         cpt.exportInfo(infoReceiver);
      }
   }


   private void fixNewDragCable(Cable cbl) {
      if (assembly != null) {
         justFixedCbl = cbl;
         assembly.addCable(cbl);
         
      }
   }


   public void newShapeReleased() {
      if (keepDragged && assembly != null) {
         assembly.addShape(newDragShape);
         editShape(newDragShape);
      }
      newDragShape = null;
   }


   private void editShape(Shape shape) {
      if (componentEditor != null) {
         componentEditor.editShape(shape);
      } else {
         E.error(" no cpt editor");
      }
      shownItem = shape;
   }


   private void showShape(Shape shape) {
      if (componentEditor != null) {
         componentEditor.showShape(shape);
      } else {
         E.error(" no cpt editor");
      }
      shownItem = shape;
   }


   private void editComponent(AssemblyComponent cpt) {
      if (componentEditor != null) {
         componentEditor.editComponent(cpt);
      } else {
        // TODO - maybe want this?
    	  // E.error(" no cpt editor");
      }
      shownItem = cpt;
   }


   private void showComponent(AssemblyComponent cpt) {
      if (componentEditor != null) {
         componentEditor.showComponent(cpt);
      } else {
    	  // TODO maybe
    	  // E.error(" no cpt editor");
      }
      shownItem = cpt;
   }



   public boolean reallyRepaint() {
      return true;
   }


   public void instruct(Painter p, Builder b) {
	  p.setAntialias(true);
	   
      worldXRange = p.getWorldCanvasWidth();
      
      if (assembly != null) {
         SpatchAssemblySpec ts = assembly.getAssemblySpec();
         if (ts.hasIconSpecs()) {
            for (IconSpec ispec : ts.getIconSpecs()) {
               instructFrame(p, ispec);
            }
         }

         if (ts.hasAssemblyLabels()) {
            for (AssemblyLabel al : ts.getAssemblyLabels()) {
               p.drawText(al.getText(), al.getX(), al.getY());
            }
         }

/*
         Object obj = assembly.getWorkpiece();
         if (obj != null) {
            instructWorkpiece(obj, p, b);
         }
*/
         
         instructShapes(p, b, assembly.getShapes());

         instructComponents(p, b, assembly.getComponents());
         
         instructCables(p, b, assembly.getCables());
      }



      if (newDragCpt != null) {
         componentPainter.instruct(p, newDragCpt, shapePainter);
      //   instructPlugPickables(p, b, newDragCpt.getPlugs());
         keepDragged = componentPainter.isOnCanvas(p, newDragCpt);

      } else if (newDragCbl != null && newDragCbl != justFixedCbl) {
         cablePainter.instruct(p, newDragCbl);
         keepDragged = cablePainter.isOnCanvas(p, newDragCbl);
         

      } else if (newDragShape != null) {
         shapePainter.instruct(p, newDragShape);
         keepDragged = shapePainter.isOnCanvas(p, newDragShape);

         /*
          * } else if (focusCpt != null) { keepDragged =
          * componentPainter.isOnCanvas(p, focusCpt);
          */
      }
      if (dragInTrash) {
         keepDragged = false;
      }
      
      
      socketStore.setPixelSize(p.getPixelSize());

      if (dragInTrash || deleteMode) {
         p.paintLiveTrash();
      } else {
         p.paintTrash();
      }
   }



   private void instructFrame(Painter p, IconSpec ispec) {
      p.setColorWhite();
      double x = ispec.getX();
      double y = ispec.getY();
      double scale = ispec.getScale();

      p.drawDashedRectangle(x, y, 0.7 * scale, 0.7 * scale);
      p.drawDashedRectangle(x, y, scale, scale);

   }


   private void instructComponents(Painter p, Builder b, ArrayList<? extends AssemblyComponent> cpts) {
       
      for (AssemblyComponent cpt : cpts) {
         componentPainter.instruct(p, cpt, shapePainter);

         if (cpt == focusCpt) {
        	 instructPlugPickables(p, b, cpt.getPlugs());
             instructHandles(p, b, cpt.getBoundaryHandles());
             
         } else {
            b.addPickableRegion(cpt.getBoundaryRegion());
         }
      }
      
      if (focusCpt != null) {
         // componentPainter.paintLabels(p, focusCpt);
      }
   }
   
   
   private void instructCables(Painter p, Builder b, ArrayList<Cable> cbls) {

      for (Cable cbl : cbls) {
         cablePainter.instruct(p, cbl);

         b.addPickablePoint(cbl.getHandlePoint());
         
         for (CablePlug cp : cbl.getPlugs()) {
            if (cp.isFree()) {
               b.addPickablePoint(cp.getPickablePoint());
            }
         }
       }
   }




   private void instructShapes(Painter p, Builder b, ArrayList<Shape> shapes) {

      for (Shape shp : shapes) {

         shapePainter.instruct(p, shp);

         if (shp == focusShape) {
            ShapePoint[] spa = shp.getPoints();
            for (int j = 0; j < spa.length; j++) {
               b.addPickablePoint(spa[j].getPickablePoint());
            }

            if (shp == hoverItem) {
               // MISSING;
            }
            
            if (shp.isExtensible()) {

               ShapePoint[] spp = shp.getProtoPoints();
               for (int j = 0; j < spp.length; j++) {
                  b.addPickablePoint(spp[j].getPickablePoint());
               }
            }

         } else {
            b.addPickableRegion(shp.getBoundaryRegion());
         }
      }
   }


   private void instructHandles(Painter p, Builder b, RegionHandle[] rhs) {
      if (rhs != null) {
         p.setColorBlue();
         for (RegionHandle rh : rhs) {
            b.addPickablePoint(rh.getPickablePoint());
            p.drawCenteredPixelLine(rh.getX(), rh.getY(), rh.getXPts(), rh.getYPts());
         }
      }
   }


   private void instructPlugPickables(Painter p, Builder b, ArrayList<ComponentPlug> plugs) {
      if (plugs != null) {
         for (ComponentPlug plug : plugs) {
 
        	 
               b.addPickablePoint(((ComponentPlug)plug).getPickablePoint());
             

         }
      }

   }



   public void instructWorkpiece(Object obj, Painter p, Builder b) {
     if (workpieceInstructor == null) {
    	 E.error("Null workpiece instructor ");
    	 
     } else {
         socketStore.checkUpdateWorkpiece(obj);

         workpieceInstructor.setSubject(obj);
         workpieceInstructor.instruct(p, b);
      }
   }



   private void setFocusCpt(AssemblyComponent cpt) {
      focusCpt = cpt;
   }
   
   private void clearFocusCpt() {
      focusCpt = null;   
      if (focusShape == null) {
         UserFocus.get().clear();
      }
   }
   
   private void setFocusShape(Shape shp) {
      focusShape = shp;
   }
   
   private void clearFocusShape() {
      focusShape = null;   
      if (focusCpt == null) {
         UserFocus.get().clear();
      }
   }
   
   
   
   
   // REFAC into handlers!!!!!

   public void backgroundPressed(int button, int x, int y) {
      clearFocusCpt();
      dragCpt = null;
      clearFocusShape();
      if (deleteMode) {
         setNormalMode();
      }
   }

   
   public void trashPressed() {
      if (deleteMode) {
         setNormalMode();
      } else {
         deleteMode = true;
         rbCanvas.setCursor("cross");
         rbCanvas.requestRepaint();
      }
   }
   
   private void setNormalMode() {
      deleteMode = false;
      rbCanvas.setCursor("default");
      rbCanvas.requestRepaint();
   }

   
   public void pickHovered(Pickable pbl) {
	//   E.info("pick hovered " + focusCpt);
      if (focusCpt != null) {
         hoverItem = focusCpt;
      } else if (focusShape != null) {
         hoverItem = focusShape;
      }
      rbCanvas.requestRepaint();
   }
   
   
   public void pickPressed(Pickable pbl, int button, int x, int y) {
      dragInTrash = false;
      keepDragged = true;
      nearPress = true;
      doneDisconnect = false;
      
    
      if (focusCpt != null) {
         downPosition = new Position(focusCpt.getAbsolutePosition());
         downSize = focusCpt.getSize();
      }

      Object obj = pbl.getRef();
      
      
      if (obj instanceof AssemblyComponent) {
         clearFocusShape();

         if (button == Mouse.LEFT) {
            AssemblyComponent cpt = (AssemblyComponent)obj;
            if (cpt == dragCpt) {
               setFocusCpt(cpt);
               downPosition = new Position(focusCpt.getAbsolutePosition());
               downSize = focusCpt.getSize();
         //      showComponent(focusCpt);
           //    focusCpt.exportInfo(infoReceiver);

            } else {
               dragCpt = cpt;
               clearFocusCpt();
               showComponent(dragCpt);
               dragCpt.exportInfo(infoReceiver);
            }


         } else if (button == Mouse.RIGHT) {
            if (obj instanceof RefComponent) {
               RefComponent rc = (RefComponent)obj;
               String[] sa = rc.getOptions();
               contextMenu.setContext(sa, new RefSelectionActor(rc, this));
               contextMenu.showAt(rc.getAbsolutePosition());
               
          
            } else {
               AssemblyComponent cpt = (AssemblyComponent)obj;
               if (cpt instanceof TableComponent) {
                  TableComponent tc = (TableComponent)obj;
                  if (tc.isMenuSettable()) {
                     String[] sa = tc.getMenuOptions();
                     contextMenu.setContext(sa, new SubrefSelectionActor(tc, this));
                     contextMenu.showAt(tc.getAbsolutePosition());
                  }
               } else {
                  cpt.cycleDisplay();
               }
            }
         }

      } else if (obj instanceof RegionHandle) {

      } else if (obj instanceof Plug) {

      } else if (obj instanceof Shape) {
         setFocusShape((Shape)obj);
         focusShape.select();
         showShape(focusShape);
         clearFocusCpt();
         dragCpt = null;
           
         focusShape.regionPressed();

      } else if (obj instanceof ShapePoint) {
         ShapePoint sp = (ShapePoint)obj;

         
         
         if (sp.isType("proto")) {
            if (deleteMode) {
               
            } else {
               sp.getParent().addPoint(sp.getIndex(), sp);
            }
            
         } else {
            if (deleteMode) {   
               boolean bdone = sp.getParent().deletePoint(sp);
               if (!bdone) {
                  
                  assembly.deleteShape(sp.getParent());
                  
               }
            
            } else {
               sp.getParent().pointPressed(sp);
            }
         }

         
      } else if (obj instanceof Cable) {
         downPosition = new Position(((Cable)obj).getAbsolutePosition());
         clearFocusCpt();
         dragCpt = null;
         clearFocusShape();
         
      } else {
         E.error("unknown pick pressed");
      }

   }


   public void pickDragged(Pickable pbl, Position pos, int button, int x, int y) {
      if (deleteMode) {
         return;
      }
      
      if (pressPosition == null) {
         pressPosition = pos.copy();
         nearPress = true;
      } else if (nearPress && pos.distanceFrom(pressPosition) > 0.2) { // ADHOC
         nearPress = false;
      }

      Object obj = pbl.getRef();

      if (obj instanceof RegionHandle) {
         RegionHandle rh = (RegionHandle)obj;
         // REFAC - more objects;
         rh.manipPositionSize(downPosition, downSize, pos);
         focusCpt.setPosition(rh.getManipPosition());
         focusCpt.setSize(rh.getManipSize());


      } else if (obj instanceof AssemblyComponent) {
         AssemblyComponent cpt = (AssemblyComponent)obj;
         cpt.setPosition(pos);

         if (cpt.hasPlugs()) {
            if (button == Mouse.RIGHT && nearPress) {
               if (doneDisconnect) {

               } else {
                  cpt.disconnect();
                  doneDisconnect = true;
               }
            } else {
               checkConnect(cpt);
            }
         }

      } else if (obj instanceof Cable) {
         Cable cbl = (Cable)obj;
         cbl.setPosition(pos);
         if (button == Mouse.RIGHT && nearPress) {
            if (doneDisconnect) {

            } else {
               cbl.disconnect();
               doneDisconnect = true;
            }
         } else {
            checkConnect(cbl);
         }
         
         
         

      } else if (obj instanceof Plug) {
         Plug plug = (Plug)obj;
         plug.setXYLocation(pos);
         plug.syncLocation();

         checkConnect(plug);

      } else if (obj instanceof Shape) {
         Shape shape = (Shape)obj;
         if (button == Mouse.LEFT) {
            shape.setPosition(pos);

         } else if (button == Mouse.RIGHT) {
            shape.rotate(pos);
         }


      } else if (obj instanceof ShapePoint) {
         ShapePoint sp = (ShapePoint)obj;
         Shape shape = sp.getParent();

         if (button == Mouse.LEFT) {
            shape.movePoint(sp, pos, Shape.MOVE_POINT);

         } else if (button == Mouse.RIGHT) {
            shape.movePoint(sp, pos, Shape.ROTATE_SHAPE);
         }
      }
   }



   public void pickReleased(Pickable pbl, int button) {
      if (deleteMode) {
         return;
      }
      pressPosition = null;

      if (!keepDragged) {
         // E.warning("maybe should kill dragee??");
         // pickTrashed(pbl);
         
         
      } else if (dragCpt != null) {
         socketStore.componentMoved(dragCpt);
      }
      keepDragged = true;
      dragInTrash = false;
      newDragCbl = null;
   }


   public void pickEnteredTrash(Pickable pbl) {
      dragInTrash = true;
   }


   public void pickLeftTrash(Pickable pbl) {
      dragInTrash = false;
   }


   public void pickTrashed(Pickable pbl) {
      Object obj = pbl.getRef();
      if (obj instanceof Shape) {
         assembly.deleteShape((Shape)obj);
      
      } else if (obj instanceof AssemblyComponent) {
         assembly.deleteComponent((AssemblyComponent)obj);
         
      } else if (obj instanceof Cable) {
         assembly.deleteCable((Cable)obj);
         
      } else {
         E.warning("not deleting (unrecognized type)  " + obj);
      } 
      
      if (obj == shownItem) {
         showComponent(null);
      }

      clearFocusCpt();
      dragCpt = null;
      clearFocusShape();
   }

 
   public void checkConnect(Cable cbl) {
      if (assembly == null) {
         return;
      }
      Plug[] plugs = cbl.getPlugs();

      for (int i = 0; i < plugs.length; i++) {
         if (checkConnect(cbl, plugs[i])) {
            cbl.flavorize();            
            break;

         }
      }
   }



   public boolean checkConnect(Cable cbl, Plug plug) {
      boolean ret = false;
      if (plug.isFree()) {
         Socket skt = socketStore.getInRange(plug);
         if (skt != null) {
            if (cbl == newDragCbl && cbl != justFixedCbl) {
               // E.info("fixing new drag cpt " + plug.getXYLocation());
               fixNewDragCable(cbl);
            
            }
            if (plug.directionMatches(skt)) {
           
            	if (cbl.isVirgin()) {
            		plug.connectTo(skt);
            		ret = true;
               
            	} else if (cbl.flavorMatches(skt)) {
            		plug.connectTo(skt);
            		ret = true;
            	}
            }
         }
      }
      return ret;
   }



   public void checkConnect(AssemblyComponent cpt) {
      if (assembly == null) {
         return;
      }
      if (cpt.hasPlugs()) {
         for (Plug plug : cpt.getPlugs()) {
            plug.syncLocation();
         }
         
         for (Plug plug : cpt.getPlugs()) {
            if (checkConnect(cpt, plug)) {
               break;
            }
         }
      }
   }



   public boolean checkConnect(AssemblyComponent cpt, Plug plug) {
      boolean ret = false;
      if (plug.isFree()) {
         Socket skt = socketStore.getInRange(plug);
         if (skt != null) {
            if (cpt == newDragCpt) {
               // E.info("fixing new drag cpt " + plug.getXYLocation());
               fixNewDragComponent(cpt);
               newDragCpt = null;
            }
            plug.connectTo(skt);
            ret = true;
         }
      }
      return ret;
   }


   public boolean checkConnect(Plug plug) {
      boolean ret = false;
      if (plug.isFree()) {
         Socket skt = socketStore.getInRange(plug);
         if (skt != null) {
            plug.connectTo(skt);
            ret = true;
         }
      }
      return ret;
   }



   public void setInfoReceiver(InfoReceiver ir) {
      infoReceiver = ir;
   }


 

 

@Override
public Box getLimitBox(Painter p) {
	// TODO Auto-generated method stub
	return null;
}

 
}
