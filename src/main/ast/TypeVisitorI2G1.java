package main.ast;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 *
 * A class which extends ASTVisitor and tracks is used to track the number of
 * occurrences of targetType in an AST.
 *
 */
public class TypeVisitorI2G1 extends ASTVisitor {
	public class countPair {
		private int declarationCount = 0;
		private int referenceCount = 0;

		public int getDeclarationCount() {
			return declarationCount;
		}

		public int getReferenceCount() {
			return referenceCount;
		}

		public void incrementDeclarationCount() {
			declarationCount++;
		}

		public void incrementReferenceCount() {
			referenceCount++;
		}
	}

	public HashMap<String, countPair> countMap = new HashMap<String, countPair>();

	/**
	 * Set the HashMap that will be used to hold the counts
	 *
	 * @param counts
	 */
	public void setCounts(HashMap<String, countPair> counts) {
		countMap = counts;
	}

	@Override
	public boolean visit(AnnotationTypeDeclaration node) {
		ITypeBinding bind = node.resolveBinding();

		String name = bind.getQualifiedName();
		if (name.equals("")) {
			name = bind.getName();
		}
		countPair current = countMap.get(name);
		if (current == null) {
			current = new countPair();
		}
		current.incrementDeclarationCount();
		countMap.put(name, current);

		return true;
	}

	@Override
	public boolean visit(EnumDeclaration node) {
		ITypeBinding bind = node.resolveBinding();

		String name;
		if (bind != null) {
			name = bind.getQualifiedName();
			if (name.equals("")) {
				name = bind.getName();
			}
		} else {
			name = node.getName().getFullyQualifiedName();
		}
		countPair current = countMap.get(name);
		if (current == null) {
			current = new countPair();
		}
		current.incrementDeclarationCount();
		countMap.put(name, current);

		return true;
	}

	@Override
	public boolean visit(FieldAccess node) {
		ITypeBinding bind = node.getExpression().resolveTypeBinding();
		if (!bind.isClass() && !bind.isAnnotation() && !bind.isEnum() && !bind.isInterface()) {
			// We only care about static methods, so exit when other types are reached.
			return true;
		}

		String name = bind.getQualifiedName();

		countPair current = countMap.get(name);
		if (current == null) {
			current = new countPair();
		}
		current.incrementReferenceCount();
		countMap.put(name, current);

		return true;
	}

	@Override
	public boolean visit(MarkerAnnotation node) {
		ITypeBinding bind = node.resolveTypeBinding();

		String name;
		if (bind != null) {
			name = bind.getQualifiedName();
		} else {
			name = node.getTypeName().getFullyQualifiedName();
		}
		countPair current = countMap.get(name);
		if (current == null) {
			current = new countPair();
		}
		current.incrementReferenceCount();
		countMap.put(name, current);

		return true;
	}

	@Override
	public boolean visit(MethodInvocation node) {
		ITypeBinding bind = node.getExpression().resolveTypeBinding();
		if (!bind.isClass() && !bind.isAnnotation() && !bind.isEnum() && !bind.isInterface()) {
			// We only care about static methods, so exit when other types are reached.
			return true;
		}

		String name = bind.getQualifiedName();

		countPair current = countMap.get(name);
		if (current == null) {
			current = new countPair();
		}
		current.incrementReferenceCount();
		countMap.put(name, current);

		return true;
	}

	@Override
	public boolean visit(NormalAnnotation node) {
		ITypeBinding bind = node.resolveTypeBinding();

		String name;
		if (bind != null) {
			name = bind.getQualifiedName();
		} else {
			name = node.getTypeName().getFullyQualifiedName();
		}
		countPair current = countMap.get(name);
		if (current == null) {
			current = new countPair();
		}
		current.incrementReferenceCount();
		countMap.put(name, current);

		return true;
	}

	@Override
	public boolean visit(PrimitiveType node) {
		ITypeBinding bind = node.resolveBinding();

		String name = bind.getQualifiedName();

		if (name.equals("void")) {
			// Ignore the void type
			return true;
		}

		countPair current = countMap.get(name);
		if (current == null) {
			current = new countPair();
		}
		current.incrementReferenceCount();
		countMap.put(name, current);

		return true;
	}

	@Override
	public boolean visit(QualifiedName node) {
		ITypeBinding bind = node.getQualifier().resolveTypeBinding();
		if (bind != null && (bind.isClass() || bind.isEnum() || bind.isInterface() || bind.isAnnotation())) {
			String name = bind.getQualifiedName();
			countPair current = countMap.get(name);

			if (current == null) {
				current = new countPair();
			}
			current.incrementReferenceCount();
			countMap.put(name, current);
		}
		return true;
	}

	@Override
	public boolean visit(SimpleType node) {
		ITypeBinding bind = node.resolveBinding();

		String name;
		if (bind != null) {
			name = bind.getQualifiedName();
		} else {
			name = node.getName().getFullyQualifiedName();
		}
		countPair current = countMap.get(name);
		if (current == null) {
			current = new countPair();
		}
		current.incrementReferenceCount();
		countMap.put(name, current);

		return true;
	}

	@Override
	public boolean visit(SingleMemberAnnotation node) {
		ITypeBinding bind = node.resolveTypeBinding();

		String name;
		if (bind != null) {
			name = bind.getQualifiedName();
		} else {
			name = node.getTypeName().getFullyQualifiedName();
		}
		countPair current = countMap.get(name);
		if (current == null) {
			current = new countPair();
		}
		current.incrementReferenceCount();
		countMap.put(name, current);

		return true;
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		ITypeBinding bind = node.resolveBinding();

		String name = bind.getQualifiedName();
		if (name.equals("")) {
			name = bind.getName();
		}
		countPair current = countMap.get(name);
		if (current == null) {
			current = new countPair();
		}
		current.incrementDeclarationCount();
		countMap.put(name, current);

		return true;
	}
}
