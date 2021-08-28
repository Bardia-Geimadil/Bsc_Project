#include <SoftwareSerial.h>


#define USE_ARDUINO_INTERRUPTS true    // Set-up low-level interrupts for most acurate BPM math.
#include <PulseSensorPlayground.h>     // Includes the PulseSensorPlayground Library.   

#include <Wire.h>
#include <Adafruit_Sensor.h> 
#include <Adafruit_ADXL345_U.h>

Adafruit_ADXL345_Unified accel = Adafruit_ADXL345_Unified();

//  Variables
const int PulseWire = 0;       // PulseSensor PURPLE WIRE connected to ANALOG PIN 0
const int LED13 = 13;          // The on-board Arduino LED, close to PIN 13.
int Threshold = 527;           // Determine which Signal to "count as a beat" and which to ignore.
                               // Use the "Gettting Started Project" to fine-tune Threshold Value beyond default setting.
                               // Otherwise leave the default "550" value. 
                               
PulseSensorPlayground pulseSensor;  // Creates an instance of the PulseSensorPlayground object called "pulseSensor"



SoftwareSerial MyBlue(2, 3); // RX | TX 
byte flag = 0; 

int buzzer = 8;
int button = 11;

int fall_threshold = 7;


void setup() 
{   

  pinMode(buzzer , OUTPUT);
  pinMode (button , INPUT_PULLUP);
    
  Serial.begin(9600); 
  MyBlue.begin(9600); 
  
  
  Serial.println("Ready to connect\nDefualt password is 1234 or 000");

   if(!accel.begin())
   {
      Serial.println("No ADXL345 sensor detected.");
      while(1);
   }
   else
   {
      Serial.println("ADXL345 sensor detected.");
   }

  pulseSensor.analogInput(PulseWire);   
  //pulseSensor.blinkOnPulse(LED13);       //auto-magically blink Arduino's LED with heartbeat.
  pulseSensor.setThreshold(Threshold);   


   if (pulseSensor.begin()) {
    Serial.println("We created a pulseSensor Object !");  //This prints one time at Arduino power-up,  or on Arduino reset.  
  }
} 


  int default_bpm = 80;
  String bpm = "80";


  int x_old=0 , y_old=0 , z_old=0;
  int x_new=0 , y_new =0 , z_new=0;

  bool call = false ; 
  bool first_time = true;

  int bz_cntr = 20;

  bool alarm = false;
  

void loop() 
{ 
  if (MyBlue.available() > 0 ) {
    fall_threshold = MyBlue.read(); 
    Serial.print("The Falling threshold is ........... "); 
    Serial.print(fall_threshold); 
  }




  bpm = "80";



   sensors_event_t event; 
   accel.getEvent(&event);

   x_new = event.acceleration.x;
   y_new = event.acceleration.y;
   z_new = event.acceleration.z;

  if(first_time)
  {
  
   
   x_old = x_new;
   y_old = y_new;
   z_old = z_new;
   
  }
      


   

//   Serial.print("X: "); Serial.print(event.acceleration.x); Serial.print("  ");
//   Serial.print("Y: "); Serial.print(event.acceleration.y); Serial.print("  ");
//   Serial.print("Z: "); Serial.print(event.acceleration.z); Serial.print("  ");
//   Serial.println("m/s^2 ");


  int myBPM = pulseSensor.getBeatsPerMinute();  
   

    if (pulseSensor.sawStartOfBeat()) {            // Constantly test to see if "a beat happened". 
//        Serial.println("♥  A HeartBeat Happened ! "); // If test is "true", print a message "a heartbeat happened".
//        Serial.print("BPM: ");                        // Print phrase "BPM: " 
//        Serial.println(myBPM);                        // Print the value inside of myBPM.   
        
        if(myBPM<100)
        {
          bpm = "0"  + String(myBPM);
        }
        else
        {
          bpm = String(myBPM);
        }
        

       // Serial.println(bpm);
        
            
    }


    Serial.println("-------------------------------------------");


      if( abs(x_new - x_old) > fall_threshold  || abs(y_new - y_old) > fall_threshold || abs(z_new - z_old) > fall_threshold ) // Checking for fall detection
      {
          Serial.println("fall detected");
          call = true;
      }


      if(myBPM > 1 && myBPM < 40) // checking for heartBeat problems
      { 
        Serial.println("heart problem detected");
        call = true;
      }

      if(digitalRead(button) == LOW) // cheking for manual alarm
      {

        Serial.println("manual alert detected");
        
        call = true;
      }


     if(call)
     {
        
        MyBlue.print("aaaaaaa");
       
        alarm = true;
        digitalWrite(buzzer , HIGH);
       
        call = false;
        
     }
     else{

        MyBlue.print(bpm); 
     }

     
 
     
    delay(300);

    x_old = x_new;
    y_old = y_new;
    z_old = z_new;
    
    first_time = false;


    if(alarm && bz_cntr > 0)
    {
        bz_cntr--;
    }

    if(bz_cntr == 0)
    {
      alarm = false;
      digitalWrite(buzzer , LOW);
      bz_cntr = 20;
    }
    

    

} 
