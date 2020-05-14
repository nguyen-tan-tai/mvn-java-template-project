package caphe.model.dao;

import caphe.model.entity.impl.Kanji;
import framework.model.Dao;

public class KanjiDao extends Dao<Kanji> {

    public KanjiDao() {
        super(Kanji.class);
    }

    public Kanji selectByCharacter(String kanji) {
        return this.selectFirstBySql("select * from kanji where kanji = '" + kanji + "'",
                this.entityManager.getEntityClass());
    }
}
