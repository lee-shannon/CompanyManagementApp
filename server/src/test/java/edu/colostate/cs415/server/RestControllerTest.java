package edu.colostate.cs415.server;

import org.junit.*;
import java.util.*;

import edu.colostate.cs415.model.*;
import edu.colostate.cs415.db.*;
import edu.colostate.cs415.dto.*;

import static org.mockito.Mockito.*;

import java.io.IOException;

import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.HttpResponseException;
import org.apache.hc.client5.http.fluent.Content;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpResponse;
import org.junit.Test;

import com.google.gson.Gson;


public class RestControllerTest {
    private static DBConnector dbConnector = mock(DBConnector.class);
    private static RestController restController = new RestController(4567, dbConnector);
    private static Company testCompany;
    private static Set<Qualification> validQs = new HashSet<Qualification>();
    private static Set<Worker> validWorkers = new HashSet<Worker>();
    private static Set<Project> validPs = new HashSet<Project>();

    private Gson gson = new Gson();

    @BeforeClass
    public static void init(){
        when(dbConnector.loadCompanyData()).thenAnswer((i) -> testCompany);
    }
/*******************************************************************************************/
    // API for Qualification
    @Test
    public void testOneQualificationPOST() throws IOException{
        testCompany = new Company("Test Company");
        Qualification qualification = new Qualification("POSTedQualification");
        QualificationDTO qualificationDTO = qualification.toDTO();
        //Convert to JSON for POST body
        Gson gson = new Gson();
        String json = gson.toJson(qualificationDTO);
        
        restController.start();
        String reply = Request.post("http://localhost:4567/api/qualifications/POSTedQualification").bodyString(json, ContentType.APPLICATION_JSON).execute().returnContent().asString();
        assert(testCompany.getQualifications().contains(qualification));
    }

