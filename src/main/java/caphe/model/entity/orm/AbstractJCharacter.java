package caphe.model.entity.orm;

import javax.persistence.Column;

@SuppressWarnings("serial")
public abstract class AbstractJCharacter extends AbstractEntity {
    @Column(length = 2048)
    public String jcharacter;

    @Column(length = 2048)
    public String strokeSvg;

    @Column(precision = 3, columnDefinition = "tinyint")
    public Integer strokeCount;

    @Column(length = 2048)
    public String chineseVietnameseMeaning;
}
