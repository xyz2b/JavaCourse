package inboud;

import filter.HeaderHttpRequestFilter;
import filter.HeaderHttpResponseFilter;
import filter.HttpRequestFilter;
import filter.HttpResponseFilter;
import outbound.netty4.HttpOutboundClient;
import router.HttpEndpointRouter;
import router.RandomHttpEndpointRouter;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

import java.util.List;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter {
    private final List<String> proxyServer;
    private HttpRequestFilter requestFilter = new HeaderHttpRequestFilter();
    private HttpResponseFilter responseFilter = new HeaderHttpResponseFilter();
    private HttpEndpointRouter router = new RandomHttpEndpointRouter();
    private HttpOutboundClient handler = new HttpOutboundClient();

    public HttpInboundHandler(List<String> proxyServer) {
        this.proxyServer = proxyServer;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
            String uri = fullHttpRequest.uri();

            if(uri.contains("/test")) {
                handlerTest(fullHttpRequest, ctx);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            ReferenceCountUtil.release(msg);
        }
    }

    private void handlerTest(FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx) {
        FullHttpResponse response = null;

        try {
            // 请求过滤器
            requestFilter.filter(fullHttpRequest, ctx);

            // 路由
            String backendServer = router.route(this.proxyServer);
            String url = backendServer + fullHttpRequest.uri();

            // 重写uri
            fullHttpRequest.setUri("/test/test");

            response = handler.run(url, fullHttpRequest);

            if (response == null) {
                throw new Exception("request backend response is null");
            }

            // 响应过滤器
            responseFilter.filter(response);
        } catch (Exception e) {
            System.out.println("处理出错:"+e.getMessage());
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NO_CONTENT);
        } finally {
            if (fullHttpRequest != null) {
                if(!HttpUtil.isKeepAlive(fullHttpRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    if(response != null) {
                        response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                        ctx.write(response);
                    }
                }
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
