package kr.co.justkimlol.mainfragment.champion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import co.yml.charts.common.extensions.isNotNull
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kr.co.justkimlol.viewModel.SharedViewModel
import kr.co.justkimlol.dataclass.ChampAllListData
import kr.co.justkimlol.dataclass.ChampionRotationData
import kr.co.justkimlol.internet.getJsonFileFromHttps
import kr.co.justkimlol.room.data.RoomHelper
import kr.co.justkimlol.room.data.roomHelperValue
import kr.co.justkimlol.ui.component.button.ChampionList
import kr.co.justkimlol.ui.component.championInfo.all.ChampLolInfo
import kr.co.justkimlol.ui.theme.LolInfoViewerTheme
import kr.co.justkimlol.room.LolInfoDao

class ChampionFragment : Fragment() {

    // 로그인 viewModel
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var helper: RoomHelper? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileId = sharedViewModel.profileId.value!!
        val userName = sharedViewModel.userId.value!!
        val userTier = sharedViewModel.loltiar.value!!
        val skillNum = sharedViewModel.summonerLevel.value!!
        val apiKey = sharedViewModel.apiKey.value!!
        val championAllList = mutableListOf<ChampAllListData>()

        this.context ?.let { context ->
            helper = roomHelperValue(context)
            val lolInfoDb = helper!!.roomMemoDao()
            val list =  lolInfoDb.getChampAll()
            for(item in list) {
                val championList = ChampAllListData(
                    item.champKeyId,
                    item.nameKor,
                    item.nameEng,
                    item.title,
                    item.tagList
                )
                championAllList.add(championList)
            }
            rotationChamp(apiKey, lolInfoDb)
            this
        }

        return ComposeView(requireContext()).apply {
            setContent {
                LolInfoViewerTheme() {
                    Column {
                        val championRotationData = sharedViewModel.champNameRotations.observeAsState().value!!
                        ChampLolInfo(
                            helper,
                            profileId,
                            userName,
                            userTier,
                            skillNum,
                            championAllList,
                            championRotationData,
                        )
                    }
                }
            }
        }
    }

    private fun rotationChamp(apiKey: String, lolInfoDb: LolInfoDao)  {
        if(sharedViewModel.championRotationData.isNotNull()) {
            val championRotationData = mutableListOf<ChampionList>()
            GlobalScope.launch(Dispatchers.IO) {
                val rotationChamp = getJsonFileFromHttps(
                    "https://kr.api.riotgames.com/lol/platform/v3/champion-rotations?api_key=${apiKey}"
                )
                val champ = Gson().fromJson(rotationChamp, ChampionRotationData::class.java)
                sharedViewModel.champRotations(champ.freeChampionIds)
                val rotationItem = sharedViewModel.championRotationData.value!!
                for(item in rotationItem) {
                    val resultItem = lolInfoDb.getChampItem(item)
                    if(resultItem.isNotEmpty()) {
                        val engName = resultItem[0].nameEng
                        val korName = resultItem[0].nameKor
                        championRotationData.add(ChampionList(engName, korName))
                    }
                }
                sharedViewModel.setChampNameRotations(championRotationData)
            }
        }
    }
}






