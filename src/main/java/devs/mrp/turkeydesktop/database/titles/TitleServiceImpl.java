/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.titles;

import devs.mrp.turkeydesktop.common.WorkerFactory;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignation;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationDao;
import devs.mrp.turkeydesktop.database.group.assignations.GroupAssignationRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.LongConsumer;
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
    private static final GroupAssignationDao assignationRepo = GroupAssignationRepository.getInstance();
    private static final Logger logger = Logger.getLogger(TitleServiceImpl.class.getName());

    private static Map<String, Title> conditionsMap;

    public TitleServiceImpl() {
        initConditionsMap();
    }

    private void initConditionsMap() {
        if (conditionsMap == null) {
            conditionsMap = new ConcurrentHashMap<>();
            retrieveAll(r -> {}); // it assigns values to the hashmap inside the function
        }
    }

    @Override
    public void save(Title element, LongConsumer consumer) {
        if (element == null) {
            consumer.accept(-1);
        } else {
            element.setSubStr(element.getSubStr().toLowerCase());
            if (conditionsMap.containsKey(element.getSubStr())) {
                if (conditionsMap.get(element.getSubStr()).getType() != element.getType()) {
                    // we have this value but is different, so we update
                    conditionsMap.put(element.getSubStr(), element);
                    consumer.accept(update(element));
                } else {
                    // else the value is the same as the one stored
                    consumer.accept(0);
                }
            } else {
                // we don't have this value so we add new
                conditionsMap.put(element.getSubStr(), element);
                WorkerFactory.runLongWorker(() -> repo.add(element), consumer);
            }
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

    private void retrieveAll(Consumer<List<Title>> consumer) {
        WorkerFactory.runResultSetWorker(() -> repo.findAll(), set -> {
            WorkerFactory.runWorker(() -> {
                List<Title> elements = new ArrayList<>();
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
                consumer.accept(elements);
            });
        });
    }

    @Override
    public void findAll(Consumer<List<Title>> consumer) {
        TitleServiceFactory.runTitleListWorker(() -> conditionsMap.entrySet().stream()
                .map(e -> {
                    Title el = new Title();
                    el.setSubStr(e.getKey());
                    el.setType(e.getValue().getType());
                    return el;
                })
                .collect(Collectors.toList()), consumer);
    }

    @Override
    public void findBySubString(String subStr, Consumer<Title> consumer) {
        String lowerCaseSubStr = subStr.toLowerCase();
        if (conditionsMap.containsKey(lowerCaseSubStr)) {
            Title el = new Title();
            el.setSubStr(lowerCaseSubStr);
            el.setType(conditionsMap.get(lowerCaseSubStr).getType());
            consumer.accept(el);
        } else {
            consumer.accept(null);
        }
    }

    @Override
    public void deleteBySubString(String subStr, LongConsumer consumer) {
        if (subStr == null) {
            consumer.accept(-1);
        } else {
            String lowerCased = subStr.toLowerCase();
            conditionsMap.remove(lowerCased);
            assignationRepo.deleteByElementId(GroupAssignation.ElementType.TITLE, lowerCased);
            WorkerFactory.runLongWorker(() -> repo.deleteById(lowerCased), consumer);
        }
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
    public void findContainedByAndNegativeFirst(String title, Consumer<List<Title>> consumer) {
        TitleServiceFactory.runTitleListWorker(() -> conditionsMap.entrySet().stream()
                .filter(e -> StringUtils.containsIgnoreCase(title, e.getKey()))
                .map(e -> e.getValue())
                .sorted((e1, e2) -> e2.getType().compareTo(e1.getType())) // "NEGATIVE" before "POSITIVE" in natural order
                .collect(Collectors.toList()), consumer);
    }
    
    @Override
    public void findLongestContainedBy(String title, Consumer<Title> consumer) {
        TitleServiceFactory.runTitleWorker(() -> conditionsMap.entrySet().stream()
                .filter(e -> StringUtils.containsIgnoreCase(title, e.getKey()))
                .max((e1, e2) -> Long.compare(e1.getKey().length(), e2.getKey().length()))
                .map(e -> e.getValue())
                .orElse(null),
                consumer);
    }

    @Override
    public void countTypesOf(Title.Type type, String title, LongConsumer consumer) {
        WorkerFactory.runLongWorker(() -> conditionsMap.entrySet().stream()
                .filter(e -> StringUtils.containsIgnoreCase(title, e.getKey()))
                .filter(e -> e.getValue().getType().equals(type))
                .count(),
                consumer);
    }

}
