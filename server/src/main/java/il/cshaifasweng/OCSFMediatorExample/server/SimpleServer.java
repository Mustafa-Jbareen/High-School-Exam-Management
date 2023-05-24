package il.cshaifasweng.OCSFMediatorExample.server;

import aidClasses.Message;
import il.cshaifasweng.OCSFMediatorExample.entities.ManyToMany.*;
import il.cshaifasweng.OCSFMediatorExample.entities.appUsers.Student;
import il.cshaifasweng.OCSFMediatorExample.entities.appUsers.Teacher;
import il.cshaifasweng.OCSFMediatorExample.entities.appUsers.User;
import il.cshaifasweng.OCSFMediatorExample.entities.examBuliding.ComputerizedExamToExecute;
import il.cshaifasweng.OCSFMediatorExample.entities.examBuliding.Exam;
import il.cshaifasweng.OCSFMediatorExample.entities.examBuliding.Question;
import il.cshaifasweng.OCSFMediatorExample.entities.gradingSystem.Copy;
import il.cshaifasweng.OCSFMediatorExample.entities.gradingSystem.Grade;
import il.cshaifasweng.OCSFMediatorExample.server.Generating.GenerateAll;
import il.cshaifasweng.OCSFMediatorExample.server.Generating.GetEducational;
import il.cshaifasweng.OCSFMediatorExample.server.Generating.GetUsers;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import il.cshaifasweng.OCSFMediatorExample.entities.educational.*;

import aidClasses.Warning;

import javax.persistence.Query;
import javax.xml.crypto.Data;

public class SimpleServer extends AbstractServer {
	private static Session session;

	public static Session getSession() {
		return session;
	}

	public static void setSession(Session session) {
		SimpleServer.session = session;
	}


