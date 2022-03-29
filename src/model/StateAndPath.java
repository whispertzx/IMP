package model;
import java.util.List;

/**
 * @auther Wing
 * @date 2021-10-10 19:31
 */
public class StateAndPath {

    private List <String>State;
    private List <String>Path;

    public StateAndPath(){}

    public StateAndPath(List <String>state,List <String>path){
        this.State = state;
        this.Path = path;
    }

    public List<String> getState() {
        return State;
    }

    public void setState(List<String> state) {
        State = state;
    }

    public List<String> getPath() {
        return Path;
    }

    public void setPath(List<String> path) {
        Path = path;
    }

}
