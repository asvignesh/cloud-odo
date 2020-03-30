package io.cloudodo.configclient;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ClientBean {
  @Value("${k1}")
  private String k1;

  public String getK1() {
    return k1;
  }

  @PostConstruct
  public void postConstruct() {
    System.out.println(k1);
  }
}