	private static SessionFactory getSessionFactory() throws HibernateException {

		Configuration configuration = new Configuration();
		configuration.addAnnotatedClass(User.class);
		configuration.addAnnotatedClass(Student.class);
		configuration.addAnnotatedClass(Teacher.class);
		configuration.addAnnotatedClass(Course.class);
		configuration.addAnnotatedClass(Subject.class);
		configuration.addAnnotatedClass(ComputerizedExamToExecute.class);
		configuration.addAnnotatedClass(Exam.class);
		configuration.addAnnotatedClass(Question.class);
		configuration.addAnnotatedClass(Copy.class);
		configuration.addAnnotatedClass(Grade.class);
		configuration.addAnnotatedClass(Teacher_Subject.class);
		configuration.addAnnotatedClass(Teacher_Course.class);
		configuration.addAnnotatedClass(Course_Question.class);
		configuration.addAnnotatedClass(Exam_Question.class);
		configuration.addAnnotatedClass(Student_Course.class);
		configuration.addAnnotatedClass(Student_Subject.class);
		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties())
				.build();
		return configuration.buildSessionFactory(serviceRegistry);
	}


	public SimpleServer(int port) {
		super(port);
		SessionFactory sessionFactory = getSessionFactory();
		session = sessionFactory.openSession();
		session.beginTransaction();
		GenerateAll.generateEducational(session);  // moving the students to the database
		//GetUsers.generateUsers(session);
		session.getTransaction().commit();
	}

	public void logout(User user) {
		session.beginTransaction();
		user.setConnected(false);
		session.update(user);
		session.flush();
		session.clear();
		if(user.getUserName().equals("3"))
		{
			System.out.println("h");
		}
		session.getTransaction().commit();
	}

	public void login(User user) {
		if(user.getUserName().equals("3"))
		{
			System.out.println("h");
		}
		session.beginTransaction();
		user.setConnected(true);
		session.update(user);
		session.flush();
		session.clear();

		session.getTransaction().commit();
	}

	public void addQuestion(Question question,List<Course> questionCourses,Subject questionSubject,Teacher theTeacher) {
		//session.flush();
		session.beginTransaction();
		session.clear();
		session.save(question);
		session.flush();

		question.setQuestionSubject(questionSubject);
		session.update(question);
		session.flush();

		question.setTeacherThatCreated(theTeacher);
		session.update(question);
		session.flush();

		theTeacher.getQuestionsCreated().add(question);
		session.update(theTeacher);
		session.flush();

		Subject subject = question.getQuestionSubject();
		subject.getSubjectQuestions().add(question);
		session.update(subject);
		session.flush();

		Course_Question cq;
		for(Course course:questionCourses)
		{
			cq=new Course_Question(course,question);
			session.save(cq);
			session.flush();

			course.getCourseQuestions().add(cq);
			session.update(course);
			session.flush();

			question.getQuestionCourses().add(cq);
			session.update(question);
			session.flush();
		}

		//session.clear();
		session.getTransaction().commit();
	}

	public void addExam(Exam exam,Teacher teacher,Course course,Subject subject,List<Question> questions) {
		/*saving exam*/
		session.beginTransaction();
		session.clear();
		session.save(exam);
		session.flush();
		/*end of saving exam*/

		/*updating exam*/
		exam.setTeacherThatCreated(teacher);
		session.update(exam);
		session.flush();

		exam.setExamSubject(subject);
		session.update(exam);
		session.flush();

		exam.setExamCourse(course);
		session.update(exam);
		session.flush();
		/*end of updating exam*/

		/*updating teacher*/
		teacher.getExamsCreated().add(exam);
		session.update(teacher);
		session.flush();
		/*end of updating teacher*/

		/*updating course*/
		course.getCourseExams().add(exam);
		session.update(course);
		session.flush();
		/*end of updating course*/

		/*updating subject*/
		subject.getSubjectExams().add(exam);
		session.update(subject);
		session.flush();
		/*end of updating subject*/

		Exam_Question eq;
		for(Question question:questions)
		{
			eq=new Exam_Question(exam,question);
			session.save(eq);
			session.flush();

			question.getQuestionExams().add(eq);
			session.update(question);
			session.flush();

			exam.getExamQuestions().add(eq);
			session.update(exam);
			session.flush();


		}
		int subjectId=subject.getId()-1;
		int courseId=course.getId()-1;
		int examId=exam.getId()-1;
		String exam_ID="";
		if(subjectId<10)
		{
			exam_ID+="0"+subjectId;
		}
		else
		{
			exam_ID+=subjectId;
		}
		if(courseId<10)
		{
			exam_ID+="0"+courseId;
		}
		else
		{
			exam_ID+=courseId;
		}
		if(examId<10)
		{
			exam_ID+="0"+examId;
		}
		else
		{
			exam_ID+=examId;
		}
		exam.setExam_ID(exam_ID);
		session.update(exam);
		session.flush();
		session.getTransaction().commit();
	}
	public void addCompExam(ComputerizedExamToExecute compExam)
	{
		session.beginTransaction();

		session.save(compExam);
		session.flush();

		Teacher teacher=compExam.getTeacherThatExecuted();
		teacher.getExecutedExams().add(compExam);
		session.update(teacher);
		session.flush();


		Exam exam=compExam.getExam();
		exam.getCompExamsToExecute().add(compExam);
		session.update(exam);
		session.flush();

		session.getTransaction().commit();
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) throws IOException {
		if (msg.getClass().equals(Message.class)) {
			try {
				Message msgFromClient = (Message) msg;
				String contentOfMsg = msgFromClient.getMsg();
				if (contentOfMsg.equals("#login")) {
					String[] userDetails = (String[]) (((Message) msg).getObj());
					String userName = userDetails[0];
					String password = userDetails[1];

					String queryString="FROM User WHERE userName = : userName";
					Query query = session.createQuery(queryString,User.class);
					query.setParameter("userName",userName);
					List<User> users=query.getResultList();
					if (users.size() == 0) {
						Warning warning = new Warning("there is no such a username");
						try {
							client.sendToClient(warning);
							System.out.format("Sent warning to client %s\n", client.getInetAddress().getHostAddress());
							return;
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
					if (!users.get(0).getPassWord().equals(password)) {
						Warning warning = new Warning("wrong password");
						try {
							client.sendToClient(warning);
							System.out.format("Sent warning to client %s\n", client.getInetAddress().getHostAddress());
							return;
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
					if (users.get(0).getConnected()) {
						Warning warning = new Warning("user already logged in!");
						try {
							client.sendToClient(warning);
							System.out.format("Sent warning to client %s\n", client.getInetAddress().getHostAddress());
							return;
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
					//login(users.get(0));
					Message msgToClient = new Message("successful login", users.get(0));
					client.sendToClient(msgToClient);
					return;
				}
				if (contentOfMsg.equals("#logout")) {
					User userToLogout = (User) msgFromClient.getObj();
					session.clear();
					//logout(userToLogout);
					Message msgToClient = new Message("successful logout", null);
					client.sendToClient(msgToClient);
					return;
				}
				if (contentOfMsg.equals("#addQuestion")) {
					List<Object> dataFromClient=(List<Object>) msgFromClient.getObj();
					Question question = (Question) (dataFromClient.get(0));
					addQuestion(question,(List<Course>)dataFromClient.get(1),(Subject)dataFromClient.get(2),(Teacher)dataFromClient.get(3));
					Message messageToClient = new Message("added the question successfully", (Teacher)dataFromClient.get(3));
					try {
						client.sendToClient(messageToClient);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				if (contentOfMsg.equals("#addExam")) {
					List<Object> dataFromClient=(List<Object>) msgFromClient.getObj();
					addExam((Exam) dataFromClient.get(0),(Teacher) dataFromClient.get(1), (Course) dataFromClient.get(2),(Subject)dataFromClient.get(3),(List<Question>) dataFromClient.get(4));
					Message messageToClient = new Message("added the exam successfully", (Teacher)dataFromClient.get(1));
					try {
						client.sendToClient(messageToClient);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				if (contentOfMsg.equals("#addCompExam")) {
					ComputerizedExamToExecute compExam = (ComputerizedExamToExecute) msgFromClient.getObj();
					addCompExam(compExam);
					Message messageToClient = new Message("added the CompExam successfully", compExam.getTeacherThatExecuted());
					try {
						client.sendToClient(messageToClient);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				if(contentOfMsg.equals("#teacherSubjects"))
				{
					int id=(int)msgFromClient.getObj();
					List<Subject> teacherSubjects=GetUsers.getTeacherSubjects(session,id);
					Message messageToClient = new Message("sending teacher subjects",teacherSubjects);
					try {
						client.sendToClient(messageToClient);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				if(contentOfMsg.equals("#teacherCouses"))
				{
					int id=(int)msgFromClient.getObj();
					List<Course> teacherCourses=GetUsers.getTeacherCourses(session,id);
					Message messageToClient = new Message("sending teacher courses",teacherCourses);
					try {
						client.sendToClient(messageToClient);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				if (contentOfMsg.equals("#close")) {
					session.close();
				}



			} catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		} else {
			String msgString = msg.toString();
			if (msgString.startsWith("#warning")) {
				Warning warning = new Warning("Warning from server!");
				try {
					client.sendToClient(warning);
					System.out.format("Sent warning to client %s\n", client.getInetAddress().getHostAddress());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}


	}
}
