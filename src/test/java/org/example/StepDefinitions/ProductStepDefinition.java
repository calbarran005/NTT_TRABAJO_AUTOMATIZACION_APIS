package org.example.StepDefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import org.example.Models.Product;
import org.example.Questions.ResponseBody;
import org.example.Questions.ResponseCode;
import org.example.Questions.ResponseField;
import org.example.Questions.ResponseNumber;
import org.example.Taks.Products.CreateProduct;
import org.example.Taks.Products.DeleteProduct;
import org.example.Taks.Products.GetProductById;
import org.example.Taks.Products.ListProducts;
import org.example.Taks.Products.UpdateProduct;
import org.example.Taks.Products.PatchProduct;
import org.example.Utils.Endpoints;

import java.util.List;
import java.util.Map;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.notNullValue;

public class ProductStepDefinition {


    @Before
    public void setTheStage() {
        OnStage.setTheStage(new OnlineCast());
    }

    // DAR FORMATO AL ID CON EL DE LA API
    private static final String UUID_PATTERN = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";

    // FROMATO PARA PRECIO
    private static final double PRICE_DELTA = 0.001;

    // page y limit para listado
    private int listPage;
    private int listLimit;



    //URL de los productos
    @Given("el {actor} establece el endpoint de productos")
    public void elActorEstableceElEndpointDeProductos(Actor actor) {
        actor.whoCan(CallAnApi.at(Endpoints.BASE_URL));
    }


    // POST DE UN PRODCUTO
    @When("el actor crea un producto con {string} {string} {string} {string} {string}")
    public void elActorCreaUnProducto(String name, String price, String description, String stock, String category) {
        theActorInTheSpotlight().attemptsTo(
                CreateProduct.withData(new Product(name, Double.parseDouble(price), description, Integer.parseInt(stock), category))
        );
    }

    // LISTADO DE TODOS LOS PRODCUTOS
    @When("el actor lista los productos con page {string} y limit {string}")
    public void elActorListaLosProductos(String page, String limit) {
        listPage = Integer.parseInt(page);
        listLimit = Integer.parseInt(limit);
        theActorInTheSpotlight().attemptsTo(ListProducts.withPageAndLimit(listPage, listLimit));
    }

    // LISTADO DE PRODCUTO POR ID
    @When("el actor consulta el producto con id {string}")
    public void elActorConsultaElProductoConId(String id) {
        theActorInTheSpotlight().attemptsTo(GetProductById.withId(id));
    }

    // PATCH: (precio y stock)
    @When("el actor actualiza el producto con id {string} con precio {string} y stock {string}")
    public void elActorActualizaElProducto(String id, String price, String stock) {
        theActorInTheSpotlight().attemptsTo(
                PatchProduct.withPriceAndStock(id, Double.parseDouble(price), Integer.parseInt(stock))
        );
    }

    // PUT: ACTUALIZACION COMPLETA DEL PRODCUTOS EN TODOS SUS CAMPOS
    @When("el actor reemplaza el producto con id {string} con {string} {string} {string} {string} {string}")
    public void elActorReemplazaElProducto(String id, String name, String price, String description, String stock, String category) {
        theActorInTheSpotlight().attemptsTo(
                UpdateProduct.withData(id,
                        new Product(name, Double.parseDouble(price), description, Integer.parseInt(stock), category))
        );
    }

    // DELETE: ELIMINAMOSM EL PRODCUTOS PRO EL ID
    @When("el actor elimina el producto con id {string}")
    public void elActorEliminaElProducto(String id) {
        theActorInTheSpotlight().attemptsTo(DeleteProduct.withId(id));
    }


    // ---------- Then: validaciones ----------

    // Revisa que la API haya respondido con el codigo esperado (200, 201, 404)
    @Then("el codigo de respuesta debe ser {int}")
    public void elCodigoDeRespuestaDebeSer(int statusCode) {
        theActorInTheSpotlight().should(
                seeThat("El codigo de respuesta", ResponseCode.getStatus(), equalTo(statusCode))
        );
    }

    // Revisa que la API diga que  ("success": true) -- para eso usamos el ResponseField
    @Then("la respuesta debe ser exitosa")
    public void laRespuestaDebeSerExitosa() {
        theActorInTheSpotlight().should(
                seeThat("El campo success", ResponseField.value("success"), equalTo("true"))
        );
    }

    // Al crear un producto, revisamos que la API le haya puesto un id valido y la fecha de creacion
    @Then("el producto debe tener un id generado")
    public void elProductoDebeTenerUnIdGenerado() {
        theActorInTheSpotlight().should(
                seeThat("El id generado", ResponseField.value("data.id"), matchesPattern(UUID_PATTERN)),
                seeThat("La fecha de creacion", ResponseField.value("data.created_at"), notNullValue())
        );
    }

    // Revisa que se haya modificado el producto correctto con el id q mandamos
    @Then("el id del producto debe ser {string}")
    public void elIdDelProductoDebeSer(String id) {
        theActorInTheSpotlight().should(
                seeThat("El id del producto", ResponseField.value("data.id"), equalTo(id))
        );
    }

    // Revisamos que el producto que devolvio la API tenga los mismos datos que mandamos al momento de POST Y PUT
    @Then("el producto debe tener {string} {string} {string} {string} {string}")
    public void elProductoDebeTener(String name, String price, String description, String stock, String category) {
        validateProduct(new Product(name, Double.parseDouble(price), description, Integer.parseInt(stock), category));
    }

