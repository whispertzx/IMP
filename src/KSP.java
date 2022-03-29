/**
 * @auther Wing
 * @date 2021-10-24 13:06
 * 使用版本
 */
import javax.script.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import model.*;

public class KSP {
    private static String sOr = "⋁";

    public static void main(String[] args) throws Exception {


        /**
         * 读入转化后的一阶谓词逻辑文本，用正则表达式split的⋁进行分割，并将其转入迁移结构中
         * Test为读入的一阶谓词逻辑
         * AL为存储的所有状态迁移结构
         */

        File r = new File(System.getProperty("user.dir")+"/src/File/input.txt");
        BufferedReader br = new BufferedReader(new FileReader(r));
        String s = null;
        String Test = "";
        while ((s = br.readLine()) != null) {
            Test = Test + s + " ⋁\n";
        }
        Test = Test.substring(0,Test.length()-3);
        br.close();


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
         * 其中V需要预先定义完毕，包含变量有哪些以及变量的初始值
         */
        int[] D = new int[]{0,1,2};
        V_Label_Value V = new V_Label_Value(new String[]{"a", "c"},new int[]{0,0});//或者改为1,0
        V_Label_Value VT = new V_Label_Value(new String[]{"a", "c"},new int[]{0,0});//或者改为1,0
        String s0 = "a=0 & c=0";
        First_order_logic fol = new First_order_logic(D,V,s0,AL);

        /**
         * 测试输出所有的一阶逻辑谓词公式
         */
        for(int i = 0; i < fol.getR().size(); i++){
            System.out.println("--条件是："+fol.getRi(i).getCondition()+"--PC是："+fol.getRi(i).getPC()+"--NPC是："+fol.getRi(i).getNpc()+"--PC1是："+fol.getRi(i).getTpc1()+"\n"+
                    "--PC2是："+fol.getRi(i).getTpc2()+"--NPC1是："+fol.getRi(i).getNpc1()+"--NPC2是："+fol.getRi(i).getNpc2()+"-表达式是："+"\n"+
                    fol.getRi(i).getExpression()+"---"+fol.getRi(i).getExpressionT());
            System.out.println("---------------------------------------------");
        }

        /**
         * 定义节点和路径的存储A和B，以及构建路径的结构Sp
         */
        List<String> A = new ArrayList<String>();
        List<String> B = new ArrayList<String>();
        StateAndPath Sp = new StateAndPath(A,B);

        /**
         * 运行深度优先搜索，进行递归遍历，将并返回的重复的状态迁移删除
         */

        Sp = NextRelationN(AL.get(0).getPC(),Sp,"",AL,V,VT);
        List newList = removeDuplicate(Sp.getPath());

        /**
         * 将生产的状态迁移写入文本
         */
        File f = new File(System.getProperty("user.dir")+"/src/File/output4.txt");
        FileOutputStream fos1=new FileOutputStream(f);
        OutputStreamWriter dos1=new OutputStreamWriter(fos1);
        for(int t = 0; t < newList.size();t++){
            dos1.write(newList.get(t)+";\n");
        }
        for(int t = 0; t < Sp.getState().size();t++){
            dos1.write(Sp.getState().get(t)+";\n");
        }
        dos1.close();

        /**
         * 将画图的命令进行保存
         */
        File t = new File(System.getProperty("user.dir")+"/src/File/draw.txt");
        FileOutputStream fos2=new FileOutputStream(t);
        OutputStreamWriter dos2=new OutputStreamWriter(fos2);
        dos2.write("digraph pic {\n");
        for(int x = 0; x < newList.size();x++){
            dos2.write(newList.get(x)+";\n");
        }
        dos2.write("}");
        dos2.close();

        /**
         * 运行dot命令进行画图
         */
        genGraph(System.getProperty("user.dir")+"/src/File/draw.txt",System.getProperty("user.dir")+"/src/File/picture.png");

    }

    public static void genGraph(String sourcePath,String targetPath) throws IOException, InterruptedException {
        Runtime run = Runtime.getRuntime();
        run.exec("dot "+sourcePath+" -T png -o "+targetPath);
        Thread.sleep(1000);
    }

