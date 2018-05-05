//package share.fair.fairshare;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import share.fair.fairshare.models.Group;
//import share.fair.fairshare.services.FireBaseServerApi;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotEquals;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//
///**
// * Created by niryo on 29/09/2017.
// */
//
//@RunWith(MockitoJUnitRunner.class)
//public class GroupListTest {
//    private GroupList groupList;
////    private List<Group> mockGroupList;
//
//    @Mock
//    FireBaseServerApi fireBaseServerApi;
//
//    @Before
//    public void init(){
//        this.groupList = new GroupList(fireBaseServerApi);
//
//        Group group1 = new Group("hello");
//        Group group2 = new Group("world");
////        this.mockGroupList = new ArrayList<>();
////        mockGroupList.add(group1);
////        mockGroupList.add(group2);
////        groupList.setGroups(mockGroupList);
//    }
//    @Test
//    public void getAllGroupsIds(){
//        Group group1 = new Group("id1","group1");
//        Group group2 = new Group("id2","group2");
//        List<Group> groups = new ArrayList<>();
//        groups.add(group1);
//        groups.add(group2);
//        groupList.setGroups(groups);
//        assertEquals(groupList.getAllGroupsIds().get(0), "id1");
//        assertEquals(groupList.getAllGroupsIds().get(1), "id2");
//    }
//
//    @Test
//    public void setGroups(){
//        Group group1 = new Group("group1");
//        Group group2 = new Group("group2");
//        List groups = new ArrayList<>();
//        groups.add(group1);
//        groups.add(group2);
//        groupList.setGroups(groups);
//
//        +++
//        assertEquals(groupList.getGroups(), groups);
//    }
//
//    @Test
//    public void createNewGroup(){
//        groupList.createNewGroup("testName");
//        assertEquals(groupList.getAllGroupsIds().size(), 1);
//        String groupKey = groupList.getGroups().get(0).getKey();
//        verify(fireBaseServerApi, times(1)).addGroup(groupKey, "testName");
//    }
//
//
//    @Test
//    public void getAllGroupNames() {
//        Group group1 = new Group("group1");
//        Group group2 = new Group("group2");
//        List groups = new ArrayList<>();
//        groups.add(group1);
//        groups.add(group2);
//        groupList.setGroups(groups);
//        assertEquals(groupList.getAllGroupsNames().get(0), "group1");
//        assertEquals(groupList.getAllGroupsNames().get(1), "group2");
//    }
//
//    @Test
//    public void getGroupById() {
//        Group group = new Group("testId","group1");
//        List groups = new ArrayList<>();
//        groups.add(group);
//        groupList.setGroups(groups);
//        assertEquals(groupList.getGroupById("testId"), group);
//    }
//
//
//    @Test
//    public void removeGroup() {
//        Group group1 = new Group("group1");
//        Group group2 = new Group("group2");
//        List groups = new ArrayList<>();
//        groups.add(group1);
//        groups.add(group2);
//        groupList.setGroups(groups);
//        groupList.removeGroup(group1);
//        assertEquals(groupList.getGroups().size(), 1);
//        assertEquals(groupList.getGroups().get(0), group2);
//    }
//
//}
