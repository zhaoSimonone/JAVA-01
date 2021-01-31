package gateway.filter;

import io.netty.handler.codec.http.FullHttpResponse;

/**
 * 拦截Response,在response上set key-value
 */
public class HeaderHttpResponseFilter implements HttpResponseFilter {
    @Override
    public void filter(FullHttpResponse response) {
        response.headers().set("simon", "SimonZhao is learning Netty");
    }
}
