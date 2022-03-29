/**
 * @auther Wing
 * @date 2021-09-26 18:55
 */
package model;

public class Node_state {

    String[] V;
    int[] value;

    public Node_state(){}

    public Node_state(String[] v, int[] value) {
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
