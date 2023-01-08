package devs.mrp.turkeydesktop.database.group.expor;

import devs.mrp.turkeydesktop.common.DbCache;
import devs.mrp.turkeydesktop.common.GenericWorker;
import devs.mrp.turkeydesktop.common.SingleConsumer;
import io.reactivex.rxjava3.core.Observable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExportedGroupFactory {
    
    private static Supplier<DbCache<ExportedGroupId,ExportedGroup>> dbCacheSupplier;

    public static void setDbCacheSupplier(Supplier<DbCache<ExportedGroupId, ExportedGroup>> dbCacheSupplier) {
        ExportedGroupFactory.dbCacheSupplier = dbCacheSupplier;
    }
    
    public static DbCache<ExportedGroupId,ExportedGroup> getDbCache() {
        return dbCacheSupplier.get();
    }
    
    public static ExportedGroupService getService() {
        return new ExportedGroupServiceImpl();
    }
    
    public static void runExportedGroupWorker(Supplier<ExportedGroup> supplier, Consumer<ExportedGroup> consumer) {
        new GenericWorker<ExportedGroup>().runWorker(supplier, consumer);
    }
    
    public static void runExportedGroupListWorker(Supplier<List<ExportedGroup>> supplier, Consumer<List<ExportedGroup>> consumer) {
        new GenericWorker<List<ExportedGroup>>().runWorker(supplier, consumer);
    }
    
    public static Consumer<ExportedGroup> exportedGroupConsumer(Consumer<ExportedGroup> consumer) {
        return new SingleConsumer<>(consumer);
    }
    
    public static Consumer<List<ExportedGroup>> exportedGroupListConsumer(Consumer<List<ExportedGroup>> consumer) {
        return new SingleConsumer<>(consumer);
    }
    
    public static Observable<ExportedGroup> elementsFromResultSet(ResultSet set) {
        return Observable.create(subscribe -> {
            try {
                while (set.next()) {
                    subscribe.onNext(elementFromResultSetEntry(set));
                }
            } catch (SQLException ex) {
                subscribe.onError(ex);
            }
            subscribe.onComplete();
        });
    }

    private static ExportedGroup elementFromResultSetEntry(ResultSet set) {
        ExportedGroup el = new ExportedGroup();
        try {
            el.setGroup(set.getLong(ExportedGroup.GROUP));
            el.setFile(set.getString(ExportedGroup.FILE));
            el.setDays(set.getLong(ExportedGroup.DAYS));
        } catch (SQLException ex) {
            log.error("Error extracting ExportedGroup from ResultSet", ex);
        }
        return el;
    }
    
}
