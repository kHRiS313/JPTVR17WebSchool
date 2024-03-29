/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import entity.Journal;
import entity.People;
import entity.Subject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import session.JournalFacade;
import session.PeopleFacade;
import session.SubjectFacade;

/**
 *
 * @author pupil
 */
@WebServlet(name = "SchoolServlet", urlPatterns = {
    "/SchoolServlet",
    "/newPeople",
    "/subject",
    "/newSubject",
    "/newJournal",
    "/allSubjects",
    "/allGrades",
    "/allPeople",
    "/editSubject",
    "/changeSubject",
    "/changeJournal",
    "/changePerson",
    "/editGrade",
    "/editPerson"
    //"/",
})
public class SchoolServlet extends HttpServlet {
    @EJB SubjectFacade subjectFacade;
    @EJB PeopleFacade peopleFacade;
    @EJB JournalFacade journalFacade;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String path = request.getServletPath();
        switch (path) {
            
            case "/newPeople":
                if(request.getParameter("role") != null & request.getParameter("name") != null){
                    String name = request.getParameter("name");
                    Integer role = Integer.parseInt(request.getParameter("role"));
                    People people = new People(role, name);
                    peopleFacade.create(people);
                    request.setAttribute("people", people);
                }
                request.getRequestDispatcher("/WEB-INF/newPeople.jsp").forward(request, response);
                break;
               
                
            case "/newSubject":
                Integer hours = 0;
                String subjectName = request.getParameter("subjectName");
                
                 if (request.getParameter("subjectHours") != null) {
                        hours = Integer.parseInt(request.getParameter("subjectHours"));

                }
                Subject subject = new Subject(subjectName, hours);
                request.setAttribute("subject", subject);
                subjectFacade.create(subject);
                request.getRequestDispatcher("/WEB-INF/newSubject.jsp").forward(request, response);
                break;
                
            case "/newJournal":
                
                List<Subject> listSubjects = subjectFacade.findAll();
                
                List<People> listStudents = new ArrayList<People>();
                for(People i : peopleFacade.findAll()){
                   if(i.getRole() == 0){
                       listStudents.add(i);
                   }
                }
                List<People> listTeachers = new ArrayList<People>();
                for(People i : peopleFacade.findAll()){
                   if(i.getRole() == 1){
                       listTeachers.add(i);
                   }
                }
                
                System.out.println(listStudents);
                System.out.println(listSubjects);
                System.out.println(listTeachers);
                request.setAttribute("listTeachers", listTeachers);
                request.setAttribute("listStudents", listStudents);
                request.setAttribute("listSubjects", listSubjects);
                try{
                    Date date = new Date();
                    
                    String gradeid = request.getParameter("grade");
                    String teacherid = request.getParameter("teacher");
                    String studentid = request.getParameter("student");
                    String subjectid = request.getParameter("subject");

                    People teacherj = peopleFacade.find(Integer.parseInt(teacherid));
                    Subject subjectj = subjectFacade.find(Integer.parseInt(subjectid));
                    People studentj = peopleFacade.find(Integer.parseInt(studentid));
                    

                    
                    Journal journal = new Journal(subjectj, studentj, gradeid, teacherj, date);
                    System.out.println(journal.toString());
                    request.setAttribute("journal", journal);
                    journalFacade.create(journal);

                }
                catch(Exception e){
                    System.out.println("error "+ e);
                }
                request.getRequestDispatcher("/WEB-INF/newJournal.jsp").forward(request, response);
                break;
                
            case "/allPeople":
                List<People> listPeople = peopleFacade.findAll();
                request.setAttribute("listPeople", listPeople);
                request.getRequestDispatcher("/WEB-INF/allPeople.jsp").forward(request, response);
                break;
                
            case "/allGrades":
                List<Journal> listJournals = journalFacade.findAll();
                request.setAttribute("listJournals", listJournals);
                request.getRequestDispatcher("/WEB-INF/allGrades.jsp").forward(request, response);
                break;
                
            case "/allSubjects":
                if("subjectName" != null){
                listSubjects = subjectFacade.findAll();
                request.setAttribute("listSubjects", listSubjects);
                request.getRequestDispatcher("/WEB-INF/allSubjects.jsp").forward(request, response);
                break;}
                
            case "/editSubject":
                String subjectID = request.getParameter("id");
                subject = subjectFacade.find(Integer.parseInt(subjectID));
                request.setAttribute("subject", subject);
                request.getRequestDispatcher("/WEB-INF/editSubject.jsp").forward(request, response);
                break;
                
            case "/changeSubject":
                String sid = request.getParameter("id");
                subject = subjectFacade.find(Integer.parseInt(sid));
                String sname = request.getParameter("name");
                String subhours = request.getParameter("hours");
                subject.setHours(Integer.parseInt(subhours));
                subject.setName(sname);
                subjectFacade.edit(subject);
                request.setAttribute("info", "Изменено.");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                break;
                
            case "/editGrade":
                
                listSubjects = subjectFacade.findAll();
                
                listStudents = new ArrayList<People>();
                for(People i : peopleFacade.findAll()){
                   if(i.getRole() == 0){
                       listStudents.add(i);
                   }
                }
                listTeachers = new ArrayList<People>();
                for(People i : peopleFacade.findAll()){
                   if(i.getRole() == 1){
                       listTeachers.add(i);
                   }
                }
                
                System.out.println(listStudents);
                System.out.println(listSubjects);
                System.out.println(listTeachers);
                
                request.setAttribute("listTeachers", listTeachers);
                request.setAttribute("listStudents", listStudents);
                request.setAttribute("listSubjects", listSubjects);
                
                String JournalID = request.getParameter("id");
                Journal journal = journalFacade.find(Integer.parseInt(JournalID));
                request.setAttribute("journal", journal);
                request.getRequestDispatcher("/WEB-INF/editGrade.jsp").forward(request, response);
                break;
                
            case "/changeJournal":
                Date jdate = new Date();
                String id = request.getParameter("id");
                String jgrade = request.getParameter("grade");
                String jstudent = request.getParameter("student");
                String jteacher = request.getParameter("teacher");
                String jsubject = request.getParameter("subject");
                journal = journalFacade.find(Integer.parseInt(id));
                journal.setDate(jdate);
                journal.setGrade(jgrade);
                journal.setStudent(peopleFacade.find(Integer.parseInt(jstudent)));
                journal.setTeacher(peopleFacade.find(Integer.parseInt(jteacher)));
                journal.setSubject(subjectFacade.find(Integer.parseInt(jsubject))   );
                journalFacade.edit(journal);
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                break;
                
                
            case "/editPerson":
                String personID = request.getParameter("id");
                People person = peopleFacade.find(Integer.parseInt(personID));
                request.setAttribute("person", person);
                request.getRequestDispatcher("/WEB-INF/editPerson.jsp").forward(request, response);
                break;
                
            case "/changePerson":
                personID = request.getParameter("id");
                String name = request.getParameter("name");
                String role = request.getParameter("role");
                person = peopleFacade.find(Integer.parseInt(personID));
                person.setName(name);
                person.setRole(Integer.parseInt(role));
                peopleFacade.edit(person);
                request.getRequestDispatcher("/index.jsp").forward(request, response);
                break;
                

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
