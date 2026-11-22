package org.example.Taks.Products;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

import static net.serenitybdd.screenplay.Tasks.instrumented;


public class GetProductById implements Task {

    private final String id;

    public GetProductById(String id) {
        this.id = id;
    }

    public static Performable withId(String id) {
        return instrumented(GetProductById.class, id);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Get.resource("products/{id}")
                        .with(request -> request
                                .pathParam("id", id)
                                .log().all())
        );

        SerenityRest.lastResponse().prettyPrint();
    }
}
