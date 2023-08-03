package com.example.SmartServer.ESP32.przelacznik;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Getter
@Setter
public class Przelacznik {
    @Id
    private String adres;
    private Boolean wlaczony;
}
