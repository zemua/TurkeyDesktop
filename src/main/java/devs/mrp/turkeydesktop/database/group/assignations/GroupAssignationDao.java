package devs.mrp.turkeydesktop.database.group.assignations;

import devs.mrp.turkeydesktop.database.GeneralDao;
import java.sql.ResultSet;
import io.reactivex.rxjava3.core.Single;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

public interface GroupAssignationDao extends GeneralDao<GroupAssignation, GroupAssignationDao.ElementId> {
    
    public Single<ResultSet> findByElementId(GroupAssignation.ElementType elementType, String elementId);
    public Single<Long> deleteByElementId(GroupAssignation.ElementType elementType, String elementId);
    public Single<ResultSet> findAllElementTypeOfGroup(GroupAssignation.ElementType elementType, Long groupId);
    public Single<ResultSet> findAllOfType(GroupAssignation.ElementType elementType);
    public Single<Long> deleteByGroupId(long groupId);
    
    @EqualsAndHashCode
    @Getter
    @AllArgsConstructor
    @Builder
    public static class ElementId {
        GroupAssignation.ElementType type;
        String elementId;
    }
}
