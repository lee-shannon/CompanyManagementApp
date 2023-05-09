package edu.colostate.cs415.model;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.runners.statements.ExpectException;
import org.junit.runner.RunWith;

import java.util.*;
import edu.colostate.cs415.dto.ProjectDTO;
import edu.colostate.cs415.model.*;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;

@RunWith(JUnitParamsRunner.class)
public class ProjectTest {

	Set<Qualification> singleQualification = new HashSet<Qualification>();
	Object[][] equalsObjects;

	@Before
	public void establistVariables(){
		singleQualification.add(new Qualification("Constructor"));
		Project nullProject = null; 
		Project controlProject = new Project("Control",singleQualification,ProjectSize.SMALL);
		Project imposterProject = new Project("Control",singleQualification,ProjectSize.BIG);
		Project differentProject = new Project("Test",singleQualification,ProjectSize.BIG);
		Qualification qualificationObject = new Qualification("Control");
		equalsObjects = new Object[][] {
					new Object[] { controlProject, controlProject, true }, 
					new Object[] { controlProject, nullProject, false },
					new Object[] { controlProject, qualificationObject, false }, 
					new Object[] { controlProject, imposterProject, true },
					new Object[] { controlProject, differentProject, false } 
				};
	}

	
	private Set<Qualification> createQSet() {
        Qualification q1 = new Qualification("Public Speaking");
        Set<Qualification> qset = new HashSet<>();
        qset.add(q1);
        return qset;
    }   

    private void clearQSet(Set<Qualification> qset) {
        qset.clear();
    }
	
	@Test
	public void testProjectConstructor() {
		String projectName = "Order 66";
		ProjectSize sizeOfProject = ProjectSize.BIG;
		Project testProject = new Project(projectName,singleQualification,sizeOfProject);
		assert(testProject.getName().equals("Order 66"));
		assert(testProject.getSize().getValue() == 3);
		assert(testProject.getRequiredQualifications().size() == 1);
		assert (testProject.getStatus() == ProjectStatus.PLANNED);
	}

	@Test (expected = NullPointerException.class)
	public void testConstructor_nullName() {
		Project TEST = new Project(null, new HashSet<>(), ProjectSize.BIG);
	}

	@Test (expected = NullPointerException.class)
	public void testConstructor_nullQualifications() {
		Project TEST = new Project("PROJECT 1", null, ProjectSize.BIG);
	}

	@Test
	public void testProjectGetName(){
		Project testName = new Project("Skywalker", singleQualification, ProjectSize.BIG);
		assert(testName.getName().equals("Skywalker"));
	}

	@Test (expected = NullPointerException.class)
	public void testNullGetName() {
		Project testName = new Project(null, singleQualification, ProjectSize.BIG);
		testName.getName();
	}

	@Test (expected = IllegalArgumentException.class)
	public void testIllegalGetName1() {
		Project testName = new Project("",singleQualification, ProjectSize.BIG);
		testName.getName();
	}

	@Test (expected = IllegalArgumentException.class)
	public void testIllegalGetName2() {
		Project testName = new Project("   ", singleQualification, ProjectSize.BIG);
		testName.getName();
	}

	@Test
	public void testProjectGetSize(){
		Project testSize = new Project("Skywalker", singleQualification, ProjectSize.BIG);
		assert(testSize.getSize().getValue() == 3);
	}

	@Test(expected = NullPointerException.class)
	public void testProjectWithNullSize(){
		Project testSize = new Project("Skywalker", singleQualification, null);
	}

	@Test
	public void testProjectGetQualifications(){
		Qualification testQualification = new Qualification("Rebels");
		Set<Qualification> hashSetQualification = new HashSet<Qualification>();
		hashSetQualification.add(testQualification);
		Project testProjectQualifications = new Project("TEST", hashSetQualification, ProjectSize.SMALL);
		assert(testProjectQualifications.getRequiredQualifications().size() == 1);	
	}

