package org.example.Questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.rest.questions.LastResponse;

// LO USAMOS EN EL  DELETE PARA VALIDAR QUE EL 204  Y COMPROBAMOS QUE VENGA VACIO
public class ResponseBody implements Question<String> {


    public static Question<String> asText() {
        return new ResponseBody();
    }

    @Override
    public String answeredBy(Actor actor) {
        return LastResponse.received().answeredBy(actor).asString();
    }
}
