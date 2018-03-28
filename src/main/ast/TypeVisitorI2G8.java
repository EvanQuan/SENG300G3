package main.ast;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class TypeVisitorI2G8 extends ASTVisitor {

	public HashMap<String, Integer> logReferences = new HashMap<>();
	public HashMap<String, Integer> logDeclarations = new HashMap<>();

	@Override
	public boolean visit(AnnotationTypeDeclaration node) {

		if (logDeclarations.containsKey(node.resolveBinding().getQualifiedName())) {
			logDeclarations.put(node.resolveBinding().getQualifiedName().toString(),
					logDeclarations.get(node.resolveBinding().getQualifiedName().toString()) + 1);
		} else {
			logDeclarations.put(node.resolveBinding().getQualifiedName().toString(), 1);
		}
		return true;
	}

	@Override
	public boolean visit(AnonymousClassDeclaration node) {

		if (logReferences.containsKey(node.resolveBinding().getQualifiedName())) {
			logReferences.put(node.resolveBinding().getQualifiedName().toString(),
					logReferences.get(node.resolveBinding().getQualifiedName().toString()) + 1);
		} else {
			logReferences.put(node.resolveBinding().getQualifiedName().toString(), 1);
		}
		return true;
	}

	@Override
	public boolean visit(ArrayType node) {

		if (logReferences.containsKey(node.resolveBinding().getQualifiedName())) {
			logReferences.put(node.resolveBinding().getQualifiedName().toString(),
					logReferences.get(node.resolveBinding().getQualifiedName().toString()) + 1);
		} else {
			logReferences.put(node.resolveBinding().getQualifiedName().toString(), 1);
		}
		return true;
	}

	@Override
	public boolean visit(EnumDeclaration node) {

		if (logDeclarations.containsKey(node.resolveBinding().getQualifiedName())) {
			logDeclarations.put(node.resolveBinding().getQualifiedName().toString(),
					logDeclarations.get(node.resolveBinding().getQualifiedName().toString()) + 1);
		} else {
			logDeclarations.put(node.resolveBinding().getQualifiedName().toString(), 1);
		}
		return true;
	}

	@Override
	public boolean visit(MarkerAnnotation node) {

		if (logReferences.containsKey(node.resolveAnnotationBinding().getName())) {
			logReferences.put(node.resolveAnnotationBinding().getName().toString(),
					logReferences.get(node.resolveAnnotationBinding().getName().toString()) + 1);
		} else {
			logReferences.put(node.resolveAnnotationBinding().getName().toString(), 1);
		}
		return true;
	}

	@Override
	public boolean visit(NormalAnnotation node) {

		if (logReferences.containsKey(node.resolveAnnotationBinding().getName())) {
			logReferences.put(node.resolveAnnotationBinding().getName().toString(),
					logReferences.get(node.resolveAnnotationBinding().getName().toString()) + 1);
		} else {
			logReferences.put(node.resolveAnnotationBinding().getName().toString(), 1);
		}
		return true;
	}

	@Override
	public boolean visit(PrimitiveType node) {

		if (!(node.resolveBinding().toString().equals("void"))) {
			if (logReferences.containsKey(node.resolveBinding().getQualifiedName())) {
				logReferences.put(node.resolveBinding().getQualifiedName().toString(),
						logReferences.get(node.resolveBinding().getQualifiedName().toString()) + 1);
			} else {
				logReferences.put(node.resolveBinding().getQualifiedName().toString(), 1);
			}
		}
		return true;
	}

	@Override
	public boolean visit(SimpleType node) {

		if (logReferences.containsKey(node.resolveBinding().getQualifiedName())) {
			logReferences.put(node.resolveBinding().getQualifiedName().toString(),
					logReferences.get(node.resolveBinding().getQualifiedName().toString()) + 1);
		} else {
			logReferences.put(node.resolveBinding().getQualifiedName().toString(), 1);
		}
		return true;
	}

	@Override
	public boolean visit(TypeDeclaration node) {

		if (logDeclarations.containsKey(node.resolveBinding().getQualifiedName())) {
			logDeclarations.put(node.resolveBinding().getQualifiedName().toString(),
					logDeclarations.get(node.resolveBinding().getQualifiedName().toString()) + 1);
		} else {
			logDeclarations.put(node.resolveBinding().getQualifiedName().toString(), 1);
		}
		return true;
	}
}
