public class JoinNode extends OperationNode {
    String condition;

    public JoinNode(String condition) {
        super("JoinNode (" + condition + ")");
        this.condition = condition;
    }

    @Override
    void execute() {
        // Lógica de execução aqui
    }
}