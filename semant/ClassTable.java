import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * This class may be used to contain the semantic information such as the
 * inheritance graph. You may use it or not as you like: it is only here to
 * provide a container for the supplied methods.
 */
class ClassTable {
    private int semantErrors;
    private PrintStream errorStream;
    private ClassNode root;
    private HashMap<AbstractSymbol, ClassNode> classMap;
    // For convenience, set environment public
    public HashMap<AbstractSymbol, SymbolTable> objEnv;
    public HashMap<AbstractSymbol, SymbolTable> methodEnv;
    
    private class ClassNode {
        private class_c curClass;
        private ArrayList<ClassNode> children;
        
        private ClassNode(class_c curClass) {
            this.curClass = curClass;
            this.children = new ArrayList<ClassNode>();
        }
        
        private void addChild(ClassNode child) {
            this.children.add(child);
        }
    }
    
    private void initRoot(class_c curClass) {
        root = new ClassNode(curClass);
        classMap.put(curClass.name, root);
    }
    
    private void addBasicClass(class_c curClass) {
        ClassNode node = new ClassNode(curClass);
        classMap.put(curClass.name, node);
        classMap.get(curClass.parent).addChild(node);
    }
    
    private void BuildInheritGraph(Classes cls) {
        for (Enumeration e = cls.getElements(); e.hasMoreElements(); ) {
            class_c curClass = (class_c) e.nextElement();
            if (curClass.name == TreeConstants.SELF_TYPE ||
                curClass.name == TreeConstants.Object_ ||
                curClass.name == TreeConstants.Int ||
                curClass.name == TreeConstants.Bool ||
                curClass.name == TreeConstants.Str ||
                curClass.name == TreeConstants.IO) {
                semantError().format(
                        "%s:%s: Redefinition of basic class %s.\n",
                        curClass.filename, curClass.lineNumber, curClass.name);
                return;
            }
            ClassNode node = new ClassNode(curClass);
            if (classMap.containsKey(curClass.name)) {
                semantError().format(
                        "%s:%s: Class %s was previously defined.\n",
                        curClass.filename, curClass.lineNumber, curClass.name);
                return;
            }
            classMap.put(curClass.name, node);
        }
        
        for (Enumeration e = cls.getElements(); e.hasMoreElements(); ) {
            class_c curClass = (class_c) e.nextElement();
            
            if (curClass.parent == TreeConstants.Int ||
                    curClass.parent == TreeConstants.Bool ||
                    curClass.parent == TreeConstants.Str ||
                    curClass.parent == TreeConstants.SELF_TYPE) {
                semantError().format(
                        "%s:%s: Class %s cannot inherit class %s.\n",
                        curClass.filename, curClass.lineNumber, curClass.name, curClass.parent);
                return;
            } 

            ClassNode node = classMap.get(curClass.parent);
            if (node == null) {
                    semantError().format(
                            "%s:%s: Class %s inherits from an undefined class %s.\n",
                            curClass.filename, curClass.lineNumber, curClass.name, curClass.parent);
                    return;
            }
            node.addChild(classMap.get(curClass.name));
        }
        
        checkCycles();
    }
    
    private void checkCycles() {
        // DFS
        HashSet<ClassNode> visited = new HashSet<ClassNode>();
        dfs(root, visited);
        
        if (visited.size() < classMap.size()) {
            semantError().println("Inheritance graph has cycles.");
        }
    }
    
    private void dfs(ClassNode curNode, HashSet<ClassNode> visited) {
        visited.add(curNode);
        for (Iterator<ClassNode> iter = curNode.children.iterator(); iter.hasNext(); ) {
            dfs(iter.next(), visited);
        }
    }
    
    /*private void checkCycles() {
        // DFS
        HashSet<ClassNode> visited = new HashSet<ClassNode>();
        if (!dfs(root, visited)) {
            return;
        }
        
        for (Iterator<AbstractSymbol> iter = classMap.keySet().iterator(); iter.hasNext(); ) {
            ClassNode node = classMap.get(iter.next());
            if (!visited.contains(node)) {
                if (!dfs(node, visited)) {
                    return;
                }
            }
        }
    }
    
    private boolean dfs(ClassNode curNode, HashSet<ClassNode> visited) {
        if (visited.contains(curNode)) {
            semantError(curNode.class_c).println("Inheritance graph has cycles.");
            return false;
        }
        
        visited.add(curNode);
        for (Iterator<ClassNode> iter = curNode.children.iterator(); iter.hasNext(); ) {
            if (!dfs(iter.next(), visited)) {
                return false;
            }
        }
        return true;
    }*/
    
    /**
     * Require the inheritance graph to be valid. whether c2 <= c1
     * 
     * @param c1
     *            do not need to be in classMap
     * @param c2
     *            must in classMap
     */
    public boolean isChild(AbstractSymbol c1, AbstractSymbol c2, AbstractSymbol curClass) {
        if (c2 == TreeConstants.No_type) {
            return true;
        }
        
        if (c1 == TreeConstants.SELF_TYPE) {
            if (c2 == TreeConstants.SELF_TYPE) {
                return true;
            } else {
                return false;
            }
        }
        
        AbstractSymbol c;
        if (c2 == TreeConstants.SELF_TYPE) {
            c = curClass;
        } else {
            c = c2;
        }
        
        while (c != TreeConstants.No_class) {
            if (c == c1) {
                return true;
            }
            c = classMap.get(c).curClass.parent;
        }
        return false;
    }
    
