// -*- mode: java -*- 
//
// file: cool-tree.m4
//
// This file defines the AST
//
//////////////////////////////////////////////////////////

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.io.PrintStream;
import java.util.Vector;

/** Defines simple phylum Program */
abstract class Program extends TreeNode {
    protected Program(int lineNumber) {
        super(lineNumber);
    }

    public abstract void dump_with_types(PrintStream out, int n);

    public abstract void semant();

}

/** Defines simple phylum Class_ */
abstract class Class_ extends TreeNode {
    protected Class_(int lineNumber) {
        super(lineNumber);
    }

    public abstract void dump_with_types(PrintStream out, int n);

}

/**
 * Defines list phylum Classes
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
class Classes extends ListNode {
    public final static Class elementClass = Class_.class;

    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }

    protected Classes(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }

    /** Creates an empty "Classes" list */
    public Classes(int lineNumber) {
        super(lineNumber);
    }

    /** Appends "Class_" element to this list */
    public Classes appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }

    public TreeNode copy() {
        return new Classes(lineNumber, copyElements());
    }
}

/** Defines simple phylum Feature */
abstract class Feature extends TreeNode {
    protected Feature(int lineNumber) {
        super(lineNumber);
    }

    public abstract void dump_with_types(PrintStream out, int n);

    public abstract void semant(ClassTable classTable, class_c curClass);

}

/**
 * Defines list phylum Features
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
class Features extends ListNode {
    public final static Class elementClass = Feature.class;

    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }

    protected Features(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }

    /** Creates an empty "Features" list */
    public Features(int lineNumber) {
        super(lineNumber);
    }

    /** Appends "Feature" element to this list */
    public Features appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }

    public TreeNode copy() {
        return new Features(lineNumber, copyElements());
    }
}

/** Defines simple phylum Formal */
abstract class Formal extends TreeNode {
    protected Formal(int lineNumber) {
        super(lineNumber);
    }

    public abstract void dump_with_types(PrintStream out, int n);

}

/**
 * Defines list phylum Formals
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
class Formals extends ListNode {
    public final static Class elementClass = Formal.class;

    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }

    protected Formals(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }

    /** Creates an empty "Formals" list */
    public Formals(int lineNumber) {
        super(lineNumber);
    }

    /** Appends "Formal" element to this list */
    public Formals appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }

    public TreeNode copy() {
        return new Formals(lineNumber, copyElements());
    }
}

/** Defines simple phylum Expression */
abstract class Expression extends TreeNode {
    protected Expression(int lineNumber) {
        super(lineNumber);
    }

    private AbstractSymbol type = null;

    public AbstractSymbol get_type() {
        return type;
    }

    public Expression set_type(AbstractSymbol s) {
        type = s;
        return this;
    }

    public abstract void dump_with_types(PrintStream out, int n);

    public void dump_type(PrintStream out, int n) {
        if (type != null) {
            out.println(Utilities.pad(n) + ": " + type.getString());
        } else {
            out.println(Utilities.pad(n) + ": _no_type");
        }
    }

    public abstract void semant(ClassTable classTable, class_c curClass);
}

/**
 * Defines list phylum Expressions
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
class Expressions extends ListNode {
    public final static Class elementClass = Expression.class;

    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }

    protected Expressions(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }

    /** Creates an empty "Expressions" list */
    public Expressions(int lineNumber) {
        super(lineNumber);
    }

    /** Appends "Expression" element to this list */
    public Expressions appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }

    public TreeNode copy() {
        return new Expressions(lineNumber, copyElements());
    }
}

/** Defines simple phylum Case */
abstract class Case extends TreeNode {
    protected Case(int lineNumber) {
        super(lineNumber);
    }

    public abstract void dump_with_types(PrintStream out, int n);

}

/**
 * Defines list phylum Cases
 * <p>
 * See <a href="ListNode.html">ListNode</a> for full documentation.
 */
class Cases extends ListNode {
    public final static Class elementClass = Case.class;

    /** Returns class of this lists's elements */
    public Class getElementClass() {
        return elementClass;
    }

    protected Cases(int lineNumber, Vector elements) {
        super(lineNumber, elements);
    }

    /** Creates an empty "Cases" list */
    public Cases(int lineNumber) {
        super(lineNumber);
    }

    /** Appends "Case" element to this list */
    public Cases appendElement(TreeNode elem) {
        addElement(elem);
        return this;
    }

    public TreeNode copy() {
        return new Cases(lineNumber, copyElements());
    }
}

/**
 * Defines AST constructor 'programc'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class programc extends Program {
    protected Classes classes;

    /**
     * Creates "programc" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for classes
     */
    public programc(int lineNumber, Classes a1) {
        super(lineNumber);
        classes = a1;
    }

    public TreeNode copy() {
        return new programc(lineNumber, (Classes) classes.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "programc\n");
        classes.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_program");
        for (Enumeration e = classes.getElements(); e.hasMoreElements();) {
            // sm: changed 'n + 1' to 'n + 2' to match changes elsewhere
            ((Class_) e.nextElement()).dump_with_types(out, n + 2);
        }
    }

    /**
     * This method is the entry point to the semantic checker. You will need to
     * complete it in programming assignment 4.
     * <p>
     * Your checker should do the following two things:
     * <ol>
     * <li>Check that the program is semantically correct
     * <li>Decorate the abstract syntax tree with type information by setting
     * the type field in each Expression node. (see tree.h)
     * </ol>
     * <p>
     * You are free to first do (1) and make sure you catch all semantic errors.
     * Part (2) can be done in a second stage when you want to test the complete
     * compiler.
     */
    public void semant() {
        /* ClassTable constructor may do some semantic analysis */
        ClassTable classTable = new ClassTable(classes);

        if (classTable.errors()) {
            System.err
                    .println("Compilation halted due to static semantic errors.");
            System.exit(1);
        }

        /* some semantic analysis code may go here */
        classTable.buildEnvironment();
        for (Enumeration e = classes.getElements(); e.hasMoreElements();) {
            class_c curClass = (class_c) e.nextElement();
            curClass.semant(classTable);
        }

        if (classTable.errors()) {
            System.err
                    .println("Compilation halted due to static semantic errors.");
            System.exit(1);
        }
    }

}

