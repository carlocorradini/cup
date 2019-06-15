package it.unitn.disi.wp.cup.service;

import com.alibaba.fastjson.support.jaxrs.FastJsonProvider;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;

/**
 * Application for public services
 *
 * @author Carlo Corradini
 */
@ApplicationPath("service")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Register resources classes
     *
     * @param resources A resources set
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(RecoverService.class);
        resources.add(FastJsonProvider.class);
    }
}