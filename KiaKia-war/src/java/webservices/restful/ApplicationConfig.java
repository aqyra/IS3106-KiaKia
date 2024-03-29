/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservices.restful;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Natalienovaela
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(webservices.restful.BucketListResource.class);
        resources.add(webservices.restful.BudgetExpenseResource.class);
        resources.add(webservices.restful.CORSFilter.class);
        resources.add(webservices.restful.CityOrCountryResource.class);
        resources.add(webservices.restful.ExploreResource.class);
        resources.add(webservices.restful.ItineraryResource.class);
        resources.add(webservices.restful.PlacesResource.class);
        resources.add(webservices.restful.TripsResource.class);
        resources.add(webservices.restful.UsersResource.class);
    }
    
}
