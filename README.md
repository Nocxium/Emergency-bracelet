## Project Title:

### Information:

This was a school project made by me and three other members back in 2021
### Powerpoint presentation
<a href="https://docs.google.com/presentation/d/12cy1bKWnt8Tml3p7kkejRTj0_9LRWfaZkk9XWHpPL2Q/edit?usp=sharing">Emergency bracelet presentation </a>

### Abstract
 This projects aim is to provide a device that mainly can monitor if a person is in danger in form of a potential fall accident or a possible medical seizure. This IoT-device is in the form of an bracelet that is thought to be worn by the user and in case of emergency a message will be sent to example relatives of the user so that they can either contact the user or ultimately SOS.
### Background
The idea of making an emergency bracelet arose when we studied different kind of health problems in Sweden. When doing this we clearly noticed that there is a health problem that we could make a project about. This being the health problem of mainly elderly people falling. Swedens public health authority (Folkhälsomyndigheten) declares falling as a public health concern. They also reveal statistics which we found shocking. A total of 111667 people over 65 years have been hospitalized due to a falling accident in 2019. With this in mind we decided to try to make a difference by creating our emergency bracelet.

## Idea
In this section we will present briefly how we aim to make our emergency bracelet a reality. There will be no in depth explaining of the technical functions however our idea will be explained in words. 

Ideally the emergency bracelet is thought to mainly detect a potential fall but it also has features that includes detection of some medical seizures. When we type seizures we are mainly focusing on epilectical seizures. The idea is to achieve this by using two sensors, that being a 3-axis gyro accelerometer to detect fall and also a vibrationssensor for seizures. Ideally if any of the values from the sensors are abnormal we will by the help of an app notify relatives/related persons to the user. <br />

The fall sensor will work as such that it will firstly check if a fall has happened. If theres been a fall it will check for movements the next ten seconds. If no movements registers the message will be sent to relatives because of a possible faint. If the person does move after the fall has happened but can't get up again the user has the possibility to click on a button connected to the bracelet to manually notify quickly that something has happened. 

For the seizures the vibrationssensor will work in a way so that it will measure if the user is vibrating (shaking) and how strong those shakes are. If the shaking is long and the values from the sensor are high then there might be a seizure. This will therefore also notify relatives in case of emergency.

### Method

<a href="https://gitlab.lnu.se/1dt308/student/grupp-12---emergency-bracelet/project/-/blob/main/doc/hardware.md">Hardware</a>

<a href="https://gitlab.lnu.se/1dt308/student/grupp-12---emergency-bracelet/project/-/blob/main/doc/requirements.md">Requirements</a>

<a href="https://gitlab.lnu.se/1dt308/student/grupp-12---emergency-bracelet/project/-/blob/main/doc/setup.md">Setup</a>

<a href="https://gitlab.lnu.se/1dt308/student/grupp-12---emergency-bracelet/project/-/blob/main/doc/test.md">Test</a>

<a href="https://gitlab.lnu.se/1dt308/student/grupp-12---emergency-bracelet/project/-/blob/main/doc/timelog.md">Timelog</a>


### Results

## 3-Axis gyro accelerometer (MPU6050)

This was the sensor we chose to work with to detect falls. We had previously been working with another sensor that could detect the alitude with the help of pressure. Unfortunately that sensor did not work as advertised. It was supposed to have a inaccuracy of 8-17 CM, but ended up with an inaccuracy of approximately 1,5 meters.

The way we setup the MPU6050 was with calculating the magnitude of X Y Z (3-axis) so we could just get a vector and its size. With this information we could now make a code that could detect a fall, and then a fast change in the value when the person/sensor hit the floor. We decided that the sensor will only send an alarm if the person is not moving at all in 10 seconds.
If the person is able to move, they can press the button instead.

<img src="https://cdn.discordapp.com/attachments/927503185441087489/931569427869237349/mpu.jpeg" width="400"/>

--- 

## Vibration sensor (qungi sw-420)

The sensors sensitivty can be adjusted, but we noticed that the values the sensor gave to us while we were emulating a seizure, were very inconsistent. So we ditched our original idea of
calculating the values in a timeframe, and instead calculated the amount of times the sensor went above a threshold value we added (we got this value by testing). Now we appended these values that went above the threshold to a list within a timeframe, and later used the length of the list to determine whether a seizure accured or not.

<img src="https://cdn.discordapp.com/attachments/920795258160295990/931289764735385600/IMG_2010.jpg" width="400"/>

--- 

## Push Button

We added a simple pushbutton that send a emergency message by simply being held down for 3 seconds. This is there to give the user the ability to manually send a emergency signal if
they get themselves into any other dangerous situation. For example if a person falls, but isnt unconscious and can move, they can press the emergency situation since the fall sensor wont send a signal if the person is moving.

<img src="https://cdn.discordapp.com/attachments/920795258160295990/931281831729434694/IMG_0107.jpg" width="400"/>

---
## Sensor box, bracelet and the junctionbox
Our main goal with the bracelet from the start was to make it like a smartwatch with a heartrate monitor that detects falling and/or seizures but with our own sleek design.
To make this work we used the LoPy4 and two sensors with a antenna for connecting to the LoRa network.
At first our idea was to make our own silicon bracelet but due to lack of time and problems with amazon we bought a sport bracelet instead. Otherwise our result for the assembling of the project was exactly like the schematics that were made. If we could we would also had made it completely wireless but we did not have the resources or the skills to succesfully create a wireless connection to the sensors from the pycom. We wanted our bracelet to withstand rain and be waterproof which we succeded because of the rubber coat of the sport bracelet and the IP certified box that we had our pycom in. 

We used CAD and 3D-printed a box that contains all the hardware for the sensors including a button that you can press if you have an emergency that the sensors won't detect.
The sensor box is placed in a sport-bracelet that is waterproof and adjustable. From the box we connect the cables to the pycom that is placed in a waterproof IP-box with the antenna. We use a powerbank to sufice the pycom with power.

<img src="https://media.discordapp.net/attachments/920795258160295990/931194207949946890/IMG_1160.jpg?width=843&height=1123" width="400"/>

<img src="https://media.discordapp.net/attachments/920795258160295990/931281831729434694/IMG_0107.jpg?width=843&height=1123" width="400"/>

<img src="https://cdn.discordapp.com/attachments/920795258160295990/931155350713958410/Final_Pin.png" width="400"/>

---

## Lora
Our idéa with the project was of course to have it connected to the LoRa network 
From LoRa we connected to a MTTQ server where an app has been made so if the sensors detect something it will alert in the application that was made. 
Everything worked as intended with the connections with LoRa ,MTTQ and the application that we made.

---
## Mobile app
We had a plenty of options in order to notificate a user but we choose to use MQTT to transfer messages from TTN to our app. We build our app in Android Studio using Java language. Our main activity (the one that executes the first) executes our MQQT service and brings a page with notifications on foreground so the user can see. When MQQT service starts so trying we to connect to TTN's MQTT server. After succesfull connection to MQTT server, our app waits for a message. When we got a message it triggers a special callback function that handles the message and shows a notification in both app and notification panel so the user knows that there's something that might have happened.
<img src="https://gitlab.lnu.se/1dt308/student/grupp-12---emergency-bracelet/project/-/raw/main/img/notification.png" width="400"/>
<br>Picture above illustrates how does notifications look in notifications panel.</br>

<img src="https://gitlab.lnu.se/1dt308/student/grupp-12---emergency-bracelet/project/-/raw/main/img/notificationInApp.png" width="400"/>
<br>Picture above illustrates how does notifications look in the application.</br>
