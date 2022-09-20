package io.trustody.assetlibrary.configuration;

import ammer.tech.commons.configuration.ZMQServiceConfiguration;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class Configuration extends ZMQServiceConfiguration {

    @PostConstruct
    public void init(){
        this.buildConfiguration();
    }

}
