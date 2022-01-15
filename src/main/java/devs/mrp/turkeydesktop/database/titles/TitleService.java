/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.titles;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author miguel
 */
public class TitleService implements ITitleService {

    private final TitleDao repo = TitleRepository.getInstance();

    private static Map<String, Title> conditionsMap;

    public TitleService() {
        initConditionsMap();
    }

    private void initConditionsMap() {
        if (conditionsMap == null) {
            conditionsMap = new HashMap<>();
            retrieveAll(); // it assigns values to the hashmap inside the function
        }
    }

    @Override
    public long save(Title element) {
        if (element == null) {
            return -1;
        } else {
            if (conditionsMap.containsKey(element.getSubStr())) {
                if (conditionsMap.get(element.getSubStr()).getType() != element.getType()) {
                    // we have this value but is different, so we update
                    conditionsMap.put(element.getSubStr(), element);
                    return update(element);
                } else {
                    // else the value is the same as the one stored
                    return 0;
                }
            }
            // we don't have this value so we add new
            conditionsMap.put(element.getSubStr(), element);
            return repo.add(element);
        }
    }

    private long update(Title element) {
        if (element == null || element.getSubStr() == null) {
            return -1;
        } else {
            conditionsMap.put(element.getSubStr(), element);
            return repo.update(element);
        }
    }

    private List<Title> retrieveAll() {
        List<Title> elements = new ArrayList<>();
        ResultSet set = repo.findAll();
        try {
            while (set.next()) {
                Title el = elementFromResultSetEntry(set);
                elements.add(el);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TitleService.class.getName()).log(Level.SEVERE, null, ex);
        }
        conditionsMap.clear();
        elements.forEach(e -> conditionsMap.put(e.getSubStr(), e));
        return elements;
    }

    @Override
    public List<Title> findAll() {
        return conditionsMap.entrySet().stream()
                .map(e -> {
                    Title el = new Title();
                    el.setSubStr(e.getKey());
                    el.setType(e.getValue().getType());
                    return el;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Title findBySubString(String subStr) {
        if (conditionsMap.containsKey(subStr)) {
            Title el = new Title();
            el.setSubStr(subStr);
            el.setType(conditionsMap.get(subStr).getType());
            return el;
        }
        return null;
    }

    @Override
    public long deleteBySubString(String subStr) {
        if (subStr == null) {
            return -1;
        }
        conditionsMap.remove(subStr);
        return repo.deleteById(subStr);
    }

    private Title elementFromResultSetEntry(ResultSet set) {
        Title el = new Title();
        try {
            el.setSubStr(set.getString(Title.SUB_STR));
            el.setType(Title.Type.valueOf(set.getString(Title.TYPE)));
        } catch (SQLException ex) {
            Logger.getLogger(TitleService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return el;
    }

    @Override
    public List<Title> findContainedBy(String title) {
        return conditionsMap.entrySet().stream()
                .filter(e -> title.contains(e.getKey()))
                .map(e -> e.getValue())
                .collect(Collectors.toList());
    }

    @Override
    public long countTypesOf(Title.Type type, String title) {
        return conditionsMap.entrySet().stream()
                .filter(e -> title.contains(e.getKey()))
                .filter(e -> e.getValue().getType().equals(type))
                .count();
    }

}
