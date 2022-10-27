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
     * pingRoute
     */
    @GetMapping("/ping")
    public String pingRoute() {
        return "pong";
    }

}
