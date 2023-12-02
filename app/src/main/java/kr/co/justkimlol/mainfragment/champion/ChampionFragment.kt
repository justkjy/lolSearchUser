package kr.co.justkimlol.mainfragment.champion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kr.co.justkimlol.SharedViewModel
import kr.co.justkimlol.databinding.FragmentChampionBinding
import kr.co.justkimlol.dataclass.ChampAllListData
import kr.co.justkimlol.room.data.RoomHelper
import kr.co.justkimlol.room.data.roomHelperValue
import kr.co.justkimlol.ui.component.button.ChampionList
import kr.co.justkimlol.ui.theme.LolInfoViewerTheme

class ChampionFragment : Fragment() {

    // 로그인 viewModel
   private lateinit var sharedViewModel: SharedViewModel
   private var _binding: FragmentChampionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var helper: RoomHelper? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val profileId = sharedViewModel.profileId.value!!
        val userName = sharedViewModel.userId.value!!
        val userTier = sharedViewModel.loltear.value!!
        val skillnum = sharedViewModel.summonerLevel.value!!


        var championAllList = mutableListOf<ChampAllListData>()
        var championRotationData = mutableListOf<ChampionList>()

        val fragmentContext = this.context ?.let { context ->
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

            val rotationItem = sharedViewModel.championRotationData.value!!
            for(item in rotationItem) {
                val resultItem = lolInfoDb.getChampItem(item)
                if(resultItem.isNotEmpty()) {
                    val engName = resultItem.get(0).nameEng
                    val korName = resultItem.get(0).nameKor
                    championRotationData.add(ChampionList(engName, korName))
                }
            }

            this
        }



        return ComposeView(requireContext()).apply {
            setContent {
                LolInfoViewerTheme() {
                    Column {
                        ChampLolInfo(
                            helper,
                            profileId,
                            userName,
                            userTier,
                            skillnum,
                            championAllList,
                            championRotationData,
                        )
                    }
                }
            }
        }
    }
}






