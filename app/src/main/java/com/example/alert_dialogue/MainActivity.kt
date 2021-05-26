package com.example.alert_dialogue

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    var cityData = mutableListOf<State>()
    lateinit var stateAdapter : StateAdapter
    var flag = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val reset = findViewById<Button>(R.id.reset)
        val add = findViewById<Button>(R.id.add)

        reset.setOnClickListener{
            resetList()
        }

        add.setOnClickListener{
            addState()
            if(flag == false){
                Toast.makeText(this, "Invalid Details!!", Toast.LENGTH_LONG).show()
            }
            flag = true;
        }

        loadStates()

        val states = findViewById<ListView>(R.id.state_list)
        stateAdapter = StateAdapter(cityData)
        states.adapter = stateAdapter
    }

    private fun resetList(){
        val builder = AlertDialog.Builder(this)
        with(builder){
            setTitle(getString(R.string.confirm_reset))
            setMessage(getString(R.string.reset_message))

            setPositiveButton(getString(R.string.yes)
            ) { dialog, which ->
                cityData.clear()
                saveCities()
            }

            setNegativeButton("No"
            ) { dialog, which -> }

            val alertDialog = builder.create()
            alertDialog.show()
        }
    }

    private fun saveCities(){
        val gson = Gson()
        val cities = cityData.map { gson.toJson(it) }

        val sharedPreference = getPreferences(Context.MODE_PRIVATE)
        with((sharedPreference.edit())){
            putStringSet("ABC", cities.toSet())
            commit()
        }

        stateAdapter.notifyDataSetChanged()
    }

    private fun loadStates(){
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        val states = sharedPref.getStringSet("ABC", null)
        val gson = Gson()
        states?.forEach{
            cityData.add(gson.fromJson(it, State :: class.java))
        }

        cityData.sortBy { it.state_name }
    }

    private fun addState(){
        val builder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.state_add, null)
        val state_name = dialogView.findViewById<TextView>(R.id.state_name)
        val country_name = dialogView.findViewById<TextView>(R.id.country_name)

        setErrorListener(state_name)
        setErrorListener(country_name)

        with(builder){
            setView(dialogView)
            setTitle("Add City")
            setPositiveButton("ADD", object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    val state = state_name.text.toString()
                    val country = country_name.text.toString()
                    if(country.isNotBlank() && state.isNotBlank()){
                        cityData.add(State(state, country))
                        cityData.sortBy { it.state_name }
                        saveCities()
                    }
                    else{
                        flag = false
                    }
                }
            })
            setNegativeButton("Cancel", object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                }
            })
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun setErrorListener(editText : TextView){
        editText.error = if(editText.text.toString().isNotEmpty()) null else "Field Cannot be Empty"
        editText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editText.error = if(editText.text.toString().isNotEmpty()) null else "Field Cannot be Empty"
            }
        })
    }
}