/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.type;

import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignation;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationDao;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import rx.Observable;
import rx.Single;

/**
 *
 * @author miguel
 */
public class TypeServiceImpl implements TypeService {
    
    TypeDao repo = TypeRepository.getInstance();
    private static final GroupAssignationDao assignationRepo = GroupAssignationRepository.getInstance();
    private static final Logger logger = Logger.getLogger(TypeServiceImpl.class.getName());

    @Override
    public Single<Long> add(Type element) {
        if (element == null) {
            return Single.just(-1L);
        } else {
            // because H2 doesn't support INSERT OR REPLACE we have to check manually if it exists
            return repo.findById(element.getProcess()).flatMap(rs -> {
                try{
                    if (rs.next()){
                        return update(element);
                    }
                } catch (SQLException ex) {
                    logger.log(Level.SEVERE, null, ex);
                }
                return repo.add(element);
            });
        }
    }

    @Override
    public Single<Long> update(Type element) {
        if (element == null || element.getProcess() == null) {
            return Single.just(-1L);
        } else {
            return findById(element.getProcess()).flatMap(saved -> {
                if (saved != null && saved.getType() != null && !saved.getType().equals(element.getType())) {
                    // if we are changing the type of the process, then remove from any existing groups
                    return assignationRepo.deleteByElementId(GroupAssignation.ElementType.PROCESS, element.getProcess())
                            .flatMap(r -> repo.update(element));
                }
                return repo.update(element);
            });
        }
    }

    @Override
    public Observable<Type> findAll() {
        return repo.findAll().flatMapObservable(this::listFromResultSet);
    }
    
    @Override
    public Observable<Type> findByType(Type.Types type) {
        return repo.findByType(type.toString()).flatMapObservable(this::listFromResultSet);
    }

    @Override
    public Single<Type> findById(String id) {
        return repo.findById(id).map(set -> {
            Type type = new Type();
            try {
                if (set.next()) {
                    type.setProcess(set.getString(Type.PROCESS_NAME));
                    type.setType(Type.Types.valueOf(set.getString(Type.TYPE)));
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return type;
        });
    }

    @Override
    public Single<Long> deleteById(String id) {
        return assignationRepo.deleteByElementId(GroupAssignation.ElementType.PROCESS, id)
                .flatMap(r -> repo.deleteById(id));
    }
    
    private Observable<Type> listFromResultSet(ResultSet set) {
        return Observable.create(submitter -> {
            try {
                while (set.next()) {
                    Type type = new Type();
                    type.setProcess(set.getString(Type.PROCESS_NAME));
                    type.setType(Type.Types.valueOf(set.getString(Type.TYPE)));
                    submitter.onNext(type);
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            submitter.onCompleted();
        });
    }
    
}
