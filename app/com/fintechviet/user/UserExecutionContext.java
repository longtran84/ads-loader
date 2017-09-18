package com.fintechviet.user;

import akka.actor.ActorSystem;
import play.libs.concurrent.CustomExecutionContext;

import javax.inject.Inject;

/**
 * Custom execution context wired to "post.repository" thread pool
 */
public class UserExecutionContext extends CustomExecutionContext {

    @Inject
    public UserExecutionContext(ActorSystem actorSystem) {
        super(actorSystem, "user.repository");
    }
}
