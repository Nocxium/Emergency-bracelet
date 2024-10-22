from network import LoRa
import time
import pycom

class LORA:
    _app_eui = None
    _app_key = None
    _dev_eui = None
    _LORA = None

    def __init__(self, app_key, app_eui, dev_eui):
        global _LORA

        self._app_key = app_key
        self._app_eui = app_eui
        self._dev_eui = dev_eui
        self._LORA = LoRa(mode=LoRa.LORAWAN, region=LoRa.EU868)
        

    def lora_join(self):
        global _LORA

        self._LORA.join(activation=LoRa.OTAA, auth=(self._dev_eui, self._app_eui, self._app_key), timeout=0)

        while not self._LORA.has_joined():
            time.sleep(2.5)
            #pycom.rgbled(0x7f0000)
            print('Not yet joined...')

        print('Joined')
        #pycom.rgbled(0x007f00)
        time.sleep(5)