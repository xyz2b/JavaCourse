package outbound.netty4;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.HttpPostRequestEncoder;
import io.netty.util.AttributeKey;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;


public class HttpOutboundClient {
    public static void main(String[] args) throws InterruptedException, HttpPostRequestEncoder.ErrorDataEncoderException, URISyntaxException, UnsupportedEncodingException {
        String url = "http://127.0.0.1:8801/test/";
        URI uri = new URI(url);
        String msg = "Are you ok?";

        // 构建http请求
        DefaultFullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET,
                uri.toASCIIString(), Unpooled.wrappedBuffer(msg.getBytes("UTF-8")));

        request.headers().set(HttpHeaders.Names.HOST, "127.0.0.1:8801");
        request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        request.headers().set(HttpHeaders.Names.CONTENT_LENGTH, request.content().readableBytes());

        FullHttpResponse response = new HttpOutboundClient().run(url, request);
        System.out.println(response);
    }

    public FullHttpResponse run(String url, FullHttpRequest request) throws InterruptedException, URISyntaxException {
        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            throw e;
        }

        String scheme = uri.getScheme() == null ? "http" : uri.getScheme();
        String host = uri.getHost() == null ? "localhost" : uri.getHost();
        int port = uri.getPort();
        if (port == -1) {
            if ("http".equalsIgnoreCase(scheme)) {
                port = 80;
            }
        }

        if (!"http".equalsIgnoreCase(scheme)) {
            System.err.println("Only HTTP(S) is supported.");
        }

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new HttpOutboundInitializer(request));

            Channel ch = b.connect(host, port).sync().channel();
            ch.closeFuture().sync();

            AttributeKey<FullHttpResponse> key = AttributeKey.valueOf("BackendResponseData");
            return ch.attr(key).get();
        } finally {
            group.shutdownGracefully();
        }
    }
}
