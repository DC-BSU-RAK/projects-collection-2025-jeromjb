package com.example.virtualdjapp

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var btnSound1: Button
    private lateinit var btnSound2: Button
    private lateinit var btnSelectSounds: Button
    private lateinit var volume1: SeekBar
    private lateinit var volume2: SeekBar
    private lateinit var crossfadeSeekBar: SeekBar

    private var mediaPlayer1: MediaPlayer? = null
    private var mediaPlayer2: MediaPlayer? = null

    private val soundNames = arrayOf(
        "Track C", "Track D", "Track E", "Track F", "Track G", "Track H",
        "Track I", "Track J", "Track K", "Track L", "Track N"
    )

    private val soundResources = arrayOf(
        R.raw.track_c, R.raw.track_d, R.raw.track_e, R.raw.track_f, R.raw.track_g,
        R.raw.track_h, R.raw.track_i, R.raw.track_j, R.raw.track_k, R.raw.track_l, R.raw.track_n
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSound1 = findViewById(R.id.btnSound1)
        btnSound2 = findViewById(R.id.btnSound2)
        btnSelectSounds = findViewById(R.id.btnSelectSounds)
        volume1 = findViewById(R.id.volume1)
        volume2 = findViewById(R.id.volume2)
        crossfadeSeekBar = findViewById(R.id.crossfadeSeekBar)

        btnSound1.setOnClickListener {
            mediaPlayer1?.start()
        }

        btnSound2.setOnClickListener {
            mediaPlayer2?.start()
        }

        btnSelectSounds.setOnClickListener {
            showSoundSelectionDialog()
        }

        volume1.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val volume = progress / 100f
                mediaPlayer1?.setVolume(volume, volume)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        volume2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val volume = progress / 100f
                mediaPlayer2?.setVolume(volume, volume)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        crossfadeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val ratio = progress / 100f
                mediaPlayer1?.setVolume(1 - ratio, 1 - ratio)
                mediaPlayer2?.setVolume(ratio, ratio)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun showSoundSelectionDialog() {
        val selectedIndices = mutableListOf<Int>()
        val checkedItems = BooleanArray(soundNames.size)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select 2 Sounds")
        builder.setMultiChoiceItems(soundNames, checkedItems) { dialogInterface, which, isChecked ->
            if (isChecked) {
                if (selectedIndices.size < 2) {
                    selectedIndices.add(which)
                } else {
                    // Only allow 2 selections
                    (dialogInterface as AlertDialog).listView.setItemChecked(which, false)
                    Toast.makeText(this, "Only 2 sounds can be selected", Toast.LENGTH_SHORT).show()
                }
            } else {
                selectedIndices.remove(which)
            }
        }

        builder.setPositiveButton("OK") { dialog, _ ->
            if (selectedIndices.size == 2) {
                mediaPlayer1?.release()
                mediaPlayer2?.release()

                mediaPlayer1 = MediaPlayer.create(this, soundResources[selectedIndices[0]])
                mediaPlayer2 = MediaPlayer.create(this, soundResources[selectedIndices[1]])

                mediaPlayer1?.isLooping = true
                mediaPlayer2?.isLooping = true

                Toast.makeText(this, "Tracks Loaded", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please select exactly 2 sounds", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }

    override fun onDestroy() {
        mediaPlayer1?.release()
        mediaPlayer2?.release()
        super.onDestroy()
    }
}
