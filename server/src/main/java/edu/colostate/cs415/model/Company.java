package edu.colostate.cs415.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Company {

	private String name;
	private Set<Worker> employees;
	private Set<Worker> available;
	private Set<Worker> assigned;
	private Set<Project> projects;
	private Set<Qualification> qualifications;

	public Company(String name) {
		if (name.trim().isEmpty()) {
			throw new IllegalArgumentException();
		}
		this.name = name;
		this.employees = new HashSet<>();
		this.available = new HashSet<>();
		this.assigned = new HashSet<>();
		this.projects = new HashSet<>();
		this.qualifications = new HashSet<>();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this)
			return true;
		if(other == null)
			return false;
		if (!(other instanceof Company)) {
			return false;
		}

		Company otherCompany = (Company) other;
		return this.name.equals(otherCompany.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.name);
	}

	@Override
	public String toString() {
		return this.name + ":" + this.available.size() + ":" + this.projects.size();
	}

	public String getName() {
		return this.name;
	}

	public Set<Worker> getEmployedWorkers() {
		if(this.employees.isEmpty()) return Collections.emptySet();
		return new HashSet<Worker>(this.employees);
	}

	public Set<Worker> getAvailableWorkers() {
		if (this.available.isEmpty()) {
			return Collections.emptySet();
		}

		return new HashSet<Worker>(this.available);
	}

	public Set<Worker> getUnavailableWorkers() {
		if(this.employees.isEmpty()) {
			return Collections.emptySet();
		}
		//Without creating a new set, Java assigns the *memory value* of the original to the copy set, so any changes you make to one happens to the other 
		/*
		Set<Worker> employeeCopy = this.employees;
		Set<Worker> availableCopy = this.available;
		*/

		Set<Worker> employeeCopy = new HashSet<Worker>(this.employees);
		Set<Worker> availableCopy = new HashSet<Worker>(this.available);

		employeeCopy.removeAll(availableCopy);
		return employeeCopy;
	}

	public Set<Worker> getAssignedWorkers() {
		if (this.assigned.isEmpty()) {
			return Collections.emptySet();
		}
		return new HashSet<Worker>(this.assigned);
	}

	public Set<Worker> getUnassignedWorkers() {
		Set<Worker> employedButNotAssigned = getEmployedWorkers();
		Set<Worker> assignedCopy = getAssignedWorkers();
		employedButNotAssigned.removeAll(assignedCopy);
		if (employedButNotAssigned.isEmpty()) {
			return Collections.emptySet();
		}
		return employedButNotAssigned;
	}

	public Set<Project> getProjects() {
		if (this.projects.isEmpty()) {
			return Collections.emptySet();
		}
		return new HashSet<Project>(this.projects);
	}

	public Set<Qualification> getQualifications() {
		if (this.qualifications.isEmpty()) {
			return Collections.emptySet();
		}
		return new HashSet<Qualification>(this.qualifications);
	}

	public Worker createWorker(String name, Set<Qualification> qualifications, double salary){
		if (name == null || qualifications == null) return null; 
		try{
			if(this.qualifications.containsAll(qualifications)){
				Worker hiredWorker = new Worker(name, qualifications, salary);
				if(this.assigned.contains(hiredWorker) || this.employees.contains(hiredWorker) || this.available.contains(hiredWorker)) throw new IllegalArgumentException();
				this.available.add(hiredWorker);
				this.employees.add(hiredWorker);
				for(Qualification companyQualification : this.qualifications){
					for(Qualification workerQualification : hiredWorker.getQualifications()){
						if(companyQualification.equals(workerQualification)) companyQualification.addWorker(hiredWorker);
					}
				}
				return hiredWorker;
			}
			else return null;
		} catch(IllegalArgumentException e){return null;}
	}

	public Qualification createQualification(String description) {
		if(description == null) return null;
		try {
			Qualification addQualification = new Qualification(description);
			this.qualifications.add(addQualification);
			return addQualification;
		}catch(IllegalArgumentException e){return null;}
	}

	public Project createProject(String name, Set<Qualification> qualifications, ProjectSize size) {
		if (name == null || qualifications == null) return null;
		try{
			Project newProject = new Project(name, qualifications, size);
			if(this.qualifications.containsAll(qualifications)){
			this.projects.add(newProject);
			return newProject;
			}
			else return null;
		}catch(IllegalArgumentException e){return null;}
	}
	
	public void start(Project project) {
		if(project == null) throw new NullPointerException();
		if(!projects.contains(project)) throw new IllegalArgumentException("Project not in Company project");

		if(project.getStatus() == ProjectStatus.SUSPENDED || project.getStatus() == ProjectStatus.PLANNED){
			if(qualifications.containsAll(project.getRequiredQualifications()) && project.getMissingQualifications().size() == 0){
				project.setStatus(ProjectStatus.ACTIVE);
			}
			else throw new IllegalArgumentException();
		}
	}

	public void finish(Project project) {
		if(project == null) throw new NullPointerException();
		if(!projects.contains(project)) throw new IllegalArgumentException("Project is not in Company project list");

		if(project.getStatus() == ProjectStatus.ACTIVE) {
			//REPLACE WITH UNASSIGN ALL WHEN COMPLETED FOR READABILITY
			for(Worker w : project.getWorkers()) {
				unassign(w, project);
			}
			project.setStatus(ProjectStatus.FINISHED);
		}
	}

	public void assign(Worker worker, Project project) {
		if(!(worker!=null) || !(project!=null)) throw new IllegalArgumentException();
		if(isWorkerEmployedAndAvailable(worker) && !isWorkerInProject(worker,project) 
		&& !isProjectActiveOrFinished(project) && !worker.willOverload(project) 
		&& isWorkerHelpfulToProject(worker,project)
		&& isProjectInCompanyProject(project)
		){
			if(!isWorkerAlreadyAssigned(worker)) this.assigned.add(worker);
			project.addWorker(worker);
			worker.addProject(project);
			shouldWorkerBeUnavailable(worker);
		}
		else{ throw new IllegalArgumentException();}
	}

	private boolean isProjectInCompanyProject(Project project){
		return this.projects.contains(project);
	}

	private boolean isWorkerEmployedAndAvailable(Worker worker){
		if(this.employees.contains(worker) && this.available.contains(worker)) return true;
		else return false;
	}
	private boolean isWorkerInProject(Worker worker, Project project){
		if(project.getWorkers().contains(worker) || worker.getProjects().contains(project)) return true;
		else return false;
	}
	private boolean isProjectActiveOrFinished(Project project){
		ProjectStatus state = project.getStatus();
		if(state == ProjectStatus.ACTIVE || state == ProjectStatus.FINISHED) return true;
		else if(state == ProjectStatus.PLANNED || state == ProjectStatus.SUSPENDED) return false;
		else return false;
	}

	private boolean isWorkerHelpfulToProject(Worker worker, Project project){
		return project.isHelpful(worker);
	}

	private boolean isWorkerAlreadyAssigned(Worker worker){
		if(getAssignedWorkers().contains(worker)) return true;
		else return false;
	}

	private void shouldWorkerBeUnavailable(Worker worker){
		if(!worker.isAvailable()){
			this.available.remove(worker);
		}
	}

	private boolean workerOnlyHasThisProject(Worker worker, Project project) {
		if (worker.getProjects().size() == 1 && worker.getProjects().contains(project)) {
			return true;
		} 
		return false;
	}

	public void unassign(Worker worker, Project project) {

		if (worker == null || project == null || !this.projects.contains(project)) {
			throw new IllegalArgumentException();
		}

		if (project.getStatus() == ProjectStatus.FINISHED) {
			throw new IllegalArgumentException("Cannot unassign worker from finished project");
		}

		if (isWorkerInProject(worker, project) && this.assigned.contains(worker)) {
			if (workerOnlyHasThisProject(worker, project)) {
				this.assigned.remove(worker);
			}
			
			worker.removeProject(project);
			project.removeWorker(worker);

			if (worker.isAvailable() && !this.available.contains(worker)) {
				this.available.add(worker);
			}

			if (project.getStatus() == ProjectStatus.ACTIVE && project.isHelpful(worker)) {
				project.setStatus(ProjectStatus.SUSPENDED);
			}
		} else {
			throw new IllegalArgumentException("Can't unassign a worker from project who was not previously assigned");
		}
	}

	public void unassignAll(Worker worker) {
		if (worker == null) {
			throw new IllegalArgumentException();
		}
		Set<Project> pset = new HashSet<Project>(worker.getProjects());

		for (Project p : pset) {
			unassign(worker, p);
		}

		this.assigned.remove(worker);
	}
}
