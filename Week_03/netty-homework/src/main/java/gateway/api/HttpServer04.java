package gateway.api;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.ReferenceCountUtil;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * 使用netty实现Server
 */
public class HttpServer04 {
  public static void main(String[] args) {
    int port = 8804;

    EventLoopGroup bossGroup = new NioEventLoopGroup(4);
    EventLoopGroup workerGroup = new NioEventLoopGroup(30);

    try {
      ServerBootstrap b = new ServerBootstrap();
      b.option(ChannelOption.SO_BACKLOG, 128)
              .option(ChannelOption.TCP_NODELAY, true)
              .option(ChannelOption.SO_KEEPALIVE, true)
              .option(ChannelOption.SO_REUSEADDR, true)
              .option(ChannelOption.SO_RCVBUF, 32 * 1024)
              .option(ChannelOption.SO_SNDBUF, 32 * 1024)
              .option(EpollChannelOption.SO_REUSEPORT, true)
              .childOption(ChannelOption.SO_KEEPALIVE, true)
              .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

      b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
              .handler(new LoggingHandler(LogLevel.INFO))
              .childHandler(new HttpInitializer());

      Channel ch = b.bind(port).sync().channel();
      System.out.println("开启netty http服务器，监听地址和端口为 http://127.0.0.1:" + port + '/');
      ch.closeFuture().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      bossGroup.shutdownGracefully();
      workerGroup.shutdownGracefully();
    }
  }

  private static class HttpInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
      ChannelPipeline p = socketChannel.pipeline();
      p.addLast(new HttpServerCodec());
      p.addLast(new HttpObjectAggregator(1024 * 1024));
      p.addLast(new HttpHandler());
    }
  }

  private static class HttpHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
      ctx.flush();
    }

    /**
     * 读取请求
     *
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
      try {
        Thread.sleep(5);//sleep 5ms,模拟IO
        FullHttpRequest fullRequest = (FullHttpRequest) msg;
        handler(fullRequest, ctx);
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        ReferenceCountUtil.release(msg);
      }
    }

    private void handler(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {
      FullHttpResponse response = null;
      try {
        String value = "hello netty nio"; //响应数据
        response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(value.getBytes("UTF-8")));
        response.headers().set("Content-Type", "application/json");
        response.headers().setInt("Content-Length", response.content().readableBytes());

      } catch (Exception e) {
        System.out.println("处理出错:" + e.getMessage());
        response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
      } finally {
        if (fullRequest != null) {
          if (!HttpUtil.isKeepAlive(fullRequest)) {
            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
          } else {
            response.headers().set(CONNECTION, KEEP_ALIVE);
            ctx.write(response);
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
}
