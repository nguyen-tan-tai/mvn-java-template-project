package caphe.model.dao;

import javax.inject.Inject;

import org.junit.Test;

import caphe.model.entity.impl.Kanji;

public class KanjiDaoTest {

    @Inject
    public KanjiDao JCharacterDao;

    @Test
    public void testInsert() {
        Kanji jcharacter = new Kanji();
        jcharacter.kanji = "hoge";
        jcharacter.soNet = 10;
        JCharacterDao.insert(jcharacter);
    }

    @Test
    public void testUpdate() {
        Kanji jcharacter = new Kanji();
        jcharacter.id = 1L;
        jcharacter.kanji = "fuga";
        jcharacter.soNet = 20;
        JCharacterDao.update(jcharacter);
    }
}
