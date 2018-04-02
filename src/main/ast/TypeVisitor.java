package main.ast;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IPackageBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import main.util.Multiset;

/**
 * A visitor for abstract syntax trees. For each different concrete AST node
 * type T, the visitor will locate the different java types present in the
 * source code, and count the number of declarations of references for each of
 * the java types present.
 *
 * @author Evan Quan
 * @version 3.2.0
 * @since 29 March 2018
 */
public class TypeVisitor extends ASTVisitor {

	private boolean debug;
	private ArrayList<String> types;
	private Multiset<String> declarations;
	private Multiset<String> anonymous;
	private Multiset<String> local;
	private Multiset<String> nested;
	private Multiset<String> references;
	private String packageName;
	private ArrayList<String> importedNames;

	/**
	 * Checks if the passed type already exists within the types list. [false -> add
	 * type to list create entry <type, 0> in decCounter create entry <type, 0> in
	 * refCounter] [true -> do nothing]
	 *
	 * @param type:
	 *            String, java type
	 */
	private void addTypeToList(String type) {
		if (!types.contains(type)) {
			types.add(type);
		}
	}

	/**
	 * Increment the declaration count for a given type.
	 *
	 * @param type
	 *            String, java type
	 */
	private void incrementDeclaration(String type) {
		// Check if the type exists, then increment their associated value by 1
		addTypeToList(type);
		declarations.add(type);
	}

	/**
	 * Increment the anonymous declaration count for a given type.
	 * 
	 * @param type
	 */
	private void incrementAnonymous(String type) {
		anonymous.add(type);
	}

	/**
	 * Increment the local declaration count for a given type.
	 * 
	 * @param type
	 */
	private void incrementLocal(String type) {
		addTypeToList(type);
		local.add(type);
	}

	/**
	 * Increment the nested declaration count for a given type.
	 * 
	 * @param type
	 */
	private void incrementNested(String type) {
		addTypeToList(type);
		nested.add(type);
	}

	/**
	 * Increment the reference count for a given type.
	 *
	 * @param type
	 *            String, java type
	 */
	private void incrementReference(String type) {
		// Check if the type exists, then increment their associated value by 1
		addTypeToList(type);
		references.add(type);
	}

	/*
	 * ========================= DEBUG FUNCTIONS =========================
	 */
	private void debug(Object message) {
		if (debug) {
			System.out.println(message);
		}
	}

	private void debug(Object node, Object type) {
		if (debug) {
			System.out.println("Node: " + node + " | Type: " + type);
		}
	}
	/*
	 * ========================= HELPER FUNCTIONS =========================
	 */

	/**
	 * Default constructor. Initialize the list of types, and the HashMaps for the
	 * counters to null.
	 */
	public TypeVisitor() {
		this(false); // Debug is false
	}

	/**
	 * Debug constructor.
	 * 
	 * @param debug
	 */
	public TypeVisitor(boolean debug) {
		this.debug = debug;
		this.types = new ArrayList<String>();
		this.declarations = new Multiset<String>();
		this.anonymous = new Multiset<String>();
		this.local = new Multiset<String>();
		this.nested = new Multiset<String>();
		this.references = new Multiset<String>();
		this.importedNames = new ArrayList<String>();
	}

	/**
	 * Get count of all declarations found.
	 *
	 * @return declarations
	 */
	public Multiset<String> getDeclarations() {
		return declarations;
	}

	/**
	 * Get count of all anonymous declarations found.
	 *
	 * @return declarations
	 */
	public Multiset<String> getAnonymous() {
		return anonymous;
	}

	/**
	 * Get count of all local declarations found.
	 *
	 * @return declarations
	 */
	public Multiset<String> getLocal() {
		return local;
	}

	/**
	 * Get count of all nested declarations found.
	 *
	 * @return declarations
	 */
	public Multiset<String> getNested() {
		return nested;
	}

	/**
	 * Get list of all types found.
	 *
	 * @return types
	 */
	public ArrayList<String> getTypes() {
		return types;
	}

	/**
	 * Get count of all references found.
	 *
	 * @return references
	 */
	public Multiset<String> getReferences() {
		return references;
	}

	/**
	 * Add hoc solution. Call before every time visitor is accepted. TODO IS there a
	 * better way to do this?
	 */
	public void resetToNewFile() {
		// Imported names accumulate within a file, but not between files
		importedNames.clear();
	}

	/**
	 * @return the total number of declarations made
	 */
	public int getDeclarationCount() {
		return declarations.getElementCount() + anonymous.getElementCount();
	}

