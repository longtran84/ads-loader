package vn.fintechviet.content;

import akka.actor.ActorSystem;
import play.libs.concurrent.CustomExecutionContext;

import javax.inject.Inject;

/**
 * Custom execution context wired to "post.repository" thread pool
 */
public class ContentExecutionContext extends CustomExecutionContext {

    @Inject
    public ContentExecutionContext(ActorSystem actorSystem) {
        super(actorSystem, "content.repository");
    }
}
