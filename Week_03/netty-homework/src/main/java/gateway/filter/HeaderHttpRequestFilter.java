package gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * 拦截Request,对请求做出处理
 */
public class HeaderHttpRequestFilter implements HttpRequestFilter {

  @Override
  public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
    if (fullRequest.headers().get("simon") != null) {
      System.out.println("fullRequest headers simon = " + fullRequest.headers().get("simon"));
    }
  }
}
