package gateway.filter;

public class HeaderHttpRequestFilter implements gateway.filter.HttpRequestFilter {

    @Override
    public void filter(io.netty.handler.codec.http.FullHttpRequest fullRequest, io.netty.channel.ChannelHandlerContext ctx) {
        fullRequest.headers().set("test", "test1");
    }
}
