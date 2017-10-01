package share.fair.fairshare.models;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by niryo on 29/09/2017.
 */

public class User {
    private String id;
    private String name;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public User(String name) {
        this.id = new BigInteger(130, new SecureRandom()).toString(32).substring(0, 6);
        this.name = name;
    }

    public String getId() {
        return id;
    }
}