    // cONSULTAR UN PRDUTO Y Q SU CUERPO NO ESTÉ VACIO
    @Then("el producto debe tener todos sus datos")
    public void elProductoDebeTenerTodosSusDatos() {
        theActorInTheSpotlight().should(
                seeThat("El nombre", ResponseField.value("data.name"), notNullValue()),
                seeThat("El precio", ResponseField.value("data.price"), notNullValue()),
                seeThat("La descripcion", ResponseField.value("data.description"), notNullValue()),
                seeThat("El stock", ResponseField.value("data.stock"), notNullValue()),
                seeThat("La categoria", ResponseField.value("data.category"), notNullValue()),
                seeThat("La fecha de creacion", ResponseField.value("data.created_at"), notNullValue())
        );
    }

        // Ver q precio y el stock hayan cambiado, y que quede guardada la fecha del cambio al hacer el patch
        @Then("el producto debe tener precio {string} y stock {string}")
        public void elProductoDebeTenerPrecioYStock(String price, String stock) {
            theActorInTheSpotlight().should(
                    seeThat("El precio", ResponseNumber.value("data.price"), closeTo(Double.parseDouble(price), PRICE_DELTA)),
                    seeThat("El stock", ResponseField.value("data.stock"), equalTo(stock)),
                    seeThat("La fecha de actualizacion", ResponseField.value("data.updated_at"), notNullValue())
            );
        }

        // Revisa que se hayan recorrido todas las paginas de la lista y que no falte ningun producto
        @Then("la respuesta debe contener una lista de productos con paginacion")
        public void laRespuestaDebeContenerUnaListaDeProductos() {
            Actor actor = theActorInTheSpotlight();
            List<Map<String, Object>> allProducts = actor.recall(ListProducts.ALL_PRODUCTS);
            int total = actor.recall(ListProducts.TOTAL_PRODUCTS);

            // Cuantos productos deberiamos tener contando desde la pagina pedida hasta la ultima
            int expectedProducts = Math.max(total - (listPage - 1) * listLimit, 0);

            actor.should(
                    seeThat("El total de productos", a -> total, greaterThan(0)),
                    seeThat("La cantidad de productos recorridos", a -> allProducts.size(), equalTo(expectedProducts)),
                    seeThat("El id del primer producto", a -> String.valueOf(allProducts.get(0).get("id")), matchesPattern(UUID_PATTERN)),
                    seeThat("El limit de la respuesta", ResponseField.value("pagination.limit"), equalTo(String.valueOf(listLimit))),
                    seeThat("La ultima pagina no tiene siguiente", ResponseField.value("pagination.hasNext"), equalTo("false"))
            );
        }

        // Al eliminar, la API no devuelve nada: revisa que la respuesta venga vacia
        @Then("la respuesta no debe tener contenido")
        public void laRespuestaNoDebeTenerContenido() {
            theActorInTheSpotlight().should(
                    seeThat("El body de la respuesta", ResponseBody.asText(), equalTo(""))
            );
        }

        // Busca otra vez el producto eliminado para confirmar que de verdad ya no esta (debe dar 404)
        @Then("el producto con id {string} ya no debe existir")
        public void elProductoYaNoDebeExistir(String id) {
            theActorInTheSpotlight().attemptsTo(GetProductById.withId(id));
            theActorInTheSpotlight().should(
                    seeThat("El codigo de respuesta", ResponseCode.getStatus(), equalTo(404)),
                    seeThat("El campo success", ResponseField.value("success"), equalTo("false"))
            );
        }


        // ---------- Escenarios negativos ----------
        // Revisamos que la API haya rechazado la peticion y explique el motivo con el error y el mensaje esperados
        @Then("la respuesta debe ser fallida con el error {string} y el mensaje {string}")
        public void laRespuestaDebeSerFallida(String error, String message) {
            theActorInTheSpotlight().should(
                    seeThat("El campo success", ResponseField.value("success"), equalTo("false")),
                    seeThat("El codigo de error", ResponseField.value("error"), equalTo(error)),
                    seeThat("El mensaje de error", ResponseField.value("message"), equalTo(message))
            );
        }

        // Cuando mandamos un dato errones, revisa que la API nos diga cual campo estaba mal y por que
        @Then("el detalle del error debe indicar el campo {string} con el mensaje {string}")
        public void elDetalleDelErrorDebeIndicarElCampo(String field, String message) {
            theActorInTheSpotlight().should(
                    seeThat("El campo con error", ResponseField.value("details[0].field"), equalTo(field)),
                    seeThat("El mensaje del campo", ResponseField.value("details[0].message"), equalTo(message))
            );
        }


        // VERIFICAR Q AL CREAR PRODUCTO LOS CAMPOS SEAN LOS CORRECTOS
        private void validateProduct(Product expected) {
            theActorInTheSpotlight().should(
                    seeThat("El nombre", ResponseField.value("data.name"), equalTo(expected.getName())),
                    seeThat("El precio", ResponseNumber.value("data.price"), closeTo(expected.getPrice(), PRICE_DELTA)),
                    seeThat("La descripcion", ResponseField.value("data.description"), equalTo(expected.getDescription())),
                    seeThat("El stock", ResponseField.value("data.stock"), equalTo(String.valueOf(expected.getStock()))),
                    seeThat("La categoria", ResponseField.value("data.category"), equalTo(expected.getCategory()))
            );
        }
}
