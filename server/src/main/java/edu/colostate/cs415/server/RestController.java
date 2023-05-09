package edu.colostate.cs415.server;

import static spark.Spark.after;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.options;
import static spark.Spark.path;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.redirect;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Set;
import java.util.logging.Logger;
import java.util.HashSet;

import com.google.gson.Gson;

import edu.colostate.cs415.db.DBConnector;
import edu.colostate.cs415.dto.*;
import edu.colostate.cs415.model.*;
import spark.Request;
import spark.Response;
import spark.Spark;

public class RestController {

	private static Logger log = Logger.getLogger(RestController.class.getName());
	private static String OK = "OK";
	private static String KO = "KO";

	private DBConnector dbConnector;
	private Company company;
	private Gson gson;

	public RestController(int port, DBConnector dbConnector) {
		port(port);
		this.dbConnector = dbConnector;
		gson = new Gson();
	}

	public void start() {
		// Load data from DB
		company = dbConnector.loadCompanyData();

		// Redirect
		redirect.get("/", "/helloworld");

		// Logging
		after("/*", (req, res) -> logRequest(req, res));
		exception(Exception.class, (exc, req, res) -> handleException(exc, res));

		// Hello World
		get("/helloworld", (req, res) -> helloWorld());

		// API
		path("/api", () -> {
			// Enable CORS
			options("/*", (req, res) -> optionsCORS(req, res));
			after("/*", (req, res) -> enableCORS(res));

			// Qualifications
			path("/qualifications", () -> {
				get("", (req, res) -> getQualifications(), gson::toJson);
				get("/:description", (req, res) -> getQualification(req.params("description")),
						gson::toJson);
				post("/:description", (req, res) -> createQualification(req));
			});

			// Workers
			path("/workers",() -> {
				get("", (req, res) -> getWorkers(), gson::toJson);
				get("/:name", (req, res) -> getWorker(req.params("name")), gson::toJson);
				post("/:name", (req, res) -> createWorker(req));
			});

			//Projects
			path("/projects", () -> {
				get("", (req, res) -> getProjects(), gson::toJson);
				get("/:name", (req, res) -> getProject(req.params("name")), gson::toJson);
				post("/:name", (req, res) -> createProject(req));
			});

			//Assign
			path("/assign", () -> {
				put("", (req, res) -> assign(req), gson::toJson);
			});


			//Unassign
			path("/unassign", () -> {
				put("", (req, res) -> unassign(req), gson::toJson);
			});

			path("/start", () -> {
				put("", (req, res) -> start(req), gson::toJson);
			});

			//Finish
			path("/finish", () -> {
				put("", (req, res) -> finish(req), gson::toJson);
			});
		});
	}

	public void stop() {
		Spark.stop();
	}

	private String helloWorld() {
		return "Hello World!";
	}

	private ProjectDTO[] getProjects() {
		Set<Project> companyProjectSet = company.getProjects(); 
		int size = companyProjectSet.size();
		int counter = 0;
		ProjectDTO[] projectArray = new ProjectDTO[size]; 

		for (Project project : companyProjectSet) { 
			ProjectDTO DTO_project = project.toDTO(); 
			projectArray[counter] = DTO_project; 
			counter++; 
		}
		return projectArray; 
	}

	private ProjectDTO getProject(String name) {
        Set<Project> getAllProjects = new HashSet<Project>(company.getProjects());
        for (Project project : getAllProjects) {
            if (project.getName().equals(name)) {
                return project.toDTO();
            }
        }
		throw new RuntimeException("No project found.");
    }
	
	private WorkerDTO[] getWorkers() {
		WorkerDTO[] getTheWorkers = new WorkerDTO[company.getEmployedWorkers().size()];
		Set<Worker> allWorkers = new HashSet<Worker>(company.getEmployedWorkers());
		int index = 0;
		for(Worker all : allWorkers){
			getTheWorkers[index] = all.toDTO();
			index++;
		}
		return getTheWorkers;
	}

	private WorkerDTO getWorker(String name) {
		Set<Worker> allWorkers = new HashSet<Worker>(company.getEmployedWorkers());
		for(Worker all : allWorkers){
			if (all.getName().equals(name)) return all.toDTO();
		}
		throw new RuntimeException("No worker found.");
	}

	private QualificationDTO[] getQualifications() {
		QualificationDTO[] getTheQualifications = new QualificationDTO[company.getQualifications().size()];
		Set<Qualification> allQualifications = company.getQualifications();
		int index = 0;
		for(Qualification qualification : allQualifications){
			getTheQualifications[index] = qualification.toDTO();
			index++;
		}

		return getTheQualifications;
	}

