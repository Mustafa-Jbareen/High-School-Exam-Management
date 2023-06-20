package il.cshaifasweng.OCSFMediatorExample.client.Student;


import aidClasses.GlobalDataSaved;
import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.entities.examBuliding.Question;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExamCopy {

    @FXML
    private Button backToGradesTable;

    @FXML
    private RadioButton firstChoice;
    @FXML
    private RadioButton secondChoice;
    @FXML
    private RadioButton thirdChoice;
    @FXML
    private RadioButton fourthChoice;

    @FXML
    private Button nextQuestion;

    @FXML
    private Button prevQuestion;

    @FXML
    private ToggleGroup questionChoices;

    @FXML
    private Text questionContent;

    @FXML
    private Text questionNo;

    @FXML
    private Text questionStatus;

    @FXML
    private Text correctAnswer;

    public List<Question> examQuestions = new ArrayList<>();
    private int questionCounter = 0;
    public List<String> choicesList;
    public List<Question> questionList;
    public List<String> studentAnswers;
    @FXML
    public void initialize() throws IOException {
        examQuestions = GlobalDataSaved.questionList;
        questionList = sortedQuestionsList(examQuestions);
        studentAnswers = GlobalDataSaved.studentAnswers;
        question();
    }

    void question() throws IOException {
        Question q = questionList.get(questionCounter);
        getCurrentQuestion(q);
    }

    public List<Question> sortedQuestionsList(List<Question> list)
    {
        List<Question> questionList = new ArrayList<>();

        for(Question exam_question:list) {
            questionList.add(exam_question);
        }

        Collections.sort(questionList, new sortById());

        return questionList;
    }

    void getCurrentQuestion(Question question)
    {
        firstChoice.setDisable(true);
        secondChoice.setDisable(true);
        thirdChoice.setDisable(true);
        fourthChoice.setDisable(true);

        firstChoice.setSelected(false);
        secondChoice.setSelected(false);
        thirdChoice.setSelected(false);
        fourthChoice.setSelected(false);


        if (questionCounter == 0){
            prevQuestion.setDisable(true);
            nextQuestion.setDisable(false);
        }
        else {
            prevQuestion.setDisable(false);
            nextQuestion.setDisable(false);
        }
        if (questionCounter + 1 == examQuestions.size()){
            nextQuestion.setDisable(true);
        }
        choicesList = question.getChoices();
        questionContent.setText(question.getStudentNotes());
        questionNo.setText("Question "+ (questionCounter+1) + ":");
        firstChoice.setText(choicesList.get(0));
        secondChoice.setText(choicesList.get(1));
        thirdChoice.setText(choicesList.get(2));
        fourthChoice.setText(choicesList.get(3));

        if (studentAnswers.get(questionCounter) != null) {
            setButtonSelectedBefore(studentAnswers,questionCounter,firstChoice,secondChoice,thirdChoice,fourthChoice);
        }
        Question q;
        q = questionList.get(questionCounter);
        if (q.getCorrectChoice().equals(GlobalDataSaved.studentAnswers.get(questionCounter))) {
            questionStatus.setText("Correct");
            questionStatus.setFill(Paint.valueOf("#04a90c"));
        }
        else {
            questionStatus.setText("Incorrect");
            questionStatus.setFill(Paint.valueOf("#ff0000"));
        }
        correctAnswer.setText(q.getCorrectChoice());

    }

    public static void setButtonSelectedBefore(List<String> studentAnswers, int questionCounter, RadioButton firstChoice,
                                               RadioButton secondChoice, RadioButton thirdChoice, RadioButton fourthChoice) {
        if (studentAnswers.get(questionCounter).equals(firstChoice.getText())) {
            firstChoice.setSelected(true);
        } else if (studentAnswers.get(questionCounter).equals(secondChoice.getText())) {
            secondChoice.setSelected(true);
        } else if (studentAnswers.get(questionCounter).equals(thirdChoice.getText())) {
            thirdChoice.setSelected(true);
        } else if (studentAnswers.get(questionCounter).equals(fourthChoice.getText())) {
            fourthChoice.setSelected(true);
        }
    }
    @FXML
    void backToGradesTable(ActionEvent event) throws IOException {
        if (GlobalDataSaved.copyToStudent){
            App.setRoot("studentGrades");
        }
        else if (GlobalDataSaved.copyToPrincipal){
            App.setRoot("principalGrades");
        }
        else if (GlobalDataSaved.copyToTeacher) {
            App.setRoot("approvement");
        }
    }

    @FXML
    void goToNextQuestion(ActionEvent event) throws IOException {
        questionCounter++;
        question();
    }

    @FXML
    void goToPreviousQuestion(ActionEvent event) throws IOException {
        questionCounter--;
        question();
    }


}