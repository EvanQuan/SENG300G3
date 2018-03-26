package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * TypeVisitor.java
 *
 * A visitor for abstract syntax trees. For each different concrete AST node
 * type T, the visitor will locate the different java types present in the
 * source code, and count the number of declarations of references for each of
 * the java types present.
 *
 * @author Sze Lok Irene Chan
 * @version 1.0.0
 *
 * @since 20 March 2018
 */
public class TypeVisitorI2G7 extends ASTVisitor {

	// Global variables
	private static ArrayList<String> types = new ArrayList<String>();

	private static HashMap<String, Integer> decCounter = new HashMap<String, Integer>();

	private static HashMap<String, Integer> refCounter = new HashMap<String, Integer>();

	private static boolean debug = false;

	/**
	 * Checks if the passed type already exists within the types list. [false -> add
	 * type to list create entry <type, 0> in decCounter create entry <type, 0> in
	 * refCounter] [true -> do nothing]
	 *
	 * @param type:
	 *            String, java type
	 */
	private static void addTypeToList(String type) {
		if (!types.contains(type)) {
			types.add(type);
			decCounter.put(type, 0);
			refCounter.put(type, 0);
		}
	}

	/**
	 * Increment the counter value for a given type in decCounter.
	 *
	 * @param type
	 *            String, java type
	 */
	private static void incDecCount(String type) {
		// Check if the type exists, then increment their associated value by 1
		if (decCounter.containsKey(type)) {
			decCounter.put(type, decCounter.get(type) + 1);
		}
	}

	/**
	 * Increment the counter value for a given type in refCounter.
	 *
	 * @param type
	 *            String, java type
	 */
	private static void incRefCount(String type) {
		// Check if the type exists, then increment their associated value by 1
		if (refCounter.containsKey(type)) {
			refCounter.put(type, refCounter.get(type) + 1);
		}
	}

	public static void printTypes() {
		for (String type : types) {
			int refCount = refCounter.get(type);
			int decCount = decCounter.get(type);
			System.out.println(type + ". Declarations found: " + decCount + "; references found: " + refCount + ".");
		}
	}

	/*
	 * ============================== HELPER FUNCTIONS ==============================
	 */

	/**
	 * constructor, removed implementation
	 */
	public TypeVisitorI2G7() {}

	/**
	 * Accessor method. Fetches the map of declaration counter.
	 *
	 * @return HashMap : decCounter
	 */
	public Map<String, Integer> getDecCount() {
		return decCounter;
	}

	/**
	 * Accessor method. Fetches the list of types.
	 *
	 * @return ArrayList<String> : types
	 */
	public ArrayList<String> getList() {
		return types;
	}

	/**
	 * Accessor method. Fetches the map of reference counter.
	 *
	 * @return HashMap : refCounter
	 */
	public Map<String, Integer> getRefCount() {
		return refCounter;
	}

	/**
	 * Clean up method. Allows caller to reset all list and Maps
	 * in the current instance.
	 */
	public void resetCounters(){
		types.clear();
		decCounter.clear();
		refCounter.clear();
	}
	
	/*
	 * ============================== ASTVisitor FUNCTIONS ==============================
	 */
	
	/**
	 * Visits an annotation type declaration AST node type. Looks for
	 *
	 * @interface <identifier> { }
	 *
	 *            Determine the type of the annotation, add it to types, and
	 *            increment its type's counter in decCounter.
	 *
	 *            CounterType: DECLARATION
	 *
	 * @param node
	 *            AnnotationTypeDeclaration
	 * @return boolean true to visit the children of this node
	 */
	@Override
	public boolean visit(AnnotationTypeDeclaration node) {
		ITypeBinding typeBind = node.resolveBinding();
		String type = typeBind.getQualifiedName();

		addTypeToList(type);
		incDecCount(type);

		return true;
	}

	/**
	 * Visits a Enum declaration AST node type. Determine the type of the Enum
	 * identifier, add it to types, and increment its type's counter value in
	 * decCounter.
	 *
	 * CounterType: DECLARATION
	 *
	 * @param node
	 *            : EnumDeclaration
	 * @return boolean : True to visit the children of this node
	 */
	@Override
	public boolean visit(EnumDeclaration node) {
		ITypeBinding typeBind = node.resolveBinding();
		String type = typeBind.getQualifiedName();

		addTypeToList(type);
		incDecCount(type);

		return true;
	}

