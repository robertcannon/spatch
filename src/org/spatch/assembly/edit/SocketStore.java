package org.spatch.assembly.edit;



import java.util.ArrayList;
import java.util.HashMap;
 
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.viz.plot.Size;
import org.spatch.assembly.base.AssemblyComponent;
import org.spatch.assembly.base.AttachmentPoint;
import org.spatch.assembly.base.ConnectionFlavor;
import org.spatch.assembly.base.Plug;
import org.spatch.assembly.base.PointAttachable;
import org.spatch.assembly.base.Socket;
import org.spatch.assembly.base.SpatchAssembly;
import org.spatch.assembly.base.WorkpieceSocket;
 

public class SocketStore {

   Size pixelSize;

   SpatchAssembly assembly;

   HashMap<ConnectionFlavor, SocketSet> flavorSets;


   Object workpiece;



   public SocketStore() {
      flavorSets = new HashMap<ConnectionFlavor, SocketSet>();
   }


   public void setPixelSize(Size sz) {
      pixelSize = sz;
   }


   public void setAssembly(SpatchAssembly ass) {
      assembly = ass;
      rebuild();
   }


   public void newComponent(AssemblyComponent cpt) {
      rebuild(); // EFF
   }


   public void componentMoved(AssemblyComponent cpt) {
      rebuild(); // EFF
   }


   public Socket getInRange(Plug p) {
      ConnectionFlavor flav = p.getFlavor();
      Socket ret = null;
       
      if (flav == null) {
        for (SocketSet ss : flavorSets.values()) {
           Socket st = ss.getInRange(p, pixelSize);
           if (st != null) {
              ret = st;
              break;
           }
        }
         
      } else {
         SocketSet sset = getSocketSet(flav);
         ret = sset.getInRange(p, pixelSize);
      }
      return ret;
   }



   public SocketSet getSocketSet(ConnectionFlavor flav) {
      SocketSet sset = null;
      
      if (flavorSets.containsKey(flav)) {
         sset = flavorSets.get(flav);

      } else {
         sset = new SocketSet(flav);
         flavorSets.put(flav, sset);
      }
      return sset;
   }



   public void clear() {
      for (SocketSet ss : flavorSets.values()) {
         ss.clear();
      }
   }


   public void rebuild() {
      clear();
      if (assembly != null) {
         for (AssemblyComponent cpt : assembly.getComponents()) {
            addComponent(cpt);
         }
      }

      if (workpiece != null) {
         addWorkpiece();
      }
   }


   public void addComponent(AssemblyComponent cpt) {
      ArrayList<Socket> skts = cpt.getSockets();
      if (skts != null) {
         for (Socket socket : skts) {
            storeSocket(socket);
         }
      }
   }


   private void storeSocket(Socket skt) {
      SocketSet sset = getSocketSet(skt.getFlavor());
      sset.add(skt);
   }


   public void checkUpdateWorkpiece(Object obj) {
      if (workpiece == null || workpiece != obj) {
         workpiece = obj;
         rebuild();
      }
   }


   private void addWorkpiece() {
      if (workpiece == null) {

      } else if (false) { // workpiece instanceof Socketed) {
         E.debugError("need code for socketed workpieces");

      } else if (workpiece instanceof PointAttachable) {

         ArrayList<AttachmentPoint> apa = ((PointAttachable)workpiece).getAttachmentPoints();

         for (AttachmentPoint ap : apa) {
            WorkpieceSocket wksk = new WorkpieceSocket(ap);

            storeSocket(wksk);
         }

      } else {
         E.error("workpiece doesnt implement any socket interfaces " + workpiece);
      }
   }


}
