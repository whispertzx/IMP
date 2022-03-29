/**
 * @auther Wing
 * @date 2021-09-27 10:26
 */
package model;

public class V_Label_Value {

    public String[] V;
    public int[] value;

    public V_Label_Value(){}

    public V_Label_Value(String[] v, int[] value) {
        this.V = v;
        this.value = value;
    }

    public String[] getV() {
        return V;
    }

    public void setV(String[] v) {
        V = v;
    }

    public int[] getValue() {
        return value;
    }

    public void setValue(int[] value) {
        this.value = value;
    }
}
