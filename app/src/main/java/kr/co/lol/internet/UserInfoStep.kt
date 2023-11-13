package kr.co.lol.internet

const val WAITSTEP = "WAITSTEP"
const val DOSEARCH = "SEARCHSTEP"

sealed class UserInfoStep {
    object loading : UserInfoStep()
    class SearchMsg(val userId: String): UserInfoStep()
    class Success: UserInfoStep()
    class Fail(val code:Int) : UserInfoStep()
}
typealias userInfoGetloading = UserInfoStep.loading
typealias userStepMsg = UserInfoStep.SearchMsg
typealias userInfoSuccess = UserInfoStep.Success
typealias userInfoFail = UserInfoStep.Fail

