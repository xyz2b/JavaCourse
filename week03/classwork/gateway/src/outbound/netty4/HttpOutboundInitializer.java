package outbound.netty4;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpContentDecompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;

public class HttpOutboundInitializer extends ChannelInitializer<SocketChannel> {

    private FullHttpRequest request;

    public HttpOutboundInitializer(FullHttpRequest request) {
        this.request = request;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        p.addLast(new HttpClientCodec());
        p.addLast(new HttpObjectAggregator(65536));
        // Remove the following line if you don't want automatic content decompression.
        p.addLast("inflater", new HttpContentDecompressor());
        p.addLast("handler", new HttpOutboundHandler(request));
    }
}
