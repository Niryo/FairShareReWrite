package share.fair.fairshare.models;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by niryo on 29/09/2017.
 */

public class User {
    private String id;
    private String name;
    private Double ballance;

    public User(String id, String name, Double ballance) {
        this.id = id;
        this.name = name;
        this.ballance = ballance;
    }
    public User(String name) {
        this.id = new BigInteger(130, new SecureRandom()).toString(32).substring(0, 6);
        this.name = name;
        this.ballance = 0.0;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public Double getBallance() {
        return ballance;
    }

}
