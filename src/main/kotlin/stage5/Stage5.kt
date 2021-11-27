package stage5

import java.io.File
import java.io.FileNotFoundException

data class Flashcard(var card: String = "n.a.", var definition: String = "n.a.") {

    fun toExportString() = "$card|$definition"

    companion object {
        fun fromExportString(s: String): Flashcard {
            val (card, def) = s.split("|")
            return Flashcard(card, def)
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
        println(flashcards.toString())
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
}

fun versionFive() {
    loop()
}

// this is the main loop of the program
fun loop() {
    while (true) {
        println("Input the action (add, remove, import, export, ask, exit):")
        val action = readLine()!!
        when (action) {
            "add" -> add()
            "remove" -> remove()
            "import" -> import()
            "export" -> export()
            "ask" -> ask()
            "exit" -> {
                println("Bye bye!")
                break
            }
        }
        //Cards.printAll()
        //println("--------")
    }
}

fun add() {
    println("The card:")
    val card = readLine()!!
    if (Cards.containsCard(card)) {
        println("The card \"$card\" already exists.")
        return
    }
    println("The definition of the card:")
    val def = readLine()!!
    if (Cards.containsDefinition(def)) {
        println("The definition \"$def\" already exists.")
        return
    }
    Cards.add(card, def)
    println("""The pair ("$card":"$def") has been added.""")
}

fun ask() {
    println("How many times to ask?")
    val count = readLine()!!.toInt()

    repeat (count) {
        val card = Cards.nextRandom()

        println("""Print the definition of "${card.card}":""")
        val answer = readLine()!!

        if (answer == card.definition) {
            println("Correct!")
        } else if (Cards.containsDefinition(answer)){
            val correctCard = Cards.getByDefinition(answer)
            val response = """Wrong. The right answer is "${card.definition}", but your definition is correct for "${correctCard.card}"."""
            println(response)
        } else {
            println("""Wrong. The right answer is "${card.definition}"""")
        }
    }
}

fun export() {
    println("File name:")
    val filename = readLine()!!
    Cards.exportToFile(filename)
    println("${Cards.deckSize()} cards have been saved.")
}

fun import() {
    println("File name:")
    val filename = readLine()!!
    Cards.importFromFile(filename)
}

fun remove() {
    println("Which card?")
    val card = readLine()!!
    if (!Cards.containsCard(card)) {
        println("Can't remove \"$card\": there is no such card.")
        return
    }
    Cards.removeCard(card)
    println("The card has been removed.")
}