/**
 * Defines AST constructor 'class_c'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class class_c extends Class_ {
    protected AbstractSymbol name;
    protected AbstractSymbol parent;
    protected Features features;
    protected AbstractSymbol filename;

    /**
     * Creates "class_c" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for name
     * @param a1
     *            initial value for parent
     * @param a2
     *            initial value for features
     * @param a3
     *            initial value for filename
     */
    public class_c(int lineNumber, AbstractSymbol a1, AbstractSymbol a2,
            Features a3, AbstractSymbol a4) {
        super(lineNumber);
        name = a1;
        parent = a2;
        features = a3;
        filename = a4;
    }

    public TreeNode copy() {
        return new class_c(lineNumber, copy_AbstractSymbol(name),
                copy_AbstractSymbol(parent), (Features) features.copy(),
                copy_AbstractSymbol(filename));
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "class_c\n");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, parent);
        features.dump(out, n + 2);
        dump_AbstractSymbol(out, n + 2, filename);
    }

    public AbstractSymbol getFilename() {
        return filename;
    }

    public AbstractSymbol getName() {
        return name;
    }

    public AbstractSymbol getParent() {
        return parent;
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_class");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, parent);
        out.print(Utilities.pad(n + 2) + "\"");
        Utilities.printEscapedString(out, filename.getString());
        out.println("\"\n" + Utilities.pad(n + 2) + "(");
        for (Enumeration e = features.getElements(); e.hasMoreElements();) {
            ((Feature) e.nextElement()).dump_with_types(out, n + 2);
        }
        out.println(Utilities.pad(n + 2) + ")");
    }

    public void semant(ClassTable classTable) {
        for (Enumeration e = features.getElements(); e.hasMoreElements();) {
            Feature fea = (Feature) e.nextElement();
            fea.semant(classTable, this);
        }
    }

}

/**
 * Defines AST constructor 'method'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class method extends Feature {
    protected AbstractSymbol name;
    protected Formals formals;
    protected AbstractSymbol return_type;
    protected Expression expr;

    /**
     * Creates "method" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for name
     * @param a1
     *            initial value for formals
     * @param a2
     *            initial value for return_type
     * @param a3
     *            initial value for expr
     */
    public method(int lineNumber, AbstractSymbol a1, Formals a2,
            AbstractSymbol a3, Expression a4) {
        super(lineNumber);
        name = a1;
        formals = a2;
        return_type = a3;
        expr = a4;
    }

    public TreeNode copy() {
        return new method(lineNumber, copy_AbstractSymbol(name),
                (Formals) formals.copy(), copy_AbstractSymbol(return_type),
                (Expression) expr.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "method\n");
        dump_AbstractSymbol(out, n + 2, name);
        formals.dump(out, n + 2);
        dump_AbstractSymbol(out, n + 2, return_type);
        expr.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_method");
        dump_AbstractSymbol(out, n + 2, name);
        for (Enumeration e = formals.getElements(); e.hasMoreElements();) {
            ((Formal) e.nextElement()).dump_with_types(out, n + 2);
        }
        dump_AbstractSymbol(out, n + 2, return_type);
        expr.dump_with_types(out, n + 2);
    }

    @Override
    public void semant(ClassTable classTable, class_c curClass) {
        if (!classTable.containsClass(return_type)
            && return_type != TreeConstants.SELF_TYPE) {
            classTable.semantError().format(
                    "%s:%s: Undefined return type %s in method %s.\n",
                    curClass.filename, lineNumber, return_type, name);
            return_type = TreeConstants.Object_;
        }
        
        ArrayList<AbstractSymbol> paraList1 = 
                classTable.queryMethodEnvironment(curClass.parent, name);
        ArrayList<AbstractSymbol> paraList2 = 
                classTable.queryMethodEnvironment(curClass.name, name);
        if (paraList1 != null) {
            if (paraList1.size() != paraList2.size()) {
                classTable.semantError().format(
                        "%s:%s: Incompatible number of formal parameters in redefined method %s.\n",
                        curClass.filename, lineNumber, name);
            } else {
                for (int i = 1; i < paraList1.size() - 1; i += 2) {
                    if (paraList1.get(i) != paraList2.get(i)) {
                        classTable.semantError().format(
                                "%s:%s: In redefined method %s, parameter type %s is different from original type %s.\n",
                                curClass.filename, lineNumber, name, paraList2.get(i), paraList1.get(i));
                    }
                }
            }
        }
        
        SymbolTable env = classTable.objEnv.get(curClass.name);
        env.enterScope();
        for (Enumeration e = formals.getElements(); e.hasMoreElements(); ) {
            formalc f = (formalc) e.nextElement();
            if (f.name == TreeConstants.self) {
                classTable.semantError().format(
                        "%s:%s: 'self' cannot be the name of a formal parameter.\n",
                        curClass.filename, lineNumber);
            }
            if (f.type_decl == TreeConstants.SELF_TYPE) {
                classTable.semantError().format(
                        "%s:%s: Formal parameter %s cannot have type SELF_TYPE.\n",
                        curClass.filename, lineNumber, f.name);
                f.type_decl = TreeConstants.Object_;
            } else if (!classTable.containsClass(f.type_decl)) {
                classTable.semantError().format(
                        "%s:%s: Undefined type %s for formal parameter %s.\n",
                        curClass.filename, lineNumber, f.type_decl, f.name);
                f.type_decl = TreeConstants.Object_;
            }
            if (env.probe(f.name) != null) {
                classTable.semantError().format(
                        "%s:%s: Formal parameter %s is multiply defined.\n",
                        curClass.filename, lineNumber, f.name);
            } else {
                env.addId(f.name, f.type_decl);
            }
        }
        
        expr.semant(classTable, curClass);
        env.exitScope();
        AbstractSymbol type = expr.get_type();
        if (!classTable.isChild(return_type, type, curClass.name)) {
            classTable.semantError().format(
                    "%s:%s: Inferred return type %s of method %s does not conform to declared return type %s.\n",
                    curClass.filename, lineNumber, type, name, return_type);
        }
    }

}

