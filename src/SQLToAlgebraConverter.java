import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLToAlgebraConverter {
    private static final Pattern SELECT_PATTERN = Pattern.compile("^SELECT\\s+(.*)$", Pattern.CASE_INSENSITIVE);
    private static final Pattern FROM_PATTERN = Pattern.compile("^FROM\\s+(.*)$", Pattern.CASE_INSENSITIVE);
    private static final Pattern JOIN_PATTERN = Pattern.compile("^JOIN\\s+(.*?)\\s+ON\\s+(.*)$", Pattern.CASE_INSENSITIVE);
    private static final Pattern WHERE_PATTERN = Pattern.compile("^WHERE\\s+(.*)$", Pattern.CASE_INSENSITIVE);

    static class Node {
        String operation;
        List<Node> children = new ArrayList<>();

        Node(String operation) {
            this.operation = operation;
        }

        void addChild(Node child) {
            children.add(child);
        }

        @Override
        public String toString() {
            return operation;
        }
    }

    public static Node buildQueryGraph(String[] sqlLines, Schema schema) {
        Node root = null;
        Node current = null;

        for (String line : sqlLines) {
            Matcher m = SELECT_PATTERN.matcher(line);
            if (m.matches()) {
                root = new Node("π " + m.group(1));
                continue;
            }

            m = FROM_PATTERN.matcher(line);
            if (m.matches()) {
                String tableName = m.group(1);
                if (!schema.tableExists(tableName)) {
                    System.err.println("Tabela '" + tableName + "' não encontrada no esquema.");
                    return null;
                }
                current = new Node(tableName);
                continue;
            }

            m = JOIN_PATTERN.matcher(line);
            if (m.matches()) {
                String tableName = m.group(1);

                if (!schema.tableExists(tableName)) {
                    System.err.println("Tabela '" + tableName + "' não encontrada no esquema.");
                    return null;
                }
                if (current == null) {
                    System.err.println("Erro: cláusula JOIN antes da definição da tabela FROM.");
                    return null;
                }
                Node joinNode = new Node("⨝ " + m.group(2));
                joinNode.addChild(current);
                joinNode.addChild(new Node(tableName));
                current = joinNode;
                continue;
            }

            m = WHERE_PATTERN.matcher(line);
            if (m.matches()) {
                if (current == null) {
                    System.err.println("Erro: cláusula WHERE antes da definição da tabela FROM.");
                    return null;
                }
                Node whereNode = new Node("σ " + m.group(1));
                whereNode.addChild(current);
                current = whereNode;
            }
        }
        if (root != null && current != null) {
            root.addChild(current);
        }

        return root;
    }


    public static String printExecutionOrder(Node root) {
        StringBuilder executionOrder = new StringBuilder();
        List<String> executionList = new ArrayList<>();
        buildExecutionOrder(root, executionList);
        Collections.reverse(executionList);
        int count = 1;
        for (String operation : executionList) {
            executionOrder.append(count++).append(". ").append(operation).append("\n");
        }
        return executionOrder.toString();
    }
    private static void buildExecutionOrder(Node node, List<String> executionOrder) {
        if (node == null) {
            return;
        }
        executionOrder.add(node.operation);
        for (Node child : node.children) {
            buildExecutionOrder(child, executionOrder);
        }
    }

    public static String generateAlgebra(Node node) {
        StringBuilder builder = new StringBuilder();
        builder.append(node.operation);

        if (!node.children.isEmpty()) {
            builder.append(" (");
            for (int i = 0; i < node.children.size(); i++) {
                if (i > 0) {
                    builder.append(", ");
                }
                builder.append(generateAlgebra(node.children.get(i)));
            }
            builder.append(")");
        }

        return builder.toString();
    }

    public static void main(String[] args) {
        QueryProcessorGUI gui = new QueryProcessorGUI();

    }
}
