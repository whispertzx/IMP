import javax.script.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import model.*;

/**
 * @auther Wing
 * @date 2021-09-26 13:48
 * 随意测试
 */
public class FOLtoKS {

    private static String sAnd = "∧";
    private static String sOr = "⋁";
    private static String sEqual = "=";
    private static String sInitialize = "⊥";
    private static String lInitialize = "U";
    private static String sTpc = "PC";
    private static String sNpc = "PC'";
    private static String sTPC1 = "PC1";
    private static String sNpc1 = "PC1'";
    private static String sTpc2 = "PC2";
    private static String sNpc2 = "PC2'";
    private static String s1Enter = "P11";
    private static String s2Enter = "P21";
    private static String spcInitialize = "M";
    private static String spcFinal = "M'";
    private static String s_No_Change = "SAME(ALL)";
    private static String sReverse = "!";
    int sWaitStatusCount = 2;

    public static void main(String[] args) throws Exception {


        /**
         * 读入转化后的一阶谓词逻辑文本，用正则表达式split的⋁进行分割，并测试输出
         */


        String test2 = "pc=#_Start & pc'=#_UNDEFINED & pc_1=#_UNDEFINED & pc_1'=#_1 & pc_2=#_UNDEFINED & pc_2'=#_9 ⋁\n" +
                "pc_1=#_1 & pc'_1=#_2 & same(pc_2,pc) & c=0 & same(V\\V_1{c}) ⋁\n" +
                "pc_1=#_2 & pc'_1=#_3 & same(pc_2,pc) & a=0 & same(V\\V_1{a}) ⋁\n" +
                "pc_1=#_3 & pc'_1=#_4 & same(pc_2,pc) & c<=2 & same(V)\n" +
                "pc_1=#_3 & pc'_1=#_End_1 & same(pc_2,pc) & !(c<=2) & same(V) ⋁\n" +
                "pc_1=#_4 & pc'_1=#_5 & same(pc_2,pc) & a=a+1 & same(V\\V_1{a}) ⋁\n" +
                "pc_1=#_5 & pc'_1=#_6 & same(pc_2,pc) & a==2 & same(V) ⋁\n" +
                "pc_1=#_5 & pc'_1=#_7 & same(pc_2,pc) & !(a==2) & same(V) ⋁\n" +
                "pc_1=#_6 & pc'_1=#_8 & same(pc_2,pc) & a=0 & same(V\\V_1{a}) ⋁\n" +
                "pc_1=#_7 & pc'_1=#_8 & same(pc_2,pc) & a=1 & same(V\\V_1{a}) ⋁\n" +
                "pc_1=#_8 & pc'_1=null & same(pc_2,pc) & c=c+1 & same(V\\V_1{c}) ⋁\n" +
                "pc_2=#_9 & pc'_2=#_End_1 & same(pc_1,pc) & a==0 & same(V_2) ⋁\n" +
                "pc_2=#_9 & pc'_2=#_9 & same(pc_1,pc) & !(a==0) & same(V_2) ⋁\n" +
                "pc=#_UNDEFINED & pc'=#_End & pc_1=#_End_2 & pc_1'=#_UNDEFINED & pc_2=#_End_2 & pc_2'=#_UNDEFINED";

        String all = "pc=#_Start & pc'=#_1 & c=0 & same(V\\{c})⋁\n" +
                "pc=#_1 & pc'=#_2 & a=0 & same(V\\{a})⋁\n" +
                "pc=#_2 & pc'=#_3 & c<=2 & same(V)⋁\n" +
                "pc=#_2 & pc'=#_End & !(c<=2) & same(V)⋁\n" +
                "pc=#_3 & pc'=#_4 & a=a+1 & same(V\\{a})⋁\n" +
                "pc=#_4 & pc'=#_5 & a==2 & same(V)⋁\n" +
                "pc=#_4 & pc'=#_6 & !(a==2) & same(V)⋁\n" +
                "pc=#_5 & pc'=#_End & a=0 & same(V\\{a})⋁\n" +
                "pc=#_6 & pc'=#_7 & a=1 & same(V\\{a})⋁\n" +
                "pc=#_7 & pc'=#_End & c=c+1 & same(V\\{c})";

        String[] sArray=test2.split(sOr);
        for (int i = 0 ; i <sArray.length ; i++ )
        {
            System.out.println(sArray[i]);
        }
        ArrayList<Transition> AL = new ArrayList<Transition>();
        Transition Ft = new Transition("null","#_Start","#_UNDEFINED","#_UNDEFINED","#_UNDEFINED","#_1","#_9","null", "null");
        AL.add(Ft);
        for(int i = 1; i < sArray.length; i++){
            Transition si = TransitionNsp(sArray[i]);
            AL.add(si);
        }

        /**
         * 形成一阶谓词逻辑的D、V、S0、R
         */
        int[] D = new int[]{0,1,2};
        V_Label_Value V = new V_Label_Value(new String[]{"a", "c"},new int[]{0,0});
        V_Label_Value VT = new V_Label_Value(new String[]{"a", "c"},new int[]{0,0});
        String s0 = "a=0 & c=2";

        /**
         *  this is a test
        Transition s1 = TransitionSp(sArray[0]);
        Transition s2 = TransitionSp(sArray[1]);
        ArrayList<Transition> T = new ArrayList<>();
        T.add(s1);
        T.add(s2);
         **/
        First_order_logic fol = new First_order_logic(D,V,s0,AL);

        /**
         * 测试输出所有的一阶逻辑谓词公式
         */
        for(int i = 0; i < fol.getD().length; i++){
            System.out.println(fol.getDi(i));
        }

        for(int i = 0; i < fol.getV().V.length; i++){
            System.out.println(fol.getVi(i));
        }

        System.out.println(fol.getS0());

        for(int i = 0; i < fol.getR().size(); i++){
            System.out.println(fol.getRi(i).getCondition()+"---"+fol.getRi(i).getPC()+"---"+fol.getRi(i).getNpc()+"---"+fol.getRi(i).getTpc1()+"\n"+
                    "---"+fol.getRi(i).getTpc2()+"---"+fol.getRi(i).getNpc1()+"---"+fol.getRi(i).getNpc2()+"---"+"\n"+
                    fol.getRi(i).getExpression()+"---"+fol.getRi(i).getExpressionT());
        }


//        List<String> A = new ArrayList<String>(){{
//            add("\"#_Start a=0 c=0 \"");
//        }};
//        List<String> B = new ArrayList<String>();
//        StateAndPath Sp = new StateAndPath(A,B);
//        System.out.println("第一个的pc为"+T.get(0).getTpc1());
//        Sp = NextRelationN(T.get(0).getTpc1(),Sp,"c=0",T,V,VT);
//        System.out.println(Sp.getPath()+"-------"+Sp.getState());
//        NextRelation(T.get(0).getNpc1(),node_state,"null",T,V);
    }

