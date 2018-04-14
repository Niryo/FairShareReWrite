package share.fair.fairshare.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by niryo on 13/04/2018.
 */

public class GroupNameAndKey implements Serializable{
    public String name;
    public String key;
    public GroupNameAndKey(String name, String key) {
        this.name = name;
        this.key = key;
    }
}
