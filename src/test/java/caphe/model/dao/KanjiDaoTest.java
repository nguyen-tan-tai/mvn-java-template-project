package caphe.model.dao;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;

import caphe.model.entity.impl.Kanji;
import framework.AbstractTest;
import framework.TestRunner;

@RunWith(TestRunner.class)
public class KanjiDaoTest extends AbstractTest {

    @Inject
    public KanjiDao JCharacterDao;

    @Test
    public void testInsert() {
        Kanji jcharacter = new Kanji();
        jcharacter.kanji = "hoge";
        jcharacter.soNet = 10;
        jcharacter.amHanViet = "meaning";
        JCharacterDao.insert(jcharacter);
    }

    @Test
    public void testUpdate() {
        Kanji jcharacter = new Kanji();
        jcharacter.id = 1L;
        jcharacter.kanji = "fuga";
        jcharacter.soNet = 20;
        jcharacter.amHanViet = "no";
        JCharacterDao.update(jcharacter);
    }
}
