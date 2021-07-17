package cn.hollomyfoolish.mq;

import okhttp3.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

public class MQManager {
    static InetSocketAddress proxy = new InetSocketAddress("localhost", 8888);
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private static final OkHttpClient client = new OkHttpClient.Builder().proxy(new Proxy(Proxy.Type.HTTP, proxy)).build();
    private static final String authorization = "Basic Z3Vlc3Q6Z3Vlc3Q=";

    public boolean addUser(String name, String password) throws IOException {
        RequestBody body = RequestBody.create(JSON, "{\"username\":\""+ name +"\",\"password\":\""+ password +"\",\"tags\":\"administrator\"}");
        Request request = new Request.Builder()
                .url(String.format("http://localhost:15672/api/users/%s", name))
                .put(body)
                .addHeader("authorization", authorization)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }


    public static void main(String[] args) {
        MQManager manager = new MQManager();
        try {
            System.out.println(manager.addUser("backend", "Initial0"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
