package framework.model;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.CaseFormat;

import lombok.Getter;

public class EntityManager<T extends Entity> {
    @Getter
    private Class<? extends T> entityClass;

    public EntityManager(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public String getTableName() {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this.entityClass.getSimpleName());
    }

    public String convertFieldNameToColumnName(Field field) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());
    }

    public String getInsertStatementQuery() {
        List<String> columns = Arrays.asList(this.entityClass.getFields()).stream()
                .filter(f -> f.getAnnotation(GeneratedValue.class) == null)
                .map(field -> "`" + convertFieldNameToColumnName(field) + "`").collect(Collectors.toList());
        return "INSERT INTO `" + this.getTableName() + "` (" + StringUtils.join(columns, ", ") + ") VALUES ("
                + columns.stream().map(f -> "?").collect(Collectors.joining(", ")) + ");";
    }

    public String getUpdateStatementQuery(T entity) {
        return "UPDATE `" + this.getTableName() + "` " + this.getPreparedUpdateString(entity) + " WHERE `"
                + this.getIdColumnName() + "` = ?";
    }

    public String getIdColumnName() {
        for (Field field : this.entityClass.getFields()) {
            if (field.getAnnotation(Id.class) != null) {
                return field.getName();
            }
        }
        throw new RuntimeException("Entity has no Id column");
    }

    private String getPreparedUpdateString(T entity) {
        return Arrays.asList(this.entityClass.getFields()).stream()
                .map(field -> "SET `" + convertFieldNameToColumnName(field) + "` = ?")
                .collect(Collectors.joining(", "));
    }

    public String getSelectQuery() {
        return "select * from " + this.getTableName();
    }
}
