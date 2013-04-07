package org.spatch.drawing;

import java.awt.Color;
import java.util.HashSet;
import java.util.StringTokenizer;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.type.visualization.Circle;
import org.lemsml.jlems.core.type.visualization.DrawingConnector;
import org.lemsml.jlems.core.type.visualization.DrawingElement;
import org.lemsml.jlems.core.type.visualization.PolyFill;
import org.lemsml.jlems.core.type.visualization.PolyLine;
import org.lemsml.jlems.core.type.visualization.Rectangle;
import org.lemsml.jlems.core.type.visualization.Visualization;
import org.lemsml.jlems.core.xml.XMLElement;
 

public class DrawingImporter {

	Visualization viz = null;
	
	
	
	public DrawingImporter(Visualization v) {
		viz = v;
	}


	public VectorIcon makeVectorIcon() {
		VectorIcon ret = new VectorIcon();
		
		
		for (DrawingElement de : viz.drawingElements) {
			ret.add(makeDrawingComponent(de));
		}
		
		for (DrawingConnector dc : viz.drawingConnectors) {
			
		}
		
		return ret;
	}

	public FixedDrawingComponent makeDrawingComponent(DrawingElement de) {
		FixedDrawingComponent ret = null;
	 
		
		if (de instanceof PolyLine) {
			GenericShape gs = new GenericShape();
			PolyLine pl = (PolyLine)de;
			gs.setXPoints(readDoubleArray(pl.xpts));
			gs.setYPoints(readDoubleArray(pl.ypts));
			readProps(de, gs);
			ret = gs;
			
		} else if (de instanceof PolyFill) {
			GenericShape gs = new GenericShape();
			gs.setFilled();
			PolyFill pf = (PolyFill)de;
			gs.setXPoints(readDoubleArray(pf.xpts));
			gs.setYPoints(readDoubleArray(pf.ypts));
			readProps(de, gs);
			 
			ret = gs;
			
		} else if (de instanceof Rectangle) {
			Rectangle r = (Rectangle)de;
			FDRectangle rec = new FDRectangle(r.x, r.y, 0.5 * r.width, 0.5 * r.height);
			readProps(r, rec);
			ret = rec;

		} else if (de instanceof Circle) {
			Circle c = (Circle)de;
			FDCircle circ = new FDCircle(c.x, c.y, c.diameter);
			readProps(c, circ);
			ret = circ;
			
	 
		} else {
			E.warning("cant import " + de);
		}
		
		
		return ret;
	}

	
	
	private void readProps(DrawingElement de, FixedDrawingComponent cpt) {
		cpt.setLineWidth(de.lineWidth);
		cpt.setLineColor(de.lineColor);
		if (de.fillColor != null) {
			cpt.setFillColor(de.fillColor);
		}
	}
	
	 
	
	
	
	private int parseInt(String s) {
		int ret = Integer.parseInt(s);
		return ret;
	}
	
	private double parseDouble(String s) {
		double ret = Double.parseDouble(s);
		return ret;
	}
	
	private double[] readDoubleArray(String s) {
		StringTokenizer st = new StringTokenizer(s, ", ");
		int n = st.countTokens();
		double[] ret = new double[n];
		for (int i = 0; i < n; i++) {
			ret[i] = Double.parseDouble(st.nextToken());
		}
		return ret;
	}
	
	  public Color parseColor(String astr) {
		  	String str = astr;
		  	if (str.indexOf("#") == 0) {
		  		str = str.substring(1, str.length());
		  	} 
		  	Color ret = new Color(
	          Integer.parseInt(str.substring(0,2), 16),
	          Integer.parseInt(str.substring(2,4), 16),
	          Integer.parseInt(str.substring(4,6), 16));
		  	return ret;
	      }




}
