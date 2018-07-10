package hello;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
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

  @RequestMapping(value = "/mock_create/**", method = RequestMethod.POST, produces = {
      "application/json"})
  public ResponseEntity<Void> addMockResponse(HttpServletRequest request,
      @RequestBody String body) {
    String key = request.getRequestURI();
    APIData apiData = gson.fromJson(body, APIData.class);
    if (apiData == null) {
      log("Unable to save value for key: " + key + " value is null: " + body);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    String s = jedis.set(key, new JsonParser().parse(body).getAsJsonObject().toString());
    log("Value saved for key: " + key + ", response: " + s);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @RequestMapping(value = "/mock/**", method = RequestMethod.POST)
  public ResponseEntity<String> getMockResponse(HttpServletRequest request,
      @RequestBody String body) throws InterruptedException {
    String key = request.getRequestURI();
    return getMockResponse(HttpMethod.POST, key);
  }


  @RequestMapping(value = "/mock/**", method = RequestMethod.PUT)
  public ResponseEntity<String> getMockResponsePut(HttpServletRequest request,
      @RequestBody String body) throws InterruptedException {
    String key = request.getRequestURI();
    return getMockResponse(HttpMethod.PUT, key);
  }

  @RequestMapping(value = "/mock/**", method = RequestMethod.GET)
  public ResponseEntity<String> getMockResponse(HttpServletRequest request)
      throws InterruptedException {
    String key = request.getRequestURI();
    return getMockResponse(HttpMethod.GET, key);
  }


  private ResponseEntity<String> getMockResponse(HttpMethod httpMethod, String url)
      throws InterruptedException {
    String str = jedis.get(url);
    if (str == null) {
      log("Value not found for key: " + url);
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    APIData apiData = gson.fromJson(str, APIData.class);
    Thread.sleep(apiData.delay);
    if (apiData.method.equalsIgnoreCase(httpMethod.name())) {
      HttpHeaders headers = new HttpHeaders();
      if (apiData.produce != null) {
        headers.add("content-type", apiData.produce);
      }
      log("found and returned value for key: " + url + ", method: " + httpMethod);
      return new ResponseEntity<>(apiData.body, headers, HttpStatus.valueOf(apiData.statusCode));
    }
    log("Value found for key: " + url + ", but different method: " + apiData);
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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

  private static void log(Object object) {
    System.out.println(object);
  }

}
