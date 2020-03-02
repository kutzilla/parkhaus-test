package de.viadee.parkhaus.manager.resource;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
class ParkticketResourceTest {

   @Inject
   ParkticketResource parkticketResource;

   @Inject
   EmbeddedServer server;

   @Inject
   @Client("/")
   HttpClient client;

   @Test
   public void createTest(){
         assertNotNull(parkticketResource.create(LocalDateTime.now()));
   }

   @Test
   public void createIT(){
      HttpResponse<String> response =  client.toBlocking().exchange(HttpRequest.POST("/parkhaus?validTo=2020-01-31T18:00", ""));
      assertEquals(HttpStatus.OK, response.getStatus());
   }

}