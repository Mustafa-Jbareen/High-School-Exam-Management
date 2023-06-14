package aidClasses;

import aidClasses.aidClassesForTeacher.DisplayGrade;
import il.cshaifasweng.OCSFMediatorExample.entities.ManyToMany.Exam_Question;
import il.cshaifasweng.OCSFMediatorExample.entities.appUsers.Principal;
import il.cshaifasweng.OCSFMediatorExample.entities.appUsers.Student;
import il.cshaifasweng.OCSFMediatorExample.entities.appUsers.Teacher;
import il.cshaifasweng.OCSFMediatorExample.entities.appUsers.User;
import il.cshaifasweng.OCSFMediatorExample.entities.educational.Course;
import il.cshaifasweng.OCSFMediatorExample.entities.educational.Subject;
import il.cshaifasweng.OCSFMediatorExample.entities.examBuliding.ComputerizedExamToExecute;
import il.cshaifasweng.OCSFMediatorExample.entities.examBuliding.Exam;
import il.cshaifasweng.OCSFMediatorExample.entities.examBuliding.ExamToExecute;
import il.cshaifasweng.OCSFMediatorExample.entities.examBuliding.Question;
import il.cshaifasweng.OCSFMediatorExample.entities.gradingSystem.Copy;
import il.cshaifasweng.OCSFMediatorExample.entities.gradingSystem.Grade;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;

public class GlobalDataSaved {

    public static User connectedUser;
    public static List<Subject> teacherSubjects=new ArrayList<>();
    public static List<Course> teacherCourses=new ArrayList<>();
    public static List<Grade> gradeList;
    public static boolean AddFlag = true;
    public static ObservableList<Subject> subjects = FXCollections.observableArrayList();

    public static ExamToExecute examToExecute;
    public static List<Question> questionList;
    public static List<String> studentAnswers;
    public static int changeTextCounter = 0;
    public static int selectedGradeForExamCopy = 0;
    public static Grade currentGrade = new Grade();
    public static Copy currentCopy = new Copy();

    public static List<Exam> allExamsForTeacher;
    public static List<Exam_Question> examQuestionsToShowPrepare;
    public static List<Exam> allExamsForPrincipal = new ArrayList<>();
    public static List<Question> allQuestionsForPrincipal = new ArrayList<>();
    public static List<Grade> allGradesForPrincipal = new ArrayList<>();

    public static Question PrincipalQuestionToShow = null;
    public static Exam PrincipalExamToShow = null;

    public static List<Question> courseQuestionsForMakeExam;
    public static List<ExamToExecute> teacherExamsToApprove;
    public static List<Grade> compExamGrades;
    public static boolean ThePrincipalInQuestions = false;
    public static boolean ThePrincipalInExams = false;
    public static boolean ThePrincipalInGrades = false;
    public static boolean ThePrincipalInExtraTime = false;
    public static List<Course> allCoursesForPrincipal = new ArrayList<>();
    public static List<Student> allStudentsForPrincipal = new ArrayList<>();
    public static List<Teacher> allTeachersForPrincipal = new ArrayList<>();
    public static ObservableList<Exam> allStatisticalDataExamsForPrincipal = FXCollections.observableArrayList();
    public static String CurrentStatisticalCatagory = "Course";

    public static Teacher StatisticalTeacherForPrincipal = null;
    public static Course StatisticalCourseForPrincipal = null;
    public static Student StatisticalStudentForPrincipal = null;

    public static List<Subject> allSubjects;
    public static List<ExamToExecute> getTeacherExamsToExecutes;
    public static int forQuestion=0;
    public static boolean isManualToApprove;
    public static List<ExamToExecute> allExamsToExecuteForPrincipal = new ArrayList<>();
    public static List<Question> allQuestionsForTeacher;
    public static Question selectedQuestionsToCopy;
    public static Subject selectedSubjectToCopy;
    public static List<Exam> allExamsForTeacherToCopy;
    public static Exam selectedExamToCopy;
    public static List<Question> allQuestionsForExamToCopy;
    public static List<Exam> allTeacherExamsCreated;
    public static List<Grade> allTeacherExamGrades;
    public static List<Question> principalExamToShowQuestions;
    public static boolean copyToStudent;
    public static boolean copyToTeacher;
    public static boolean copyToPrincipal;
    public static int currentGradeId;
    public static int currentCopyId;
    public static ObservableList<DisplayGrade> observableListForTeacherToApprove;
}
