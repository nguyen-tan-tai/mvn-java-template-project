package caphe.model.dao;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;

import caphe.model.entity.impl.JCharacter;
import framework.AbstractTest;
import framework.TestRunner;
import framework.util.JsonUtils;

@RunWith(TestRunner.class)
public class JCharacterDaoTest extends AbstractTest {
    @Inject
    public JCharacterDao JCharacterDao;

    @Test
    public void test() {
        System.out.println(JsonUtils.toPrettyJsonString(JCharacterDao.selectAll()));
    }

    @Test
    public void testInsert() {
        JCharacter jcharacter = new JCharacter();
        jcharacter.jcharacter = "hoge";
        JCharacterDao.insert(jcharacter);
    }

}