	/*
	 * ========================== ASTVisitor FUNCTIONS ==========================
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
	@Override // SAME
	public boolean visit(AnnotationTypeDeclaration node) {
		ITypeBinding typeBind = node.resolveBinding();
		String type = typeBind.getQualifiedName();

		debug("AnnotationTypeDeclaration", type);
		incrementDeclaration(type);

		return true;
	}

	/**
	 * Visits an Array reference Foo[]
	 * 
	 * @param node
	 * @return true to visit the children of this node
	 */
	@Override
	public boolean visit(ArrayType node) {
		ITypeBinding typeBind = node.resolveBinding();
		String type = typeBind.getQualifiedName();

		debug("ArrayType", type);
		incrementReference(type);

		return true;
	}

	// TODO return here
	@Override
	public boolean visit(PrimitiveType node) {
		ITypeBinding typeBind = node.resolveBinding();
		String type = typeBind.getQualifiedName();

		// void is not a primitive type
		if (!type.equals("void")) {
			debug("PrimitiveType", type);
			incrementReference(type);
		}
		return true;
	}

	// TODO what is this for? Example of QualifiedType
	// @Override
	// public boolean visit(QualifiedType node) {
	// ITypeBinding typeBind = node.resolveBinding();
	// String type = typeBind.getQualifiedName();
	//
	// debug("QualifiedType", type);
	// incrementReference(type);
	//
	// return true;
	// }

	/**
	 * Used to detect static field calls. type: Class.field qualifier: Class
	 */
	@Override
	public boolean visit(QualifiedName node) {
		String type = node.getFullyQualifiedName();
		debug("QualifiedName", type);
		debug("\tnode.getName(): " + node.getName());
		debug("\tnode.getFullyQualifiedName(): " + node.getFullyQualifiedName());
		debug("\tnode.getQualifier(): " + node.getQualifier());

		// RETURN HERE
		Name qualifier = node.getQualifier();
		String qualifierName = qualifier.getFullyQualifiedName();
		if (!qualifier.isQualifiedName()) {
			qualifierName = appendPackageName(qualifierName);
		}
		debug("\tname qualified: " + qualifierName);

		ASTNode parent = node.getParent();
		Class<? extends ASTNode> parentNode = parent.getClass();
		String parentNodeName = parentNode.getSimpleName();

		debug("\tParent: " + parentNodeName);
		//
		// Check parent.
		// VariableDeclarationFragment means staticField returned
		// Assignment means static field set
		if (parentNode.equals(VariableDeclarationFragment.class) || parentNode.equals(Assignment.class)) {
			incrementReference(qualifierName);
		}
		return true;
	}

	/**
	 * Detects static method calls
	 */
	@Override
	public boolean visit(SimpleName node) {
		ASTNode parent = node.getParent();
		Class<? extends ASTNode> parentNode = parent.getClass();
		String parentNodeName = parentNode.getSimpleName();

		// Check parent.
		// MethodInvocation means staticMethod is called
		if (parentNode.equals(MethodInvocation.class)) {
			String type = node.getFullyQualifiedName();
			type = appendPackageName(type);
			debug("SimpleName", type);
			debug("\tParent" + parentNodeName);
			incrementReference(type);
		}
		return true;
	}

	@Override
	public void preVisit(ASTNode node) {
		// debug("\n\nPREVISIT");
	}

	@Override
	public void postVisit(ASTNode node) {
		// debug("POSTVISIT");
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

		debug("EnumDeclaration", type);
		incrementDeclaration(type);

		return true;
	}

	@Override
	public boolean visit(PackageDeclaration node) {
		IPackageBinding packageBind = node.resolveBinding();
		packageName = packageBind.getName();
		debug("PackageDeclaration", packageName);
		return true;
	}

	/**
	 * Append the current package name to name if it exists
	 * 
	 * @param name
	 * @return
	 */
	private String appendPackageName(String name) {
		debug("\tBEFORE APPEND:" + name);
		// Do not append package name if in default package
		// Do not append package name if imported from default package
		// NOTE: It is not valid Java syntax to import from the default package, but
		// this must be considered anyways.
		if ((packageName != null && !name.contains(".")) && !importedNames.contains(name)) {
			name = packageName + "." + name;
		}
		debug("\tAFTER APPEND:" + name);
		return name;
	}

	/**
	 * Track anonymous class declarations Note that this does not increment regular
	 * declaration count
	 */
	@Override
	public boolean visit(AnonymousClassDeclaration node) {
		ITypeBinding typeBind = node.resolveBinding();
		String name = typeBind.getQualifiedName();
		debug("AnonymousClassDeclaration", name);
		incrementAnonymous(name);
		return true;
	}

