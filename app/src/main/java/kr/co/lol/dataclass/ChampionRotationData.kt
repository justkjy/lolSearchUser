package kr.co.lol.dataclass

data class ChampionRotationData(
    val freeChampionIds: List<Int>,
    val freeChampionIdsForNewPlayers: List<Int>,
    val maxNewPlayerLevel: Int)