    public void buildEnvironment() {
        objEnv = new HashMap<AbstractSymbol, SymbolTable>();
        methodEnv = new HashMap<AbstractSymbol, SymbolTable>();
        for (Iterator<Entry<AbstractSymbol, ClassNode>> itr = 
                classMap.entrySet().iterator(); itr.hasNext(); ) {
            Entry<AbstractSymbol, ClassNode> entry = itr.next();
            AbstractSymbol className = entry.getKey();
            class_c curClass = entry.getValue().curClass;
            
            SymbolTable objTab = new SymbolTable();
            objTab.enterScope();
            SymbolTable methodTab = new SymbolTable();
            methodTab.enterScope();
            
            for (Enumeration e = curClass.features.getElements(); e.hasMoreElements(); ) {
                Feature ft = (Feature) e.nextElement();
                if (ft instanceof attr) {
                    attr a = (attr) ft;
                    objTab.addId(a.name, a.type_decl);
                } else if (ft instanceof method) {
                    method m = (method) ft;
                    ArrayList<AbstractSymbol> list = new ArrayList<AbstractSymbol>();
                    for (int i = 0; i < m.formals.getLength(); i++) {
                        list.add(((formalc) m.formals.getNth(i)).name);
                        list.add(((formalc) m.formals.getNth(i)).type_decl);
                    }
                    list.add(m.return_type);
                    methodTab.addId(m.name, list);
                } else {
                    assert false : "Should never reach here.";
                }
            }
            objTab.addId(TreeConstants.self, TreeConstants.SELF_TYPE);
            objEnv.put(className, objTab);
            methodEnv.put(className, methodTab);
        }
        
        if (!classMap.containsKey(TreeConstants.Main)) {
            semantError().println("Class Main is not defined.");
        } else {
            ClassNode node = classMap.get(TreeConstants.Main);
            ArrayList<AbstractSymbol> paraList = 
                    (ArrayList<AbstractSymbol>) methodEnv.get(TreeConstants.Main).probe(TreeConstants.main_meth);
            if (paraList == null) {
                semantError().format(
                        "%s:%s: Method main is not defined in class Main.\n",
                        node.curClass.filename, node.curClass.lineNumber);
            } else if (paraList.size() > 1) {
                semantError().format("%s:%s: Method main in class Main takes no formal parameters.\n",
                        node.curClass.filename, node.curClass.lineNumber);
            }
        }
    }
    
    public boolean containsClass(AbstractSymbol className) {
        return classMap.containsKey(className);
    }
    
    /**
     * c1 and c2 must be in classMap.
     * 
     * @param c1
     * @param c2
     * @param curClass
     * @return
     */
    public AbstractSymbol leastUpperBound(AbstractSymbol c1, AbstractSymbol c2, AbstractSymbol curClass) {
        if (c1 == TreeConstants.SELF_TYPE) {
            if (c2 == TreeConstants.SELF_TYPE) {
                return TreeConstants.SELF_TYPE;
            } else {
                c1 = curClass;
            }
        } else if (c2 == TreeConstants.SELF_TYPE) {
            c2 = curClass;
        }
        
        ArrayList<AbstractSymbol> path1 = new ArrayList<AbstractSymbol>();
        ArrayList<AbstractSymbol> path2 = new ArrayList<AbstractSymbol>();
        while (c1 != TreeConstants.No_class) {
            path1.add(0, c1);
            c1 = classMap.get(c1).curClass.parent;
        }
        while (c2 != TreeConstants.No_class) {
            path2.add(0, c2);
            c2 = classMap.get(c2).curClass.parent;
        }
        
        int i = 0;
        while(i < path1.size() 
                && i < path2.size() 
                && path1.get(i) == path2.get(i)) { 
            i++;
        }
        return path1.get(i - 1);
    }
    
    public AbstractSymbol queryObjectEnvironment(AbstractSymbol curClass, AbstractSymbol objId) {
        AbstractSymbol c = curClass;
        while (c != TreeConstants.No_class) {
            AbstractSymbol type = (AbstractSymbol) objEnv.get(c).lookup(objId);
            if (type != null) {
                return type;
            }
            c = classMap.get(c).curClass.parent;
        }
        return null;
    }
    
    public ArrayList<AbstractSymbol> queryMethodEnvironment(AbstractSymbol curClass, AbstractSymbol methodId) {
        AbstractSymbol c = curClass;
        while (c != TreeConstants.No_class) {
            ArrayList<AbstractSymbol> ret = (ArrayList<AbstractSymbol>) methodEnv.get(c).lookup(methodId);
            if (ret != null) {
                return ret;
            }
            c = classMap.get(c).curClass.parent;
        }
        return null;
    }
    
