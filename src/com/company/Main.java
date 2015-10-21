package com.company;

public class Main {

    public static void main(String[] args) {
        String[] spamKeys = {"спам","привет"};

        TextAnalyzer[] textAnalyzers = {new SpamAnalyzer(spamKeys), new NegativeTextAnalyzer(), new TooLongTextAnalyzer(10)};

        System.out.println(checkLabels(textAnalyzers, "Это весело"));
        System.out.println(checkLabels(textAnalyzers, "23232312 :( "));
        System.out.println(checkLabels(textAnalyzers, "23232312 23232312 23232312 23232312 "));
        System.out.println(checkLabels(textAnalyzers, "23232312 23232312 23232312 23232312 привет"));
    }

    static Label checkLabels(TextAnalyzer[] analyzers, String text) {
        for (TextAnalyzer analyzer : analyzers) {
            if (!analyzer.processText(text).equals(Label.OK)) {
                return analyzer.processText(text);
            }
        }
        return Label.OK;
    }
}


abstract class KeywordAnalyzer implements TextAnalyzer {
    protected abstract String[] getKeywords();

    protected abstract Label getLabel();

    @Override //TODO
    public Label processText(String text) {
        for (String keyword : getKeywords()) {
            if (text.contains(keyword)) {
                return getLabel();
            }
        }
        return Label.OK;
    }
}

class SpamAnalyzer extends KeywordAnalyzer{
    private String[] keywords;


    public SpamAnalyzer(String[] keywords) {
        this.keywords = keywords;
    }

    @Override
    protected String[] getKeywords() {
        return keywords;
    }

    @Override
    protected Label getLabel() {
        return Label.SPAM;
    }
}

class NegativeTextAnalyzer extends KeywordAnalyzer{
    private String[] keywords = {":(", "=(", ":|"};

    public NegativeTextAnalyzer() {
    }

    @Override
    protected String[] getKeywords() {
        return keywords;
    }

    @Override
    protected Label getLabel() {
        return Label.NEGATIVE_TEXT;
    }
}

class TooLongTextAnalyzer implements TextAnalyzer {
    int maxLength;

    public TooLongTextAnalyzer(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public Label processText(String text) {
        if (text.length()<=maxLength)
            return Label.OK;
        return Label.TOO_LONG;
    }
}

interface TextAnalyzer {
    Label processText(String text);
}

enum Label {
    SPAM, NEGATIVE_TEXT, TOO_LONG, OK
}

