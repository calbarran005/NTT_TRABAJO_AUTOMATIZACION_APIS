package org.example.Questions;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.rest.questions.LastResponse;


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
