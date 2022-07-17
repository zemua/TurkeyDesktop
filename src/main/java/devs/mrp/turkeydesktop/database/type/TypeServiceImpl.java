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
import java.util.ArrayList;
import java.util.List;
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
    public long add(Type element) {
        if (element == null) {
            return -1;
        } else {
            // because H2 doesn't support INSERT OR REPLACE we have to check manually if it exists
            ResultSet rs = repo.findById(element.getProcess());
            try{
                if (rs.next()){
                return update(element);
            }
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
            return repo.add(element);
        }
    }

    @Override
    public long update(Type element) {
        if (element == null || element.getProcess() == null) {
            return -1;
        } else {
            Type saved = findById(element.getProcess());
            if (saved != null && saved.getType() != null && !saved.getType().equals(element.getType())) {
                // if we are changing the type of the process, then remove from any existing groups
                assignationRepo.deleteByElementId(GroupAssignation.ElementType.PROCESS, element.getProcess());
            }
            return repo.update(element);
        }
    }

    @Override
    public List<Type> findAll() {
        return listFromResultSet(repo.findAll());
    }
    
    @Override
    public List<Type> findByType(Type.Types type) {
        return listFromResultSet(repo.findByType(type.toString()));
    }

    @Override
    public Type findById(String id) {
        ResultSet set = repo.findById(id);
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
    }

    @Override
    public long deleteById(String id) {
        assignationRepo.deleteByElementId(GroupAssignation.ElementType.PROCESS, id);
        return repo.deleteById(id);
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
