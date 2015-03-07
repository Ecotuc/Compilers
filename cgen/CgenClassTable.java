/*
Copyright (c) 2000 The Regents of the University of California.
All rights reserved.

Permission to use, copy, modify, and distribute this software for any
purpose, without fee, and without written agreement is hereby granted,
provided that the above copyright notice and the following two
paragraphs appear in all copies of this software.

IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR
DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT
OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE UNIVERSITY OF
CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS
ON AN "AS IS" BASIS, AND THE UNIVERSITY OF CALIFORNIA HAS NO OBLIGATION TO
PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 */

// This is a project skeleton file

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.Stack;
import java.util.Enumeration;

/**
 * This class is used for representing the inheritance tree during code
 * generation. You will need to fill in some of its methods and potentially
 * extend it in other useful ways.
 */
class CgenClassTable extends SymbolTable {

    /** All classes in the program, represented as CgenNode */
    private Vector nds;

    /** This is the stream to which assembly instructions are output */
    private PrintStream str;

    private int stringclasstag;
    private int intclasstag;
    private int boolclasstag;

    // whether garbage collector is used?
    public boolean gcUsed; 
    
    // Useful when generating code for labels
    public int labelCount;

    public HashMap<AbstractSymbol, HashMap<AbstractSymbol, Integer>> attrOffsetMap;
    public HashMap<AbstractSymbol, HashMap<AbstractSymbol, Integer>> methodOffsetMap;
    
    // two HashMap are combined to store <class>.<method>
    public HashMap<AbstractSymbol, ArrayList<AbstractSymbol>> classMap;
    public HashMap<AbstractSymbol, ArrayList<AbstractSymbol>> methodMap;

    // store height of class in inheritance tree.
    private HashMap<AbstractSymbol, Integer> heightMap;
    // store range of classtag for children of class (include itself) in
    // inheritance tree.
    // Because I use depth first search to tag class, so the range is interval,
    // saved as [min, max]
    // This property is convenient for code generation for 'case'
    private HashMap<AbstractSymbol, int[]> rangeMap;

    public int getHeight(AbstractSymbol className) {
        return heightMap.get(className);
    }

    public int[] getChildrenRange(AbstractSymbol className) {
        return rangeMap.get(className);
    }

    // Inner use by sortNdsAndInitMap
    private void dfs(CgenNode nd) {
        nds.add(nd);
        int height = heightMap.get(nd.name);
        int min = nds.indexOf(nd);
        int max = min;

        for (Enumeration e = nd.getChildren(); e.hasMoreElements();) {
            CgenNode childNode = (CgenNode) e.nextElement();
            heightMap.put(childNode.name, height + 1);
            dfs(childNode);
            max = rangeMap.get(childNode.name)[1];
        }

        rangeMap.put(nd.name, new int[] { min, max });
    }

    // sort nds according to depth first search on inheritance tree
    // and initialize heightMap and rangeMap
    private void sortNdsAndInitMap() {
        nds.clear();
        heightMap = new HashMap<AbstractSymbol, Integer>();
        rangeMap = new HashMap<AbstractSymbol, int[]>();
        CgenNode root = root();
        heightMap.put(root.name, 0);
        dfs(root);
    }

    // The following methods emit code for constants and global
    // declarations.

