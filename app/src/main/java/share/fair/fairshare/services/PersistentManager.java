package share.fair.fairshare.services;

/**
 * Created by niryo on 13/04/2018.
 */

public class PersistentManager {
    private static final String GROUP_NAMES_FILE = "groupNames";

    private  FireBaseServerApi cloudApi;
    private DeviceStorageManager deviceStorageManager;

    public PersistentManager(FireBaseServerApi  cloudApi,  DeviceStorageManager storageManager ) {
        this.cloudApi = cloudApi;
        this.deviceStorageManager = storageManager;
    }

    public void persistNewGroup() {

    }
}
