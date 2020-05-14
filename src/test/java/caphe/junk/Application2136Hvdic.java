package caphe.junk;

import java.io.File;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;

import caphe.model.dao.KanjiDao;
import caphe.model.entity.impl.Kanji;
import framework.ContainerRegistry;
import framework.util.JsonUtils;

public class Application2136Hvdic {

    @Inject
    public KanjiDao kanjiDao;

    public static void main(String... args) throws Exception {
        ContainerRegistry.init(new Properties());
        Application2136Hvdic app = ContainerRegistry.selfRegister(Application2136Hvdic.class);
        app.go();
    }

    private void go() throws Exception {
//        List<Kanji> kanjis = kanjiDao.selectAll();
        List<String> f = new ArrayList<>();
        List<String> kanjis = Arrays.asList("乙 ", "人 ", "入 ", "八 ", "刀 ", "亡 ", "卜 ", "卩 ", "小 ", "尢 ", "巛 ", "已 ",
                "彐 ", "心 ", "戸 ", "手 ", "攵 ", "无 ", "月 ", "歹 ", "毋 ", "水 ", "火 ", "爪 ", "爿 ", "牛 ", "犬 ", "王 ", "用 ",
                "疋 ", "示 ", "竹 ", "糸 ", "罒 ", "羊 ", "耂 ", "聿 ", "肉 ", "艹 ", "衣 ", "西 ", "言 ", "走 ", "足 ", "⻌ ", "邑 ",
                "金 ", "長 ", "阜 ", "雨 ", "青 ", "面 ", "風 ", "食 ", "高 ", "麦 ", "黄 ", "黒 ", "歯 ", "竜 ", "亀 ");
        for (String ks : kanjis) {
            Kanji k = new Kanji();
            k.kanji = ks.trim();
            try {
                int code = Character.codePointAt(k.kanji, 0);
                File file = new File("D:/kanji/hvdic.thivien.net/0" + Integer.toHexString(code) + ".html");
                if (!file.exists()) {
                    String hvdic = doIt(k);
                    FileUtils.write(file, hvdic, StandardCharsets.UTF_8);
                }
//            try {
//                saveIt(k, FileUtils.readFileToString(file, StandardCharsets.UTF_8));
//            } catch (Exception e) {
//                System.out.println(k.kanji);
//                System.out.println(file.getName());
//            }
            } catch (Exception e) {
                f.add(k.kanji);
                System.out.println(k.kanji + "   faild" + e.getMessage());
            }
        }
        System.out.println(JsonUtils.toPrettyJsonString(f));
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
        HttpGet httpGet = new HttpGet(new URI("https://hvdic.thivien.net/whv/" + kanji.kanji));
        System.out.println(httpGet);
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create().setUserAgent(
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
        try (CloseableHttpClient httpClient = httpClientBuilder.build()) {
            try (CloseableHttpResponse httpResponse = httpClient.execute(httpGet)) {
                return EntityUtils.toString(httpResponse.getEntity(), StandardCharsets.UTF_8);
            }
        }
    }
}
