package com.example.restaurantdesignxml

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun goToActivity2(view: View){
        val intent=Intent(this@MainActivity,MainActivity2::class.java)
        startActivity(intent)
    }
}

import RPi.GPIO as GPIO
import time
from RPLCD.i2c import CharLCD

# --- GPIO SETUP ---
SENSOR_PIN = 17
GREEN_LED = 22
RED_LED = 27
BUZZER = 23

GPIO.setmode(GPIO.BCM)
GPIO.setup(SENSOR_PIN, GPIO.IN)
GPIO.setup(GREEN_LED, GPIO.OUT)
GPIO.setup(RED_LED, GPIO.OUT)
GPIO.setup(BUZZER, GPIO.OUT)

# --- LCD SETUP ---
lcd = CharLCD(
    i2c_expander='PCF8574',
    address=0x27,
    port=1,
    cols=16,
    rows=2,
    charmap='A00'
)

lcd.clear()

try:
    while True:
        air_status = GPIO.input(SENSOR_PIN)

        lcd.clear()

        if air_status == 0:
            # HAVA TEMİZ
            GPIO.output(GREEN_LED, GPIO.HIGH)
            GPIO.output(RED_LED, GPIO.LOW)
            GPIO.output(BUZZER, GPIO.LOW)

            lcd.write_string("Hava Temiz")
            lcd.cursor_pos = (1, 0)
            lcd.write_string("Durum: IYI")

        else:
            # HAVA KIRLI
            GPIO.output(GREEN_LED, GPIO.LOW)
            GPIO.output(RED_LED, GPIO.HIGH)
            GPIO.output(BUZZER, GPIO.HIGH)

            lcd.write_string("Hava KIRLI!")
            lcd.cursor_pos = (1, 0)
            lcd.write_string("UYARI!!!")

        time.sleep(1)

except KeyboardInterrupt:
    print("Cikis yapiliyor...")

finally:
    lcd.clear()
    GPIO.cleanup()
