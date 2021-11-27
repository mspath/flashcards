package stage4

fun versionFour() {
    // (1) ask for the number of flashcards
    println("Input the number of cards:")
    val size = readLine()!!.toInt()
    val flashcards = mutableMapOf<String, String>()
    val flashcardsIndex = mutableMapOf<String, String>()

    // (2) define the flashcards
    for (index in 0 until size) {

        // (2a) get a unique name
        println("Card #${index + 1}:")
        var card = readLine()!!
        while (flashcards.containsKey(card)) {
            println("The term \"${card}\" already exists. Try again:")
            card = readLine()!!
        }

        // (2b) get a unique definition
        println("The definition for card #${index + 1}:")
        var definition = readLine()!!
        while (flashcards.containsValue(definition)) {
            println("The definition \"${definition}\" already exists. Try again:")
            definition = readLine()!!
        }

        flashcards[card] = definition
        flashcardsIndex[definition] = card
    }

    // (3) quiz the cards
    flashcards.forEach {
        println("Print the definition of \"${it.key}\":")
        val answer = readLine()!!
        if (answer == it.value) {
            println("Correct!")
        } else if (flashcardsIndex.containsKey(answer)) {
            val correctKey = flashcardsIndex[answer]!!
            println("Wrong. The right answer is \"${it.value}\", "
                    + "but your definition is correct for \"${correctKey}\".")
        } else {
            println("Wrong. The right answer is \"${it.value}\".")
        }
    }
}