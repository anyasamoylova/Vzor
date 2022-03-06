package software.girls.vzor.screen.home.elm

object State

sealed class Event {
    sealed class Ui : Event() {
        object Init : Ui()
        object OpenFloatingWindowClick : Ui()
    }

    sealed class Internal : Event() {
        object Evnt : Internal()
    }
}

sealed class Command {

}

sealed class Effect {
    object OpenFloatingWindow : Effect()
    data class ShowError(val error: String): Effect()
}