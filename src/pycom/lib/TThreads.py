import machine
import time
import _thread
import utime
from machine import Pin, ADC
import mpu6050 as mpu



class Threads:
    _vibrations = False
    _acceleration = False
    _threads_started = False
    

    def vib_data(self):
        history = []
        history_sum = 0
        VibSensorPin = 'P16' # sensor connected to P16. Valid pins are P13 to P20.
        Pin(VibSensorPin, mode=Pin.IN)  # set up pin mode to input
        adc = ADC(bits=12)             # create an ADC object bits=12 means range 0-4095 the higher value the more vibration detected, the higher bits value means that there are more possible combinations.
        apin = adc.channel(attn=ADC.ATTN_11DB, pin=VibSensorPin)
        while True:   
            ddd = apin()
            if ddd > 150:
                print(str(ddd))
                time_start = utime.ticks_ms()
                time_diff = 0
                while time_diff < 10000:
                    time.sleep(0.01)
                    time_diff = utime.ticks_ms() - time_start
                    result = apin()
                    if result > 350:
                        print(str(result))
                        history.append(result)
                history_sum = sum(history)
                print("Length:", len(history))

                if len(history) > 30:
                    self._vibrations = True
                    print("Something might have happened", history_sum)
                history = []
                history_sum = 0


    def gyr_data(self):
        i2c = mpu.I2C()
        accelerometer = mpu.accel(i2c)
        value = 0
        fall = False
        while True:
            if mpu.ac(value) < 0.2 and fall == False:
                fall = mpu.check_ac(500, 2)
                print("Detected fall")      

            if fall == True:
                time.sleep(2)
                fall = mpu.check_gy(10000, 30)
                print("waiting 10 seconds")
            
            if fall == True:
                print("We have a fall!")
                self._acceleration = True

            fall = False


    def start_threads(self):
        print("\nInitialized thread class")
        self._threads_started = True
        _thread.start_new_thread(self.vib_data, ())
        _thread.start_new_thread(self.gyr_data, ())