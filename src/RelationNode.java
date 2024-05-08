class RelationNode extends OperationNode {
    String tableName;

    public RelationNode(String tableName) {
        super(tableName);
        this.tableName = tableName;
    }

    @Override
    void execute() {
        System.out.println("Accessing data from table: " + tableName);
        for (OperationNode child : children) {
            child.execute();
        }
    }
}