/**
 * Defines AST constructor 'attr'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class attr extends Feature {
    protected AbstractSymbol name;
    protected AbstractSymbol type_decl;
    protected Expression init;

    /**
     * Creates "attr" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for name
     * @param a1
     *            initial value for type_decl
     * @param a2
     *            initial value for init
     */
    public attr(int lineNumber, AbstractSymbol a1, AbstractSymbol a2,
            Expression a3) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
        init = a3;
    }

    public TreeNode copy() {
        return new attr(lineNumber, copy_AbstractSymbol(name),
                copy_AbstractSymbol(type_decl), (Expression) init.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "attr\n");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
        init.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_attr");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
        init.dump_with_types(out, n + 2);
    }

    @Override
    public void semant(ClassTable classTable, class_c curClass) {
        if (name == TreeConstants.self) {
            classTable.semantError().format(
                    "%s:%s: 'self' cannot be the name of an attribute.\n",
                    curClass.filename, lineNumber);
        } else if (classTable.queryObjectEnvironment(curClass.parent, name) != null) {
            classTable.semantError().format(
                    "%s:%s: Attribute %s is an attribute of an inherited class.\n",
                    curClass.filename, lineNumber, name);
        }
        if (!classTable.containsClass(type_decl)
            && type_decl != TreeConstants.SELF_TYPE) {
            classTable.semantError().format(
                    "%s:%s: Undefined type %s for attribute %s.\n",
                    curClass.filename, lineNumber, type_decl, name);
            type_decl = TreeConstants.Object_;
        }
        init.semant(classTable, curClass);
        AbstractSymbol type = init.get_type();
        if (!classTable.isChild(type_decl, type, curClass.name)) {
            classTable.semantError().format(
                    "%s:%s: Inferred type %s of initialization of attribute %s does not conform to declared type %s.\n",
                    curClass.filename, lineNumber, type, name, type_decl);
        }
    }

}

/**
 * Defines AST constructor 'formalc'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class formalc extends Formal {
    protected AbstractSymbol name;
    protected AbstractSymbol type_decl;

    /**
     * Creates "formalc" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for name
     * @param a1
     *            initial value for type_decl
     */
    public formalc(int lineNumber, AbstractSymbol a1, AbstractSymbol a2) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
    }

    public TreeNode copy() {
        return new formalc(lineNumber, copy_AbstractSymbol(name),
                copy_AbstractSymbol(type_decl));
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "formalc\n");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_formal");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
    }

}

/**
 * Defines AST constructor 'branch'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class branch extends Case {
    protected AbstractSymbol name;
    protected AbstractSymbol type_decl;
    protected Expression expr;

    /**
     * Creates "branch" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for name
     * @param a1
     *            initial value for type_decl
     * @param a2
     *            initial value for expr
     */
    public branch(int lineNumber, AbstractSymbol a1, AbstractSymbol a2,
            Expression a3) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
        expr = a3;
    }

    public TreeNode copy() {
        return new branch(lineNumber, copy_AbstractSymbol(name),
                copy_AbstractSymbol(type_decl), (Expression) expr.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "branch\n");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
        expr.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_branch");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, type_decl);
        expr.dump_with_types(out, n + 2);
    }
    
    public void semant(ClassTable classTable, class_c curClass) {
        if (type_decl == TreeConstants.SELF_TYPE) {
            classTable.semantError().format(
                    "%s:%s: Case statement cannot have branch SELF_TYPE.\n",
                    curClass.filename, lineNumber);
            type_decl = TreeConstants.Object_;
        } else if (!classTable.containsClass(type_decl)) {
            classTable.semantError().format(
                    "%s:%s: Undefined type %s in case statement.\n",
                    curClass.filename, lineNumber, type_decl);
            type_decl = TreeConstants.Object_;
        }
        
        if (name == TreeConstants.self) {
            classTable.semantError().format(
                    "%s:%s: 'self' cannot be bound in a case statement.\n",
                    curClass.filename, lineNumber);
        }
        SymbolTable env = classTable.objEnv.get(curClass.name);
        env.enterScope();
        env.addId(name, type_decl);
        expr.semant(classTable, curClass);
        env.exitScope();
    }
    
}

/**
 * Defines AST constructor 'assign'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class assign extends Expression {
    protected AbstractSymbol name;
    protected Expression expr;

    /**
     * Creates "assign" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for name
     * @param a1
     *            initial value for expr
     */
    public assign(int lineNumber, AbstractSymbol a1, Expression a2) {
        super(lineNumber);
        name = a1;
        expr = a2;
    }

    public TreeNode copy() {
        return new assign(lineNumber, copy_AbstractSymbol(name),
                (Expression) expr.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "assign\n");
        dump_AbstractSymbol(out, n + 2, name);
        expr.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_assign");
        dump_AbstractSymbol(out, n + 2, name);
        expr.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    public void semant(ClassTable classTable, class_c curClass) {
        AbstractSymbol t1 = classTable.queryObjectEnvironment(curClass.name, name);
        if (t1 == null) {
            classTable.semantError().format(
                    "%s:%s: Undeclared identifier %s.\n",
                    curClass.filename, lineNumber, name);
            t1 = TreeConstants.Object_;
        }
        if (name == TreeConstants.self) {
            classTable.semantError().format(
                    "%s:%s: Cannot assign to 'self'.\n",
                    curClass.filename, lineNumber);
        }
        
        expr.semant(classTable, curClass);
        AbstractSymbol t2 = expr.get_type();
        if (classTable.isChild(t1, t2, curClass.name)) {
            set_type(t2);
        } else {
            classTable.semantError().format(
                    "%s:%s: Inferred type %s of assigned expression does not conform to declared type %s of identifier %s.\n",
                    curClass.filename, lineNumber, t2, t1, name);
            set_type(TreeConstants.Object_);
        }
    }

}

