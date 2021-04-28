package caphe.net;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;

import lombok.NonNull;

public class UriUtil {
    public static URI createUri(String uri, NameValuePair... parameters) {
        if (parameters == null) {
            return createUri(uri);
        }
        try {
            return new URIBuilder(uri).addParameters(Arrays.asList(parameters)).build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static URI createUri(String uri, List<NameValuePair> parameters) {
        try {
            return new URIBuilder(uri).addParameters(parameters).build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static URI addParams(URI uri, List<NameValuePair> parameters) {
        try {
            return new URIBuilder(uri).addParameters(parameters).build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getQueryFirstValueByName(@NonNull URI uri, @NonNull String name) {
        for (NameValuePair p : new URIBuilder(uri).getQueryParams()) {
            if (name.equals(p.getName())) {
                return p.getValue();
            }
        }
        return null;
    }

    public static URI replaceOrAddParam(URI uri, NameValuePair modifiedParam) {
        if (uri == null || modifiedParam == null) {
            return uri;
        }
        try {
            List<NameValuePair> modifiedParams = new ArrayList<>();
            boolean isReplaced = false;
            for (NameValuePair oldParam : new URIBuilder(uri).getQueryParams()) {
                if (oldParam.getName().equals(modifiedParam.getName())) {
                    modifiedParams.add(modifiedParam);
                    isReplaced = true;
                } else {
                    modifiedParams.add(oldParam);
                }
            }
            if (!isReplaced) {
                modifiedParams.add(modifiedParam);
            }
            return new URIBuilder(uri).setParameters(modifiedParams).build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static URI removeParamByName(URI uri, String paramName) {
        if (uri == null || paramName == null) {
            return uri;
        }
        try {
            List<NameValuePair> modifiedParams = new ArrayList<>();
            for (NameValuePair oldParam : new URIBuilder(uri).getQueryParams()) {
                if (!oldParam.getName().equals(paramName)) {
                    modifiedParams.add(oldParam);
                }
            }
            return new URIBuilder(uri).setParameters(modifiedParams).build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
