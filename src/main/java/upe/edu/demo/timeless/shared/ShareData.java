package upe.edu.demo.timeless.shared;

import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
@ToString
public class ShareData {

  private ConcurrentHashMap<String, Object> dataMap = new ConcurrentHashMap<>();

  public void putData(String key, Object value) {
    dataMap.put(key, value);
  }

  public Object getData(String key) {
    return dataMap.get(key);
  }

  public void removeData(String key) {
    dataMap.remove(key);
  }

  public boolean containsKey(String key) {
    return dataMap.containsKey(key);
  }

  public void clearMap() {
    dataMap.clear();
  }
}
