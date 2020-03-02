package de.viadee.parkhaus.schranke.api;


import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

@Client("parkticket")
public interface ParkhausManager {

   @Get("{id}/isAllowedToExit")
   public boolean isAllowedToExit(String id);
}