	@Test
	public void testProjectGetQualifications_SingleQualification(){
		Project testProjectQualifications = new Project("TEST", singleQualification, ProjectSize.SMALL);
		assert(testProjectQualifications.getRequiredQualifications().size() == 1);
	}

	@Test
	public void testRemovingQualifications(){
		Project testProjectQualifications = new Project("TEST", singleQualification, ProjectSize.SMALL);
		assert(testProjectQualifications.getRequiredQualifications().size() == 1);

		testProjectQualifications.getRequiredQualifications().remove(new Qualification("Constructor"));
		assert(testProjectQualifications.getRequiredQualifications().size() == 1);

	}

	@Test
	public void testRemovingQualificationsInProject(){
		Set<Qualification> multipleQualifications = new HashSet<Qualification>();
		for(int number = 0; number < 100; number++) multipleQualifications.add(new Qualification(String.format("Qualification #%d",number)));
		Project testProjectQualifications = new Project("TEST", multipleQualifications, ProjectSize.SMALL);
		for(int number = 0; number < 50; number++) testProjectQualifications.getRequiredQualifications().remove(new Qualification(String.format("Qualification #%d",number)));
		assert(testProjectQualifications.getRequiredQualifications().size() == 100);
	}

	@Test
	public void testEquals() {
		Project p1 = new Project("Harvard", singleQualification, ProjectSize.SMALL);
		Project p2 = new Project("Harvard11", singleQualification, ProjectSize.SMALL);
		Project p3 = new Project("AE12036", singleQualification, ProjectSize.BIG);
		Project p4 = new Project("Harvard", singleQualification, ProjectSize.BIG);
	
		assert(!p1.equals(p2));
		assert(p1.equals(p4));
    }

	@Test
	public void testEqualsWhenObjectIsNull(){
		Project control = new Project("Darth Vader", singleQualification,ProjectSize.SMALL);
		assert(!control.equals(null));
	}
	
	@Test
	public void testEqualsWhenObjectIsOtherThanProject(){
		Project control = new Project("Darth Vader", singleQualification,ProjectSize.SMALL);
		Qualification qualificationObject = new Qualification("Sith Lord");
		assert(!control.equals(qualificationObject));
	}

	@Test
	public void testHashCodeForValues() {
		Set<Qualification> singleQualification = new HashSet<Qualification>();
		singleQualification.add(new Qualification("Control"));
		Project[] testing = new Project[]{new Project("Project 1",singleQualification, ProjectSize.SMALL), new Project("Banana 1",singleQualification, ProjectSize.SMALL)};
		Project CONTROL = new Project("Project 1", singleQualification, ProjectSize.BIG);
		for(Project test : testing){
			if(test.getName().equals(CONTROL.getName())) assert((CONTROL.hashCode() == test.hashCode()) == true);
			else assert((CONTROL.hashCode() == test.hashCode()) == false);
		}
	}