/**
 * Defines AST constructor 'static_dispatch'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class static_dispatch extends Expression {
    protected Expression expr;
    protected AbstractSymbol type_name;
    protected AbstractSymbol name;
    protected Expressions actual;

    /**
     * Creates "static_dispatch" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for expr
     * @param a1
     *            initial value for type_name
     * @param a2
     *            initial value for name
     * @param a3
     *            initial value for actual
     */
    public static_dispatch(int lineNumber, Expression a1, AbstractSymbol a2,
            AbstractSymbol a3, Expressions a4) {
        super(lineNumber);
        expr = a1;
        type_name = a2;
        name = a3;
        actual = a4;
    }

    public TreeNode copy() {
        return new static_dispatch(lineNumber, (Expression) expr.copy(),
                copy_AbstractSymbol(type_name), copy_AbstractSymbol(name),
                (Expressions) actual.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "static_dispatch\n");
        expr.dump(out, n + 2);
        dump_AbstractSymbol(out, n + 2, type_name);
        dump_AbstractSymbol(out, n + 2, name);
        actual.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_static_dispatch");
        expr.dump_with_types(out, n + 2);
        dump_AbstractSymbol(out, n + 2, type_name);
        dump_AbstractSymbol(out, n + 2, name);
        out.println(Utilities.pad(n + 2) + "(");
        for (Enumeration e = actual.getElements(); e.hasMoreElements();) {
            ((Expression) e.nextElement()).dump_with_types(out, n + 2);
        }
        out.println(Utilities.pad(n + 2) + ")");
        dump_type(out, n);
    }

    @Override
    public void semant(ClassTable classTable, class_c curClass) {
        expr.semant(classTable, curClass);
        for (Enumeration e = actual.getElements(); e.hasMoreElements(); ) {
            ((Expression) e.nextElement()).semant(classTable, curClass);
        }
        if (!classTable.containsClass(type_name)) {
            classTable.semantError().format(
                    "%s:%s: Undefined static dispatch type %s.\n",
                    curClass.filename, lineNumber, type_name);
            set_type(TreeConstants.Object_);
            return;
        } else if (!classTable.isChild(type_name, expr.get_type(), curClass.name)) {
            classTable.semantError().format(
                    "%s:%s: Expression type %s does not conform to declared static dispatch type %s.\n",
                    curClass.filename, lineNumber, expr.get_type(), type_name);
            set_type(TreeConstants.Object_);
            return;
        }
        ArrayList<AbstractSymbol> list = classTable.queryMethodEnvironment(type_name, name);
        if (list == null) {
            classTable.semantError().format(
                    "%s:%s: Dispatch to undefined method %s.\n",
                    curClass.filename, lineNumber, name);
            set_type(TreeConstants.Object_);
        } else {
            if ((list.size() - 1) / 2 != actual.getLength()) {
                classTable.semantError().format(
                        "%s:%s: Incompatible number of parameters in call of method %s.\n",
                        curClass.filename, lineNumber, name);
                set_type(TreeConstants.Object_);
            } else {
                for (int i = 0; i < actual.getLength(); i++) {
                    if (!classTable.isChild(list.get(2*i + 1),
                            ((Expression) actual.getNth(i)).get_type(), 
                            curClass.name)) {
                        classTable.semantError().format(
                                "%s:%s: In call of method %s, type %s of parameter %s does not conform to declared type %s.\n",
                                curClass.filename, lineNumber, name, ((Expression) actual.getNth(i)).get_type(),
                                list.get(2*i), list.get(2*i + 1));
                        set_type(TreeConstants.Object_);
                        return;
                    }
                }
                AbstractSymbol tlast = list.get(list.size() - 1);
                if (tlast == TreeConstants.SELF_TYPE) {
                    tlast = expr.get_type();
                }
                set_type(tlast);
            }
        }
    }

}