	private QualificationDTO getQualification(String description) {
		Set<Qualification> allQualifications = new HashSet<Qualification>(company.getQualifications());
		for(Qualification qualification : allQualifications){
			Qualification qualificationInList = new Qualification(description);
			if(qualification.equals(qualificationInList)) return qualification.toDTO();
		}
		throw new RuntimeException("No qualification found.");
	}

	private String createQualification(Request request) {
		QualificationDTO assignmentDTO = gson.fromJson(request.body(), QualificationDTO.class);
		if (request.params("description").equals(assignmentDTO.getDescription())) {
			company.createQualification(assignmentDTO.getDescription());
		} else
			throw new RuntimeException("Qualification descriptions do not match.");
		return OK;
	}

	private String createWorker(Request request) {
		WorkerDTO assignmentDTO = gson.fromJson(request.body(), WorkerDTO.class);
		Set<Qualification> dtoQs = new HashSet<Qualification>();
		for (String qual : assignmentDTO.getQualifications()){
			dtoQs.add(new Qualification(qual));
		}
		if (request.params("name").equals(assignmentDTO.getName())) {
			company.createWorker(assignmentDTO.getName(), dtoQs, assignmentDTO.getSalary());
		} else
			throw new RuntimeException("Worker name do not match.");
		return OK;
	}

	private String createProject(Request request) {
		ProjectDTO assignmentDTO = gson.fromJson(request.body(), ProjectDTO.class);
		Set<Qualification> dtoQs = new HashSet<Qualification>();

		for (String qualification : assignmentDTO.getQualifications()) {
			dtoQs.add(new Qualification(qualification));
		}

		if (request.params("name").equals(assignmentDTO.getName())) {
			company.createProject(assignmentDTO.getName(), dtoQs, assignmentDTO.getSize());
		} else {
			throw new RuntimeException("Project names do not match.");
		}
		return OK;
	}

	private String assign(Request request){
		AssignmentDTO unassigned = gson.fromJson(request.body(), AssignmentDTO.class);
		Worker worker = null;
		Project project = null;

		try{
			for(Worker companyWorker: company.getEmployedWorkers()) {
				if(companyWorker.getName().equals(unassigned.getWorker()))
					worker = companyWorker;
			}

			for(Project companyProject: company.getProjects()) {
				if(companyProject.getName().equals(unassigned.getProject()))
					project = companyProject;
			}
			company.assign(worker, project);
		}catch(Exception e){
			throw e;
		}

		return OK;
	}

	private String unassign(Request request){
		AssignmentDTO assigned = gson.fromJson(request.body(), AssignmentDTO.class);
		try{
			// Check if worker or project is in company
			getWorker(assigned.getWorker());
			getProject(assigned.getProject());
			// Find worker and project if in company, then check if can assign them
			Worker w = null;
			Project p = null;
			for(Worker compW: company.getAssignedWorkers()) if(compW.getName().equals(assigned.getWorker())) w = compW;
			for(Project compP: company.getProjects()) if(compP.getName().equals(assigned.getProject())) p = compP;
			if(w != null && p != null){
				company.unassign(w, p);
			}
		}catch(Exception e){throw e;}
		return OK;
	}

	private String finish(Request request) {
		ProjectDTO started = gson.fromJson(request.body(), ProjectDTO.class);
		Project p = null;
		try{
			for(Project compP: company.getProjects()) {
				if(compP.getName().equals(started.getName()) && compP.getStatus() == ProjectStatus.ACTIVE){
					p = compP;
				}
			}
			if(p != null) {
				company.finish(p);
				return OK;
			} 
		} catch(Exception e){
			throw e;
		}
		return KO;
	}
		

	private String start(Request request) {
		ProjectDTO projectDTO = gson.fromJson(request.body(), ProjectDTO.class);
		Project projectToStart = null;
		for (Project companyProject: company.getProjects()){
			if(companyProject.getName().equals(projectDTO.getName())){
				projectToStart = companyProject;
			}
		}
		try{
			company.start(projectToStart);
			return OK;
		}catch (Exception e){
			throw e;
		}
	}

	// Logs every request received
	private void logRequest(Request request, Response response) {
		log.info(request.requestMethod() + " " + request.pathInfo() + "\nREQUEST:\n" + request.body() + "\nRESPONSE:\n"
				+ response.body());
	}

	// Exception handling
	private void handleException(Exception exception, Response response) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		exception.printStackTrace();
		exception.printStackTrace(pw);
		log.severe(sw.toString());
		response.body(KO);
		response.status(500);
	}

	// Enable CORS
	private void enableCORS(Response response) {
		response.header("Access-Control-Allow-Origin", "*");
	}

	// Enable CORS
	private String optionsCORS(Request request, Response response) {
		String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
		if (accessControlRequestHeaders != null)
			response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);

		String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
		if (accessControlRequestMethod != null)
			response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
		return OK;
	}
}
