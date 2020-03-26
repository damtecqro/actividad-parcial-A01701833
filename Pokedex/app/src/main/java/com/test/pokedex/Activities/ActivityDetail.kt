package com.test.pokedex.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.koushikdutta.ion.Ion
import com.test.pokedex.R
import kotlinx.android.synthetic.main.activity_detail.*

class ActivityDetail: AppCompatActivity() {

    //Declaracion de variables para obtener datos del detail del pokemon
    private lateinit var nombre: TextView
    private lateinit var numero: TextView
    private lateinit var tipo: TextView
    private lateinit var stats: TextView
    private lateinit var movements: TextView

    //Declaracion de variable no nula url
    var url : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        nombre = findViewById(R.id.pokemon_name)
        numero = findViewById(R.id.pokemon_number)
        tipo = findViewById(R.id.pokemon_types)
        stats = findViewById(R.id.pokemon_stats)
        movements = findViewById(R.id.pokemon_moves)

        val bundle :Bundle ?=intent.extras
        url = bundle!!.getString("url").toString()

        pokeStart()


    }

    fun pokeStart(){

        Ion.with(this)
            .load(url)
            .asJsonObject()
            .done { e, result ->
                if(e == null){
                    nombre.text = result.get("name").toString().replace("\"","").capitalize()
                    numero.text = result.get("id").toString()
                    tipo.text = getTipos(result.get("types").asJsonArray)
                    stats.text = getStats(result.get("stats").asJsonArray)
                    movements.text = getMovements(result.get("moves").asJsonArray)

                    if(!result.get("sprites").isJsonNull){
                        if(result.get("sprites").asJsonObject.get("front_default") != null){
                            Log.i("Res", result.get("sprites").asJsonObject.get("front_default").asString)

                            Glide
                                .with(this)
                                .load(result.get("sprites").asJsonObject.get("front_default").asString)
                                .placeholder(R.drawable.pokemon_logo_min)
                                .error(R.drawable.pokemon_logo_min)
                                .into(pokemon_image)
                        }else{
                            pokemon_image.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.pokemon_logo_min))
                        }
                    }else{
                        pokemon_image.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.pokemon_logo_min))
                    }
                }
            }
    }
 // FUNCION DE MOVIMIENTOS
    fun getMovements(a: JsonArray): String{
        var mset: String = ""

        for(i in a){
            var x: JsonObject = i.asJsonObject.getAsJsonObject("move")
            mset = mset + x.get("name").asString.capitalize() + "\n"
        }

        return mset
    }
    // FUNCION DE STATS
    fun getStats(a: JsonArray): String{
        var stats: String = ""

        //Loop obtener nombre del stat y valor
        for(i in a){
            val stat = i.asJsonObject.get("base_stat").asString
            val nstat: JsonObject = i.asJsonObject.getAsJsonObject("stat")

            stats = stats + nstat.get("name").asString.capitalize() + ": " + stat + "\n"
        }

        return stats
    }
    // FUNCION DE TIPOS
    fun getTipos(a: JsonArray): String{

        var tipos: String = ""


        for(i in a){
            var x: JsonObject = i.asJsonObject.getAsJsonObject("type")
            tipos = tipos + x.get("name").asString.capitalize() + " \n"
        }

        return tipos
    }







}