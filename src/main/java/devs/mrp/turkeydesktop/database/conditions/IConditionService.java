/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.conditions;

import java.util.List;

/**
 *
 * @author miguel
 */
public interface IConditionService {
    public long add(Condition element);
    public long update(Condition element);
    public List<Condition> findAll();
    public List<Condition> findByGroupId(Long groupId);
    public Condition findById(Long id);
    public long deleteById(Long id);
    public long deleteByGroupId(long id);
    public long deleteByTargetId(long id);
}
