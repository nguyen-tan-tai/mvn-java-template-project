package framework.model;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.CaseFormat;

import framework.util.DateUtils;
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

    public List<Field> getNonAutoIncrementField() {
        return Arrays.asList(this.entityClass.getFields()).stream()
                .filter(f -> f.getAnnotation(GeneratedValue.class) == null).collect(Collectors.toList());
    }

    public String getInsertStatementQuery() {
        List<String> columns = this.getNonAutoIncrementField().stream()
                .map(field -> "`" + convertFieldNameToColumnName(field) + "`").collect(Collectors.toList());
        return "INSERT INTO `" + this.getTableName() + "` (" + StringUtils.join(columns, ", ") + ") VALUES ("
                + columns.stream().map(f -> "?").collect(Collectors.joining(", ")) + ");";
    }

    public String getUpdateStatementQuery(T entity) {
        return "UPDATE `" + this.getTableName() + "` SET " + this.getPreparedUpdateString(entity) + " WHERE `"
                + this.getIdField().getName() + "` = ?";
    }

    public Field getIdField() {
        for (Field field : this.entityClass.getFields()) {
            if (field.getAnnotation(Id.class) != null) {
                return field;
            }
        }
        throw new RuntimeException("Entity has no Id column");
    }

    public String getPreparedUpdateString(T entity) {
        return this.getNonAutoIncrementField().stream()
                .map(field -> "`" + convertFieldNameToColumnName(field) + "` = ?").collect(Collectors.joining(", "));
    }

    public String getSelectQuery() {
        return "select * from " + this.getTableName();
    }

    public void preparedInsert(PreparedStatement preparedStatement, T entity) {
        List<Field> fields = this.getNonAutoIncrementField();
        int parameterSize = fields.size();
        for (int i = 1; i <= parameterSize; i++) {
            prepareStatement(preparedStatement, fields.get(i - 1), entity, i);
        }
    }

    public void preparedUpdate(PreparedStatement preparedStatement, T entity) {
        List<Field> fields = this.getNonAutoIncrementField();
        int parameterSize = fields.size();
        for (int i = 1; i <= parameterSize; i++) {
            prepareStatement(preparedStatement, fields.get(i - 1), entity, i);
        }
        prepareStatement(preparedStatement, this.getIdField(), entity, parameterSize + 1);
    }

    public void prepareStatement(PreparedStatement preparedStatement, Field field, T entity, int index) {
        try {
            Object value = field.get(entity);
            if (value == null) {
                preparedStatement.setObject(index, null);
            } else if (field.getType().equals(Boolean.class)) {
                preparedStatement.setBoolean(index, (Boolean) value);
            } else if (field.getType().equals(Short.class)) {
                preparedStatement.setShort(index, (Short) value);
            } else if (field.getType().equals(Integer.class)) {
                preparedStatement.setInt(index, (Integer) value);
            } else if (field.getType().equals(Long.class)) {
                preparedStatement.setLong(index, (Long) value);
            } else if (field.getType().equals(BigDecimal.class)) {
                preparedStatement.setBigDecimal(index, BigDecimal.valueOf(Double.parseDouble((String) value)));
            } else if (field.getType().equals(LocalDate.class)) {
                preparedStatement.setString(index, DateUtils.toLocalDate((String) value));
            } else if (field.getType().equals(LocalDateTime.class)) {
                preparedStatement.setString(index, DateUtils.toLocalDateTime((String) value));
            } else {
                preparedStatement.setString(index, (String) value);
            }
        } catch (SQLException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
