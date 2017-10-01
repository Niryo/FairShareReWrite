package share.fair.fairshare;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import share.fair.fairshare.models.Group;
import share.fair.fairshare.models.GroupList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by niryo on 29/09/2017.
 */

public class GroupListTest {
    private GroupList groupList;
//    private List<Group> mockGroupList;

    @Before
    public void init(){
        this.groupList = new GroupList();
        Group group1 = new Group("hello");
        Group group2 = new Group("world");
//        this.mockGroupList = new ArrayList<>();
//        mockGroupList.add(group1);
//        mockGroupList.add(group2);
//        groupList.setGroups(mockGroupList);
    }
    @Test
    public void getAllGroupsIds(){
        Group group1 = new Group("id1","group1");
        Group group2 = new Group("id2","group2");
        List<Group> groups = new ArrayList<>();
        groups.add(group1);
        groups.add(group2);
        groupList.setGroups(groups);
        assertEquals(groupList.getAllGroupsIds().get(0), "id1");
        assertEquals(groupList.getAllGroupsIds().get(1), "id2");
    }

    @Test
    public void setGroups(){
        Group group1 = new Group("group1");
        Group group2 = new Group("group2");
        List groups = new ArrayList<>();
        groups.add(group1);
        groups.add(group2);
        groupList.setGroups(groups);
        assertEquals(groupList.getGroups(), groups);
    }

    @Test
    public void createNewGroup(){
        groupList.createNewGroup("testName");
        assertEquals(groupList.getAllGroupsIds().size(), 1);
    }


    @Test
    public void getAllGroupNames() {
        Group group1 = new Group("group1");
        Group group2 = new Group("group2");
        List groups = new ArrayList<>();
        groups.add(group1);
        groups.add(group2);
        groupList.setGroups(groups);
        assertEquals(groupList.getAllGroupsNames().get(0), "group1");
        assertEquals(groupList.getAllGroupsNames().get(1), "group2");
    }

    @Test
    public void getGroupById() {
        Group group = new Group("testId","group1");
        List groups = new ArrayList<>();
        groups.add(group);
        groupList.setGroups(groups);
        assertEquals(groupList.getGroupById("testId"), group);
    }

}
