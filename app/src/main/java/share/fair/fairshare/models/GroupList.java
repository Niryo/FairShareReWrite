package share.fair.fairshare.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by niryo on 29/09/2017.
 */

public class GroupList {
    private List<Group> groups = new ArrayList<>();


    public List<String> getAllGroupsIds() {
        List<String> groupIds = new ArrayList<>();
        for(Group group: this.groups){
            groupIds.add(group.getId());
        }
        return groupIds;
    }

    public void createNewGroup (String GroupName){
            Group newGroup = new Group(GroupName);
            this.groups.add(newGroup);
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Group> getGroups() {
        return this.groups;
    }

    public List<String> getAllGroupsNames() {
        List<String> groupsNames = new ArrayList<>();
        for(Group group: this.groups){
            groupsNames.add(group.getName());
        }
        return groupsNames;
    }

    public Group getGroupById(String groupId) {
        for(Group group: this.groups) {
            if(group.getId().equals(groupId)) {
                return group;
            }
        }
        return null;
    }
}
