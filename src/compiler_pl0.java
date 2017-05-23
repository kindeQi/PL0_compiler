import java.util.Scanner;

/**
 * Created by rui on 2016/12/23.
 */
public class compiler_pl0 {
    public static void main(String [] args){
        String fileName;
        System.out.println("选择一个文件输入1-5（其他数字默认显示文件1）");
        System.out.println("查看存在错误的文件，输入6");
        System.out.print(">>");
        Scanner s=new Scanner(System.in);
        fileName=s.next();
        /*System.out.println("your fileName is: "+fileName);*/
        /*其实还可以手动选择*/
        /*int temp=Integer.parseInt(fileName);*/
        switch (fileName){
            case "1":
                fileName="pl0code1.txt";
                break;
            case "6":
                fileName="pl0code1_wrong.txt";
                break;
            case "2":
                fileName="testPL1.txt";
                break;
            case "7":
                fileName="testPL1_wrong.txt";
                break;
            case "3":
                fileName="testPL2.txt";
                break;
            case "8":
                fileName="testPL2_wrong.txt";
                break;
            case "4":
                fileName="testPL5.txt";
                break;
            case "9":
                fileName="testPL5_wrong.txt";
                break;
            case "5":
                fileName="testPL6.txt";
                break;
            case "10":
                fileName="testPL6_wrong.txt";
                break;
            default:
                fileName="pl0code1.txt";
        }
        /*fileName="testPL6.txt";*/
        /*Lex_Console lex_console=new Lex_Console(fileName);*/
        Console console=new Console(fileName);

    }
}
