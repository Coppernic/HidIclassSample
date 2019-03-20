package fr.coppernic.sample.hidiclass.home

import fr.coppernic.sdk.hid.iclassProx.Card

interface HomeView {
    fun displayTags(card: Card)
    fun showError(message: String)
    fun showFab(visible: Boolean)
    fun playSound()
}