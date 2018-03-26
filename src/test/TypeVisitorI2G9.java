package test;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class TypeVisitorI2G9 extends ASTVisitor {


	public static HashMap <String, ArrayList<Integer>> typeMap = new HashMap <String, ArrayList<Integer>>();

		/**
		 * finds Type Declarations and adds to
		 * declarationCounter if
		 * the node string matches the specified
		 * declaration type for classes and interfaces
		 */
		public boolean visit(TypeDeclaration node) {
			ITypeBinding typeBind = node.resolveBinding();
			String nodeName = typeBind.getQualifiedName();
			if(typeMap.containsKey(nodeName)) {
				
				ArrayList<Integer> currentArray = typeMap.get(nodeName); 
				int currentValue = currentArray.get(0);
				currentArray.set(0, currentValue+1);
				typeMap.replace(nodeName, currentArray);
			}
			else {
				
				ArrayList<Integer> intArray = new ArrayList<Integer>();
				intArray.add(1);
				intArray.add(0);
				typeMap.put(nodeName,intArray);
			}
			return true;
		}
		
		/**
		 * Used for Enum declarations
		 * Gets the name of the node and compares to
		 * the type to look for, if the same
		 * increases dCount by 1
		 * @param node of the type EnumDeclaration
		 * @return true to visit the children nodes
		 */
		public boolean visit(EnumDeclaration node) {
			ITypeBinding typeBind = node.resolveBinding();
			
			String typeNode = typeBind.getQualifiedName();
			if(typeMap.containsKey(typeNode)) {
				ArrayList<Integer> currentArray = typeMap.get(typeNode); //declerations
				int currentValue = currentArray.get(0);
				currentArray.set(0, currentValue+1);
				
				typeMap.replace(typeNode, currentArray);
			}
			else {
				ArrayList<Integer> intArray = new ArrayList<Integer>();
				intArray.add(1);
				intArray.add(0);
				typeMap.put(typeNode,intArray);
			}
			
			return true;
		}
		
		/**
		 * Used for Annotation declarations
		 * Gets the name of the node and compares to
		 * the type to look for, if the same
		 * increases dCount by 1
		 * @param node of the type AnnotationTypeDeclaration
		 * @return true to visit the children nodes
		 */
		public boolean visit(AnnotationTypeDeclaration node) {
			ITypeBinding typeBind = node.resolveBinding();
			String typeNode = typeBind.getQualifiedName();				
			if(typeMap.containsKey(typeNode)) {
				ArrayList<Integer> currentArray = typeMap.get(typeNode); //declerations
				int currentValue = currentArray.get(0);
				currentArray.set(0, currentValue+1);
				
				typeMap.replace(typeNode, currentArray);
			}
			else{
				ArrayList<Integer> intArray = new ArrayList<Integer>();
				intArray.add(1);
				intArray.add(0);
				typeMap.put(typeNode,intArray);
				}
			return true;
		}
		
	
		
		/**
		 * @param node of type VariableDeclarationFragment
		 * Resolves the bindings so we can acquire the node's full name
		 * @return false, don't visit children
		 */
		public boolean visit(VariableDeclarationFragment node) {
			IVariableBinding rNode = node.resolveBinding();
			String typeNode = rNode.getType().getQualifiedName();
			if(typeMap.containsKey(typeNode)) {
				ArrayList<Integer> currentArray = typeMap.get(typeNode); //rcount
				int currentValue = currentArray.get(1);
				currentArray.set(1, currentValue+1);
				
				typeMap.replace(typeNode, currentArray);
			}
			else { 
				ArrayList<Integer> intArray = new ArrayList<Integer>();
				intArray.add(0);
				intArray.add(1);
				typeMap.put(typeNode,intArray);
			}
			return false;
		}
		
		/**
		 * node of type SimpleType
		 */
		public boolean visit(SimpleType node) {
			ITypeBinding  typeBind = node.resolveBinding();
			String typeNode = typeBind.getQualifiedName();
			if(typeMap.containsKey(typeNode)) {
				ArrayList<Integer> currentArray = typeMap.get(typeNode); //rcount
				int currentValue = currentArray.get(1);
				currentArray.set(1, currentValue+1);
				
				typeMap.replace(typeNode, currentArray);
			}
			else { 
				ArrayList<Integer> intArray = new ArrayList<Integer>();
				intArray.add(0);
				intArray.add(1);
				typeMap.put(typeNode,intArray);
			}
			return true;
		}
		
		
		/**
		 * Visits the import declarations, adds to the counter
		 */
		public boolean visit(ImportDeclaration node) {
			IBinding typeBind = node.resolveBinding();
			String typeNode = typeBind.getName();
			if(typeMap.containsKey(typeNode)) {
				ArrayList<Integer> currentArray = typeMap.get(typeNode); //rcount
				int currentValue = currentArray.get(1);
				currentArray.set(1, currentValue+1);
				
				typeMap.replace(typeNode, currentArray);
			}
			else { 
				ArrayList<Integer> intArray = new ArrayList<Integer>();
				intArray.add(0);
				intArray.add(1);
				typeMap.put(typeNode,intArray);
			}
			return true;
		}

		/**
		 * Counts the references involved in annotations that are not declarations
		 */
		public boolean visit(NormalAnnotation node) {
			IBinding typeBind = node.resolveAnnotationBinding();
			String typeNode = typeBind.getName();
			if(typeMap.containsKey(typeNode)) {
				ArrayList<Integer> currentArray = typeMap.get(typeNode); //rcount
				int currentValue = currentArray.get(1);
				currentArray.set(1, currentValue+1);
				
				typeMap.replace(typeNode, currentArray);
			}
			else { 
				ArrayList<Integer> intArray = new ArrayList<Integer>();
				intArray.add(0);
				intArray.add(1);
				typeMap.put(typeNode,intArray);
			}
			return true;
		}
}
