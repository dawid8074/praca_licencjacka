package com.example.SmartServer.ESP32.czujnik;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
public class CzujnikController {
    private CzujnikService czujnikService;

    @PutMapping("/czujnik") //dodawanie/edycja
    public void zmienWartoscCzujnika(@RequestBody CzujnikInputDTO czujnikInputDTO) throws IOException, InterruptedException {

        czujnikService.edytujCzujnik(czujnikInputDTO);
    }


    @GetMapping("/czujniki")
    public List<Czujnik> wyswietlCzujniki()
    {
        return czujnikService.wyszukajCzujniki();
    }

    @GetMapping("/czujnik/{adres}")
    @CrossOrigin
    public String dajWartoscCzujnika(@PathVariable String adres)
    {
        return czujnikService.wyszukajCzujnik(adres);
    }
}
