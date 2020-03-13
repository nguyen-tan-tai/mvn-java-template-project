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
            try (PreparedStatement pstm = connection.prepareStatement(this.entityManager.getInsertStatementQuery(),
                    Statement.RETURN_GENERATED_KEYS)) {
                pstm.executeUpdate();
                try (ResultSet rs = pstm.getGeneratedKeys()) {
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
            try (PreparedStatement pstm = connection
                    .prepareStatement(this.entityManager.getUpdateStatementQuery(entity))) {
                return pstm.executeUpdate();
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

    public void condasf(Field field, T entity) {
        if (field.getType().equals(Boolean.class)) {
        } else if (field.getType().equals(Short.class)) {
        } else if (field.getType().equals(Integer.class)) {
        } else if (field.getType().equals(Long.class)) {
        } else if (field.getType().equals(BigDecimal.class)) {
        } else if (field.getType().equals(LocalDate.class)) {
        } else if (field.getType().equals(LocalDateTime.class)) {
        } else {
        }
    }

    protected <U> U convertResultSetToObject(ResultSet rs, Class<? extends U> clazz) {
        try {
            U object = clazz.newInstance();
            for (Field field : clazz.getFields()) {
                if (field.getType().equals(Boolean.class)) {
                    field.set(object, rs.getBoolean(field.getName()));
                } else if (field.getType().equals(Short.class)) {
                    field.set(object, rs.getShort(field.getName()));
                } else if (field.getType().equals(Integer.class)) {
                    field.set(object, rs.getInt(field.getName()));
                } else if (field.getType().equals(Long.class)) {
                    field.set(object, rs.getLong(field.getName()));
                } else if (field.getType().equals(BigDecimal.class)) {
                    field.set(object, rs.getBigDecimal(field.getName()));
                } else if (field.getType().equals(LocalDate.class)) {
                    field.set(object, Instant.ofEpochMilli(rs.getDate(field.getName()).getTime())
                            .atZone(ZoneId.of("UTC")).toLocalDate());
                } else if (field.getType().equals(LocalDateTime.class)) {
                    field.set(object, Instant.ofEpochMilli(rs.getDate(field.getName()).getTime())
                            .atZone(ZoneId.of("UTC")).toLocalDateTime());
                } else {
                    field.set(object, rs.getString(field.getName()));
                }
            }
            return object;
        } catch (InstantiationException | IllegalAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
