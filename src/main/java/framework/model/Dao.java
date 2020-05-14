package framework.model;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.logging.log4j.Logger;

import com.google.common.base.CaseFormat;

public abstract class Dao<T extends Entity> {

    @Inject
    public Logger logger;

    @Inject
    public DataSource dataSource;

    public EntityManager<T> entityManager;

    public Dao(Class<T> entityClass) {
        this.entityManager = new EntityManager<>(entityClass);
    }

    public Long insert(T entity) {
        try (Connection connection = this.dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection
                    .prepareStatement(this.entityManager.getInsertStatementQuery(), Statement.RETURN_GENERATED_KEYS)) {
                this.entityManager.preparedInsert(preparedStatement, entity);
                logger.debug(preparedStatement.toString());
                preparedStatement.executeUpdate();
                try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                    rs.next();
                    return rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int update(T entity) {
        try (Connection connection = this.dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection
                    .prepareStatement(this.entityManager.getUpdateStatementQuery(entity))) {
                this.entityManager.preparedUpdate(preparedStatement, entity);
                logger.debug(preparedStatement.toString());
                return preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public T selectById(Long id) {
        if (id == null) {
            return null;
        }
        return this.selectFirstBySql(this.entityManager.getSelectQuery(), this.entityManager.getEntityClass());
    }

    public List<T> selectAll() {
        return this.selectBySql(this.entityManager.getSelectQuery(), this.entityManager.getEntityClass());
    }

    public <U> List<U> selectBySql(String sql, Class<? extends U> clazz) {
        logger.debug(sql);
        try (Connection connection = this.dataSource.getConnection()) {
            try (Statement Statement = connection.createStatement()) {
                try (ResultSet rs = Statement.executeQuery(sql)) {
                    List<U> list = new ArrayList<>();
                    while (rs.next()) {
                        list.add(this.convertResultSetToObject(rs, clazz));
                    }
                    return list;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public <U> U selectFirstBySql(String sql, Class<? extends U> clazz) {
        logger.debug(sql);
        try (Connection connection = this.dataSource.getConnection()) {
            try (Statement Statement = connection.createStatement()) {
                try (ResultSet rs = Statement.executeQuery(sql)) {
                    while (rs.next()) {
                        return this.convertResultSetToObject(rs, clazz);
                    }
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected <U> U convertResultSetToObject(ResultSet rs, Class<? extends U> clazz) {
        try {
            U object = clazz.newInstance();
            for (Field field : clazz.getFields()) {
                String fieldName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());
                if (field.getType().equals(Boolean.class)) {
                    field.set(object, rs.getBoolean(fieldName));
                } else if (field.getType().equals(Short.class)) {
                    field.set(object, rs.getShort(fieldName));
                } else if (field.getType().equals(Integer.class)) {
                    field.set(object, rs.getInt(fieldName));
                } else if (field.getType().equals(Long.class)) {
                    field.set(object, rs.getLong(fieldName));
                } else if (field.getType().equals(BigDecimal.class)) {
                    field.set(object, rs.getBigDecimal(fieldName));
                } else if (field.getType().equals(LocalDate.class)) {
                    field.set(object, Instant.ofEpochMilli(rs.getDate(fieldName).getTime()).atZone(ZoneId.of("UTC"))
                            .toLocalDate());
                } else if (field.getType().equals(LocalDateTime.class)) {
                    field.set(object, Instant.ofEpochMilli(rs.getDate(fieldName).getTime()).atZone(ZoneId.of("UTC"))
                            .toLocalDateTime());
                } else {
                    field.set(object, rs.getString(fieldName));
                }
            }
            return object;
        } catch (InstantiationException | IllegalAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
