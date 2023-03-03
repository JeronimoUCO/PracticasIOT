#include <ESP8266WiFi.h>
#include <ESP8266WiFiMulti.h>
#include <WebSocketsServer.h>
#include <Hash.h>
#include <ESP8266WebServer.h>
#include <ESP8266mDNS.h>
#include "index.h"

//Instancing WiFi credentials
static const char ssid[] = "xxxxxx";
static const char password[] = "xxxxxx";

ESP8266WiFiMulti WiFiMulti;

//Starting Web Server on port 80
ESP8266WebServer server(80);
//Starting WebSockets server on port 81
WebSocketsServer webSocket = WebSocketsServer(81);

const int LEDONEPIN = LED_BUILTIN;
const int LEDTWOPIN = D1;

bool LEDONEStatus;
bool LEDTWOStatus;

// Commands sent through Web Socket
const char LEDONEON[] = "ledOneOn";
const char LEDONEOFF[] = "ledOneOff";
const char LEDTWOON[] = "ledTwoOn";
const char LEDTWOOFF[] = "ledTwoOff";


static void writeLED(bool LEDon,int PIN, bool LEDStatus)
{
  LEDStatus = LEDon;
  if (LEDon) {
    digitalWrite(PIN, 1);
  }
  else {
    digitalWrite(PIN, 0);
  }
}
//Handling Web Sockets Server actions
void webSocketEvent(uint8_t num, WStype_t type, uint8_t * payload, size_t length)
{
  Serial.printf("webSocketEvent(%d, %d, ...)\r\n", num, type);
  switch(type) {
    case WStype_DISCONNECTED:
      Serial.printf("[%u] Disconnected!\r\n", num);
      break;
      //On WS server conection print to serial console ip
    case WStype_CONNECTED:
      {
        IPAddress ip = webSocket.remoteIP(num);
        Serial.printf("[%u] Connected from %d.%d.%d.%d url: %s\r\n", num, ip[0], ip[1], ip[2], ip[3], payload);
        if (LEDONEStatus) {
          webSocket.sendTXT(num, LEDONEON, strlen(LEDONEON));
        }
        else {
          webSocket.sendTXT(num, LEDONEOFF, strlen(LEDONEOFF));
        }

        if(LEDTWOStatus){
          webSocket.sendTXT(num, LEDTWOON, strlen(LEDTWOON));
        }
        else{
          webSocket.sendTXT(num, LEDTWOOFF, strlen(LEDTWOOFF));
        }
      }
      break;
      //Handling WS messages on client button press
    case WStype_TEXT:
      Serial.printf("[%u] get Text: %s\r\n", num, payload);

      if (strcmp(LEDONEON, (const char *)payload) == 0) {
        writeLED(true, LEDONEPIN, LEDONEStatus);
      }
      else if (strcmp(LEDONEOFF, (const char *)payload) == 0) {
        writeLED(false,LEDONEPIN, LEDONEStatus);
      }
      else {
        Serial.println("Unknown command");
      }
      if (strcmp(LEDTWOON, (const char *)payload) == 0) {
        writeLED(true, LEDTWOPIN, LEDTWOStatus);
      }
      else if (strcmp(LEDTWOOFF, (const char *)payload) == 0) {
        writeLED(false,LEDTWOPIN, LEDTWOStatus);
      }
      else {
        Serial.println("Unknown command");
      }
      // send data to all connected clients
      webSocket.broadcastTXT(payload, length);
      break;

    case WStype_BIN:
      Serial.printf("[%u] get binary length: %u\r\n", num, length);
      hexdump(payload, length);

      // echo data back to browser
      webSocket.sendBIN(num, payload, length);
      break;
    default:
      Serial.printf("Invalid WStype [%d]\r\n", type);
      break;
  }
}

void handleRoot()
{
  server.send_P(200, "text/html", INDEX_HTML);
}

void handleNotFound()
{
  String message = "File Not Found\n\n";
  message += "URI: ";
  message += server.uri();
  message += "\nMethod: ";
  message += (server.method() == HTTP_GET)?"GET":"POST";
  message += "\nArguments: ";
  message += server.args();
  message += "\n";
  for (uint8_t i=0; i<server.args(); i++){
    message += " " + server.argName(i) + ": " + server.arg(i) + "\n";
  }
  server.send(404, "text/plain", message);
}

void setup()
{
  pinMode(LEDONEPIN, OUTPUT);
  pinMode(LEDTWOPIN, OUTPUT);
  writeLED(false,LEDONEPIN, LEDONEStatus);
  writeLED(false,LEDTWOPIN, LEDTWOStatus);

  Serial.begin(115200);

  WiFiMulti.addAP(ssid, password);

  while(WiFiMulti.run() != WL_CONNECTED) {
    Serial.print(".");
    delay(100);
  }

  Serial.println("");
  Serial.print("Connected to ");
  Serial.println(ssid);
  Serial.print("IP address: ");
  Serial.println(WiFi.localIP());

  if (mdns.begin("espWebSock", WiFi.localIP())) {
    Serial.println("MDNS responder started");
    mdns.addService("http", "tcp", 80);
    mdns.addService("ws", "tcp", 81);
  }
  else {
    Serial.println("MDNS.begin failed");
  }
  Serial.print("Connect to http://espWebSock.local or http://");
  Serial.println(WiFi.localIP());

  server.on("/", handleRoot);
  server.onNotFound(handleNotFound);

  server.begin();

  webSocket.begin();
  webSocket.onEvent(webSocketEvent);
}

void loop()
{
  webSocket.loop();
  server.handleClient();
}