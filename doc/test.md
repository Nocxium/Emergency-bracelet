## Testing of components

### Vibrationssensor
The vibrationssensor will detect if there is any movement going on and every movement is presented with a value indicating how strong the movement is. Specifically this means that it will check how much something is vibrating. To test this so that it works for a seizure we have tried to recreate an epilectical seizure. Although this is impossible because noone in the group fortunately does not have any form of epilepsy this way was the only way to test (manually shaking). We noticed that the values we were getting are very different and does not seem reliable at all therefore we changed the plan with this sensor. To explain it we can assume that a value of 350 or above is high. Therefore we set a threeshold in the code so that if we get a value over 350 we will start a timer for ten seconds to see how many values over 350 we get in that span of time. For our manual testing of a seizure we came to a conclusion that if we get a value from the sensor over 350 more than 30 times this should by our measurements be a seizure. Notice that instead of measuring the values we instead measured the length of the values over the threshold. By doing it like this we have gotten much better results that is more accurate. We did a lot of testing only looking at the values from the pin but quickly noticed that it is very unreliable. Values were different depending on who was doing the manual shake which rings a warning. Even though we tested and are using the length method instead we can not guarantee that this is 100% accurate obviously because we do not have these seizures normally. This is what the text above looks like in code: 
```        
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
            print("Something might have happened", history_sum)
            lora_send(1)

```


### LoRa
In our case testing LoRa is easy because of the fact that we will only send something to LoRa if there is an emergency. Therefore we test LoRa by sending any bytes and see if it will be sent. With this easy setup we never had a problem when testing and it's gone very smooth. However some of us that live far away from the university are not able to send anything. 

We will send a 1 when there might have been an emergency. If a 1 has been sent we will through the application get a notification.

## Powerbank
We have unfortunately not been able to test the durability of the Powerbank because of quarantine and components being on with different people.

## Full package
As stated above we only were able to see eachother again as of 2022-01-11 which means we haven't been able to test the whole unit to much. We have however tested it by falling, vibrating (shaking) and also clicking the emergency button. Everything works as intented however we had to change one thing and that being the calibration of the fall sensor. When the fallsensor is around the bracelet the falls will give values much softer then if we just drop the bracelet. This caused us to lower the threshold for a fall and now it works as intended. This was also expected 