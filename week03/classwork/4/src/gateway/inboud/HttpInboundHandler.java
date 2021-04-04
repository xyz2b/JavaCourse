package gateway.inboud;

import gateway.filter.HeaderHttpRequestFilter;
import gateway.filter.HeaderHttpResponseFilter;
import gateway.filter.HttpRequestFilter;
import gateway.filter.HttpResponseFilter;
import gateway.router.HttpEndpointRouter;
import gateway.router.RandomHttpEndpointRouter;
import io.netty.channel.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;
import gateway.outboud.netty4.NettyHttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.util.*;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

    private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(HttpInboundHandler.class);
    private final java.util.List<String> proxyServer;
    private HttpRequestFilter requestFilter = new HeaderHttpRequestFilter();
    private HttpResponseFilter responseFilter = new HeaderHttpResponseFilter();
    private HttpEndpointRouter router = new RandomHttpEndpointRouter();

    private NettyHttpClient handler;

    public HttpInboundHandler(List<String> proxyServer) {
        this.proxyServer = proxyServer;
        this.handler = new NettyHttpClient();
    }




    @Override
    public void channelReadComplete(io.netty.channel.ChannelHandlerContext ctx) {
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
            ReferenceCountUtil.release(msg);
        }
    }

    private void handlerTest(FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx) {
        FullHttpResponse response = null;

        try {
            requestFilter.filter(fullHttpRequest, ctx);

            String backendServer = router.route(this.proxyServer);
            String url = backendServer + fullHttpRequest.uri();

            Map<String, String> requestHeader = new HashMap<String, String>();

            HttpHeaders requestHeaders = fullHttpRequest.headers();

            List<Map.Entry<String, String>> headerList = requestHeaders.entries();

            for (Map.Entry<String, String> header : headerList) {
                requestHeader.put(header.getKey(), header.getValue());
            }

            HttpRequest request = NettyHttpClient.getRequestMethod(requestHeader, url, "get");

            response = handler.run(url, request); // "hello,kimmking"; // 对接上次作业的httpclient或者okhttp请求另一个url的响应数据

            responseFilter.filter(response);
        } catch (Exception e) {
            System.out.println("处理出错:"+e.getMessage());
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NO_CONTENT);
        } finally {
            if (fullHttpRequest != null) {
                if(!HttpUtil.isKeepAlive(fullHttpRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                    ctx.write(response);
                }
            }
        }
    }

    private String httpClient() throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet("http://localhost:8801/");

        CloseableHttpResponse response = httpclient.execute(httpGet);

        try {
            if (response == null || response.getCode() != 200) {
                throw new Exception("Request backend failed!");
            }

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity, "utf-8");
            }

            throw new Exception("Backend response is null!");
        } catch (ParseException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        } finally {
            if (response != null) {
                response.close();
            }

            httpclient.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
