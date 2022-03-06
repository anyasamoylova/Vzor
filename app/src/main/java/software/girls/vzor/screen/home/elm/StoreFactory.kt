package software.girls.vzor.screen.home.elm

import vivid.money.elmslie.core.store.ElmStore

object StoreFactory {
    fun create() = ElmStore(
        initialState = State,
        reducer = HomeReducer(),
        actor = HomeActor()
    )
}