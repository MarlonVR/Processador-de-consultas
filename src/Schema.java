import java.util.*;

class Table {
    private String name;
    private List<Column> columns;

    public Table(String name) {
        this.name = name;
        this.columns = new ArrayList<>();
    }

    public void addColumn(Column column) {
        columns.add(column);
    }

    public String getName() {
        return name;
    }
    public List<Column> getColumns() {
        return columns;
    }
}

class Column {
    private String name;
    private String type;

    public Column(String name, String type) {
        this.name = name;
        this.type = type;
    }
    public String getName() {
        return name;
    }
    public String getType() {
        return type;
    }
}

public class Schema {
    private Map<String, Table> tables;

    public Schema() {
        tables = new HashMap<>();
        createTables();
    }

    private void createTables() {
        Table categoria = new Table("Categoria");
        categoria.addColumn(new Column("idCategoria", "INT NOT NULL"));
        categoria.addColumn(new Column("Descricao", "VARCHAR(45) NOT NULL"));
        tables.put("Categoria", categoria);

        Table produto = new Table("Produto");
        produto.addColumn(new Column("idProduto", "INT NOT NULL"));
        produto.addColumn(new Column("Nome", "VARCHAR(45) NOT NULL"));
        produto.addColumn(new Column("Descricao", "VARCHAR(200) NULL"));
        produto.addColumn(new Column("Preco", "DECIMAL(18,2) NOT NULL DEFAULT 0"));
        produto.addColumn(new Column("QuantEstoque", "DECIMAL(10,2) NOT NULL DEFAULT 0"));
        produto.addColumn(new Column("Categoria_idCategoria", "INT NOT NULL"));
        tables.put("Produto", produto);

        Table tipoCliente = new Table("TipoCliente");
        tipoCliente.addColumn(new Column("idTipoCliente", "INT NOT NULL"));
        tipoCliente.addColumn(new Column("Descricao", "VARCHAR(45) NULL"));
        tables.put("TipoCliente", tipoCliente);

        Table cliente = new Table("Cliente");
        cliente.addColumn(new Column("idCliente", "INT NOT NULL"));
        cliente.addColumn(new Column("Nome", "VARCHAR(45) NOT NULL"));
        cliente.addColumn(new Column("Email", "VARCHAR(100) NOT NULL"));
        cliente.addColumn(new Column("Nascimento", "DATETIME NULL"));
        cliente.addColumn(new Column("Senha", "VARCHAR(200) NULL"));
        cliente.addColumn(new Column("TipoCliente_idTipoCliente", "INT NOT NULL"));
        cliente.addColumn(new Column("DataRegistro", "DATETIME NOT NULL DEFAULT Now()"));
        tables.put("Cliente", cliente);

        Table tipoEndereco = new Table("TipoEndereco");
        tipoEndereco.addColumn(new Column("idTipoEndereco", "INT NOT NULL"));
        tipoEndereco.addColumn(new Column("Descricao", "VARCHAR(45) NOT NULL"));
        tables.put("TipoEndereco", tipoEndereco);

        Table endereco = new Table("Endereco");
        endereco.addColumn(new Column("idEndereco", "INT NOT NULL"));
        endereco.addColumn(new Column("EnderecoPadrao", "TINYINT NOT NULL DEFAULT 0"));
        endereco.addColumn(new Column("Logradouro", "VARCHAR(45) NULL"));
        endereco.addColumn(new Column("Numero", "VARCHAR(45) NULL"));
        endereco.addColumn(new Column("Complemento", "VARCHAR(45) NULL"));
        endereco.addColumn(new Column("Bairro", "VARCHAR(45) NULL"));
        endereco.addColumn(new Column("Cidade", "VARCHAR(45) NULL"));
        endereco.addColumn(new Column("UF", "VARCHAR(2) NULL"));
        endereco.addColumn(new Column("CEP", "VARCHAR(8) NULL"));
        endereco.addColumn(new Column("TipoEndereco_idTipoEndereco", "INT NOT NULL"));
        endereco.addColumn(new Column("Cliente_idCliente", "INT NOT NULL"));
        tables.put("Endereco", endereco);

        Table telefone = new Table("Telefone");
        telefone.addColumn(new Column("Numero", "VARCHAR(42) NOT NULL"));
        telefone.addColumn(new Column("Cliente_idCliente", "INT NOT NULL"));
        tables.put("Telefone", telefone);

        Table status = new Table("Status");
        status.addColumn(new Column("idStatus", "INT NOT NULL"));
        status.addColumn(new Column("Descricao", "VARCHAR(45) NOT NULL"));
        tables.put("Status", status);

        Table pedido = new Table("Pedido");
        pedido.addColumn(new Column("idPedido", "INT NOT NULL"));
        pedido.addColumn(new Column("Status_idStatus", "INT NOT NULL"));
        pedido.addColumn(new Column("DataPedido", "DATETIME NOT NULL DEFAULT Now()"));
        pedido.addColumn(new Column("ValorTotalPedido", "DECIMAL(18,2) NOT NULL DEFAULT 0"));
        pedido.addColumn(new Column("Cliente_idCliente", "INT NOT NULL"));
        tables.put("Pedido", pedido);

        Table pedidoHasProduto = new Table("Pedido_has_Produto");
        pedidoHasProduto.addColumn(new Column("idPedidoProduto", "INT NOT NULL AUTO_INCREMENT"));
        pedidoHasProduto.addColumn(new Column("Pedido_idPedido", "INT NOT NULL"));
        pedidoHasProduto.addColumn(new Column("Produto_idProduto", "INT NOT NULL"));
        pedidoHasProduto.addColumn(new Column("Quantidade", "DECIMAL(10,2) NOT NULL"));
        pedidoHasProduto.addColumn(new Column("PrecoUnitario", "DECIMAL(18,2) NOT NULL"));
        tables.put("Pedido_has_Produto", pedidoHasProduto);
    }

    public boolean isValidTable(String tableName) {
        return tables.containsKey(tableName);
    }

    public boolean isValidColumn(String tableName, String columnName) {
        Table table = tables.get(tableName);
        return table != null && table.getColumns().stream().anyMatch(column -> column.getName().equals(columnName));
    }

    public Map<String, Table> getTables() {
        return tables;
    }

    public Table getTable(String tableName) {
        return tables.get(tableName);
    }

    public boolean tableExists(String tableName) {
        return tables.containsKey(tableName);
    }

    public boolean attributeExists(String tableName, String attributeName) {
        Table table = tables.get(tableName);
        return table != null && table.getColumns().stream().anyMatch(column -> column.getName().equalsIgnoreCase(attributeName));
    }

}
