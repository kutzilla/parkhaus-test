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
public class ParkhausResourceTest {

    @Inject
    private ParkticketResource parkticketResource;

    /*
     * Unittest
     */
    @Test
    public void testCreateMethod() {
        LocalDateTime entered = LocalDateTime.now().plusDays(1);

        assertNotNull(parkticketResource.create(entered));


        assertThrows(BadRequestException.class,
                () -> parkticketResource.create(null));

    }

    /*
     * Integrationstest
     */
    @Test
    public void testCreateEndpoint() {
        given().queryParam("entered", "2020-01-31T18:00")
                .post("/parkticket")
                .then().statusCode(200);
    }
}