package com.proyecto.braingasha.data.database

import com.proyecto.braingasha.data.dao.CardDao
import com.proyecto.braingasha.data.entity.Card
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseInitializer(
    private val cardDao: CardDao
) {
    suspend fun initializeDatabase() {
        withContext(Dispatchers.IO) {
            // Verificar si ya hay cartas en la base de datos
            val existingCards = cardDao.getAllCards()
            if (existingCards.isEmpty()) {
                // Crear cartas de ejemplo
                val sampleCards = listOf(
                    Card(
                        name = "Dragón de Fuego",
                        rarity = "Legendary",
                        imageUri = "drawable/dragon_fire",
                        description = "Un poderoso dragón que domina el elemento fuego",
                        power = 100
                    ),
                    Card(
                        name = "Guerrero Élfico",
                        rarity = "Epic",
                        imageUri = "drawable/elf_warrior",
                        description = "Un ágil guerrero élfico con habilidades de combate",
                        power = 75
                    ),
                    Card(
                        name = "Mago Elemental",
                        rarity = "Rare",
                        imageUri = "drawable/elemental_mage",
                        description = "Un mago que controla los elementos naturales",
                        power = 60
                    ),
                    Card(
                        name = "Caballero Humano",
                        rarity = "Common",
                        imageUri = "drawable/human_knight",
                        description = "Un valiente caballero humano",
                        power = 40
                    ),
                    Card(
                        name = "Orco Berserker",
                        rarity = "Common",
                        imageUri = "drawable/orc_berserker",
                        description = "Un feroz orco con gran fuerza física",
                        power = 45
                    ),
                    Card(
                        name = "Hada de Luz",
                        rarity = "Rare",
                        imageUri = "drawable/light_fairy",
                        description = "Una mágica hada que bendice a sus aliados",
                        power = 55
                    ),
                    Card(
                        name = "Demonio del Abismo",
                        rarity = "Epic",
                        imageUri = "drawable/abyss_demon",
                        description = "Un temible demonio del abismo",
                        power = 80
                    ),
                    Card(
                        name = "Fénix Renacido",
                        rarity = "Legendary",
                        imageUri = "drawable/rebirth_phoenix",
                        description = "Una criatura mítica que renace de sus cenizas",
                        power = 95
                    ),
                    Card(
                        name = "Goblin Ladrón",
                        rarity = "Common",
                        imageUri = "drawable/goblin_thief",
                        description = "Un astuto goblin con habilidades de sigilo",
                        power = 35
                    ),
                    Card(
                        name = "Troll Montañés",
                        rarity = "Rare",
                        imageUri = "drawable/mountain_troll",
                        description = "Un troll gigante de las montañas",
                        power = 65
                    )
                )
                
                // Insertar las cartas en la base de datos
                sampleCards.forEach { card ->
                    cardDao.insertCard(card)
                }
            }
        }
    }
}
