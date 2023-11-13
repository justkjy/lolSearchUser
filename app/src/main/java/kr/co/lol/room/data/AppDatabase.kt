package kr.co.lol.room.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kr.co.lol.room.LolInfoDao


@Database( entities = [LolVersionEntity::class,LolChampInfoEntity::class],version = 1, exportSchema = false)
abstract class RoomHelper : RoomDatabase() {
    abstract fun roomMemoDao(): LolInfoDao
}

// onCreate 밑에서 사용하자.
fun roomHelperValue(context:Context) : RoomHelper? {
    return Room.databaseBuilder(context, RoomHelper::class.java, "lol_justdatabase")
        .allowMainThreadQueries()
        .build()
}

