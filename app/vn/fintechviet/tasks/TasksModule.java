package vn.fintechviet.tasks;

/**
 * Created by tungn on 4/12/2018.
 */
import com.google.inject.AbstractModule;

public class TasksModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(LatestNewsTask.class).asEagerSingleton();
    }
}