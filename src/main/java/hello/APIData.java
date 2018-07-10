package hello;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
  public int statusCode = HttpStatus.OK.value();

  @JsonProperty("produces")
  public String produce = "application/json";

  @Override
  public String toString() {
    return "[" + method + ":" + statusCode + ":" + body + ":" + produce + "]";
  }

}
