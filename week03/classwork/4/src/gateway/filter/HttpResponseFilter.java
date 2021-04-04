package gateway.filter;

public interface HttpResponseFilter {

    void filter(io.netty.handler.codec.http.FullHttpResponse response);

}
