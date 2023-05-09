package edu.colostate.cs415.model;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.Iterator;

import edu.colostate.cs415.dto.QualificationDTO;

public class Qualification {

	private String description;
	private Set<Worker> workers;

	public Qualification(String description) {
		if (description.trim().isEmpty())
			throw new IllegalArgumentException();
		this.description = description;
		this.workers = new HashSet<>();
	}

	@Override
	public boolean equals(Object other) {
		if(other instanceof Qualification){
			if (other.toString().equals(this.description)){
				return true;
			}
			else return false;
		}
		else return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.description);
	}

	@Override
	public String toString() {
		return this.description;
	}

	public Set<Worker> getWorkers() {
		Set<Worker> copy = new HashSet<Worker>(this.workers);
		return copy;
	}

	public void addWorker(Worker worker) {
		if(worker==null) throw new NullPointerException("Cannot add a null worker to Qualification");
		this.workers.add(worker);
	}

	public void removeWorker(Worker worker) {
		if(worker==null) throw new NullPointerException("Cannot remove a null worker to Qualification");
		this.workers.remove(worker);
	}

	private String[] convert(Set<Worker> wset) {
		String[] qsetToArray = new String[wset.size()];
		Iterator<Worker> iterator = wset.iterator();

		int index = 0;
		while (iterator.hasNext()) {
			qsetToArray[index] = iterator.next().getName();
			index++;
		}

		return qsetToArray;
	}

	public QualificationDTO toDTO() {
		return new QualificationDTO(toString(), convert(getWorkers()));
	}
}
