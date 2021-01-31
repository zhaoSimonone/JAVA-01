package gateway.outbound.okhttp;

import gateway.filter.HeaderHttpResponseFilter;
import gateway.filter.HttpRequestFilter;
import gateway.filter.HttpResponseFilter;
import gateway.router.HttpEndpointRouter;
import gateway.router.RandomHttpEndpointRouter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import okhttp3.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class OkhttpOutboundHandler {

  private OkHttpClient okHttpClient;
  private ExecutorService proxyService;
  private List<String> backendUrls;

  HttpResponseFilter filter = new HeaderHttpResponseFilter();
  HttpEndpointRouter router = new RandomHttpEndpointRouter();


  private String formatUrl(String backend) {
    return backend.endsWith("/") ? backend.substring(0, backend.length() - 1) : backend;
  }

  public OkhttpOutboundHandler(List<String> backends) {
    this.backendUrls = backends.stream().map(this::formatUrl).collect(Collectors.toList());

    int cores = Runtime.getRuntime().availableProcessors();
    long keepAliveTime = 1000;
    int queueSize = 2048;

    RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();//.DiscardPolicy();

    proxyService = new ThreadPoolExecutor(cores, cores,
            keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(queueSize),
            new NamedThreadFactory("proxyService"), handler);


    okHttpClient = new OkHttpClient();

  }

  public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, HttpRequestFilter filter) {
    String backendUrl = router.route(this.backendUrls);
    final String url = backendUrl + fullRequest.uri();
    filter.filter(fullRequest, ctx);
    proxyService.submit(() -> fetchGet(fullRequest, ctx, url));
  }

  private void fetchGet(final FullHttpRequest inbound, final ChannelHandlerContext ctx, final String url) {
    final HttpGet httpGet = new HttpGet(url);
    httpGet.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);

    //创建一个Request
    Request request = new Request.Builder()
            .get()
            .url(url)
            .header(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE)
            .build();

    //通过client发起请求
    okHttpClient.newCall(request).enqueue(new Callback() {
      @Override
      public void onFailure(Call call, IOException e) {
        //请求失败时会执行onFailure方法
      }

      @Override
      public void onResponse(Call call, Response response) throws IOException {
        if (response.isSuccessful()) {
          try {
            handleResponse(inbound, ctx, response);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    });
  }

  /**
   * 将okHttp获得的Response进行封装,封装为Netty的Response
   * @param fullRequest
   * @param ctx
   * @param okhttpResponse
   * @throws Exception
   */
  private void handleResponse(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, final Response okhttpResponse) throws Exception {
    Headers headers = okhttpResponse.headers();
    FullHttpResponse response = null;
    try {
      //获得响应中的body
      byte[] body =  okhttpResponse.body().bytes();
      response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(body));
      response.headers().set("Content-Type", "application/json");
      response.headers().setInt("Content-Length", Integer.parseInt(headers.get("Content-Length")));
      filter.filter(response);
    } catch (Exception e) {
      e.printStackTrace();
      response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
      exceptionCaught(ctx, e);
    } finally {
      if (fullRequest != null) {
        if (!HttpUtil.isKeepAlive(fullRequest)) {
          ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        } else {
          //将response返回给客户端
          ctx.write(response);
        }
      }
      ctx.flush();
    }
  }

  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }
}