	/**
	 * Gets most things
	 */
	@Override
	public boolean visit(SimpleType node) {
		ITypeBinding typeBind = node.resolveBinding();
		String nameQualified;
		// Strips parameterized generics off
		nameQualified = typeBind.getTypeDeclaration().getQualifiedName();
		String nameSimple = typeBind.getTypeDeclaration().getName();

		// Add package name if does not contain package name and not in default package
		// If imported from default package (which is not valid Java syntax), it will
		// count
		// as a reference to the default package and not append the current package name
		IPackageBinding packBind = typeBind.getPackage();
		String packName = packBind.getName();

		// If SimpleType was imported from the default package, increment simple name
		debug("Imports: " + importedNames);
		if (importedNames.contains(nameSimple)) {
			incrementReference(nameSimple);
			debug("SimpleType simple", nameSimple);
			return true;
		} else if (!nameQualified.contains(".") && packName.length() > 0) {
			nameQualified = packName + "." + nameQualified;
		}

		incrementReference(nameQualified);
		debug("SimpleType qualified", nameQualified);

		return true;
	}

	/**
	 * import bar.Foo; Gets "bar.Foo"
	 * 
	 * import C; Gets "C" (even though you cannot legally import from the default
	 * package)
	 * 
	 * import bar.*; Gets nothing
	 */
	@Override
	public boolean visit(ImportDeclaration node) {
		if (node.getName().resolveTypeBinding() != null) {
			String type = node.getName().toString();
			// Importing wildcard (eg. import bar.*) will return only package name (bar).
			// Since we want a fully qualified class name, we reject only package name.
			if (type.contains(".") || !type.contains("*")) {
				debug("ImportDeclaration", type);
				incrementReference(type);
				importedNames.add(type);
			}
		}

		return true;
	}

	/**
	 * Visits a Marker annotation node type. Marker annotation "@<typeName>" is
	 * equivalent to normal annotation "@<typeName>()"
	 *
	 * Determine the type of annotation, add it to types, and increment its type's
	 * counter value in refCounter.
	 *
	 * CounterType: REFERENCE
	 *
	 * @param node
	 *            MarkerAnnotation
	 * @return boolean : True to visit the children of this node
	 *
	 *         TODO: Cannot recognize full qualified names for IMPORTS. Works for
	 *         java.lang.* e.g. @Test from org.junit.Test appears as
	 *         <currentPackage>.Test
	 */
	@Override
	public boolean visit(MarkerAnnotation node) {
		IAnnotationBinding annBind = node.resolveAnnotationBinding();
		ITypeBinding typeBind = annBind.getAnnotationType();
		String type = typeBind.getQualifiedName();

		debug("MarkerAnnotation", type);
		incrementReference(type);

		return true;
	}

	/**
	 * Visits normal annotation AST node type. @ TypeName ( [ MemberValuePair { ,
	 * MemberValuePair } ] )
	 *
	 * Determine the typename of the normal annotation, add it to the types, and
	 * increment the type's counter in refCounter.
	 *
	 * This also goes into the MemberValuePair, and for all TypeLiterals, the type
	 * is recorded, and its reference counter incremented
	 *
	 * CounterType: Reference
	 *
	 * @param node
	 *            NormalAnnotation
	 * @return boolean true to visit its children nodes
	 */
	// @Override
	@Override
	public boolean visit(NormalAnnotation node) {
		IAnnotationBinding annBind = node.resolveAnnotationBinding();
		ITypeBinding typeBind = annBind.getAnnotationType();
		String type = typeBind.getQualifiedName();

		debug("NormalAnnotation", type);
		incrementReference(type);

		return true;
	}

	@Override
	public boolean visit(SingleMemberAnnotation node) {
		IAnnotationBinding annBind = node.resolveAnnotationBinding();
		ITypeBinding typeBind = annBind.getAnnotationType();
		String type = typeBind.getQualifiedName();

		debug("SingleMemberAnnotation", type);
		incrementReference(type);

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
		ITypeBinding typeBind = node.resolveBinding();
		String type = typeBind.getQualifiedName();

		// Local classes do not have qualified names, only simple names
		if (type.equals("")) {
			type = typeBind.getTypeDeclaration().getName();
			debug("TypeDeclaration LOCAL", type);
			incrementLocal(type);
		} else {

			// Nested classes have at least 1 parent node as a TypeDeclaration
			// TODO Can a type be both local and nested?
			while (true) {
				ASTNode parent = node.getParent();
				Class<? extends ASTNode> parentNode = parent.getClass();
				String parentNodeName = parentNode.getSimpleName();

				debug("\tParent: " + parentNodeName);

				if (parentNode.equals(TypeDeclaration.class)) {
					debug("TypeDeclaration NESTED", type);
					incrementNested(type);
					break;
				} else if (parentNode.equals(CompilationUnit.class)) {
					debug("TypeDeclaration", type);
					break;
				}
			}

		}

		incrementDeclaration(type);
		return true;
	}
}
