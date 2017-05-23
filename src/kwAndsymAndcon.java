import java.util.HashMap;
import java.util.Map;

/**
 * Created by rui on 2016/12/23.
 */
public class kwAndsymAndcon {
    public Map<String, String> getDictionary() {
        return dictionary;
    }
    private Map<String, String> dictionary = new HashMap<String, String>();
    public kwAndsymAndcon(){
        dictionary.put("const","1");
        dictionary.put("var","2");
        dictionary.put("procedure","3");
        dictionary.put("odd","4");
        /*odd是判断一个数是否为奇数*/
        dictionary.put("if","5");
        dictionary.put("then","6");
        dictionary.put("else","6.5");
        dictionary.put("while","7");
        dictionary.put("do","8");
        dictionary.put("call","9");
        dictionary.put("begin","10");
        dictionary.put("end","11");
        dictionary.put("repeat","11.4");
        dictionary.put("until","11.6");

        dictionary.put("read","12");
        dictionary.put("write","13");

        dictionary.put("=","14");
        dictionary.put("<","15");
        dictionary.put("<=","15");
        dictionary.put(">=","16");
        dictionary.put(">","17");
        dictionary.put("<>","18");

        dictionary.put("+","19");
        dictionary.put("-","20");
        dictionary.put("*","21");
        dictionary.put("/","22");

        dictionary.put(":=","23");
        dictionary.put(",","24");
        dictionary.put(";","25");
        dictionary.put(".","26");
        dictionary.put("(","27");
        dictionary.put(")","28");

        dictionary.put("identifer","29");
        dictionary.put("constnumber","30");

        dictionary.put("others","-1");

    }
}
