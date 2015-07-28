package com.github.davityle.ngprocessor.deps;

import com.github.davityle.ngprocessor.util.MessageUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UtilsModule {

    private MessageUtils messageUtils;
    private ProcessingEnvironment processingEnvironment;

    @Provides
    @Singleton
    public MessageUtils messageUtils(ProcessingEnvironment processingEnvironment){
        if(this.messageUtils == null || this.processingEnvironment != processingEnvironment) {
            this.messageUtils = new MessageUtils(processingEnvironment);
            this.processingEnvironment = processingEnvironment;
        }
        return messageUtils;
    }
}
