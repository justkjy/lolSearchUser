package kr.co.justkimlol.ui.usermatch

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.delay
import kr.co.justkimlol.dataclass.UserMatchId
import kr.co.justkimlol.internet.TAG
import kr.co.justkimlol.internet.retrofit.LolQueryMatchId

private const val STARTING_KEY = 1

class MatchPageSource(
    private val retrofitService : LolQueryMatchId,
    private val matchIdlist: List<String>,
    private val apiKey: String
) : PagingSource<Int, UserMatchId>() {
    init {
        Log.i(TAG, "Page Source")
    }

    private fun getMatchList(start: Int, count : Int) : List<String> {
        val startIndex = start - 1
        val endIndex = startIndex + count

        if(startIndex >= matchIdlist.size ) {
            return emptyList<String>()
        }

        if(matchIdlist.getOrNull(endIndex).isNullOrBlank()) {
            return matchIdlist.subList(startIndex, matchIdlist.lastIndex+1)
        }

        return matchIdlist.subList(startIndex, endIndex)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserMatchId> {
        val page = params.key ?: STARTING_KEY

        var loadSize = params.loadSize
        if(page == 1) {
            loadSize = params.loadSize/3
        }

        val range = page.until(page + loadSize)

        val matchIdList = getMatchList(range.start, range.count())
        var matchList = mutableListOf<UserMatchId>()
        if(matchIdList.isNotEmpty()) {
            for (matchId in matchIdList) {
                val response = retrofitService.getMatchList(matchId, apiKey)
                if (response.isSuccessful) {
                    if (response.code() == 200) {
                        response.body().let {
                            matchList.add(it!!)
                        }
                    }
                } else {
                    // 마지막 페이지 카드뷰로 처리
                }
            }

            if (page != STARTING_KEY) {
                delay(1000)
            }

            return LoadResult.Page(
                data = matchList,
                prevKey = null,
                nextKey = page + loadSize
            )
        } else {
            return LoadResult.Page(
                data = matchList,
                prevKey = null,
                nextKey = page
            )
        }
    }

    override fun getRefreshKey(state: PagingState<Int, UserMatchId>): Int? {
        TODO("Not yet implemented")
    }
}