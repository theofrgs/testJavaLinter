package com.outside;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ApplicationDevHelpers <b>class</b>.
 *
 * @see Annotation
 */
@RestController
public class ApplicationDevHelpers {

    /**
     * Doubles the value.
     * The long and detailed explanation what the method does.
     *
     * @param value for doubling.
     * @return double value.
     * */
    @GetMapping("/ping")
    public String pingRoute() {
        return "pong";
    }

}