    /**
     * Emits code to start the .data segment and to declare the global names.
     * */
    private void codeGlobalData() {
        // The following global names must be defined first.

        str.print("\t.data\n" + CgenSupport.ALIGN);
        str.println(CgenSupport.GLOBAL + CgenSupport.CLASSNAMETAB);
        str.print(CgenSupport.GLOBAL);
        CgenSupport.emitProtObjRef(TreeConstants.Main, str);
        str.println("");
        str.print(CgenSupport.GLOBAL);
        CgenSupport.emitProtObjRef(TreeConstants.Int, str);
        str.println("");
        str.print(CgenSupport.GLOBAL);
        CgenSupport.emitProtObjRef(TreeConstants.Str, str);
        str.println("");
        str.print(CgenSupport.GLOBAL);
        BoolConst.falsebool.codeRef(str);
        str.println("");
        str.print(CgenSupport.GLOBAL);
        BoolConst.truebool.codeRef(str);
        str.println("");
        str.println(CgenSupport.GLOBAL + CgenSupport.INTTAG);
        str.println(CgenSupport.GLOBAL + CgenSupport.BOOLTAG);
        str.println(CgenSupport.GLOBAL + CgenSupport.STRINGTAG);

        // We also need to know the tag of the Int, String, and Bool classes
        // during code generation.

        str.println(CgenSupport.INTTAG + CgenSupport.LABEL + CgenSupport.WORD
                + intclasstag);
        str.println(CgenSupport.BOOLTAG + CgenSupport.LABEL + CgenSupport.WORD
                + boolclasstag);
        str.println(CgenSupport.STRINGTAG + CgenSupport.LABEL
                + CgenSupport.WORD + stringclasstag);

    }

    /**
     * Emits code to start the .text segment and to declare the global names.
     * */
    private void codeGlobalText() {
        str.println(CgenSupport.GLOBAL + CgenSupport.HEAP_START);
        str.print(CgenSupport.HEAP_START + CgenSupport.LABEL);
        str.println(CgenSupport.WORD + 0);
        str.println("\t.text");
        str.print(CgenSupport.GLOBAL);
        CgenSupport.emitInitRef(TreeConstants.Main, str);
        str.println("");
        str.print(CgenSupport.GLOBAL);
        CgenSupport.emitInitRef(TreeConstants.Int, str);
        str.println("");
        str.print(CgenSupport.GLOBAL);
        CgenSupport.emitInitRef(TreeConstants.Str, str);
        str.println("");
        str.print(CgenSupport.GLOBAL);
        CgenSupport.emitInitRef(TreeConstants.Bool, str);
        str.println("");
        str.print(CgenSupport.GLOBAL);
        CgenSupport.emitMethodRef(TreeConstants.Main, TreeConstants.main_meth,
                str);
        str.println("");
    }

    /** Emits code definitions for boolean constants. */
    private void codeBools(int classtag) {
        BoolConst.falsebool.codeDef(classtag, str);
        BoolConst.truebool.codeDef(classtag, str);
    }

    /** Generates GC choice constants (pointers to GC functions) */
    private void codeSelectGc() {
        str.println(CgenSupport.GLOBAL + "_MemMgr_INITIALIZER");
        str.println("_MemMgr_INITIALIZER:");
        str.println(CgenSupport.WORD
                + CgenSupport.gcInitNames[Flags.cgen_Memmgr]);

        str.println(CgenSupport.GLOBAL + "_MemMgr_COLLECTOR");
        str.println("_MemMgr_COLLECTOR:");
        str.println(CgenSupport.WORD
                + CgenSupport.gcCollectNames[Flags.cgen_Memmgr]);

        str.println(CgenSupport.GLOBAL + "_MemMgr_TEST");
        str.println("_MemMgr_TEST:");
        str.println(CgenSupport.WORD
                + ((Flags.cgen_Memmgr_Test == Flags.GC_TEST) ? "1" : "0"));
    }

    /**
     * Emits code to reserve space for and initialize all of the constants.
     * Class names should have been added to the string table (in the supplied
     * code, it is done during the construction of the inheritance graph), and
     * code for emitting string constants as a side effect adds the string's
     * length to the integer table. The constants are emitted by running through
     * the stringtable and inttable and producing code for each entry.
     */
    private void codeConstants() {
        // Add constants that are required by the code generator.
        AbstractTable.stringtable.addString("");
        AbstractTable.inttable.addString("0");

        AbstractTable.stringtable.codeStringTable(stringclasstag, str);
        AbstractTable.inttable.codeStringTable(intclasstag, str);
        codeBools(boolclasstag);
    }

    private void codeClassNameTable() {
        str.print(CgenSupport.CLASSNAMETAB + CgenSupport.LABEL);
        for (int i = 0; i < nds.size(); i++) {
            str.print(CgenSupport.WORD);
            AbstractSymbol name = ((CgenNode) nds.get(i)).name;
            ((StringSymbol) AbstractTable.stringtable
                    .addString(name.toString())).codeRef(str);
            str.println("");
        }
    }

