package com.example.SmartServer.ESP32.czujnik;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CzujnikService {
    private CzujnikRepository czujnikRepository;

    public void dodajCzujnik(CzujnikInputDTO czujnikInputDTO)
    {

        Czujnik czujnik=new Czujnik(czujnikInputDTO.getAdres(),czujnikInputDTO.getWartosc());
        czujnikRepository.save(czujnik);
    }


    public void edytujCzujnik(CzujnikInputDTO czujnikInputDTO)
    {
        if (!(czujnikRepository.existsById(czujnikInputDTO.getAdres())))
        {
            dodajCzujnik(czujnikInputDTO);
        }
        else
        {
            Czujnik czujnik=czujnikRepository.getById(czujnikInputDTO.getAdres());
            czujnik.setWartosc(czujnikInputDTO.getWartosc());
            czujnikRepository.save(czujnik);
        }
    }
    public List<Czujnik> wyszukajCzujniki()
    {
        return czujnikRepository.findAll();
    }
    public String wyszukajCzujnik(String adres)
    {
        Czujnik czujnik=czujnikRepository.getById(adres);
        return czujnik.getWartosc();
    }


}
