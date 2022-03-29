/**
 * @auther Wing
 * @date 2021-09-26 13:42
 */
package model;

public class Transition {

    /**
     * 每一个Transition关系所包含的成员变量
     */
    private String Condition;
    private String PC;
    private String Npc;
    private String Tpc1;
    private String Tpc2;
    private String Npc1;
    private String Npc2;
    private String Expression;
    private String ExpressionT;

    /**
     * 构造函数
     */
    public Transition(){}

    public Transition(String condition,String pc,String npc,String tpc1,String tpc2,String npc1,String npc2,String expression,String expressionT){
        this.Condition = condition;
        this.PC = pc;
        this.Npc = npc;
        this.Tpc1 = tpc1;
        this.Tpc2 = tpc2;
        this.Npc1 = npc1;
        this.Npc2 = npc2;
        this.Expression = expression;
        this.ExpressionT = expressionT;
    }

    public String getCondition() {
        return Condition;
    }

    public void setCondition(String condition) {
        Condition = condition;
    }

    public String getPC() {
        return PC;
    }

    public void setPC(String PC) {
        this.PC = PC;
    }

    public String getNpc() {
        return Npc;
    }

    public void setNpc(String npc) {
        Npc = npc;
    }

    public String getTpc1() {
        return Tpc1;
    }

    public void setTpc1(String tpc1) {
        Tpc1 = tpc1;
    }

    public String getNpc1() {
        return Npc1;
    }

    public void setNpc1(String npc1) {
        Npc1 = npc1;
    }

    public String getTpc2() {
        return Tpc2;
    }

    public void setTpc2(String tpc2) {
        Tpc2 = tpc2;
    }

    public String getNpc2() {
        return Npc2;
    }

    public void setNpc2(String npc2) {
        Npc2 = npc2;
    }

    public String getExpression() {
        return Expression;
    }

    public void setExpression(String expression) {
        Expression = expression;
    }

    public String getExpressionT() {
        return ExpressionT;
    }

    public void setExpressionT(String expressionT) {
        ExpressionT = expressionT;
    }
}
