package com.kevin.androidroomdbdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.kevin.androidroomdbdemo.databinding.ActivityMainBinding
import com.kevin.androidroomdbdemo.room.database.PersonDataBase
import com.kevin.androidroomdbdemo.room.entity.Person
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val db: PersonDataBase by lazy {
        Room.databaseBuilder(this.applicationContext, PersonDataBase::class.java, "person_db")
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()

    }

    private fun initViews() {
        binding.submitBtn.setOnClickListener {

            if (binding.nameEt.validate() && binding.ageEt.validate() && binding.addressEt.validate() && binding.professionEt.validate()) {
                val name = binding.nameEt.textString()
                val age = binding.ageEt.textInt()
                val profession = binding.professionEt.textString()
                val address = binding.addressEt.textString()

                val person = Person(0, age, name, profession, address)
                lifecycleScope.launch{
                    db.personDao().insert(person)
                }
            }
        }
    }


}