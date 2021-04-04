package gateway.inboud;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpInboundInitializer extends ChannelInitializer<io.netty.channel.socket.SocketChannel> {
    private java.util.List<String> proxyServer;

    public HttpInboundInitializer(java.util.List<String> proxyServer) {
        this.proxyServer = proxyServer;
    }

    @Override
    public void initChannel(io.netty.channel.socket.SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        p.addLast(new HttpServerCodec());
        p.addLast(new HttpObjectAggregator(1024 * 1024));
        p.addLast(new HttpInboundHandler(this.proxyServer));
    }
}
