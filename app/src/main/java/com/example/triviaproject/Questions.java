package com.example.triviaproject;

public class Questions {
    private String answer,question,category;

    //Constructor
    public Questions  (String question, String answer, String category)
    {
        this.question = question;
        this.answer = answer;
        this.category = category;
    }
    // Getters
    public String getQuestion() { return question; }
    public String getAnswer() { return answer; }
    public String getCategory() { return category; }

    // Setters
    public void setQuestion(String questions) {  question = questions; }
    public void setAnswer(String answers) {  answer = answers; }
    public void setCategory(String categories) {  category = categories; }


}