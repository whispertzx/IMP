import javax.script.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import model.*;

/**
 * @auther Wing
 * @date 2021-10-23 18:49
 * 更新版本2
 */
public class IMP {
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

        File r = new File("/Users/wing/Desktop/input.txt");
        BufferedReader br = new BufferedReader(new FileReader(r));// 构造一个BufferedReader类来读取文件
        String s = null;
        String Test = "";
        while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
            Test = Test + s + " ⋁\n";
        }
        Test = Test.substring(0,Test.length()-3);
        br.close();

        System.out.println(Test);

        String test3 = "pc=#_Start & pc'=#_UNDEFINED & pc_1=#_UNDEFINED & pc_1'=#_1 & pc_2=#_UNDEFINED & pc_2'=#_9 ⋁\n" +
                "pc_1=#_1 & pc'_1=#_2 & same(pc_2,pc) & c=0 & same(V\\V_1{c}) ⋁\n" +
                "pc_1=#_2 & pc'_1=#_3 & same(pc_2,pc) & a=0 & same(V\\V_1{a}) ⋁\n" +
                "pc_1=#_3 & pc'_1=#_4 & same(pc_2,pc) & c<=1 & same(V) ⋁\n" +
                "pc_1=#_3 & pc'_1=#_End_1 & same(pc_2,pc) & !(c<=1) & same(V) ⋁\n" +
                "pc_1=#_4 & pc'_1=#_5 & same(pc_2,pc) & a=a+1 & same(V\\V_1{a}) ⋁\n" +
                "pc_1=#_5 & pc'_1=#_6 & same(pc_2,pc) & a==2 & same(V) ⋁\n" +
                "pc_1=#_5 & pc'_1=#_7 & same(pc_2,pc) & !(a==2) & same(V) ⋁\n" +
                "pc_1=#_6 & pc'_1=#_8 & same(pc_2,pc) & a=0 & same(V\\V_1{a}) ⋁\n" +
                "pc_1=#_7 & pc'_1=#_8 & same(pc_2,pc) & a=1 & same(V\\V_1{a}) ⋁\n" +
                "pc_1=#_8 & pc'_1=#_3 & same(pc_2,pc) & c=c+1 & same(V\\V_1{c}) ⋁\n" +
                "pc_2=#_9 & pc'_2=#_10 & same(pc_1,pc) & a==0 & same(V_2) ⋁\n" +
                "pc_2=#_9 & pc'_2=#_9 & same(pc_1,pc) & !(a==0) & same(V_2) ⋁\n" +
                "pc_2=#_10 & pc'_2=#_End_2 & same(pc_1,pc) & c=2 & same(V\\V_2{c}) ⋁\n" +
                "pc=#_UNDEFINED & pc'=#_End & pc_1=#_End_1 & pc_1'=#_UNDEFINED & pc_2=#_End_2 & pc_2'=#_UNDEFINED";



        String[] sArray=Test.split(sOr);
        for (int i = 0 ; i <sArray.length ; i++ )
        {
            System.out.println(sArray[i]);
        }
        ArrayList<Transition> AL = new ArrayList<Transition>();
        for(int i = 0; i < sArray.length; i++){
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
            System.out.println("--条件是："+fol.getRi(i).getCondition()+"--PC是："+fol.getRi(i).getPC()+"--NPC是："+fol.getRi(i).getNpc()+"--PC1是："+fol.getRi(i).getTpc1()+"\n"+
                    "--PC2是："+fol.getRi(i).getTpc2()+"--NPC1是："+fol.getRi(i).getNpc1()+"--NPC2是："+fol.getRi(i).getNpc2()+"-表达式是："+"\n"+
                    fol.getRi(i).getExpression()+"---"+fol.getRi(i).getExpressionT());
            System.out.println("---------------------------------------------");
        }


