package de.viadee.parkhaus.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;

@SpringBootApplication(proxyBeanMethods = false, exclude = SpringDataWebAutoConfiguration.class)
public class ParkhausManagerApp {
   public static void main(String[] args) {
      SpringApplication.run(ParkhausManagerApp.class, args);
   }
}
