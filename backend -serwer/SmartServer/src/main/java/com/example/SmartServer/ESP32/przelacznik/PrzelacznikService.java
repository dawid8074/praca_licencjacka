package com.example.SmartServer.ESP32.przelacznik;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

@Service
@AllArgsConstructor
public class PrzelacznikService {
    private PrzelacznikRepository przelacznikRepository;


    public void dodajPrzelacznik(String adres, boolean status)
    {
        Przelacznik przelacznik=new Przelacznik(adres,status);
        przelacznikRepository.save(przelacznik);
    }
    public void edytujPrzelacznik(String adres, boolean status) {
        if (!(przelacznikRepository.existsById(adres))) {
            dodajPrzelacznik(adres,status);
        } else {
            Przelacznik przelacznik = przelacznikRepository.getById(adres);
            przelacznik.setWlaczony(status);
            przelacznikRepository.save(przelacznik);
        }
    }
    public List<Przelacznik> wyszukajPrzelacnziki()
    {
        return przelacznikRepository.findAll();
    }
    public boolean wyszukajPrzelacznik(String adres)
    {
        Przelacznik przelacznik=przelacznikRepository.getById(adres);
        return przelacznik.getWlaczony();
    }


    public void wylaczPrzelacznik(String adres) throws IOException, InterruptedException {
        try
        {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://"+adres+"/update?relay=1&state=0"))
                    .timeout(Duration.ofMillis(2000))
                    .build();

            HttpResponse response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
            edytujPrzelacznik(adres, false);
        }
       catch (Exception e)
       {
           throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT);
       }
    }

    public void wlaczPrzelacznik(String adres) throws IOException, InterruptedException {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://"+adres+"/update?relay=1&state=1"))
                    .timeout(Duration.ofMillis(2000))
                    .build();

            HttpResponse response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
            edytujPrzelacznik(adres,true);
        }
       catch (Exception e)
       {
           throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT);

       }

    }

    public void przelaczPrzelacznik(String adres) throws IOException, InterruptedException {
        Przelacznik przelacznik=przelacznikRepository.getById(adres);
        if (przelacznik.getWlaczony())
        {
            wylaczPrzelacznik(adres);
        }
        else
        {
            wlaczPrzelacznik(adres);
        }


    }
}
