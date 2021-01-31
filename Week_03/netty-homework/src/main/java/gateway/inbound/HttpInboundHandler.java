package gateway.inbound;

import gateway.outbound.okhttp.OkhttpOutboundHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;
import gateway.filter.HeaderHttpRequestFilter;
import gateway.filter.HttpRequestFilter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Slf4j
public class HttpInboundHandler extends ChannelInboundHandlerAdapter {

  private static Logger logger = LoggerFactory.getLogger(HttpInboundHandler.class);
  private final List<String> proxyServer;
  private OkhttpOutboundHandler handler;
  private HttpRequestFilter filter = new HeaderHttpRequestFilter();

  public HttpInboundHandler(List<String> proxyServer) {
    this.proxyServer = proxyServer;
    this.handler = new OkhttpOutboundHandler(this.proxyServer);
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
    ctx.flush();
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    try {
      FullHttpRequest fullRequest = (FullHttpRequest) msg;
      String uri = fullRequest.uri();
      System.out.println("接收到的请求url为" + uri);
      //HttpInboundHandler中调用HttpInboundHandler的handler方法对该请求进行处理
      //filter为传入的过滤规则
      handler.handle(fullRequest, ctx, filter);

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      ReferenceCountUtil.release(msg);
    }
  }
}
