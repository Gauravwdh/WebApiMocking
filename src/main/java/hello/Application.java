package hello;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by gauravwadhwa on 14/09/17.
 */
@SpringBootApplication
@RestController
public class Application {

    private static Gson gson;
    private static Jedis jedis;

    @RequestMapping(value = "/mock/**", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<Void> addMockResponse(HttpServletRequest request, @RequestBody String body) {
        String key = request.getRequestURI();
        String s = jedis.set(key, new JsonParser().parse(body).getAsJsonObject().toString());
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/mock/**", method = RequestMethod.GET, produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<String> getMockResponse(HttpServletRequest request) throws InterruptedException {
        String key = request.getRequestURI();
        String str = jedis.get(key);
        if (str == null) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
        JsonObject jsonObject = new JsonParser().parse(str).getAsJsonObject();
        long delay = 0;
        if (jsonObject.has("delay")) {
            delay = jsonObject.get("delay").getAsLong();
        }
        int statusCode = HttpStatus.OK.value();
        if (jsonObject.has("status_code")) {
            statusCode = jsonObject.get("status_code").getAsInt();
        }
        Thread.sleep(delay);
        String r = jsonObject.get("body").getAsJsonObject().toString();
        return new ResponseEntity<String>(r, HttpStatus.valueOf(statusCode));
    }

    public static void main(String[] args) throws Exception {
        gson = new Gson();
        jedis = new Jedis("redis");
        String auth = System.getenv("auth");
        System.out.println("Redis auth: " + auth);
        jedis.auth(auth);
        jedis.flushAll();
        SpringApplication.run(Application.class, args);
    }

}
