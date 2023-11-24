package kr.co.lol.ui.usermatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kr.co.lol.dataclass.UserMatchId
import kr.co.lol.internet.retrofit.GetJsonFromRerofit
import kr.co.lol.internet.retrofit.LolQueryMatchId
import retrofit2.create

class MatchViewModel : ViewModel() {

    private val retrofitService = GetJsonFromRerofit.getInstanceAsia().create(LolQueryMatchId::class.java)
    lateinit var items : Flow<PagingData<UserMatchId>>

    fun setPageItem(matchList: List<String>, apiKey: String) {
        items = Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = {
                MatchPageSource(retrofitService, matchList, apiKey)
            }
        ).flow
            .cachedIn(viewModelScope)
    }


}