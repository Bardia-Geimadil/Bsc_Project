#include <SoftwareSerial.h>


#define USE_ARDUINO_INTERRUPTS true    // Set-up low-level interrupts for most acurate BPM math.
#include <PulseSensorPlayground.h>     // Includes the PulseSensorPlayground Library.   

//  Variables
const int PulseWire = 0;       // PulseSensor PURPLE WIRE connected to ANALOG PIN 0
const int LED13 = 13;          // The on-board Arduino LED, close to PIN 13.
int Threshold = 527;           // Determine which Signal to "count as a beat" and which to ignore.
                               // Use the "Gettting Started Project" to fine-tune Threshold Value beyond default setting.
                               // Otherwise leave the default "550" value. 
                               
PulseSensorPlayground pulseSensor;  // Creates an instance of the PulseSensorPlayground object called "pulseSensor"



SoftwareSerial MyBlue(2, 3); // RX | TX 
byte flag = 0; 
int LED = 8; 

void setup() 
{   

  Serial.begin(9600); 
  MyBlue.begin(9600); 
  pinMode(LED, OUTPUT); 
  Serial.println("Ready to connect\nDefualt password is 1234 or 000");

   pulseSensor.analogInput(PulseWire);   
  pulseSensor.blinkOnPulse(LED13);       //auto-magically blink Arduino's LED with heartbeat.
  pulseSensor.setThreshold(Threshold);   


   if (pulseSensor.begin()) {
    Serial.println("We created a pulseSensor Object !");  //This prints one time at Arduino power-up,  or on Arduino reset.  
  }
} 


  int default_bpm = 80;
  String bpm = "80";

void loop() 
{ 
  if (MyBlue.available()) {
    flag = MyBlue.read(); 
  }

  bpm = "80";


  int myBPM = pulseSensor.getBeatsPerMinute();  
   

    if (pulseSensor.sawStartOfBeat()) {            // Constantly test to see if "a beat happened". 
       // Serial.println("♥  A HeartBeat Happened ! "); // If test is "true", print a message "a heartbeat happened".
       // Serial.print("BPM: ");                        // Print phrase "BPM: " 
        //Serial.println(myBPM);                        // Print the value inside of myBPM.   
        if(myBPM<100)
        {
          bpm = "0"  + String(myBPM);
        }
        else
        {
          bpm = String(myBPM);
        }
        

        Serial.println(bpm);
        
        MyBlue.print(bpm);      
    }


    Serial.println("-------------------------------------------");

    delay(200);

} 
