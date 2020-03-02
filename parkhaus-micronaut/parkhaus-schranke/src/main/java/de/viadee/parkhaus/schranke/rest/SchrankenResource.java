package de.viadee.parkhaus.schranke.rest;

import de.viadee.parkhaus.schranke.service.SchrankeService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;

@Controller("schranke")
public class SchrankenResource {

    SchrankeService schrankenService;

    public SchrankenResource(SchrankeService schrankenService) {
        this.schrankenService = schrankenService;
    }

    @Get(value = "{id}", produces = MediaType.TEXT_PLAIN)
    public String get(@PathVariable("id") String id) {
        return this.schrankenService.canExit(id) ? "GREEN" : "RED";
    }
}

