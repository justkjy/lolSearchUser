package kr.co.justkimlol.mainfragment.home.internet

sealed class UserInfoStep {
    data object Loading : UserInfoStep()
    class SearchMsg(val userId: String, val tagLine: String): UserInfoStep()
    class Success(val userId : String, val tagLine: String): UserInfoStep()
    class Fail(val code:Int) : UserInfoStep()
}
typealias userInfoGetLoading = UserInfoStep.Loading
typealias userStepMsg = UserInfoStep.SearchMsg
typealias userInfoSuccess = UserInfoStep.Success
typealias userInfoFail = UserInfoStep.Fail

