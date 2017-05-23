/**
 * Created by rui on 2016/12/24.
 */
public class proc_node {
    /*原本nodename是用来表明，这个node在整棵树里边的位置，
    其实我用level，就是实际的level信息，也能做到同样的事情*/

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getIdentifer() {
        return identifer;
    }

    public void setIdentifer(String identifer) {
        this.identifer = identifer;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    /*private String nodename;*/
    private int level;
    private String father;
    private String identifer;
    public proc_node(){

    }
    public proc_node(int le,String fa,String id){
        level=le;
        father=fa;
        identifer=id;
    }
    public proc_node(proc_node n){
        level=n.getLevel();
        father=n.getFather();
        identifer=n.getIdentifer();
    }
    public boolean checkNone(proc_node p){
        /*if(p.getFather().equals("none")&&p.getIdentifer().equals("none")&&p.getLevel()==-1){
            return true;
        }*/
        if(p.getLevel()==-1){
            return true;
        }
        return false;
    }
}