    public static String BuildPath(String Tpc,V_Label_Value V){
        String GraphPath =  "\"" + Tpc + " ";
        for(int zz = 0 ; zz<V.V.length;zz++){
            GraphPath = GraphPath+V.V[zz]+"="+V.value[zz]+" ";
        }
        GraphPath = GraphPath + "\"";
        return GraphPath;
//        if(Tpc.equals("#_End")) {
//            GraphPath = GraphPath + "\" \n";
//            return GraphPath;
//        }
//        else {
//            GraphPath = GraphPath + "\" ->\n";
//            return GraphPath;
//        }
    }

    public static StateAndPath BuildGraph(StateAndPath Gp ,String Now,String Next){
        if(Gp.getState().size() == 1){
            if(!Now.equals(Next)){
                Gp.getState().add(Next);
                Gp.getPath().add(Now+"->"+Next);
            }
            else {
                Gp.getPath().add(Now+"->"+Next);
            }
        }
        else {
            int flag = 0;
            for(int i = 0; i < Gp.getState().size();i++){
                if(Gp.getState().get(i).equals(Next)){
                    Gp.getPath().add(Now+"->"+Next);
                    flag = 1;
                    break;
                }
            }
            if(flag == 0){
                Gp.getState().add(Next);
                Gp.getPath().add(Now+"->"+Next);
            }
        }
        return Gp;
    }

