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
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author miguel
 */
public class TitleServiceImpl implements TitleService {

    private final TitleDao repo = TitleRepository.getInstance();
    private static final Logger logger = Logger.getLogger(TitleServiceImpl.class.getName());

    private static Map<String, Title> conditionsMap;

    public TitleServiceImpl() {
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
            element.setSubStr(element.getSubStr().toLowerCase());
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
            element.setSubStr(element.getSubStr().toLowerCase());
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
            logger.log(Level.SEVERE, null, ex);
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
        String lowerCaseSubStr = subStr.toLowerCase();
        if (conditionsMap.containsKey(lowerCaseSubStr)) {
            Title el = new Title();
            el.setSubStr(lowerCaseSubStr);
            el.setType(conditionsMap.get(lowerCaseSubStr).getType());
            return el;
        }
        return null;
    }

    @Override
    public long deleteBySubString(String subStr) {
        if (subStr == null) {
            return -1;
        }
        conditionsMap.remove(subStr.toLowerCase());
        return repo.deleteById(subStr.toLowerCase());
    }

    private Title elementFromResultSetEntry(ResultSet set) {
        Title el = new Title();
        try {
            el.setSubStr(set.getString(Title.SUB_STR).toLowerCase());
            el.setType(Title.Type.valueOf(set.getString(Title.TYPE)));
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return el;
    }

    @Override
    public List<Title> findContainedByAndNegativeFirst(String title) {
        return conditionsMap.entrySet().stream()
                .filter(e -> StringUtils.containsIgnoreCase(title, e.getKey()))
                .map(e -> e.getValue())
                .sorted((e1, e2) -> e2.getType().compareTo(e1.getType())) // "NEGATIVE" before "POSITIVE" in natural order
                .collect(Collectors.toList());
    }
    
    @Override
    public Title findLongestContainedBy(String title) {
        return conditionsMap.entrySet().stream()
                .filter(e -> StringUtils.containsIgnoreCase(title, e.getKey()))
                .max((e1, e2) -> Long.compare(e1.getKey().length(), e2.getKey().length()))
                .map(e -> e.getValue())
                .orElse(null);
    }

    @Override
    public long countTypesOf(Title.Type type, String title) {
        return conditionsMap.entrySet().stream()
                .filter(e -> StringUtils.containsIgnoreCase(title, e.getKey()))
                .filter(e -> e.getValue().getType().equals(type))
                .count();
    }

}
