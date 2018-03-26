package test;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

/**
 * Iteration 1 Group 7 TypeVisitor
 *
 */
public class TypeVisitorI1G7 extends ASTVisitor {

	public int declarationCount = 0;
	public int referenceCount = 0;
	public ASTNode rootNode;
	public String qualTypeToFind;

	public TypeVisitorI1G7(ASTNode root, String type) {

		this.rootNode = root;
		this.qualTypeToFind = type;
	}

	@Override
	public boolean visit(TypeDeclaration node) {

		String binding = node.resolveBinding().getQualifiedName();
		// System.out.println(binding);
		if (binding.equals(this.qualTypeToFind)) {

			this.declarationCount++;
		}
		return true;
	}

	@Override
	public boolean visit(EnumDeclaration node) {

		String binding = node.resolveBinding().getQualifiedName();
		// System.out.println(binding);
		if (binding.equals(this.qualTypeToFind)) {

			this.declarationCount++;
		}
		return true;
	}

	@Override
	public boolean visit(AnnotationTypeDeclaration node) {

		String binding = node.resolveBinding().getQualifiedName();
		if (binding.equals(this.qualTypeToFind)) {

			this.declarationCount++;
		}
		return true;
	}

	@Override
	public boolean visit(MarkerAnnotation node) {

		String binding = node.resolveAnnotationBinding().getAnnotationType().getQualifiedName();
		System.out.println(binding);
		if (binding.equals(this.qualTypeToFind)) {

			this.referenceCount++;
		}
		return true;
	}

	@Override
	public boolean visit(AnnotationTypeMemberDeclaration node) {
		String binding = node.getType().resolveBinding().getQualifiedName();
		if (binding.equals(this.qualTypeToFind)) {

			this.referenceCount++;
		}
		return true;
	}

	@Override
	public boolean visit(NormalAnnotation node) {

		String binding = node.resolveAnnotationBinding().getAnnotationType().getQualifiedName();
		System.out.println(binding);
		if (binding.equals(this.qualTypeToFind)) {

			this.referenceCount++;
		}
		return true;
	}

	@Override
	public boolean visit(SingleMemberAnnotation node) {
		String binding = node.resolveAnnotationBinding().getAnnotationType().getQualifiedName();
		System.out.println(binding);
		if (binding.equals(this.qualTypeToFind)) {

			this.referenceCount++;
		}
		return true;
	}

	@Override
	public boolean visit(FieldDeclaration node) {
		String binding = node.getType().resolveBinding().getQualifiedName();
		// System.out.println(binding);
		if (binding.equals(this.qualTypeToFind)) {

			this.referenceCount++;
		}
		return true;
	}

	@Override
	public boolean visit(ClassInstanceCreation node) {
		String binding = node.getType().resolveBinding().getQualifiedName();
		// System.out.println(binding);
		if (binding.equals(this.qualTypeToFind)) {

			this.referenceCount++;
		}
		return true;
	}

	@Override
	public boolean visit(VariableDeclarationStatement node) {
		String binding = node.getType().resolveBinding().getQualifiedName();
		// System.out.println(binding);
		if (binding.equals(this.qualTypeToFind)) {

			this.referenceCount++;
		}
		return true;
	}

	public int getDeclarationCount() {
		return declarationCount;
	}

	public void setDeclarationCount(int declarationCount) {
		this.declarationCount = declarationCount;
	}

	public int getReferenceCount() {
		return referenceCount;
	}

	public void setReferenceCount(int referenceCount) {
		this.referenceCount = referenceCount;
	}

	public ASTNode getRootNode() {
		return rootNode;
	}

	public void setRootNode(ASTNode rootNode) {
		this.rootNode = rootNode;
	}

	public String getQualTypeToFind() {
		return qualTypeToFind;
	}

	public void setQualTypeToFind(String qualTypeToFind) {
		this.qualTypeToFind = qualTypeToFind;
	}

}
