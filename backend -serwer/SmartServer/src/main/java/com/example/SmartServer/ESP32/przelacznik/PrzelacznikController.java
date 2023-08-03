package com.example.SmartServer.ESP32.przelacznik;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
public class PrzelacznikController {
    private PrzelacznikService przelacznikService;



    @PutMapping("/przelacznik/wylacz/{adres}")
    @CrossOrigin
    public void wylaczPrzeczlacznik(@PathVariable String adres) throws IOException, InterruptedException {
        przelacznikService.wylaczPrzelacznik(adres);
    }
    @PutMapping("/przelacznik/wlacz/{adres}")
    @CrossOrigin
    public void wlaczPrzeczlacznik(@PathVariable String adres) throws IOException, InterruptedException {
        przelacznikService.wlaczPrzelacznik(adres);
    }

    @PutMapping("/przelacznik/przelacz/{adres}")
    @CrossOrigin
    public void przelaczPrzeczlacznik(@PathVariable String adres) throws IOException, InterruptedException {
        przelacznikService.przelaczPrzelacznik(adres);

    }


    @GetMapping("/przelaczniki")
    public List<Przelacznik> wyswietlPrzelaczniki()
    {
        return przelacznikService.wyszukajPrzelacnziki();
    }

    @GetMapping("/przelacznik/{adres}")
    @CrossOrigin
    public boolean dajWartosc(@PathVariable String adres)
    {
        return przelacznikService.wyszukajPrzelacznik(adres);
    }



}