    @Test 
    public void testMultipleValidQualificationsPOST() throws IOException{
        testCompany = new Company("Test Company");
        Qualification[] qualificationsList = new Qualification[5];
        QualificationDTO[] qualificationDTOList = new QualificationDTO[5];
        String[] qualificationsJSONs = new String[5];

        for(int i = 0; i < 5; i++){
            Qualification qualification = testCompany.createQualification(String.format("Qualification%d", i));
            qualificationsList[i] = qualification;
            qualificationDTOList[i] = qualification.toDTO();
            Gson gson = new Gson();
            qualificationsJSONs[i] = gson.toJson(qualificationDTOList[i]);
        }
        restController.start();
        for(int i = 0; i < 5; i++){
            String reply = Request.post("http://localhost:4567/api/qualifications/" + qualificationsList[i].toString()).bodyString(qualificationsJSONs[i], ContentType.APPLICATION_JSON).execute().returnContent().asString();
        }
        assert(testCompany.getQualifications().containsAll(new HashSet<>(Arrays.asList(qualificationsList))));
    }
    @Test
    public void testOneDuplicateQualificationPOST() throws IOException{
        testCompany = new Company("Test Company");
        Qualification qualification = new Qualification("DuplicateQualification");
        Qualification theSameQualification = new Qualification("DuplicateQualification");
        QualificationDTO qualificationDTO = qualification.toDTO();
        QualificationDTO qualificationDTODuplicate = qualification.toDTO();
        //Convert to JSON for POST body
        Gson gson = new Gson();
        String json = gson.toJson(qualificationDTO);
        Gson gsonDuplicate = new Gson();
        String jsonDuplicate = gson.toJson(qualificationDTODuplicate);
        
        restController.start();
        String reply = Request.post("http://localhost:4567/api/qualifications/DuplicateQualification").bodyString(json, ContentType.APPLICATION_JSON).execute().returnContent().asString();
        String replyDuplicate = Request.post("http://localhost:4567/api/qualifications/DuplicateQualification").bodyString(jsonDuplicate, ContentType.APPLICATION_JSON).execute().returnContent().asString();

        assert(testCompany.getQualifications().contains(qualification));
        assert(testCompany.getQualifications().size() == 1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testOneInvalidQualificationPOST() throws IOException{
        testCompany = new Company("Test Company");
        Qualification qualification = new Qualification(" ");
        QualificationDTO qualificationDTO = qualification.toDTO();
        //Convert to JSON for POST body
        Gson gson = new Gson();
        String json = gson.toJson(qualificationDTO);
        //Shouldn't be reached
        restController.start();
        String reply = Request.post("http://localhost:4567/api/qualifications/ ").bodyString(json, ContentType.APPLICATION_JSON).execute().returnContent().asString();
        assert(testCompany.getQualifications().isEmpty());
    }

    //TODO We may later convert spaces to '-', until now we'll throw an exception
    @Test(expected = ClientProtocolException.class)
    public void testInvalidPOST_SpacesInQualificationDescription() throws IOException{
        testCompany = new Company("Test Company");
        Qualification qualification = new Qualification("Should be no spaces");
        QualificationDTO qualificationDTO = qualification.toDTO();
        //Convert to JSON for POST body
        Gson gson = new Gson();
        String json = gson.toJson(qualificationDTO);
        
        restController.start();
        String reply = Request.post("http://localhost:4567/api/qualifications/" + qualification.toString()).bodyString(json, ContentType.APPLICATION_JSON).execute().returnContent().asString();
    }

    @Test(expected = ClientProtocolException.class)
    public void testInvalidPOST_SlashInQualificationDescription() throws IOException{
        testCompany = new Company("Test Company");
        Qualification qualification = new Qualification("/ThisMessesUpThePath");
        QualificationDTO qualificationDTO = qualification.toDTO();
        //Convert to JSON for POST body
        Gson gson = new Gson();
        String json = gson.toJson(qualificationDTO);
        
        restController.start();
        String reply = Request.post("http://localhost:4567/api/qualifications/" + qualification.toString()).bodyString(json, ContentType.APPLICATION_JSON).execute().returnContent().asString();
        assert(testCompany.getQualifications().isEmpty());
    }
    
    @Test
    public void testQualificationGET_IndividualQualifications() throws IOException{
        testCompany = new Company("Test Individual Qualifications");
        QualificationDTO[] qualificationDTOList = new QualificationDTO[5];

        for (int i = 0; i < 5; i++) {
            Qualification qualification = testCompany.createQualification(String.format("Qualification%d", i));
            qualificationDTOList[i] = qualification.toDTO();
        }

        restController.start();
        String[] qualificationResponse = {Request.get("http://localhost:4567/api/qualifications/Qualification0").execute().returnContent().asString(),
                Request.get("http://localhost:4567/api/qualifications/Qualification1").execute().returnContent().asString(),
                Request.get("http://localhost:4567/api/qualifications/Qualification2").execute().returnContent().asString(),
                Request.get("http://localhost:4567/api/qualifications/Qualification3").execute().returnContent().asString(),
                Request.get("http://localhost:4567/api/qualifications/Qualification4").execute().returnContent().asString()};

        for(int i = 0; i < 5 ; i++) {
            assert(qualificationResponse[i].contains(gson.toJson(qualificationDTOList[i])));
        }
    }

    @Test
    public void testQualificationGET_RequestQualificationNotInCompany() throws IOException{
        testCompany = new Company("Test Individual Qualifications");
        QualificationDTO valid = testCompany.createQualification(String.format("ValidQualification")).toDTO();

        restController.start();
        String validResponse = Request.get("http://localhost:4567/api/qualifications/ValidQualification").execute().returnContent().asString();
        HttpResponse fiveHundredResponse = Request.get("http://localhost:4567/api/qualifications/InvalidQualification").execute().returnResponse();

        assert(validResponse.contains(gson.toJson(valid)));
        assert(fiveHundredResponse.getCode() == 500);
    }

/*******************************************************************************************/
    // API for Worker
    @Test
    public void testGetAllWorkers() throws IOException{   
        testCompany = new Company("Company 2");
        //Qualifications
        Set<Qualification> validQs = new HashSet<Qualification>();
        for(int i = 0; i < 10; i++){
            Qualification addQ = testCompany.createQualification(String.format("Qualification #%d", i));
            validQs.add(addQ);
        }
        WorkerDTO[] check = new WorkerDTO[5];
        //Workers
        Set<Worker> validWorkers = new HashSet<Worker>();
        for(int i = 0; i < 5; i++){
            Worker addW = testCompany.createWorker(String.format("Worker #%d", i), validQs, 150000);
            check[i] = addW.toDTO();
            validWorkers.add(addW);
        }
        restController.start();
        String workerResponse = Request.get("http://localhost:4567/api/workers").execute().returnContent().asString();

        for(WorkerDTO individual : check){
            assert(workerResponse.contains(gson.toJson(individual)));
        }
    }

    @Test
    public void testQualificationsGET_AllQualificationsInCompany() throws IOException{
        testCompany = new Company("Test Company");
        QualificationDTO[] qualificationDTOList = new QualificationDTO[3];
        for (int i = 0; i < 3; i++) {
            Qualification qualification = testCompany.createQualification(String.format("Qualification #%d", i));
            qualificationDTOList[i] = qualification.toDTO();
        }

        restController.start();
        String response = Request.get("http://localhost:4567/api/qualifications").execute().returnContent().asString();

        for (QualificationDTO expectedQualification : qualificationDTOList) {
            assert(response.contains(gson.toJson(expectedQualification)));
        }
    }

    @Test
    public void testGetOneWorker() throws IOException{
        testCompany = new Company("Company 3");
        //Qualifications
        Set<Qualification> validQs = new HashSet<Qualification>();
        for(int i = 0; i < 10; i++){
            Qualification addQ = testCompany.createQualification(String.format("Qualification #%d", i));
            validQs.add(addQ);
        }
        WorkerDTO[] check = new WorkerDTO[5];
        //Workers
        Set<Worker> validWorkers = new HashSet<Worker>();
        for(int i = 0; i < 5; i++){
            Worker addW = testCompany.createWorker(String.format("Worker%d", i), validQs, 150000);
            check[i] = addW.toDTO();
            validWorkers.add(addW);
        }
        restController.start();
        String[] workerResponse = { Request.get("http://localhost:4567/api/workers/Worker0").execute().returnContent().asString(),
                                        Request.get("http://localhost:4567/api/workers/Worker1").execute().returnContent().asString(),
                                        Request.get("http://localhost:4567/api/workers/Worker2").execute().returnContent().asString(),
                                        Request.get("http://localhost:4567/api/workers/Worker3").execute().returnContent().asString(),
                                        Request.get("http://localhost:4567/api/workers/Worker4").execute().returnContent().asString()};
        for(int i = 0; i < 5 ; i++)
                assert(workerResponse[i].contains(gson.toJson(check[i])));
        }


    @Test
    public void testGetNoWorker() throws IOException{
        testCompany = new Company("Company 4");
        //Qualifications
        Set<Qualification> validQs = new HashSet<Qualification>();
        for(int i = 0; i < 10; i++){
            Qualification addQ = testCompany.createQualification(String.format("Qualification #%d", i));
            validQs.add(addQ);
        }
        WorkerDTO[] check = new WorkerDTO[5];
        //Workers
        Set<Worker> validWorkers = new HashSet<Worker>();
        for(int i = 0; i < 5; i++){
            Worker addW = testCompany.createWorker(String.format("Worker%d", i), validQs, 150000);
            check[i] = addW.toDTO();
            validWorkers.add(addW);
        }
        restController.start();
        HttpResponse fiveHundredResponse = Request.get("http://localhost:4567/api/workers/Yoda").execute().returnResponse();

        String workerResponse = Request.get("http://localhost:4567/api/workers/Worker0").execute().returnContent().asString();
        assert(workerResponse.contains(gson.toJson(check[0])));
        assert(fiveHundredResponse.getCode() == 500);
    }
    
    @Test
    public void testGetWorkersAssignedToProjects() throws IOException {   
        testCompany = new Company("Company 5");
        //Qualifications
        for(int i = 0; i < 10; i++){
            Qualification addQ = testCompany.createQualification(String.format("Qualification #%d", i));
            validQs.add(addQ);
        }
        WorkerDTO[] check = new WorkerDTO[5];

        for(int i = 0; i < 5; i++){
            Project addP = testCompany.createProject(String.format("Project%d",i), validQs, ProjectSize.SMALL);
            Worker addW = testCompany.createWorker(String.format("Worker%d", i), validQs, 150000);
            testCompany.assign(addW,addP);
            check[i] = addW.toDTO();
            validWorkers.add(addW);
        }
        restController.start();
        for(int i = 0; i < 5; i++){
            String workerResponse = Request.get(String.format("http://localhost:4567/api/workers/Worker%d",i)).execute().returnContent().asString();
            assert(workerResponse.equals(gson.toJson(check[i])));
        }
   }

    @Test
    public void testPostWorker() throws IOException{
        testCompany = new Company("Viva La Company");
        Set<Qualification> validQs = new HashSet<Qualification>();
        for(int i = 0; i < 10; i++){
            Qualification addQ = testCompany.createQualification(String.format("Qualification #%d", i));
            validQs.add(addQ);
        }
        assert(testCompany.getEmployedWorkers().size() == 0);

        Worker testPostWorker = new Worker("Yoda", validQs, 150000);
        WorkerDTO testPostWorkerDTO = testPostWorker.toDTO();
        assert(testCompany.getEmployedWorkers().size() == 0);
        restController.start();

        String reply = Request.post("http://localhost:4567/api/workers/Yoda").bodyString(gson.toJson(testPostWorkerDTO), ContentType.APPLICATION_JSON).execute().returnContent().asString();
        assert(reply.equals("OK"));
        assert(testCompany.getEmployedWorkers().size() == 1);
        assert(testCompany.getEmployedWorkers().contains(testPostWorker));
    }

/*******************************************************************************************/
    // API for Project
    @Test
    public void testGetProjects() throws IOException {
        testCompany = new Company("Test Company");
        testCompany.createQualification("Qualification #1");

        Set<Qualification> worker_qSet = new HashSet<>();
        worker_qSet.add(new Qualification("Qualification #1"));
        Worker john = testCompany.createWorker("John", worker_qSet, 100.0);

        Project testProject = testCompany.createProject("test project", worker_qSet, ProjectSize.SMALL);
        testCompany.assign(john, testProject);   
        testCompany.start(testProject);  
        ProjectDTO testProjectDTO = testProject.toDTO();


        restController.start();
        String response = Request.get("http://localhost:4567/api/projects").execute().returnContent().asString();
        assert(response.contains(gson.toJson(testProjectDTO)));
    }

    @Test
    public void testGetOneProject() throws IOException {
        testCompany = new Company("Test Company");
        testCompany.createQualification("Qualification #1");

        Set<Qualification> worker_qSet = new HashSet<>();
        worker_qSet.add(new Qualification("Qualification #1"));
        Worker john = testCompany.createWorker("John", worker_qSet, 100.0);

        Project testProject = testCompany.createProject("test project", worker_qSet, ProjectSize.SMALL);
        Project testProject2 = testCompany.createProject("test project 2", worker_qSet, ProjectSize.SMALL);
        testCompany.assign(john, testProject);   
        testCompany.start(testProject);  
        ProjectDTO testProjectDTO = testProject.toDTO();
        ProjectDTO testProject2DTO = testProject2.toDTO();

        restController.start();
        String[] response = { Request.get("http://localhost:4567/api/projects/test%20project").execute().returnContent().asString(),
                              Request.get("http://localhost:4567/api/projects/test%20project%202").execute().returnContent().asString() };

        for (int i = 0; i < 2; i++) {
            assert(response[i].contains(gson.toJson(testProjectDTO))) || response[i].contains(gson.toJson(testProject2DTO));
        }
    }

    @Test
    public void testGetProjectNotInCompany() throws IOException {
        testCompany = new Company("Test Company");
        testCompany.createQualification("Qualification #1");

        Set<Qualification> worker_qSet = new HashSet<>();
        worker_qSet.add(new Qualification("Qualification #1"));
        Worker john = testCompany.createWorker("John", worker_qSet, 100.0);

        Project testProject = testCompany.createProject("test project", worker_qSet, ProjectSize.SMALL);

        Project notInCompany = new Project("Not In Company", worker_qSet, ProjectSize.SMALL);

        testCompany.assign(john, testProject);   
        testCompany.start(testProject);  

        ProjectDTO testProjectDTO = testProject.toDTO();

        restController.start();
        String response = Request.get("http://localhost:4567/api/projects/test%20project").execute().returnContent().asString();
        HttpResponse fiveHundredResponse = Request.get("http://localhost:4567/api/projects/Not%20In%20Company").execute().returnResponse();

        assert(response.contains(gson.toJson(testProjectDTO)));
        assert(fiveHundredResponse.getCode() ==  500);

    }

    @Test
    public void testPostProject() throws IOException {
        testCompany = new Company("Company Co.");
        testCompany.createQualification("Qualification #1");
        testCompany.createQualification("Qualification #2");
        testCompany.createQualification("Qualification #3");

        Set<Qualification> projectQs = new HashSet<>();
        projectQs.add(new Qualification("Qualification #1"));
        projectQs.add(new Qualification("Qualification #2"));

        Project testProject = new Project("Test Project", projectQs, ProjectSize.BIG);
        ProjectDTO testProject_toDTO = testProject.toDTO();

        assert(testCompany.getProjects().size() == 0);

        restController.start();
        String response = Request.post("http://localhost:4567/api/projects/Test%20Project").bodyString(gson.toJson(testProject_toDTO), ContentType.APPLICATION_JSON).execute().returnContent().asString();
        assert(response.equals("OK"));
        assert(testCompany.getProjects().contains(testProject));
    }

/*******************************************************************************************/
    // API for Company
    @Test
    public void testAssignPUT() throws IOException{
        testCompany = new Company("Assign");
        //Qualifications
        for(int i = 0; i < 10; i++){
            Qualification addQ = testCompany.createQualification(String.format("Qualification #%d", i));
            validQs.add(addQ);
        }

        Project project = testCompany.createProject(String.format("Project_1"), validQs, ProjectSize.SMALL);
        Worker worker = testCompany.createWorker(String.format("Worker_1"), validQs, 150000);

        AssignmentDTO unassignedWP = new AssignmentDTO(worker.getName(),project.getName());
        assert(testCompany.getAssignedWorkers().size()==0);

        restController.start();
        String response = Request.put("http://localhost:4567/api/assign").bodyString(gson.toJson(unassignedWP), ContentType.APPLICATION_JSON).execute().returnContent().asString();
        assert(response.contains("OK"));

        assert(testCompany.getAssignedWorkers().size()==1);
    }

    @Test(expected = HttpResponseException.class)
    public void testAssignPUT_WorkerNotInCompany() throws IOException{
        testCompany = new Company("Assign_WorkerNotInCompany");
        //Qualifications
        for(int i = 0; i < 10; i++){
            Qualification addQ = testCompany.createQualification(String.format("Qualification #%d", i));
            validQs.add(addQ);
        }

        Project project = testCompany.createProject(String.format("Project_1"), validQs, ProjectSize.SMALL);
        Worker worker = new Worker("Worker_not_employed",validQs, 150000);

        AssignmentDTO unassignedWP = new AssignmentDTO(worker.getName(),project.getName());
        assert(testCompany.getAssignedWorkers().size()==0);

        restController.start();
        String response = Request.put("http://localhost:4567/api/assign").bodyString(gson.toJson(unassignedWP), ContentType.APPLICATION_JSON).execute().returnContent().asString();
        assert(response.contains("KO"));
        assert(testCompany.getAssignedWorkers().size()==0);
    }

    @Test(expected = HttpResponseException.class)
    public void testAssignPUT_ProjectNotInCompany() throws IOException{
        testCompany = new Company("Assign_WorkerNotInCompany");
        //Qualifications
        for(int i = 0; i < 10; i++){
            Qualification addQ = testCompany.createQualification(String.format("Qualification #%d", i));
            validQs.add(addQ);
        }

        Project project = new Project("Project_1", validQs, ProjectSize.SMALL);
        Worker worker = testCompany.createWorker(String.format("Worker_1"), validQs, 150000);

        AssignmentDTO unassignedWP = new AssignmentDTO(worker.getName(),project.getName());
        assert(testCompany.getAssignedWorkers().size()==0);

        restController.start();
        String response = Request.put("http://localhost:4567/api/assign").bodyString(gson.toJson(unassignedWP), ContentType.APPLICATION_JSON).execute().returnContent().asString();
        assert(response.contains("KO"));
        assert(testCompany.getAssignedWorkers().size()==0);
    }

    @Test
    public void testEndpointForUnassign() throws IOException{
        testCompany = new Company("Unassign");
        //Qualifications
        for(int i = 0; i < 10; i++){
            Qualification addQ = testCompany.createQualification(String.format("Qualification #%d", i));
            validQs.add(addQ);
        }
        AssignmentDTO[] assigned = new AssignmentDTO[5];
        AssignmentDTO[] expected = new AssignmentDTO[4];

        for(int i = 0; i < 5; i++){
            Project addP = testCompany.createProject(String.format("Project%d",i), validQs, ProjectSize.SMALL);
            Worker addW = testCompany.createWorker(String.format("Worker%d", i), validQs, 150000);
            testCompany.assign(addW,addP);
            AssignmentDTO assignWP = new AssignmentDTO(addW.getName(),addP.getName());
            assigned[i] = assignWP;
            if(i !=  4) expected[i] = assignWP;
        }
        restController.start();
        String response = Request.put("http://localhost:4567/api/unassign").bodyString(gson.toJson(assigned[4]), ContentType.APPLICATION_JSON).execute().returnContent().asString();
        assert(response.contains("OK"));
        assert(expected.length == testCompany.getAssignedWorkers().size());
    }

    @Test
    public void testKOResponseUnassign() throws IOException{
        testCompany = new Company("Unassign");
        //Qualifications
        for(int i = 0; i < 10; i++){
            Qualification addQ = testCompany.createQualification(String.format("Qualification #%d", i));
            validQs.add(addQ);
        }

        for(int i = 0; i < 5; i++){
            Project addP = testCompany.createProject(String.format("Project%d",i), validQs, ProjectSize.SMALL);
            Worker addW = testCompany.createWorker(String.format("Worker%d", i), validQs, 150000);
            testCompany.assign(addW,addP);
        }
        AssignmentDTO notInCompany = new AssignmentDTO("Yoda","Resistance");
        HttpResponse fiveHundredResponse = Request.put("http://localhost:4567/api/unassign").bodyString(gson.toJson(notInCompany), ContentType.APPLICATION_JSON).execute().returnResponse();
        assert(fiveHundredResponse.getCode() == 500);
        assert(testCompany.getAssignedWorkers().size() == 5);
    }

    @Test
    public void testKOResponseFinish() throws IOException{
        testCompany = new Company("Test Company");
        testCompany.createQualification("Qualification #1");

        Set<Qualification> worker_qSet = new HashSet<>();
        worker_qSet.add(new Qualification("Qualification #1"));
        Worker w = testCompany.createWorker("John", worker_qSet, 100.0);

        Project testProject = testCompany.createProject("test project", worker_qSet, ProjectSize.SMALL);

        testCompany.assign(w, testProject);   
        ProjectDTO testProjectDTO = testProject.toDTO();
        
        String response = Request.put("http://localhost:4567/api/finish").bodyString(gson.toJson(testProjectDTO), ContentType.APPLICATION_JSON).execute().returnContent().asString();
        assert(response.contains("KO"));
    }

    @Test
    public void testOKResponseFinish() throws IOException{
        testCompany = new Company("Test Company");
        testCompany.createQualification("Qualification #1");

        Set<Qualification> worker_qSet = new HashSet<>();
        worker_qSet.add(new Qualification("Qualification #1"));
        Worker w = testCompany.createWorker("John", worker_qSet, 100.0);

        Project testProject = testCompany.createProject("test project", worker_qSet, ProjectSize.SMALL);

        testCompany.assign(w, testProject);   
        testCompany.start(testProject); 
        ProjectDTO testProjectDTO = testProject.toDTO();
        
        restController.start();
        String response = Request.put("http://localhost:4567/api/finish").bodyString(gson.toJson(testProjectDTO), ContentType.APPLICATION_JSON).execute().returnContent().asString();
        assert(response.contains("OK"));
    }

    @Test
    public void testStartPUT() throws IOException{
        testCompany = new Company("Test company");
        Qualification q = testCompany.createQualification("Qualification #1");
        validQs = new HashSet<Qualification>();
        validQs.add(q);

        Worker worker = testCompany.createWorker("John", validQs, 100.0);
        Project testProject = testCompany.createProject("Start Project", validQs, ProjectSize.SMALL);
        ProjectDTO testProjectDTO = testProject.toDTO();

        testCompany.assign(worker, testProject);

        restController.start();
        String response = Request.put("http://localhost:4567/api/start").bodyString(gson.toJson(testProjectDTO), ContentType.APPLICATION_JSON).execute().returnContent().asString();
        assert(response.contains("OK"));
        assert(testProject.getStatus() == ProjectStatus.ACTIVE);
    }

    @Test(expected = HttpResponseException.class)
    public void testStartPUT_ProjectIsNull() throws IOException{
        testCompany = new Company("Test company");
        ProjectDTO testProjectDTO = null;

        restController.start();
        String response = Request.put("http://localhost:4567/api/start").bodyString(gson.toJson(testProjectDTO), ContentType.APPLICATION_JSON).execute().returnContent().asString();
    }
}
