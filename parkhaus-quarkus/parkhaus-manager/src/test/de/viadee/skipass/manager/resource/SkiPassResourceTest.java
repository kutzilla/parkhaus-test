package de.viadee.parkhaus.manager.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
public class parkhausResourceTest {

    @Inject
    private parkhausResource parkhausResource;

    /*
     * Unittest
     */
    @Test
    public void testCreateMethod() {
        LocalDateTime validTo = LocalDateTime.now().plusDays(1);
        LocalDateTime validFrom = LocalDateTime.now();

        assertNotNull(parkhausResource.create(validTo, validFrom));


        assertThrows(BadRequestException.class,
                () -> parkhausResource.create(null, null));


        LocalDateTime unvalidTo = LocalDateTime.now().minusDays(1);
        assertThrows(BadRequestException.class,
                () -> parkhausResource.create(unvalidTo, validFrom));
    }

    /*
     * Integrationstest
     */
    @Test
    public void testCreateEndpoint() {
        given().queryParam("validTo", "2020-01-31T18:00")
                .post("/parkhaus")
                .then().statusCode(200);
    }
}
