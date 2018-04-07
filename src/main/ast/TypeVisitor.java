package main.ast;

import java.util.ArrayList;
import java.util.Map;

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
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import main.util.Multiset;

/**
 * A visitor for abstract syntax trees. For each different concrete AST node
 * type T, the visitor will locate the different java types present in the
 * source code, and count the number of declarations of references for each of
 * the java types present.
 *
 * It also tracks where declarations are anonymous, local, or nested, and if
 * references are local or nested.
 *
 * @author Evan Quan
 * @version 3.11.0
 * @since 5 April 2018
 */
public class TypeVisitor extends ASTVisitor {

	// Debug
	private boolean debug;

	private ArrayList<String> types;
	// Declarations
	private Multiset<String> namedDeclarations;
	private Multiset<String> anonymousDeclarations;
	private Multiset<String> localDeclarations;
	private Multiset<String> nestedDeclarations;
	// References
	private Multiset<String> references;
	private Multiset<String> localReferences;
	private Multiset<String> nestedReferences;

	private String packageName;
	private ArrayList<String> importedNames;
	private ArrayList<String> importedNamesSimple;

	/**
	 * Default constructor. Initialize the list of types, and the HashMaps for the
	 * counters to null.
	 */
	public TypeVisitor() {
		this(false);
	}

	/**
	 * Debug constructor.
	 *
	 * @param debugPrintStream.
	 *            The visitor prints to its own PrintStream
	 */
	public TypeVisitor(boolean debug) {
		this.debug = debug;
		this.types = new ArrayList<String>();
		this.namedDeclarations = new Multiset<String>();
		this.anonymousDeclarations = new Multiset<String>();
		this.localDeclarations = new Multiset<String>();
		this.nestedDeclarations = new Multiset<String>();
		this.references = new Multiset<String>();
		this.localReferences = new Multiset<String>();
		this.nestedReferences = new Multiset<String>();
		this.importedNames = new ArrayList<String>();
		this.importedNamesSimple = new ArrayList<String>();
		packageName = null;
	}

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

	/*
	 * ========================= DEBUG FUNCTIONS =========================
	 */
	private void debug(ASTNode node, String message) {
		if (debug) {
			System.out.println("===" + node.getClass().getSimpleName() + "===  (" + message + ")");
		}
	}

	private void debug(Object message) {
		if (debug) {
			System.out.println("\t" + message);
		}
	}

	/*
	 * ========================= HELPER FUNCTIONS =========================
	 */

	/**
	 * Get all anonymous declarations found.
	 *
	 * @return declarations
	 */
	public Multiset<String> getAnonymousDeclarations() {
		return anonymousDeclarations;
	}

	/**
	 * @return the total number of declarations made (named declarations + anonymous
	 *         declarations)
	 */
	public int getDeclarationCount() {
		return namedDeclarations.getElementCount() + anonymousDeclarations.getElementCount();
	}

	/**
	 * Get all local declarations found.
	 *
	 * @return declarations
	 */
	public Multiset<String> getLocalDeclarations() {
		return localDeclarations;
	}

	/**
	 * Get local references found
	 *
	 * @return
	 */
	public Multiset<String> getLocalReferences() {
		return localReferences;
	}

	/**
	 * Get all named declarations found.
	 *
	 * @return declarations
	 */
	public Multiset<String> getNamedDeclarations() {
		return namedDeclarations;
	}

	/**
	 * Get count of all nested declarations found.
	 *
	 * @return declarations
	 */
	public Multiset<String> getNestedDeclarations() {
		return nestedDeclarations;
	}

	/**
	 * Get all nested references found
	 *
	 * @return
	 */
	public Multiset<String> getNestedReferences() {
		return nestedReferences;
	}

