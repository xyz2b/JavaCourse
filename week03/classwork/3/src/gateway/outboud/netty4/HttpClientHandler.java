package gateway.outboud.netty4;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.AttributeKey;

public class HttpClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        messageReceived(ctx, msg);
    }

    private void messageReceived(ChannelHandlerContext ctx, Object msg) {
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