/**
 * Defines AST constructor 'dispatch'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class dispatch extends Expression {
    protected Expression expr;
    protected AbstractSymbol name;
    protected Expressions actual;

    /**
     * Creates "dispatch" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for expr
     * @param a1
     *            initial value for name
     * @param a2
     *            initial value for actual
     */
    public dispatch(int lineNumber, Expression a1, AbstractSymbol a2,
            Expressions a3) {
        super(lineNumber);
        expr = a1;
        name = a2;
        actual = a3;
    }

    public TreeNode copy() {
        return new dispatch(lineNumber, (Expression) expr.copy(),
                copy_AbstractSymbol(name), (Expressions) actual.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "dispatch\n");
        expr.dump(out, n + 2);
        dump_AbstractSymbol(out, n + 2, name);
        actual.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_dispatch");
        expr.dump_with_types(out, n + 2);
        dump_AbstractSymbol(out, n + 2, name);
        out.println(Utilities.pad(n + 2) + "(");
        for (Enumeration e = actual.getElements(); e.hasMoreElements();) {
            ((Expression) e.nextElement()).dump_with_types(out, n + 2);
        }
        out.println(Utilities.pad(n + 2) + ")");
        dump_type(out, n);
    }

    @Override
    public void semant(ClassTable classTable, class_c curClass) {
        expr.semant(classTable, curClass);
        for (Enumeration e = actual.getElements(); e.hasMoreElements(); ) {
            ((Expression) e.nextElement()).semant(classTable, curClass);
        }
        AbstractSymbol t0 = expr.get_type();
        AbstractSymbol t0p;
        if (t0 == TreeConstants.SELF_TYPE) {
            t0p = curClass.name;
        } else {
            t0p = t0;
        }
        ArrayList<AbstractSymbol> list = classTable.queryMethodEnvironment(t0p, name);
        if (list == null) {
            classTable.semantError().format(
                    "%s:%s: Dispatch to undefined method %s.\n",
                    curClass.filename, lineNumber, name);
            set_type(TreeConstants.Object_);
        } else {
            if ((list.size() - 1) / 2 != actual.getLength()) {
                classTable.semantError().format(
                        "%s:%s: Incompatible number of parameters in call of method %s.\n",
                        curClass.filename, lineNumber, name);
                set_type(TreeConstants.Object_);
            } else {
                for (int i = 0; i < actual.getLength(); i++) {
                    if (!classTable.isChild(list.get(2*i + 1),
                            ((Expression) actual.getNth(i)).get_type(), 
                            curClass.name)) {
                        classTable.semantError().format(
                                "%s:%s: In call of method %s, type %s of parameter %s does not conform to declared type %s.\n",
                                curClass.filename, lineNumber, name, ((Expression) actual.getNth(i)).get_type(),
                                list.get(2*i), list.get(2*i + 1));
                        set_type(TreeConstants.Object_);
                        return;
                    }
                }
                AbstractSymbol tlast = list.get(list.size() - 1);
                if (tlast == TreeConstants.SELF_TYPE) {
                    tlast = t0;
                }
                set_type(tlast);
            }
        }
    }

}

/**
 * Defines AST constructor 'cond'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class cond extends Expression {
    protected Expression pred;
    protected Expression then_exp;
    protected Expression else_exp;

    /**
     * Creates "cond" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for pred
     * @param a1
     *            initial value for then_exp
     * @param a2
     *            initial value for else_exp
     */
    public cond(int lineNumber, Expression a1, Expression a2, Expression a3) {
        super(lineNumber);
        pred = a1;
        then_exp = a2;
        else_exp = a3;
    }

    public TreeNode copy() {
        return new cond(lineNumber, (Expression) pred.copy(),
                (Expression) then_exp.copy(), (Expression) else_exp.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "cond\n");
        pred.dump(out, n + 2);
        then_exp.dump(out, n + 2);
        else_exp.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_cond");
        pred.dump_with_types(out, n + 2);
        then_exp.dump_with_types(out, n + 2);
        else_exp.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    public void semant(ClassTable classTable, class_c curClass) {
        pred.semant(classTable, curClass);
        then_exp.semant(classTable, curClass);
        else_exp.semant(classTable, curClass);
        if (pred.get_type() != TreeConstants.Bool) {
            classTable.semantError().format(
                    "%s:%s: If condition does not have type Bool.\n",
                    curClass.filename, lineNumber);
            set_type(TreeConstants.Object_);
        } else {
            set_type(classTable.leastUpperBound(
                    then_exp.get_type(), else_exp.get_type(), curClass.name));
        }
    }

}

/**
 * Defines AST constructor 'loop'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class loop extends Expression {
    protected Expression pred;
    protected Expression body;

    /**
     * Creates "loop" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for pred
     * @param a1
     *            initial value for body
     */
    public loop(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        pred = a1;
        body = a2;
    }

    public TreeNode copy() {
        return new loop(lineNumber, (Expression) pred.copy(),
                (Expression) body.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "loop\n");
        pred.dump(out, n + 2);
        body.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_loop");
        pred.dump_with_types(out, n + 2);
        body.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    public void semant(ClassTable classTable, class_c curClass) {
        pred.semant(classTable, curClass);
        body.semant(classTable, curClass);
        if (pred.get_type() != TreeConstants.Bool) {
            classTable.semantError().format(
                    "%s:%s: If condition does not have type Bool.\n",
                    curClass.filename, lineNumber);
        }
        set_type(TreeConstants.Object_);
    }

}

/**
 * Defines AST constructor 'typcase'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class typcase extends Expression {
    protected Expression expr;
    protected Cases cases;

    /**
     * Creates "typcase" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for expr
     * @param a1
     *            initial value for cases
     */
    public typcase(int lineNumber, Expression a1, Cases a2) {
        super(lineNumber);
        expr = a1;
        cases = a2;
    }

    public TreeNode copy() {
        return new typcase(lineNumber, (Expression) expr.copy(),
                (Cases) cases.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "typcase\n");
        expr.dump(out, n + 2);
        cases.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_typcase");
        expr.dump_with_types(out, n + 2);
        for (Enumeration e = cases.getElements(); e.hasMoreElements();) {
            ((Case) e.nextElement()).dump_with_types(out, n + 2);
        }
        dump_type(out, n);
    }

    @Override
    public void semant(ClassTable classTable, class_c curClass) {
        expr.semant(classTable, curClass);
        HashSet<AbstractSymbol> met = new HashSet<AbstractSymbol>();
        for (Enumeration e = cases.getElements(); e.hasMoreElements(); ) {
            branch br = ((branch) e.nextElement());
            br.semant(classTable, curClass);
            if (met.contains(br.type_decl)) {
                classTable.semantError().format(
                        "%s:%s: Duplicate branch %s in case statement.\n",
                        curClass.filename, br.lineNumber, br.type_decl);
            }
            met.add(br.type_decl);
        }
        
        AbstractSymbol lub = ((branch) cases.getNth(0)).expr.get_type();
        for (int i = 1; i < cases.getLength(); i++) {
            lub = classTable.leastUpperBound(lub, 
                    ((branch) cases.getNth(i)).expr.get_type(), curClass.name);
        }
        set_type(lub);
    }

}

