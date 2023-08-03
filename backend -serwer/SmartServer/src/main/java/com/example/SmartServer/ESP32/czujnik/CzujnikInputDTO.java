package com.example.SmartServer.ESP32.czujnik;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class CzujnikInputDTO {
    private String adres;
    private String wartosc;
}
