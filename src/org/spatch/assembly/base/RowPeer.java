package org.spatch.assembly.base;


public interface RowPeer {


   void initFromRow(Row table);

   void attachRow(Row table);

   Row getRow();
 
   void notifyObservers();


}
