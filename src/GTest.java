/**
 * @auther Wing
 * @date 2021-09-27 18:43
 * 调用测试
 */
import java.io.File;

public class GTest {
    public static void main(String[] args){
        GTest gtest = new GTest();
        String[] nodes = {"A","B","C","D","E","F","G"};
        String[] preline = {"B -> A","D -> B","E -> D","C -> E","G -> C","F -> G"};
        gtest.start(nodes, preline);
    }
    private void start(String[] nodes,String[] preline){

        Graphviz gv = new Graphviz();
        //定义每个节点的style
        String nodesty = "[shape = polygon, sides = 6, peripheries = 2, color = lightblue, style = filled]";
        //String linesty = "[dir=\"none\"]";

        gv.addln(gv.start_graph());//SATRT
        gv.addln("edge[fontname=\"DFKai-SB\" fontsize=15 fontcolor=\"black\" color=\"brown\" style=\"filled\"]");
        gv.addln("size =\"8,8\";");
        //设置节点的style
        for(int i=0;i<nodes.length;i++){
            gv.addln(nodes[i]+" "+nodesty);
        }
        for(int i=0;i<preline.length;i++){
            gv.addln(preline[i]+" "+" [dir=\"none\"]");
        }
        gv.addln(gv.end_graph());//END
        //节点之间的连接关系输出到控制台
        System.out.println(gv.getDotSource());
        //输出什么格式的图片(gif,dot,fig,pdf,ps,svg,png,plain)
        String type = "png";
        //输出到文件夹以及命名
        File out = new File("/Users/wing/Desktop/test." + type);   // Linux
        //File out = new File("c:/eclipse.ws/graphviz-java-api/out." + type);    // Windows
        gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), type ), out );
    }
}
