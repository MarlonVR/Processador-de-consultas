class QueryTree {
    private OperationNode root;

    public void setRootNode(OperationNode node) {
        root = node;
    }

    public void execute() {
        if (root != null) root.execute();
    }

    public OperationNode getRoot(){
        return root;
    }

    public void addNode(OperationNode parentNode, OperationNode newNode) {
        if (parentNode != null) {
            parentNode.addChild(newNode);
        } else {
            if (root == null) {
                setRootNode(newNode); // Se não há raiz, estabelece como raiz
            } else {
                root.addChild(newNode); // Caso contrário, adiciona ao nó raiz se não há pai especificado
            }
        }
    }


}
