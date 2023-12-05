package kr.co.justkimlol.dataclass


data class UserMatchId(
    val info: Info,
    val metadata: Metadata
) {
    data class Info(
        val gameCreation: Long,
        val gameDuration: Int,  // 게임 시간 60으로 나누면 시간나옴
        val gameEndTimestamp: Long,
        val gameId: Long,

        // gameMode list
/*
[
    {
        "gameMode": "CLASSIC",
        "description": "Classic Summoner's Rift and Twisted Treeline games"
    },
    {
        "gameMode": "ODIN",
        "description": "Dominion/Crystal Scar games"
    },
    {
        "gameMode": "ARAM",
        "description": "ARAM games"
    },
    {
        "gameMode": "TUTORIAL",
        "description": "Tutorial games"
    },
    {
        "gameMode": "URF",
        "description": "URF games"
    },
    {
        "gameMode": "DOOMBOTSTEEMO",
        "description": "Doom Bot games"
    },
    {
        "gameMode": "ONEFORALL",
        "description": "One for All games"
    },
    {
        "gameMode": "ASCENSION",
        "description": "Ascension games"
    },
    {
        "gameMode": "FIRSTBLOOD",
        "description": "Snowdown Showdown games"
    },
    {
        "gameMode": "KINGPORO",
        "description": "Legend of the Poro King games"
    },
    {
        "gameMode": "SIEGE",
        "description": "Nexus Siege games"
    },
    {
        "gameMode": "ASSASSINATE",
        "description": "Blood Hunt Assassin games"
    },
    {
        "gameMode": "ARSR",
        "description": "All Random Summoner's Rift games"
    },
    {
        "gameMode": "DARKSTAR",
        "description": "Dark Star: Singularity games"
    },
    {
        "gameMode": "STARGUARDIAN",
        "description": "Star Guardian Invasion games"
    },
    {
        "gameMode": "PROJECT",
        "description": "PROJECT: Hunters games"
    },
    {
        "gameMode": "GAMEMODEX",
        "description": "Nexus Blitz games"
    },
    {
        "gameMode": "ODYSSEY",
        "description": "Odyssey: Extraction games"
    },
    {
        "gameMode": "NEXUSBLITZ",
        "description": "Nexus Blitz games"
    },
    {
        "gameMode": "ULTBOOK",
        "description": "Ultimate Spellbook games"
    }
]
 */
        val gameMode: String, // ARAM 칼바람
        val gameName: String,
        val gameStartTimestamp: Long,
        val gameType: String,
        val gameVersion: String,
        val mapId: Int, // 칼바람 맵 번호 12
        val participants: List<Participant>,
        val platformId: String,
        val queueId: Int,
        val teams: List<Team>,
        val tournamentCode: String
    ) {
        data class Participant( // 참가자
            val allInPings: Int,
            val assistMePings: Int,
            val assists: Int,                      // 어시스트
            val baitPings: Int,
            val baronKills: Int,
            val basicPings: Int,
            val bountyLevel: Int,
            val challenges: Challenges,
            val champExperience: Int,
            val champLevel: Int,                    // 최종 래벨
            val championId: Int,                    // 챔피언 아이디
            val championName: String,               // 챔피언 이름
            val championTransform: Int,
            val commandPings: Int,
            val consumablesPurchased: Int,
            val damageDealtToBuildings: Int,      // 포탑에 가한 피해량
            val damageDealtToObjectives: Int,
            val damageDealtToTurrets: Int,
            val damageSelfMitigated: Int,
            val dangerPings: Int,
            val deaths: Int,              // 죽은 수
            val detectorWardsPlaced: Int,
            val doubleKills: Int,
            val dragonKills: Int,
            val eligibleForProgression: Boolean,
            val enemyMissingPings: Int,
            val enemyVisionPings: Int,
            val firstBloodAssist: Boolean,
            val firstBloodKill: Boolean,
            val firstTowerAssist: Boolean,
            val firstTowerKill: Boolean,
            val gameEndedInEarlySurrender: Boolean,
            val gameEndedInSurrender: Boolean,
            val getBackPings: Int,
            val goldEarned: Int,
            val goldSpent: Int,
            val holdPings: Int,
            val individualPosition: String,
            val inhibitorKills: Int,
            val inhibitorTakedowns: Int,
            val inhibitorsLost: Int,
            val item0: Int,   // 아이템 정보
            val item1: Int,
            val item2: Int,
            val item3: Int,
            val item4: Int,
            val item5: Int,
            val item6: Int,
            val itemsPurchased: Int,
            val killingSprees: Int,
            val kills: Int,                          // 킬수
            val lane: String,
            val largestCriticalStrike: Int,
            val largestKillingSpree: Int,
            val largestMultiKill: Int,
            val longestTimeSpentLiving: Int,
            val magicDamageDealt: Int,
            val magicDamageDealtToChampions: Int, // 챔피언에게 가한 마법 피해량
            val magicDamageTaken: Int, // 적에게 받은 마법 피해량
            val needVisionPings: Int,
            val neutralMinionsKilled: Int,
            val nexusKills: Int,
            val nexusLost: Int,
            val nexusTakedowns: Int,
            val objectivesStolen: Int,
            val objectivesStolenAssists: Int,
            val onMyWayPings: Int,
            val participantId: Int,
            val pentaKills: Int,
            val perks: Perks,
            val physicalDamageDealt: Int,
            val physicalDamageDealtToChampions: Int,
            val physicalDamageTaken: Int,
            val placement: Int,
            val playerAugment1: Int,
            val playerAugment2: Int,
            val playerAugment3: Int,
            val playerAugment4: Int,
            val playerSubteamId: Int,
            val profileIcon: Int,
            val pushPings: Int,
            val puuid: String,
            val quadraKills: Int,
            val riotIdName: String,
            val riotIdTagline: String,
            val role: String,
            val sightWardsBoughtInGame: Int,
            val spell1Casts: Int,
            val spell2Casts: Int,
            val spell3Casts: Int,
            val spell4Casts: Int,
            val subteamPlacement: Int,
            // 스펠

            // 스펠
            // 스펠 1 == 정화
            // 스펠 3 == 탈진
            // 스펠 4 == 점멸
            // 스펠 6 == 유체화
            // 스펠 7 == 회복
            // 스펠 11 = 강타
            // 스펠 12 = 텔(순간이동)
            // 스펠 13 = 총명
            // 스펠 14 = 점화
            // 스펠 21 = 방어막
            // 스펠 32 = 표식 (눈덩이)
            val summoner1Casts: Int,
            val summoner1Id: Int,     // 스펠
            val summoner2Casts: Int,
            val summoner2Id: Int,    // 스펠
            val summonerId: String,
            val summonerLevel: Int,
            val summonerName: String,
            val teamEarlySurrendered: Boolean,
            val teamId: Int,
            val teamPosition: String,
            val timeCCingOthers: Int,
            val timePlayed: Int,
            val totalAllyJungleMinionsKilled: Int,
            val totalDamageDealt: Int,
            val totalDamageDealtToChampions: Int,
            val totalDamageShieldedOnTeammates: Int,
            val totalDamageTaken: Int,
            val totalEnemyJungleMinionsKilled: Int,
            val totalHeal: Int,
            val totalHealsOnTeammates: Int,
            val totalMinionsKilled: Int, // 미니언 수
            val totalTimeCCDealt: Int,
            val totalTimeSpentDead: Int,
            val totalUnitsHealed: Int,
            val tripleKills: Int,
            val trueDamageDealt: Int,
            val trueDamageDealtToChampions: Int,
            val trueDamageTaken: Int,
            val turretKills: Int,
            val turretTakedowns: Int,
            val turretsLost: Int,
            val unrealKills: Int,
            val visionClearedPings: Int,
            val visionScore: Int,
            val visionWardsBoughtInGame: Int,
            val wardsKilled: Int,
            val wardsPlaced: Int,
            val win: Boolean
        ) {
            data class Challenges(
                val `12AssistStreakCount`: Int,
                val abilityUses: Int,  // 1팀 어시스트
                val acesBefore15Minutes: Int,
                val alliedJungleMonsterKills: Int,
                val baronTakedowns: Int,
                val blastConeOppositeOpponentCount: Int,
                val bountyGold: Int,
                val buffsStolen: Int,
                val completeSupportQuestInTime: Int,
                val controlWardTimeCoverageInRiverOrEnemyHalf: Double,
                val controlWardsPlaced: Int,
                val damagePerMinute: Double,
                val damageTakenOnTeamPercentage: Double,
                val dancedWithRiftHerald: Int,
                val deathsByEnemyChamps: Int,
                val dodgeSkillShotsSmallWindow: Int,
                val doubleAces: Int,
                val dragonTakedowns: Int,
                val earliestDragonTakedown: Double,
                val earlyLaningPhaseGoldExpAdvantage: Int,
                val effectiveHealAndShielding: Double,
                val elderDragonKillsWithOpposingSoul: Int,
                val elderDragonMultikills: Int,
                val enemyChampionImmobilizations: Int,
                val enemyJungleMonsterKills: Int,
                val epicMonsterKillsNearEnemyJungler: Int,
                val epicMonsterKillsWithin30SecondsOfSpawn: Int,
                val epicMonsterSteals: Int,
                val epicMonsterStolenWithoutSmite: Int,
                val fasterSupportQuestCompletion: Int,
                val fastestLegendary: Double,
                val firstTurretKilled: Int,
                val firstTurretKilledTime: Double,
                val flawlessAces: Int,
                val fullTeamTakedown: Int,
                val gameLength: Double,
                val getTakedownsInAllLanesEarlyJungleAsLaner: Int,
                val goldPerMinute: Double,
                val hadOpenNexus: Int,
                val highestChampionDamage: Int,
                val highestCrowdControlScore: Int,
                val highestWardKills: Int,
                val immobilizeAndKillWithAlly: Int,
                val initialBuffCount: Int,
                val initialCrabCount: Int,
                val jungleCsBefore10Minutes: Double,
                val junglerKillsEarlyJungle: Int,
                val junglerTakedownsNearDamagedEpicMonster: Int,
                val kTurretsDestroyedBeforePlatesFall: Int,
                val kda: Double,                           // 평점
                val killAfterHiddenWithAlly: Int,
                val killParticipation: Double,             //킬관여 1이 최대
                val killedChampTookFullTeamDamageSurvived: Int,
                val killingSprees: Int,
                val killsNearEnemyTurret: Int,
                val killsOnLanersEarlyJungleAsJungler: Int,
                val killsOnOtherLanesEarlyJungleAsLaner: Int,
                val killsOnRecentlyHealedByAramPack: Int,
                val killsUnderOwnTurret: Int,
                val killsWithHelpFromEpicMonster: Int,
                val knockEnemyIntoTeamAndKill: Int,
                val landSkillShotsEarlyGame: Int,
                val laneMinionsFirst10Minutes: Int,
                val laningPhaseGoldExpAdvantage: Int,
                val legendaryCount: Int,
                val lostAnInhibitor: Int,
                val maxCsAdvantageOnLaneOpponent: Double,
                val maxKillDeficit: Int,
                val maxLevelLeadLaneOpponent: Int,
                val mejaisFullStackInTime: Int,
                val moreEnemyJungleThanOpponent: Double,
                val multiKillOneSpell: Int,
                val multiTurretRiftHeraldCount: Int,
                val multikills: Int,
                val multikillsAfterAggressiveFlash: Int,
                val outerTurretExecutesBefore10Minutes: Int,
                val outnumberedKills: Int,
                val outnumberedNexusKill: Int,
                val perfectDragonSoulsTaken: Int,
                val perfectGame: Int,
                val pickKillWithAlly: Int,
                val poroExplosions: Int,
                val quickCleanse: Int,
                val quickFirstTurret: Int,
                val quickSoloKills: Int,
                val riftHeraldTakedowns: Int,
                val saveAllyFromDeath: Int,
                val scuttleCrabKills: Int,
                val skillshotsDodged: Int,
                val skillshotsHit: Int,
                val snowballsHit: Int,
                val soloBaronKills: Int,
                val soloKills: Int,
                val soloTurretsLategame: Int,
                val stealthWardsPlaced: Int,
                val survivedSingleDigitHpCount: Int,
                val survivedThreeImmobilizesInFight: Int,
                val takedownOnFirstTurret: Int,
                val takedowns: Int,
                val takedownsAfterGainingLevelAdvantage: Int,
                val takedownsBeforeJungleMinionSpawn: Int,
                val takedownsFirstXMinutes: Int,
                val takedownsInAlcove: Int,
                val takedownsInEnemyFountain: Int,
                val teamBaronKills: Int,
                val teamDamagePercentage: Double,
                val teamElderDragonKills: Int,
                val teamRiftHeraldKills: Int,
                val teleportTakedowns: Int,
                val tookLargeDamageSurvived: Int,
                val turretPlatesTaken: Int,
                val turretTakedowns: Int,
                val turretsTakenWithRiftHerald: Int,
                val twentyMinionsIn3SecondsCount: Int,
                val twoWardsOneSweeperCount: Int,
                val unseenRecalls: Int,
                val visionScoreAdvantageLaneOpponent: Double,
                val visionScorePerMinute: Double,
                val wardTakedowns: Int,
                val wardTakedownsBefore20M: Int,
                val wardsGuarded: Int
            )

            data class Perks(
                val statPerks: StatPerks,
                val styles: List<Style>
            ) {
                data class StatPerks(
                    val defense: Int,
                    val flex: Int,
                    val offense: Int
                )

                data class Style(
                    val description: String,
                    val selections: List<Selection>,
                    val style: Int
                ) {
                    // primaryStyle 일때  8229 신비로운 유성
                    // 8229
                    // 8226 : 마나순환 팔찌
                    // 8234 : 기민함
                    // 8236 :폭풍결집
                    // subStyle
                    // 8345 비스켓
                    // 8347 우주통찰력

                    data class Selection(
                        val perk: Int,
                        val var1: Int,
                        val var2: Int,
                        val var3: Int
                    )
                }
            }
        }

        // 리스트 2개 나오는데 첫번째 나오는게 레드 두번째 나오는게 블루
        data class Team(
            val bans: List<Any>,
            val objectives: Objectives,
            val teamId: Int,
            val win: Boolean   // 2팀 승패
        ) {
            data class Objectives(
                val baron: Baron,
                val champion: Champion,
                val dragon: Dragon,
                val inhibitor: Inhibitor,
                val riftHerald: RiftHerald,
                val tower: Tower
            ) {
                data class Baron(
                    val first: Boolean,
                    val kills: Int
                )

                data class Champion(
                    val first: Boolean,
                    val kills: Int           // 2팀 챔피언 킬
                )

                data class Dragon(
                    val first: Boolean,
                    val kills: Int
                )

                data class Inhibitor(
                    val first: Boolean,
                    val kills: Int
                )

                data class RiftHerald(
                    val first: Boolean,
                    val kills: Int
                )

                data class Tower(
                    val first: Boolean,
                    val kills: Int
                )
            }
        }
    }

    data class Metadata(
        val dataVersion: String,
        val matchId: String,
        val participants: List<String>
    )
}