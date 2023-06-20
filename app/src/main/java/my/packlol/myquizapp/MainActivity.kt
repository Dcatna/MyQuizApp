package my.packlol.myquizapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val et_name : EditText = findViewById<EditText>(R.id.et_name)
        val btnStart : Button = findViewById<Button>(R.id.buttonStart)
        btnStart.setOnClickListener {
            if(et_name.text.isEmpty()) {
                Toast.makeText(this, "Please Enter Your Name", Toast.LENGTH_LONG).show()
            }else{
                val intent = Intent(this, QuizQuestionActivity::class.java)//moves us to the next activity
                intent.putExtra(Constants.USER_NAME, et_name.text.toString())
                startActivity(intent)//needs to actually start
                finish()//need to close the main activity window
            }
        }
    }
}