package com.hsyn.animals

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class QuizActivity : AppCompatActivity(), IClickListener {
    var mediaPlayer: MediaPlayer? = null
    val animals = ArrayList<Animals>()
    var cnt = -1
    var score = 0

    private var answerLocation = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)
        init()
    }
    private fun imageCount(counter: Int): Int {
        when (counter) {
            in -1..2 -> {
                return 2
            }
            in 3..5 -> {
                return 3
            }
            in 6..8 -> {
                return 4
            }
            in 9..11 -> {
                return 6
            }
            else -> {
                return 8
            }
        }
    }
    private fun init() {
        animals.let {
            it.add(Animals("Bee", R.drawable.bee, R.raw.bee))
            it.add(Animals("Bird", R.drawable.bird, R.raw.robin))
            it.add(Animals("Cat", R.drawable.cat, R.raw.cat))
            it.add(Animals("Cow", R.drawable.cow, R.raw.cow))
            it.add(Animals("Dog", R.drawable.dog, R.raw.dog))
            it.add(Animals("Duck", R.drawable.duck, R.raw.duck))
            it.add(Animals("Elephant", R.drawable.elephant, R.raw.elephant))
            it.add(Animals("Frog", R.drawable.frog, R.raw.frog))
            it.add(Animals("Horse", R.drawable.horse, R.raw.horse))
            it.add(Animals("Lion", R.drawable.lion, R.raw.lion))
            it.add(Animals("Owl", R.drawable.owl, R.raw.owl))
            it.add(Animals("Sheep", R.drawable.sheep, R.raw.sheep))
            it.add(Animals("Crocodile", R.drawable.croc, R.raw.croc))
            it.add(Animals("Whale", R.drawable.whale, R.raw.whale))
            it.add(Animals("Bear", R.drawable.bear, R.raw.bear))
            it.add(Animals("Chicken", R.drawable.chicken, R.raw.chicken))
            it.add(Animals("Crow", R.drawable.raven, R.raw.crow))
            it.add(Animals("Monkey", R.drawable.monkey, R.raw.monkey))
            it.add(Animals("Snake", R.drawable.snake, R.raw.rattlesnake))
            it.add(Animals("Bull", R.drawable.bull, R.raw.bull))
        }
        answerLocation = makeQuest(imageCount(cnt))
        cnt += 1
    }

    private fun makeQuest(sizeOfAnswer: Int): Int {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewAnswers)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val selected = mutableSetOf<Animals>()
        while (selected.size < sizeOfAnswer) {
            val item = animals.random()
            selected.add(item)
        }

        val answers = arrayListOf<Animals>().apply {
            this.addAll(selected)
        }

        val adapter = ContentAdapter(animals = answers, clickListener = this)
        adapter.also { recyclerView.adapter = it }

        val trueSelection = answers.random()
        val indexOfCorrectAnswer = answers.indexOf(element = trueSelection)

        speak(sound = R.raw.question)
        this.mediaPlayer?.setOnCompletionListener {
            onStop()
            speak(trueSelection.sound)
        }

        return indexOfCorrectAnswer
    }

    private fun speak(sound: Int) {
        if (mediaPlayer == null) {
            MediaPlayer.create(this, sound).also { mediaPlayer = it }
            this.mediaPlayer!!.start()
        } else mediaPlayer!!.start()
    }

    override fun onStop() {
        super.onStop()

        if (mediaPlayer != null) {
            this.mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }

    private fun writeToFile(point: Int) {
        val pref = this.getSharedPreferences(
            "score_file",
            Activity.MODE_PRIVATE
        )
        val highScore = pref!!.getInt("score", 0)

        if (point > highScore) {
            val editor = pref.edit().also {
                it.putInt("score", point)
                it.apply()
            }
        }
    }

    override fun listener(position: Int) {
        if (position == answerLocation) {
            onStop()
            speak(R.raw.its_true)

            cnt += 1
                this.mediaPlayer?.setOnCompletionListener {
                onStop()
                answerLocation = this.makeQuest(imageCount(cnt))
                score += this.imageCount(cnt)
            }
        } else {
            this.onStop()
            speak(R.raw.wrong)
            writeToFile(score)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}