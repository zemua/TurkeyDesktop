/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.view.categorizetitles;

/**
 *
 * @author miguel
 */
public enum CategorizeTitlesFilter {
    FILTER_ALL(0), FILTER_NOT_CATEGORIZED(1), FILTER_POSITIVE(2), FILTER_NEGATIVE(3), FILTER_NEUTRAL(4);
    
    private int filter;

    public int getFilter() {
        return filter;
    }

    public void setFilter(int filter) {
        this.filter = filter;
    }
    
    CategorizeTitlesFilter(int filter) {
        this.filter = filter;
    }
}
