package hello;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true)
public class APIData {

  @JsonProperty("method")
  public String method;

  @JsonProperty("delay")
  public long delay = 0l;

  @JsonProperty("body")
  public JsonNode body;

  @JsonProperty("status_code")
  public int statusCode;

  @JsonProperty("produces")
  public String produce;

  @Override
  public String toString() {
    return "[" + method + ":" + statusCode + ":" + body + ":" + produce + "]";
  }
}
