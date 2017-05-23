/**
 * Created by rui on 2016/12/23.
 */
public class token {
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }



    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    private String type;
    private String typeid;
    private int line;
    /*line这个字段作为一个重要的性质放在这里其实很让我意外*/
    /*包括层次还有address这些信息,存储的东西比我一开始期待的多不少*/
    private String value;
    public token(){

    }
    public token(/*String type,*/String type,int line, String value){
        setLine(line);
        /*setType(type);*/
        setType(type);
        setValue(value);
    }
}
