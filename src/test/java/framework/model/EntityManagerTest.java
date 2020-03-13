package framework.model;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.junit.Test;

public class EntityManagerTest {
    @Test
    public void test() {
        EntityManager<TestEntity> entityManager = new EntityManager<>(TestEntity.class);
        assertEquals(entityManager.getTableName(), "test_entity");
        TestEntity testEntity = new TestEntity();
        testEntity.id = 2000L;
        testEntity.stringType = "string";
        testEntity.shortType = 0;
        testEntity.integerType = 1000;
        testEntity.integerType = 1000;
        testEntity.localDateType = LocalDate.now();
        testEntity.localDateTimeType = LocalDateTime.now();
        assertEquals(entityManager.getInsertStatementQuery(),
                "INSERT INTO `test_entity` (`string_type`, `short_type`, `integer_type`, `local_date_type`, `local_date_time_type`) VALUES (?, ?, ?, ?, ?);");

        assertEquals(entityManager.getUpdateStatementQuery(testEntity),
                "UPDATE `test_entity` SET `id` = ?, SET `string_type` = ?, SET `short_type` = ?, SET `integer_type` = ?, SET `local_date_type` = ?, SET `local_date_time_type` = ? WHERE `id` = ?");
    }

    public static class TestEntity extends Entity {
        private static final long serialVersionUID = 1L;
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(precision = 11)
        public Long id;
        @Column(length = 1024)
        public String stringType;
        public Short shortType;
        public Integer integerType;
        public LocalDate localDateType;
        public LocalDateTime localDateTimeType;
    }
}
