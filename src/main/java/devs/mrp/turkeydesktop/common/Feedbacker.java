/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package devs.mrp.turkeydesktop.common;

/**
 *
 * @author miguel
 */
public interface Feedbacker<T, F> {
    public void addFeedbackListener(FeedbackListener<T, F> listener);
    public void giveFeedback(T tipo, F feedback);
}
