// Copyright (c) 2014 K Team. All Rights Reserved.
package org.kframework.kast;

import org.kframework.kil.loader.Context;
import org.kframework.main.FrontEnd;
import org.kframework.main.GlobalOptions;
import org.kframework.utils.inject.DefinitionLoadingModule;
import org.kframework.utils.inject.First;
import org.kframework.utils.options.DefinitionLoadingOptions;

import com.google.inject.AbstractModule;

public class KastModule extends AbstractModule {

    private final KastOptions options;

    public KastModule(KastOptions options) {
        this.options = options;
    }

    @Override
    public void configure() {
        bind(FrontEnd.class).to(KastFrontEnd.class);
        bind(KastOptions.class).toInstance(options);
        bind(GlobalOptions.class).toInstance(options.global);
        bind(DefinitionLoadingOptions.class).toInstance(options.definitionLoading);

        install(new DefinitionLoadingModule());

        bind(Context.class).annotatedWith(First.class).to(Context.class);
    }

}
