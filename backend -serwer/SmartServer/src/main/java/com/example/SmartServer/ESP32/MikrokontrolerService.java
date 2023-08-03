package com.example.SmartServer.ESP32;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MikrokontrolerService {

    private MikrokontrolerRepository mikrokontrolerRepository;

    public boolean dajStatus(String adres)
    {
        Mikrokontroler mikrokontroler=mikrokontrolerRepository.getById(adres);
        return mikrokontroler.getPolaczony();
    }
}
