public class SelectionNode extends OperationNode {
    String condition;

    public SelectionNode(String condition) {
        super("SelectionNode (" + condition + ")");
        this.condition = condition;
    }

    @Override
    void execute() {
        //execução aqui
    }
}