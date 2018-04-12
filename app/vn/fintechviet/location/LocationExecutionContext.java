package vn.fintechviet.location;

import akka.actor.ActorSystem;
import play.libs.concurrent.CustomExecutionContext;

import javax.inject.Inject;

/**
 * Custom execution context wired to "post.repository" thread pool
 */
public class LocationExecutionContext extends CustomExecutionContext {

    @Inject
    public LocationExecutionContext(ActorSystem actorSystem) {
        super(actorSystem, "location.repository");
    }
}
