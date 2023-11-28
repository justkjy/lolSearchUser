package kr.co.justkimlol.dataclass

data class ChampionRotationData(
    val freeChampionIds: List<Int>,
    val freeChampionIdsForNewPlayers: List<Int>,
    val maxNewPlayerLevel: Int)