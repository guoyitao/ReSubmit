package com.example.demo.web;


import com.example.demo.annotation.ReSubmitLock;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {

    @ReSubmitLock(prefix = "/lock",waitTime = 100,expire = 100)
    @GetMapping("/lock")
    public String query(@RequestParam String token) {
        return "success - " + token;
    }


    @GetMapping("/unlock")
    public String query2(@RequestParam String token) {
        return "success - " + token;
    }
}