/**
 * Defines AST constructor 'block'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class block extends Expression {
    protected Expressions body;

    /**
     * Creates "block" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for body
     */
    public block(int lineNumber, Expressions a1) {
        super(lineNumber);
        body = a1;
    }

    public TreeNode copy() {
        return new block(lineNumber, (Expressions) body.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "block\n");
        body.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_block");
        for (Enumeration e = body.getElements(); e.hasMoreElements();) {
            ((Expression) e.nextElement()).dump_with_types(out, n + 2);
        }
        dump_type(out, n);
    }

    @Override
    public void semant(ClassTable classTable, class_c curClass) {
        for (Enumeration e = body.getElements(); e.hasMoreElements(); ) {
            ((Expression) e.nextElement()).semant(classTable, curClass);
        }
        set_type(((Expression) body.getNth(body.getLength() - 1)).get_type());
    }

}

/**
 * Defines AST constructor 'let'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class let extends Expression {
    protected AbstractSymbol identifier;
    protected AbstractSymbol type_decl;
    protected Expression init;
    protected Expression body;

    /**
     * Creates "let" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for identifier
     * @param a1
     *            initial value for type_decl
     * @param a2
     *            initial value for init
     * @param a3
     *            initial value for body
     */
    public let(int lineNumber, AbstractSymbol a1, AbstractSymbol a2,
            Expression a3, Expression a4) {
        super(lineNumber);
        identifier = a1;
        type_decl = a2;
        init = a3;
        body = a4;
    }

    public TreeNode copy() {
        return new let(lineNumber, copy_AbstractSymbol(identifier),
                copy_AbstractSymbol(type_decl), (Expression) init.copy(),
                (Expression) body.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "let\n");
        dump_AbstractSymbol(out, n + 2, identifier);
        dump_AbstractSymbol(out, n + 2, type_decl);
        init.dump(out, n + 2);
        body.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_let");
        dump_AbstractSymbol(out, n + 2, identifier);
        dump_AbstractSymbol(out, n + 2, type_decl);
        init.dump_with_types(out, n + 2);
        body.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    public void semant(ClassTable classTable, class_c curClass) {
        init.semant(classTable, curClass);
        SymbolTable env = classTable.objEnv.get(curClass.name);
        env.enterScope();
        if (identifier == TreeConstants.self) {
            classTable.semantError().format(
                    "%s:%s: 'self' cannot be bound in a 'let' expression.\n",
                    curClass.filename, lineNumber);
        } else { 
            env.addId(identifier, type_decl);
        }
        body.semant(classTable, curClass);
        env.exitScope();
        if (classTable.isChild(type_decl, init.get_type(), curClass.name)) {
            set_type(body.get_type());
        } else {
            classTable.semantError().format(
                    "%s:%s: Inferred type %s of initialization of identifier %s does not conform to declared type %s.\n",
                    curClass.filename, lineNumber, init.get_type(), identifier, type_decl);
            set_type(TreeConstants.Object_);
        }
    }

}

/**
 * Defines AST constructor 'plus'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class plus extends Expression {
    protected Expression e1;
    protected Expression e2;

    /**
     * Creates "plus" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for e1
     * @param a1
     *            initial value for e2
     */
    public plus(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }

    public TreeNode copy() {
        return new plus(lineNumber, (Expression) e1.copy(),
                (Expression) e2.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "plus\n");
        e1.dump(out, n + 2);
        e2.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_plus");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    public void semant(ClassTable classTable, class_c curClass) {
        e1.semant(classTable, curClass);
        e2.semant(classTable, curClass);
        if (e1.get_type() == TreeConstants.Int
                && e2.get_type() == TreeConstants.Int) {
            set_type(TreeConstants.Int);
        } else {
            classTable.semantError().format(
                    "%s:%s: non-Int arguments: %s + %s.\n",
                    curClass.filename, lineNumber, e1.get_type(), e2.get_type());
            set_type(TreeConstants.Object_);
        }
    }

}

/**
 * Defines AST constructor 'sub'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class sub extends Expression {
    protected Expression e1;
    protected Expression e2;

    /**
     * Creates "sub" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for e1
     * @param a1
     *            initial value for e2
     */
    public sub(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }

    public TreeNode copy() {
        return new sub(lineNumber, (Expression) e1.copy(),
                (Expression) e2.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "sub\n");
        e1.dump(out, n + 2);
        e2.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_sub");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    public void semant(ClassTable classTable, class_c curClass) {
        e1.semant(classTable, curClass);
        e2.semant(classTable, curClass);
        if (e1.get_type() == TreeConstants.Int
                && e2.get_type() == TreeConstants.Int) {
            set_type(TreeConstants.Int);
        } else {
            classTable.semantError().format(
                    "%s:%s: non-Int arguments: %s - %s.\n",
                    curClass.filename, lineNumber, e1.get_type(), e2.get_type());
            set_type(TreeConstants.Object_);
        }
    }
    
}

/**
 * Defines AST constructor 'mul'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class mul extends Expression {
    protected Expression e1;
    protected Expression e2;

    /**
     * Creates "mul" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for e1
     * @param a1
     *            initial value for e2
     */
    public mul(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }

    public TreeNode copy() {
        return new mul(lineNumber, (Expression) e1.copy(),
                (Expression) e2.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "mul\n");
        e1.dump(out, n + 2);
        e2.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_mul");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }
    
    @Override
    public void semant(ClassTable classTable, class_c curClass) {
        e1.semant(classTable, curClass);
        e2.semant(classTable, curClass);
        if (e1.get_type() == TreeConstants.Int
                && e2.get_type() == TreeConstants.Int) {
            set_type(TreeConstants.Int);
        } else {
            classTable.semantError().format(
                    "%s:%s: non-Int arguments: %s * %s.\n",
                    curClass.filename, lineNumber, e1.get_type(), e2.get_type());
            set_type(TreeConstants.Object_);
        }
    }

}

