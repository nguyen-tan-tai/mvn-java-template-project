package caphe.junk;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import caphe.model.dao.KanjiDao;
import caphe.model.entity.impl.Kanji;
import framework.ContainerRegistry;

public class Application2136Kanji2 {

    @Inject
    public KanjiDao kanjiDao;

    public static void main(String... args) throws Exception {
        ContainerRegistry.init(new Properties());
        Application2136Kanji2 app = ContainerRegistry.selfRegister(Application2136Kanji2.class);
        app.go();
    }

    private void go() throws Exception {
        Document document = Jsoup.parse(FileUtils.readFileToString(new File("D:/haha.html"), StandardCharsets.UTF_8));
        Elements elements = document.select("tbody tr");
        for (int i = 0; i < elements.size(); i++) {
            doIt(elements.get(i));
        }
    }

    private void doIt(Element element) {
        Elements tds = element.select("td");
        Kanji k = new Kanji();
        k.phanLoai = 1;
        k.thuTu = Integer.parseInt(tds.get(0).text());
        String kanji = tds.get(1).text();
        if (!kanji.contains("(")) {
            k.kanji = kanji;
        } else {
            k.kanji = kanji.split("\\(")[0];
            k.bienThe = kanji.split("\\(")[1].replace(")", "");
        }
        k.soNet = Integer.parseInt(tds.get(2).text());
        k.mean = tds.get(3).text();
        try {
            k.frequency = Integer.parseInt(tds.get(4).text().replace(",", ""));
        } catch (Exception e) {
            System.out.println(k);
        }
        kanjiDao.insert(k);
    }
}
