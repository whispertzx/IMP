package model;
import java.util.ArrayList;
/**
 * @auther Wing
 * @date 2021-09-26 13:33
 * 没啥用
 */
public class First_order_logic {

    /**
     * 每一个First_order_logic所包含的成员变量
     */
    private int[] D;
    private V_Label_Value V;
    private String s0;
    private ArrayList<Transition> R;
    private String Pv_signal;

    /**
     * 构造函数
     */
    public First_order_logic(){}

    public First_order_logic(int[] d,V_Label_Value v, String s0,ArrayList<Transition> r){
        this.D = d;
        this.V = v;
        this.s0 =s0;
        this.R = r;
    }

    public int [] getD() {
        return D;
    }

    public void setD(int[] d) {
        D = d;
    }

    public V_Label_Value getV() {
        return V;
    }

    public void setV(V_Label_Value v) {
        V = v;
    }

    public String getS0() {
        return s0;
    }

    public void setS0(String s0) {
        this.s0 = s0;
    }

    public ArrayList<Transition> getR() {
        return R;
    }

    public void setR(ArrayList<Transition> r) {
        R = r;
    }

    public int getDi(int i){
        return D[i];
    }

    public String getVi(int i){
        return V.V[i];
    }

    public void addR(Transition s){
        R.add(s);
    }

    public Transition getRi(int i){
        return R.get(i);
    }

    public String getPv_signal() {
        return Pv_signal;
    }

    public void setPv_signal(String pv_signal) {
        Pv_signal = pv_signal;
    }
}
