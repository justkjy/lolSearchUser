package kr.co.lol.ui.champion

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kr.co.lol.R
import kr.co.lol.SharedViewModel
import kr.co.lol.databinding.FragmentChampionBinding
import kr.co.lol.databinding.FragmentHomeBinding
import kr.co.lol.dataclass.ChampAllListData
import kr.co.lol.internet.getConnectUrl
import kr.co.lol.internet.getJsonFailed
import kr.co.lol.internet.getJsonFileFromHttps
import kr.co.lol.internet.getJsonLoad
import kr.co.lol.internet.getJsonSuccess
import kr.co.lol.dataclass.ChampionRotationData
import kr.co.lol.internet.TAG
import kr.co.lol.internet.retrofit.GetJsonFromRerofit
import kr.co.lol.internet.retrofit.LolQueryUserName
import kr.co.lol.room.data.RoomHelper
import kr.co.lol.room.data.roomHelperValue
import kr.co.lol.ui.component.button.ChampionList
import kr.co.lol.ui.home.viewModel.ChampionInitViewModel
import kr.co.lol.ui.home.viewModel.PatchState
import kr.co.lol.ui.navigation.navFailState
import kr.co.lol.ui.navigation.navSuccessState
import kr.co.lol.ui.navigation.navWaitState
import kr.co.lol.ui.navigation.navcheckState
import kr.co.lol.ui.theme.LolInfoViewerTheme
import kr.co.lol.ui.theme.LolWhiteTheme

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






