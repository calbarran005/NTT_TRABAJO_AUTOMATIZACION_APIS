package org.example.Taks.Products;

import io.restassured.http.ContentType;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;
import org.example.Models.Product;

import static net.serenitybdd.screenplay.Tasks.instrumented;


public class CreateProduct implements Task {

    public static final String PRODUCT_ID = "productId";

    private final Product product;

    public CreateProduct(Product product) {
        this.product = product;
    }

    public static Performable withData(Product product) {
        return instrumented(CreateProduct.class, product);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Post.to("products")
                        .with(request -> request
                                .contentType(ContentType.JSON)
                                .body(product)
                                .log().all())
        );

        SerenityRest.lastResponse().prettyPrint();


        if (SerenityRest.lastResponse().statusCode() == 201) {
            actor.remember(PRODUCT_ID, SerenityRest.lastResponse().path("data.id").toString());
        }
    }
}