    /**
     *
     * @param pc
     * @param Sp
     * @param condition
     * @param T
     * @param V
     * @return
     * @throws Exception
     */
    public static StateAndPath NextRelationN(String pc,StateAndPath Sp,String condition,ArrayList<Transition> T,V_Label_Value V,V_Label_Value Pre) throws Exception {
        if(pc.equals("#_End")){
            return Sp;
        }
        for(int i = 0; i < T.size(); i++) {
            if (T.get(i).getTpc1().equals("#_End")){
                //do nothing
            }
            else if(T.get(i).getTpc1().equals(pc)){
                String TestCondition = T.get(i).getCondition();
                System.out.println(TestCondition);
                /*
                if(TestCondition == "true"){
                    GraphPath = appendGraph(pc,V,GraphPath);
                    Sp = BuildGraph(Sp,BuildPath(T.get(i).getTpc1(),V),BuildPath(T.get(i).getNpc1(),V));
                    Sp = NextRelationN(T.get(i).getNpc1(),Sp,condition,T,V,Pre,GraphPath);
                }
                else if(TestCondition == "false"){
                    //????
                }
                 */
                if(TestCondition.contains("=") && (!TestCondition.contains("<")) && (!TestCondition.contains("==")) && (!TestCondition.contains(">")) ){
                    //赋值语句
                    System.out.println("==========================进入赋值判断==========================");
                    String[] Tc = TestCondition.split("=");
                    /*
                    引入 XYZ 作为临时变量，这边的V_Label_Value新建会产生数据同步？？？尚未解决
                     */
                    List XYZ = new ArrayList();
                    for(int j = 0;j<V.value.length;j++){
                        XYZ.add(V.value[j]);
                    }
                    for(int j = 0;j<V.V.length;j++){
                        if(V.V[j].equals(Tc[0])){
                            String []AA = V.V;
                            int []BB = V.value;
                            System.out.println("判断的语句为"+TestCondition);
                            // 获取JS执行引擎,并获取变量
                            ScriptEngine se = new ScriptEngineManager().getEngineByName("javascript");
                            Bindings bindings = se.createBindings();
                            se.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
                            se.eval(new FileReader("/Users/wing/Desktop/assign.js"));
                            if (se instanceof Invocable) {
                                Invocable in = (Invocable) se;
                                Double result = Double.parseDouble((String) in.invokeFunction("assign",AA,BB,TestCondition));
                                V.value[j] = result.intValue();
                                break;
                            }
                        }
                    }
//                    System.out.println("当前的pc值为"+T.get(i).getTpc1());
//                    System.out.println("下一个状态的节点为："+T.get(i).getNpc1());
//                    GraphPath = appendGraph(T.get(i).getTpc1(),V,GraphPath);
//                    System.out.println("Pre的V和value值分别为"+ Arrays.toString(Pre.getV()) +"-----"+ Arrays.toString(Pre.getValue()));
//                    System.out.println("下一个节点的V和value值分别为"+ Arrays.toString(V.getV()) +"-----"+ Arrays.toString(V.getValue()));
                    for(int j = 0;j<V.value.length;j++){
                        Pre.value[j] = (int) XYZ.get(j);
                    }
                    Sp = BuildGraph(Sp,BuildPath(T.get(i).getTpc1(),Pre),BuildPath(T.get(i).getNpc1(),V));
                    for(int j = 0;j<V.value.length;j++){
                        Pre.value[j] = V.value[j];
                    }
                    Sp = NextRelationN(T.get(i).getNpc1(),Sp,condition,T,V,Pre);
                }
                else if(TestCondition == null){
                    //do nothing?
                }
                else{
                    System.out.println("==========================进入true or false判断==========================");
                    String[] s = new String[V.V.length+1];
                    String []AA = V.V;
                    int []BB = V.value;
                    System.out.println("判断的语句为"+TestCondition);
                    // 获取JS执行引擎,并获取变量
                    ScriptEngine se = new ScriptEngineManager().getEngineByName("javascript");
                    Bindings bindings = se.createBindings();
                    se.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
                    se.eval(new FileReader("/Users/wing/Desktop/test.js"));
                    if (se instanceof Invocable) {
                        Invocable in = (Invocable) se;
                        Boolean result = (Boolean) in.invokeFunction("judge",AA,BB,TestCondition);
                        if(result){
                            Sp = BuildGraph(Sp,BuildPath(T.get(i).getTpc1(),V),BuildPath(T.get(i).getNpc1(),V));
                            Sp = NextRelationN(T.get(i).getNpc1(),Sp,condition,T,V,Pre);
                        }
                        else {
                            System.out.println("False");
                        }

                    }
                }
//                else if(TestCondition.contains("=") && !TestCondition.contains("<") && !TestCondition.contains("==") && !TestCondition.contains(">") && !TestCondition.contains("!") && !TestCondition.contains("&&") && !TestCondition.contains("||")){
//
//                }
            }
        }
        return Sp;
    }

