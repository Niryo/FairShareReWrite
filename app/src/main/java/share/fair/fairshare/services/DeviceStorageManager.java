package share.fair.fairshare.services;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import share.fair.fairshare.Constants.FileNames;
import share.fair.fairshare.models.Group;
import share.fair.fairshare.models.GroupNameAndKey;

/**
 * Created by niryo on 28/09/2017.
 */

public class DeviceStorageManager {
    private static void writeObject(Context context, String key, Object object) throws IOException {
        FileOutputStream fos = context.openFileOutput(key, Context.MODE_PRIVATE);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(object);
        oos.close();
        fos.close();
    }

    private static Object readObject(Context context, String key) throws IOException,
            ClassNotFoundException {
        FileInputStream fis = context.openFileInput(key);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object object = ois.readObject();
        return object;
    }

    public static void saveGroupNamesAndKeys(Context context, List<GroupNameAndKey> groupNameAndKeys){
        try {
            DeviceStorageManager.writeObject(context, FileNames.GROUP_NAMES_AND_KEYS, groupNameAndKeys);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<GroupNameAndKey> readGroupNamesAndKeys(Context context){
        try {
            return (List<GroupNameAndKey>) DeviceStorageManager.readObject(context,FileNames.GROUP_NAMES_AND_KEYS);
        } catch (FileNotFoundException e) {
            List<GroupNameAndKey> groupNameAndKeys = new ArrayList<>();
            DeviceStorageManager.saveGroupNamesAndKeys(context, groupNameAndKeys);
            return groupNameAndKeys;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void saveGroup(Context context, Group group){
        try {
            DeviceStorageManager.writeObject(context, group.getKey(), group);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Group readGroup(Context context, String groupKey){
        try {
            return (Group) DeviceStorageManager.readObject(context, groupKey);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
