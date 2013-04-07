package org.spatch.dev;
 
import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.sim.Sim;
import org.lemsml.jlems.io.logging.DefaultLogger;
import org.lemsml.jlems.io.out.FileResultWriterFactory;
import org.lemsml.jlems.io.util.JUtil;
import org.lemsml.jlems.viz.datadisplay.SwingDataViewerFactory;
import org.spatch.models.ModelRoot;

public class Reader {

	

	public static void main(String[] argv) {
		try {
		String srcText = JUtil.getRelativeResource(ModelRoot.class, "spatch.xml");
		 
		
		SwingDataViewerFactory.initialize();
		DefaultLogger.initialize();
		FileResultWriterFactory.initialize();
		 
			Sim sim = new Sim(srcText);
	 
			sim.readModel();	
 			
		
		    sim.build();
	        sim.run();
			} catch (Exception ex) {
				E.report("Failed to run ", ex);
			}
	}
	 
	
	
}
