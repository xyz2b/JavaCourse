package gateway.filter;

public class HeaderHttpResponseFilter implements gateway.filter.HttpResponseFilter {
    @Override
    public void filter(io.netty.handler.codec.http.FullHttpResponse response) {
        response.headers().set("outbound", "netty4");
    }
}
