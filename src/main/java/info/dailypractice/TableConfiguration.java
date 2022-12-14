package info.dailypractice;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TableConfiguration {
    private String tableName;
    private List<Map<String, String>> fields;

    private List<String> primaryKey;

    public TableConfiguration() {
    }


    public String getTableName() {
        return tableName;
    }

    public List<Map<String, String>> getFields() {
        return fields;
    }

    public List<String> getPrimaryKey() {
        return primaryKey;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setFields(List<Map<String, String>> fields) {
        this.fields = fields;
    }

    public void setPrimaryKey(List<String> primaryKey) {
        this.primaryKey = primaryKey;
    }

    @Override
    public String toString() {
        return "TableConfiguration{" +
                "tableName='" + tableName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableConfiguration that = (TableConfiguration) o;
        return tableName.equals(that.tableName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableName);
    }
}
