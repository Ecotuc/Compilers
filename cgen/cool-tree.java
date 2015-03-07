// -*- mode: java -*- 
//
// file: cool-tree.m4
//
// This file defines the AST
//
//////////////////////////////////////////////////////////

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.io.PrintStream;
import java.util.Vector;

/** Defines simple phylum Program */
abstract class Program extends TreeNode {
    protected Program(int lineNumber) {
        super(lineNumber);
    }

    public abstract void dump_with_types(PrintStream out, int n);

    public abstract void semant();

    public abstract void cgen(PrintStream s);

}

/** Defines simple phylum Class_ */
abstract class Class_ extends TreeNode {
    protected Class_(int lineNumber) {
        super(lineNumber);
    }

    public abstract void dump_with_types(PrintStream out, int n);

    public abstract AbstractSymbol getName();

    public abstract AbstractSymbol getParent();

    public abstract AbstractSymbol getFilename();

    public abstract Features getFeatures();

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

    /**
     * Generates code for expression. Keep the following invariants: save the
     * result in $acc($a0) remain registers $fp $self($s0) unmodified keep stack
     * as is
     * 
     * @param s
     *            the output stream
     * @curClass which class the expression appears in
     * @param table
     *            CgenClassTable holds all other useful information
     * @param env
     *            stores location of local variables relative to frame pointer
     * @param sp
     *            represents the difference of current sp and fp (wordsize
     *            counts as 1). Useful for 'let'. (We need to know the location
     *            on stack of the variables introduced by let)
     * 
     */
    public abstract void code(PrintStream s, AbstractSymbol curClass,
            CgenClassTable table, SymbolTable env, int sp);

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
 * Defines AST constructor 'program'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class program extends Program {
    public Classes classes;

    /**
     * Creates "program" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for classes
     */
    public program(int lineNumber, Classes a1) {
        super(lineNumber);
        classes = a1;
    }

    public TreeNode copy() {
        return new program(lineNumber, (Classes) classes.copy());
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "program\n");
        classes.dump(out, n + 2);
    }

    public void dump_with_types(PrintStream out, int n) {
        dump_line(out, n);
        out.println(Utilities.pad(n) + "_program");
        for (Enumeration e = classes.getElements(); e.hasMoreElements();) {
            ((Class_) e.nextElement()).dump_with_types(out, n + 1);
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

        /* some semantic analysis code may go here */

        if (classTable.errors()) {
            System.err
                    .println("Compilation halted due to static semantic errors.");
            System.exit(1);
        }
    }

    /**
     * This method is the entry point to the code generator. All of the work of
     * the code generator takes place within CgenClassTable constructor.
     * 
     * @param s
     *            the output stream
     * @see CgenClassTable
     * */
    public void cgen(PrintStream s) {
        // spim wants comments to start with '#'
        s.print("# start of generated code\n");

        CgenClassTable codegen_classtable = new CgenClassTable(classes, s);

        s.print("\n# end of generated code\n");
    }

}

/**
 * Defines AST constructor 'class_'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class class_ extends Class_ {
    public AbstractSymbol name;
    public AbstractSymbol parent;
    public Features features;
    public AbstractSymbol filename;

    /**
     * Creates "class_" AST node.
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
    public class_(int lineNumber, AbstractSymbol a1, AbstractSymbol a2,
            Features a3, AbstractSymbol a4) {
        super(lineNumber);
        name = a1;
        parent = a2;
        features = a3;
        filename = a4;
    }

    public TreeNode copy() {
        return new class_(lineNumber, copy_AbstractSymbol(name),
                copy_AbstractSymbol(parent), (Features) features.copy(),
                copy_AbstractSymbol(filename));
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "class_\n");
        dump_AbstractSymbol(out, n + 2, name);
        dump_AbstractSymbol(out, n + 2, parent);
        features.dump(out, n + 2);
        dump_AbstractSymbol(out, n + 2, filename);
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

    public AbstractSymbol getName() {
        return name;
    }

    public AbstractSymbol getParent() {
        return parent;
    }

    public AbstractSymbol getFilename() {
        return filename;
    }

    public Features getFeatures() {
        return features;
    }

}

/**
 * Defines AST constructor 'method'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class method extends Feature {
    public AbstractSymbol name;
    public Formals formals;
    public AbstractSymbol return_type;
    public Expression expr;

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

}

/**
 * Defines AST constructor 'attr'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class attr extends Feature {
    public AbstractSymbol name;
    public AbstractSymbol type_decl;
    public Expression init;

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

}

/**
 * Defines AST constructor 'formal'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class formal extends Formal {
    public AbstractSymbol name;
    public AbstractSymbol type_decl;

    /**
     * Creates "formal" AST node.
     * 
     * @param lineNumber
     *            the line in the source file from which this node came.
     * @param a0
     *            initial value for name
     * @param a1
     *            initial value for type_decl
     */
    public formal(int lineNumber, AbstractSymbol a1, AbstractSymbol a2) {
        super(lineNumber);
        name = a1;
        type_decl = a2;
    }

