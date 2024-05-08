import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TreeVisualizer extends JFrame {
    private OperationNode root;

    public TreeVisualizer(OperationNode root) {
        this.root = root;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Query Execution Tree");
        setSize(800, 600);
        add(new TreeComponent());
        setVisible(true);
    }

    class TreeComponent extends JComponent {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (root != null) {
                drawTree(g, root, getWidth() / 2, 50, getWidth() / 4);
            }
        }

        private void drawTree(Graphics g, OperationNode node, int x, int y, int space) {
            int childCount = node.children.size();
            if (childCount > 0) {
                int totalWidth = (childCount - 1) * space;
                int startX = x - totalWidth / 2;

                for (OperationNode child : node.children) {
                    int childIndex = node.children.indexOf(child);
                    int childX = startX + childIndex * space;
                    g.drawLine(x, y, childX, y + 100);
                    drawTree(g, child, childX, y + 100, Math.max(space / 2, 50));  // Adjust space reduction
                }
            }

            setColorForNode(g, node);
            g.fillOval(x - 15, y - 15, 30, 30);
            g.setColor(Color.BLACK);
            g.drawString(node.operationDescription, x - 10, y + 5);
        }

        private void setColorForNode(Graphics g, OperationNode node) {
            if (node instanceof JoinNode) {
                g.setColor(Color.GREEN);
            } else if (node instanceof SelectionNode) {
                g.setColor(Color.ORANGE);
            } else if (node instanceof ProjectionNode) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.BLUE);
            }
        }
    }
}