/**
 * Defines AST constructor 'divide'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class divide extends Expression {
    protected Expression e1;
    protected Expression e2;

    /**
     * Creates "divide" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for e1
     * @param a1
     *            initial value for e2
     */
    public divide(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }

    public TreeNode copy() {
        return new divide(lineNumber, (Expression) e1.copy(),
                (Expression) e2.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "divide\n");
        e1.dump(out, n + 2);
        e2.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_divide");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    public void semant(ClassTable classTable, class_c curClass) {
        e1.semant(classTable, curClass);
        e2.semant(classTable, curClass);
        if (e1.get_type() == TreeConstants.Int
                && e2.get_type() == TreeConstants.Int) {
            set_type(TreeConstants.Int);
        } else {
            classTable.semantError().format(
                    "%s:%s: non-Int arguments: %s / %s.\n",
                    curClass.filename, lineNumber, e1.get_type(), e2.get_type());
            set_type(TreeConstants.Object_);
        }
    }
    
}

/**
 * Defines AST constructor 'neg'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class neg extends Expression {
    protected Expression e1;

    /**
     * Creates "neg" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for e1
     */
    public neg(int lineNumber, Expression a1) {
        super(lineNumber);
        e1 = a1;
    }

    public TreeNode copy() {
        return new neg(lineNumber, (Expression) e1.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "neg\n");
        e1.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_neg");
        e1.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    public void semant(ClassTable classTable, class_c curClass) {
        e1.semant(classTable, curClass);
        if (e1.get_type() == TreeConstants.Int) {
            set_type(TreeConstants.Int);
        } else {
            classTable.semantError().format(
                    "%s:%s: non-Int arguments: ~%s.\n",
                    curClass.filename, lineNumber, e1.get_type());
            set_type(TreeConstants.Object_);
        }
    }

}

/**
 * Defines AST constructor 'lt'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class lt extends Expression {
    protected Expression e1;
    protected Expression e2;

    /**
     * Creates "lt" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for e1
     * @param a1
     *            initial value for e2
     */
    public lt(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }

    public TreeNode copy() {
        return new lt(lineNumber, (Expression) e1.copy(),
                (Expression) e2.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "lt\n");
        e1.dump(out, n + 2);
        e2.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_lt");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    public void semant(ClassTable classTable, class_c curClass) {
        e1.semant(classTable, curClass);
        e2.semant(classTable, curClass);
        if (e1.get_type() == TreeConstants.Int
                && e2.get_type() == TreeConstants.Int) {
            set_type(TreeConstants.Bool);
        } else {
            classTable.semantError().format(
                    "%s:%s: non-Int arguments: %s < %s.\n",
                    curClass.filename, lineNumber, e1.get_type(), e2.get_type());
            set_type(TreeConstants.Object_);
        }
    }

}

/**
 * Defines AST constructor 'eq'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class eq extends Expression {
    protected Expression e1;
    protected Expression e2;

    /**
     * Creates "eq" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for e1
     * @param a1
     *            initial value for e2
     */
    public eq(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }

    public TreeNode copy() {
        return new eq(lineNumber, (Expression) e1.copy(),
                (Expression) e2.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "eq\n");
        e1.dump(out, n + 2);
        e2.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_eq");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }
    
    @Override
    public void semant(ClassTable classTable, class_c curClass) {
        e1.semant(classTable, curClass);
        e2.semant(classTable, curClass);
        if ((e1.get_type() == TreeConstants.Int || e1.get_type() == TreeConstants.Str || e1.get_type() == TreeConstants.Bool)
         || (e2.get_type() == TreeConstants.Int || e2.get_type() == TreeConstants.Str || e2.get_type() == TreeConstants.Bool)) {
            if (e1.get_type() == e2.get_type()) {
                set_type(TreeConstants.Bool);
            } else {
                set_type(TreeConstants.Object_);
                classTable.semantError().format(
                        "%s:%s: Illegal comparison with a basic type.\n",
                        curClass.filename, lineNumber);
            }
        } else {
            set_type(TreeConstants.Bool);
        }
    }

}

/**
 * Defines AST constructor 'leq'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class leq extends Expression {
    protected Expression e1;
    protected Expression e2;

    /**
     * Creates "leq" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for e1
     * @param a1
     *            initial value for e2
     */
    public leq(int lineNumber, Expression a1, Expression a2) {
        super(lineNumber);
        e1 = a1;
        e2 = a2;
    }

    public TreeNode copy() {
        return new leq(lineNumber, (Expression) e1.copy(),
                (Expression) e2.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "leq\n");
        e1.dump(out, n + 2);
        e2.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_leq");
        e1.dump_with_types(out, n + 2);
        e2.dump_with_types(out, n + 2);
        dump_type(out, n);
    }
    
    @Override
    public void semant(ClassTable classTable, class_c curClass) {
        e1.semant(classTable, curClass);
        e2.semant(classTable, curClass);
        if (e1.get_type() == TreeConstants.Int
                && e2.get_type() == TreeConstants.Int) {
            set_type(TreeConstants.Bool);
        } else {
            classTable.semantError().format(
                    "%s:%s: non-Int arguments: %s <= %s.\n",
                    curClass.filename, lineNumber, e1.get_type(), e2.get_type());
            set_type(TreeConstants.Object_);
        }
    }

}