    public TreeNode copy() {
        return new formal(lineNumber, copy_AbstractSymbol(name),
                copy_AbstractSymbol(type_decl));
    }

    public void dump(PrintStream out, int n) {
        out.print(Utilities.pad(n) + "formal\n");
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
    public AbstractSymbol name;
    public AbstractSymbol type_decl;
    public Expression expr;

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

}

/**
 * Defines AST constructor 'assign'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class assign extends Expression {
    public AbstractSymbol name;
    public Expression expr;

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
    public void code(PrintStream s, AbstractSymbol curClass,
            CgenClassTable table, SymbolTable env, int sp) {

        expr.code(s, curClass, table, env, sp);

        Integer offset;
        if ((offset = (Integer) env.lookup(name)) != null) {
            CgenSupport.emitStore(CgenSupport.ACC, offset, CgenSupport.FP, s);
        } else {
            offset = table.attrOffsetMap.get(curClass).get(name);
            CgenSupport.emitStore(CgenSupport.ACC, offset, CgenSupport.SELF, s);
            
            if (table.gcUsed) {
                // _GenGC_Assign saves $a0, so no need to save here
                CgenSupport.emitAddiu(CgenSupport.A1, CgenSupport.SELF, offset * CgenSupport.WORD_SIZE, s);
                CgenSupport.emitJal("_GenGC_Assign", s);
            }
        }
    }

}

/**
 * Defines AST constructor 'static_dispatch'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class static_dispatch extends Expression {
    public Expression expr;
    public AbstractSymbol type_name;
    public AbstractSymbol name;
    public Expressions actual;

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
    public void code(PrintStream s, AbstractSymbol curClass,
            CgenClassTable table, SymbolTable env, int sp) {
        for (Enumeration e = actual.getElements(); e.hasMoreElements();) {
            ((Expression) e.nextElement()).code(s, curClass, table, env, sp);
            CgenSupport.emitPush(CgenSupport.ACC, s);
            sp++;
        }

        expr.code(s, curClass, table, env, sp);
        int label = table.labelCount;
        CgenSupport.emitBne(CgenSupport.ACC, CgenSupport.ZERO,
                table.labelCount++, s);
        CgenSupport.emitLoadString(CgenSupport.ACC,
                (StringSymbol) AbstractTable.stringtable
                        .lookup(((CgenNode) table.probe(curClass)).filename
                                .toString()), s);
        CgenSupport.emitLoadImm(CgenSupport.T1, lineNumber, s);
        CgenSupport.emitJal("_dispatch_abort", s);

        CgenSupport.emitLabelDef(label, s);
        int offset = table.methodOffsetMap.get(type_name).get(name);
        AbstractSymbol type = table.classMap.get(type_name).get(offset);
        CgenSupport.emitJal(type + "." + name, s);
    }
}

/**
 * Defines AST constructor 'dispatch'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class dispatch extends Expression {
    public Expression expr;
    public AbstractSymbol name;
    public Expressions actual;

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
    public void code(PrintStream s, AbstractSymbol curClass,
            CgenClassTable table, SymbolTable env, int sp) {
        for (Enumeration e = actual.getElements(); e.hasMoreElements();) {
            ((Expression) e.nextElement()).code(s, curClass, table, env, sp);
            CgenSupport.emitPush(CgenSupport.ACC, s);
            sp++;
        }

        expr.code(s, curClass, table, env, sp);

        int label = table.labelCount;
        CgenSupport.emitBne(CgenSupport.ACC, CgenSupport.ZERO,
                table.labelCount++, s);
        CgenSupport.emitLoadString(CgenSupport.ACC,
                (StringSymbol) AbstractTable.stringtable
                        .lookup(((CgenNode) table.probe(curClass)).filename
                                .toString()), s);
        CgenSupport.emitLoadImm(CgenSupport.T1, lineNumber, s);
        CgenSupport.emitJal("_dispatch_abort", s);

        CgenSupport.emitLabelDef(label, s);

        // Very weird here. Seems to be the only place we need to know the type
        // information?
        AbstractSymbol type = expr.get_type();
        if (type == TreeConstants.SELF_TYPE) {
            type = curClass;
        }
        int offset = table.methodOffsetMap.get(type).get(name);
        CgenSupport.emitLoad(CgenSupport.T1, CgenSupport.DISPTABLE_OFFSET,
                CgenSupport.ACC, s);
        CgenSupport.emitLoad(CgenSupport.T1, offset, CgenSupport.T1, s);

        CgenSupport.emitJalr(CgenSupport.T1, s);
    }
}

/**
 * Defines AST constructor 'cond'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class cond extends Expression {
    public Expression pred;
    public Expression then_exp;
    public Expression else_exp;

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
    public void code(PrintStream s, AbstractSymbol curClass,
            CgenClassTable table, SymbolTable env, int sp) {
        pred.code(s, curClass, table, env, sp);
        CgenSupport.emitLoad(CgenSupport.T1, CgenSupport.DEFAULT_OBJFIELDS,
                CgenSupport.ACC, s);

        // table.labelCount will change in code generation for subexpression,
        // so we need to record it.
        int label1 = table.labelCount;
        CgenSupport.emitBeqz(CgenSupport.T1, table.labelCount++, s);

        then_exp.code(s, curClass, table, env, sp);
        int label2 = table.labelCount;
        CgenSupport.emitBranch(table.labelCount++, s);

        CgenSupport.emitLabelDef(label1, s);
        else_exp.code(s, curClass, table, env, sp);

        CgenSupport.emitLabelDef(label2, s);
    }

}

/**
 * Defines AST constructor 'loop'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class loop extends Expression {
    public Expression pred;
    public Expression body;

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
    public void code(PrintStream s, AbstractSymbol curClass,
            CgenClassTable table, SymbolTable env, int sp) {

        int label1 = table.labelCount;
        CgenSupport.emitLabelDef(table.labelCount++, s);
        pred.code(s, curClass, table, env, sp);

        CgenSupport.emitLoad(CgenSupport.T1, CgenSupport.DEFAULT_OBJFIELDS,
                CgenSupport.ACC, s);
        int label2 = table.labelCount;
        CgenSupport.emitBeqz(CgenSupport.T1, table.labelCount++, s);

        body.code(s, curClass, table, env, sp);
        CgenSupport.emitBranch(label1, s);

        CgenSupport.emitLabelDef(label2, s);
        CgenSupport.emitMove(CgenSupport.ACC, CgenSupport.ZERO, s);

    }
}

/**
 * Defines AST constructor 'typcase'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class typcase extends Expression {
    public Expression expr;
    public Cases cases;

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
    public void code(PrintStream s, AbstractSymbol curClass,
            CgenClassTable table, SymbolTable env, int sp) {

        // Tricky part
        // Sort cases in descending order according to height in class
        // inheritance tree, so we can capture least ancester first!
        ArrayList<branch> branchList = new ArrayList<branch>();
        for (int i = 0; i < cases.getLength(); i++) {
            branchList.add((branch) cases.getNth(i));
        }

        // Use a very simple selection sort here.
        for (int i = 0; i < branchList.size(); i++) {
            int maxHeight = table.getHeight(branchList.get(i).type_decl);
            int maxIndex = i;
            for (int j = i + 1; j < branchList.size(); j++) {
                int height = table.getHeight(branchList.get(j).type_decl);
                if (height > maxHeight) {
                    maxIndex = j;
                    maxHeight = height;
                }
            }
            branch tmp = branchList.get(i);
            branchList.set(i, branchList.get(maxIndex));
            branchList.set(maxIndex, tmp);
        }

        int label = table.labelCount;
        table.labelCount += cases.getLength() + 2;

        expr.code(s, curClass, table, env, sp);
        CgenSupport.emitBne(CgenSupport.ACC, CgenSupport.ZERO, label + 1, s);
        CgenSupport.emitLoadString(CgenSupport.ACC,
                (StringSymbol) AbstractTable.stringtable
                        .lookup(((CgenNode) table.probe(curClass)).filename
                                .toString()), s);
        CgenSupport.emitLoadImm(CgenSupport.T1, lineNumber, s);
        CgenSupport.emitJal("_case_abort2", s);

        CgenSupport.emitLabelDef(label + 1, s);
        CgenSupport.emitLoad(CgenSupport.T2, 0, CgenSupport.ACC, s);

        for (int i = 0; i < branchList.size(); i++) {
            branch br = branchList.get(i);
            int max = table.getChildrenRange(br.type_decl)[1];
            int min = table.getChildrenRange(br.type_decl)[0];

            CgenSupport.emitBlti(CgenSupport.T2, min, label + i + 2, s);
            CgenSupport.emitBgti(CgenSupport.T2, max, label + i + 2, s);

            CgenSupport.emitPush(CgenSupport.ACC, s);
            env.enterScope();
            env.addId(br.name, -sp);

            br.expr.code(s, curClass, table, env, sp + 1);
            env.exitScope();

            CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP,
                    CgenSupport.WORD_SIZE, s);

            CgenSupport.emitBranch(label, s);
            CgenSupport.emitLabelDef(label + i + 2, s);
        }

        CgenSupport.emitJal("_case_abort", s);

        CgenSupport.emitLabelDef(label, s);
    }

}

/**
 * Defines AST constructor 'block'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class block extends Expression {
    public Expressions body;

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
    public void code(PrintStream s, AbstractSymbol curClass,
            CgenClassTable table, SymbolTable env, int sp) {
        for (Enumeration e = body.getElements(); e.hasMoreElements();) {
            Expression ex = (Expression) e.nextElement();
            ex.code(s, curClass, table, env, sp);
        }

    }
}

/**
 * Defines AST constructor 'let'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class let extends Expression {
    public AbstractSymbol identifier;
    public AbstractSymbol type_decl;
    public Expression init;
    public Expression body;

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
    public void code(PrintStream s, AbstractSymbol curClass,
            CgenClassTable table, SymbolTable env, int sp) {
        if (init instanceof no_expr) {
            if (type_decl == TreeConstants.Int
                    || type_decl == TreeConstants.Bool
                    || type_decl == TreeConstants.Str) {
                CgenSupport.emitLoadAddress(CgenSupport.ACC, type_decl
                        + CgenSupport.PROTOBJ_SUFFIX, s);
            } else {
                CgenSupport.emitMove(CgenSupport.ACC, CgenSupport.ZERO, s);
            }
        } else {
            init.code(s, curClass, table, env, sp);
        }

        CgenSupport.emitPush(CgenSupport.ACC, s);

        env.enterScope();
        env.addId(identifier, -sp);
        sp++;
        body.code(s, curClass, table, env, sp);
        env.exitScope();

        CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP,
                CgenSupport.WORD_SIZE, s);
    }
}

/**
 * Defines AST constructor 'plus'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class plus extends Expression {
    public Expression e1;
    public Expression e2;

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
    public void code(PrintStream s, AbstractSymbol curClass,
            CgenClassTable table, SymbolTable env, int sp) {
        e1.code(s, curClass, table, env, sp);
        CgenSupport.emitPush(CgenSupport.ACC, s);
        sp++;

        e2.code(s, curClass, table, env, sp);
        CgenSupport.emitJal("Object.copy", s);
        CgenSupport.emitFetchInt(CgenSupport.T2, CgenSupport.ACC, s);
        CgenSupport.emitPop(CgenSupport.T1, s);
        CgenSupport.emitFetchInt(CgenSupport.T1, CgenSupport.T1, s);
        sp--;

        CgenSupport.emitAdd(CgenSupport.T1, CgenSupport.T1, CgenSupport.T2, s);
        CgenSupport.emitStoreInt(CgenSupport.T1, CgenSupport.ACC, s);
    }

}

/**
 * Defines AST constructor 'sub'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class sub extends Expression {
    public Expression e1;
    public Expression e2;

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
    public void code(PrintStream s, AbstractSymbol curClass,
            CgenClassTable table, SymbolTable env, int sp) {
        e1.code(s, curClass, table, env, sp);
        CgenSupport.emitPush(CgenSupport.ACC, s);
        sp++;

        e2.code(s, curClass, table, env, sp);
        CgenSupport.emitJal("Object.copy", s);
        CgenSupport.emitFetchInt(CgenSupport.T2, CgenSupport.ACC, s);
        CgenSupport.emitPop(CgenSupport.T1, s);
        CgenSupport.emitFetchInt(CgenSupport.T1, CgenSupport.T1, s);
        sp--;

        CgenSupport.emitSub(CgenSupport.T1, CgenSupport.T1, CgenSupport.T2, s);
        CgenSupport.emitStoreInt(CgenSupport.T1, CgenSupport.ACC, s);
    }

}

/**
 * Defines AST constructor 'mul'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class mul extends Expression {
    public Expression e1;
    public Expression e2;

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
    public void code(PrintStream s, AbstractSymbol curClass,
            CgenClassTable table, SymbolTable env, int sp) {
        e1.code(s, curClass, table, env, sp);
        CgenSupport.emitPush(CgenSupport.ACC, s);
        sp++;

        e2.code(s, curClass, table, env, sp);
        CgenSupport.emitJal("Object.copy", s);
        CgenSupport.emitFetchInt(CgenSupport.T2, CgenSupport.ACC, s);
        CgenSupport.emitPop(CgenSupport.T1, s);
        CgenSupport.emitFetchInt(CgenSupport.T1, CgenSupport.T1, s);
        sp--;

        CgenSupport.emitMul(CgenSupport.T1, CgenSupport.T1, CgenSupport.T2, s);
        CgenSupport.emitStoreInt(CgenSupport.T1, CgenSupport.ACC, s);
    }

}

/**
 * Defines AST constructor 'divide'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class divide extends Expression {
    public Expression e1;
    public Expression e2;

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
    public void code(PrintStream s, AbstractSymbol curClass,
            CgenClassTable table, SymbolTable env, int sp) {
        e1.code(s, curClass, table, env, sp);
        CgenSupport.emitPush(CgenSupport.ACC, s);
        sp++;

        e2.code(s, curClass, table, env, sp);
        CgenSupport.emitJal("Object.copy", s);
        CgenSupport.emitFetchInt(CgenSupport.T2, CgenSupport.ACC, s);
        CgenSupport.emitPop(CgenSupport.T1, s);
        CgenSupport.emitFetchInt(CgenSupport.T1, CgenSupport.T1, s);
        sp--;

        CgenSupport.emitDiv(CgenSupport.T1, CgenSupport.T1, CgenSupport.T2, s);
        CgenSupport.emitStoreInt(CgenSupport.T1, CgenSupport.ACC, s);
    }

}

/**
 * Defines AST constructor 'neg'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class neg extends Expression {
    public Expression e1;

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
    public void code(PrintStream s, AbstractSymbol curClass,
            CgenClassTable table, SymbolTable env, int sp) {

        e1.code(s, curClass, table, env, sp);
        CgenSupport.emitJal("Object.copy", s);
        CgenSupport.emitFetchInt(CgenSupport.T1, CgenSupport.ACC, s);
        CgenSupport.emitNeg(CgenSupport.T1, CgenSupport.T1, s);
        CgenSupport.emitStoreInt(CgenSupport.T1, CgenSupport.ACC, s);
    }

}

/**
 * Defines AST constructor 'lt'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class lt extends Expression {
    public Expression e1;
    public Expression e2;

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
    public void code(PrintStream s, AbstractSymbol curClass,
            CgenClassTable table, SymbolTable env, int sp) {

        e1.code(s, curClass, table, env, sp);
        CgenSupport.emitPush(CgenSupport.ACC, s);
        sp++;

        e2.code(s, curClass, table, env, sp);
        CgenSupport.emitFetchInt(CgenSupport.T2, CgenSupport.ACC, s);

        CgenSupport.emitPop(CgenSupport.T1, s);
        CgenSupport.emitFetchInt(CgenSupport.T1, CgenSupport.T1, s);
        sp--;

        CgenSupport.emitLoadBool(CgenSupport.ACC, BoolConst.truebool, s);
        CgenSupport.emitBlt(CgenSupport.T1, CgenSupport.T2, table.labelCount++,
                s);
        CgenSupport.emitLoadBool(CgenSupport.ACC, BoolConst.falsebool, s);

        CgenSupport.emitLabelDef(table.labelCount - 1, s);
    }

}

/**
 * Defines AST constructor 'eq'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class eq extends Expression {
    public Expression e1;
    public Expression e2;

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
    public void code(PrintStream s, AbstractSymbol curClass,
            CgenClassTable table, SymbolTable env, int sp) {
        e1.code(s, curClass, table, env, sp);
        CgenSupport.emitPush(CgenSupport.ACC, s);
        sp++;
        e2.code(s, curClass, table, env, sp);
        CgenSupport.emitPop(CgenSupport.T1, s);
        CgenSupport.emitMove(CgenSupport.T2, CgenSupport.ACC, s);

        // The provided "equality_test" in trap.handler assumes t1 != t2,
        // so we need to handle the case separately
        CgenSupport.emitLoadBool(CgenSupport.ACC, BoolConst.truebool, s);
        int label = table.labelCount;
        CgenSupport.emitBeq(CgenSupport.T1, CgenSupport.T2, table.labelCount++,
                s);

        CgenSupport.emitLoadBool(CgenSupport.A1, BoolConst.falsebool, s);
        CgenSupport.emitJal("equality_test", s);

        CgenSupport.emitLabelDef(label, s);
    }
}

/**
 * Defines AST constructor 'leq'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class leq extends Expression {
    public Expression e1;
    public Expression e2;

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
    public void code(PrintStream s, AbstractSymbol curClass,
            CgenClassTable table, SymbolTable env, int sp) {

        e1.code(s, curClass, table, env, sp);
        CgenSupport.emitPush(CgenSupport.ACC, s);
        sp++;

        e2.code(s, curClass, table, env, sp);
        CgenSupport.emitFetchInt(CgenSupport.T2, CgenSupport.ACC, s);

        CgenSupport.emitPop(CgenSupport.T1, s);
        CgenSupport.emitFetchInt(CgenSupport.T1, CgenSupport.T1, s);
        sp--;

        CgenSupport.emitLoadBool(CgenSupport.ACC, BoolConst.truebool, s);
        CgenSupport.emitBleq(CgenSupport.T1, CgenSupport.T2,
                table.labelCount++, s);
        CgenSupport.emitLoadBool(CgenSupport.ACC, BoolConst.falsebool, s);

        CgenSupport.emitLabelDef(table.labelCount - 1, s);
    }

}

/**
 * Defines AST constructor 'comp'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class comp extends Expression {
    public Expression e1;

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
    public void code(PrintStream s, AbstractSymbol curClass,
            CgenClassTable table, SymbolTable env, int sp) {

        e1.code(s, curClass, table, env, sp);
        CgenSupport.emitLoad(CgenSupport.T1, CgenSupport.DEFAULT_OBJFIELDS,
                CgenSupport.ACC, s);
        CgenSupport.emitLoadBool(CgenSupport.ACC, BoolConst.truebool, s);
        CgenSupport.emitBeqz(CgenSupport.T1, table.labelCount++, s);
        CgenSupport.emitLoadBool(CgenSupport.ACC, BoolConst.falsebool, s);

        CgenSupport.emitLabelDef(table.labelCount - 1, s);
    }

}

/**
 * Defines AST constructor 'int_const'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class int_const extends Expression {
    public AbstractSymbol token;

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
    public void code(PrintStream s, AbstractSymbol curClass,
            CgenClassTable table, SymbolTable env, int sp) {
        CgenSupport
                .emitLoadInt(CgenSupport.ACC,
                        (IntSymbol) AbstractTable.inttable.lookup(token
                                .getString()), s);
    }

}

/**
 * Defines AST constructor 'bool_const'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class bool_const extends Expression {
    public Boolean val;

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
    public void code(PrintStream s, AbstractSymbol curClass,
            CgenClassTable table, SymbolTable env, int sp) {
        CgenSupport.emitLoadBool(CgenSupport.ACC, new BoolConst(val), s);
    }

}

/**
 * Defines AST constructor 'string_const'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class string_const extends Expression {
    public AbstractSymbol token;

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
    public void code(PrintStream s, AbstractSymbol curClass,
            CgenClassTable table, SymbolTable env, int sp) {
        CgenSupport.emitLoadString(CgenSupport.ACC,
                (StringSymbol) AbstractTable.stringtable.lookup(token
                        .getString()), s);
    }

}

/**
 * Defines AST constructor 'new_'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class new_ extends Expression {
    public AbstractSymbol type_name;

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
    public void code(PrintStream s, AbstractSymbol curClass,
            CgenClassTable table, SymbolTable env, int sp) {
        if (type_name == TreeConstants.SELF_TYPE) {
            CgenSupport.emitLoadAddress(CgenSupport.T1,
                    CgenSupport.CLASSOBJTAB, s);
            CgenSupport.emitLoad(CgenSupport.T2, 0, CgenSupport.SELF, s);
            CgenSupport.emitSll(CgenSupport.T2, CgenSupport.T2, 3, s);
            CgenSupport.emitAddu(CgenSupport.T1, CgenSupport.T1,
                    CgenSupport.T2, s);
            CgenSupport.emitPush(CgenSupport.T1, s);

            CgenSupport.emitLoad(CgenSupport.ACC, 0, CgenSupport.T1, s);
            CgenSupport.emitJal("Object.copy", s);

            CgenSupport.emitPop(CgenSupport.T1, s);
            CgenSupport.emitLoad(CgenSupport.T1, 1, CgenSupport.T1, s);
            CgenSupport.emitJalr(CgenSupport.T1, s);

        } else {
            CgenSupport.emitPartialLoadAddress(CgenSupport.ACC, s);
            CgenSupport.emitProtObjRef(type_name, s);
            s.println("");

            CgenSupport.emitJal("Object.copy", s);
            CgenSupport.emitJal(type_name + CgenSupport.CLASSINIT_SUFFIX, s);
        }
    }
}

/**
 * Defines AST constructor 'isvoid'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class isvoid extends Expression {
    public Expression e1;

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
    public void code(PrintStream s, AbstractSymbol curClass,
            CgenClassTable table, SymbolTable env, int sp) {

        e1.code(s, curClass, table, env, sp);
        CgenSupport.emitMove(CgenSupport.T1, CgenSupport.ACC, s);

        CgenSupport.emitLoadBool(CgenSupport.ACC, BoolConst.truebool, s);
        CgenSupport.emitBeqz(CgenSupport.T1, table.labelCount++, s);
        CgenSupport.emitLoadBool(CgenSupport.ACC, BoolConst.falsebool, s);

        CgenSupport.emitLabelDef(table.labelCount - 1, s);
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
    public void code(PrintStream s, AbstractSymbol curClass,
            CgenClassTable table, SymbolTable env, int sp) {
        // It's Ok to do nothing here
    }

}

/**
 * Defines AST constructor 'object'.
 * <p>
 * See <a href="TreeNode.html">TreeNode</a> for full documentation.
 */
class object extends Expression {
    public AbstractSymbol name;

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
    public void code(PrintStream s, AbstractSymbol curClass,
            CgenClassTable table, SymbolTable env, int sp) {

        if (name == TreeConstants.self) {
            CgenSupport.emitMove(CgenSupport.ACC, CgenSupport.SELF, s);
            return;
        }

        Integer offset;
        if ((offset = (Integer) env.lookup(name)) != null) {
            CgenSupport.emitLoad(CgenSupport.ACC, offset, CgenSupport.FP, s);
        } else {
            offset = table.attrOffsetMap.get(curClass).get(name);
            CgenSupport.emitLoad(CgenSupport.ACC, offset, CgenSupport.SELF, s);
        }
    }

}
