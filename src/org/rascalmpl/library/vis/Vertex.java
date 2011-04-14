/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library.vis;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.properties.PropertyManager;

/**
 * Vertex: a point that is part of a shape.
 * TODO: subclass from container?
 * 
 * @author paulk
 *
 */
public class Vertex extends Figure {
	Figure marker;
	float deltax;
	float deltay;
	float leftAnchor;
	float rightAnchor;
	float topAnchor;
	float bottomAnchor;
	private static boolean debug = false;

	private float getIntOrReal(IValue v){
		if(v.getType().isIntegerType())
			return ((IInteger) v).intValue();
		if(v.getType().isRealType())
			return ((IReal) v).floatValue();
		return 0;
		
	}
	public Vertex(IFigureApplet fpa, PropertyManager properties, IValue dx, IValue dy) {
		super(fpa, properties);
		deltax = getIntOrReal(dx);
		deltay = getIntOrReal(dy);
	}
	
	public Vertex(IFigureApplet fpa, PropertyManager properties, IValue dx, IValue dy, IConstructor marker, IEvaluatorContext ctx) {
		super(fpa, properties);
		deltax = getIntOrReal(dx);
		deltay = getIntOrReal(dy);
		if(marker != null)
			this.marker = FigureFactory.make(fpa, marker, properties, ctx);
		if(debug)System.err.printf("Vertex at %f, %f\n", deltax, deltay);
	}

	@Override
	public
	void bbox(float desiredWidth, float desiredHeight){

		if(marker != null){
			//TODO is this ok?
			marker.bbox(AUTO_SIZE, AUTO_SIZE);
			if(debug) System.err.printf("Vertex: marker anchors hor (%f, %f), vert (%f, %f)\n",
					   marker.leftAnchor(), marker.rightAnchor(), marker.topAnchor(), marker.bottomAnchor());
			if(marker.leftAnchor() >= deltax){
				leftAnchor = marker.leftAnchor() - deltax;
				width = marker.width;
				rightAnchor = width - leftAnchor;
			} else {
				leftAnchor = 0;
				width = deltax + marker.rightAnchor();
				rightAnchor = width;
			}
			
			if(marker.bottomAnchor() >= deltay){
				bottomAnchor = marker.bottomAnchor();
				topAnchor = marker.topAnchor() + deltay;
				height = bottomAnchor + topAnchor;
			} else {
				bottomAnchor = 0;
				height = deltay + marker.topAnchor();
				topAnchor = height;
			}
			
		} else {
			width = deltax;
			height = deltay;
			leftAnchor = bottomAnchor = 0;
			rightAnchor = width;
			topAnchor = height;
		}
		if(debug)System.err.printf("bbox.vertex: deltax=%f, deltay=%f, width = %f (%f, %f), height= %f (%f, %f))\n", 
							deltax, deltay, width, leftAnchor, rightAnchor, height, topAnchor, bottomAnchor);
	}
	
	@Override
	public
	void draw(float left, float top) {
		this.setLeft(left);
		this.setTop(top);
		applyProperties();
		if(debug){
			System.err.println("Vertex: marker = " + marker);
			System.err.printf("Vertex: marker at %f, %f\n", left, top);
		}
		if(marker != null){
			marker.bbox(AUTO_SIZE, AUTO_SIZE);
			marker.draw(left-marker.width/2, top-marker.height/2);
		}
	}
	
	@Override
	public float leftAnchor(){
		return leftAnchor;
	}
	
	@Override
	public float rightAnchor(){
		return rightAnchor;
	}
	
	@Override
	public float topAnchor(){
		return topAnchor;
	}
	
	@Override
	public float bottomAnchor(){
		return bottomAnchor;
	}

}