    // class_objTab is useful for 'self' and 'SELF_TYPE'
    private void codeClassObjectTable() {
        str.print(CgenSupport.CLASSOBJTAB + CgenSupport.LABEL);
        for (int i = 0; i < nds.size(); i++) {
            AbstractSymbol name = ((CgenNode) nds.get(i)).name;
            str.print(CgenSupport.WORD);
            CgenSupport.emitProtObjRef(name, str);
            str.println("");

            str.print(CgenSupport.WORD);
            CgenSupport.emitInitRef(name, str);
            str.println("");
        }
    }

    private void codePrototypeObjects() {

        // used to store attributes of class
        HashMap<AbstractSymbol, ArrayList<AbstractSymbol>> attrNameMap = new HashMap<AbstractSymbol, ArrayList<AbstractSymbol>>();
        HashMap<AbstractSymbol, ArrayList<AbstractSymbol>> attrTypeMap = new HashMap<AbstractSymbol, ArrayList<AbstractSymbol>>();

        attrNameMap
                .put(TreeConstants.No_class, new ArrayList<AbstractSymbol>());
        attrTypeMap
                .put(TreeConstants.No_class, new ArrayList<AbstractSymbol>());

        Stack<CgenNode> todoList = new Stack<CgenNode>();
        todoList.add((CgenNode) probe(TreeConstants.Object_));

        while (!todoList.isEmpty()) {
            CgenNode nd = todoList.pop();
            AbstractSymbol className = nd.name;

            ArrayList<AbstractSymbol> attrNameList = new ArrayList<AbstractSymbol>(
                    attrNameMap.get(nd.getParent()));
            ArrayList<AbstractSymbol> attrTypeList = new ArrayList<AbstractSymbol>(
                    attrTypeMap.get(nd.getParent()));

            for (Enumeration e = nd.features.getElements(); e.hasMoreElements();) {
                Object ft = e.nextElement();
                if (ft instanceof attr) {
                    attr a = (attr) ft;
                    attrNameList.add(a.name);
                    attrTypeList.add(a.type_decl);
                }
            }

            str.println(CgenSupport.WORD + "-1");
            CgenSupport.emitProtObjRef(className, str);
            str.print(CgenSupport.LABEL);

            int classTag = nds.indexOf(nd);
            str.println(CgenSupport.WORD + classTag);

            int objectSize = attrNameList.size()
                    + CgenSupport.DEFAULT_OBJFIELDS;
            str.println(CgenSupport.WORD + objectSize);

            str.print(CgenSupport.WORD);
            CgenSupport.emitDispTableRef(className, str);
            str.println("");

            for (int i = 0; i < attrTypeList.size(); i++) {
                str.print(CgenSupport.WORD);

                AbstractSymbol type = attrTypeList.get(i);
                if (type == TreeConstants.Int || type == TreeConstants.Str
                        || type == TreeConstants.Bool) {
                    CgenSupport.emitProtObjRef(type, str);
                } else {
                    str.print(0);
                }

                str.println("");
            }

            attrNameMap.put(className, attrNameList);
            attrTypeMap.put(className, attrTypeList);

            HashMap<AbstractSymbol, Integer> attrOffset = new HashMap<AbstractSymbol, Integer>();
            for (int i = 0; i < attrNameList.size(); i++) {
                attrOffset.put(attrNameList.get(i),
                        CgenSupport.DEFAULT_OBJFIELDS + i);
            }
            attrOffsetMap.put(className, attrOffset);

            for (Enumeration e = nd.getChildren(); e.hasMoreElements();) {
                todoList.push((CgenNode) e.nextElement());
            }
        }
    }

