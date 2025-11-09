import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import android.os.Handler
import android.os.Looper

object PetManager {

    // ชื่อไฟล์ที่จะใช้เก็บข้อมูล
    private const val PREF_NAME = "PeanutPrefs"

    // Key สำหรับเก็บค่าต่าง ๆ
    private const val KEY_NAME = "PET_NAME"
    private const val KEY_HAPPINESS = "PET_HAPPINESS"
    private const val KEY_HUNGER = "PET_HUNGER"
    private const val KEY_ENERGY = "PET_ENERGY"

    // Check game over
    val onGameOver = MutableLiveData<Boolean>()
    private const val DECAY_INTERVAL = 30000L //เวลาลด
    private val timerHandler = Handler(Looper.getMainLooper()) //แจ้งเตือนเวลาครบลูป
    // ค่าเริ่มต้น
    private const val DEFAULT_STAT = 50

    private lateinit var prefs: SharedPreferences

    // ฟังก์ชันนี้ต้องถูกเรียก "ครั้งเดียว" ตอนเปิดแอป
    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // ใช้ .apply() เพื่อ save ข้อมูล
    private fun saveInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    private fun saveString(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    // ตัวแปรสำหรับดึง/บันทึกค่าพลัง
    // เราใช้ custom getter/setter เพื่อให้มันบันทึกอัตโนมัติ
    var petName: String
        get() = prefs.getString(KEY_NAME, "Peanut") ?: "Peanut"
        set(value) = saveString(KEY_NAME, value)

    var happiness: Int
        get() = prefs.getInt(KEY_HAPPINESS, DEFAULT_STAT)
        set(value) {
            val newValue = value.coerceIn(0, 100)
            saveInt(KEY_HAPPINESS, newValue) // บังคับให้อยู่ระหว่าง 0-100
            if (newValue <= 0) {
                onGameOver.postValue(true) // gameover
                stopDecayTimer()
            }
        }


    var hunger: Int
        get() = prefs.getInt(KEY_HUNGER, DEFAULT_STAT)
        set(value) {
            val newValue = value.coerceIn(0, 100)
            saveInt(KEY_HUNGER, newValue)
            if (newValue <= 0) {
                onGameOver.postValue(true)
                stopDecayTimer()
            }
        }

    var energy: Int
        get() = prefs.getInt(KEY_ENERGY, DEFAULT_STAT)
        set(value) {
            val newValue= value.coerceIn(0, 100)
            saveInt(KEY_ENERGY, newValue)
            if (newValue <= 0) {
                onGameOver.postValue(true)
                stopDecayTimer()
            }
        }

    private val statDecayRunnable = object : Runnable {
        //ลดค่าพลังทุก 30 วิ
        override fun run() {
            hunger -= 1
            happiness -= 1
            energy -= 2
            timerHandler.postDelayed(this, DECAY_INTERVAL)
        }
    }
    fun startDecayTimer() { //เริ่มนับ
        timerHandler.removeCallbacks(statDecayRunnable)
        timerHandler.postDelayed(statDecayRunnable, DECAY_INTERVAL)
    }

    fun stopDecayTimer() { //หยุดนับ
        timerHandler.removeCallbacks(statDecayRunnable)
    }

    // ฟังก์ชันสำหรับรีเซ็ตค่า เมื่อเริ่มเกมใหม่
    fun startNewGame(name: String) {
        petName = name
        happiness = DEFAULT_STAT
        hunger = DEFAULT_STAT
        energy = DEFAULT_STAT

        startDecayTimer()
    }
}