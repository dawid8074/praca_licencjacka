package com.example.SmartServer;

import com.example.SmartServer.ESP32.Mikrokontroler;
import com.example.SmartServer.ESP32.MikrokontrolerRepository;
import com.example.SmartServer.ESP32.czujnik.Czujnik;
import com.example.SmartServer.ESP32.czujnik.CzujnikRepository;
import com.example.SmartServer.ESP32.przelacznik.Przelacznik;
import com.example.SmartServer.ESP32.przelacznik.PrzelacznikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Component
public class DataLoader implements ApplicationRunner {

    private MikrokontrolerRepository mikrokontrolerRepository;
    private CzujnikRepository czujnikRepository;
    private PrzelacznikRepository przelacznikRepository;

    @Autowired
    public DataLoader(MikrokontrolerRepository mikrokontrolerRepository, CzujnikRepository czujnikRepository, PrzelacznikRepository przelacznikRepository) {
        this.mikrokontrolerRepository = mikrokontrolerRepository;
        this.przelacznikRepository=przelacznikRepository;
        this.czujnikRepository=czujnikRepository;
    }



    @Override
    public void run(ApplicationArguments args) throws Exception {

        //obiekty na start
        Mikrokontroler mikrokontroler = new Mikrokontroler("192.168.0.18", false);
        mikrokontrolerRepository.save(mikrokontroler);
        Czujnik czujnik = new Czujnik("192.168.0.18", "0");
        czujnikRepository.save(czujnik);
        Przelacznik przelacznik = new Przelacznik("192.168.0.18", false);
        przelacznikRepository.save(przelacznik);


        //ustawianie wartosc dla połaczenia z kontrolerem oraz temperatury na start
        while (true) {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://192.168.0.18/temperaturec"))
                        .timeout(Duration.ofMillis(2000))
                        .build();

                HttpResponse response = client.send(request,
                        HttpResponse.BodyHandlers.ofString());

                czujnik.setWartosc((String) response.body());
                czujnikRepository.save(czujnik);

                mikrokontroler.setPolaczony(true);
                mikrokontrolerRepository.save(mikrokontroler);
                break;

            } catch (Exception e) {

                mikrokontroler.setPolaczony(false);
                mikrokontrolerRepository.save(mikrokontroler);
                Thread.sleep(1000);

            }
        }
        //ustawienie wartosci przelacznika
        while (true) {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://192.168.0.18/relayStatus?relay=1"))
                        .timeout(Duration.ofMillis(2000))
                        .build();

                HttpResponse response = client.send(request,
                        HttpResponse.BodyHandlers.ofString());

                if (response.body().equals("0"))
                    przelacznik.setWlaczony(false);
                else
                    przelacznik.setWlaczony(true);
                przelacznikRepository.save(przelacznik);

                mikrokontroler.setPolaczony(true);
                mikrokontrolerRepository.save(mikrokontroler);
                break;

            } catch (Exception e) {

                mikrokontroler.setPolaczony(false);
                mikrokontrolerRepository.save(mikrokontroler);
                Thread.sleep(1000);

            }
        }

        System.out.println("serwer gotowy");
        //ciągłe sprawdzanie temperatury wraz z statusem polaczenia
        while (true) {

            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://192.168.0.18/temperaturec"))
                        .timeout(Duration.ofMillis(2000))
                        .build();

                HttpResponse response = client.send(request,
                        HttpResponse.BodyHandlers.ofString());


                String zmienna1=(String) response.body();
                double zmienna2= Double.parseDouble(zmienna1);
                if (Math.abs(zmienna2-Float.parseFloat(czujnik.getWartosc()))<20) {
                    czujnik.setWartosc((String) response.body());
                    czujnikRepository.save(czujnik);
                }

                mikrokontroler.setPolaczony(true);
                mikrokontrolerRepository.save(mikrokontroler);

                Thread.sleep(1000);

            } catch (Exception e) {
                mikrokontroler.setPolaczony(false);
                mikrokontrolerRepository.save(mikrokontroler);
                Thread.sleep(1000);


            }

        }
    }

}
