package com.example.SmartServer.ESP32;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MikrokontrolerController {

    private MikrokontrolerService mikrokontrolerService;

    @GetMapping("/status/{adres}")
    @CrossOrigin
    public boolean dajStatus(@PathVariable String adres)
    {
        return mikrokontrolerService.dajStatus(adres);
    }
}
