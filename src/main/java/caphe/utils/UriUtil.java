package caphe.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;

import lombok.NonNull;

public class UriUtil {

  /**
   * Create an URI from uri string and optional parameters
   *
   * @param uri
   * @param parameters
   * @return URI
   */
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

  /**
   * Create an URI from uri string and parameter list.
   *
   * @param uri
   * @param parameters
   * @return URI
   */
  public static URI createUri(String uri, List<NameValuePair> parameters) {
    try {
      return new URIBuilder(uri).addParameters(parameters).build();
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Add more param for uri exists.<br>
   * Note: Don't check coincides param available.
   *
   * @param uri
   * @param parameters
   * @return URI
   */
  public static URI addParams(URI uri, List<NameValuePair> parameters) {
    try {
      return new URIBuilder(uri).addParameters(parameters).build();
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Return value of specific query parameter<br>
   * Return null if there is no such parameter
   *
   * @param uri
   * @param name
   * @return String
   */
  public static String getQueryFirstValueByName(@NonNull URI uri, @NonNull String name) {
    for (NameValuePair p : new URIBuilder(uri).getQueryParams()) {
      if (name.equals(p.getName())) {
        return p.getValue();
      }
    }
    return null;
  }
}
