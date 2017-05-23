import java.util.ArrayList;
import java.util.List;

/**
 * Created by rui on 2016/12/26.
 */
public class symbolTable {
    private int myconst=1,var=2,procedure=3;

    private List<symrow> symtable=new ArrayList<symrow>();
    private int tablePtr=-1;
    private int length=0;

    private int treePtr=0;
    private int treelength=0;
    /*符号表的标号一定是从零开始的，tableptr指向的是符号表中最后一个元素*/

    public List<symrow> getSymtable() {
        return symtable;
    }
    private proc_node [] proc_tree=new proc_node[100];

    public void setSymtable(List<symrow> symtable) {
        this.symtable = symtable;
    }

    public int getTablePtr() {
        return tablePtr;
    }

    public void setTablePtr(int tablePtr) {
        this.tablePtr = tablePtr;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }



    public symbolTable(){
        initTree();
    }
/*    public void addSymrow(symrow row){
        tablePtr++;
        symtable.add(row);
        length++;

        if(row.getType()==3){
            if(){

            }
        }
        *//*
    }*/
    /*这个长度管理可能会出问题呀，留心*/
    /*这个add之前需要先去查重，留心*/
    public void addConst(String name,int level,int value,int address,String proc){
        symrow temp=new symrow();
        temp.setName(name);
        temp.setLevel(level);
        temp.setValue(value);
        temp.setAddress(address);
        temp.setType(myconst);
        temp.setSize(4);
        temp.setProcedure(proc);
        symtable.add(temp);
        tablePtr++;
        length++;
    }
    public void addVar(String name,int level,int value,int address,String proc){
        symrow temp=new symrow();
        temp.setName(name);
        temp.setLevel(level);
        temp.setValue(value);
        temp.setAddress(address);
        temp.setType(var);
        temp.setSize(0);
        temp.setProcedure(proc);
        symtable.add(temp);
        tablePtr++;
        length++;
    }
    public void addProc(String name,int level,int address,String proc){
        symrow temp=new symrow();
        temp.setName(name);
        temp.setLevel(level);
        temp.setValue(0);
        temp.setAddress(address);
        temp.setType(procedure);
        temp.setSize(0);
        temp.setProcedure(proc);
        symtable.add(temp);
        tablePtr++;
        length++;

    }

    public symrow getRow(int i){
        if(i>0&&i<length){
            return symtable.get(i);
        }
        else{
            return null;
        }
    }

    public proc_node[] getProc_tree() {
        return proc_tree;
    }
    public void setProc_tree(proc_node[] proc_tree) {
        this.proc_tree = proc_tree;
    }
    public void initTree(){
        for(int i=0;i<proc_tree.length;i++){
            proc_tree[i]=new proc_node(-1,"none","none");
        }
    }
    public void addProc_node(String fathernode,String id,int le ){
        proc_tree[treePtr].setFather(fathernode);
        proc_tree[treePtr].setIdentifer(id);
        proc_tree[treePtr].setLevel(le);
        treelength++;
        treePtr++;
    }

    /*接下来写的是填表查重的过程，语义分析的重要过程*/

    /*这三个findexist的函数是去查找，在同一个proc之下，是否存在重名定义*/
    public boolean findExistProc(String name,String proc){
        for(int i=0;i<symtable.size();i++){
            /*procedure在符号表里边，type和*/
            if(name.equals(symtable.get(i).getName())&&(symtable.get(i).getType()==3)
                    &&proc.equals(symtable.get(i).getProcedure())){
                return true;
            }
        }
        return false;
    }
    public boolean findExistConst(String name,String proc){
        for(int i=0;i<symtable.size();i++){
            /*procedure在符号表里边，type和*/
            if(name.equals(symtable.get(i).getName())&&(symtable.get(i).getType()==1)
                    &&proc.equals(symtable.get(i).getProcedure())){
                return true;
            }
        }
        return false;
    }
    public boolean findExistVar(String name,String proc){
        for(int i=0;i<symtable.size();i++){
            /*procedure在符号表里边，type和*/
            if(name.equals(symtable.get(i).getName())&&(symtable.get(i).getType()==2)
                    &&proc.equals(symtable.get(i).getProcedure())){
                return true;
            }
        }
        return false;
    }

    /*这三个find是去查找一个函数是否被定义过*/
    public symrow findDefineProc(String name,String proc){
        /*while(!proc.equals("root")){*/
        for(int i=0;i<symtable.size();i++){
        /*procedure在符号表里边，type和*/
            if(name.equals(symtable.get(i).getName())&&(symtable.get(i).getType()==3)
                    &&proc.equals(symtable.get(i).getProcedure())){
                return symtable.get(i);
            }
        }
        return null;
        /*for(int i=0;i<symtable.size();i++){
            String tempProc;
            if(symtable.get(i).getType()==3&&(symtable.get(i).getName().equals(proc))){

            }
            proc
        }*/
    }
    public symrow findDefineConst(String name,String proc){
        while(!proc.equals("root")){
            for(int i=0;i<symtable.size();i++){
        /*procedure在符号表里边，type和*/
                if(name.equals(symtable.get(i).getName())&&(symtable.get(i).getType()==1)
                        &&proc.equals(symtable.get(i).getProcedure())){
                    return symtable.get(i);
                }
            }
            /*for(int i=0;i<symtable.size();i++){*/
            for(int i=symtable.size()-1;i>=0;i--){
                if(proc.equals(symtable.get(i).getName())&&(symtable.get(i).getType()==3)){
                    proc=symtable.get(i).getProcedure();
                }
            }
        }
        for(int i=symtable.size()-1;i>=0;i--){
            if(name.equals(symtable.get(i).getName())&&(symtable.get(i).getType()==1)
                    &&(symtable.get(i).getProcedure().equals("root"))){
                return symtable.get(i);
            }
        }
        return null;
    }
    public symrow findDefineVar(String name,String proc){
        while(!proc.equals("root")){
            for(int i=0;i<symtable.size();i++){
        /*procedure在符号表里边，type和*/
                if(name.equals(symtable.get(i).getName())&&(symtable.get(i).getType()==2)
                        &&proc.equals(symtable.get(i).getProcedure())){
                    return symtable.get(i);
                }
            }
            /*for(int i=0;i<symtable.size();i++){*/
            for(int i=symtable.size()-1;i>=0;i--){
                if(proc.equals(symtable.get(i).getName())&&(symtable.get(i).getType()==3)){
                    proc=symtable.get(i).getProcedure();
                }
            }
        }
        for(int i=symtable.size()-1;i>=0;i--){
            if(name.equals(symtable.get(i).getName())&&(symtable.get(i).getType()==2)
                    &&(symtable.get(i).getProcedure().equals("root"))){
                return symtable.get(i);
            }
        }
        return null;
    }
}
