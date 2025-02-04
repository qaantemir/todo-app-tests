package todoapp.specs;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.builder.RequestSpecBuilder;

// не получилось реализовать по аналогии
public class ReqSpecs {
    public RequestSpecification unauthSpecs() {
        RequestSpecBuilder reqSpecBuilder = new RequestSpecBuilder();
        reqSpecBuilder.setContentType(ContentType.JSON);
        return reqSpecBuilder.build();
    }
}
