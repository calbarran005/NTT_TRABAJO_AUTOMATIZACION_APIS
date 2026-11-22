package org.example.Taks.Products;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Delete;

import static net.serenitybdd.screenplay.Tasks.instrumented;


public class DeleteProduct implements Task {

    private final String id;

    public DeleteProduct(String id) {
        this.id = id;
    }

    public static Performable withId(String id) {
        return instrumented(DeleteProduct.class, id);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
                Delete.from("products/{id}")
                        .with(request -> request
                                .pathParam("id", id)
                                .log().all())
        );
    }
}
