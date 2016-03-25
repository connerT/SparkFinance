package com.sparkfinance; /**
 * Created by conner.tolley on 3/23/16.
 */

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static spark.Spark.*;

@Configuration
@ComponentScan({ "com.sparkinance"})
public class App {

    public static void main(String[] args) {

        get("/hello", (req, res) -> "Hello Conner");

    }
}
