package com.example.SmartServer.ESP32;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Mikrokontroler {
    @Id
    private String adres;
    private Boolean polaczony;
}
