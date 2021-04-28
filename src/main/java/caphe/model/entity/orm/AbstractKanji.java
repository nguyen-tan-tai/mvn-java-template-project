package caphe.model.entity.orm;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@SuppressWarnings("serial")
public abstract class AbstractKanji extends AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Column
    public String kanji;

    @Column
    public String unicode;

    @Column
    public String amViet;

    @Column
    public Integer soNet;

    @Column
    public String boThu;

    @Column
    public String hinhThai;

    @Column
    public String netBut;

    @Column
    public Integer frequency;

    @Column
    public Integer level;

    @Column
    public String on;

    @Column
    public String kun;
}