    private void codeDispatchTables() {
        // two HashMap are combined to store <class>.<method>
        classMap = new HashMap<AbstractSymbol, ArrayList<AbstractSymbol>>();
        methodMap = new HashMap<AbstractSymbol, ArrayList<AbstractSymbol>>();

        classMap.put(TreeConstants.No_class, new ArrayList<AbstractSymbol>());
        methodMap.put(TreeConstants.No_class, new ArrayList<AbstractSymbol>());

        Stack<CgenNode> todoList = new Stack<CgenNode>();
        todoList.add((CgenNode) probe(TreeConstants.Object_));

        while (!todoList.isEmpty()) {
            CgenNode nd = todoList.pop();
            AbstractSymbol className = nd.name;

            ArrayList<AbstractSymbol> classList = new ArrayList<AbstractSymbol>(
                    classMap.get(nd.getParent()));
            ArrayList<AbstractSymbol> methodList = new ArrayList<AbstractSymbol>(
                    methodMap.get(nd.getParent()));

            for (Enumeration e = nd.features.getElements(); e.hasMoreElements();) {
                Object ft = e.nextElement();
                if (ft instanceof method) {
                    AbstractSymbol methodName = ((method) ft).name;

                    int index = methodList.indexOf(methodName);
                    if (index == -1) {
                        classList.add(className);
                        methodList.add(methodName);
                    } else {
                        classList.set(index, className);
                    }
                }
            }

            CgenSupport.emitDispTableRef(className, str);
            str.print(CgenSupport.LABEL);

            for (int i = 0; i < methodList.size(); i++) {
                str.print(CgenSupport.WORD);
                CgenSupport.emitMethodRef(classList.get(i), methodList.get(i),
                        str);
                str.println("");
            }

            classMap.put(className, classList);
            methodMap.put(className, methodList);

            HashMap<AbstractSymbol, Integer> methodOffset = new HashMap<AbstractSymbol, Integer>();
            for (int i = 0; i < methodList.size(); i++) {
                methodOffset.put(methodList.get(i), i);
            }
            methodOffsetMap.put(className, methodOffset);

            for (Enumeration e = nd.getChildren(); e.hasMoreElements();) {
                todoList.push((CgenNode) e.nextElement());
            }
        }
    }

