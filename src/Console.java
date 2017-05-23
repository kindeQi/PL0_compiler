import java.util.ArrayList;
import java.util.List;

/**
 * Created by rui on 2016/12/23.
 */

/*本来console是一个调用的控制台。。。简单起见，同时承担语法分析的工作*/
public class Console {
    /*private String fileName;*/
    private Lex_Console lex_console;
    /*private symrow row=null;*/
    private List<token> tokencollection = null;
    //private List<symrow> symtable=null;

    private token currentToken = null;
    private symbolTable symboltable = new symbolTable();

    /*private List<proc_node> proc_tree=null;*/
    /*默认procedure的个数不超过100*/
    /*private proc_node [] proc_tree=new proc_node[100];*/
    private int tokenptr = 0;
    private int level = 0;
    /*private int address=0;*/
    private String currentProc = "root";
    private int currentLine = 0;
    private error errorInConsole;

    private AllPcode Pcode = new AllPcode();
    /*初始化，procedure树*/
    /*private void initTree(){
        for(int i=0;i<proc_tree.length;i++){
            proc_tree[i]=new proc_node("none","none","none");
        }
    }*/
    private int pick=0;

    /*这个获取token的函数，将当前的token放在了currentToken里边，同时，移动了一次指针*/
    /*我的tokencollection的遍历，元素为什么要去减一？？玄学代码*/
    public boolean getCurrentToken() {
        try {
            int length = tokencollection.size() - 1;
            if (length > tokenptr) {
                currentToken = tokencollection.get(tokenptr);
                tokenptr++;
                return true;
            }
            return false;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        /*会不会越界。。。*/
    }
    public boolean hasCurrentToken() {
        try {
            int length = tokencollection.size() - 1;
            if (length > tokenptr) {
                currentToken = tokencollection.get(tokenptr);
                return true;
            }
            return false;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        /*会不会越界。。。*/
    }
    /*这三个添加符号表的函数都还没有做语义分析，没有做错误处理*/
    /*为了不让一个错误就终止整个程序，必须要在else里边在上getCurrentToken这个函数
    * 错了，因为有while的缘故，不需要去在else里边在上getCurrentToken这个函数
    * */
    public void myconst() {
        String name;
        int value;
        symrow row;
        if (currentToken.getType().equals("identifer")) {
            name = currentToken.getValue();
            getCurrentToken();
            if (currentToken.getType().equals("=")) {
                getCurrentToken();
                if (currentToken.getType().equals("constnumber")) {
                    value = Integer.parseInt(currentToken.getValue());
                    if(Integer.parseInt(currentToken.getValue())>Integer.MAX_VALUE){
                        errorInConsole.adderror(30,currentToken.getLine(),"it should less than max_value");
                    }
                    /*查重工作先不做*/
                    if (!symboltable.findExistConst(name, currentProc)) {
                        symboltable.addConst(name, level, value, 3, currentProc);
                    } else {
                        /*常量重复*/
                    }
                    //symboltable.addConst(name,level,value,3,currentProc);
                    //address+=addrIncrement;             //登录符号表后地址加1指向下一个
                    /*因为这个是const的标识符，符号表里边不用为他分配地址（实际是数据栈里边不用分配）*/
                    //getCurrentToken();
                }
                else{
                    errorInConsole.adderror(2,currentToken.getLine(),"After =, it should be number");
                }
            } else {
                errorInConsole.adderror(3,currentToken.getLine(),"After identifer, it should be =");
                return;
            }
        } else {
            errorInConsole.adderror(4,currentToken.getLine(),"After const, it should be identifer");
            return;
        }
    }

    public int myvar() {
        String name;
        int value;
        symrow row;
        int add = 3;
        if (currentToken.getType().equals("identifer")) {
            name = currentToken.getValue();
            if (!symboltable.findExistVar(name, currentProc)) {
                symboltable.addVar(name, level, -1, add, currentProc);
            } else {
                /*变量重复*/
            }
            getCurrentToken();
            add++;
            while (currentToken.getType().equals(",")) {
                getCurrentToken();
                if (currentToken.getType().equals("identifer")) {
                    name = currentToken.getValue();
                    //getCurrentToken();
                    //symboltable.addConst(name,level,value,3,currentProc);
                    if (!symboltable.findExistVar(name, currentProc)) {
                        symboltable.addVar(name, level, -1, add, currentProc);
                    } else {
                        /*变量重复*/
                    }
                    add++;
                } else {
                    //getCurrentToken();
                    /*error*/
                    /*这个else一旦读错，就放弃掉整个var行*/
                    return -1;
                }
                getCurrentToken();
            }
            if (currentToken.getType().equals(";")) {
                return add;
            } else {
                errorInConsole.adderror(5,currentToken.getLine(),"missing ;");
                return -1;
            }
        } else {
            errorInConsole.adderror(4,currentToken.getLine(),"After var, it should be identifer");
            return -1;
        }
    }

    public void proc() {
        String name;
        int value;
        symrow row;
        if (currentToken.getType().equals("identifer")) {
            name = currentToken.getValue();
            getCurrentToken();
            /*这个address还没有去定义*/
            /*很重要的一点，就是去重新定义当前的currentProc*/
            if (!symboltable.findExistProc(name, currentProc)) {
                /**/
                symboltable.addProc(name, level,/*currentLine*/Pcode.getCodePtr(), currentProc);
            } else {
                errorInConsole.adderror(6,currentToken.getLine(),"wrong type after procedure");
            }
            level++;
            currentProc = name;
            if (currentToken.getType().equals(";")) {
                return;
            } else {
                errorInConsole.adderror(5,currentToken.getLine(),"missing ;");
                return;
            }
        } else {
            errorInConsole.adderror(4,currentToken.getLine(),"After procedure, it should be identifer");
            return;
        }
    }

    public void grammerAnalysis() {
       /* block();*/
       /*超重要改动*/
       /*while(hasCurrentToken()){
           blockwithoutwhile();
       }*/
       block();
        if (!currentToken.getType().equals(".")) {
            errorInConsole.adderror(9,currentToken.getLine(),"it should be .");
        }
    }
    public void block(){
        while(getCurrentToken()){
            if(currentToken.equals(".")){
                break;
            }
            blockwithoutwhile();

        }
    }
    private void blockwithoutwhile() {
        /*改一下循环条件，免得他自己总是会多读。。。这个坑死了*/
        {
            /*String type=currentToken.getType();*/
            int address = 3;
            int tempCodePtr= Pcode.getCodePtr();
            Pcode.gen(Pcode.getJMP(),0,0);
            /*这个第一句Pcode其实是一个占位子的作用*/
            /*if(currentToken.getType().equals(";")){
                getCurrentToken();
            }*/
            if (currentToken.getType().equals("const")) {
                getCurrentToken();
                myconst();
                getCurrentToken();
                while (currentToken.getType().equals(",")) {
                    getCurrentToken();
                    myconst();
                    getCurrentToken();
                }
                if(currentToken.getType().equals(";")){
                    getCurrentToken();
                }
                else{
                    errorInConsole.adderror(5,currentToken.getLine(),"missing ;");
                }
            }
            /*if(currentToken.getType().equals(";")){
                getCurrentToken();
            }*/
            if (currentToken.getType().equals("var")) {
                getCurrentToken();
                address = myvar();
                getCurrentToken();
                /*这个也是会多加一项地址*/
                if (!(address > 3)) {
                    /*error*/
                }
            }
            /*if(currentToken.getType().equals(";")){
                getCurrentToken();
            }*/
            /*亲测。。。如果之前那俩读到；，这里就读不进来*/
            if (currentToken.getType().equals("procedure")) {
                getCurrentToken();
                String tempProc = currentProc;
                proc();
                getCurrentToken();
                //tokenptr--;
                /*这个代码真是难看。。。。。。*/
                blockwithoutwhile();/*block对应的是分程序*/
                /*这里边肯定得循环调用proc*/
                /*proc结束之后*/
                currentProc = tempProc;
                level--;
            }
            Pcode.getPcodeArray()[tempCodePtr].setA(Pcode.getCodePtr());
            Pcode.gen(Pcode.getINT(),0,address);        //生成分配内存的代码
            /*这里肯定是有了新的currentLine,不然没法做*/
            sentence();
            Pcode.gen(Pcode.getOPR(),0,0);      //生成退出过程的代码，若是主程序，则直接退出程序
        }
    }
    private int isSentence(int i){
        i++;
        return i;
    }
    private void sentence() {

        //getCurrentToken();
        if (currentToken.getType().equals("read")) {
            pick=isSentence(pick);
            getCurrentToken();
            if (currentToken.getType().equals("(")) {
                getCurrentToken();
                if (currentToken.getType().equals("identifer")) {
                    symrow tempRow = symboltable.findDefineVar(currentToken.getValue(), currentProc);
                    if (tempRow == null) {
                        errorInConsole.adderror(11,currentToken.getLine(),"undefined var");
                    } else {           //sto未定义变量的错误
                        /*TableRow tempTable=STable.getRow(STable.getNameRow(rv[terPtr].getValue()));*/
                        Pcode.gen(Pcode.getOPR(), 0, 16);         //OPR 0 16	从命令行读入一个输入置于栈顶   //层差的含义所在？？直接用嵌套的层次数作为参数不可以吗？
                        Pcode.gen(Pcode.getSTO(), level - tempRow.getLevel(), tempRow.getAddress());  //STO L ，a 将数据栈栈顶的内容存入变量（相对地址为a，层次差为L）
                    }//if标识符是否为变量类型
                }
                getCurrentToken();
                while (currentToken.getType().equals(",")) {
                    getCurrentToken();
                    if (currentToken.getType().equals("identifer")) {
                        symrow tempRow = symboltable.findDefineVar(currentToken.getValue(), currentProc);
                        if (tempRow == null) {
                            errorInConsole.adderror(11,currentToken.getLine(),"undefined var");
                        } else {           //sto未定义变量的错误
                        /*TableRow tempTable=STable.getRow(STable.getNameRow(rv[terPtr].getValue()));*/
                            Pcode.gen(Pcode.getOPR(), 0, 16);         //OPR 0 16	从命令行读入一个输入置于栈顶   //层差的含义所在？？直接用嵌套的层次数作为参数不可以吗？
                            Pcode.gen(Pcode.getSTO(), level - tempRow.getLevel(), tempRow.getAddress());  //STO L ，a 将数据栈栈顶的内容存入变量（相对地址为a，层次差为L）
                        }//if标识符是否为变量类型
                        getCurrentToken();
                    } else {

                    }

                }
                if (currentToken.getType().equals(")")) {
                    getCurrentToken();
                }
                else{
                    errorInConsole.adderror(22,currentToken.getLine(),"it should be )");
                }
            }
            else {
                errorInConsole.adderror(40,currentToken.getLine(),"it should be (");
            }
        }
        /*结束掉read这个if*/
        else if(currentToken.getType().equals("write")){
            pick=isSentence(pick);
            getCurrentToken();
            if(currentToken.getType().equals("(")){
                getCurrentToken();
                exp();
                Pcode.gen(Pcode.getOPR(),0,14);         //输出栈顶的值到屏幕
                while(currentToken.getType().equals(",")){
                    getCurrentToken();
                    exp();
                    Pcode.gen(Pcode.getOPR(),0,14);         //输出栈顶的值到屏幕
                }
                Pcode.gen(Pcode.getOPR(),0,15);         //输出换行
                if(currentToken.getType().equals(")")){
                    getCurrentToken();
                }else{
                    errorInConsole.adderror(22,currentToken.getLine(),"it should be )");
                    return;
                }
            }else{
                errorInConsole.adderror(40,currentToken.getLine(),"it should be (");
                return;
            }
        }
        /*以上结束了write*/
        else if(currentToken.getType().equals("if")){
            pick=isSentence(pick);
            int cx1;
            getCurrentToken();
            lexp();
            if(currentToken.getType().equals("then")){
                cx1=Pcode.getCodePtr();             //用cx1记录jpc ，0，0（就是下面这一条语句产生的目标代码）在Pcode中的地址，用来一会回填
                Pcode.gen(Pcode.getJPC(),0,0);  //产生条件转移指令，条件的bool值为0时跳转，跳转的目的地址暂时填为0

                getCurrentToken();
                sentence();
                int cx2=Pcode.getCodePtr();
                Pcode.gen(Pcode.getJMP(),0,0);
                /*if语句要考虑跳转到else执行之后，而then语句同样*/
                Pcode.getPcodeArray()[cx1].setA(Pcode.getCodePtr());        //地址回填，将jpc，0，0中的A回填
                Pcode.getPcodeArray()[cx2].setA(Pcode.getCodePtr());

                if(currentToken.getType().equals("else")){
                    getCurrentToken();
                    sentence();
                    Pcode.getPcodeArray()[cx2].setA(Pcode.getCodePtr());
                }//没了？
            }else{
                errorInConsole.adderror(16,currentToken.getLine(),"it should be then");
                return;
            }
        }
        /*end of if then else*/
        else if(currentToken.getType().equals("while")){
            pick=isSentence(pick);
            int cx1=Pcode.getCodePtr();     //保存条件表达式在Pcode中的地址
            getCurrentToken();
            lexp();
            if(currentToken.getType().equals("do")){
                int cx2=Pcode.getCodePtr();     //保存条件跳转指令的地址，在回填时使用，仍是条件不符合是跳转
                Pcode.gen(Pcode.getJPC(),0,0);
                getCurrentToken();
                sentence();
                Pcode.gen(Pcode.getJMP(),0,cx1);    //完成DO后的相关语句后，需要跳转至条件表达式处，检查是否符合条件，即是否继续循环
                Pcode.getPcodeArray()[cx2].setA(Pcode.getCodePtr());        //回填条件转移指令
            }else{
                errorInConsole.adderror(18,currentToken.getLine(),"it should be do");
                return;
            }
        }
        /*end of while*/
        else if(currentToken.getType().equals("call")){
            pick=isSentence(pick);
            getCurrentToken();
            if(currentToken.getType().equals("identifer")){
                symrow tempRow=symboltable.findDefineProc(currentToken.getValue(),currentProc);
                if(tempRow==null){
                    errorInConsole.adderror(11,currentToken.getLine(),"undefined procedure");
                    errorInConsole.adderror(15,currentToken.getLine(),"cannot use this proc");
                }
                else{           //cal 未定义变量的错误
                    Pcode.gen(Pcode.getCAL(),level-tempRow.getLevel(),tempRow.getAddress());
                    getCurrentToken();
                }
            }else{
                errorInConsole.adderror(14,currentToken.getLine(),"it should be identifer");
                return;
            }
        }
        /*end of call*/
        else if(currentToken.getType().equals("begin")){
            pick=isSentence(pick);
            getCurrentToken();
            sentence();
            while(currentToken.getType().equals(";")){
                getCurrentToken();
                pick++;
                sentence();
            }
            if(currentToken.getType().equals("end")){
                getCurrentToken();
            }else{
                errorInConsole.adderror(17,currentToken.getLine(),"it should be end");
                return;
            }
        }
        /*end of begin and end*/
        else if(currentToken.getType().equals("identifer")){      //赋值语句
            pick=isSentence(pick);
            String name=currentToken.getValue();
            symrow tempRow = symboltable.findDefineVar(currentToken.getValue(), currentProc);
            String errorType=currentToken.getType();
            getCurrentToken();
            if(currentToken.getType().equals(":=")){
                getCurrentToken();
                exp();
                if(tempRow==null){
                    errorInConsole.adderror(11,currentToken.getLine(),"undefined var");
                }
                if(errorType.equals("const")||errorType.equals("procedure")){
                    errorInConsole.adderror(12,currentToken.getLine(),"cannot give number to var/procedure");
                }
                else{
                    Pcode.gen(Pcode.getSTO(),level-tempRow.getLevel(),tempRow.getAddress());
                }
            }else{
                errorInConsole.adderror(13,currentToken.getLine(),"it should be :=");
                return;
            }
        }
        else if(currentToken.getType().equals("repeat")){
            pick=isSentence(pick);
            int reAddress=Pcode.getCodePtr();
            getCurrentToken();
            sentence();
            if(currentToken.getType().equals(";")){
                errorInConsole.adderror(8,currentToken.getLine(),"After sentence, biaodianfuhao is wrong");
            }
            while(currentToken.getType().equals(";")){
                getCurrentToken();
                sentence();
            }
            if(currentToken.getType().equals("until")){
                getCurrentToken();
                lexp();
                Pcode.gen(Pcode.getJPC(),0,reAddress);
            }
            else{
                /*error没有until*/
            }
        }
        if(pick==0){
            errorInConsole.adderror(7,currentToken.getLine(),"it should be sentence");
        }
    }
    public void exp(){
        String tempType=currentToken.getType();
        if(currentToken.getType().equals("+")){
            getCurrentToken();
        }else if(currentToken.getType().equals("-")){
            getCurrentToken();
        }
        term();/*term表示项*/
        if(tempType.equals("-")){
            Pcode.gen(Pcode.getOPR(),0,1);      //  OPR 0 1	栈顶元素取反
        }
        while(currentToken.getType().equals("+")||currentToken.getType().equals("-")){
            String tempId=currentToken.getType();
            getCurrentToken();
            term();
            if(tempId.equals("+")){
                Pcode.gen(Pcode.getOPR(),0,2);       //OPR 0 2	次栈顶与栈顶相加，退两个栈元素，结果值进栈
            }else if(tempId.equals("-")){
                Pcode.gen(Pcode.getOPR(),0,3);      //OPR 0 3	次栈顶减去栈顶，退两个栈元素，结果值进栈
            }
        }
    }
    public void term(){
        factor();
        while(currentToken.getType().equals("*")||currentToken.getType().equals("/")){
            String tempId=currentToken.getType();
            getCurrentToken();
            factor();
            if(tempId.equals("*")){
                Pcode.gen(Pcode.getOPR(),0,4);       //OPR 0 4	次栈顶乘以栈顶，退两个栈元素，结果值进栈
            }else if(tempId.equals("/")){
                Pcode.gen(Pcode.getOPR(),0,5);      // OPR 0 5	次栈顶除以栈顶，退两个栈元素，结果值进栈
            }
        }
    }
    public void factor(){
        if(currentToken.getType().equals("constnumber")){
            Pcode.gen(Pcode.getLIT(),0,Integer.parseInt(currentToken.getValue()));    //是个数字,  LIT 0 a 取常量a放入数据栈栈顶
            if(Integer.parseInt(currentToken.getValue())>Integer.MAX_VALUE){
                errorInConsole.adderror(30,currentToken.getLine(),"it should less than max_value");
            }
            getCurrentToken();

        }else if(currentToken.getType().equals("(")){
            getCurrentToken();
            exp();
            if(currentToken.getType().equals(")")){
                getCurrentToken();
            }else{
                errorInConsole.adderror(40,currentToken.getLine(),"it should be (");
            }
        }else if(currentToken.getType().equals("identifer")){
            String name=currentToken.getValue();
            /*这里到了关键的测试define函数正确与否的地方了*/
            /*proc+name+type去定位，取出符号表的元素*/
            symrow tempRow = symboltable.findDefineVar(currentToken.getValue(), currentProc);
            if(tempRow==null){
                tempRow = symboltable.findDefineConst(currentToken.getValue(), currentProc);
            }
            if(tempRow==null){
                errorInConsole.adderror(11,currentToken.getLine(),"undefined identifer");
            }
            /*这里的断点很关键*/
            {           //未定义变量的错误
                if(tempRow.getType()==2){ //标识符是变量类型
                    Pcode.gen(Pcode.getLOD(),level-tempRow.getLevel(),tempRow.getAddress());    //变量，LOD L  取变量（相对地址为a，层差为L）放到数据栈的栈顶
                }else if (tempRow.getType()==1){
                    Pcode.gen(Pcode.getLIT(),0,tempRow.getValue());         //常量，LIT 0 a 取常量a放入数据栈栈顶
                }
                else{       //类型不一致的错误
                    /*errorHapphen=true;
                    showError(12,"");*/
                    return;
                }
            }
            getCurrentToken();
        }else {
            /*errorHapphen=true;
            showError(1,"");*/
        }
    }
    /*lexp是条件*/
    public void lexp(){
        if(currentToken.getType().equals("odd")){
            getCurrentToken();
            exp();
            Pcode.gen(Pcode.getOPR(),0,6);  //OPR 0 6	栈顶元素的奇偶判断，结果值在栈顶
        }else{
            exp();
            String loperator=lop();        //返回值用来产生目标代码，如下
            /*lop是针对关系运算符*/
            if(loperator==null){
                /*error*/
            }
            exp();
            if(loperator.equals("=")){
                Pcode.gen(Pcode.getOPR(),0,8);      //OPR 0 8	次栈顶与栈顶是否相等，退两个栈元素，结果值进栈
            }else if(loperator.equals("<>")){
                Pcode.gen(Pcode.getOPR(),0,9);      //OPR 0 9	次栈顶与栈顶是否不等，退两个栈元素，结果值进栈
            }else if(loperator.equals("<")){
                Pcode.gen(Pcode.getOPR(),0,10);     //OPR 0 10	次栈顶是否小于栈顶，退两个栈元素，结果值进栈
            }else if(loperator.equals("<=")){
                Pcode.gen(Pcode.getOPR(),0,13);     // OPR 0 13	次栈顶是否小于等于栈顶，退两个栈元素，结果值进栈
            }else if(loperator.equals(">")){
                Pcode.gen(Pcode.getOPR(),0,12);     //OPR 0 12	次栈顶是否大于栈顶，退两个栈元素，结果值进栈
            }else if(loperator.equals(">=")){
                Pcode.gen(Pcode.getOPR(),0,11);     //OPR 0 11	次栈顶是否大于等于栈顶，退两个栈元素，结果值进栈
            }
        }
    }

    public String lop(){
        String loperator;
        if(currentToken.getType().equals("=")){
            getCurrentToken();
            return "=";
        }else if(currentToken.getType().equals("<>")){
            getCurrentToken();
            return "<>";
        }else if(currentToken.getType().equals("<")){
            getCurrentToken();
            return "<";
        }else if(currentToken.getType().equals("<=")){
            getCurrentToken();
            return "<=";
        }else if(currentToken.getType().equals(">")){
            getCurrentToken();
            return ">";
        }else if(currentToken.getType().equals(">=")){
            getCurrentToken();
            return ">=";
        }
        else{
            errorInConsole.adderror(20,currentToken.getLine(),"it should be like =,<>,<,>,<=,>=");
        }
        return null;
    }

    public void showPcode(){
        String []showpcode=new String[10000];
        PerPcode temp;
        String []temparray=Pcode.getFString();
        for(int i=0;i<=9999;i++){
            temp=Pcode.getPcodeArray()[i];
            if(temp.getF()==-1){
                break;
            }
            showpcode[i]=Pcode.getFString()[temp.getF()]+" "+String.valueOf(temp.getL())
                    +" "+String.valueOf(temp.getA());
        }
        System.out.println("这是Pcode(this is Pcode)：");
        for(int i=0;i<showpcode.length;i++){
            if(showpcode[i]!=null){
                System.out.println("Line: "+i+" "+showpcode[i]);
            }
            else{
                break;
            }
        }
    }
    public void showSymboltable(){
        System.out.println("这是符号表(this is symbol table):");
        for(int i=0;i<symboltable.getSymtable().size();i++){
            /*procedure在符号表里边，type和*/
            /*String temp[]=new String[3];
            temp[1]="const";temp[2]="var";temp[3]="procedure";*/
            symrow temprow=symboltable.getSymtable().get(i);
            System.out.print("name: "+"\t"+temprow.getName()+"\t"+"type: "+getType(temprow.getType())+
                    "\t"+"value: "+temprow.getValue()+"\t"+"level: "+temprow.getLevel());
            System.out.println("\t"+"address: "+temprow.getAddress()+"\t"+"procedure: "+temprow.getProcedure());
        }
    }
    public String getType(int i){
        switch (i){
            case 1:
                return "const    ";
            case 2:
                return "var      ";
            case 3:
                return "procedure";
            default:
                return "unknow type, compiler itself error";
        }
    }
    public void showErrormsg(){
        System.out.println("可能发生了错误，错误信息(Error may happens)：");
        for(int i=0;i<errorInConsole.getErrorMsg().length;i++){
            String temp=errorInConsole.getErrorMsg()[i];
            if(temp!=null){
                if(i>=1&&(!temp.equals(errorInConsole.getErrorMsg()[i-1]))){
                    System.out.println(errorInConsole.getErrorMsg()[i]);
                }
                if(i==0){
                    System.out.println(errorInConsole.getErrorMsg()[i]);
                }
            }
            else{
                break;
            }
        }
    }
    public Console(String fn) {
        /*setFileName(fn);*/
        lex_console = new Lex_Console(fn);
        tokencollection = lex_console.getTokencollection();
        /*errorInConsole = new error(lex_console.getErrorinlex());*/
        errorInConsole=new error();
        /*最好再给我的error添加行号信息*/
        /*以上是词法分析的结果*/

        /*下面是语法分析相关的代码*/

        /*tree是用来管理procedure之间的关系的一个数据结构，
        第一遍实现的时候先不去管procedure之间的相互电泳关系，所以进到procedure你变的时候，
        针对示例代码里边，对于procedure查看变量的活动范围的不足，做的一个改进*/
        grammerAnalysis();
        showPcode();
        showSymboltable();
        showErrormsg();
        tokenptr++;
    }

}
