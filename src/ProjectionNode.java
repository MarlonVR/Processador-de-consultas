import java.util.Arrays;

public class ProjectionNode extends OperationNode {
    String[] attributes;

    public ProjectionNode(String[] attributes) {
        super("ProjectionNode (" + Arrays.toString(attributes) + ")");
        this.attributes = attributes;
    }

    @Override
    void execute() {
        // Lógica de execução aqui
    }
}