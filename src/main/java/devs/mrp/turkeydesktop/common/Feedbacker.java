package devs.mrp.turkeydesktop.common;

public interface Feedbacker<T, F> {
    public void addFeedbackListener(FeedbackListener<T, F> listener);
    public void giveFeedback(T tipo, F feedback);
}
