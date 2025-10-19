package com.proyecto.braingasha.data.repository

import com.proyecto.braingasha.data.dao.CardDao
import com.proyecto.braingasha.data.entity.Card
import com.proyecto.braingasha.data.entity.UserCard
import com.proyecto.braingasha.data.dao.CardWithQuantity
import kotlinx.coroutines.flow.Flow

class CardRepository(
    private val cardDao: CardDao
) {
    suspend fun getAllCards(): List<Card> {
        return cardDao.getAllCards()
    }

    suspend fun getCardById(cardId: Long): Card? {
        return cardDao.getCardById(cardId)
    }

    suspend fun getCardsByRarity(rarity: String): List<Card> {
        return cardDao.getCardsByRarity(rarity)
    }

    suspend fun insertCard(card: Card): Long {
        return cardDao.insertCard(card)
    }

    suspend fun getUserCards(userId: Long): List<CardWithQuantity> {
        return cardDao.getUserCards(userId)
    }

    fun getUserCardsFlow(userId: Long): Flow<List<CardWithQuantity>> {
        return cardDao.getUserCardsFlow(userId)
    }

    suspend fun getUserCardCount(userId: Long): Int {
        return cardDao.getUserCardCount(userId)
    }

    suspend fun addCardToUser(userId: Long, cardId: Long) {
        val existingQuantity = cardDao.getUserCardQuantity(userId, cardId)
        if (existingQuantity > 0) {
            cardDao.incrementUserCardQuantity(userId, cardId)
        } else {
            cardDao.addCardToUser(userId, cardId)
        }
    }

    suspend fun drawRandomCard(userId: Long): Card? {
        val allCards = getAllCards()
        if (allCards.isEmpty()) return null

        // Sistema de probabilidades basado en rareza
        val random = Math.random()
        val selectedCard = when {
            random < 0.01 -> allCards.filter { it.rarity == "Legendary" }.randomOrNull()
            random < 0.05 -> allCards.filter { it.rarity == "Epic" }.randomOrNull()
            random < 0.20 -> allCards.filter { it.rarity == "Rare" }.randomOrNull()
            else -> allCards.filter { it.rarity == "Common" }.randomOrNull()
        } ?: allCards.random()

        addCardToUser(userId, selectedCard.id)
        return selectedCard
    }
}
