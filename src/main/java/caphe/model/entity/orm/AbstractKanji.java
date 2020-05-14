package caphe.model.entity.orm;

import javax.persistence.Column;

@SuppressWarnings("serial")
public abstract class AbstractKanji extends AbstractEntity {
    @Column(length = 50)
    public String kanji;

    @Column(precision = 4, columnDefinition = "tinyint")
    public Integer phanLoai;

    @Column(precision = 6, columnDefinition = "smallint")
    public Integer thuTu;

    @Column(precision = 4, columnDefinition = "tinyint")
    public Integer doKho;

    @Column(precision = 4, columnDefinition = "tinyint")
    public Integer soNet;

    @Column(precision = 6, columnDefinition = "smallint")
    public Integer frequency;

    @Column(length = 50)
    public String bienThe;

    @Column(length = 50)
    public String radical;

    @Column(length = 1024)
    public String amHanViet;

    @Column(length = 1024)
    public String nghia;

    @Column(length = 1024)
    public String mean;

    @Column(length = 1024)
    public String on;

    @Column(length = 1024)
    public String kun;
}
