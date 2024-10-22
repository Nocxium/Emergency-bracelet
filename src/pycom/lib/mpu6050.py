import machine
from machine import I2C, Pin
import math
import utime

# The first class "accel" in this code, is a modified version of NielsOerbaeks code from his github https://github.com/NielsOerbaek/MPU6050-LoPy4
# We have asked him for his permission to modify his class and use it in our project, which he was okay with.

class accel():
    def __init__(self, i2c, addr=0x68):
        self.iic = i2c
        self.addr = addr
        self.iic.writeto(self.addr, bytearray([107, 0]))

    def get_raw_values(self):
        a = self.iic.readfrom_mem(self.addr, 0x3B, 14)
        return a

    def get_ints(self):
        b = self.get_raw_values()
        c = []
        for i in b:
            c.append(i)
        return c

    def bytes_toint(self, firstbyte, secondbyte):
        if not firstbyte & 0x80:
            return firstbyte << 8 | secondbyte
        return - (((firstbyte ^ 255) << 8) | (secondbyte ^ 255) + 1)

    def get_values(self):
        raw_ints = self.get_raw_values()
        vals = {}
        AFS = 16384 # https://www.haoyuelectronics.com/Attachment/GY-521/mpu6050.pdf This is the default AFS
        FS = 131 # https://www.haoyuelectronics.com/Attachment/GY-521/mpu6050.pdf This is the default FS

        vals["AcX"] = self.bytes_toint(raw_ints[0], raw_ints[1]) / AFS # Number will be in force (g)
        vals["AcY"] = self.bytes_toint(raw_ints[2], raw_ints[3]) / AFS # Number will be in force (g)
        vals["AcZ"] = self.bytes_toint(raw_ints[4], raw_ints[5]) / AFS # Number will be in force (g)
        vals["Tmp"] = self.bytes_toint(raw_ints[6], raw_ints[7]) / 340.00 + 36.53
        vals["GyX"] = self.bytes_toint(raw_ints[8], raw_ints[9]) / FS # Number will be in degree/s (°/s)
        vals["GyY"] = self.bytes_toint(raw_ints[10], raw_ints[11]) / FS # Number will be in degree/s (°/s)
        vals["GyZ"] = self.bytes_toint(raw_ints[12], raw_ints[13]) / FS # Number will be in degree/s (°/s)
        return vals  # returned in range of Int16
        # -32768 to 32767

    
def ac(value):
    vals = accelerometer.get_values()
    # Magnitude is given by a = sqrt(X^2 + Y^2 + Z^2)
    value = math.sqrt((vals["AcX"]**2)+(vals["AcY"]**2)+(vals["AcZ"]**2)) # The Magnitude of Ac X Y Z
    return value
    
    
def gy(value):
    vals = accelerometer.get_values()
    # Magnitude is given by a = sqrt(X^2 + Y^2 + Z^2)
    value = math.sqrt((vals["GyX"]**2)+(vals["GyY"]**2)+(vals["GyZ"]**2)) # The Magnitude of Gy X Y Z
    return value

def check_ac(time, threshold_ac):         # The amount of time for loop, The threshold of ac value, Artifical value we use to assign a value to fall
	time_start = utime.ticks_ms()
	time_diff = 0
	while time_diff < time:
		time_diff = utime.ticks_ms() - time_start
		if ac(current_val) > threshold_ac:
			return True
	return False


def check_gy(time, threshold_gy):         # The amount of time for loop, The threshold of gy value, Artifical value we use to assign a value to fall
	time_start = utime.ticks_ms()
	time_diff = 0
	while time_diff < time:
		time_diff = utime.ticks_ms() - time_start
		if gy(current_val) > threshold_gy:
			return False
	return True


i2c = I2C()
accelerometer = accel(i2c)
current_val = 0
