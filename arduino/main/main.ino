#include <SPI.h>
#include <WiFi.h>
#include <Wire.h>
#include <Adafruit_Sensor.h>
#include <Adafruit_BME280.h>
#include <ArduinoJson.h>

/*
    Capteur : BME280
*/

Adafruit_BME280 capteur;

float  pressionatm = 1013.2; //pression atmosphérique au niveau de la mer hPa

char ssid[] = "SFR-5450";
char pass[] = "mot de passe";
int keyIndex = 0;

int status = WL_IDLE_STATUS;

WiFiServer serveur(80);

void setup() {
  Serial.begin(9600);

  if (WiFi.status() == WL_NO_SHIELD) {
    Serial.println("Pas de carte Wifi");
    while (true);
  }

  Serial.print("Connexion à");
  Serial.println(ssid);
  status = WiFi.begin(ssid, pass);

  delay(10000);

  if (status != WL_CONNECTED) {
    Serial.println("Erreur de connexion");
    while (true);
  } else {
    serveur.begin();
    Serial.print("Connecté à l'adresse :");
    IPAddress iplocale = WiFi.localIP();
    Serial.println(iplocale);
  }


  if (!capteur.begin(0x76)) {
    Serial.println("Problème avec le capteur");
  } else {
    Serial.println("okcapteur");
  }

}

void loop() {
  WiFiClient client = serveur.available();
  if (client) {
    Serial.println("new client");
    bool currentLineIsBlank = true;
    
    while (client.connected()) {
      if (client.available()) {
        char c = client.read();
        Serial.write(c);
        if (c == '\n' && currentLineIsBlank) {
          
          //En-tête de la réponse HTTP
          client.println("HTTP/1.1 200 OK");
          client.println("Content-Type: application/json");
          client.println("Connection: close");
          client.println();
          
          DynamicJsonDocument rep(1024);
          
          rep["pression"]   = capteur.readPressure() / 100.0F; // en hPa 
          rep["altitude"]   = capteur.readAltitude(pressionatm); // en m 
          rep["humid"]   = capteur.readHumidity();  // en %
          rep["temp"]   = capteur.readTemperature(); // en °C
          String json;
          serializeJson(rep, json);
          client.println(json);
        }
      }
    }
    delay(1);
    client.stop();
    Serial.println("Fin de la requête");
  }
}
