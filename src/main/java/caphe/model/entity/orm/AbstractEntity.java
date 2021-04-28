package caphe.model.entity.orm;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import mvc.model.Entity;

@SuppressWarnings("serial")
public abstract class AbstractEntity extends Entity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(precision = 19)
    public Long id;
}
