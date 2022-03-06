package software.girls.vzor.screen.home.elm

import vivid.money.elmslie.core.store.dsl_reducer.DslReducer

class HomeReducer : DslReducer<Event, State, Effect, Command>() {

    override fun Result.reduce(event: Event) = when (event) {
        Event.Ui.Init -> Unit
        Event.Ui.OpenFloatingWindowClick -> effects { +Effect.OpenFloatingWindow }
        Event.Internal.Evnt -> {}
    }
}