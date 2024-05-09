import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import javax.swing.*;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class DisplayGraph {
    public DisplayGraph(SQLToAlgebraConverter.Node root){
        visualizeGraph(root);
    }
    private void visualizeGraph(SQLToAlgebraConverter.Node root) {
        Graph<String, DefaultEdge> graph = new SimpleDirectedGraph<>(DefaultEdge.class);
        addNodesAndEdges(root, graph);
        JFrame frame = new JFrame("SQL Query Graph Visualization");
        JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<>(graph);
        configureStyles(graphAdapter);
        configureAndExecuteLayout(graphAdapter);
        mxGraphComponent graphComponent = new mxGraphComponent(graphAdapter);
        graphComponent.setConnectable(false);
        graphComponent.zoomAndCenter();
        frame.add(graphComponent);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private void addNodesAndEdges(SQLToAlgebraConverter.Node root, Graph<String, DefaultEdge> graph) {
        Queue<SQLToAlgebraConverter.Node> queue = new LinkedList<>();
        queue.add(root);
        graph.addVertex(root.toString());

        while (!queue.isEmpty()) {
            SQLToAlgebraConverter.Node current = queue.remove();
            for (SQLToAlgebraConverter.Node child : current.children) {
                graph.addVertex(child.toString());
                graph.addEdge(current.toString(), child.toString());
                queue.add(child);
            }
        }
    }

    private void configureStyles(JGraphXAdapter<String, DefaultEdge> graphAdapter) {
        Map<String, Object> style = graphAdapter.getStylesheet().getDefaultVertexStyle();
        style.put(mxConstants.STYLE_FILLCOLOR, "#FFFFFF");
        style.put(mxConstants.STYLE_STROKECOLOR, "#000000");
        style.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        style.put(mxConstants.STYLE_FONTSIZE, 12);
        style.put(mxConstants.STYLE_AUTOSIZE, "1");
        style.put(mxConstants.STYLE_SPACING, 15);
        style.put(mxConstants.STYLE_SPACING_TOP, 5);
        style.put(mxConstants.STYLE_SPACING_BOTTOM, 5);
        style.put(mxConstants.STYLE_SPACING_LEFT, 5);
        style.put(mxConstants.STYLE_SPACING_RIGHT, 5);

        Map<String, Object> edgeStyle = graphAdapter.getStylesheet().getDefaultEdgeStyle();
        edgeStyle.put(mxConstants.STYLE_ROUNDED, true);
        edgeStyle.put(mxConstants.STYLE_EDGE, mxConstants.EDGESTYLE_ELBOW);
        edgeStyle.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        edgeStyle.put(mxConstants.STYLE_LABEL_BACKGROUNDCOLOR, "#FFFFFF");
        edgeStyle.put(mxConstants.STYLE_NOLABEL, "1");

        edgeStyle.put(mxConstants.STYLE_ENDARROW, mxConstants.ARROW_CLASSIC);
        edgeStyle.put(mxConstants.STYLE_ROTATION, 180); // Rotação para cima
    }

    private static void configureAndExecuteLayout(JGraphXAdapter<String, DefaultEdge> graphAdapter) {
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graphAdapter);
        layout.setIntraCellSpacing(50);
        layout.execute(graphAdapter.getDefaultParent());
    }
}
