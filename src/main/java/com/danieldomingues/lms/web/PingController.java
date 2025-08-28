package com.danieldomingues.lms.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {
    @GetMapping("/")
    public String home() { return "LMS backend is running"; }

    @GetMapping("/api/ping")
    public String ping() { return "pong"; }
}
