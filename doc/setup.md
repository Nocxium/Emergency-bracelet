# Setup

- Clone the  <a href="https://gitlab.lnu.se/1dt308/student/grupp-12---emergency-bracelet/project">project</a>.

## Pycom

### Node.js
- Download and install node.js from node.js official website.

### Python
- Download and install python from python's official website.

### Visual Studio Code
- Click on <ins>*Extensions*</ins> and install Python, and Pymakr.

### The Things Network (TTN)

- Create an account on <a href="https://www.thethingsnetwork.org/">TTN's homepage</a>.

- Create a new application and click <ins>*register a new device*</ins>.

- Click on <ins>*try manual device registration*</ins>.

- Frequency plan. Choose corresponding frequency thats allowed in your region. Since we live in Sweden our choice is <ins>*Europe 863-870 MHz (SF9 for RX2)*</ins>.

- LoRaWAN version. In according to the official <a href="https://docs.pycom.io/firmwareapi/pycom/network/lora/">pycom docs</a> LoRa class utilizes LoRaWAN version 1.0.2, choose that. 

- Regional parameters version. Choose <ins>*PHY V1.0.2 REV A*</ins>.

- Generate DevEUI, AppEUI, AppKey and save it somewhere.

- Click <ins>*Register end device*</ins>.

### LoRa

- In order connect to the TTN with LoRa using your credentials, open `src` folder from recently cloned project, navigate to `pycom` folder and open `main.py`. 
- Change <ins>*app_eui*</ins>, <ins>*app_key*</ins> and <ins>*dev_eui*</ins> to the ones you saved during device registration.

```python
app_eui = ubinascii.unhexlify('0000000000000000')
app_key = ubinascii.unhexlify('26B527ACD79B9317E115E35DDE23088D')
dev_eui = ubinascii.unhexlify('70B3D57ED004B37B')

lora = LORA(app_key, app_eui, dev_eui)
```
## Mobile app (Android only)
- In order to be able to setup, download and install android application you have to:
    * Download and install Android Studio.
<!-- -->
- (For testing purposes only)
    * In Android Studio at the top right corner locate AVD manager menu.
    * In the newly opened window at the left bottom corner click on Create Virtual Device. 
    * Choose Pixel 5, click Next, then choose R(API level 30) as system image, then Next again and after that click on Finish.

### Android Studio
- Log on your application on TTN website. On the left corner click on <ins>*Integrations*</ins>, then choose <ins>*MQTT*</ins>. Copy public addres, username and password, save it somewhere.
<!-- -->
- Open Android Studio. In the project's hierarchy locate MqttConnectionManagerService file `(app/java/com.example.teeest/MqttConnectionManagerService)`.
<!-- -->
- Find serverUrl, password, username and change them to the ones you saved from your application's website. Change topic to 'v3/{ttn app id}@ttn/devices/{ttn device id}/up'. You can find both <ins>*ttn device id*</ins> and <ins>*ttn app id*</ins> on you applications website.
<!-- -->
- If you want to test it you can press 'Run app' button located at the top panel. If everything works correctly and you want to download compiled app on your mobile phone, follow these steps:
    * Open file explorer and locate mobile's app folder.
    * In the root folder of the app navigate by following path `app_root_folder\app\build\outputs\apk\debug`.
    * Copy app-debug.apk file on your phone and install it.


## Pycom code

- Connect your pycom device to the pc.
- Open folder `pycom`.
- Click `Upload project to device`.
