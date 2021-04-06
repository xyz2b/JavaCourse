package outbound.netty4;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.AttributeKey;

public class HttpOutboundHandler extends ChannelInboundHandlerAdapter {
    private FullHttpRequest request;

    public HttpOutboundHandler(FullHttpRequest request) {
        this.request = request;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(request);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FullHttpResponse) {
            FullHttpResponse content = (FullHttpResponse) msg;
            AttributeKey<FullHttpResponse> key = AttributeKey.valueOf("BackendResponseData");
            ctx.channel().attr(key).set(content);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
