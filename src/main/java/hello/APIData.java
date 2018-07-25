package hello;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.springframework.http.HttpStatus;

@JsonIgnoreProperties(ignoreUnknown = true)
public class APIData {

  @JsonProperty("method")
  public String method;

  @JsonProperty("delay")
  public long delay = 0l;

  @JsonProperty("body")
  public JsonElement body;

  @JsonProperty("status_code")
  public int statusCode;

  @JsonProperty("produces")
  public String produce;

  @Override
  public String toString() {
    return "[" + method + ":" + statusCode + ":" + body + ":" + produce + "]";
  }


  public static void main(String[] args) {
    String s = "{\"delay\":\"100\",\"method\":\"post\",\"status_code\":500,\"body\":{\"error\":0,\"message\":\"Amount Debited Successfully\",\"gatewayTransactionId\":\"TestingTransactionByMockAPI\"}}";
    Gson gson = new Gson();
    APIData apiData = gson.fromJson(s, APIData.class);
    System.out.println(apiData);
  }
}