    public static Transition TransitionNsp(String s){
        String[] sArray = s.split("&");
        Transition tr = new Transition();
        for(int i=0; i<sArray.length; i++){
            if(sArray[i].contains("pc=")){
                String ns = sArray[i].trim().replaceAll("pc=","");
                tr.setPC(ns);
            }
            else if(sArray[i].contains("pc_1=")){
                String ns = sArray[i].trim().replaceAll("pc_1=","");
                tr.setTpc1(ns);
            }
            else if(sArray[i].contains("pc_2=")){
                String ns = sArray[i].trim().replaceAll("pc_2=","");
                tr.setTpc2(ns);
            }
            else if(sArray[i].contains("pc'_1=")){
                String ns = sArray[i].trim().replaceAll("pc'_1=","");
                tr.setNpc1(ns);
            }
            else if(sArray[i].contains("pc'_2=")){
                String ns = sArray[i].trim().replaceAll("pc'_2=","");
                tr.setNpc2(ns);
            }
            else if((sArray[i].contains("=")&&(!sArray[i].contains("pc")))||sArray[i].contains("true")||sArray[i].contains("false")){
                String ns = sArray[i].trim();
                tr.setCondition(ns);
            }
            else if(sArray[i].contains("same")){
                String ns = sArray[i].trim();
                tr.setExpression(ns);
            }
        }
        return tr;
    }

    /**
     * 将字符串s进行分割，也就是将关系R中的每一个转移关系进行分割
     * 分割尚未完全完善---------
     * @param s
     * @return Transition
     */
    public static Transition TransitionSp(String s) {
        String[] sArray = s.split("&");
        Transition tr = new Transition();
        if (sArray.length == 4) {
            for (int i = 0; i < sArray.length; i++) {
                if (i == 0) {
                    String s1 = sArray[0].trim().replaceAll("pc=", "");
                    tr.setTpc1(s1);
                }
                if (i == 1) {
                    String s1 = sArray[1].trim().replaceAll("pc'=", "");
                    String s2 = s1.replaceFirst("\\)", "");
                    tr.setNpc1(s2);
                }
                if (i == 2) {
                    String s1 = sArray[2].trim();
                    tr.setCondition(s1);
                }
                if (i == 3) {
                    tr.setExpression(sArray[3].trim().substring(0,sArray[3].length()-1));
                }

            }
        }
        if (sArray.length == 5) {
            for (int i = 0; i < sArray.length; i++) {
                if (i == 0) {
                    String s1 = sArray[0].replaceAll("\\(", "");
                    tr.setPC(s1);
//                    this.PC = s1;
                }
                if (i == 1) {
                    if (sArray[1].contains("PC1=")) {
//                        this.Tpc1 = sArray[1];
                        tr.setTpc1(sArray[1]);
                    } else if (sArray[1].contains("PC2=")) {
                        tr.setTpc2(sArray[1]);
//                        this.Tpc2 = sArray[1];
                    }
                }
                if (i == 2) {
                    if (sArray[2].contains("PC1'=")) {
                        tr.setNpc1(sArray[2]);
//                        this.Npc1 = sArray[2];
                    } else if (sArray[2].contains("PC2'=")) {
                        tr.setNpc2(sArray[2]);
//                        this.Npc2 = sArray[2];
                    } else {
                        tr.setCondition(sArray[2]);
//                        this.Condition = sArray[2];
                    }
                }
                if (i == 3) {
                    if (sArray[3].contains("SAME(")) {
                        tr.setExpression(sArray[3]);
//                        this.Expression = sArray[3];
                    }
                }
                if (i == 4) {
                    if (sArray[4].contains("SAME(")) {
                        tr.setExpressionT(sArray[4].substring(0, sArray[4].length() - 1));
//                        this.ExpressionT = sArray[4].substring(0,sArray[4].length()-1);
                    }
                }
            }
        }
        if (sArray.length == 6) {
            for (int i = 0; i < sArray.length; i++) {
                if (i == 0) {
                    String s1 = sArray[0].replaceAll("\\(", "");
                    tr.setPC(s1);
                }
                if (i == 1) {
                    if (sArray[1].contains("PC1=")) {
                        tr.setTpc1(sArray[1]);
                    } else if (sArray[1].contains("PC2=")) {
                        tr.setTpc2(sArray[1]);
                    }
                }
                if (i == 2) {
                    if (sArray[2].contains("PC1'=")) {
                        tr.setNpc1(sArray[2]);
                    } else if (sArray[2].contains("PC2'=")) {
                        tr.setNpc2(sArray[2]);
                    } else {
                        tr.setCondition(sArray[2]);
                    }
                }
                if (i == 3) {
                    if (sArray[3].contains("PC1'=")) {
                        tr.setNpc1(sArray[3]);
                    } else if (sArray[3].contains("PC2'=")) {
                        tr.setNpc2(sArray[3]);
                    }
                }
                if (i == 4) {
                    if (sArray[4].contains("SAME(")) {
                        tr.setExpression(sArray[4]);
                    }
                }
                if (i == 5) {
                    if (sArray[5].contains("SAME(")) {
                        tr.setExpressionT(sArray[5].substring(0, sArray[5].length() - 1));
                    }
                }
            }
        }
        return tr;
    }

