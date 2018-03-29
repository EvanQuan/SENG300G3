package main.ast;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

	/**
	 * Iteration 2 Group 3
	 * @author Dominic Demierre
	 * @author Osagie Omigie
	 *
	 */
public class TypeVisitorI2G3 extends ASTVisitor {
	    private ArrayList<String> types;
	    private ArrayList<Integer> declarations;
	    private ArrayList<Integer> references;
	    
	    /**
	     * Creates a new QualifiedNameCounter object.
	     */
	    public TypeVisitorI2G3() {
	        types = new ArrayList<String>();
	        declarations = new ArrayList<Integer>();
	        references = new ArrayList<Integer>();
	    }
	    
	    /**
	     * Checks whether a Java type is accounted for, and if not adds it to types ArrayList.
	     * 
	     * @param node the node whose corresponding java type is to be checked
	     */
	    private void isInType(ASTNode node) {
	        if (node instanceof Name) {
	            String name = ((Name) node).getFullyQualifiedName();
	            if (types.indexOf(name) == -1) {
	                types.add(name);
	                declarations.add(0);
	                references.add(0);
	            }
	        }
	        else if (node instanceof AbstractTypeDeclaration) {
	            String name = ((AbstractTypeDeclaration) node).resolveBinding().getQualifiedName();
	            if (types.indexOf(name) == -1) {
	                types.add(name);
	                declarations.add(0);
	                references.add(0);
	            }
	        }
	        else if (node instanceof Type) {
	            String name = ((Type) node).resolveBinding().getQualifiedName();
	            if (types.indexOf(name) == -1) {
	                types.add(name);
	                declarations.add(0);
	                references.add(0);
	            }
	        }
	    }
	    
	    /**
	     * Counts an Annotation type declaration.
	     * 
	     * @return      True, to check children.
	     */
	    @Override
		public boolean visit(AnnotationTypeDeclaration node) {
	        ITypeBinding iType = node.resolveBinding();
	        String name = iType.getQualifiedName();
	        isInType(node);
	        int index = types.indexOf(name);
	        declarations.set(index, declarations.get(index) + 1);
	        
	        return true;
	    }
	    /**
	     * Counts an enum declaration.
	     * 
	     * @return      True, to check children.
	     */
	    @Override
		public boolean visit(EnumDeclaration node) {
	        ITypeBinding iType = node.resolveBinding();
	        String name = iType.getQualifiedName();
	        
	        isInType(node);
	        int index = types.indexOf(name);
	        declarations.set(index, declarations.get(index) + 1);
	        
	        return true;
	    }
	    
	    /**
	     * Counts a reference to a primitive type.
	     * 
	     * @return      True, to check children.
	     */
	    @Override
		public boolean visit(PrimitiveType node) {
	        ITypeBinding iType = node.resolveBinding();
	        String name = iType.getQualifiedName();
	        
	        isInType(node);
	        int index = types.indexOf(name);
	        references.set(index, references.get(index) + 1);
	        
	        return true;
	    }
	    
	    /**
	     * Counts a reference to a simple name.
	     * 
	     * @return      True, to check children.
	     */
	    @Override
		public boolean visit(SimpleName node) {
	        if (!node.isDeclaration()) {
	            ASTNode parent = node.getParent();
	            if (parent.getNodeType() == ASTNode.SIMPLE_TYPE) {
	                ITypeBinding parentType = ((SimpleType) parent).resolveBinding();
	                String parentName = parentType.getQualifiedName();
	                if (types.indexOf(parentName) > -1) return true;
	            }
	            else if (parent.getNodeType() == ASTNode.QUALIFIED_NAME) {                  // Do not add a type thats parent (qualified name)
	                String parentName = ((QualifiedName) parent).getFullyQualifiedName();   // has already been aded.
	                if (types.indexOf(parentName) > -1) return true;
	            }
	            else if (parent.getNodeType() == ASTNode.PACKAGE_DECLARATION) return true;  // Do not count package declarations.
	            
	            String name = node.getFullyQualifiedName(); 
	            isInType(node);
	            int index = types.indexOf(name);
	            references.set(index, references.get(index) + 1);
	        }           
	        return true;
	    }
	    
	    /**
	     * Counts a reference to a simple type.
	     * 
	     * @return      True, to check children.
	     */
	    @Override
		public boolean visit(SimpleType node) {
	        if (node.resolveBinding() != null) {
	            ITypeBinding iType = node.resolveBinding();
	            String name = iType.getQualifiedName();
	            isInType(node);
	            int index = types.indexOf(name);
	            references.set(index, references.get(index) + 1);
	        }
	        
	        return true;
	    }
	    /**
	     * Counts a type declaration.
	     * 
	     * @return      True, to check children.
	     */
	    @Override
		public boolean visit(TypeDeclaration node) {
	        ITypeBinding iType = node.resolveBinding();
	        String name = iType.getQualifiedName();
	        
	        isInType(node);
	        int index = types.indexOf(name);
	        declarations.set(index, declarations.get(index) + 1);
	        
	        return true;
	    }
	    
	    /**
	     * Returns an ArrayList containing all found Java types.
	     * 
	     * @return the ArrayList of String objects
	     */
	    public ArrayList<String> getFoundTypes() {
	        return types;
	    }
	    
	    /**
	     * Returns an ArrayList containing the numbers of declarations of each type.
	     * 
	     * @return the ArrayList of Integer objects
	     */
	    public ArrayList<Integer> getDeclarationsArray() {
	        return declarations;
	    }
	    
	    /**
	     * Returns an ArrayList containing the numbers of references to each type.
	     * 
	     * @return the ArrayList of Integer objects
	     */
	    public ArrayList<Integer> getReferencesArray() {
	        return references;
	    }
}
