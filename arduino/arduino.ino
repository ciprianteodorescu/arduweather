#include <LiquidCrystal.h>
#include <SoftwareSerial.h>
#include <SparkFunESP8266WiFi.h>
#include <OneWire.h>
#include <DallasTemperature.h>
#include <string.h>
#include <stdlib.h>

//const long SEND_WAIT = 3600000; // 60 min
const long SEND_WAIT = 10000; // 10 sec
const long LCD_WAIT = 1000; // 1 sec

unsigned long waitTime = -SEND_WAIT;
unsigned long lcdTime = -LCD_WAIT;

OneWire oneWire(10);
DallasTemperature sensors(&oneWire);
DeviceAddress therms[3];
int lastTemps[3];

LiquidCrystal lcd(7, 6, 5, 4, 3, 2);

const char mySSID[] = "STK-LX1";
const char myPSK[] = "palindrim";
ESP8266Client client;

// connection details
char server[] = "192.168.71.203";

class Measurement {
  public:
    int temp;
    int light;
};

Measurement measurement;

void setup() {
  measurement.temp = 0;
  measurement.light = 0;
  randomSeed(analogRead(0)); // take noise for seed
  
//  waitTime = millis();
//  lcdTime = millis();
  lcd.begin(16, 2);
  Serial.begin(9600);
  
  while (esp8266.begin() != true)
  {
    Serial.print("Error connecting to ESP8266.");
  delay(1000);
  }
  
  if (esp8266.status() <= 0)
  {
    while (esp8266.connect(mySSID, myPSK) < 0)
      delay(1000);
  }
  delay(1000);
  Serial.println(esp8266.localIP());
  lcd.print(esp8266.localIP());


  //temp sensors setup
  sensors.begin();
  Serial.print("Locating devices...");
  Serial.print("Found ");
  Serial.print(sensors.getDeviceCount(), DEC);
  Serial.println(" devices.");

  if (!sensors.getAddress(therms[0], 0)) 
    Serial.println("Unable to find address for Device 0");
  if (!sensors.getAddress(therms[1], 1)) 
    Serial.println("Unable to find address for Device 1");
  if (!sensors.getAddress(therms[2], 2)) 
    Serial.println("Unable to find address for Device 2");

  for(int i = 0; i < 3; i++){
    char x[100] = "Device ";
    strcat(x, (char)i);
    strcat(x, " Address: ");
    Serial.print(x);
    printAddress(therms[i]);
    Serial.println();

    sensors.setResolution(therms[i], 9);
  }
  delay(500);
  lcd.clear();
}

void printAddress(DeviceAddress deviceAddress)
{
  for (uint8_t i = 0; i < 8; i++)
  {
    // zero pad the address if necessary
    if (deviceAddress[i] < 16) Serial.print("0");
    Serial.print(deviceAddress[i], HEX);
  }
}

int printT(DeviceAddress deviceAddress)
{
  float tempC = sensors.getTempC(deviceAddress);
  if(tempC == DEVICE_DISCONNECTED_C) 
  {
    Serial.println("Error: Could not read temperature data");
    return;
  }
  //Serial.print("Temp C: ");
  //Serial.print(tempC);
  return tempC;
}

void printData(DeviceAddress deviceAddress)
{
  Serial.print("Device Address: ");
  printAddress(deviceAddress);
  Serial.print(" ");
  printT(deviceAddress);
  Serial.println();
}

int sendData(){
  int resp = client.connect(server, 8080);
  Serial.println(resp);
  //client.println("GET " + path_name + "?temp=5");
  char req[200];
  sprintf(req, "GET /arduweather/postTemp.jsp?idArduino=1&idSensorTemp=7&idSensorLight=8&temp=%d&light=%d HTTP/1.1", measurement.temp, measurement.light);
  Serial.println(req);
  client.println(req);
  client.println("Host: " + String(server));
  client.println("Connection: close");
  client.println();

//  while(client.connected()) {
//    if(client.available()){
//      // read an incoming byte from the server and print it to serial monitor:
//      char c = client.read();
//      Serial.print(c);
//    }
//  }
  
  client.stop();
  Serial.println("disconnected");
  return resp;
}

void lcdPrintTemp(){
  //sensors.requestTemperatures();
  lcd.clear();
  char x[30];
  sprintf(x, "temp=%d", measurement.temp);
  lcd.setCursor(0, 0);  
  lcd.print(x);
  sprintf(x, "light=%d%", measurement.light);
  lcd.setCursor(0, 1);
  lcd.print(x);
}

void readRandomData() {
  int temp = random(20, 31);
  int light = random(80, 96);
  int r1 = random(6);
  int r2 = random(2);

  Serial.println();
  Serial.print(temp);
  Serial.print(" ");
  Serial.print(light);
  Serial.print(" ");
  Serial.print(r1);
  Serial.print(" ");
  Serial.print(r2);

  measurement.temp = r2 == 0 ? temp + r1 : temp - r1;
  measurement.light = r2 == 0 ? light + r1 : light - r1;

  Serial.print(" ");
  Serial.print(measurement.temp);
  Serial.print(" ");
  Serial.println(measurement.light);
}

void(* resetFunc) (void) = 0;

void loop() {
  if(millis() - lcdTime >= LCD_WAIT){
    readRandomData();
    lcdPrintTemp();
    lcdTime = millis();
  }
  if(millis() - waitTime >= SEND_WAIT) {
    /*for(int i = 0; i < 3; i++)
      if(lastTemps[i] != printT(therms[i])){
        lcd.clear();
        while(sendData() < 0);
        waitTime = millis();
        break;
      }*/
      while(sendData() < 0);
      waitTime = millis();
  }
  if(millis() > 86400000)
    resetFunc();
}
