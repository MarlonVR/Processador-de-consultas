import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLParser {
    static class RelationalAlgebra {
        static class Projection {
            String[] attributes;
            Projection(String... attributes) {
                this.attributes = attributes;
            }
        }

        static class Selection {
            String condition;
            Selection(String condition) {
                this.condition = condition;
            }
        }

        static class Join {
            String condition;
            Join(String condition) {
                this.condition = condition;
            }
        }

        static class Relation {
            String name;
            Relation(String name) {
                this.name = name;
            }
        }
    }

    static class SQLtoAlgebra {
        static RelationalAlgebra.Projection convertSelectClause(String selectClause) {
            return new RelationalAlgebra.Projection(selectClause.split(",\\s*"));
        }

        static RelationalAlgebra.Selection convertWhereClause(String whereClause) {
            return new RelationalAlgebra.Selection(whereClause);
        }

        static RelationalAlgebra.Join convertJoinClause(String joinTable, String onClause) {
            return new RelationalAlgebra.Join(onClause);
        }

        static RelationalAlgebra.Relation convertFromClause(String fromClause) {
            return new RelationalAlgebra.Relation(fromClause);
        }
    }

    private static final Pattern SELECT_PATTERN = Pattern.compile("SELECT\\s+(.*?)\\s+FROM", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final Pattern FROM_PATTERN = Pattern.compile("FROM\\s+(.*?)\\s*(WHERE|JOIN|$)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final Pattern WHERE_PATTERN = Pattern.compile("WHERE\\s+(.*?)(\\Z|ORDER BY|GROUP BY|HAVING)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final Pattern JOIN_PATTERN = Pattern.compile("JOIN\\s+([^\\s]+)\\s+ON\\s+([^\\)]+?)(?=\\s+JOIN|\\s+WHERE|\\Z)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    private QueryTree graph;
    private OperationNode currentNode;

    public void parseSQL(String sql) {
        long startTime = System.currentTimeMillis();
        System.out.println("Parsing Query:\n" + sql);
        sql = sql.replaceAll("\\s+", " ").trim();
        this.graph = new QueryTree(); // Inicializando a árvore de consulta

        // Definindo o nó raiz da árvore a partir da cláusula FROM
        OperationNode lastNode = null;
        Matcher fromMatcher = FROM_PATTERN.matcher(sql);
        if (fromMatcher.find()) {
            RelationalAlgebra.Relation relation = SQLtoAlgebra.convertFromClause(fromMatcher.group(1).trim());
            RelationNode relationNode = new RelationNode(relation.name);
            graph.setRootNode(relationNode);
            lastNode = relationNode;
        }

        // Processando junções
        Matcher joinMatcher = JOIN_PATTERN.matcher(sql);
        while (joinMatcher.find()) {
            RelationalAlgebra.Join join = SQLtoAlgebra.convertJoinClause(joinMatcher.group(1).trim(), joinMatcher.group(2).trim());
            JoinNode joinNode = new JoinNode(join.condition);
            graph.addNode(lastNode, joinNode); // Adicionando o nó de junção
            lastNode = joinNode; // Atualiza lastNode para o nó de junção recém-adicionado
        }

        // Processando a cláusula WHERE
        Matcher whereMatcher = WHERE_PATTERN.matcher(sql);
        if (whereMatcher.find()) {
            RelationalAlgebra.Selection selection = SQLtoAlgebra.convertWhereClause(whereMatcher.group(1).trim());
            SelectionNode selectionNode = new SelectionNode(selection.condition);
            graph.addNode(lastNode, selectionNode); // Adicionando o nó de seleção
            lastNode = selectionNode; // Atualiza lastNode para o nó de seleção
        }

        // Processando a cláusula SELECT
        Matcher selectMatcher = SELECT_PATTERN.matcher(sql);
        if (selectMatcher.find()) {
            RelationalAlgebra.Projection projection = SQLtoAlgebra.convertSelectClause(selectMatcher.group(1).trim());
            ProjectionNode projectionNode = new ProjectionNode(projection.attributes);
            graph.addNode(lastNode, projectionNode); // Adicionando o nó de projeção
            lastNode = projectionNode; // Atualiza lastNode para o nó de projeção
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Parsing time: " + (endTime - startTime) + " ms\n");

        visualizeTree(); // Chamando a visualização da árvore após a construção
    }


    public void visualizeTree() {
        if (graph.getRoot() != null) {
            TreeVisualizer visualizer = new TreeVisualizer(graph.getRoot());
            visualizer.setVisible(true);
        } else {
            System.out.println("No tree to visualize.");
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter SQL query (end input by pressing Enter on an empty line):");

        StringBuilder sqlBuilder = new StringBuilder();
        String line;
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            if (line.isEmpty()) {
                break;
            }
            sqlBuilder.append(line).append("\n");
        }

        String sqlQuery = sqlBuilder.toString();
        SQLParser parser = new SQLParser();
        parser.parseSQL(sqlQuery);
        parser.visualizeTree();
        scanner.close();
    }
/*SELECT cliente.nome, pedido.id
FROM Cliente
JOIN pedido ON cliente.id = pedido.cliente_id
WHERE cliente.ativo = 1
AND (pedido.valor_total > 500 OR pedido.data > '2021-01-01')*/

}
