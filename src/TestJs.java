/**
 * @auther Wing
 * @date 2021-10-10 16:20
 * js测试
 */

import java.io.FileReader;
import java.util.Scanner;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class TestJs {
    public static void main(String[] args) throws Exception {

        // 获取JS执行引擎
        ScriptEngine se = new ScriptEngineManager().getEngineByName("javascript");
        // 获取变量
        Bindings bindings = se.createBindings();
        se.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
//        Scanner sc = new Scanner(System.in);
//        while (sc.hasNextInt()) {
            String s = "a = a * c + a";
            String []AA = new String[]{"a", "c"};
            int []BB = new int[]{2,3};
//            V_Label_Value V = new V_Label_Value(new String[]{"a", "c"},new int[]{3,0});
//            int a = 3;
//            int a = sc.nextInt();
//            int b = sc.nextInt();
//            System.out.println("输入的参数【" + a + "】 + 【" + b + "】");
            System.out.println("c的值为"+s);
            se.eval(new FileReader("/Users/wing/Desktop/assign.js"));
            // 是否可调用
            if (se instanceof Invocable) {
                Invocable in = (Invocable) se;
                int result = ((Double) in.invokeFunction("assign",AA,BB,s)).intValue();
                System.out.println("获得的结果：" + result);

            }

//        }

    }
}
