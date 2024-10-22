import time
from TThreads import Threads
import socket
import ubinascii
import ustruct
from loraLib import LORA
import utime
from machine import Pin
    



button = Pin('P11', mode=Pin.IN, pull=None)
count = 0


SOCKET = None

#Function to sending info via LoRa

def send_float(temp):
    global SOCKET

    packet = ustruct.pack('f', temp)

    if SOCKET is None:
        SOCKET = socket.socket(socket.AF_LORA, socket.SOCK_RAW)

    SOCKET.setsockopt(socket.SOL_LORA, socket.SO_DR, 0)

    SOCKET.setblocking(True)

    SOCKET.send(packet)

    SOCKET.setblocking(False)
    
    print("data sent")

#Set up LoRa

app_eui = ubinascii.unhexlify('0000000000000000')
app_key = ubinascii.unhexlify('26B527ACD79B9317E115E35DDE23088D')
dev_eui = ubinascii.unhexlify('70B3D57ED004B37B')

lora = LORA(app_key, app_eui, dev_eui)

threads = Threads()
time_start = 0
while True:
    try:
        time_diff = utime.ticks_ms() - time_start
        if not lora._LORA.has_joined():
            print("\nNot connected, connecting...")
            lora.lora_join()

        if not threads._threads_started:
            threads.start_threads()

        if time_diff > 61000:
            if threads._vibrations or threads._acceleration:    # Check if either of the booleans are True. If they are, it means either the
                time_start = utime.ticks_ms()                   # vib sensor or fall sensor has sent us a warning signal. We then send our emergancy signal here.
                print(str(time_start))
                print(str(threads._vibrations))
                print(str(threads._acceleration))
                print("SENT")
                send_float(1)
                threads._acceleration = False
                threads._vibrations = False
            elif button.value() == 1:                           # Checks if button is being pressed, if so then executes the following code 
                for i in range(3):                              # Check if button is being held for 3 seconds
                    if button.value() == 1:
                        count +=1
                        time.sleep(1)
            if count == 3:                                      # If the above requirement are met, send alarm.
                print("emergency")
                send_float(1)
            count = 0

    except Exception as e:
        print(str(e))
    time.sleep(0.1)