    /**
     *
     * @param Tpc
     * @param V
     * @param GraphPath
     * @return
     */
    public static String appendGraph(String Tpc,V_Label_Value V,String GraphPath){
        GraphPath = GraphPath + "\"" + Tpc + " ";
        for(int zz = 0 ; zz<V.V.length;zz++){
            GraphPath = GraphPath+V.V[zz]+"="+V.value[zz]+" ";
        }
        if(Tpc.equals("#_End")) {
            GraphPath = GraphPath + "\" \n";
            return GraphPath;
        }
        else {
            GraphPath = GraphPath + "\" ->\n";
            return GraphPath;
        }

    }


    /**
     * 跳转到下一个关系
     * @param pc
     * @param node_state
     * @param condition
     * @param T
     * @param V
     */
    /**
     public static void NextRelation(String pc,Node_state node_state,String condition,ArrayList<Transition> T,V_Label_Value V){
     //        Update_Node_State(condition,V,node_state);

     if(pc.equals("#_End")){
     return;
     }
     for(int i = 0; i < T.size(); i++){
     if (T.get(i).getTpc1().equals("#_End")){
     return;
     }
     else if(T.get(i).getTpc1().equals(pc)){
     String TestCondition = T.get(i).getCondition();
     if(TestCondition == "true"){
     //更新节点
     //画图
     //NextRelation();
     }
     else if(TestCondition == "false"){
     //????---
     }
     else if(TestCondition.contains("==")&&!(TestCondition.contains("!"))){
     String[] Tc = TestCondition.split("==");
     for(int j = 0;j<V.V.length;j++){
     if(V.V[j].equals(Tc[0])){
     if(V.value[j] == Integer.parseInt(Tc[1])){
     for(int z = 0;z<V.V.length;z++)
     {
     System.out.print(V.V[z]+"---"+V.value[z]);
     }
     System.out.println("=======");
     NextRelation(T.get(i).getNpc1(),node_state,condition,T,V);
     }
     break;
     }
     }
     //解析==两边的变量，从V中找到匹配值，并获取当前节点的value值，进行判断
     //如果成功，则进行跳转，如果失败，则寻找下一个
     }
     else if(TestCondition.contains("<=")&&!(TestCondition.contains("!"))){
     String[] Tc = TestCondition.split("<=");
     for(int j = 0;j<V.V.length;j++){
     if(V.V[j].equals(Tc[0])){
     if(V.value[j] < Integer.parseInt(Tc[1])|| V.value[j] == Integer.parseInt(Tc[1])){
     for(int z = 0;z<V.V.length;z++)
     {
     System.out.print(V.V[z]+"---"+V.value[z]);
     }
     System.out.println("<=");
     System.out.println(T.get(i).getNpc1());
     NextRelation(T.get(i).getNpc1(),node_state,condition,T,V);
     break;
     }
     }
     }
     //解析<=两边的变量，从V中找到匹配值，并获取当前节点的value值，进行判断
     //如果成功，则进行跳转，如果失败，则寻找下一个
     }
     else if(TestCondition.contains("!")){
     if(TestCondition.contains("<=")){
     String[] Tc = TestCondition.substring(1,TestCondition.length()-1).split("<=");
     for(int j = 0;j<V.V.length;j++){
     if(V.V[j].equals(Tc[0])){
     if(V.value[j] > Integer.parseInt(Tc[1])){
     for(int z = 0;z<V.V.length;z++)
     {
     System.out.print(V.V[z]+"---"+V.value[z]);
     }
     System.out.println("!<=");
     System.out.println(T.get(i).getNpc1());
     NextRelation(T.get(i).getNpc1(),node_state,condition,T,V);
     break;
     }
     }
     }
     }
     else if(TestCondition.contains("==")){
     String[] Tc = TestCondition.substring(1,TestCondition.length()-1).split("==");
     //                        System.out.println("this is true");
     for(int j = 0;j<V.V.length;j++){
     if(V.V[j].equals(Tc[0])){
     if(V.value[j] > Integer.parseInt(Tc[1])||V.value[j] < Integer.parseInt(Tc[1])){
     for(int z = 0;z<V.V.length;z++)
     {
     System.out.print(V.V[z]+"---"+V.value[z]);
     }
     System.out.println("!==");
     System.out.println(T.get(i).getNpc1());
     NextRelation(T.get(i).getNpc1(),node_state,condition,T,V);
     break;
     }
     }
     }
     }
     //语义分析？？？
     }
     else if(TestCondition.contains("⋁")){
     //????---
     }
     else if(TestCondition.contains("⋀")){
     //????---
     }
     else if(TestCondition.contains("=")){
     String[] Tc = TestCondition.split("=");
     if(TestCondition.contains("+")){
     String[] tcx = Tc[1].split("\\+");
     for(int j = 0;j<V.V.length;j++){
     if(V.V[j].equals(Tc[0])){
     for(int z = 0; z < V.V.length; z++){
     if(V.V[z].equals(tcx[0])){
     V.value[j] = V.value[z]+Integer.parseInt(tcx[1]);
     break;
     }
     }
     break;
     }
     }
     }
     else if(TestCondition.contains("-")){
     String[] tcx = Tc[1].split("-");
     for(int j = 0;j<V.V.length;j++){
     if(V.V[j].equals(Tc[0])){
     for(int z = 0; z < V.V.length; z++){
     if(V.V[z].equals(tcx[0])){
     V.value[j] = V.value[z]-Integer.parseInt(tcx[1]);
     break;
     }
     }
     break;
     }
     }
     }
     else {
     for(int j = 0;j<V.V.length;j++){
     if(V.V[j].equals(Tc[0])){
     V.value[j] = Integer.parseInt(Tc[1]);
     break;
     }
     }
     }

     for(int j = 0;j<V.V.length;j++)
     {
     System.out.print(V.V[j]+"---"+V.value[j]);
     }
     System.out.println("=======");
     System.out.println(T.get(i).getNpc1());
     NextRelation(T.get(i).getNpc1(),node_state,condition,T,V);
     //更新节点信息，并进行跳转
     }
     }
     }
     }**/

    /**
     * 更新节点状态
     * @param condition
     * @param V
     * @param node_state
     */
    /**
     public static void Update_Node_State(String condition,String[] V,Node_state node_state){
     if(condition.contains("same")){
     String s1 = condition.substring(5,condition.length()-1);
     System.out.println(s1);

     if(s1.equals("V")){
     // do nothing!
     }
     else{
     String s2 = s1.substring(3,s1.length()-1);
     for(int i = 0; i < V.length; i++){
     // do noting!
     }
     }

     }

     if(condition.equals("c=2")){
     node_state.setY(2);
     }

     }
     **/

}