	/**
	 * Get all references found.
	 *
	 * @return references
	 */
	public Multiset<String> getReferences() {
		return references;
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
	 * Increment the anonymous declaration count for a given type.
	 *
	 * @param type
	 */
	private void incrementAnonymousDeclaration(String type) {
		anonymousDeclarations.add(type);
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
		namedDeclarations.add(type);
	}

	/**
	 * Increment the local declaration count for a given type.
	 *
	 * @param type
	 */
	private void incrementLocalDeclaration(String type) {
		addTypeToList(type);
		localDeclarations.add(type);
	}

	/**
	 * Increment the local reference count for a given type
	 *
	 * @param type
	 */
	private void incrementLocalReference(String type) {
		addTypeToList(type);
		localReferences.add(type);
	}

	/**
	 * Increment the nested declaration count for a given type.
	 *
	 * @param type
	 */
	private void incrementNestedDeclaration(String type) {
		addTypeToList(type);
		nestedDeclarations.add(type);
	}

	/**
	 * Increment the nested reference count for a given type
	 *
	 * @param type
	 */
	private void incrementNestedReference(String type) {
		addTypeToList(type);
		nestedReferences.add(type);
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
	 * ========================== ASTVisitor FUNCTIONS ==========================
	 */

	@Override
	public void postVisit(ASTNode node) {
		// debug("POSTVISIT");
	}

	@Override
	public void preVisit(ASTNode node) {
		// debug("\n\nPREVISIT");
	}

	/**
	 * Add hoc solution. Call before every time visitor is accepted. TODO IS there a
	 * better way to do this?
	 */
	public void resetToNewFile() {
		// Imported names accumulate within a file, but not between files
		importedNames.clear();
		importedNamesSimple.clear();
		packageName = null;
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
		debug(node, "Annotation type declarations");
		ITypeBinding typeBind = node.resolveBinding();
		String nameQualified = typeBind.getQualifiedName();

		debug("Added: " + nameQualified);
		incrementDeclaration(nameQualified);

		return true;
	}

	/**
	 * Track anonymous class declarations Note that this does not increment regular
	 * declaration count
	 */
	@Override
	public boolean visit(AnonymousClassDeclaration node) {
		
		debug(node, "Anonymous class declarations");
		ITypeBinding typeBind = node.resolveBinding();
		
		if(typeBind == null) {
			return true;
		}
		
		String namedQualified = typeBind.getQualifiedName();
		debug("Added: " + namedQualified + " (should be empty string)");
		incrementAnonymousDeclaration(namedQualified);
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
		debug("===ArrayType===");
		debug(node, "Nested array references");
		ITypeBinding typeBind = node.resolveBinding();
		
		if(typeBind == null) {
			return true;
		}
		
		
		
		String nameQualified = typeBind.getQualifiedName();
		String nameSimple = typeBind.getName();
		debug("nameQualified: " + nameQualified);
		debug("nameSimple: " + nameSimple);

		// If local array type, then BOTH nameQualified and nameSimple are empty string
		// To get local array type reference, start from SimpleType and find ArrayType
		// parent
		// CHECK OUT SimpleType node
		if (nameQualified.equals("")) {
			// If local array, then skip this node to not add empty string reference
			debug("Local array found. Skipped. Dealt with in SimpleType node.");
			return true;
		}

		// If nested array type, then the nameQualified (stripping off the "[]") should
		// be in the nested declarations
		String nameQualifiedStrip = nameQualified.substring(0, nameQualified.length() - 2);
		if (nestedDeclarations.contains(nameQualifiedStrip)) {
			incrementNestedReference(nameQualified);
			debug("Added nested: " + nameQualified);
		}

		debug("Added: " + nameQualified);
		incrementReference(nameQualified);

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
		String nameQualified = typeBind.getQualifiedName();
		debug(node, "Enum declarations");
		debug("Added: " + nameQualified);
		incrementDeclaration(nameQualified);

		return true;
	}

	/**
	 * import bar.Foo; Gets "bar.Foo"
	 *
	 * import C; Gets "C" (even though you cannot legally import from the default
	 * package)
	 *
	 * import bar.*; Gets nothing
	 *
	 * TODO cannot deal with other.bar.*;
	 */
	@Override
	public boolean visit(ImportDeclaration node) {
		debug(node, "Imported type references");
		if (node.getName().resolveTypeBinding() != null) {
			String nameQualified = node.getName().toString(); // Original, does not include asterisks
			String nameSimple = nameQualified.substring(nameQualified.lastIndexOf('.') + 1);
			String importStatement = node.toString(); // May include asterisks
			debug("importStatement: " + importStatement);
			debug("nameQualified: " + nameQualified);
			debug("nameSimple: " + nameSimple);
			// Importing wildcard (eg. import bar.*) will return only package name (bar).
			// Since we want a fully qualified class name, we reject only package name.
			if (!importStatement.contains(".") || !importStatement.contains("*")) {
				debug("Added import class: " + nameQualified);
				incrementReference(nameQualified);
				importedNames.add(nameQualified);
				importedNamesSimple.add(nameSimple);
			} else {
				debug("Not added: " + nameQualified + " is not a reference to a class");
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
		debug(node, "Mark annotation references");
		IAnnotationBinding annBind = node.resolveAnnotationBinding();
		if(annBind == null) {
			return true;
		}
		
		
		ITypeBinding typeBind = annBind.getAnnotationType();
		String nameQualified = typeBind.getQualifiedName();
		String nameSimple = typeBind.getName();
		if (importedNamesSimple.contains(nameSimple)) {
			String nameQualifiedImported = importedNames.get(importedNamesSimple.indexOf(nameSimple));
			debug("Added imported reference: " + nameQualifiedImported);
			incrementReference(nameQualifiedImported);
		} else {
			debug("Added reference: " + nameQualified);
			incrementReference(nameQualified);
		}
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
		debug(node, "Normal annotation references");
		IAnnotationBinding annBind = node.resolveAnnotationBinding();
		ITypeBinding typeBind = annBind.getAnnotationType();
		String nameQualified = typeBind.getQualifiedName();
		String nameSimple = typeBind.getName();
		if (importedNamesSimple.contains(nameSimple)) {
			String nameQualifiedImported = importedNames.get(importedNamesSimple.indexOf(nameSimple));
			debug("Added imported reference: " + nameQualifiedImported);
			incrementReference(nameQualifiedImported);
		} else {
			debug("Added reference: " + nameQualified);
			incrementReference(nameQualified);
		}
		return true;
	}

	@Override
	public boolean visit(PackageDeclaration node) {
		IPackageBinding packageBind = node.resolveBinding();
		this.packageName = packageBind.getName();
		debug(node, "Set current package name. Does not add references");
		debug("Current package is: " + this.packageName);
		return true;
	}

	// TODO return here
	@Override
	public boolean visit(PrimitiveType node) {
		ITypeBinding typeBind = node.resolveBinding();
		
		if(typeBind == null) {
			return true;
		}
		
		
		
		
		String nameQualified = typeBind.getQualifiedName();
		debug(node, "Primtive type references");

		// void is not a primitive type
		if (!nameQualified.equals("void")) {
			debug("Added: " + nameQualified);
			incrementReference(nameQualified);
		} else {
			debug("Not added: void is not a primitive");
		}
		return true;
	}

	/**
	 * Used to detect static field calls only. type: Class.field qualifier: Class
	 */
	@Override
	public boolean visit(QualifiedName node) {
		debug(node, "Static field calls");
		String nameQualified = node.getFullyQualifiedName();
		debug("nameQualified" + nameQualified);
		debug("node.getName(): " + node.getName());
		debug("node.getFullyQualifiedName(): " + node.getFullyQualifiedName());
		debug("node.getQualifier(): " + node.getQualifier());

		// RETURN HERE
		Name qualifier = node.getQualifier();
		String qualifierName = qualifier.getFullyQualifiedName();
		if (!qualifier.isQualifiedName()) {
			qualifierName = appendPackageName(qualifierName);
		}
		debug("Qualifer name: " + qualifierName);

		ASTNode parent = node.getParent();
		Class<? extends ASTNode> parentNode = parent.getClass();
		String parentNodeName = parentNode.getSimpleName();

		debug("Parent: " + parentNodeName);

		// Check parent.
		// VariableDeclarationFragment means staticField returned
		// Assignment means static field set
		if (parentNode.equals(VariableDeclarationFragment.class) || parentNode.equals(Assignment.class)) {
			debug("Added static field call of class: " + qualifierName);
			incrementReference(qualifierName);
		} else {
			debug("Not added. " + qualifierName + " is not a class calling a static field");
		}
		return true;
	}

	/**
	 * Detects static method calls. Rejects normal method calls.
	 * https://help.eclipse.org/mars/index.jsp?topic=%2Forg.eclipse.jdt.doc.isv%2Freference%2Fapi%2Forg%2Feclipse%2Fjdt%2Fcore%2Fdom%2FSimpleName.html
	 */
	@Override
	public boolean visit(SimpleName node) {
		debug(node, "Static method call references");
		String nameQualified = node.getFullyQualifiedName();
		debug("nameQualified: " + nameQualified);
		// Determine if the name is a class or method name
		// Class if static method call
		// else method name
		ITypeBinding typeBinding = node.resolveTypeBinding();
		if (typeBinding != null) {
			// Reject. Following debug statements are just to clarify what is being
			// rejected.
			if (!typeBinding.isTypeVariable()) {
				debug("Not added. " + nameQualified + " is not a class calling a static method.");
			} else {
				debug("Not added. " + nameQualified + " is a method declaration, or a generic type.");
			}
		} else {
			// Can be either class calling static method or name of static method
			// Determine which one by finding parent
			String identifier = node.getIdentifier();
			int nodeType = node.getNodeType();
			int flags = node.getFlags();
			debug("nodeType: " + nodeType);
			debug("flags: " + flags);
			debug("Identifier: " + identifier);
			ASTNode parent = node.getParent();
			Map<?, ?> parentProperties = parent.properties();
			StructuralPropertyDescriptor spd = node.getLocationInParent();
			String id = spd.getId();
			debug("Parent properties: " + parentProperties);
			debug("ID: " + id);
			Class<? extends ASTNode> parentNode = parent.getClass();
			String parentNodeName = parentNode.getSimpleName();

			// Check parent for staticMethod
			// MethodInvocation means staticMethod is called
			// AD HOC: to distinguish class from method name, the id of the the node's
			// StructuralPropertieDescriptor is "expression" for the class and "name" for
			// the method name
			if (parentNode.equals(MethodInvocation.class) && id.equals("expression")) {
				nameQualified = appendPackageName(nameQualified);
				debug("Parent: " + parentNodeName);
				debug("Added named reference: " + nameQualified);
				incrementReference(nameQualified);
			} else {
				debug("Not added. " + nameQualified + " is not a class calling a static method.");
			}
		}
		return true;

	}

	/**
	 * Gets most things.
	 */
	@Override
	public boolean visit(SimpleType node) {
		ITypeBinding typeBind = node.resolveBinding();
		String nameQualified;
		// Strips parameterized generics off
		
		if(typeBind == null) {
			return true;
		}
		
		
		nameQualified = typeBind.getTypeDeclaration().getQualifiedName();
		String nameSimple = typeBind.getTypeDeclaration().getName();

		// Add package name if does not contain package name and not in default package
		// If imported from default package (which is not valid Java syntax), it will
		// count
		// as a reference to the default package and not append the current package name
		IPackageBinding packBind = typeBind.getPackage();
		// Check that packBind exists to prevent NullPointerException
		String packName;
		if (packBind != null) {
			packName = packBind.getName();
		} else {
			packName = "";
		}

		// If SimpleType was imported from the default package, increment simple name
		debug(node, "Most references");
		debug("Imported classes: " + importedNames);
		debug("Imported classes simple: " + importedNamesSimple);
		debug("nameSimple: " + nameSimple);
		debug("nameQualified: " + nameQualified);
		debug("nestedDeclarations: " + nestedDeclarations);
		debug("localDeclarations: " + localDeclarations);
		if (importedNames.contains(nameSimple)) {
			// Check for import from default package
			incrementReference(nameSimple);
			debug("Added nameSimple from default package reference: " + nameSimple);
			return true;
		} else if (node.getParent().getClass().equals(ArrayType.class) && nameQualified.equals("")) {
			// Check if local array, which has an empty qualifiedName
			// Add array references
			incrementLocalReference(nameSimple + "[]");
			incrementReference(nameSimple + "[]");
			// Add simpleName reference
			incrementLocalReference(nameSimple);
			incrementReference(nameSimple);
			debug("Added local array reference: " + nameSimple + "[]");
			debug("Added local reference: " + nameSimple);
			debug("Added array reference: " + nameSimple + "[]");
			debug("Added reference: " + nameSimple);
			return true;
		} else if (localDeclarations.contains(nameSimple)) {
			// Local reference
			incrementLocalReference(nameSimple);
			incrementReference(nameSimple);
			debug("Added reference: " + nameSimple);
			debug("Added local reference: " + nameSimple);
			return true;
		} else if (!nameQualified.contains(".") && packName.length() > 0) {
			// Check if need to add package to qualified name
			nameQualified = packName + "." + nameQualified;
		}
		if (packBind == null) {
			debug("Not added: " + nameQualified + ". Is generic type.");
		} else if (importedNamesSimple.contains(nameSimple)) {
			String nameQualifiedImported = importedNames.get(importedNamesSimple.indexOf(nameSimple));
			incrementReference(nameQualifiedImported);
			debug("Added imported reference: " + nameQualifiedImported);
		} else {
			incrementReference(nameQualified);
			debug("Added reference: " + nameQualified);
		}

		// Check for nested and local types
		if (nestedDeclarations.contains(nameQualified)) {
			debug("Added nested reference: " + nameQualified);
			incrementNestedReference(nameQualified);
		}
		return true;
	}

	@Override
	public boolean visit(SingleMemberAnnotation node) {
		debug(node, "Single member annotation references");
		IAnnotationBinding annBind = node.resolveAnnotationBinding();
		ITypeBinding typeBind = annBind.getAnnotationType();
		String nameQualified = typeBind.getQualifiedName();
		String nameSimple = typeBind.getName();
		if (importedNamesSimple.contains(nameSimple)) {
			String nameQualifiedImported = importedNames.get(importedNamesSimple.indexOf(nameSimple));
			debug("Added imported reference: " + nameQualifiedImported);
			incrementReference(nameQualifiedImported);
		} else {
			debug("Added reference: " + nameQualified);
			incrementReference(nameQualified);
		}
		return true;
	}

	/**
	 * Visits a type declaration node type. Type declaration node is the union of
	 * class declaration, and interface declaration.
	 *
	 * Determine the type of class, add it to types, and increment the declaration
	 * counter associated to the type.
	 *
	 * CounterType: declaration, nested declaration, local declarations
	 * TODO Declarations that are both nested and local. Try to avoid infinite loop
	 *
	 * @param node
	 *            : TypeDeclaration
	 * @return boolean : True to visit the children of this node
	 */
	@Override
	public boolean visit(TypeDeclaration node) {
		debug(node, "Named Class/Interface declarations. Normal, local, nested.");
		ITypeBinding typeBind = node.resolveBinding();
		String nameQualified = typeBind.getQualifiedName();
		String nameSimple = typeBind.getName();

		boolean isNested = false;
		boolean isLocal = false;
		// Local classes do not have qualified names (so would be empty string) only
		// But they do have simple names
		if (typeBind.isLocal()) {
			nameQualified = typeBind.getTypeDeclaration().getName();
			isLocal = true;
		}
		// Nested classes have at least 1 parent node as a TypeDeclaration
		ASTNode parent = null;
		while (!(isNested && isLocal)) {

			if (parent != null) {
				if (parent.equals(node.getParent())) {
					// Two parents of the same type indicates infinite loop
					// Seems to be true, but may not be
					debug("Breaking loop of repeat parent: " + parent.getClass().getSimpleName());
					break;
				}
			}
			parent = node.getParent();
			
			if (parent == null) {
				// no more parents
				// TODO Does this ever run?
				// Just to be safe?
				break;
			}
			Class<? extends ASTNode> parentNode = parent.getClass();
			String parentNodeName = parentNode.getSimpleName();

			debug("Parent: " + parentNodeName);

			if (parentNode.equals(TypeDeclaration.class) || parentNode.equals(AnonymousClassDeclaration.class)) {
				isNested = true;
			} else if (parentNode.equals(CompilationUnit.class) || parentNode.equals(TypeDeclarationStatement.class)) {
				// CompilationUnit: Reached the top, so no more parents
				// TypeDeclarationStatement: In some cases, this becomes the parent forever, so break to stop infinite loop
				break;
			} else if (parentNode.equals(MethodInvocation.class)) {//  TODO Is this the correct approach? Does this ever run?
				isLocal = true;
			}
		}
	
		if (isNested) {
			if (nameQualified.equals("")) {
				debug("Added nested declaration in anonymous declaration: " + nameSimple);
				incrementNestedDeclaration(nameSimple);
				nameQualified = nameSimple; // Changed so named declaration adds simple name
			} else {
				debug("Added nested declaration: " + nameQualified);
				incrementNestedDeclaration(nameQualified);
			}
		}
		if (isLocal) {
			debug("Added local declaration: " + nameQualified);
			incrementLocalDeclaration(nameQualified);
		}
		debug("Added named declaration: " + nameQualified);
		incrementDeclaration(nameQualified);
		return true;
	}
}
