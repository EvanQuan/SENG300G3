package test;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

/**
 * Iteration 1: Group 11
 *
 */
public class TypeVisitorI1G11 extends ASTVisitor {

	public int declarationCount;
	public int referenceCount;
	public String javaType;

	public TypeVisitorI1G11(String type) {
		declarationCount = 0;
		referenceCount = 0;
		javaType = type;
	}

	// finds when class or interface
	@Override
	public boolean visit(TypeDeclaration node) {
		SimpleName name = node.getName();

		// bindings to get the qualified name
		ITypeBinding bind = node.resolveBinding();
		if (bind.getQualifiedName().equalsIgnoreCase(javaType)) {
			declarationCount++;
		}
		return true;
	}

	// finds when enum type declared
	@Override
	public boolean visit(EnumDeclaration node) {
		SimpleName name = node.getName();

		// bindings to get the qualified name
		ITypeBinding bind = node.resolveBinding();
		if (bind.getQualifiedName().equalsIgnoreCase(javaType)) {
			declarationCount++;
		}
		return true;
	}

	// finds when annotation type declared
	@Override
	public boolean visit(AnnotationTypeDeclaration node) {
		SimpleName name = node.getName();

		// binding issues, gets a NullPointerException
		/*
		 * ITypeBinding bind = node.resolveTypeBinding(); if
		 * (bind.getQualifiedName().equalsIgnoreCase(javaType)) { declarationCount++; }
		 */

		if (name.toString().equalsIgnoreCase(javaType)) {
			declarationCount++;
		}
		return true;
	}

	// finds reference of annotation type
	@Override
	public boolean visit(MarkerAnnotation node) {
		Name name = node.getTypeName();

		// binding issues, gets a NullPointerException
		/*
		 * ITypeBinding bind = node.resolveTypeBinding(); if
		 * (bind.getQualifiedName().equalsIgnoreCase(javaType)) { referenceCount++; }
		 */

		if (name.toString().equalsIgnoreCase(javaType)) {
			referenceCount++;
		}
		return true;
	}

	// finds reference of class: eg. A a = new A(); would be 2 references of A
	@Override
	public boolean visit(SimpleType node) {
		Name name = node.getName();

		// bindings to get the qualified name
		ITypeBinding bind = node.resolveBinding();
		if (bind.getQualifiedName().equalsIgnoreCase(javaType)) {
			referenceCount++;
			System.out.println(javaType + " Declaration: " + declarationCount + " Reference: " + referenceCount);
		}
		return true;
	}

	// finds reference of annotation type
	@Override
	public boolean visit(SingleMemberAnnotation node) {
		Name name = node.getTypeName();

		// binding issues, gets a NullPointerException
		/*
		 * ITypeBinding bind = node.resolveTypeBinding(); if
		 * (bind.getQualifiedName().equalsIgnoreCase(javaType)) { referenceCount++; }
		 */

		if (name.toString().equalsIgnoreCase(javaType)) {
			referenceCount++;
		}
		return true;
	}

	// finds reference of annotation type
	@Override
	public boolean visit(NormalAnnotation node) {
		Name name = node.getTypeName();

		// binding issues, gets a NullPointerException
		ITypeBinding bind = node.resolveTypeBinding();
		if (bind.getQualifiedName().equalsIgnoreCase(javaType)) {
			referenceCount++;
		}
		return true;
	}

	@Override
	public boolean visit(VariableDeclarationFragment node) {
		SimpleName name = node.getName();

		// binding to get qualified name
		ITypeBinding bind = name.resolveTypeBinding();
		String qualifiedName = bind.getQualifiedName();
		// check if it is a primitive type
		if (qualifiedName.equalsIgnoreCase("int") || qualifiedName.equalsIgnoreCase("short")
				|| qualifiedName.equalsIgnoreCase("byte") || qualifiedName.equalsIgnoreCase("boolean")
				|| qualifiedName.equalsIgnoreCase("long") || qualifiedName.equalsIgnoreCase("char")
				|| qualifiedName.equalsIgnoreCase("float") || qualifiedName.equalsIgnoreCase("double")) {
			if (qualifiedName.equalsIgnoreCase(javaType)) {
				referenceCount++;
			}
		}

		return true;
	}

}
