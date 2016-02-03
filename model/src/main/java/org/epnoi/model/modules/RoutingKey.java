package org.epnoi.model.modules;

import lombok.Data;
import org.epnoi.model.domain.Resource;

/**
 * Created by cbadenes on 26/11/15.
 */
@Data
public class RoutingKey {

    String key;

    private RoutingKey(String key){
        this.key = key;
    }

    public static RoutingKey all(){
        return new RoutingKey("#");
    }

    public static RoutingKey of(Resource.Type resource, Resource.State state){
        return new RoutingKey(resource.key()+"."+state.key());
    }
}
