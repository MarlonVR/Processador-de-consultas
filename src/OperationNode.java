import java.util.ArrayList;
import java.util.List;

abstract class OperationNode {
    List<OperationNode> children;
    String operationDescription;

    public OperationNode(String operationDescription) {
        this.children = new ArrayList<>();
        this.operationDescription = operationDescription;
    }

    abstract void execute();

    void addChild(OperationNode child) {
        children.add(child);
    }

}