    /**
     * Creates data structures representing basic Cool classes (Object, IO, Int,
     * Bool, String). Please note: as is this method does not do anything
     * useful; you will need to edit it to make if do what you want.
     * */
    private void installBasicClasses() {
        AbstractSymbol filename = AbstractTable.stringtable
                .addString("<basic class>");

        // The following demonstrates how to create dummy parse trees to
        // refer to basic Cool classes. There's no need for method
        // bodies -- these are already built into the runtime system.

        // IMPORTANT: The results of the following expressions are
        // stored in local variables. You will want to do something
        // with those variables at the end of this method to make this
        // code meaningful.

        // The Object class has no parent class. Its methods are
        // cool_abort() : Object aborts the program
        // type_name() : Str returns a string representation
        // of class name
        // copy() : SELF_TYPE returns a copy of the object

        class_c Object_class = new class_c(
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

        // The IO class inherits from Object. Its methods are
        // out_string(Str) : SELF_TYPE writes a string to the output
        // out_int(Int) : SELF_TYPE "    an int    " "     "
        // in_string() : Str reads a string from the input
        // in_int() : Int "   an int     " "     "

        class_c IO_class = new class_c(
                0,
                TreeConstants.IO,
                TreeConstants.Object_,
                new Features(0)
                        .appendElement(
                                new method(0, TreeConstants.out_string,
                                        new Formals(0)
                                                .appendElement(new formalc(0,
                                                        TreeConstants.arg,
                                                        TreeConstants.Str)),
                                        TreeConstants.SELF_TYPE, new no_expr(0)))
                        .appendElement(
                                new method(0, TreeConstants.out_int,
                                        new Formals(0)
                                                .appendElement(new formalc(0,
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

        // The Int class has no methods and only a single attribute, the
        // "val" for the integer.

        class_c Int_class = new class_c(0, TreeConstants.Int,
                TreeConstants.Object_, new Features(0).appendElement(new attr(
                        0, TreeConstants.val, TreeConstants.prim_slot,
                        new no_expr(0))), filename);

        // Bool also has only the "val" slot.
        class_c Bool_class = new class_c(0, TreeConstants.Bool,
                TreeConstants.Object_, new Features(0).appendElement(new attr(
                        0, TreeConstants.val, TreeConstants.prim_slot,
                        new no_expr(0))), filename);

        // The class Str has a number of slots and operations:
        // val the length of the string
        // str_field the string itself
        // length() : Int returns length of the string
        // concat(arg: Str) : Str performs string concatenation
        // substr(arg: Int, arg2: Int): Str substring selection

        class_c Str_class = new class_c(
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
                                                .appendElement(new formalc(0,
                                                        TreeConstants.arg,
                                                        TreeConstants.Str)),
                                        TreeConstants.Str, new no_expr(0)))
                        .appendElement(
                                new method(
                                        0,
                                        TreeConstants.substr,
                                        new Formals(0)
                                                .appendElement(
                                                        new formalc(
                                                                0,
                                                                TreeConstants.arg,
                                                                TreeConstants.Int))
                                                .appendElement(
                                                        new formalc(
                                                                0,
                                                                TreeConstants.arg2,
                                                                TreeConstants.Int)),
                                        TreeConstants.Str, new no_expr(0))),
                filename);

        /*
         * Do somethind with Object_class, IO_class, Int_class, Bool_class, and
         * Str_class here
         */
        initRoot(Object_class);
        addBasicClass(IO_class);
        addBasicClass(Int_class);
        addBasicClass(Bool_class);
        addBasicClass(Str_class);
    }

    public ClassTable(Classes cls) {
        semantErrors = 0;
        errorStream = System.err;

        /* fill this in */
        classMap = new HashMap<AbstractSymbol, ClassNode>();
        installBasicClasses();
        BuildInheritGraph(cls);
    }

    /**
     * Prints line number and file name of the given class.
     * 
     * Also increments semantic error count.
     * 
     * @param c
     *            the class
     * @return a print stream to which the rest of the error message is to be
     *         printed.
     * 
     * */
    public PrintStream semantError(class_c c) {
        return semantError(c.getFilename(), c);
    }

    /**
     * Prints the file name and the line number of the given tree node.
     * 
     * Also increments semantic error count.
     * 
     * @param filename
     *            the file name
     * @param t
     *            the tree node
     * @return a print stream to which the rest of the error message is to be
     *         printed.
     * 
     * */
    public PrintStream semantError(AbstractSymbol filename, TreeNode t) {
        errorStream.print(filename + ":" + t.getLineNumber() + ": ");
        return semantError();
    }

    /**
     * Increments semantic error count and returns the print stream for error
     * messages.
     * 
     * @return a print stream to which the error message is to be printed.
     * 
     * */
    public PrintStream semantError() {
        semantErrors++;
        return errorStream;
    }

    /** Returns true if there are any static semantic errors. */
    public boolean errors() {
        return semantErrors != 0;
    }
}
