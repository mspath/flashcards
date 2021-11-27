package stage3

data class Flashcard(var card: String = "n.a.", var definition: String = "n.a.")

fun versionThree() {
    println("Input the number of cards:")
    val size = readLine()!!.toInt()

    val flashcards = MutableList<Flashcard>(size) { Flashcard() }

    flashcards.forEachIndexed { index, flashcard ->
        println("Card #${index}:")
        flashcard.card = readLine()!!
        println("The definition for card #${index}:")
        flashcard.definition = readLine()!!
    }

    flashcards.forEach {
        println("Print the definition of \"${it.card}\":")
        val answer = readLine()!!
        if (answer == it.definition) {
            println("Correct!")
        } else {
            println("Wrong. The right answer is \"${it.definition}\".")
        }
    }
}