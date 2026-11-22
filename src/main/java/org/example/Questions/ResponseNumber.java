package org.example.Questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.rest.questions.LastResponse;

// "¿Que numero hay en este campo?"
// Busca un campo en la respuesta de la API y te lo da como NUMERO CON DECIMALES.
// Se usa para el precio: los decimales se comparan "mas o menos" con closeTo,
// asi 999.9 y 999.90002 cuentan como iguales.
// Ej: si la API responde { "data": { "price": 999.9 } }
//   ResponseNumber.value("data.price") -> 999.9
// Uso: seeThat("El precio", ResponseNumber.value("data.price"), closeTo(999.9, PRICE_DELTA))
public class ResponseNumber implements Question<Double> {


    private final String path;

    public ResponseNumber(String path) {
        this.path = path;
    }

    public static Question<Double> value(String path) {
        return new ResponseNumber(path);
    }


    @Override
    public Double answeredBy(Actor actor) {
        return LastResponse.received().answeredBy(actor).jsonPath().getDouble(path);
    }
}