    private void codeObjectInitializer() {
        for (Enumeration e1 = nds.elements(); e1.hasMoreElements();) {
            CgenNode nd = (CgenNode) e1.nextElement();

            CgenSupport.emitInitRef(nd.name, str);
            str.print(CgenSupport.LABEL);

            CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, -3
                    * CgenSupport.WORD_SIZE, str);
            CgenSupport.emitStore(CgenSupport.FP, 3, CgenSupport.SP, str);
            CgenSupport.emitStore(CgenSupport.SELF, 2, CgenSupport.SP, str);
            CgenSupport.emitStore(CgenSupport.RA, 1, CgenSupport.SP, str);

            CgenSupport.emitAddiu(CgenSupport.FP, CgenSupport.SP,
                    CgenSupport.WORD_SIZE * 3, str);
            CgenSupport.emitMove(CgenSupport.SELF, CgenSupport.ACC, str);

            if (nd.getParent() != TreeConstants.No_class) {
                str.print(CgenSupport.JAL);
                CgenSupport.emitInitRef(nd.getParent(), str);
                str.println();
            }

            HashMap<AbstractSymbol, Integer> offsetMap = attrOffsetMap
                    .get(nd.name);
            for (Enumeration e2 = nd.features.getElements(); e2
                    .hasMoreElements();) {
                Object ft = e2.nextElement();
                if (ft instanceof attr) {
                    attr a = (attr) ft;
                    if (!(a.init instanceof no_expr)) {
                        SymbolTable env = new SymbolTable();
                        env.enterScope();
                        a.init.code(str, nd.name, this, env, 3);
                        int offset = offsetMap.get(a.name);
                        CgenSupport.emitStore(CgenSupport.ACC, offset,
                                CgenSupport.SELF, str);
                        env.exitScope();
                        
                        if (gcUsed) {
                            // _GenGC_Assign saves $a0, so no need to save here
                            CgenSupport.emitAddiu(CgenSupport.A1, CgenSupport.SELF, offset * CgenSupport.WORD_SIZE, str);
                            CgenSupport.emitJal("_GenGC_Assign", str);
                        }
                    }
                }
            }

            CgenSupport.emitMove(CgenSupport.ACC, CgenSupport.SELF, str);
            CgenSupport.emitLoad(CgenSupport.FP, 3, CgenSupport.SP, str);
            CgenSupport.emitLoad(CgenSupport.SELF, 2, CgenSupport.SP, str);
            CgenSupport.emitLoad(CgenSupport.RA, 1, CgenSupport.SP, str);
            CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP,
                    3 * CgenSupport.WORD_SIZE, str);
            CgenSupport.emitReturn(str);
        }
    }

    private void codeClassMethods() {
        for (Enumeration e1 = nds.elements(); e1.hasMoreElements();) {
            CgenNode nd = (CgenNode) e1.nextElement();
            if (nd.basic()) {
                continue;
            }

            for (Enumeration e2 = nd.features.getElements(); e2
                    .hasMoreElements();) {
                Object ft = e2.nextElement();
                if (ft instanceof method) {
                    method m = (method) ft;
                    CgenSupport.emitMethodRef(nd.name, m.name, str);
                    str.print(CgenSupport.LABEL);

                    CgenSupport.emitAddiu(CgenSupport.SP, CgenSupport.SP, -3
                            * CgenSupport.WORD_SIZE, str);
                    CgenSupport.emitStore(CgenSupport.FP, 3, CgenSupport.SP,
                            str);
                    CgenSupport.emitStore(CgenSupport.SELF, 2, CgenSupport.SP,
                            str);
                    CgenSupport.emitStore(CgenSupport.RA, 1, CgenSupport.SP,
                            str);

                    CgenSupport.emitAddiu(CgenSupport.FP, CgenSupport.SP,
                            CgenSupport.WORD_SIZE * 3, str);
                    CgenSupport
                            .emitMove(CgenSupport.SELF, CgenSupport.ACC, str);

                    SymbolTable env = new SymbolTable();
                    env.enterScope();
                    for (int i = 0; i < m.formals.getLength(); i++) {
                        env.addId(((formal) m.formals.getNth(i)).name,
                                m.formals.getLength() - i);
                    }

                    m.expr.code(str, nd.name, this, env, 3);

                    CgenSupport
                            .emitLoad(CgenSupport.FP, 3, CgenSupport.SP, str);
                    CgenSupport.emitLoad(CgenSupport.SELF, 2, CgenSupport.SP,
                            str);
                    CgenSupport
                            .emitLoad(CgenSupport.RA, 1, CgenSupport.SP, str);
                    CgenSupport
                            .emitAddiu(CgenSupport.SP, CgenSupport.SP,
                                    (3 + m.formals.getLength())
                                            * CgenSupport.WORD_SIZE, str);
                    CgenSupport.emitReturn(str);
                }
            }
        }
    }

    /**
     * Creates data structures representing basic Cool classes (Object, IO, Int,
     * Bool, String). Please note: as is this method does not do anything
     * useful; you will need to edit it to make if do what you want.
     * */
    private void installBasicClasses() {
        AbstractSymbol filename = AbstractTable.stringtable
                .addString("<basic class>");

        // A few special class names are installed in the lookup table
        // but not the class list. Thus, these classes exist, but are
        // not part of the inheritance hierarchy. No_class serves as
        // the parent of Object and the other special classes.
        // SELF_TYPE is the self class; it cannot be redefined or
        // inherited. prim_slot is a class known to the code generator.

        addId(TreeConstants.No_class, new CgenNode(new class_(0,
                TreeConstants.No_class, TreeConstants.No_class,
                new Features(0), filename), CgenNode.Basic, this));

        addId(TreeConstants.SELF_TYPE, new CgenNode(new class_(0,
                TreeConstants.SELF_TYPE, TreeConstants.No_class,
                new Features(0), filename), CgenNode.Basic, this));

        addId(TreeConstants.prim_slot, new CgenNode(new class_(0,
                TreeConstants.prim_slot, TreeConstants.No_class,
                new Features(0), filename), CgenNode.Basic, this));

        // The Object class has no parent class. Its methods are
        // cool_abort() : Object aborts the program
        // type_name() : Str returns a string representation
        // of class name
        // copy() : SELF_TYPE returns a copy of the object

        class_ Object_class = new class_(
                0,
                TreeConstants.Object_,
                TreeConstants.No_class,
                new Features(0)
                        .appendElement(
                                new method(0, TreeConstants.cool_abort,
                                        new Formals(0), TreeConstants.Object_,
                                        new no_expr(0)))
                        .appendElement(
                                new method(0, TreeConstants.type_name,
                                        new Formals(0), TreeConstants.Str,
                                        new no_expr(0)))
                        .appendElement(
                                new method(0, TreeConstants.copy,
                                        new Formals(0),
                                        TreeConstants.SELF_TYPE, new no_expr(0))),
                filename);

        installClass(new CgenNode(Object_class, CgenNode.Basic, this));

        // The IO class inherits from Object. Its methods are
        // out_string(Str) : SELF_TYPE writes a string to the output
        // out_int(Int) : SELF_TYPE "    an int    " "     "
        // in_string() : Str reads a string from the input
        // in_int() : Int "   an int     " "     "

        class_ IO_class = new class_(
                0,
                TreeConstants.IO,
                TreeConstants.Object_,
                new Features(0)
                        .appendElement(
                                new method(0, TreeConstants.out_string,
                                        new Formals(0)
                                                .appendElement(new formal(0,
                                                        TreeConstants.arg,
                                                        TreeConstants.Str)),
                                        TreeConstants.SELF_TYPE, new no_expr(0)))
                        .appendElement(
                                new method(0, TreeConstants.out_int,
                                        new Formals(0)
                                                .appendElement(new formal(0,
                                                        TreeConstants.arg,
                                                        TreeConstants.Int)),
                                        TreeConstants.SELF_TYPE, new no_expr(0)))
                        .appendElement(
                                new method(0, TreeConstants.in_string,
                                        new Formals(0), TreeConstants.Str,
                                        new no_expr(0)))
                        .appendElement(
                                new method(0, TreeConstants.in_int,
                                        new Formals(0), TreeConstants.Int,
                                        new no_expr(0))), filename);

        installClass(new CgenNode(IO_class, CgenNode.Basic, this));

        // The Int class has no methods and only a single attribute, the
        // "val" for the integer.

        class_ Int_class = new class_(0, TreeConstants.Int,
                TreeConstants.Object_, new Features(0).appendElement(new attr(
                        0, TreeConstants.val, TreeConstants.prim_slot,
                        new no_expr(0))), filename);

        installClass(new CgenNode(Int_class, CgenNode.Basic, this));

        // Bool also has only the "val" slot.
        class_ Bool_class = new class_(0, TreeConstants.Bool,
                TreeConstants.Object_, new Features(0).appendElement(new attr(
                        0, TreeConstants.val, TreeConstants.prim_slot,
                        new no_expr(0))), filename);

        installClass(new CgenNode(Bool_class, CgenNode.Basic, this));

        // The class Str has a number of slots and operations:
        // val the length of the string
        // str_field the string itself
        // length() : Int returns length of the string
        // concat(arg: Str) : Str performs string concatenation
        // substr(arg: Int, arg2: Int): Str substring selection

        class_ Str_class = new class_(
                0,
                TreeConstants.Str,
                TreeConstants.Object_,
                new Features(0)
                        .appendElement(
                                new attr(0, TreeConstants.val,
                                        TreeConstants.Int, new no_expr(0)))
                        .appendElement(
                                new attr(0, TreeConstants.str_field,
                                        TreeConstants.prim_slot, new no_expr(0)))
                        .appendElement(
                                new method(0, TreeConstants.length,
                                        new Formals(0), TreeConstants.Int,
                                        new no_expr(0)))
                        .appendElement(
                                new method(0, TreeConstants.concat,
                                        new Formals(0)
                                                .appendElement(new formal(0,
                                                        TreeConstants.arg,
                                                        TreeConstants.Str)),
                                        TreeConstants.Str, new no_expr(0)))
                        .appendElement(
                                new method(
                                        0,
                                        TreeConstants.substr,
                                        new Formals(0)
                                                .appendElement(
                                                        new formal(
                                                                0,
                                                                TreeConstants.arg,
                                                                TreeConstants.Int))
                                                .appendElement(
                                                        new formal(
                                                                0,
                                                                TreeConstants.arg2,
                                                                TreeConstants.Int)),
                                        TreeConstants.Str, new no_expr(0))),
                filename);

        installClass(new CgenNode(Str_class, CgenNode.Basic, this));
    }

    // The following creates an inheritance graph from
    // a list of classes. The graph is implemented as
    // a tree of `CgenNode', and class names are placed
    // in the base class symbol table.

    private void installClass(CgenNode nd) {
        AbstractSymbol name = nd.getName();
        if (probe(name) != null)
            return;
        nds.addElement(nd);
        addId(name, nd);
    }

    private void installClasses(Classes cs) {
        for (Enumeration e = cs.getElements(); e.hasMoreElements();) {
            installClass(new CgenNode((Class_) e.nextElement(),
                    CgenNode.NotBasic, this));
        }
    }

    private void buildInheritanceTree() {
        for (Enumeration e = nds.elements(); e.hasMoreElements();) {
            setRelations((CgenNode) e.nextElement());
        }
    }

    private void setRelations(CgenNode nd) {
        CgenNode parent = (CgenNode) probe(nd.getParent());
        nd.setParentNd(parent);
        parent.addChild(nd);
    }

    /** Constructs a new class table and invokes the code generator */
    public CgenClassTable(Classes cls, PrintStream str) {
        labelCount = 0;

        nds = new Vector();

        methodOffsetMap = new HashMap<AbstractSymbol, HashMap<AbstractSymbol, Integer>>();
        attrOffsetMap = new HashMap<AbstractSymbol, HashMap<AbstractSymbol, Integer>>();

        this.str = str;

        enterScope();
        if (Flags.cgen_debug)
            System.out.println("Building CgenClassTable");

        installBasicClasses();
        installClasses(cls);
        buildInheritanceTree();

        sortNdsAndInitMap();

        stringclasstag = nds.indexOf(probe(TreeConstants.Str));
        intclasstag = nds.indexOf(probe(TreeConstants.Int));
        boolclasstag = nds.indexOf(probe(TreeConstants.Bool));

        if (Flags.cgen_Memmgr == Flags.GC_NOGC) {
            gcUsed = false;
        } else {
            gcUsed = true;
        }
        
        code();

        exitScope();
    }

    /**
     * This method is the meat of the code generator. It is to be filled in
     * programming assignment 5
     */
    public void code() {
        if (Flags.cgen_debug)
            System.out.println("coding global data");
        codeGlobalData();

        if (Flags.cgen_debug)
            System.out.println("choosing gc");
        codeSelectGc();

        if (Flags.cgen_debug)
            System.out.println("coding constants");
        codeConstants();

        // class_nameTab
        if (Flags.cgen_debug) {
            System.out.println("coding class_nameTab");
        }
        codeClassNameTable();

        // class_objTab
        if (Flags.cgen_debug) {
            System.out.println("coding class_objTab");
        }
        codeClassObjectTable();

        // dispatch tables
        if (Flags.cgen_debug) {
            System.out.println("coding dispatch tables");
        }
        codeDispatchTables();

        // prototype objects
        if (Flags.cgen_debug) {
            System.out.println("coding prototype objects");
        }
        codePrototypeObjects();

        if (Flags.cgen_debug)
            System.out.println("coding global text");
        codeGlobalText();

        // object initializer
        if (Flags.cgen_debug)
            System.out.println("coding object initializer");
        codeObjectInitializer();

        // class methods
        if (Flags.cgen_debug)
            System.out.println("coding class methods");
        codeClassMethods();
    }

    /** Gets the root of the inheritance tree */
    public CgenNode root() {
        return (CgenNode) probe(TreeConstants.Object_);
    }

}
