/**
 * Created by rui on 2016/12/23.
 */
public class error {
    String []errorMsg;

    public int getErrorptr() {
        return errorptr;
    }

    public void setErrorptr(int errorptr) {
        this.errorptr = errorptr;
    }

    int errorptr=0;
    public String[] getErrorMsg() {
        return errorMsg;
    }

    public void addError(String msg){
        errorMsg[errorptr]=msg;
        errorptr++;
    }
    public void adderror(int type,int line,String msg){
        String temperror=("Error type is: "+type+"\t"+" Line is: "+line+"\t"+ msg);
        errorMsg[errorptr]=temperror;
        errorptr++;
    }
    public error(){
        errorMsg=new String[10000];
    }
    public error(error er){
        this.errorMsg=er.getErrorMsg();
        this.errorptr=er.getErrorptr();
    }
}
