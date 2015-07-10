package eu.sofomo.dcom.common;

import java.util.ArrayList;

/**
 * Created by jarek on 10.07.15.
 */
public class AvailableWords {

    private ArrayList<String> words = new ArrayList<String>();

    public AvailableWords() {
        words.add("cow");
        words.add("chicken");
        words.add("skiing");
        words.add("jumping");
        words.add("stop");
        words.add("computer");
        words.add("java");
    }

    public ArrayList<String> getWords()
    {
        return words;
    }

    public String get(int number) {
        return words.get(number);
    }
}