	/**
	 * Visits a type declaration node type. Type declaration node is the union of
	 * class declaration, and interface declaration.
	 *
	 * Determine the type of class, add it to types, and increment the declaration
	 * counter associated to the type.
	 *
	 * CounterType: DECLARATION
	 *
	 * @param node
	 *            : TypeDeclaration
	 * @return boolean : True to visit the children of this node
	 */
	@Override
	public boolean visit(TypeDeclaration node) {
		if (!node.isLocalTypeDeclaration()) {
			// get the Identifier and add +1 to the declaration count
			ITypeBinding typeBind = node.resolveBinding();
			String type = typeBind.getQualifiedName();

			addTypeToList(type);
			incDecCount(type);
		}
		
		// if it is the local class declaration.
		else {
			// find the identifier and add +1 to declaration
			SimpleName localClassSimpleName = node.getName();
			String localClassName = localClassSimpleName.getFullyQualifiedName();
			
			addTypeToList(localClassName);
			incDecCount(localClassName);
		}
		return true;
	}
	
	/**
	 * Visits a SimpleName node type. A simple name is an identifier other
	 * than a keyword, boolean literal ("true", "false") or null literal ("null").
	 * SimpleName:
	 *      Identifier
	 *
	 * General Algorithm:
	 * 1. Determine the name of the identifier by calling node.getFullyQualifiedName()
	 * 2. Determine the name of the identifier after resolving the binding.
	 * 3. Check if the result from 1 and 2 are the same. If they are, add +1 to the ref count.
	 *
	 * CounterType: REFERENCE
	 *
	 * @param node
	 *            : SimpleName
	 * @return boolean : True to visit the children of this node
	 */
	@Override
	public boolean visit(SimpleName node) {
		// isDeclaration() returns true if a name is defined:
		// the type name in a TypeDeclaration node -> DONE
		// the method name in a MethodDeclaration node providing isConstructor is false -> WE DON'T HAVE TO BOTHER
		// The variable name in any type of VariableDeclaration node -> WE HAVE TO BOTHER
		// The enum type name in a EnumDeclaration node -> DONE
		// The enum constant name in an EnumConstantDeclaration node -> WE DON'T HAVE TO BOTHER
		// The variable name in an EnhancedForStatement node -> WE DON'T HAVE TO BOTHER
		// The type variable name in a TypeParameter node -> WE DON'T HAVE TO BOTHER
		// The type name in an AnnotationTypeDeclaration node -> DONE
		// The member name in an AnnotationTypeMemberDeclaration node -> WE DON'T HAVE TO BOTHER
		
		// NOTICE HOW ImportDeclaration and PackageDeclaration types
		// will ALWAYS pass this, since they are not listed above.
		// Therefore we can use SimpleName for ImportDeclaration and PackageDeclaration;
		// However, the PackageDeclaration will have the bindtype of 1,
		// so it will not be counted in this node -> Gotta count the PackageDeclaration
		// in a separate node
		if (!node.isDeclaration())
		{	
			String type1 = node.getFullyQualifiedName();
			// Determine what kind of binding this is.
			// bindtype == 1 if it's a package binding -> DONE
			// bindtype == 2 if it's a type binding -> DONE
			// bindtype == 3 if it's a field or local variable binding -> WE DON'T HAVE TO BOTHER
			// bindtype == 4 if it's a method or constructor binding -> DONE
			// bindtype == 5 if it's a annotation binding -> DONE
			// bindtype == 6 if it's a member value pair binding
			// If the binding null, then set the bindtype to 0
			IBinding binding = node.resolveBinding();
			int bindtype;
			
			if (binding == null) {
				bindtype = 0;
			}
			else {
				bindtype = binding.getKind();
			}
			
			// see if this is from ImportDeclaration
			// if it is not, don't count it in
			ASTNode parent = node.getParent();
			int levelCounter = 0;
			while (parent.getClass().getName().contains("QualifiedName")) {
				levelCounter++;
				parent = parent.getParent();
			}
			
			if (parent.getClass().getName().contains("ImportDeclaration") && levelCounter == 1) {
				ImportDeclaration iNode = (ImportDeclaration) parent;
				Name importingName = iNode.getName();
				String importingNameQualified = importingName.getFullyQualifiedName();
				
				addTypeToList(importingNameQualified);
				incRefCount(importingNameQualified);
				
				//System.out.println("result4: " + importingNameQualified);
				return true;
			}
			else if (parent.getClass().getName().contains("ImportDeclaration") && levelCounter != 1) {
				return true;
			}
			
			//if (debug)
				System.out.println(type1 + "'s bindtype: " + bindtype);
			
			if (bindtype == 2) {
				ITypeBinding typeBind = node.resolveTypeBinding();
				String type2 = typeBind.getName();
				
				// taking care of the parameterized types under the SimpleName type
				if (type2.contains("<") && type2.contains(">")) {
					// parse out only the identifier part
					type2 = type2.substring(0, type2.indexOf("<"));
					
					// check if the identifier part of the node name is the same as
					// the identifier of the name after resolving binding.
					// If they are equal, then add this to the ref count.
					if (type1.equals(type2)) {
						String type = typeBind.getQualifiedName();
						type = type.substring(0, type.indexOf("<"));
						addTypeToList(type);
						incRefCount(type);
						
						//System.out.println("result1: " + type);
					}
				}
				
				// taking care of the rest of the SimpleName types
				else {
					// check if the identifier part of the node name is the same as
					// the identifier of the name after resolving binding.
					// If they are equal, then add this to the ref count.
					if (type1.equals(type2)) {
						String type = typeBind.getQualifiedName();
						addTypeToList(type);
						incRefCount(type);
						
						System.out.println("result2: " + type);
					}
				}
			}
			
			// if the bindtype is 0, add the reference counter;
			// this takes care of the cases where there is a reference to a random class
			// that has not been implemented.
			else if (bindtype == 0) {
				// see if this is from SimpleType
				// if it is not, don't count it in
				if (parent.getClass().getName().contains("SimpleType")) {
					SimpleType sNode = (SimpleType) parent;
					Name simpletypeName = sNode.getName();
					String simpletypeNameQualified = simpletypeName.getFullyQualifiedName();
					
					addTypeToList(simpletypeNameQualified);
					incRefCount(simpletypeNameQualified);
					
					//System.out.println("result3: " + simpletypeNameQualified);
				}
			}

			// if the bindtype is 4, add the reference counter ONLY IF
			// the parent node is MethodDeclaration and only if the MethodDeclaration node
			// is a constructor
			else if (bindtype == 4) {
				if (parent.getClass().getName().contains("MethodDeclaration")) {
					MethodDeclaration mNode = (MethodDeclaration) parent;
					
					if (mNode.isConstructor()) {
						IMethodBinding methodBinding = mNode.resolveBinding();
						ITypeBinding declaringclass = methodBinding.getDeclaringClass();
						String declaringclassName = declaringclass.getQualifiedName();
						
						addTypeToList(declaringclassName);
						incRefCount(declaringclassName);
						
						//System.out.println("result5: " + declaringclassName);
					}
				}
			}
		}

		return true;
	}
	
