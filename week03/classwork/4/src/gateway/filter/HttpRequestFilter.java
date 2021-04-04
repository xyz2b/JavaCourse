package gateway.filter;

public interface HttpRequestFilter {
    
    void filter(io.netty.handler.codec.http.FullHttpRequest fullRequest, io.netty.channel.ChannelHandlerContext ctx);
    
}
