package org.example.Taks.Products;

import io.restassured.http.ContentType;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Patch;

import java.util.Map;

import static net.serenitybdd.screenplay.Tasks.instrumented;


public class PatchProduct implements Task {

    private final String id;
    private final double price;
    private final int stock;

    public PatchProduct(String id, double price, int stock) {
        this.id = id;
        this.price = price;
        this.stock = stock;
    }

    public static Performable withPriceAndStock(String id, double price, int stock) {
        return instrumented(PatchProduct.class, id, price, stock);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Patch.to("products/{id}")
                        .with(request -> request
                                .pathParam("id", id)
                                .contentType(ContentType.JSON)
                                .body(Map.of("price", price, "stock", stock))
                                .log().all())
        );

        SerenityRest.lastResponse().prettyPrint();
    }
}
