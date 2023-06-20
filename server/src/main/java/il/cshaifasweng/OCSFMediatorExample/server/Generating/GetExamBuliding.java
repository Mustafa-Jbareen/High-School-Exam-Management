package il.cshaifasweng.OCSFMediatorExample.server.Generating;

import il.cshaifasweng.OCSFMediatorExample.entities.ManyToMany.Course_Question;
import il.cshaifasweng.OCSFMediatorExample.entities.ManyToMany.Exam_Question;
import il.cshaifasweng.OCSFMediatorExample.entities.ManyToMany.Teacher_Course;
import il.cshaifasweng.OCSFMediatorExample.entities.appUsers.Teacher;
import il.cshaifasweng.OCSFMediatorExample.entities.educational.Course;
import il.cshaifasweng.OCSFMediatorExample.entities.educational.Subject;
import il.cshaifasweng.OCSFMediatorExample.entities.examBuliding.*;
import org.hibernate.Session;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class GetExamBuliding {
    public static  int counter=0;

    public static List<Exam> getAllExamsForCourses(Session session, int teacherId) {
        String queryString = "SELECT e FROM Exam e " +
                "join fetch e.examSubject " +
                "JOIN FETCH e.examCourse " +
                "JOIN FETCH e.teacherThatCreated " +
                "JOIN e.examCourse.courseTeachers tc " +
                "WHERE tc.teacher.id = :id";
        Query query = session.createQuery(queryString);
        query.setParameter("id", teacherId);
        List<Exam> newExams = new ArrayList<>();
        for (Exam exam : (List<Exam>) query.getResultList()) {
            Exam exam1 = new Exam(exam);
            exam1.setExamSubject(new Subject(exam.getExamSubject()));
            exam1.setExamCourse(new Course(exam.getExamCourse()));
            exam1.setTeacherThatCreated(new Teacher(exam.getTeacherThatCreated()));
            newExams.add(exam1);
        }
        return newExams;
    }
    public static List<Exam> getAllExamsToCopy(Session session, int teacherId) {
        String queryString = "SELECT e FROM Exam e " +
                "join fetch e.examSubject " +
                "JOIN FETCH e.examCourse " +
                "JOIN FETCH e.teacherThatCreated " +
                "JOIN e.examCourse.courseTeachers tc " +
                "WHERE tc.teacher.id = :id";
        Query query = session.createQuery(queryString);
        query.setParameter("id", teacherId);
        List<Exam> newExams = new ArrayList<>();
        for (Exam exam : (List<Exam>) query.getResultList()) {
            Exam exam1 = new Exam(exam);
            exam1.setExamSubject(new Subject(exam.getExamSubject()));
            exam1.setExamCourse(new Course(exam.getExamCourse()));
            exam1.setTeacherThatCreated(new Teacher(exam.getTeacherThatCreated()));
            Set<Exam_Question> examQuestions=new HashSet<>();
            for(Exam_Question exam_question:exam.getExamQuestions())
            {
                examQuestions.add(new Exam_Question(exam_question));
            }
            exam1.setExamQuestions(examQuestions);
            newExams.add(exam1);
        }
        return newExams;
    }
    public static List<Exam_Question> getExamQuestionsById(Session session,int id)
    {
        String queryString=" FROM Exam_Question WHERE exam.id = : id";
        Query query = session.createQuery(queryString,Exam_Question.class);
        query.setParameter("id",id);
        List<Exam_Question> examQuestions=new ArrayList<>();
        for(Exam_Question exam_question:(List<Exam_Question>)query.getResultList())
        {
            examQuestions.add(new Exam_Question(exam_question));
        }
        return examQuestions;
    }

    public static List<ExamToExecute> getAllExamsForTeacher(Session session, int teacherId)
    {
        String queryString="select e FROM ExamToExecute e WHERE e.teacherThatExecuted.id = : id and e.numOfStudentDoing > 0";
        Query query = session.createQuery(queryString,ExamToExecute.class);
        query.setParameter("id",teacherId);
        List<ExamToExecute> list=new ArrayList<>();
        for(ExamToExecute examToExe:(List<ExamToExecute>)(query.getResultList()))
        {
                ExamToExecute examToExecute;
                if (examToExe.getClass().equals(ComputerizedExamToExecute.class)) {
                    examToExecute = new ComputerizedExamToExecute(examToExe);
                } else {
                    examToExecute = new ManualExamToExecute(examToExe);
                }
                Exam exam1 = new Exam(examToExe.getExam());
                exam1.setExamSubject(new Subject(examToExe.getExam().getExamSubject()));
                examToExecute.setExam(exam1);
                list.add(examToExecute);
        }
        return list;
    }

    public static List<Teacher_Course> teacherCourses(Session session,int teacherId)
    {
        String queryString=" FROM Teacher_Course WHERE teacher.id = : id";
        Query query = session.createQuery(queryString,Teacher_Course.class);
        query.setParameter("id",teacherId);
        return  (List<Teacher_Course>)query.getResultList();
    }
    public static List<ExamToExecute> getAllExamsToExecute(Session session, int teacherId, String date) throws ParseException {
        counter++;
        String queryString=" FROM ExamToExecute WHERE teacherThatExecuted.id = : id";
        Query query = session.createQuery(queryString,ExamToExecute.class);
        query.setParameter("id",teacherId);
        List<ExamToExecute> list=(List<ExamToExecute>)(query.getResultList());
        List<ExamToExecute> list1=new ArrayList<>();
        for(ExamToExecute compExam:list)
        {
            String endDate=toNewDate(compExam.getDateOfExam(),compExam.getExam().getTime());
            if(compareDates(date,compExam.getDateOfExam(),endDate))
            {
                ExamToExecute exam=new ExamToExecute(compExam);
                exam.setExam(new Exam(compExam.getExam()));
                list1.add(exam);
            }
        }
        return list1;
    }
    public static String toNewDate(String date,int time) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date d = df.parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MINUTE, time);
        String newDate = df.format(cal.getTime());
        return newDate;
    }

    public static Boolean compareDates(String date1,String date2,String date3) throws ParseException {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date d1 = sdFormat.parse(date1);
        Date d2 = sdFormat.parse(date2);
        Date d3=sdFormat.parse(date3);
        if(d1.compareTo(d2)<0)              // if d1 occur before d2 return false
            return false;
        if(d3.compareTo(d1)>0)              // if d3 occur after d1 return true
            return true;
        return false;
    }
    public static Boolean compare2Dates(String date1,String date2) throws ParseException {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date d1 = sdFormat.parse(date1);
        Date d2 = sdFormat.parse(date2);
        if(d1.compareTo(d2)<0)              // if d1 occur before d2 return false
            return false;
        return true;
    }
    public static Exam getExamById(Session session,int id)
    {
        String queryString=" FROM Exam WHERE id = : id";
        Query query = session.createQuery(queryString,Exam.class);
        query.setParameter("id",id);
        return  (Exam) (query.getResultList().get(0));
    }
    public static List<Course> getCoursesById(Session session,List<Integer> courseIndexes)
    {
        List<Course> courses=new ArrayList<>();
        int size=courseIndexes.size();
        for(int i=0;i<size;i++)
        {
            String queryString=" FROM Course WHERE id = : id";
            Query query = session.createQuery(queryString,Course.class);
            query.setParameter("id",courseIndexes.get(i));
            courses.add((Course) (query.getResultList().get(0)));
        }
        return courses;
    }
    public static Subject getSubjectById(Session session,int subjectId)
    {
        String queryString=" FROM Subject WHERE id = : id";
        Query query = session.createQuery(queryString,Subject.class);
        query.setParameter("id",subjectId);
        return  (Subject) (query.getResultList().get(0));
    }
    public static Course getCourseById(Session session,int subjectId)
    {
        String queryString=" FROM Course WHERE id = : id";
        Query query = session.createQuery(queryString,Course.class);
        query.setParameter("id",subjectId);
        return  (Course) (query.getResultList().get(0));
    }
    public static List<Question> getQuestionsById(Session session, List<Integer> questionsIds)
    {
        List<Question> questions=new ArrayList<>();
        int size=questionsIds.size();
        for(int i=0;i<size;i++)
        {
            String queryString=" FROM Question WHERE id = : id";
            Query query = session.createQuery(queryString,Question.class);
            query.setParameter("id",questionsIds.get(i));
            questions.add((Question) (query.getResultList().get(0)));
        }
        return questions;
    }
    public static List<Subject> getSubjectsById(Session session, List<Integer> subjectIds)
    {
        List<Subject> subjects=new ArrayList<>();
        int size=subjectIds.size();
        for(int i=0;i<size;i++)
        {
            String queryString=" FROM Subject WHERE id = : id";
            Query query = session.createQuery(queryString,Subject.class);
            query.setParameter("id",subjectIds.get(i));
            subjects.add((Subject) (query.getResultList().get(0)));
        }
        return subjects;
    }
    public static ExamToExecute getExamToExeById(Session session,int id)
    {
        String queryString=" FROM ExamToExecute WHERE id = : id";
        Query query = session.createQuery(queryString,ExamToExecute.class);
        query.setParameter("id",id);
        return  (ExamToExecute) (query.getResultList().get(0));
    }
    public static List<Question> getAllQuestionsForTeacher(Session session)
    {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Question> query = builder.createQuery(Question.class);
        query.from(Question.class);
        List<Question> data = session.createQuery(query).getResultList();
        List<Question> questions=new ArrayList<>();
        for(Question question:data)
        {
            Question q=new Question(question);
            q.setTeacherThatCreated(new Teacher(question.getTeacherThatCreated()));
            q.setQuestionSubject(new Subject(question.getQuestionSubject()));
            questions.add(q);
        }
        return questions;
    }
    public static List<Exam> getAllExamsForTeacher(Session session)
    {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Exam> query = builder.createQuery(Exam.class);
        query.from(Exam.class);
        List<Exam> data = session.createQuery(query).getResultList();
        List<Exam> exams=new ArrayList<>();
        for(Exam exam:data)
        {
            Exam q=new Exam(exam);
            Exam exam1=new Exam(exam);
            Set<Exam_Question> examQuestions=exam.getExamQuestions();
            for(Exam_Question exam_question:examQuestions)
            {
                Exam_Question eq=new Exam_Question(exam_question);
                exam1.getExamQuestions().add(eq);
            }
            exam1.setExamCourse(new Course(exam.getExamCourse()));
            exam1.setExamSubject(new Subject(exam.getExamSubject()));
            exam1.setTeacherThatCreated(new Teacher(exam.getTeacherThatCreated()));
            exams.add(exam1);
        }
        return exams;
    }
    public static List<Question> getAllQuestionsForExamToCopy(Session session,int courseId,int subjectId)
    {
        Subject subject=GetExamBuliding.getSubjectById(session,subjectId);
        Course course=GetExamBuliding.getCourseById(session,courseId);
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Question> query = builder.createQuery(Question.class);
        query.from(Question.class);
        List<Question> data = session.createQuery(query).getResultList();
        for(Question question:data)
        {
            if(!question.getQuestionSubject().getSubjectName().equals(subject.getSubjectName()))
                data.remove(question);
            boolean isExist=false;
            for(Course_Question courseQuestion:question.getQuestionCourses())
            {
                if(courseQuestion.getCourse().getCourseName().equals(course.getCourseName()))
                    isExist=true;
            }
            if(!isExist)data.remove(question);
        }
        List<Question> questions=new ArrayList<>();
        for(Question question:data)
        {
            Question q=new Question(question);
            q.setTeacherThatCreated(new Teacher(question.getTeacherThatCreated()));
            q.setQuestionSubject(new Subject(question.getQuestionSubject()));
            questions.add(q);
        }
        return questions;
    }
    public static List<Exam> getAllExamsCreatedForTeacher(Session session,int teacherId)
    {
        String queryString=" FROM Exam WHERE teacherThatCreated.id = : id";
        Query query = session.createQuery(queryString,Exam.class);
        query.setParameter("id",teacherId);
        List<Exam> exams=new ArrayList<>();
        for(Exam exam:(List<Exam>)query.getResultList())
        {
            Exam newExam=new Exam(exam);
            newExam.setExamCourse(new Course(exam.getExamCourse()));
            newExam.setExamSubject(new Subject(exam.getExamSubject()));
            newExam.setTeacherThatCreated(new Teacher(exam.getTeacherThatCreated()));
            exams.add(newExam);
        }
        return exams;
    }

    public static List<Question> copyQuestions(Set<Exam_Question> examQuestions) {
        List<Question>  questionList = new ArrayList<>();
        for (Exam_Question exam_question : examQuestions) {
            questionList.add(new Question(exam_question.getQuestion()));
        }
        return questionList;
    }
    public static ExamToExecute CopyExamToExe(Session session,String code,String date) throws ParseException {


        ExamToExecute newExamToExe;
        String q = "from ExamToExecute where code = '"+ code +"'";
        Query query=session.createQuery(q);
        ExamToExecute examToExecute=((List<ExamToExecute>) (query.getResultList())).get(0);
        if(!GetExamBuliding.compareDates(date,examToExecute.getDateOfExam(),GetExamBuliding.toNewDate(
                examToExecute.getDateOfExam(),examToExecute.getExam().getTime()+examToExecute.getExtraTime())))
        {
            return null;
        }

        if(examToExecute.getClass().equals(ManualExamToExecute.class))
        {
            newExamToExe=new ManualExamToExecute(examToExecute);
        }
        else
        {
           newExamToExe=new ComputerizedExamToExecute(examToExecute);
        }
        Exam exam=new Exam(examToExecute.getExam());
        Subject subject=new Subject(examToExecute.getExam().getExamSubject());
        Course course=new Course(examToExecute.getExam().getExamCourse());
        Set<Exam_Question> newExamQuestions=new HashSet<>();
        for(Exam_Question exam_question:examToExecute.getExam().getExamQuestions())
        {
            newExamQuestions.add(new Exam_Question(exam_question));
        }
        exam.setExamSubject(subject);
        exam.setExamCourse(course);
        exam.setExamQuestions(newExamQuestions);
        newExamToExe.setExam(exam);
        return newExamToExe;
    }
    public static String questionID(int subjectQuestionsNumber,int id)
    {
        String questionID="";
        questionID+=String.valueOf((id-1)/10)+String.valueOf(id-1);

        if(subjectQuestionsNumber<10)
        {
            questionID+="00"+subjectQuestionsNumber;
        }
        if(subjectQuestionsNumber <100 && subjectQuestionsNumber>9)
        {
            questionID+="0"+subjectQuestionsNumber;
        }
        if(subjectQuestionsNumber>99)
        {
            questionID+=subjectQuestionsNumber;
        }

        return questionID;
    }
    public static String examID(int courseQuestionsNumber ,int subjectId,int courseId)
    {
        String exam_ID="";
        exam_ID+=String.valueOf((subjectId-1)/10)+String.valueOf(subjectId-1);
        exam_ID+=String.valueOf((courseId-1)/10)+String.valueOf(courseId-1);
        exam_ID+=String.valueOf((courseQuestionsNumber)/10)+String.valueOf(courseQuestionsNumber);
        return exam_ID;
    }

}
