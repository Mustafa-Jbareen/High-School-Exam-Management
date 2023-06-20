package il.cshaifasweng.OCSFMediatorExample.client.Student;

import aidClasses.GlobalDataSaved;
import il.cshaifasweng.OCSFMediatorExample.client.App;
import il.cshaifasweng.OCSFMediatorExample.client.WordGeneratorFile;
import il.cshaifasweng.OCSFMediatorExample.entities.gradingSystem.Grade;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.IOException;

public class ExamStudentNotes {

    @FXML
    private Button backToGradesTable;

    @FXML
    private Button goToQuestions;

    @FXML
    private TextArea teacherNotes;

    private Grade grade;

    @FXML
    public void initialize() throws IOException {
        grade = GlobalDataSaved.currentGrade;
        if (GlobalDataSaved.copyToTeacher) {
            backToGradesTable.setText("Back To Exams");
        }
        if (grade.getTeacherNotes() == null || grade.getTeacherNotes().equals("")) {
            teacherNotes.setText("There Is No Notes");
        }
        else {
            teacherNotes.setText(grade.getTeacherNotes());
        }
        if(grade.isManuel())
        {
            goToQuestions.setText("Open Exam File");
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
    void goToQuestions(ActionEvent event) throws IOException {
        if(GlobalDataSaved.currentGrade.isManuel())
        {
            WordGeneratorFile.openWord(GlobalDataSaved.currentGrade.getExamCopy().getAnswers());
        }
        else {
            App.setRoot("examCopy");
        }

    }

}
