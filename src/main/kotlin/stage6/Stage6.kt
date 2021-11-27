package stage6

import java.io.File
import java.io.FileNotFoundException

data class Flashcard(var card: String = "n.a.", var definition: String = "n.a.", var mistakes: Int = 0) {

    fun toExportString() = "$card|$definition|${mistakes}"

    companion object {
        fun fromExportString(s: String): Flashcard {
            val (card, def, mistakes) = s.split("|")
            return Flashcard(card, def, mistakes.toInt())
        }
    }
}

fun String.putInQuotes() = """"${this}""""

fun versionSix() {
    loop()
}

fun loop() {
    while (true) {
        println("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):")
        val action = readLine()
        when (action) {
            "add" -> add()
            "remove" -> remove()
            "import" -> import()
            "export" -> export()
            "ask" -> ask()
            "log" -> log()
            "hardest card" -> hardest()
            "reset stats" -> reset()
            "exit" -> {
                println("Bye bye!")
                break
            }
        }
    }
}

object Cards {
    val flashcards = mutableListOf<Flashcard>()

    fun nextRandom() = flashcards.random()

    fun add(card: String, def: String) {
        flashcards.add(Flashcard(card, def))
    }

    fun getByDefinition(def: String): Flashcard {
        val card = flashcards.filter { it.definition == def }.first()
        return card
    }

    fun addAll(cards: List<Flashcard>) {
        cards.forEach {
            if (containsCard(it.card)) {
                removeCard(it.card)
            }
            add(it)
        }
    }

    fun add(card: Flashcard) {
        flashcards.add(card)
    }

    fun printAll() {
        kotlin.io.println(flashcards.toString())
    }

    fun containsCard(card: String) = flashcards.any { card == it.card }

    fun containsDefinition(def: String) = flashcards.any { def == it.definition }

    fun removeCard(card: String) {
        flashcards.removeIf { it.card == card }
    }

    fun deckSize() = flashcards.size

    fun exportToFile(filename: String) {
        val file = File(filename)
        file.writeText("")
        flashcards.forEach {
            file.appendText(it.toExportString() + "\n")
        }
    }

    fun importFromFile(filename: String) {
        try {
            val lines = File(filename).readLines().filter { it.contains('|') }
            val cards = lines.map { s ->
                Flashcard.fromExportString(s)
            }
            //println(cards)
            addAll(cards)
            println("${lines.size} cards have been loaded.")
        } catch (e: FileNotFoundException) {
            println("File not found.")
        }
    }

    fun reset() {
        flashcards.forEach {
            it.mistakes = 0
        }
    }

    fun highestMistakes(): List<Flashcard> {
        if (flashcards.isEmpty()) {
            return flashcards.toList()
        }
        val maxMistakes = flashcards.maxOf { it.mistakes }
        return flashcards.filter { it.mistakes == maxMistakes && maxMistakes > 0 }
    }
}

object Logs {
    val logs = mutableListOf<String>()

    fun log(s: String) {
        logs.add(s)
    }

    fun printLogs() {
        logs.forEach {
            kotlin.io.println(it)
        }
    }
}

fun reset() {
    Cards.reset()
    println("Card statistics have been reset.")
}

fun hardest() {
    val hard = Cards.highestMistakes()
    when (hard.size) {
        0 -> println("""There are no cards with errors.""")
        1 -> println("""The hardest card is "${hard.first().card}". You have ${hard.first().mistakes} errors answering it""")
        else -> {
            val terms = hard.map { it.card.putInQuotes() }.joinToString()
            val mistakes = hard.first().mistakes
            println("""The hardest cards are ${terms}. You have ${mistakes} errors answering them.""")
        }
    }
}

fun log() {
    println("File name:")
    val logfile = readLine()
    File(logfile).writeText(Logs.logs.joinToString("\n"))
    println("The log has been saved.")
}

fun add() {
    println("The card:")
    val card = readLine()
    if (Cards.containsCard(card)) {
        println("The card \"$card\" already exists.")
        return
    }
    println("The definition of the card:")
    val def = readLine()
    if (Cards.containsDefinition(def)) {
        println("The definition \"$def\" already exists.")
        return
    }
    Cards.add(card, def)
    println("""The pair ("$card":"$def") has been added.""")
}

fun ask() {
    println("How many times to ask?")
    val count = readLine().toInt()

    repeat (count) {
        val card = Cards.nextRandom()

        println("""Print the definition of "${card.card}":""")
        val answer = readLine()

        if (answer == card.definition) {
            println("Correct!")
        } else if (Cards.containsDefinition(answer)) {
            val correctCard = Cards.getByDefinition(answer)
            val response = """Wrong. The right answer is "${card.definition}", but your definition is correct for "${correctCard.card}"."""
            println(response)
            // fixme encapsulate
            card.mistakes += 1
        } else {
            println("""Wrong. The right answer is "${card.definition}"""")
            // fixme encapsulate
            card.mistakes += 1
        }
    }
}

fun export() {
    println("File name:")
    val filename = readLine()
    Cards.exportToFile(filename)
    println("${Cards.deckSize()} cards have been saved.")
}

fun import() {
    println("File name:")
    val filename = readLine()
    Cards.importFromFile(filename)
}

fun remove() {
    println("Which card?")
    val card = readLine()
    if (!Cards.containsCard(card)) {
        println("Can't remove \"$card\": there is no such card.")
        return
    }
    Cards.removeCard(card)
    println("The card has been removed.")
}

fun println(s: String) {
    kotlin.io.println(s)
    Logs.log(s)
}

fun readLine(): String {
    val input = kotlin.io.readLine().toString()
    Logs.log(input)
    return input
}