    public static String BuildPath(String Pc,String Pc1,String Pc2,V_Label_Value V){
        if(Pc.equals("-")){
            Pc = "#_UNDEFINED";
        }
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

    public static Boolean ifExist(StateAndPath Gp ,String Next){
        boolean a = false ;
        for(int i = 0; i < Gp.getState().size();i++){
            if(Next.equals(Gp.getState().get(i))){
                a = true ;
            }
        }
        return a;
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
            if (pc.equals("null")||pc.equals("#_End_1")){
                if(condition.equals("#_End_2")){
                    Sp = BuildGraph(Sp,BuildPath("#_UNDEFINED","#_End_1","#_End_2",V),BuildPath("#_End","#_UNDEFINED","#_UNDEFINED",V));
                    return Sp;
                }
                else {
                    //do nothing
                }
            }
            else if(T.get(i).getTpc1().equals(pc)){
                String TestCondition = T.get(i).getCondition();
                System.out.println(TestCondition);
                if(TestCondition.contains("=") && (!TestCondition.contains("<")) && (!TestCondition.contains("==")) && (!TestCondition.contains(">")) ){
                    //赋值语句
                    System.out.println("==========================进入赋值判断==========================");
                    String[] Tc = TestCondition.split("=");
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
                            se.eval(new FileReader(System.getProperty("user.dir")+"/src/model/assign.js"));
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
                    for(int j = 0;j<V.value.length;j++){
                        V.value[j] = (int) XYZ2.get(j);
                    }
                    if(ifExist(Sp,BuildPath("#_UNDEFINED",T.get(i).getNpc1(),condition,V))){
                        Sp = BuildGraph(Sp,BuildPath("#_UNDEFINED",pc,condition,Pre),BuildPath("#_UNDEFINED",T.get(i).getNpc1(),condition,V));
//                        Sp = NextRelation2(condition,Sp,T.get(i).getNpc1(),T,V,Pre);
                        return Sp;
                    }
                    else{
                        Sp = BuildGraph(Sp,BuildPath("#_UNDEFINED",pc,condition,Pre),BuildPath("#_UNDEFINED",T.get(i).getNpc1(),condition,V));
                        Sp = NextRelationN(T.get(i).getNpc1(),Sp,condition,T,V,Pre);
                        System.out.println("***********************尝试执行第二步骤***********************");
                        for(int j = 0;j<V.value.length;j++){
                            Pre.value[j] = (int) XYZ1.get(j);
                        }
                        for(int j = 0;j<V.value.length;j++){
                            V.value[j] = (int) XYZ2.get(j);
                        }
                        Sp = NextRelation2(condition,Sp,T.get(i).getNpc1(),T,V,Pre);
                        return Sp;
                    }
                }
                else{
                    System.out.println("==========================进入true or false判断==========================");
                    String[] s = new String[V.V.length+1];
                    String []AA = V.V;
                    int []BB = V.value;
                    List XYZ1 = new ArrayList();
                    for(int j = 0;j<V.value.length;j++){
                        XYZ1.add(V.value[j]);
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
                    se.eval(new FileReader(System.getProperty("user.dir")+"/src/model/test.js"));
                    if (se instanceof Invocable) {
                        Invocable in = (Invocable) se;
                        Boolean result = (Boolean) in.invokeFunction("judge",AA,BB,TestCondition);
                        if(result){
                            if(ifExist(Sp,BuildPath("#_UNDEFINED",T.get(i).getNpc1(),condition,V))){
                                Sp = BuildGraph(Sp,BuildPath("#_UNDEFINED",pc,condition,V),BuildPath("#_UNDEFINED",T.get(i).getNpc1(),condition,V));
//                                Sp = NextRelation2(condition,Sp,T.get(i).getNpc1(),T,V,Pre);
                                return Sp;
                            }
                            else {
                                Sp = BuildGraph(Sp,BuildPath("#_UNDEFINED",pc,condition,V),BuildPath("#_UNDEFINED",T.get(i).getNpc1(),condition,V));
                                Sp = NextRelationN(T.get(i).getNpc1(),Sp,condition,T,V,Pre);
                                System.out.println("***********************尝试执行第二步骤***********************");
                                for(int j = 0;j<V.value.length;j++){
                                    Pre.value[j] = (int) XYZ1.get(j);
                                }
                                for(int j = 0;j<V.value.length;j++){
                                    V.value[j] = (int) XYZ2.get(j);
                                }
//                                    System.out.println("当前的pc值为"+T.get(i).getTpc1());
//                                    System.out.println("下一12个状态的节点为："+T.get(i).getNpc1()+"!!!!!"+condition);
//                                    System.out.println("Pre的V和value值分别为"+ Arrays.toString(Pre.getV()) +"-----"+ Arrays.toString(Pre.getValue()));
//                                    System.out.println("下一个节点的V和value值分别为"+ Arrays.toString(V.getV()) +"-----"+ Arrays.toString(V.getValue()));
                                Sp = NextRelation2(condition,Sp,T.get(i).getNpc1(),T,V,Pre);
                                return Sp;
                            }
                        }
                        else {
                            System.out.println("False");
                        }
                    }
                }
            }
            else{
//                System.out.println("什么也不做");
                //未匹配成功
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
                if(condition.equals("#_End_1")){
                    Sp = BuildGraph(Sp,BuildPath("#_UNDEFINED","#_End_1","#_End_2",V),BuildPath("#_End","#_UNDEFINED","#_UNDEFINED",V));
                    return Sp;
                }
                else {
                    return Sp;
                    //do nothing
                }
            }
            else if(T.get(i).getTpc2().equals(pc)){
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

                            se.eval(new FileReader(System.getProperty("user.dir")+"/src/model/assign.js"));
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
                    for(int j = 0;j<V.value.length;j++){
                        V.value[j] = (int) XYZ2.get(j);
                    }
                    if(ifExist(Sp,BuildPath("#_UNDEFINED",condition,T.get(i).getNpc2(),V))){
                        Sp = BuildGraph(Sp,BuildPath("#_UNDEFINED",condition,pc,Pre),BuildPath("#_UNDEFINED",condition,T.get(i).getNpc2(),V));
                        return Sp;
                    }
                    else {
                        Sp = BuildGraph(Sp,BuildPath("#_UNDEFINED",condition,pc,Pre),BuildPath("#_UNDEFINED",condition,T.get(i).getNpc2(),V));
                        Sp = NextRelationN(condition,Sp,T.get(i).getNpc2(),T,V,Pre);
                        for(int j = 0;j<V.value.length;j++){
                            Pre.value[j] = (int) XYZ1.get(j);
                        }
                        for(int j = 0;j<V.value.length;j++){
                            V.value[j] = (int) XYZ2.get(j);
                        }
                        Sp = NextRelation2(T.get(i).getNpc2(),Sp,condition,T,V,Pre);
                        return Sp;
                    }
                }
                else if(TestCondition == null){
                    return Sp;
                    //do nothing?
                }
                else{
                    System.out.println("==========================进入true or false判断==========================");
                    String[] s = new String[V.V.length+1];
                    String []AA = V.V;
                    int []BB = V.value;
                    List XYZ1 = new ArrayList();
                    for(int j = 0;j<V.value.length;j++){
                        XYZ1.add(V.value[j]);
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
                    se.eval(new FileReader(System.getProperty("user.dir")+"/src/model/test.js"));
                    if (se instanceof Invocable) {
                        Invocable in = (Invocable) se;
                        Boolean result = (Boolean) in.invokeFunction("judge",AA,BB,TestCondition);
                        if(result){
                            if(ifExist(Sp,BuildPath("#_UNDEFINED",condition,T.get(i).getNpc2(),V))){
                                Sp = BuildGraph(Sp,BuildPath("#_UNDEFINED",condition,pc,V),BuildPath("#_UNDEFINED",condition,T.get(i).getNpc2(),V));
//                                Sp = NextRelationN(condition,Sp,T.get(i).getNpc2(),T,V,Pre);
                                return Sp;
                            }
                            else {
                                Sp = BuildGraph(Sp,BuildPath("#_UNDEFINED",condition,pc,V),BuildPath("#_UNDEFINED",condition,T.get(i).getNpc2(),V));
                                Sp = NextRelation2(T.get(i).getNpc2(),Sp,condition,T,V,Pre);
                                for(int j = 0;j<V.value.length;j++){
                                    Pre.value[j] = (int) XYZ1.get(j);
                                }
                                for(int j = 0;j<V.value.length;j++){
                                    V.value[j] = (int) XYZ2.get(j);
                                }
//                                System.out.println("当前的pc值为"+T.get(i).getTpc2());
//                                System.out.println("下一21个状态的节点为："+T.get(i).getNpc2()+"!!!!!"+condition);
//                                System.out.println("Pre的V和value值分别为"+ Arrays.toString(Pre.getV()) +"-----"+ Arrays.toString(Pre.getValue()));
//                                System.out.println("下一个节点的V和value值分别为"+ Arrays.toString(V.getV()) +"-----"+ Arrays.toString(V.getValue()));
                                Sp = NextRelationN(condition,Sp,T.get(i).getNpc2(),T,V,Pre);
                                return Sp;
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

