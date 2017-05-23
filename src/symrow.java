/**
 * Created by rui on 2016/12/24.
 */
public class symrow {
    private int type;           //表示常量、变量或过程1,2,3与之对应
    private int value;          //表示常量或变量的值（只有常量才存放值，其他两种类型直接不用这个字段）
    private int level;          //嵌套层次，最大应为3，不在这里检查其是否小于等于，在SymbolTable中检查（所在的level）

    private String procedure;   //所在的procedure!!!，这个信息很关键
    // 建一个procedure树，维护procedure之间的关系，不单纯是依靠层次信息
    private int address;      //相对于所在嵌套过程基地址的地址

    private int size;         //表示常量，变量，过程所占的大小，此变量和具体硬件有关，实际上在本编译器中为了方便，统一设为4了,设置过程在SymTable中的三个enter函数中
    private String name;        //变量、常量或过程名（本身是一个标识符字段）

    public symrow(){

    }
    public symrow(int TYPE,int VALUE,int LEVEL,String PROC,int ADDRESS,int SIZE,String NAME){
        type=TYPE;value=VALUE;level=LEVEL;procedure=PROC;address=ADDRESS;size=SIZE;name=NAME;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getProcedure() {
        return procedure;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
