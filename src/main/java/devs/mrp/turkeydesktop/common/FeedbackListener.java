package devs.mrp.turkeydesktop.common;

public interface FeedbackListener<T, F> {
    public void giveFeedback(T tipo, F feedback);
}
