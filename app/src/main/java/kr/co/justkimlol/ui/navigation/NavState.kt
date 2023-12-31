package kr.co.justkimlol.ui.navigation

sealed class NavViewState {
    data object WaitState : NavViewState()
    data object CheckInputState : NavViewState()
    class FailState(val errorCode:Int) :  NavViewState()
    data object SuccessState : NavViewState()
}

typealias navWaitState = NavViewState.WaitState
typealias navSuccessState = NavViewState.SuccessState
typealias navCheckState = NavViewState.CheckInputState
typealias navFailState = NavViewState.FailState