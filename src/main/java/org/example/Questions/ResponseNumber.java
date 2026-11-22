package org.example.Questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.rest.questions.LastResponse;


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
