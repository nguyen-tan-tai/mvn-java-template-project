package caphe.junk;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.inject.Inject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import caphe.model.dao.KanjiDao;
import caphe.model.entity.impl.Kanji;
import framework.ContainerRegistry;

public class Application214Radical {

    @Inject
    public KanjiDao jCharacterDao;

    public static void main(String... args) throws Exception {
        ContainerRegistry.init(new Properties());
        Application214Radical app = ContainerRegistry.selfRegister(Application214Radical.class);
        app.go();
    }

    private void go() throws Exception {
//      URL url = new URL("https://toihoctiengtrung.com/214-bo-thu-tieng-trung");
        Document document = Jsoup.parse(new File("D:/hoge.html"), StandardCharsets.UTF_8.name());
        Elements elements = document.select("#post-2458 .entry-content .bo-thu");
        Elements elements2 = document.select("#post-2458 .entry-content .bothu");
        for (int i = 0; i < elements.size(); i++) {
            List<String> radicals = radical(elements.get(i).text());
            for (String radical : radicals) {
                doIt(radical.trim(), elements2.get(i));
            }
        }
    }

    private void doIt(String radical, Element element) throws Exception {
        String phienam2 = element.select(".phienam2").get(1).text();
        String ynghia2 = element.select(".ynghia2").text().trim();
        if (ynghia2.endsWith(",")) {
            ynghia2 = ynghia2.substring(0, ynghia2.length() - 1);
        }
        Kanji c = jCharacterDao.selectByCharacter(radical);
        if (c == null) {
            System.out.println(radical);
            return;
        }
        c.amHanViet = phienam2;
        c.nghia = ynghia2;
        jCharacterDao.update(c);
    }

    public List<String> radical(String radicals) {
        String radical = radicals.replace(")", "").split("\\.")[1];
        if (!radical.contains("(")) {
            return Arrays.asList(radical.trim());
        }
        String[] r = radical.split("\\(");
        List<String> list = new ArrayList<>();
        list.add(r[0]);
        list.addAll(Arrays.asList(r[1].split("-")));
        return list;
    }
}
