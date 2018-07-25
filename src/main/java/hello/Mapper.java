package hello;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class Mapper {

  private final ObjectMapper objectMapper = new ObjectMapper();

  private static final Mapper instance = new Mapper();

  public static Mapper getInstance() {
    return instance;
  }


  public <T> T read(String value, Class<T> classz) {
    try {
      return objectMapper.readValue(value, classz);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }


  public <T> String write(Class<T> classz) {
    try {
      return objectMapper.writeValueAsString(classz);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return null;
  }

}
