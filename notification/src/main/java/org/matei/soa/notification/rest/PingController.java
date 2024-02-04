package org.matei.soa.notification.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("*")
@RestController
public class PingController {

    @GetMapping
    public String ping() {
        return "ping";
    }

}
