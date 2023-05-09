package edu.colostate.cs415.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.Iterator;


import edu.colostate.cs415.dto.ProjectDTO;

public class Project {

	private String name;
	private ProjectSize size;
	private ProjectStatus status;
	private Set<Worker> workers;
	private Set<Qualification> qualifications;

	public Project(String name, Set<Qualification> qualifications, ProjectSize size) {
		if (name.trim().isEmpty()) {
        	throw new IllegalArgumentException();
        } else if (qualifications.size() < 1){
        	throw new IllegalArgumentException();			
		} else if( size.getValue() > 3 || size.getValue() < 1){
			throw new IllegalArgumentException();		
		}
		else {
            this.name = name;
            this.qualifications = new HashSet<Qualification>(qualifications);
			this.size = size;
        }
		this.workers = new HashSet<>();
		this.status = ProjectStatus.PLANNED;
	}

	@Override
	public boolean equals(Object other) {
  		if (other == this) {
			return true;
		}
		if (other == null || getClass() != other.getClass()) {
			return false;
		}
		Project otherProj = (Project) other;
		return name.equals(otherProj.name);
	}

	@Override
	public String toString() {
		String returnedString = getName() + ":" + this.workers.size() + ":" + getStatus().name().toUpperCase();
		return returnedString;
	}

	public String getName() {
		return this.name;
	}

	public ProjectSize getSize() {
		return this.size;
	}

	public ProjectStatus getStatus() {
		return this.status;
	}

	public void setStatus(ProjectStatus status) {
		this.status = status;
		//Should we still throw an exception after status changes to null?
		//I assumed the assumption that we messed up was that we wouldn't change the status
		if(status == null) throw new NullPointerException();
	}

	public void addWorker(Worker worker) {
		if(worker !=null){
			this.workers.add(worker);
		}
		else throw new IllegalArgumentException("Illegal Parameter passed in.");
	}

	public void removeWorker(Worker worker) {
		if(worker==null) throw new NullPointerException();
		this.workers.remove(worker);
	}

	public Set<Worker> getWorkers() {
		Set<Worker> copy = new HashSet<Worker>(this.workers);
		if(copy.isEmpty()){
			return Collections.<Worker>emptySet();
		}
		return copy;
	}

	public void removeAllWorkers() {
		this.workers.clear();
	}

	public Set<Qualification> getRequiredQualifications() {
		Set<Qualification> copy = new HashSet<Qualification>(qualifications);
		return copy;
	}

	public void addQualification(Qualification qualification) {
		if (qualification==null) throw new NullPointerException();
		if(this.status==ProjectStatus.FINISHED)
			throw new IllegalStateException("Cannot add qualifications to a finished project");
		this.qualifications.add(qualification);
		if(this.status ==  ProjectStatus.ACTIVE && getMissingQualifications().size() > 0){
				this.status = ProjectStatus.PLANNED;
		}
	}

	public Set<Qualification> getMissingQualifications() {
		Set<Qualification> missingQs = new HashSet<>();
		missingQs.addAll(getRequiredQualifications());

		for(Worker worker : getWorkers()){
			for(Qualification workerQs : worker.getQualifications()){
				missingQs.remove(workerQs);
			}
		}
		Set<Qualification> copy = new HashSet<Qualification>(missingQs);
		return copy;
		
	}

	public boolean isHelpful(Worker worker) {
		try{
			for(Qualification qual : worker.getQualifications()){
				if(getMissingQualifications().contains(qual)){
					return true;
				}
			}
			return false;
		} catch(Exception NullPointerException) {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.name);
	}

	public ProjectDTO toDTO() {
		return new ProjectDTO(getName(),this.size,getStatus(),
		stringArraySetWorkerConverter(getWorkers()),
		stringArrayQualificationConverter(getRequiredQualifications()),
		stringArrayQualificationConverter(getMissingQualifications()));
	}

	private String[] stringArraySetWorkerConverter(Set<Worker> givenWorkers){
		String[] workersToStringArray = new String[givenWorkers.size()];
		Iterator<Worker> setIterator = givenWorkers.iterator();

		int index = 0;
		while(setIterator.hasNext()){
			workersToStringArray[index] = setIterator.next().getName();
			index++;
		}
		return workersToStringArray;
	}
	private String[] stringArrayQualificationConverter(Set<Qualification> givenQualifications){
		String[] qualificationsToStringArray = new String[givenQualifications.size()];
		Iterator<Qualification> setIterator = givenQualifications.iterator();

		int index = 0;
		while(setIterator.hasNext()){
			qualificationsToStringArray[index] = setIterator.next().toString();
			index++;
		}
		return qualificationsToStringArray;
	}
}
