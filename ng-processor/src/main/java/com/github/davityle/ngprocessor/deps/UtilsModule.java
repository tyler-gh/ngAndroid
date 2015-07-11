package com.github.davityle.ngprocessor.deps;

import com.github.davityle.ngprocessor.util.MessageUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UtilsModule {
    @Provides
    @Singleton
    public MessageUtils messageUtils(ProcessingEnvironment processingEnvironment){
        return new MessageUtils(processingEnvironment);
    }
}
