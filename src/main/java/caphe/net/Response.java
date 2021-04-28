package caphe.net;

import java.util.List;

import org.apache.http.Header;

public class Response {
    public Integer status;
    public List<Header> headers;
    public String content;

    public Response(Integer status, List<Header> headers, String content) {
        this.status = status;
        this.headers = headers;
        this.content = content;
    }
}
