package main.ast;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class TypeCounterI2G4 {

	// Contains objects of type TargetType which keeps track of all the references
	// and declarations count.
	ArrayList<TargetType> types = new ArrayList<TargetType>();

	// List to keep track of all the variable declarations
	ArrayList<TargetType> types2 = new ArrayList<TargetType>();

	public ArrayList<TargetType> getList() {
		return types;
	}

	public ArrayList<TargetType> getList2() {
		return types2;
	}

	/**
	 ** Parameters: The starting node of the syntax tree, the type that the user is
	 * looking to count Count all Annotation, Enumeration, Interface, and Class
	 * declarations of the target type. Return: The the ArrayList containing all the
	 * types.
	 **/
	private ArrayList<TargetType> countDec(ASTNode cu) {
		cu.accept(new ASTVisitor() {

			// COUNT ANNOTATION DECLARATIONS
			@Override
			public boolean visit(AnnotationTypeDeclaration node) {
				addVisitDec(node);
				return true;
			}

			// COUNT ENUMERATION DECLARATIONS
			@Override
			public boolean visit(EnumDeclaration node) {
				addVisitDec(node);
				return true;
			}

			// COUNT CLASS AND INTERFACE DECLARATIONS
			@Override
			public boolean visit(TypeDeclaration node) {
				addVisitDec(node);
				return true;
			}

		});
		return types;
	}

	/**
	 ** Parameters: The starting node of the syntax tree, the type that the user is
	 * looking to count Count non-primitive variable declarations. Return: the
	 * ArrayList.
	 **/
	public ArrayList<TargetType> countVarDec(ASTNode cu) {
		cu.accept(new ASTVisitor() {

			@Override
			public boolean visit(VariableDeclarationFragment node) {
				addVisitDec(node);
				return true;
			}

		});
		return types2;
	}

	/**
	 ** Parameters: The starting node of the syntax tree, the type that the user is
	 * looking to count Count all references to the target type Return: The integer
	 * refCount.
	 **/
	private ArrayList<TargetType> countRef(ASTNode cu) {
		cu.accept(new ASTVisitor() {

			// Count import declaration type reference.
			@Override
			public boolean visit(ImportDeclaration node) {
				addVisitRef(node);
				return true;
			}

			// COUNT NORMAL ANNOTATION TYPE REFERENCES
			@Override
			public boolean visit(NormalAnnotation node) {
				try {
					addVisitDec(node);
				} catch (Exception e) {
					System.out.println("Error visiting NormalAnnotation node");
				}
				return false;
			}

			// COUNT MARKER ANNOTATION TYPE REFERENCES
			@Override
			public boolean visit(MarkerAnnotation node) {
				addVisitRef(node);
				return true;
			}

			// COUNT PRIMITIVE TYPE REFERENCES
			@Override
			public boolean visit(PrimitiveType node) {
				addVisitRef(node);
				return true;
			}

			// COUNT ALL OTHER TYPE REFERENCES
			@Override
			public boolean visit(SimpleType node) {
				addVisitRef(node);
				return true;
			}
		});

		return types;
	}

	// Return fully qualified name of the java type
	private String getFullName(MarkerAnnotation node) {
		return node.resolveTypeBinding().getQualifiedName();
	}

	/**
	 ** Parameters: MarkerAnnotation Node type. Adds the type to a list. Search for
	 * this node type and increment the references Updates the list.
	 **/
	private void addVisitRef(MarkerAnnotation node) {
		if ((types.isEmpty()) && (!(getFullName(node).equals("void")))) {
			types.add(new TargetType(getFullName(node), 1, 0));
		} else {
			if (!(types.isEmpty())) {

				TargetType repeat = null;
				boolean first = true;

				ArrayList<String> typeNames = new ArrayList<String>();

				for (TargetType s : types) {

					typeNames.add(s.getType());
					if (typeNames.contains((getFullName(node))) && first) {
						repeat = s;
						first = false;
					}
				}

				if (repeat != null) {
					repeat.addRef();
				}
				if (!(getFullName(node).equals("void")) && !(typeNames.contains(getFullName(node)))) {
					types.add(new TargetType(getFullName(node), 1, 0));
				}
			}
		}

	}

	/**
	 ** Parameters: To search for SimpleType and PrimitiveType Adds the type to a
	 * list. Search for this node type and increment the references Updates the
	 * list.
	 **/
	private void addVisitRef(Type node) {

		if ((types.isEmpty()) && (!(getFullName(node).equals("void")))) {

			types.add(new TargetType(getFullName(node), 1, 0));

		} else {
			if (!(types.isEmpty())) {

				TargetType repeat = null;
				boolean first = true;

				ArrayList<String> typeNames = new ArrayList<String>();

				for (TargetType s : types) {

					typeNames.add(s.getType());
					if (typeNames.contains((getFullName(node))) && first) {
						repeat = s;
						first = false;
					}
				}

				if (repeat != null) {
					repeat.addRef();
				}
				if (!(getFullName(node).equals("void")) && !(typeNames.contains(getFullName(node)))) {
					types.add(new TargetType(getFullName(node), 1, 0));
				}
			}
		}
	}

	/**
	 ** Parameters: VariableDeclarationFragment Node type. Only counts
	 * non-primitives. Adds the type to a list. Search for this node type and
	 * increment the Declarations Updates the list.
	 **/
	private void addVisitDec(VariableDeclarationFragment node) {

		if (!node.resolveBinding().getType().isPrimitive()
				&& !node.resolveBinding().getType().getName().equals("String")) {

			if ((types2.isEmpty())) {

				types2.add(new TargetType(node.resolveBinding().getVariableDeclaration().getName(), 0, 1));

			} else {
				if (!(types2.isEmpty())) {

					TargetType repeat = null;
					boolean first = true;

					ArrayList<String> typeNames = new ArrayList<String>();

					for (TargetType s : types2) {

						typeNames.add(s.getType());
						if (typeNames.contains((node.resolveBinding().getVariableDeclaration().getName())) && first) {
							repeat = s;
							first = false;
						}
					}

					if (repeat != null) {
						repeat.addDec();
					}
					if (!(typeNames.contains(node.resolveBinding().getVariableDeclaration().getName()))) {
						types2.add(new TargetType(node.resolveBinding().getVariableDeclaration().getName(), 0, 1));
					}
				}
			}
		}

	}

	/**
	 ** Parameters: Search for import declarations. Adds the type to a list and
	 * increments the references to this type in the TargetType class
	 */
	private void addVisitRef(ImportDeclaration node) {

		if ((types.isEmpty()) && (!(node.getName().toString().equals("void")))) {

			types.add(new TargetType(node.getName().toString(), 1, 0));

		} else {
			if (!(types.isEmpty())) {

				TargetType repeat = null;
				boolean first = true;

				ArrayList<String> typeNames = new ArrayList<String>();

				for (TargetType s : types) {

					typeNames.add(s.getType());
					if (typeNames.contains(node.getName().toString()) && first) {
						repeat = s;
						first = false;
					}
				}

				if (repeat != null) {
					repeat.addRef();
				}
				if (!(node.getName().toString().equals("void")) && !(typeNames.contains(node.getName().toString()))) {
					types.add(new TargetType(node.getName().toString(), 1, 0));
				}
			}
		}

	}

	/**
	 ** Parameters: TypeDeclaration Node type. Adds the type to a list. Search for
	 * this node type and increment the Declarations Updates the list.
	 **/
	private void addVisitDec(TypeDeclaration node) {

		if ((types.isEmpty())) {

			String temp = node.resolveBinding().getQualifiedName();

			if (!temp.equals("")) {
				types.add(new TargetType(node.resolveBinding().getQualifiedName(), 0, 1));
			} else {
				types.add(new TargetType(node.getName().toString(), 0, 1));
			}

		} else {
			if (!(types.isEmpty())) {

				TargetType repeat = null;
				boolean first = true;

				ArrayList<String> typeNames = new ArrayList<String>();

				for (TargetType s : types) {

					typeNames.add(s.getType());
					if (typeNames.contains((node.resolveBinding().getQualifiedName())) && first) {
						repeat = s;
						first = false;
					}
				}

				if (repeat != null) {
					repeat.addDec();
				}

				String temp = node.resolveBinding().getQualifiedName();
				if (!typeNames.contains(node.resolveBinding().getQualifiedName()) && !temp.equals("")) {
					types.add(new TargetType(node.resolveBinding().getQualifiedName(), 0, 1));
				} else if (!typeNames.contains(node.resolveBinding().getQualifiedName()) && temp.equals("")) {
					types.add(new TargetType(node.getName().toString(), 0, 1));
				}
			}
		}
	}

	/**
	 ** Parameters: EnumDeclaration Node type. Adds the type to a list. Search for
	 * this node type and increment the declarations Updates the list.
	 **/
	private void addVisitDec(EnumDeclaration node) {

		if ((types.isEmpty())) {

			types.add(new TargetType(node.resolveBinding().getQualifiedName(), 0, 1));

		} else {
			if (!(types.isEmpty())) {

				TargetType repeat = null;
				boolean first = true;

				ArrayList<String> typeNames = new ArrayList<String>();

				for (TargetType s : types) {

					typeNames.add(s.getType());
					if (typeNames.contains((node.resolveBinding().getQualifiedName())) && first) {
						repeat = s;
						first = false;
					}
				}

				if (repeat != null) {
					repeat.addDec();
				}
				if (!(typeNames.contains(node.resolveBinding().getQualifiedName()))) {
					types.add(new TargetType(node.resolveBinding().getQualifiedName(), 0, 1));
				}
			}
		}

	}

	/**
	 ** Parameters: AnnotationTypeDeclaration Node type. Adds the type to a list.
	 * Search for this node type and increment the declarations Updates the list.
	 **/
	private void addVisitDec(AnnotationTypeDeclaration node) {

		if ((types.isEmpty())) {

			types.add(new TargetType(node.resolveBinding().getQualifiedName(), 0, 1));

		} else {
			if (!(types.isEmpty())) {

				TargetType repeat = null;
				boolean first = true;

				ArrayList<String> typeNames = new ArrayList<String>();

				for (TargetType s : types) {

					typeNames.add(s.getType());
					if (typeNames.contains((node.resolveBinding().getQualifiedName())) && first) {
						repeat = s;
						first = false;
					}
				}

				if (repeat != null) {
					repeat.addDec();
				}
				if (!(typeNames.contains(node.resolveBinding().getQualifiedName()))) {
					types.add(new TargetType(node.resolveBinding().getQualifiedName(), 0, 1));
				}
			}
		}

	}

	/**
	 ** Parameters: NormalAnnotation Node type. Adds the type to a list. Search for
	 * this node type and increment the declarations Updates the list.
	 **/
	protected void addVisitDec(NormalAnnotation node) {

		if ((types.isEmpty())) {

			types.add(new TargetType(node.resolveTypeBinding().getQualifiedName(), 0, 1));

		} else {
			if (!(types.isEmpty())) {

				TargetType repeat = null;
				boolean first = true;

				ArrayList<String> typeNames = new ArrayList<String>();

				for (TargetType s : types) {

					typeNames.add(s.getType());
					if (typeNames.contains((node.resolveTypeBinding().getQualifiedName())) && first) {
						repeat = s;
						first = false;
					}
				}

				if (repeat != null) {
					repeat.addDec();
				}
				if (!(typeNames.contains(node.resolveTypeBinding().getQualifiedName()))) {
					types.add(new TargetType(node.resolveTypeBinding().getQualifiedName(), 0, 1));
				}
			}
		}

	}

	/**
	 ** Parameters: The starting node of the syntax tree, the type that the user is
	 * looking to count Count all references to the target type Return: The
	 * arrayList containing all types.
	 **/
	public ArrayList<TargetType> count(ASTNode cu) {
		countRef(cu);
		countDec(cu);
		return types;
	}

	// Return fully qualified name of the java type
	private String getFullName(Type node) {
		return node.resolveBinding().getQualifiedName();

	}

	/*
	 * Keeps track of the specific java types within a directory. Increments
	 * references and declaration counts and returns them
	 */
	public class TargetType {

		private String type;

		private int ref = 0;;

		private int dec = 0;

		public TargetType(String type, int ref, int dec) {

			this.type = type;
			this.ref = ref;
			this.dec = dec;
		}

		public String getType() {
			return type;
		}

		public int getRef() {
			return ref;
		}

		public int getDec() {
			return dec;
		}

		public void addRef() {
			this.ref++;
		}

		public void addDec() {
			this.dec++;
		}
	}

}
