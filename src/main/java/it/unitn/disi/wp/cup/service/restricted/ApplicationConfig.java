package it.unitn.disi.wp.cup.service.restricted;

import com.alibaba.fastjson.support.jaxrs.FastJsonProvider;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;

/**
 * Application for public services
 *
 * @author Carlo Corradini
 */
@ApplicationPath("service/restricted")
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
        resources.add(PersonService.class);
        resources.add(MultiPartFeature.class);
        resources.add(FastJsonProvider.class);
    }
}