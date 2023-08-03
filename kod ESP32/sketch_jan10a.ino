
#include <WiFi.h>
#include <ESPAsyncWebServer.h>
#include <OneWire.h>
#include <DallasTemperature.h>
//przelacznik
//Ustaw wartość true, aby zdefiniować przekaźnik jako normalnie otwarty (NO)
#define RELAY_NO    true

//Ustaw liczbę przekaźników
#define NUM_RELAYS  1

//Przyporządkuj każde GPIO do&nbsp;przekaźnika
int relayGPIOs[NUM_RELAYS] = {5};

const char* PARAM_INPUT_1 = "relay";
const char* PARAM_INPUT_2 = "state";



//temperatura
// Pin, do&nbsp;którego&nbsp;podłączony jest DS18B20
#define ONE_WIRE_BUS 4

// Konfigurowanie instancji OneWire do&nbsp;komunikacji z&nbsp;dowolnymi urządzeniami OneWire
OneWire oneWire(ONE_WIRE_BUS);

// Przekazanie referencji OneWire do&nbsp;czujnika temperatury Dallas
DallasTemperature sensors(&oneWire);

// Zamienień na&nbsp;własne dane WiFi
const char* ssid = "2.4G-Vectra-WiFi-66A824";
const char* password = "hms31exxyyx1f83p";

// Stworzenie obiektu AsyncWebServer na&nbsp;porcie 80
AsyncWebServer server(80);

String readDSTemperatureC() {
  // Wywołanie sensors.requestTemperatures() aby wysłać odczytaną temperaturę do&nbsp;wszystkich urządzeń na&nbsp;magistrali
  sensors.requestTemperatures();
  float tempC = sensors.getTempCByIndex(0);

  if(tempC == -127.00) {
    Serial.println("Błąd odczytu danych z&nbsp;czujnika DS18B20");
    return "--";
  } else {
    Serial.print("Temperatura Celsjusz: ");
    Serial.println(tempC);
  }
  return String(tempC);
}

//czytanie temperatury
String readDSTemperatureF() {
  // Wywołanie sensors.requestTemperatures() aby wysłać odczytaną temperaturę do&nbsp;wszystkich urządzeń na&nbsp;magistrali
  sensors.requestTemperatures();
  float tempF = sensors.getTempFByIndex(0);

  if(int(tempF) == -196){
    Serial.println("Błąd odczytu danych z&nbsp;czujnika DS18B20");
    return "--";
  } else {
    Serial.print("Temperatura Fahrenheit: ");
    Serial.println(tempF);
  }
  return String(tempF);
}


void setup(){
  // Port szeregowy do&nbsp;debugowania
  Serial.begin(9600);
  Serial.println();
  
      // Ustaw wszystkie przekaźniki na&nbsp;wyłączenie przy starcie programu - jeśli ustawione na&nbsp;Normally Open (NO), przekaźnik jest wyłączony, gdy&nbsp;ustawisz przekaźnik na&nbsp;HIGH
  for(int i=1; i<=NUM_RELAYS; i++){
    pinMode(relayGPIOs[i-1], OUTPUT);
    if(RELAY_NO){
      digitalWrite(relayGPIOs[i-1], HIGH);
    }
    else{
      digitalWrite(relayGPIOs[i-1], LOW);
    }
  }


  // Start biblioteki DS18B20
  sensors.begin();

  // Łączenie z&nbsp;WiFi
  WiFi.begin(ssid, password);
  Serial.println("łączenie z&nbsp;WiFi");
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println();

  // Wyświetlanie lokalnego adresu IP ESP
  Serial.println(WiFi.localIP());


  server.on("/temperaturec", HTTP_GET, [](AsyncWebServerRequest *request){
    request->send_P(200, "text/plain", readDSTemperatureC().c_str());
  });
  server.on("/temperaturef", HTTP_GET, [](AsyncWebServerRequest *request){
    request->send_P(200, "text/plain", readDSTemperatureF().c_str());
  });

  server.on("/update", HTTP_GET, [] (AsyncWebServerRequest *request) {
    String inputMessage;
    String inputParam;
    String inputMessage2;
    String inputParam2;
    //GET input1 value on&nbsp;<ESP_IP>/update?relay=<inputMessage>
    if (request->hasParam(PARAM_INPUT_1) & request->hasParam(PARAM_INPUT_2)) {
      inputMessage = request->getParam(PARAM_INPUT_1)->value();
      inputParam = PARAM_INPUT_1;
      inputMessage2 = request->getParam(PARAM_INPUT_2)->value();
      inputParam2 = PARAM_INPUT_2;
      if(RELAY_NO){
        Serial.print("NO ");
        digitalWrite(relayGPIOs[inputMessage.toInt()-1], !inputMessage2.toInt());
              }
      else{
        Serial.print("NC ");
        digitalWrite(relayGPIOs[inputMessage.toInt()-1], inputMessage2.toInt());
      }
    }
    else {
      inputMessage = "No message sent";
      inputParam = "none";
    }
    request->send(200, "text/plain", "OK");
  });
    server.on("/relayStatus", HTTP_GET, [] (AsyncWebServerRequest *request) {
    String inputMessage;
    String inputParam;
    int Status=1;
    //GET input1 value on&nbsp;<ESP_IP>/update?relay=<inputMessage>
    if (request->hasParam(PARAM_INPUT_1)) {
      inputMessage = request->getParam(PARAM_INPUT_1)->value();
      inputParam = PARAM_INPUT_1;
    if(RELAY_NO){
        Serial.print("NO ");
        Status=digitalRead(relayGPIOs[inputMessage.toInt()-1]);
        Serial.println(digitalRead(relayGPIOs[inputMessage.toInt()-1]));
              }
      else{
        Serial.print("NC ");
        Status=digitalRead(relayGPIOs[inputMessage.toInt()-1]);
        Serial.println(digitalRead(relayGPIOs[inputMessage.toInt()-1]));
      }
    }
    else {
      inputMessage = "No message sent";
      inputParam = "none";
    }
    if(Status==1)
      Status=0;
        else
      Status=1;
    request->send(200, "text/plain", String(Status));
  });

  // Start server
  server.begin();
}

void loop(){

}
