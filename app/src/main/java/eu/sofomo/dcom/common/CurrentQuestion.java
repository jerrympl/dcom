package eu.sofomo.dcom.common;

/**
 * Created by jarek on 10.07.15.
 */
public class CurrentQuestion {

    private String question;
    private String anwser;
    private int points = 5;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnwser() {
        return anwser;
    }

    public void setAnwser(String anwser) {
        this.anwser = anwser;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
