package kr.co.lol.ui.navigation

sealed class NavViewState {
    data object WaitState : NavViewState()
    data object CheckInputState : NavViewState()
    class FailState(val errorCode:Int) :  NavViewState()
    data object SuccessState : NavViewState()

}

typealias navWaitState = NavViewState.WaitState
typealias navSuccessState = NavViewState.SuccessState
typealias navcheckState = NavViewState.CheckInputState
typealias navFailState = NavViewState.FailState