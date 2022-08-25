package com.example.bebagua

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.bebagua.model.CalcularIngestaoDiaria
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var edit_peso: EditText
    private lateinit var edit_idade: EditText
    private lateinit var bt_calcular: Button
    private lateinit var txt_resultado: TextView
    private lateinit var ic_redefinir_dados: ImageView
    private lateinit var bt_lembrete: Button
    private lateinit var bt_alarme: Button
    private lateinit var txt_hora: TextView
    private lateinit var txt_minutos: TextView

    private lateinit var calcularingestaodiaria: CalcularIngestaoDiaria
    private var resultadoML = 0.0

    lateinit var timePickerDialog: TimePickerDialog
    lateinit var calendario: Calendar
    var horaAtual = 0
    var minutosAtuais = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar!!.hide()
        IniciasComponentes()
        calcularingestaodiaria = CalcularIngestaoDiaria()

        bt_calcular.setOnClickListener {

            if (edit_peso.text.toString().isEmpty()) {
                Toast.makeText(this, R.string.toast_informe_peso, Toast.LENGTH_SHORT).show()

            } else if (edit_idade.text.toString().isEmpty()) {
                Toast.makeText(this, R.string.toast_informe_idade, Toast.LENGTH_SHORT).show()

            } else {
                val peso = edit_peso.text.toString().toDouble()
                val idade = edit_idade.text.toString().toInt()
                calcularingestaodiaria.CalcularTotalMl(peso, idade)
                resultadoML = calcularingestaodiaria.ResultadoMl()
                val formatar = NumberFormat.getNumberInstance(Locale("pt", "BR"))
                formatar.isGroupingUsed = false
                txt_resultado.text = formatar.format(resultadoML) + " " + "ml"
            }
        }
        ic_redefinir_dados.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle(R.string.dialog_titulo)
                .setMessage(R.string.dialog_desc)
                .setPositiveButton("ok", { dialogInterface, i ->
                    edit_peso.setText("")
                    edit_idade.setText("")
                    txt_resultado.text = ""
                })
            alertDialog.setNegativeButton("Cancelar", { dialogInterface, i ->

            })
            val dialog = alertDialog.create()
            dialog.show()
        }
        bt_lembrete.setOnClickListener {

            calendario = Calendar.getInstance()
            horaAtual = calendario.get(Calendar.HOUR_OF_DAY)
            minutosAtuais = calendario.get(Calendar.MINUTE)
            timePickerDialog =
                TimePickerDialog(this, { TimePicker: TimePicker, hourOfDay: Int, minutes: Int ->
                    txt_hora.text = String.format("%02d", hourOfDay)
                    txt_minutos.text = String.format("%02d", minutes)

                }, horaAtual, minutosAtuais, true)
            timePickerDialog.show()
        }
        bt_alarme.setOnClickListener {
            if (!txt_hora.text.toString().isEmpty() && !txt_minutos.text.toString().isEmpty()) {
                val intent = Intent(AlarmClock.ACTION_SET_ALARM)
                intent.putExtra(AlarmClock.EXTRA_HOUR, txt_hora.text.toString().toInt())
                intent.putExtra(AlarmClock.EXTRA_MINUTES, txt_minutos.text.toString().toInt())
                intent.putExtra(AlarmClock.EXTRA_MESSAGE, getString(R.string.alarme_mensagem))
                startActivity(intent)

                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }


            }
        }

    }

    private fun IniciasComponentes() {
        edit_peso = findViewById(R.id.edit_peso)
        edit_idade = findViewById(R.id.edit_idade)
        bt_calcular = findViewById(R.id.button_calcular)
        txt_resultado = findViewById(R.id.txt_resultado_ml)
        ic_redefinir_dados = findViewById(R.id.ic_redefinir)
        bt_alarme = findViewById(R.id.bt_alarme)
        txt_hora = findViewById(R.id.txt_hora)
        txt_minutos = findViewById(R.id.txt_minutos)
        bt_lembrete = findViewById(R.id.bt_definir_lembrete)
    }
}