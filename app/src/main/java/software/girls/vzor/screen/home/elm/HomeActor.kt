package software.girls.vzor.screen.home.elm

import io.reactivex.rxjava3.core.Observable
import vivid.money.elmslie.core.store.Actor

class HomeActor : Actor<Command, Event> {

    override fun execute(command: Command): Observable<Event>  = Observable.just(Event.Internal.Evnt)
}