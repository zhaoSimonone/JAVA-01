package OkHttp;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;

@Slf4j
public class OkHttpClientExample {
  public static void main(String[] args) {
    String url = "http://localhost:8801";
    //创建一个Client
    OkHttpClient client = new OkHttpClient();
    //创建一个Request
    Request request = new Request.Builder()
            .get()
            .url(url)
            .build();
    //通过client发起请求
    client.newCall(request).enqueue(new Callback() {
      @Override
      public void onFailure(Call call, IOException e) {
        //请求失败时会执行onFailure方法
      }

      @Override
      public void onResponse(Call call, Response response) throws IOException {
        if (response.isSuccessful()) {
          Headers headers = response.headers();
          log.info("Content-Type:{}", headers.get("Content-Type"));
          log.info("Content-Length:{}", headers.get("Content-Length"));
          String responseBody = response.body().string();
          log.info("response body:{}", responseBody);
        }
      }
    });
  }
}
