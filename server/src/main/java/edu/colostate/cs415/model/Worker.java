package edu.colostate.cs415.model;

import java.util.Objects;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;

import edu.colostate.cs415.dto.WorkerDTO;

public class Worker {

	public static final int MAX_WORKLOAD = 12;

	private String name;
	private double salary;
	private Set<Project> projects;
	private Set<Qualification> qualifications;

    public Worker(String name, Set<Qualification> qualifications, double salary) {
		
		if (name.trim().isEmpty() || salary < 0 || qualifications.size() < 1 ) {
			throw new IllegalArgumentException();
		} else {
			this.name = name;
			this.qualifications = new HashSet<Qualification>(qualifications);
			this.salary = salary;
			this.projects = new HashSet<>();
		}
    }

	@Override
	public boolean equals(Object other) {
		if (other == this)
			return true;
		if(other == null)
			return false;
		if (!(other instanceof Worker)) {
			return false;
		}

		Worker otherWorker = (Worker) other;
		return this.name.equals(otherWorker.name);
	}

	@Override
	public String toString() {
		return this.name + ":" + getProjects().size() + ":" + getQualifications().size() + ":" + (int)this.salary;
	}

    public String getName() {
        return this.name;
    }


    public double getSalary() {
        return this.salary;
    }

	public void setSalary(double salary) {
		if (salary < 0) {
			throw new IllegalArgumentException();
		} else {
			this.salary = salary;
		}
	}

	public Set<Qualification> getQualifications() {
		if (this.qualifications.isEmpty()) {
			return Collections.<Qualification>emptySet();
		} 
        return Collections.unmodifiableSet(this.qualifications);
    }

    public void addQualification(Qualification qualification) {
		if(qualification == null)
				throw new NullPointerException("Cannot add a null qualification to worker");
		this.qualifications.add(qualification);
		}
	


	public Set<Project> getProjects() {
		if(this.projects.isEmpty()){
			return Collections.<Project>emptySet();
		}

		return new HashSet<Project>(this.projects);
	}

	public void addProject(Project project) {
		if (project == null) {
			throw new NullPointerException();
		}
		this.projects.add(project);
	}

	public void removeProject(Project project) {
    	if(project == null){
			throw new IllegalArgumentException();
		}
		projects.remove(project);
	}


	public int getWorkload() {
		if(projects.isEmpty())
			return 0;

		int workload = 0;
		for (Project project : projects) {
			if(project.getStatus()!= ProjectStatus.FINISHED){
				workload += project.getSize().getValue();
			}
		}
		return workload;
	}

	public boolean willOverload(Project project) {
    	if (this.projects.contains(project)) return false;

    	if(project.getStatus() == ProjectStatus.FINISHED) return false;

		int totalWorkload = getWorkload() + project.getSize().getValue();
		return totalWorkload > MAX_WORKLOAD;
	}

	public boolean isAvailable() {
		return getWorkload() < MAX_WORKLOAD;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.name);
	}

	public WorkerDTO toDTO() {
		return new WorkerDTO(getName(), getSalary(), getWorkload(), convertProjectSetToStringArray(getProjects()), convertQualificationSetToStringArray(getQualifications()));
	}
	
	private String[] convertProjectSetToStringArray(Set<Project> set){
		String[] setAsStringArray = new String[set.size()];
		Iterator<Project> setIterator = set.iterator();

		int index = 0;
		while(setIterator.hasNext()){
			setAsStringArray[index] = setIterator.next().getName();
			index++;
		}
		return setAsStringArray;
	}
	
	private String[] convertQualificationSetToStringArray(Set<Qualification> set){
		String[] setAsStringArray = new String[set.size()];
		Iterator<Qualification> setIterator = set.iterator();

		int index = 0;
		while(setIterator.hasNext()){
			setAsStringArray[index] = setIterator.next().toString();
			index++;
		}
		return setAsStringArray;
	}
}
