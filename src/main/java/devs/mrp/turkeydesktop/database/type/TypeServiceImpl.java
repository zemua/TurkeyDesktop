/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.type;

import devs.mrp.turkeydesktop.common.TurkeyAppFactory;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignation;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationDao;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.LongConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miguel
 */
public class TypeServiceImpl implements TypeService {
    
    TypeDao repo = TypeRepository.getInstance();
    private static final GroupAssignationDao assignationRepo = GroupAssignationRepository.getInstance();
    private static final Logger logger = Logger.getLogger(TypeServiceImpl.class.getName());

    @Override
    public void add(Type element, LongConsumer consumer) {
        if (element == null) {
            consumer.accept(-1);
        } else {
            // because H2 doesn't support INSERT OR REPLACE we have to check manually if it exists
            ResultSet rs = repo.findById(element.getProcess());
            try{
                if (rs.next()){
                    update(element, consumer);
                } else {
                    TurkeyAppFactory.runLongWorker(() -> repo.add(element), consumer);
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void update(Type element, LongConsumer consumer) {
        if (element == null || element.getProcess() == null) {
            consumer.accept(-1);
        } else {
            findById(element.getProcess(), saved -> {
                if (saved != null && saved.getType() != null && !saved.getType().equals(element.getType())) {
                    // if we are changing the type of the process, then remove from any existing groups
                    TurkeyAppFactory.runWorker(() -> {
                        assignationRepo.deleteByElementId(GroupAssignation.ElementType.PROCESS, element.getProcess());
                    });
                }
                TurkeyAppFactory.runLongWorker(() -> repo.update(element), consumer);
            });
        }
    }

    @Override
    public void findAll(Consumer<List<Type>> consumer) {
        TurkeyAppFactory.runResultSetWorker(() -> repo.findAll(), set -> {
            consumer.accept(listFromResultSet(set));
        });
    }
    
    @Override
    public void findByType(Type.Types type, Consumer<List<Type>> consumer) {
        TurkeyAppFactory.runResultSetWorker(() -> repo.findByType(type.toString()), res -> {
            consumer.accept(listFromResultSet(res));
        });
    }

    @Override
    public void findById(String id, Consumer<Type> consumer) {
        TurkeyAppFactory.runResultSetWorker(() -> repo.findById(id), set -> {
            Type type = new Type();
            try {
                if (set.next()) {
                    type.setProcess(set.getString(Type.PROCESS_NAME));
                    type.setType(Type.Types.valueOf(set.getString(Type.TYPE)));
                }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            consumer.accept(type);
        });
    }

    @Override
    public void deleteById(String id, LongConsumer consumer) {
        TurkeyAppFactory.runWorker(() -> assignationRepo.deleteByElementId(GroupAssignation.ElementType.PROCESS, id));
        TurkeyAppFactory.runLongWorker(() -> repo.deleteById(id), consumer);
    }
    
    private List<Type> listFromResultSet(ResultSet set) {
        List<Type> typeList = new ArrayList<>();
        try {
            while (set.next()) {
                Type type = new Type();
                type.setProcess(set.getString(Type.PROCESS_NAME));
                type.setType(Type.Types.valueOf(set.getString(Type.TYPE)));
                typeList.add(type);
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return typeList;
    }
    
}
