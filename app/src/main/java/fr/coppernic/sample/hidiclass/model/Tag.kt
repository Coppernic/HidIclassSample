package fr.coppernic.sample.hidiclass.model

import fr.coppernic.sdk.hid.iclassProx.Card
import java.util.*

data class Tag (val card: Card, var count: Int = 1){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Tag

        if(card.cardNumber >0 && card.cardNumber == other.card.cardNumber) return true
        if(card.cardSerialNumber == null) return false
        if(Arrays.equals(card.cardSerialNumber, other.card.cardSerialNumber)) return true

        return false
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(card.cardSerialNumber)
    }
}