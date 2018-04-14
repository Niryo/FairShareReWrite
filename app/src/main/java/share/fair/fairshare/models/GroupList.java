package share.fair.fairshare.models;

import java.util.ArrayList;
import java.util.List;

import share.fair.fairshare.services.FireBaseServerApi;

/**
 * Created by niryo on 29/09/2017.
 */

public class GroupList {
    private List<Group> groups = new ArrayList<>();
    private  FireBaseServerApi cloudApi;

    public GroupList(FireBaseServerApi cloudApi) {
        this.cloudApi= cloudApi;
    }

    public List<String> getAllGroupsIds() {
        List<String> groupIds = new ArrayList<>();
        for(Group group: this.groups){
            groupIds.add(group.getKey());
        }
        return groupIds;
    }

    public void createNewGroup (String groupName){
            Group newGroup = new Group(groupName);
            this.groups.add(newGroup);
            this.cloudApi.addGroup(newGroup.getKey(), groupName);
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
            if(group.getKey().equals(groupId)) {
                return group;
            }
        }
        return null;
    }

    public void removeGroup(Group group) {
        this.groups.remove(group);
    }
}
