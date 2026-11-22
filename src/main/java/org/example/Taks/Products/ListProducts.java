package org.example.Taks.Products;

import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.serenitybdd.screenplay.Tasks.instrumented;


public class ListProducts implements Task {

    public static final String ALL_PRODUCTS = "allProducts";
    public static final String TOTAL_PRODUCTS = "totalProducts";

    private final int page;
    private final int limit;

    public ListProducts(int page, int limit) {
        this.page = page;
        this.limit = limit;
    }

    public static Performable withPageAndLimit(int page, int limit) {
        return instrumented(ListProducts.class, page, limit);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        List<Map<String, Object>> allProducts = new ArrayList<>();
        int currentPage = page;
        boolean hasNext;

        do {
            final int pageToRequest = currentPage;
            actor.attemptsTo(
                    Get.resource("products")
                            .with(request -> request
                                    .queryParam("page", pageToRequest)
                                    .queryParam("limit", limit)
                                    .log().all())
            );

            Response response = SerenityRest.lastResponse();
            response.prettyPrint();


            if (response.statusCode() != 200) {
                break;
            }

            allProducts.addAll(response.jsonPath().getList("data"));
            actor.remember(TOTAL_PRODUCTS, response.jsonPath().getInt("pagination.total"));

            hasNext = response.jsonPath().getBoolean("pagination.hasNext");
            currentPage++;
        } while (hasNext);

        actor.remember(ALL_PRODUCTS, allProducts);
    }
}
