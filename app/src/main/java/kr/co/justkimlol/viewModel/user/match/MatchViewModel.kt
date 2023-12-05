package kr.co.justkimlol.viewModel.user.match

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kr.co.justkimlol.dataclass.UserMatchId
import kr.co.justkimlol.internet.retrofit.GetJsonFromRetrofit
import kr.co.justkimlol.internet.retrofit.LolQueryMatchId
import kr.co.justkimlol.mainfragment.user.usermatch.MatchPageSource

class MatchViewModel : ViewModel() {

    private val retrofitService = GetJsonFromRetrofit.getInstanceAsia().create(LolQueryMatchId::class.java)
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