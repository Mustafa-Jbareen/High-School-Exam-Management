package il.cshaifasweng.OCSFMediatorExample.entities.examBuliding;
import il.cshaifasweng.OCSFMediatorExample.entities.appUsers.Teacher;
import il.cshaifasweng.OCSFMediatorExample.entities.gradingSystem.Copy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "examsToExecute")
public class ExamToExecute implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String dateOfExam;
    private String code;
    private int examAverage;
    private int numOfStudentDoing;
    @Column(name = "inTime")
    private int numberOfStudentDoneInTime;
    @Column(name = "notInTime")
    private int numberOfStudentNotDoneInTime;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exam_id")
    private Exam exam;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "compExamToExecute")
    private Set<Copy> copies=new HashSet<>();
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id")
    private Teacher teacherThatExecuted;
    private int isExtraNeeded; // 0-> unrequested, 1-> requested, 2-> approved
    private int extraTime;
    public ExamToExecute(String dateOfExam,String code) {
        this.examAverage = 0;
        this.numberOfStudentDoneInTime = 0;
        this.numberOfStudentNotDoneInTime = 0;
        this.code=code;
        this.dateOfExam=dateOfExam;
        this.numOfStudentDoing=0;
        isExtraNeeded = 0;
        extraTime = 0;
    }
    public ExamToExecute(ExamToExecute ex)
    {
        this.id=ex.id;
        this.examAverage = ex.examAverage;
        this.numberOfStudentDoneInTime = ex.numberOfStudentDoneInTime;
        this.numberOfStudentNotDoneInTime = ex.numberOfStudentNotDoneInTime;
        this.code=ex.code;
        this.dateOfExam=ex.dateOfExam;
        this.numOfStudentDoing=ex.numOfStudentDoing;
        isExtraNeeded = ex.isExtraNeeded;
        extraTime = ex.extraTime;
    }

    public ExamToExecute() {

    }

    public void setIsExtraNeeded(int isExtraNeeded) {
        this.isExtraNeeded = isExtraNeeded;
    }

    public void setExtraTime(int extraTime) {
        this.extraTime = extraTime;
    }

    public int getExtraTime() {
        return extraTime;
    }

    public int getIsExtraNeeded() {
        return isExtraNeeded;
    }

    public int getId() {
        return id;
    }


    public int getExamAverage() {
        return examAverage;
    }

    public void setExamAverage(int average) {
        this.examAverage = average;
    }
    public int getNumberOfStudentDoneInTime() {
        return numberOfStudentDoneInTime;
    }

    public void setNumberOfStudentDoneInTime(int numberOfStudentDoneInTime) {
        this.numberOfStudentDoneInTime = numberOfStudentDoneInTime;
    }

    public int getNumberOfStudentNotDoneInTime() {
        return numberOfStudentNotDoneInTime;
    }

    public void setNumberOfStudentNotDoneInTime(int numberOfStudentNotDoneInTime) {
        this.numberOfStudentNotDoneInTime = numberOfStudentNotDoneInTime;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public Set<Copy> getCopies() {
        return copies;
    }

    public void setCopies(Set<Copy> copies) {
        this.copies = copies;
    }

    public Teacher getTeacherThatExecuted() {
        return teacherThatExecuted;
    }

    public void setTeacherThatExecuted(Teacher teacherThatExecuted) {
        this.teacherThatExecuted = teacherThatExecuted;
    }
    public String getDateOfExam() {
        return dateOfExam;
    }

    public void setDateOfExam(String dateOfExam) {
        this.dateOfExam = dateOfExam;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getNumOfStudentDoing() {
        return numOfStudentDoing;
    }

    public void setNumOfStudentDoing(int numOfStudentDoing) {
        this.numOfStudentDoing = numOfStudentDoing;
    }


    @Override
    public String toString() {
        return this.dateOfExam;
    }
}
