package caphe.junk;

import java.io.File;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Properties;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;

import caphe.model.dao.KanjiDao;
import caphe.model.entity.impl.Kanji;
import framework.ContainerRegistry;
import framework.util.JsonUtils;

public class Application2136Mazii {

    @Inject
    public KanjiDao kanjiDao;

    public static void main(String... args) throws Exception {
        ContainerRegistry.init(new Properties());
        Application2136Mazii app = ContainerRegistry.selfRegister(Application2136Mazii.class);
        app.go();
    }

    private void go() throws Exception {
        List<Kanji> kanjis = kanjiDao.selectAll();
        for (Kanji k : kanjis) {
            if (k.phanLoai == 1 || k.nghia != null) {
                continue;
            }
            int code = Character.codePointAt(k.kanji, 0);
            File file = new File("D:/kanji/mazii/0" + Integer.toHexString(code) + ".json");
            if (!file.exists()) {
                String mazii = doIt(k);
                FileUtils.write(file, mazii, StandardCharsets.UTF_8);
            }
            try {
                saveIt(k, FileUtils.readFileToString(file, StandardCharsets.UTF_8));
            } catch (Exception e) {
                System.out.println(k.kanji);
                System.out.println(file.getName());
            }
        }
    }

    private void saveIt(Kanji k, String mazii) {
        JsonNode n = JsonUtils.createJsonNodeJsonString(mazii).get("results").get(0);
        k.doKho = n.get("level").asInt();
        k.frequency = n.get("freq").asInt();
        k.kun = n.get("kun").asText();
        k.on = n.get("on").asText();
        k.amHanViet = n.get("mean").asText();
        k.nghia = n.get("detail").asText();
        k.soNet = n.get("stroke_count").asInt();
        kanjiDao.update(k);
    }

    private String doIt(Kanji kanji) throws Exception {
        HttpPost httpPost = new HttpPost(new URI("https://mazii.net/api/search"));
        httpPost.setHeader("content-type", "application/json");
        httpPost.setHeader("accept", "application/json");
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create().setUserAgent(
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
        httpPost.setEntity(
                new StringEntity("{\"dict\":\"javi\",\"type\":\"kanji\",\"query\":\"" + kanji.kanji + "\",\"page\":1}",
                        StandardCharsets.UTF_8));
        try (CloseableHttpClient httpClient = httpClientBuilder.build()) {
            try (CloseableHttpResponse httpResponse = httpClient.execute(httpPost)) {
                return EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
            }
        }
    }
}
