package store;

import com.google.inject.AbstractModule;

public class StoreModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(StoreManager.class);
    }
}
