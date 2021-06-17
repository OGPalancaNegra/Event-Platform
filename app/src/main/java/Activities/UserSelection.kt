package Activities

import Login.eoLogin
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.eventplatform.R

class UserSelection: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_selection_layout)


        @Suppress("DEPRECATION")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }


        val eg_button = findViewById<Button>(R.id.event_goer_button)

        val eo_button = findViewById<Button>(R.id.event_organizer_button)

        eg_button.setOnClickListener {

            val intent = Intent(this, egLogin::class.java)
            startActivity(intent)
        }

        eo_button.setOnClickListener {
            val intent = Intent(this, eoLogin::class.java)
            startActivity(intent)
        }

    }

    


}