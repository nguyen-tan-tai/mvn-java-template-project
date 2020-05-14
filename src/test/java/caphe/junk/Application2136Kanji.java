package caphe.junk;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import caphe.model.dao.KanjiDao;
import caphe.model.entity.impl.Kanji;
import framework.ContainerRegistry;

public class Application2136Kanji {

    @Inject
    public KanjiDao kanjiDao;

    public static void main(String... args) throws Exception {
        ContainerRegistry.init(new Properties());
        Application2136Kanji app = ContainerRegistry.selfRegister(Application2136Kanji.class);
        app.go();
    }

    private void go() throws Exception {
        Document document = Jsoup.parse(new File("D:/kanji/fuga.html"), StandardCharsets.UTF_8.name());
        Elements elements = document.select("tbody tr");
        for (int i = 1; i < elements.size(); i++) {
            doIt(elements.get(i));
        }
    }

    private void doIt(Element element) {
        Elements tds = element.select("td");
        String kanji = tds.get(1).text();
        Kanji k = new Kanji();
        k.phanLoai = 2;
        k.thuTu = Integer.parseInt(tds.get(0).text());
        k.kanji = kanji;
        if (!StringUtils.isAllBlank(tds.get(2).text())) {
            k.bienThe = tds.get(2).text();
        }
        k.radical = tds.get(3).text();
        k.soNet = Integer.parseInt(tds.get(4).text());
        try {
            k.doKho = Integer.parseInt(tds.get(5).text());
        } catch (Exception e) {
            k.doKho = 7;
        }
        k.mean = tds.get(7).text();
        kanjiDao.insert(k);
    }
}
