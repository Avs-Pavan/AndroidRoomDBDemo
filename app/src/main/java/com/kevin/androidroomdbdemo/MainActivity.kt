package com.kevin.androidroomdbdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.kevin.androidroomdbdemo.databinding.ActivityMainBinding
import com.kevin.androidroomdbdemo.extensions.hide
import com.kevin.androidroomdbdemo.extensions.show
import com.kevin.androidroomdbdemo.extensions.textInt
import com.kevin.androidroomdbdemo.extensions.textString
import com.kevin.androidroomdbdemo.extensions.validate
import com.kevin.androidroomdbdemo.recyclerview.PersonAdapter
import com.kevin.androidroomdbdemo.room.database.PersonDataBase
import com.kevin.androidroomdbdemo.room.entity.Person
import com.kevin.androidroomdbdemo.recyclerview.RecyclerListener
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    // Person being edited
    private var editPerson: Person? = null
    // Action mode flag
    var isActionMode = false
    // Selected items count
    var count = 0
    // Edit mode flag
    private var isEditMode = false
    // Person list
    private val personList = mutableListOf<Person>()
    // Selected person list
    private val selectedPersonList = mutableListOf<Person>()
    // Adapter instance
    private lateinit var mAdapter: PersonAdapter

    // View Binding
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // Database instance using lazy initialization
    // destructive migration is enabled
    private val db: PersonDataBase by lazy {
        Room.databaseBuilder(this.applicationContext,
            PersonDataBase::class.java,
            "person_db")
            .fallbackToDestructiveMigration()
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Initialize views
        initViews()

    }

    private fun initViews() {

        // Initialize adapter
        mAdapter = PersonAdapter(personList, isActionMode, object : RecyclerListener {
            override fun onLongPress(item: Int) {
                // Start action mode if not started when long pressed
                if (!isActionMode) {
                    isActionMode = true
                    mAdapter.setActionMode(this@MainActivity.isActionMode)
                    // Show toolbar
                    binding.toolbar.visibility = android.view.View.VISIBLE
                }
            }

            override fun onClick(item: Int, position: Int) {

                // If action mode is started, add or remove the selected item
                if (isActionMode) {
                    if (selectedPersonList.contains(personList[position])) {
                        selectedPersonList.remove(personList[position])
                        count--
                        binding.count.text = "$count selected"
                        if (count < 1) {
                            binding.delete.hide()
                        } else
                            binding.delete.show()
                    } else {
                        selectedPersonList.add(personList[position])
                        count++
                        binding.count.text = "$count selected"
                        if (count > 0) {
                            binding.delete.show()
                        } else
                            binding.delete.hide()
                    }
                } else {
                    // start edit action
                    startEditAction(personList[position])
                }

            }

        })

        // Set adapter to recyclerview
        binding.recyclerView.adapter = mAdapter

        // Restore list from db
        restoreList()

        // cancel action mode when back button is pressed
        binding.backBtn.setOnClickListener {
            isActionMode = false
            mAdapter.setActionMode(isActionMode)
            binding.toolbar.hide()
            count = 0
            selectedPersonList.clear()
            binding.count.text = "$count selected"
            binding.delete.hide()
        }

        // Submit button click listener
        binding.submitBtn.setOnClickListener {

            // Validate fields
            if (binding.nameEt.validate() && binding.ageEt.validate() && binding.addressEt.validate() && binding.professionEt.validate()) {
                val name = binding.nameEt.textString()
                val age = binding.ageEt.textInt()
                val profession = binding.professionEt.textString()
                val address = binding.addressEt.textString()

                // If not in edit mode, add person to db
                if (!isEditMode) {
                    val person = Person(0, age, name, profession, address)
                    addPerson(person)
                    clearFields()
                } else {
                    // If in edit mode, update the person
                    editPerson?.let {
                        it.name = name
                        it.age = age
                        it.profession = profession
                        it.address = address

                        // Update the person in db
                        lifecycleScope.launch {
                            val rows = db.personDao().update(person = it)
                            // check if rows updated is greater than 0
                            if (rows > 0) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Person updated successfully",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                mAdapter.notifyItemChanged(personList.indexOf(it))
                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Failed to update person",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    }
                    clearFields()
                    binding.cancelBtn.hide()
                    binding.submitBtn.text = "Submit"
                    isEditMode = false
                }
            }

        }

        binding.delete.setOnClickListener {
            if (selectedPersonList.isNotEmpty()) {
                // Delete selected persons from db
                lifecycleScope.launch {
                    var deletedCount = 0
                    selectedPersonList.forEach { person ->
                        deletedCount += db.personDao().delete(person)
                    }

                    // Check if all selected persons were deleted
                    if (deletedCount == selectedPersonList.size) {
                        Toast.makeText(
                            this@MainActivity,
                            "Data deleted successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Restore list from db
                        restoreList()
                        isActionMode = false
                        mAdapter.setActionMode(isActionMode)

                        binding.toolbar.hide()
                        count = 0
                        selectedPersonList.clear()
                        binding.count.text = "$count selected"
                        binding.delete.hide()
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Failed to delete data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        // Cancel button click listener to cancel edit mode
        binding.cancelBtn.setOnClickListener {
            // Clear fields
            clearFields()
            binding.submitBtn.text = "Submit"
            binding.cancelBtn.hide()
            editPerson = null
            isEditMode = false
        }
    }

    // Start edit action
    private fun startEditAction(person: Person) {
        isEditMode = true
        this.editPerson = person
        binding.submitBtn.text = "Update"
        binding.nameEt.setText(person.name)
        binding.ageEt.setText(person.age.toString())
        binding.professionEt.setText(person.profession)
        binding.addressEt.setText(person.address)
        binding.cancelBtn.show()
    }

    // Restore list from db
    private fun restoreList() {
        personList.clear()
        lifecycleScope.launch {
            personList.addAll(db.personDao().getAll())
            mAdapter.notifyDataSetChanged()
        }
    }

    // Clear fields
    private fun clearFields() {
        binding.nameEt.setText("")
        binding.ageEt.setText("")
        binding.professionEt.setText("")
        binding.addressEt.setText("")
    }

    // Add person to db
    private fun addPerson(person: Person) {
        // Add the person to the db
        lifecycleScope.launch {
            val id = db.personDao().insert(person)
            if (id > 0) {
                Toast.makeText(this@MainActivity, "Data inserted successfully", Toast.LENGTH_SHORT)
                    .show()
                personList.add(person)
                mAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this@MainActivity, "Failed to insert data", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}

