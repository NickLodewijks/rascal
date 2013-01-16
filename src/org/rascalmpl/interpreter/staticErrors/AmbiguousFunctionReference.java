/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Jurgen J. Vinju - Jurgen.Vinju@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.interpreter.staticErrors;

import org.rascalmpl.ast.AbstractAST;

public class AmbiguousFunctionReference extends StaticError {
	private static final long serialVersionUID = -1800429914313750634L;

	public AmbiguousFunctionReference(String name, AbstractAST ast) {
		super("Ambiguous unqualified function name: " + name, ast);
	}

}
