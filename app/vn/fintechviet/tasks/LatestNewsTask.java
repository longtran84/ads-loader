package vn.fintechviet.tasks;

import akka.actor.ActorSystem;
import scala.concurrent.duration.Duration;
import vn.fintechviet.content.ContentExecutionContext;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

/**
 * Created by tungn on 4/12/2018.
 */
public class LatestNewsTask {
    private final ActorSystem actorSystem;
    private final ContentExecutionContext executionContext;

    @Inject
    public LatestNewsTask(ActorSystem actorSystem, ContentExecutionContext executionContext) {
        this.actorSystem = actorSystem;
        this.executionContext = executionContext;
        this.initialize();
    }

    private void initialize() {
        this.actorSystem.scheduler().schedule(
                Duration.create(1, TimeUnit.MINUTES), // initialDelay
                Duration.create(30, TimeUnit.MINUTES), // interval
                () -> System.out.println("Running just once."),
                this.executionContext
        );
    }
}
