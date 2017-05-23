import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by rui on 2016/12/23.
 */


public class Lex_Console {
    final int maxElement = 10000;

    public error getErrorinlex() {
        return errorinlex;
    }

    private error errorinlex = new error();
    private String fileName;
    private char[] buffer;
    private int bufferptr = 0;
    private int tokenptr = 0;
    private char currentChar;
    private String currentToken;
    //private kwAndsymAndcon keywords=new kwAndsymAndcon();
    Map<String, String> dictionary = (new kwAndsymAndcon().getDictionary());

    private int line = 1;

    public List<token> getTokencollection() {
        return tokencollection;
    }

    private List<token> tokencollection = new ArrayList<token>();

    private void filereader() {
        File file = new File(fileName);
        try {
            /*这里用了根目录debug还是蛮机智的*/
            /*System.out.println(this.getClass().getResource(""));*/
            BufferedReader bfreader = new BufferedReader(new FileReader(file));
            /*System.out.println(fileName);*/
            String temp1 = "", temp2 = "";
            while ((temp1 = bfreader.readLine()) != null) {
                temp2 = temp2 + temp1 + String.valueOf('\n');
            }
            buffer = temp2.toCharArray();
            /*int temp=buffer.length;*/
            bfreader.close();
            System.out.println("这是源程序(This is source code):");
            System.out.println(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*这一段词法分析的代码比较机械。。完全的取单词的作用*/
    public void getChar() {
        try {
            if (bufferptr < buffer.length) {
                currentChar = buffer[bufferptr];
                bufferptr++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//	System.out.print(ch);
    }

    public void getBC() {
        while ((currentChar == ' ' || currentChar == '	' || currentChar == '\n') && (bufferptr < buffer.length)) {
            if (currentChar == '\n') {
                line++;
            }
            getChar();
        }
    }

    public void concat() {
        currentToken = currentToken + String.valueOf(currentChar);
    }

    public void retract() {
        bufferptr--;
        currentChar = ' ';
    }

    public boolean isLetter() {
        if (Character.isLetter(currentChar)) {
            return true;
        }
        return false;
    }

    public boolean isDigit() {
        if (Character.isDigit(currentChar)) {
            return true;
        }
        return false;
    }

    /*判断currenttoken的类型，确定是保留字还是标识符*/
    /*返回typeid*/
    private String checkToken() {
        for (Map.Entry<String, String> entry : dictionary.entrySet()) {
            if (entry.getKey().equals(currentToken)) {
                return entry.getKey();
            }
        }
        return "identifer";
        /*报错1*/
    }


    /*存在文件读到结尾的判断，符号为\n而且长度已经达到极限*/
    /*
    每次执行这个行数
    1.必须得到一个token，且只能得到一个token
    2.并且这个函数直接将token加入到tokencollection中
    3.这个函数需要维护指针变量
    */
    private void bufferToToken() {
        getChar();
        getBC();
        currentToken = "";
        token temp = null;
        /*标识一下最后的.这个符号*/
        if (currentChar == '.') {
            //tokencollection.add(new token("\n","-1",line,"-"));
            currentToken = ".";
            temp = new token(".", line, currentToken);/*不知道这个dictionary有没有使用正确*/
            if (bufferptr != (buffer.length + 1)) {
                errorinlex.addError(". factor is not the end of this file\n");
            }
        } else if (isLetter()) {
            while (isDigit() || isLetter()) {
                concat();
                getChar();
            }
            retract();
            String returnKEy = checkToken();
            
            /*if(!returnKEy.equals("This token is not in")){
                temp=new token(returnKEy,line,currentToken);
            }
            else{
                temp=new token("identifer",line,currentToken);
                    *//*errorinlex.addError("This token is wrong \n");*//*
            }*/
            temp = new token(returnKEy, line, currentToken);
        } else if (isDigit()) {
            while (isDigit()) {
                concat();
                getChar();
            }
            retract();
            temp = new token("constnumber", line, currentToken);
        } else if (currentChar == '=') {
            temp = new token("=", line, currentToken);
        } else if (currentChar == '+') {
            temp = new token("+", line, currentToken);
        } else if (currentChar == '-') {
            temp = new token("-", line, currentToken);
        } else if (currentChar == '*') {
            temp = new token("*", line, currentToken);
        } else if (currentChar == '/') {
            temp = new token("/", line, currentToken);
        } else if (currentChar == '<') {
            getChar();
            if (currentChar == '=') {
                temp = new token("<=", line, currentToken);
            } else if (currentChar == '>') {
                temp = new token("<>", line, currentToken);
            } else {
                retract();
                temp = new token("<", line, currentToken);
            }
        } else if (currentChar == '>') {
            getChar();
            if (currentChar == '=') {
                temp = new token(">=", line, currentToken);
            } else {
                retract();
                temp = new token(">", line, currentToken);
            }
        } else if (currentChar == ',') {
            temp = new token(",", line, currentToken);
        } else if (currentChar == ';') {
            temp = new token(";", line, currentToken);
        }/*else if(currentChar=='.'){
            temp=new token(".",line,currentToken);
        }*/ else if (currentChar == '(') {
            temp = new token("(", line, currentToken);
        } else if (currentChar == ')') {
            temp = new token(")", line, currentToken);
        } else if (currentChar == ':') {
            getChar();
            if (currentChar == '=') {
                temp = new token(":=", line, currentToken);
            } else {
                errorinlex.addError("This is not :=\n");
            }
        }

        tokencollection.add(temp);
        tokenptr++;
        return;
    }

    public Lex_Console(String fn) {
        fileName = fn;
        filereader();
        while (bufferptr < buffer.length) {
            bufferToToken();
        }
    }
}
        /*for(int i=0;i<tokencollection.size()-1;i++){
            token t=tokencollection.get(i);
            System.out.println(t.getLine()+" "+t.getValue()+" "+t.getType());
        }
        System.out.println(tokencollection.size());
    }*/