	@Test(expected = NullPointerException.class)
	public void testHashCode_NullValue() {
		Project CONTROL = null;
		int CONTROL_hashcode = CONTROL.hashCode();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testHashCode_IllegalValue() {
		Project CONTROL = new Project("", new HashSet<Qualification>(), ProjectSize.BIG);
		int CONTROL_hashcode = CONTROL.hashCode();
	}

	@Test
	public void testProjectGetStatus(){
		Project testProject = new Project("TEST", singleQualification, ProjectSize.SMALL);
		testProject.setStatus(ProjectStatus.ACTIVE);
		assert (testProject.getStatus() == ProjectStatus.ACTIVE);
	}

	
	@Test(expected = NullPointerException.class)
	public void testProjectSetStatusWithNull(){
		Project testProject = new Project("TEST", singleQualification, ProjectSize.SMALL);
		testProject.setStatus(null);
		//changes status to null
		assert (testProject.getStatus() == null);
	}

	@Test(expected = NullPointerException.class)
	public void testProjectSetStatusWithNullWhenSuspended(){
		Project testProject = new Project("TEST", singleQualification, ProjectSize.SMALL);
		testProject.setStatus(ProjectStatus.SUSPENDED);
		assert (testProject.getStatus() == ProjectStatus.SUSPENDED);		
		testProject.setStatus(null);
		assert (testProject.getStatus() == null);
	}

	@Test
	public void testGetWorker() {
		Project p1 = new Project("Project 1",createQSet(),ProjectSize.SMALL);
		Worker w1 = new Worker("Sarah",createQSet(),90000);
		Worker w2 = new Worker("Beth",createQSet(),90000);
		Worker w3 = new Worker("Ashley",createQSet(),90000);

		p1.addWorker(w1);
		p1.addWorker(w2);

		assert(p1.getWorkers().size() == 2);
		assert(p1.getWorkers().contains(w1));
		assert(p1.getWorkers().contains(w2));
		assert(!p1.getWorkers().contains(w3));
	}

	@Test
	public void testGetWorkerEmptySet() {
		Project p1 = new Project("Project 1",createQSet(),ProjectSize.SMALL);
		Worker w1 = new Worker("Sarah",createQSet(),90000);

		p1.addWorker(w1);
		p1.removeWorker(w1);

		assert(p1.getWorkers().isEmpty());
	}

	@Test
	public void testAddWorker() {
		Project p1 = new Project("Project 1",createQSet(),ProjectSize.SMALL);
		Worker w1 = new Worker("Sarah",createQSet(),90000);
		Worker w2 = new Worker("Beth",createQSet(),90000);

		p1.addWorker(w1);
		p1.addWorker(w2);

		assert(p1.getWorkers().contains(w1));
		assert(p1.getWorkers().contains(w1));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddWorkerWithNullWorker(){
		Project nullWorkerTest = new Project("Project 1",createQSet(),ProjectSize.SMALL);
		nullWorkerTest.addWorker(null);
		assert(nullWorkerTest.getWorkers().size() == 0);
	}

	@Test
	public void testRemoveWorker() {
		Project p1 = new Project("Project 1",createQSet(),ProjectSize.SMALL);
		Worker w1 = new Worker("Sarah",createQSet(),90000);
		Worker w2 = new Worker("Beth",createQSet(),90000);

		p1.addWorker(w1);
		p1.addWorker(w2);

		p1.removeWorker(w1);

		assert(!p1.getWorkers().contains(w1));
		assert(p1.getWorkers().contains(w2));
	}

	@Test(expected = NullPointerException.class)
	public void testRemoveWorker_NullWorker() {
		Project project = new Project("Project 1",singleQualification,ProjectSize.SMALL);
		project.removeWorker(null);
	}

	@Test
	public void testRemoveAllWorkers() {
		Project p1 = new Project("Project 1",createQSet(),ProjectSize.SMALL);
		Worker w1 = new Worker("Sarah",createQSet(),90000);
		Worker w2 = new Worker("Beth",createQSet(),90000);

		p1.addWorker(w1);
		p1.addWorker(w2);
		p1.removeAllWorkers();

		assert(p1.getWorkers().isEmpty());
	}

	@Test
	public void testProjectEquals(){
		Project testProject = new Project("TEST", singleQualification, ProjectSize.SMALL);
		// Same name, different memory location
		Project testProjectSAME_NAME_VALUE = new Project("TEST", singleQualification, ProjectSize.SMALL);
		// Same name, same memory location
		Project testProjectSAME_NAME_LOCATION = testProject; 
		// Different name
		Project testProjectDIFFERENT_NAME = new Project("Test2", singleQualification, ProjectSize.SMALL);
		
		assert(testProject.equals(testProjectSAME_NAME_VALUE));
		assert(testProject.equals(testProjectSAME_NAME_LOCATION));
		assert(!(testProject.equals(testProjectDIFFERENT_NAME)));

	}

	@Test
	public void testProjectToString(){
		Project testProject = new Project("TEST", singleQualification, ProjectSize.SMALL);
		String correctString = "TEST:0:PLANNED";
		assert(testProject.toString().equals(correctString));

		testProject.setStatus(ProjectStatus.ACTIVE);
		correctString = "TEST:0:ACTIVE";
		//This doesn't test for multiple workers yet since we don't have add worker functionality implemented
		assert(testProject.toString().equals(correctString));
	}


	@Test
	public void testProjectSetStatus(){
		Project testProject = new Project("TEST", singleQualification, ProjectSize.SMALL);
				
		testProject.setStatus(ProjectStatus.FINISHED);
		assert(testProject.getStatus() == ProjectStatus.FINISHED);

		testProject.setStatus(ProjectStatus.ACTIVE);
		assert(testProject.getStatus() == ProjectStatus.ACTIVE);

		testProject.setStatus(ProjectStatus.PLANNED);
		assert(testProject.getStatus() == ProjectStatus.PLANNED);

		testProject.setStatus(ProjectStatus.SUSPENDED);
		assert(testProject.getStatus() == ProjectStatus.SUSPENDED);

	}
	
	@Test
	public void testIsHelpful(){
		Set<Qualification> projectQualifications = new HashSet<Qualification>();

		for(int i = 0; i < 5; i++) {
			projectQualifications.add(new Qualification("test: " + i));	
		}
		Project HelpfulProject = new Project("TEST", projectQualifications, ProjectSize.SMALL);
		Worker helpfulWorker = new Worker("Worker Name: Helpful", projectQualifications, 10000);

		//tests if helpfulWorker will be helpful for a project because they have a required qualification
		assert(HelpfulProject.isHelpful(helpfulWorker) == true);
	}

	@Test
	public void testIsHelpful_notHelpful(){
		Set<Qualification> badQualifications = new HashSet<Qualification>();
		Set<Qualification> projectQualifications = new HashSet<Qualification>();
	
		for(int i = 0; i < 5; i++) {
			projectQualifications.add(new Qualification("test: " + i));	
			badQualifications.add(new Qualification("test: " + (i+100)));
		}
		Project HelpfulProject = new Project("TEST", projectQualifications, ProjectSize.SMALL);
		Worker notHelpfulWorker = new Worker("Worker Name: Not Helpful", badQualifications, 10000);
		//tests that notHelpfulWorker will be unhelpful due to lack of a required qualification
		assert(HelpfulProject.isHelpful(notHelpfulWorker) == false);
	}
	
	@Test 
	public void testIsHelpful_PassingNull() {
		Set<Qualification> projectQualifications = new HashSet<Qualification>();
		for(int i = 0; i < 5; i++) {
			projectQualifications.add(new Qualification("test: " + i));	
		}
		Project HelpfulProject = new Project("TEST", projectQualifications, ProjectSize.SMALL);
		HelpfulProject.isHelpful(null);
	}	

	@Test
	public void testGetMissingQualifications_SingleWorkerWithAllQualifications(){
		//Worker has all the needed qualifications
		Set<Qualification> projectQualifications = new HashSet<Qualification>();
		for(int quals = 0; quals < 10; quals++){
			projectQualifications.add(new Qualification("Test Number: "+ quals));
		}

		Project project = new Project("Control Project", projectQualifications, ProjectSize.BIG);
		project.addWorker(new Worker("John", projectQualifications, 10));

		assert(project.getMissingQualifications().isEmpty());
		assert(project.getRequiredQualifications().size()>0);
	}

	@Test
	public void testMissingQualifications(){
		//Project with 10 workers with 10 identical qualifications
		//Project will end with 1 missing qualification
		Set<Qualification> controlQualifications = new HashSet<Qualification>();
		Set<Qualification> testQualifications = new HashSet<Qualification>();
		for(int quals = 0; quals < 10; quals++){
			controlQualifications.add(new Qualification("Test Number: "+ quals));
			testQualifications.add(new Qualification("Test Number: "+ quals));
		}
		Set<Worker> workerSet = new HashSet<Worker>();
		for(int number = 0; number < 10; number++){
			Worker singleWorker = new Worker("Worker Name: "+ number, controlQualifications, number * 10000);
			workerSet.add(singleWorker);
		}
		Project controlProject = new Project("Control Project", testQualifications, ProjectSize.BIG);
		for(Worker worker : workerSet) controlProject.addWorker(worker);
		assert(controlProject.getMissingQualifications().size() == 0);

		controlProject.addQualification(new Qualification("Test Number: 10"));
		assert(controlProject.getRequiredQualifications().size() == 11);
		assert(controlProject.getMissingQualifications().size() == 1);
		//Checks that qualifications list isn't changed
		assert(controlProject.getRequiredQualifications().size() == 11);

		for(Qualification missing: controlProject.getMissingQualifications()){
			assert(missing.toString().equals("Test Number: 10"));
		}
		Set<Qualification> missingQ = new HashSet<Qualification>();
		missingQ.add(new Qualification("Test Number: 10"));
		Worker missingW = new Worker("Missing Link", missingQ, 10000);
		controlProject.addWorker(missingW);
		assert(controlProject.getMissingQualifications().size() == 0);
	}

  	@Test
	public void testProjectAddQualification(){
		Project testProject = new Project("AddQualification",singleQualification,ProjectSize.SMALL);
		for(int qual = 0; qual < 10; qual++){
			Qualification addTestQualification = new Qualification("Test Number: "+ qual);
			testProject.addQualification(addTestQualification);
			if(qual >= 5){
				Qualification duplicateQualification = new Qualification("Test Number: "+(qual - 5));
				testProject.addQualification(duplicateQualification);
			}
		}
		assert(testProject.getRequiredQualifications().size() == 11);
	}

	@Test(expected = NullPointerException.class)
	public void testAddQualifications_nullObject(){
		Project testProject = new Project("AddQualification",singleQualification,ProjectSize.SMALL);
		testProject.addQualification(null);
	}

	@Test
	public void testAddQualifications_activeProject(){
		Project testProject = new Project("AddQualification",singleQualification,ProjectSize.SMALL);
		testProject.setStatus(ProjectStatus.ACTIVE);
		testProject.addQualification(new Qualification("Qualifications for active project"));
		assert(testProject.getStatus() == ProjectStatus.PLANNED);
	}

	@Test(expected = IllegalStateException.class)
	public void testAddQualifications_finishedProject(){
		Project testProject = new Project("AddQualification",singleQualification,ProjectSize.SMALL);
		testProject.setStatus(ProjectStatus.FINISHED);
		testProject.addQualification(new Qualification("Qualifications for active project"));
	}

	@Test
	public void testProjectToDTOWithNoWorkers(){
		Set<Qualification> initialQualifications = new HashSet<Qualification>();
		String[] testQuals = new String[100];

		for(int addQs = 0; addQs < 100; addQs++){
			Qualification newQual = new Qualification(String.format("Test Q #%d",addQs));
			testQuals[addQs] = newQual.toString();
			initialQualifications.add(newQual);
		}
		Project testProject = new Project("Testing DTO",initialQualifications, ProjectSize.SMALL);
		ProjectDTO testProjectDTO = new ProjectDTO("Testing DTO", testProject.getSize(),testProject.getStatus(),new String[0],testQuals,testQuals);
		assert(testProjectDTO.equals(testProject.toDTO()));
		assert (testProjectDTO.getWorkers().length==0);
	}

	@Test
	public void testProjectToDTOWithWorkersAndNoMissingQualifications(){
		Project testProject = new Project("Testing DTO",singleQualification, ProjectSize.SMALL);
		String[] testQuals = new String[100];
		String[] testWorkers = new String[100];
		for(int addQs = 0; addQs < 100; addQs++){
			Qualification newQual = new Qualification(String.format("Test Q #%d",addQs));
			Set<Qualification> initialQs = new HashSet<Qualification>();
			initialQs.add(newQual);
			Worker newWorker = new Worker(String.format("Worker #%d",addQs),initialQs,addQs * 10000);
			testProject.addQualification(newQual);
			testProject.addWorker(newWorker);
			testQuals[addQs] = newQual.toString();
			testWorkers[addQs] = newWorker.getName();
		}
		ProjectDTO testProjectDTO = new ProjectDTO("Testing DTO", testProject.getSize(),testProject.getStatus(),testWorkers,testQuals,new String[0]);
		assert(testProjectDTO.equals(testProject.toDTO()));
		assert(Arrays.equals(testProjectDTO.getWorkers(),testWorkers));
	}


	public static Object[] toStringTestObjects() {
		Set<Qualification> qset = new HashSet<>();
		Qualification q1 = new Qualification("idk");
		qset.add(q1);
		Project projectWithNoWorkersActive = new Project("Active",qset,ProjectSize.SMALL);
		projectWithNoWorkersActive.setStatus(ProjectStatus.ACTIVE);
		Project projectWithNoWorkersSuspended = new Project("Suspended",qset,ProjectSize.SMALL);
		projectWithNoWorkersSuspended.setStatus(ProjectStatus.SUSPENDED);
		Project projectWithNoWorkersFinished = new Project("Finished",qset,ProjectSize.SMALL);
		projectWithNoWorkersFinished.setStatus(ProjectStatus.FINISHED);

		Project projectWithWorkers = new Project("Control",qset,ProjectSize.SMALL);
		Project projectWithOutWorkers = new Project("Control",qset,ProjectSize.SMALL);
		for(int addWorkers = 0; addWorkers < 10; addWorkers++){
			projectWithWorkers.addWorker(new Worker(String.format("Woreker #%d",addWorkers), qset, addWorkers * 10000));
		}

		Object[][] manyObjects = new Object[][] {
			new Object[]{projectWithOutWorkers, "Control:0:PLANNED", true},
			new Object[]{projectWithWorkers, "Control:10:PLANNED", true},
			new Object[]{projectWithNoWorkersActive, "Active:0:ACTIVE", true},			
			new Object[]{projectWithNoWorkersSuspended, "Suspended:0:SUSPENDED", true},			
			new Object[]{projectWithNoWorkersFinished, "Finished:0:FINISHED", true}			
  		};
		for(Object[] values: manyObjects){
		boolean condition = (Boolean) values[2];
		assert(values[0].toString().equals(values[1]) == condition);
		}
		return manyObjects;
    }

  	@Test
	public void testEqualsWithVaryingObjects(){
		for(Object[] values : equalsObjects){
			boolean expected = (Boolean)values[2];
		assert(values[0].equals(values[1]) == expected);
		}
	}

	@Test(expected = NullPointerException.class)
	public void testProjectInvalidNullConstructors(){
		Project test = new Project(null,null,null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testProjectInvalidIllegalArgConstructors(){
		Set<Qualification> testQs = new HashSet<Qualification>();
		for(int i = 0; i < 10; i++) testQs.add(new Qualification(String.format("Test Q #%d",i)));
		Project test = new Project(" ",testQs,ProjectSize.SMALL);
	}

	@Test
	public void testProjectValidConstructors(){
		Set<Qualification> testQs = new HashSet<Qualification>();
		for(int i = 0; i < 10; i++) testQs.add(new Qualification(String.format("Test Q #%d",i)));
		Project test = new Project("  Skywalker  ", testQs, ProjectSize.MEDIUM);
		assert(test.getRequiredQualifications().size() == 10);
		assert(test.getWorkers().size() == 0);
	}

	@Test
	public void testProjectConstructorCheckProjectSize(){
		int sizeValue = 1;
		for (ProjectSize size :ProjectSize.values()) {
			Project project = new Project(size.toString(),singleQualification,size);
			assert(project.getSize().getValue()==sizeValue);
			sizeValue++;
		}
	}

	@Test
	public void testProjectConstructorDefaultProjectStatus(){
		Project project = new Project("Project1",singleQualification,ProjectSize.MEDIUM);
		assert(project.getStatus()==ProjectStatus.PLANNED);
		assert(project.getStatus()!=ProjectStatus.ACTIVE);
	}
}
