package org.spatch.assembly.base;

import org.lemsml.jlems.viz.plot.AbsLocated;
import org.lemsml.jlems.viz.plot.IntPosition;
import org.lemsml.jlems.viz.plot.Position;
import org.lemsml.jlems.viz.plot.XYLocated;
import org.lemsml.jlems.viz.plot.XYLocation;
 


public class SubLocated implements AbsLocated, XYLocated {


   private SubLocated parent;

   private Position pRel; // rel is definitive - abs may get out of date
                           // (needNew flag set)
   private Position pAbs;
   private double size;

   private SubLocated parentPort;

   private AbsLocated lock;

   private boolean needNewAbs;

   private boolean intIsDefinitive = false;
   private IntPosition p_intPosition;
 

   public SubLocated() {
      this(null);
   }


   public SubLocated(SubLocated prnt) {
      parent = prnt;

      pRel = new Position();
      pAbs = new Position();

      needNewAbs = true;

      size = 1.0;
   }



   public XYLocation getXYLocation() {
      return getAbsolutePosition();
   }

   public XYLocation getRelativeLocation() {
      return getRelativePosition();
   }

   public void silentSetSize(double d) {
      size = d;
   }

   public void setSize(double d) {
      if (d == size) {
         return;
      }

      double oscl = size;
      size = d;
      double ds = (oscl - size) / (oscl + size);
      if (Math.abs(ds) > 0.01) {
         notifyDependentsOfMove();
      }
   }


   public void notifyDependentsOfMove() {

   }



   public double getSize() {
      return size;
   }


   public void setLock(AbsLocated sl) {
      lock = sl;
      needNewAbs = true;
   }


   public void releaseLock() {
      updateAbsolute();
      lock = null;
   }
 


   public void setRelativeAttachment(Position p) {
      parentPort = new SubLocated(parent);
      parentPort.setRelativePosition(p);
     
   }



   public void parentMoved() {
      needNewAbs = true;
      if (parentPort != null) {
         parentPort.parentMoved();
      }
   }


   public SubLocated getParent() {
      return parent;
   }



   public void setRelativePosition(Position p) {
      intIsDefinitive = false;
      pRel.set(p);
      updateAbsolute();
   }


   public void setAbsolutePosition(Position p) {
      intIsDefinitive = false;
      pAbs.set(p);
      updateRelative();
   }


   public boolean hasValidPosition() {
      return pAbs.isValid();
   }


   public void setXYLocation(XYLocation xyl) {
      intIsDefinitive = false;
      pAbs.set(xyl);
      updateRelative();
   }


   public void updateRelative() {
      if (parent == null) {
         pRel.set(pAbs);
      } else {
         pRel.relativize(parent.getAbsolutePosition(), parent.getSize(), pAbs);
      }
   }


   public void updateAbsolute() {
      if (lock != null) {
         pAbs.set(lock.getAbsolutePosition());
         updateRelative();
         needNewAbs = false;

      } else if (parent != null) {
         Position ppa = parent.getAbsolutePosition();
         if (ppa.isValid()) {
            pAbs.absolutize(ppa, parent.getSize(), pRel);
            needNewAbs = false;
         }

      } else if (pRel.isValid()) {
         pAbs.set(pRel);
         needNewAbs = false;
      }
   }


   public Position getRelativePosition() {
      return pRel;
   }


   public XYLocation getRelativeXYLocation() {
      return pRel;
   }


   public Position getAbsolutePosition() {
      if (lock != null) {
         return lock.getAbsolutePosition();
      }

      if (needNewAbs) {
         updateAbsolute();
      }
      return pAbs;
   }


   public Position getAbsoluteAttachmentPosition() {
      return parentPort.getAbsolutePosition();
   }


   public void setIntPosition(IntPosition intp) {
      if (p_intPosition == null) {
         p_intPosition = new IntPosition();
      }
      p_intPosition.set(intp);
      intIsDefinitive = true;
   }


   public boolean hasIntPosition() {
      return intIsDefinitive;
   }


   public IntPosition getIntPosition() {
      return p_intPosition;
   }


}
