package caphe;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.common.util.concurrent.Uninterruptibles;

import caphe.model.dao.KanjiDao;
import caphe.model.entity.impl.Kanji;
import caphe.net.ProxyRequest;
import caphe.net.Request;
import caphe.net.Response;
import caphe.net.UriUtil;
import mvc.Container;

public class Application {

    @Inject
    public KanjiDao kanjiDao;

    public void doIt() {
        for (Kanji k : kanjiDao.selectAll()) {
            System.out.println("hvdic" + k.id + " " + k.kanji);
            try {
                File f = new File("D:/kanji/hvdic/" + k.unicode + ".html");
                if (!f.exists()) {
                    Request request = new Request().withTimeout(100000);
                    Response response = request.doGet(UriUtil.createUri("https://hvdic.thivien.net/whv/" + k.kanji));
                    String d = response.content;
                    if (d.startsWith("Sorry")) {
                        break;
                    }
                    FileUtils.write(f, d, StandardCharsets.UTF_8);
                    Uninterruptibles.sleepUninterruptibly(1000, TimeUnit.MILLISECONDS);
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (e.getMessage().contains("Malformed reply from SOCKS server")) {
                    break;
                }
            }
        }
    }

    public void dot(Kanji k, File f) {
        try {
            Document doc = Jsoup.parse(f, "UTF-8");

            kanjiDao.update(k);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String... args) throws Exception {
        System.setProperty("proxySet", "true");
        System.setProperty("socksProxyVersion", "5");
        System.setProperty("socksProxyHost", "88.202.177.242");
        System.setProperty("socksProxyPort", "1090");
        Application application = Container.getInstance(Application.class);
        application.doIt();
    }
}
