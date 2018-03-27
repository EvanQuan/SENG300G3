package main.ast;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMemberValuePairBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.SimpleName;

public class TypeVisitorI1G2 extends ASTVisitor {
	private int typeDeclarationCount;
	private int typeReferenceCount;
	private String javaType;
	private String packageName;

	public TypeVisitorI1G2(String javaType) {
		this.javaType = javaType;
	}

	// Too potato to work with
	// Can't be bothered
	@Override
	public boolean visit(SimpleName node) {
		IBinding binding = node.resolveBinding();
		BindingsResolve(binding, node, node.getFullyQualifiedName());
		try {
			binding.getKind();
		} catch (Exception e) {

			if ((node.getFullyQualifiedName().equals(javaType)) && (node.isDeclaration() == false)) {
				typeReferenceCount++;
			} else if ((node.getFullyQualifiedName().equals(javaType)) && (node.isDeclaration() == true)) {
				typeDeclarationCount++;
			}
		}
		return true;
	}

	@Override
	public boolean visit(ParameterizedType node) {
		node.typeArguments().size();
		for (int i = 0; i < node.typeArguments().size(); i++) {
			if (node.typeArguments().get(i).toString().equals(javaType)) {
				typeReferenceCount++;
			} else if (("java.lang." + node.typeArguments().get(i).toString()).equals(javaType)) {
				typeReferenceCount++;
			}
		}
		return true;
	}

	@Override
	public boolean visit(AnnotationTypeDeclaration node) {
		IBinding binding = node.resolveBinding();
		if (node.getName().equals(javaType)) {
			typeDeclarationCount++;
		}
		binding.getKind();
		if (node.structuralPropertiesForType().toString().equals(javaType)) {
			// implementation in future iterations
		}
		return true;
	}

	@Override
	public boolean visit(EnumDeclaration node) {
		IBinding binding = node.resolveBinding();
		BindingsResolve(binding, node, node.getName().toString());
		return true;
	}

	// Resolves bindings of methods such that they are sorted into their respective
	// types and logic is applied to return correct counter increases
	private void BindingsResolve(IBinding aBinding, ASTNode aNode, String aName) {
		// node analysis used in some versions of logic and might be reimplemented later
		// ASTNode node = aNode;
		IBinding binding = aBinding;
		String nodeName = aName;
		if (binding instanceof IVariableBinding) {
			IVariableBinding varBinding = (IVariableBinding) binding;
			ITypeBinding declaringType = varBinding.getDeclaringClass();
			String[] fullyqualifiedArray = varBinding.toString().split(" ");
			for (int i = 0; i < fullyqualifiedArray.length; i++) {
				if (fullyqualifiedArray[i].equals(javaType)) {
					typeReferenceCount++;
				}
			}
		} else if (binding instanceof IPackageBinding) {
			IPackageBinding packBinding = (IPackageBinding) binding;
			String packBindings = packBinding.getName();
			packageName = packBindings;
			if (packBinding.getName().equals(javaType)) {
				typeDeclarationCount++;
			}
		} else if (binding instanceof ITypeBinding) {
			ITypeBinding typeBinding = (ITypeBinding) binding;
			if ((typeBinding.getQualifiedName().equals(javaType))
					&& (((javaType.equals(nodeName))) || ((packageName + "." + nodeName).equals(javaType))
							|| ((typeBinding.isInterface()) || (typeBinding.isNested())))) {
				typeDeclarationCount++;
			} else {
				// Used for testing purposes
			}
		} else if (binding instanceof IMethodBinding) {
			IMethodBinding methodBinding = (IMethodBinding) binding;
			ITypeBinding[] paramTypes = methodBinding.getParameterTypes();
			int i;
			for (i = 0; i < paramTypes.length; i++) {
				if (paramTypes[i].toString().equals(javaType)) {
					typeReferenceCount++;
				}
			}
		} else if (binding instanceof IAnnotationBinding) {
			IAnnotationBinding annotBinding = (IAnnotationBinding) binding;
			String nameOfAnnot = annotBinding.getName();
		} else if (binding instanceof IMemberValuePairBinding) {
			// possibly implemented in future iterations
		} else {
			// System.out.println("Edge case reached. Bindings resovled but not a resolved
			// type" );
		}
	}

	// Get type declaration count
	public int typeDecCount() {
		return typeDeclarationCount;
	}

	// Get type reference count
	public int typeRefCount() {
		return typeReferenceCount;
	}

}
