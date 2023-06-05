/**
 * Sample Skeleton for 'examChooseQuestions.fxml' Controller Class
 */

package il.cshaifasweng.OCSFMediatorExample.client.Teacher;

import aidClasses.GlobalDataSaved;
import aidClasses.Message;
import aidClasses.aidClassesForTeacher.ExamQuestion;
import aidClasses.aidClassesForTeacher.QuestionsExamsID;
import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.SimpleClient;
import il.cshaifasweng.OCSFMediatorExample.entities.examBuliding.Exam;
import il.cshaifasweng.OCSFMediatorExample.entities.examBuliding.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExamChooseQuestions {

    @FXML // fx:id="buttonBack"
    private Button buttonBack; // Value injected by FXMLLoader

    @FXML // fx:id="buttonSubmit"
    private Button buttonSubmit; // Value injected by FXMLLoader

    @FXML // fx:id="course1"
    private Text course1; // Value injected by FXMLLoader
    @FXML // fx:id="examQuestionTable"
    private TableView<ExamQuestion> examQuestionTable; // Value injected by FXMLLoader

    @FXML // fx:id="examQuestionID"
    private TableColumn<ExamQuestion, String> examQuestionID; // Value injected by FXMLLoader


    @FXML // fx:id="examQuestionPoints"
    private TableColumn<ExamQuestion, TextField> examQuestionPoints; // Value injected by FXMLLoader


    @FXML // fx:id="examQuestionsTheQuestion"
    private TableColumn<ExamQuestion, Question> examQuestionsTheQuestion; // Value injected by FXMLLoader

    @FXML // fx:id="examQuestionToSelectTable"
    private TableView<Question> examQuestionToSelectTable; // Value injected by FXMLLoader

    @FXML // fx:id="examQuestionsTheQuestionToSelect"
    private TableColumn<Question, String> examQuestionsTheQuestionToSelect; // Value injected by FXMLLoader

    @FXML // fx:id="examQuestionIDToSelect"
    private TableColumn<Question, String> examQuestionIDToSelect; // Value injected by FXMLLoader

    @FXML // fx:id="timeLabel"
    private TextField timeLabel; // Value injected by FXMLLoader
    @FXML // fx:id="warning"
    private Text warning; // Value injected by FXMLLoader
    private List<Question> selectedQuestions=new ArrayList<>();

    private ObservableList<ExamQuestion> observableList;
    @FXML
    public void addQuestion(MouseEvent mouseEvent) {
        warning.setText("");
        if(examQuestionToSelectTable.getSelectionModel().getSelectedItem().getId()==-1)
        {
            return;
        }
        Question question = examQuestionToSelectTable.getSelectionModel().getSelectedItem();
        if (selectedQuestions.contains(question)) {
            return;
        }
        selectedQuestions.add(question);
        TextField textField=new TextField("0");
        textField.setStyle("-fx-alignment: CENTER;");
        observableList.add(new ExamQuestion(question.getQuestionID(), question, textField));
        examQuestionTable.setItems(observableList);
    }
    @FXML
    public void removeQuestion(MouseEvent mouseEvent) {
        warning.setText("");
        if(examQuestionTable.getSelectionModel().getSelectedItem()==null)
        {
            return;
        }
        ExamQuestion eq=examQuestionTable.getSelectionModel().getSelectedItem();
        Question question=eq.getQuestion();
        observableList.remove(eq);
        examQuestionTable.setItems(observableList);
        selectedQuestions.remove(question);
    }
    @FXML
    void backToBuildExam(ActionEvent event) throws IOException {
        App.setRoot("makeExam");
    }
    @FXML
    void submitExam(ActionEvent event) {
        if(selectedQuestions.size()==0)
        {
            warning.setText("no questions selected");
            return;
        }
        int points=0,sum=0,time=0;
        String pointsString;
        List<Integer> examPoints=new ArrayList<>();
        if(timeLabel.getText().equals(""))
        {
            warning.setText("the exam have no time");
            return;
        }
        try {
            time=Integer.valueOf(timeLabel.getText());
        }
        catch (Exception ex)
        {
            warning.setText("the exam have illegal time!");
            return;
        }
        if(time<=0)
        {
            warning.setText("the exam have none positive number time");
            return;
        }
        for(ExamQuestion examQuestion:examQuestionTable.getItems())
        {
            pointsString=examQuestion.getPoints().getText();
            if(pointsString.equals(""))
            {
                warning.setText("questionID =  "+examQuestion.getQuestionID()+" "+"have no points");
                return;
            }
            try {
                points=Integer.valueOf(pointsString);
            }
            catch (Exception ex)
            {
                warning.setText("questionID =  "+examQuestion.getQuestionID()+" "+"have illegal number of points!");
                return;
            }
            if(points<=0)
            {
                warning.setText("questionID =  "+examQuestion.getQuestionID()+" "+"have none positive number of points!");
                return;
            }
            if(points>100)
            {
                warning.setText("questionID =  "+examQuestion.getQuestionID()+" "+"have number of points above 100!");
                return;
            }
            sum+=points;
            examPoints.add(points);
        }
        if(sum!=100)
        {
            warning.setText("the exam have number of points above 100!");
            return;
        }
        Exam exam=new Exam(time,"","","");
        exam.setExam_ID(QuestionsExamsID.examID(MakeExam.selectedCourse.getCourseName(),MakeExam.selectedSubject.getId(),MakeExam.selectedCourse.getId()));
        List<Object>objects=new ArrayList<>();
        objects.add(exam);
        objects.add(GlobalDataSaved.connectedUser.getId());
        objects.add(MakeExam.selectedCourse.getId());
        objects.add(MakeExam.selectedSubject.getId());
        List<Integer> questionsIds=new ArrayList<>();
        for(Question question:selectedQuestions)
        {
            questionsIds.add(question.getId());
        }
        objects.add(questionsIds);
        objects.add(examPoints);
        Message msg = new Message("#addExam", objects); // creating a msg to the server demanding the students
        try {
            SimpleClient.getClient().sendToServer(msg); // sending the msg to the server
        }
        catch (IOException exception)
        {
            System.out.println(exception.getMessage());
        }

    }
    @FXML
    public void initialize()
    {
        warning.setText("");
        observableList = FXCollections.observableArrayList();
        //courseName.setStyle("-fx-alignment: CENTER;");
        examQuestionID.setCellValueFactory(new PropertyValueFactory<ExamQuestion, String>("questionID"));
        examQuestionsTheQuestion.setCellValueFactory(new PropertyValueFactory<ExamQuestion, Question>("question"));
        examQuestionsTheQuestion.setStyle("-fx-alignment: CENTER;");
        examQuestionPoints.setCellValueFactory(new PropertyValueFactory<ExamQuestion, TextField>("points"));

        ObservableList<Question> courseQuestions = FXCollections.observableArrayList();
        examQuestionsTheQuestionToSelect.setCellValueFactory(new PropertyValueFactory<Question, String>("studentNotes"));
        examQuestionsTheQuestionToSelect.setStyle("-fx-alignment: CENTER;");
        examQuestionIDToSelect.setCellValueFactory(new PropertyValueFactory<Question,String>("questionID"));
        courseQuestions.setAll(GlobalDataSaved.courseQuestionsForMakeExam);
        examQuestionToSelectTable.setItems(courseQuestions);
    }



}
