package org.example.Questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.rest.questions.LastResponse;

// "¿Que dice este campo?"
// Busca un campo en la respuesta de la API y te lo da como TEXTO.
// Ej: si la API responde { "success": true, "data": { "name": "Laptop" } }
//   ResponseField.value("success")   -> "true"
//   ResponseField.value("data.name") -> "Laptop"
// Uso: seeThat("El nombre", ResponseField.value("data.name"), equalTo("Laptop"))
public class ResponseField implements Question<String> {


    private final String path;

    public ResponseField(String path) {
        this.path = path;
    }


    public static Question<String> value(String path) {
        return new ResponseField(path);
    }


    @Override
    public String answeredBy(Actor actor) {
        return LastResponse.received().answeredBy(actor).jsonPath().getString(path);
    }
}