	/**
	 * Visits a PrimitiveType node type.
	 * PrimitiveType:
	 *     { Annotation } byte
	 *     { Annotation } short
	 *     { Annotation } char
	 *     { Annotation } int
	 *     { Annotation } long
	 *     { Annotation } float
	 *     { Annotation } double
	 *     { Annotation } boolean
	 *     { Annotation } void
	 *
	 * Determine the type of the binding, add it to types, and increment the reference
	 * counter associated to the type.
	 *
	 * CounterType: REFERENCE
	 *
	 * @param node
	 *            : PrimitiveType
	 * @return boolean : True to visit the children of this node
	 */
	@Override
	public boolean visit(PrimitiveType node) {
		ITypeBinding typeBind = node.resolveBinding();
		String type = typeBind.getQualifiedName();
		
		if (node.getPrimitiveTypeCode().toString() != "void") {
			addTypeToList(type);
			incRefCount(type);
		}
		
		return true;
	}
	
	/**
	 * Visits a ArrayType node type.
	 * ArrayType:
	 *     Type Dimension { Dimension }
	 *
	 * Determine the type of the binding, add it to types, and increment the reference
	 * counter associated to the type.
	 *
	 * CounterType: REFERENCE
	 *
	 * @param node
	 *            : ArrayType
	 * @return boolean : True to visit the children of this node
	 */
	@Override
	public boolean visit(ArrayType node) {
		ITypeBinding typeBind = node.resolveBinding();
		String type = typeBind.getQualifiedName();
		
		addTypeToList(type);
		incRefCount(type);
		return true;
	}
	
	@Override
	public boolean visit(SingleVariableDeclaration node) {
		// if the variable refers to some array, we need to add +1 to the reference
		// get the name of the variable
		SimpleName variableName = node.getName();
		ITypeBinding nameBind = variableName.resolveTypeBinding();
		String type = nameBind.getQualifiedName();
		
		if (type.contains("[]")) {
			addTypeToList(type);
			incRefCount(type);
		}
		
		return true;
	}
} 
