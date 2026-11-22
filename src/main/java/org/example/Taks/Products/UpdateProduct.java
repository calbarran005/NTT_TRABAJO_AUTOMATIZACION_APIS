package org.example.Taks.Products;

import io.restassured.http.ContentType;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Put;
import org.example.Models.Product;

import static net.serenitybdd.screenplay.Tasks.instrumented;


public class UpdateProduct implements Task {

    private final String id;
    private final Product product;

    public UpdateProduct(String id, Product product) {
        this.id = id;
        this.product = product;
    }

    public static Performable withData(String id, Product product) {
        return instrumented(UpdateProduct.class, id, product);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Put.to("products/{id}")
                        .with(request -> request
                                .pathParam("id", id)
                                .contentType(ContentType.JSON)
                                .body(product)
                                .log().all())
        );

        SerenityRest.lastResponse().prettyPrint();
    }
}
