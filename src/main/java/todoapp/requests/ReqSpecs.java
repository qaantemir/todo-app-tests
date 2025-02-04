package todoapp.requests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ReqSpecs {
    static final RequestSpecification unauthReqSpecs = RestAssured.given()
            .contentType(ContentType.JSON);
    static final RequestSpecification authReqSpecs = RestAssured.given()
            .contentType(ContentType.JSON)
            .auth().preemptive().basic("admin", "admin");
}