//        List<String> A = new ArrayList<String>(){{
//            add("\"#_Start #_UNDEFINED #_UNDEFINED \\n a=0 c=0 \"");
//        }};
        List<String> A = new ArrayList<String>();
        List<String> B = new ArrayList<String>();
        StateAndPath Sp = new StateAndPath(A,B);
        Sp = NextRelationN(AL.get(0).getPC(),Sp,"c=0",AL,V,VT);

        List newList = removeDuplicate(Sp.getPath());
        for(int t = 0; t < newList.size();t++){
            System.out.println(newList.get(t)+";");
        }
        for(int t = 0; t < Sp.getState().size();t++){
            System.out.println(Sp.getState().get(t)+";");
        }

        File f = new File("/Users/wing/Desktop/output.txt");
        FileOutputStream fos1=new FileOutputStream(f);
        OutputStreamWriter dos1=new OutputStreamWriter(fos1);
        for(int t = 0; t < newList.size();t++){
            dos1.write(newList.get(t)+";\n");
        }
        for(int t = 0; t < Sp.getState().size();t++){
            dos1.write(Sp.getState().get(t)+";\n");
        }
        dos1.close();
    }

    public static String BuildPath(String Pc,String Pc1,String Pc2,V_Label_Value V){
        if(Pc2.equals("-")){
            Pc2 = "#_End_2";
        }
        String GraphPath =  "\"" + Pc +" " + Pc1 +" " + Pc2 + " " + "\\n ";
        for(int zz = 0 ; zz<V.V.length;zz++){
            GraphPath = GraphPath+V.V[zz]+"="+V.value[zz]+" ";
        }
        GraphPath = GraphPath + "\"";
        return GraphPath;
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
        if(pc.equals("STOP") || pc.equals("#_End")){
            System.out.println("hhhhhhhhh");
            System.out.println(Sp.getPath());
            System.out.println(Sp.getState());
            return Sp;
        }
        if(pc.equals("#_Start")){
            System.out.println("进入START状态");
            Sp = BuildGraph(Sp,BuildPath(pc,T.get(0).getTpc1(),T.get(0).getTpc2(),V),BuildPath(T.get(0).getNpc(),T.get(0).getNpc1(),T.get(0).getNpc2(),V));
            List XYZ1 = new ArrayList();
            for(int j = 0;j<Pre.value.length;j++){
                XYZ1.add(Pre.value[j]);
            }
            List XYZ2 = new ArrayList();
            for(int j = 0;j<V.value.length;j++){
                XYZ2.add(V.value[j]);
            }
            Sp = NextRelationN(T.get(0).getNpc1(),Sp,T.get(0).getNpc2(),T,V,Pre);
            System.out.println("***********************尝试执行第二步骤***********************");
            for(int j = 0;j<V.value.length;j++){
                Pre.value[j] = (int) XYZ1.get(j);
            }
            for(int j = 0;j<V.value.length;j++){
                V.value[j] = (int) XYZ2.get(j);
            }
            Sp = NextRelation2(T.get(0).getNpc2(),Sp,T.get(0).getNpc1(),T,V,Pre);
        }
        for(int i = 0; i < T.size(); i++) {
            System.out.println(i);
            if (pc.equals("null")||pc.equals("#_End_1")){
                //nothing
            }
            else if(T.get(i).getTpc1().equals(pc)){
                String TestCondition = T.get(i).getCondition();
                System.out.println(TestCondition);
                if(TestCondition.contains("=") && (!TestCondition.contains("<")) && (!TestCondition.contains("==")) && (!TestCondition.contains(">")) ){
                    //赋值语句
                    System.out.println("==========================进入赋值判断==========================");
                    String[] Tc = TestCondition.split("=");
                    /*
                    引入 XYZ 作为临时变量，这边的V_Label_Value新建会产生数据同步？？？尚未解决
                     */
                    List XYZ1 = new ArrayList();
                    for(int j = 0;j<V.value.length;j++){
                        XYZ1.add(V.value[j]);
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
                    System.out.println("当前的pc值为"+T.get(i).getTpc1());
                    System.out.println("下一个状态的节点为："+T.get(i).getNpc1()+"!!!!!");
                    System.out.println("Pre的V和value值分别为"+ Arrays.toString(Pre.getV()) +"-----"+ Arrays.toString(Pre.getValue()));
                    System.out.println("下一个节点的V和value值分别为"+ Arrays.toString(V.getV()) +"-----"+ Arrays.toString(V.getValue()));
                    List XYZ2 = new ArrayList();
                    for(int j = 0;j<V.value.length;j++){
                        XYZ2.add(V.value[j]);
                    }
                    for(int j = 0;j<V.value.length;j++){
                        Pre.value[j] = (int) XYZ1.get(j);
                    }
                    Sp = BuildGraph(Sp,BuildPath("#_UNDEFINED",pc,condition,Pre),BuildPath("#_UNDEFINED",T.get(i).getNpc1(),condition,V));
                    for(int j = 0;j<V.value.length;j++){
                        Pre.value[j] = V.value[j];
                    }
                    System.out.println("准备执行下一步！！！！");
                    Sp = NextRelationN(T.get(i).getNpc1(),Sp,condition,T,V,Pre);
                    System.out.println("***********************尝试执行第二步骤***********************");
                    for(int j = 0;j<V.value.length;j++){
                        Pre.value[j] = (int) XYZ1.get(j);
                    }
                    for(int j = 0;j<V.value.length;j++){
                        V.value[j] = (int) XYZ2.get(j);
                    }
                    Sp = NextRelation2(condition,Sp,T.get(i).getNpc1(),T,V,Pre);
                    break;
                }
                else{
                    System.out.println("==========================进入true or false判断==========================");
                    String[] s = new String[V.V.length+1];
                    String []AA = V.V;
                    int []BB = V.value;
                    List XYZ1 = new ArrayList();
                    for(int j = 0;j<Pre.value.length;j++){
                        XYZ1.add(Pre.value[j]);
                    }
                    List XYZ2 = new ArrayList();
                    for(int j = 0;j<V.value.length;j++){
                        XYZ2.add(V.value[j]);
                    }
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
                            Sp = BuildGraph(Sp,BuildPath("#_UNDEFINED",pc,condition,V),BuildPath("#_UNDEFINED",T.get(i).getNpc1(),condition,V));
//                            Sp = BuildGraph(Sp,BuildPath(T.get(i).getTpc1(),V),BuildPath(T.get(i).getNpc1(),V));
                            Sp = NextRelationN(T.get(i).getNpc1(),Sp,condition,T,V,Pre);
                            System.out.println("***********************尝试执行第二步骤***********************");
                            for(int j = 0;j<V.value.length;j++){
                                Pre.value[j] = (int) XYZ1.get(j);
                            }
                            for(int j = 0;j<V.value.length;j++){
                                V.value[j] = (int) XYZ2.get(j);
                            }
                            Sp = NextRelation2(condition,Sp,T.get(i).getNpc1(),T,V,Pre);
                            break;
                        }
                        else {
                            System.out.println("False");
                        }

                    }
                }
            }
            else{
                System.out.println("什么也不做");
                //do nothing
            }
        }
        return Sp;
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
    public static StateAndPath NextRelation2(String pc,StateAndPath Sp,String condition,ArrayList<Transition> T,V_Label_Value V,V_Label_Value Pre) throws Exception {
        if(pc.equals("#_End")){
            return Sp;
        }
        for(int i = 0; i < T.size(); i++) {
            if (pc.equals("null")||pc.equals("#_End_2")){
                //do nothing
            }
            else if(T.get(i).getTpc2().equals(pc)){
                System.out.println("这是第二个的测试"+i);
                String TestCondition = T.get(i).getCondition();
                System.out.println(TestCondition);
                if(TestCondition.contains("=") && (!TestCondition.contains("<")) && (!TestCondition.contains("==")) && (!TestCondition.contains(">")) ){
                    //赋值语句
                    System.out.println("==========================进入赋值判断==========================");
                    String[] Tc = TestCondition.split("=");
                    /*
                    引入 XYZ 作为临时变量，这边的V_Label_Value新建会产生数据同步？？？尚未解决
                     */
                    List XYZ1 = new ArrayList();
                    for(int j = 0;j<V.value.length;j++){
                        XYZ1.add(V.value[j]);
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
                    List XYZ2 = new ArrayList();
                    for(int j = 0;j<V.value.length;j++){
                        XYZ2.add(V.value[j]);
                    }
                    for(int j = 0;j<V.value.length;j++){
                        Pre.value[j] = (int) XYZ1.get(j);
                    }
                    Sp = BuildGraph(Sp,BuildPath("#_UNDEFINED",condition,pc,Pre),BuildPath("#_UNDEFINED",condition,T.get(i).getNpc1(),V));
                    for(int j = 0;j<V.value.length;j++){
                        Pre.value[j] = V.value[j];
                    }
                    Sp = NextRelationN(condition,Sp,T.get(i).getNpc2(),T,V,Pre);
                    for(int j = 0;j<V.value.length;j++){
                        Pre.value[j] = (int) XYZ1.get(j);
                    }
                    for(int j = 0;j<V.value.length;j++){
                        V.value[j] = (int) XYZ2.get(j);
                    }
                    Sp = NextRelation2(T.get(i).getNpc2(),Sp,condition,T,V,Pre);
                    break;
                }
                else if(TestCondition == null){
                    //do nothing?
                }
                else{
                    System.out.println("==========================进入true or false判断==========================");
                    String[] s = new String[V.V.length+1];
                    String []AA = V.V;
                    int []BB = V.value;
                    List XYZ1 = new ArrayList();
                    for(int j = 0;j<Pre.value.length;j++){
                        XYZ1.add(Pre.value[j]);
                    }
                    List XYZ2 = new ArrayList();
                    for(int j = 0;j<V.value.length;j++){
                        XYZ2.add(V.value[j]);
                    }
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
                            if(T.get(i).getNpc2().equals(pc)){
                                Sp = BuildGraph(Sp,BuildPath("#_UNDEFINED",condition,pc,V),BuildPath("#_UNDEFINED",condition,pc,V));
                                return Sp;
                            }
                            else {
                                Sp = BuildGraph(Sp,BuildPath("#_UNDEFINED",condition,pc,V),BuildPath("#_UNDEFINED",condition,T.get(i).getNpc2(),V));
                                Sp = NextRelationN(condition,Sp,T.get(i).getNpc2(),T,V,Pre);
                                for(int j = 0;j<V.value.length;j++){
                                    Pre.value[j] = (int) XYZ1.get(j);
                                }
                                for(int j = 0;j<V.value.length;j++){
                                    V.value[j] = (int) XYZ2.get(j);
                                }
                                Sp = NextRelation2(T.get(i).getNpc2(),Sp,condition,T,V,Pre);
                            }
                        }
                        else {
                            System.out.println("False");
                        }

                    }
                }
            }
        }
        return Sp;
    }

    public static List removeDuplicate(List list) {
        HashSet hashSet = new HashSet(list);
        list.clear();
        list.addAll(hashSet);
        return list;
    }

    public static Transition TransitionNsp(String s){
        String[] sArray = s.split("&");
        Transition tr = new Transition("-","-","-","-","-","-","-","-","-");
        for(int i=0; i<sArray.length; i++){

            if(sArray[i].contains("same")){
                String ns = sArray[i].trim();
                tr.setExpression(ns);
            }
            else if(sArray[i].contains("pc=")){
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
//            else if(sArray[i].contains("same")){
//
//            }
        }
        return tr;
    }


}
