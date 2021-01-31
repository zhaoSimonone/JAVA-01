package gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;


public interface HttpRequestFilter {

  void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx);
}
