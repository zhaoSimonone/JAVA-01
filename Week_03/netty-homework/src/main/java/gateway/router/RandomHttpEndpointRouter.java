package gateway.router;

import java.util.List;
import java.util.Random;

/**
 * 随机挑选一个后端API作为网关的请求API
 */
public class RandomHttpEndpointRouter implements HttpEndpointRouter {
    @Override
    public String route(List<String> urls) {
        int size = urls.size();
        //在创建一个Random对象的时候可以给定任意一个合法的种子数，种子数只是随机算法的起源数字，
        // 和生成的随机数的区间没有任何关系
        Random random = new Random(System.currentTimeMillis());
        //该方法的作用是生成一个随机的int值，该值介于[0,n)的区间，也就是0到n之间的随机int值，包含0而不包含n
        return urls.get(random.nextInt(size));
    }
}
