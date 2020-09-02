
 #ifdef ESP32
  #include <WiFi.h>
  #include <HTTPClient.h>
#else
  #include <ESP8266WebServer.h>
  #include <ESP8266WiFi.h>
  #include <ESP8266HTTPClient.h>
  #include <WiFiClient.h>
#endif

#include <Wire.h>
#include <Servo.h>

Servo myServo;
static const int pinServo= 15; //D8 pin del ESP8226
const char* ssid = "Nombre de la red"; 
const char* password = "contraseña"; 

#define TRIGGER 16  //D0 pin del ESP8226
#define ECHO    5   //D1 pin del ESP8226

int distanciamax= 200; // distancia máxima de 2 metros con respecto a una persona
long duracion=0;
int16_t distancia= 0; 
uint8_t contador = 0;   

const char* serverName = "http://ejemplo.com/ejemplo.php"; //link para hacer la conexión al Servidor
String apiKeyValue = "tPmAT5Ab3j7F9";

int mascarilla= 1234; //codigo de la máscarilla
String sensorName = "Ultrasonico";
String msj="";
boolean isSeguro=true;
boolean isSeguro2=true;
int comienzo=0;


void setup(void){
  
  Serial.begin(115200);
  myServo.attach(pinServo);
  myServo.write(180); // la compuerta se cierra apenas se inicia el sistema
  delay(100);
  pinMode(TRIGGER, OUTPUT);
  pinMode(ECHO, INPUT);     // Se establecen los pines de salida o entrada
  pinMode(BUILTIN_LED, OUTPUT);

  WiFi.begin(ssid, password);   // Se realiza la conexión wifi
  Serial.println("Connecting");
  while(WiFi.status() != WL_CONNECTED) { 
    delay(500);
    Serial.print(".");
  }                 // Se asegura que la conexión wifi sea exitosa
  Serial.println("");
  Serial.print("Connected to WiFi network with IP Address: ");        
  Serial.println(WiFi.localIP());
}

void loop() {
    digitalWrite(TRIGGER, LOW);  
    delayMicroseconds(2);     
    digitalWrite(TRIGGER, HIGH);
    delayMicroseconds(10);          //Se realiza la conversión de la distancia a cm
    digitalWrite(TRIGGER, LOW);
    duracion = pulseIn(ECHO, HIGH);
    distancia = (duracion/2) / 29.1;    
  
  if (distancia < 0) distancia = 0;
  
  if (distancia < distanciamax) {
      isSeguro=false;
  }
  else{
     isSeguro=true;
  }
  
  if (comienzo==0){
   isSeguro2=not isSeguro;
  }
    if (isSeguro!=isSeguro2) {     //Se envía datos cuando el estado de la persona cambie
      if (distancia < distanciamax){
    myServo.write(180);
    }
    if (distancia >= distanciamax) {
    myServo.write(-180);
    }
    msj = String(distancia);
    delay(500);
    if(WiFi.status()== WL_CONNECTED){
  
    HTTPClient http;
    http.begin(serverName); // Se hace la conexión con el servidor
    
    http.addHeader("Content-Type", "application/x-www-form-urlencoded"); // Tipo de dato que se enviarán
    
    String httpRequestData = "api_key=" + apiKeyValue + 
  "&codigo_mascarilla="+ String(mascarilla) + "&distancia=" + msj +  //Se crea un String que tendrá toda la informacion a enviar
  "&seguro=" + String(isSeguro)+ "";
    Serial.print("httpRequestData: ");
    Serial.println(httpRequestData);
    int httpResponseCode = http.POST(httpRequestData);  // se crea un int que nos arrojará un número de como fue la petición
    
  if (httpResponseCode>0) { // Lo que se espera es un número de 200, la cual indica que está todo "ok".
      Serial.print("HTTP Response code: "); 
      Serial.println(httpResponseCode);
    }
    else {
      Serial.print("Error code: ");
      Serial.println(httpResponseCode);
    }

    http.end(); //Se termina la conexión con el servidor
  }     
  else {
  Serial.println("WiFi Disconnected");
  }
    
  delay(30000);     // Se espera 30 segundos por cada envío de dato
    
  }
  Serial.println(isSeguro!= isSeguro2);
  Serial.println(distancia);    
  isSeguro2 = isSeguro;
  comienzo++;
}
