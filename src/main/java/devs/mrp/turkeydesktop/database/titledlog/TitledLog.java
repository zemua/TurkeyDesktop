/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.database.titledlog;

import devs.mrp.turkeydesktop.database.titles.Title;
import java.util.List;

/**
 *
 * @author miguel
 */
public class TitledLog {
    
    private String title;
    private long elapsed;
    private List<Title> conditions;
    private long qtyPositives;
    private long qtyNegatives;
    private long qtyNeutral;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getElapsed() {
        return elapsed;
    }

    public void setElapsed(long elapsed) {
        this.elapsed = elapsed;
    }

    public List<Title> getConditions() {
        return conditions;
    }

    public void setConditions(List<Title> conditions) {
        this.conditions = conditions;
    }

    public long getQtyPositives() {
        return qtyPositives;
    }

    public void setQtyPositives(long qtyPositives) {
        this.qtyPositives = qtyPositives;
    }

    public long getQtyNegatives() {
        return qtyNegatives;
    }

    public void setQtyNegatives(long qtyNegatives) {
        this.qtyNegatives = qtyNegatives;
    }
    
    public void setQtyNeutral(long qtyNeutral) {
        this.qtyNeutral = qtyNeutral;
    }
    
    public long getQtyNeutral() {
        return qtyNeutral;
    }
    
}
