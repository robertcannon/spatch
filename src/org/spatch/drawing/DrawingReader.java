package org.spatch.drawing;

import java.awt.Color;
import java.util.HashSet;
import java.util.StringTokenizer;

import org.lemsml.jlems.core.logging.E;
import org.lemsml.jlems.core.xml.XMLElement;
 

public class DrawingReader {

	private static HashSet<String> others = new HashSet<String>();
	{
		others.add("LinkTargetConnector");
		others.add("LinkSourceConnector");
	}
	
	
	public FixedDrawingComponent importDrawing(XMLElement xec) {
		FixedDrawingComponent ret = null;
		String snm = xec.getName();
	
	//	E.info("reading component type " + snm);
		
		
		if (snm.equals("PolyLine")) {
			GenericShape gs = new GenericShape();
			gs.setXPoints(readDoubleArray(xec.getAttribute("x")));
			gs.setYPoints(readDoubleArray(xec.getAttribute("y")));
			readProps(xec, gs);
			ret = gs;
			
		} else if (snm.equals("PolyFill")) {
			GenericShape gs = new GenericShape();
			gs.setFilled();
			gs.setXPoints(readDoubleArray(xec.getAttribute("x")));
			gs.setYPoints(readDoubleArray(xec.getAttribute("y")));
			readProps(xec, gs);
			 
			ret = gs;
			
		} else if (snm.equals("Rectangle")) {
			double[] xywh = readXYWH(xec);
			FDRectangle rec = new FDRectangle(xywh);
			readProps(xec, rec);
			ret = rec;

		} else if (snm.equals("Circle")) {
			double[] xywh = readXYWH(xec);
			FDCircle circ = new FDCircle(xywh);
			readProps(xec, circ);
			ret = circ;
			
			
		} else if (others.contains(snm)) {
			// OK - something else will handle it
			
		} else {
			E.warning("cant import " + snm);
		}
		
		
		return ret;
	}

	
	
	private void readProps(XMLElement xec, FixedDrawingComponent ret) {
		if (xec.hasAttribute("color")) {
			ret.setColor(parseColor(xec.getAttribute("color")));
		}
		if (xec.hasAttribute("lineWidth")) {
			ret.setLineWidth(parseDouble(xec.getAttribute("lineWidth")));
		}
		if (xec.hasAttribute("fillColor")) {
			ret.setFillColor(parseColor(xec.getAttribute("fillColor")));
		}
		if (xec.hasAttribute("cornerRadius")) {
			ret.setCornerRadius(parseDouble(xec.getAttribute("cornerRadius")));
		}
	}
	
	
	
	double[] readXYWH(XMLElement xec) {
	double cx = 0.;
	double cy = 0.;
	double w = 1.;
	double h = 1.;
	if (xec.hasAttribute("x")) {
		cx = parseDouble(xec.getAttribute("x"));
	}
	if (xec.hasAttribute("y")) {
		cy = parseDouble(xec.getAttribute("y"));
	}
	if (xec.hasAttribute("diameter")) {
		double d = parseDouble(xec.getAttribute("diameter"));
		w = d;
		h = d;
	}
	
	if (xec.hasAttribute("radius")) {
		double r = parseDouble(xec.getAttribute("radius"));
		w = 2 * r;
		h = 2 * r;
	}
	
	if (xec.hasAttribute("width")) {
		w = parseDouble(xec.getAttribute("width"));
	}
	if (xec.hasAttribute("height")) {
		h = parseDouble(xec.getAttribute("height"));
	}
	double[] ret = {cx, cy, w, h};
	return ret;
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