/**
 * Defines AST constructor 'comp'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class comp extends Expression {
    protected Expression e1;

    /**
     * Creates "comp" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for e1
     */
    public comp(int lineNumber, Expression a1) {
        super(lineNumber);
        e1 = a1;
    }

    public TreeNode copy() {
        return new comp(lineNumber, (Expression) e1.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "comp\n");
        e1.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_comp");
        e1.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    public void semant(ClassTable classTable, class_c curClass) {
        e1.semant(classTable, curClass);
        if (e1.get_type() == TreeConstants.Bool) {
            set_type(TreeConstants.Bool);
        } else {
            classTable.semantError().format(
                    "%s:%s: 'not' operand does not have type Bool.\n",
                    curClass.filename, lineNumber);
            set_type(TreeConstants.Object_);
        }
    }
    
}

/**
 * Defines AST constructor 'int_const'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class int_const extends Expression {
    protected AbstractSymbol token;

    /**
     * Creates "int_const" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for token
     */
    public int_const(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        token = a1;
    }

    public TreeNode copy() {
        return new int_const(lineNumber, copy_AbstractSymbol(token));
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "int_const\n");
        dump_AbstractSymbol(out, n + 2, token);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_int");
        dump_AbstractSymbol(out, n + 2, token);
        dump_type(out, n);
    }

    @Override
    public void semant(ClassTable classTable, class_c curClass) {
        set_type(TreeConstants.Int);        
    }

}

/**
 * Defines AST constructor 'bool_const'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class bool_const extends Expression {
    protected Boolean val;

    /**
     * Creates "bool_const" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for val
     */
    public bool_const(int lineNumber, Boolean a1) {
        super(lineNumber);
        val = a1;
    }

    public TreeNode copy() {
        return new bool_const(lineNumber, copy_Boolean(val));
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "bool_const\n");
        dump_Boolean(out, n + 2, val);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_bool");
        dump_Boolean(out, n + 2, val);
        dump_type(out, n);
    }

    @Override
    public void semant(ClassTable classTable, class_c curClass) {
        set_type(TreeConstants.Bool);
    }

}

/**
 * Defines AST constructor 'string_const'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class string_const extends Expression {
    protected AbstractSymbol token;

    /**
     * Creates "string_const" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for token
     */
    public string_const(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        token = a1;
    }

    public TreeNode copy() {
        return new string_const(lineNumber, copy_AbstractSymbol(token));
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "string_const\n");
        dump_AbstractSymbol(out, n + 2, token);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_string");
        out.print(Utilities.pad(n + 2) + "\"");
        Utilities.printEscapedString(out, token.getString());
        out.println("\"");
        dump_type(out, n);
    }

    @Override
    public void semant(ClassTable classTable, class_c curClass) {
        set_type(TreeConstants.Str);
    }

}

/**
 * Defines AST constructor 'new_'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class new_ extends Expression {
    protected AbstractSymbol type_name;

    /**
     * Creates "new_" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for type_name
     */
    public new_(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        type_name = a1;
    }

    public TreeNode copy() {
        return new new_(lineNumber, copy_AbstractSymbol(type_name));
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "new_\n");
        dump_AbstractSymbol(out, n + 2, type_name);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_new");
        dump_AbstractSymbol(out, n + 2, type_name);
        dump_type(out, n);
    }

    @Override
    public void semant(ClassTable classTable, class_c curClass) {
        if (type_name == TreeConstants.SELF_TYPE) {
            set_type(TreeConstants.SELF_TYPE);
        } else if (classTable.containsClass(type_name)) {
            set_type(type_name);
        } else {
            classTable.semantError().format(
                    "%s:%s: 'new' used with undefined class %s.\n",
                    curClass.filename, lineNumber, type_name);
            set_type(TreeConstants.Object_);
        }
    }

}

/**
 * Defines AST constructor 'isvoid'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class isvoid extends Expression {
    protected Expression e1;

    /**
     * Creates "isvoid" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for e1
     */
    public isvoid(int lineNumber, Expression a1) {
        super(lineNumber);
        e1 = a1;
    }

    public TreeNode copy() {
        return new isvoid(lineNumber, (Expression) e1.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "isvoid\n");
        e1.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_isvoid");
        e1.dump_with_types(out, n + 2);
        dump_type(out, n);
    }

    @Override
    public void semant(ClassTable classTable, class_c curClass) {
        e1.semant(classTable, curClass);
        set_type(TreeConstants.Bool);
    }

}

/**
 * Defines AST constructor 'no_expr'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class no_expr extends Expression {

    /**
     * Creates "no_expr" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     */
    public no_expr(int lineNumber) {
        super(lineNumber);
    }

    public TreeNode copy() {
        return new no_expr(lineNumber);
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "no_expr\n");
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_no_expr");
        dump_type(out, n);
    }

    @Override
    public void semant(ClassTable classTable, class_c curClass) {
        set_type(TreeConstants.No_type);
    }

}

/**
 * Defines AST constructor 'object'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class object extends Expression {
    protected AbstractSymbol name;

    /**
     * Creates "object" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for name
     */
    public object(int lineNumber, AbstractSymbol a1) {
        super(lineNumber);
        name = a1;
    }

    public TreeNode copy() {
        return new object(lineNumber, copy_AbstractSymbol(name));
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "object\n");
        dump_AbstractSymbol(out, n + 2, name);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_object");
        dump_AbstractSymbol(out, n + 2, name);
        dump_type(out, n);
    }

    @Override
    public void semant(ClassTable classTable, class_c curClass) {
        AbstractSymbol type = classTable.queryObjectEnvironment(curClass.name, name);
        if (type == null) {
            classTable.semantError().format(
                    "%s:%s: Undeclared identifier %s.\n",
                    curClass.filename, lineNumber, name);
            set_type(TreeConstants.Object_);
        } else {
            set_type(type);
        }
    }

}
