package gateway.outboud.netty4;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.*;

public class HttpClientInitializer extends ChannelInitializer<io.netty.channel.socket.SocketChannel> {
    @Override
    public void initChannel(io.netty.channel.socket.SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        p.addLast("request-encoder", new HttpClientCodec());
        p.addLast("response-decoder", new HttpObjectAggregator(65536));
        // Remove the following line if you don't want automatic content decompression.
        p.addLast("inflater", new HttpContentDecompressor());
        p.addLast("handler", new HttpClientHandler());
    }
}
