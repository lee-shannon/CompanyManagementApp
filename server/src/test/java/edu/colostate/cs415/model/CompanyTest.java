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

import org.junit.Test;

@RunWith(JUnitParamsRunner.class)
public class CompanyTest {
	private Company testCompanyWithWorkersAndQualifications;
	private Company testCompanyWithWorkersAndNoQualifications;
	private Company testCompanyWithNoWorkers;
	private Company testCompanyWithQualificationsAndProject;
	private Company testCompanyWithAllThree;

	private Company testCompanyWithNoWorkersButHasQualifications;

	private Set<Worker> validWorkers;
	private Set<Qualification> validQualifications;
	private Set<Qualification> notValidQualifications;
	private Set<Project> validProjects;
	private Worker singleWorker;

	@Before
	public void createObjectsForTesting(){
		//Company initialization
		this.testCompanyWithWorkersAndQualifications = new Company("Company with Workers and Qualifications");
		this.testCompanyWithWorkersAndNoQualifications = new Company("Company with Workers and No Qualifications");
		this.testCompanyWithNoWorkers = new Company("Company with No Workers");
		this.testCompanyWithQualificationsAndProject  = new Company("Company with Qualifications and Projects");
		this.testCompanyWithAllThree = new Company("Company with Worker and Projects and Qualifications");
		this.testCompanyWithNoWorkersButHasQualifications = new Company("Company with No Workers But Has Qualifications");

		//Worker initalization
		this.validWorkers = new HashSet<Worker>();
		this.validQualifications = new HashSet<Qualification>();
		this.notValidQualifications = new HashSet<Qualification>();
		this.validProjects = new HashSet<Project>();
		
		//This line breaks the tests, and I have no idea why. Luckily rn it's not being used in any tests.
		//this.singleWorker = new Worker("Lonely Worker", validQualifications, 100000);

		createQualificationsAndProjectsAndWorkersToTestCompanys();
	}

	public void createQualificationsAndProjectsAndWorkersToTestCompanys(){
		for(int i = 0; i < 10; i++){
			createQualificationsForTestCompanys(i);
			createWorkersForTestCompanys(i);
			createProjectsForTestCompanys(i);
		}
	}

	public void createQualificationsForTestCompanys(int index){
		createValidQualificationsForTestCompanys(index);
		createInvalidQualificationsForTestCompanys(index);
	}

	public void createValidQualificationsForTestCompanys(int index){
		this.validQualifications.add(new Qualification(String.format("Qualification #%d",index)));
		this.testCompanyWithQualificationsAndProject.createQualification(String.format("Qualification #%d",index));
		this.testCompanyWithWorkersAndQualifications.createQualification(String.format("Qualification #%d",index));
		this.testCompanyWithNoWorkersButHasQualifications.createQualification(String.format("Qualification #%d",index));
		this.testCompanyWithAllThree.createQualification(String.format("Qualification #%d",index));
	}

	public void createInvalidQualificationsForTestCompanys(int index){
		this.notValidQualifications.add(new Qualification(String.format("Not valid Qualification #%d", index)));
	}

	public void createWorkersForTestCompanys(int index){
		this.validWorkers.add(new Worker(String.format("Worker #%d",index),validQualifications,100000));
		this.testCompanyWithWorkersAndQualifications.createWorker(String.format("Worker #%d",index),validQualifications,100000);
		this.testCompanyWithWorkersAndNoQualifications.createWorker(String.format("Worker #%d",index),validQualifications,100000);
		this.testCompanyWithAllThree.createWorker(String.format("Worker #%d",index),validQualifications,100000);
	}

	public void createProjectsForTestCompanys(int index){
		this.testCompanyWithQualificationsAndProject.createProject(String.format("Project #%d",index),validQualifications,ProjectSize.SMALL);
		this.testCompanyWithAllThree.createProject(String.format("Project #%d",index),validQualifications,ProjectSize.SMALL);
	}

    public void populateTestWorkerBeforeSectionWithVariablesAndObjects(){
		Company defaultCompany = new Company("New Company");
		Set<Qualification> validSetOfQualifications = new HashSet<Qualification>();
		for(int i = 0; i < 10; i++) validSetOfQualifications.add(new Qualification(String.format("Qualification #%d",i)));
	}

	@Test
	public void testConstructor_getName() {
		Company test = new Company("Company 1");
		assert(test.getName().equals("Company 1"));
	}

	@Test (expected = IllegalArgumentException.class)
	public void testConstructor_EmptyName() {
		Company test = new Company("");
	}

	@Test (expected = IllegalArgumentException.class)
	public void testConstructor_WhiteSpaceName() {
		Company test = new Company("    ");
	}

	@Test (expected = NullPointerException.class)
	public void testConstructor_NullName() {
		Company test = new Company(null);
	}


	@Test
	public void testEquals_equal() {
		Company test1 = new Company("Company 1");
		Company test2 = new Company("Company 1");
		assert(test1.equals(test2) == true);
	}

	@Test
	public void testEquals_sameObject() {
		Company test1 = new Company("Company 1");
		assert (test1.equals(test1)==true);
	}

	@Test
	public void testEquals_notEqual() {
		Company test1 = new Company("Company 1");
		Company test2 = new Company("Company 2");
		assert(test1.equals(test2) == false);

		Set<Qualification> qSet = new HashSet<>();
		Qualification py = new Qualification("Python");
		qSet.add(py);

		Worker testWorker = new Worker("Company 1", qSet, 1000);
		assert(test1.equals(testWorker) == false);
	}

	@Test
	public void testEquals_null() {
		Company test1 = new Company("Company 1");
		Company test2 = null;
		assert(test1.equals(test2) == false);
	}

	@Test
	public void testHashCode_equal() {
		Company test1 = new Company("Company 1");
		Company test2 = new Company("Company 1");
		assert(test1.hashCode() == test2.hashCode());
	}

	@Test
	public void testHashCode_notEqual() {
		Company test1 = new Company("Company 1");
		Company test2 = new Company("Company 2");
		assert(test1.hashCode() != test2.hashCode());
	}

	@Test
	public void testToString() {
		testCompanyWithWorkersAndQualifications.createProject("Project",validQualifications,ProjectSize.SMALL);
		String correct = "Company with Workers and Qualifications:10:1";
		assert(testCompanyWithWorkersAndQualifications.toString().equals(correct));
	}

	@Test
	public void testToString_NoAvailableWorkers() {
		testCompanyWithNoWorkersButHasQualifications.createProject("Project",validQualifications,ProjectSize.SMALL);

		String correct = "Company with No Workers But Has Qualifications:0:1";
		assert(testCompanyWithNoWorkersButHasQualifications.toString().equals(correct));
	}

	@Test
	public void testToString_NoProjects(){
		String correct = "Company with Workers and Qualifications:10:0";
		assert(testCompanyWithWorkersAndQualifications.toString().equals(correct));
	}

	/*******************************************************************************************************************************/
	//test getProject
	@Test
	public void testGetProject_WithProjects(){
		assert(testCompanyWithQualificationsAndProject.getProjects().size() == 10);
		for (int i = 0; i < 10; i++) {
			Project project = new Project(String.format("Project #%d",i),validQualifications,ProjectSize.SMALL);
			assert(testCompanyWithQualificationsAndProject.getProjects().contains(project));
		}
	}

	@Test
	public void testGetProject_WithNoProjects(){
		assert(testCompanyWithWorkersAndQualifications.getProjects().size() == 0);
		assert(testCompanyWithWorkersAndQualifications.getProjects().equals(Collections.emptySet()));
	}

	//	*******************************************************************************************************************************/
	// test createWorker
	@Test
	public void createWorker_ValidWorker(){
		Worker example = testCompanyWithNoWorkersButHasQualifications.createWorker("Darth Vader", validQualifications, 100000);
		assert(testCompanyWithNoWorkersButHasQualifications.getAvailableWorkers().size() == 1);
		assert(testCompanyWithNoWorkersButHasQualifications.getEmployedWorkers().size() == 1);
	}

	@Test
	public void createWorker_NullName(){
		Worker example = testCompanyWithNoWorkersButHasQualifications.createWorker(null, validQualifications, 100000);
		assert(example == null);
		assert(testCompanyWithNoWorkersButHasQualifications.getAvailableWorkers().size() == 0);
		assert(testCompanyWithNoWorkersButHasQualifications.getEmployedWorkers().size() == 0);
	}

	@Test
	public void createWorker_EmptyName(){
		Worker spacedName = testCompanyWithNoWorkersButHasQualifications.createWorker("    ", validQualifications, 100000);
		assert(spacedName == null);
		assert(testCompanyWithNoWorkersButHasQualifications.getAvailableWorkers().size() == 0);
		assert(testCompanyWithNoWorkersButHasQualifications.getEmployedWorkers().size() == 0);
		Worker emptyName = testCompanyWithNoWorkersButHasQualifications.createWorker("", validQualifications, 100000);
		assert(emptyName == null);
		assert(testCompanyWithNoWorkersButHasQualifications.getAvailableWorkers().size() == 0);
		assert(testCompanyWithNoWorkersButHasQualifications.getEmployedWorkers().size() == 0);
	}

	@Test
	public void createWorker_NullSet(){
		Worker example = testCompanyWithNoWorkersButHasQualifications.createWorker("Darth Vader", null, 100000);
		assert(example == null);
		assert(testCompanyWithNoWorkersButHasQualifications.getAvailableWorkers().size() == 0);
		assert(testCompanyWithNoWorkersButHasQualifications.getEmployedWorkers().size() == 0);
	}

	@Test
	public void createWorker_EmptySet(){
		Worker example = testCompanyWithNoWorkersButHasQualifications.createWorker("Darth Vader", new HashSet<Qualification>(), 100000);
		assert(example == null);
		assert(testCompanyWithNoWorkersButHasQualifications.getAvailableWorkers().size() == 0);
		assert(testCompanyWithNoWorkersButHasQualifications.getEmployedWorkers().size() == 0);
	}

	@Test
	public void createWorker_NotASubset(){
		Worker example = testCompanyWithNoWorkersButHasQualifications.createWorker("Darth Vader", notValidQualifications, 100000);
		assert(example == null);
		assert(testCompanyWithNoWorkersButHasQualifications.getAvailableWorkers().size() == 0);
		assert(testCompanyWithNoWorkersButHasQualifications.getEmployedWorkers().size() == 0);
	}

	@Test
	public void createWorker_NegativeSalary(){
		Worker workerPaysCompany = testCompanyWithNoWorkersButHasQualifications.createWorker("Darth Vader", validQualifications, -100000);
		assert(workerPaysCompany == null);
		assert(testCompanyWithNoWorkersButHasQualifications.getAvailableWorkers().size() == 0);
		assert(testCompanyWithNoWorkersButHasQualifications.getEmployedWorkers().size() == 0);
	}

	@Test
	public void createWorker_CompanyWithNoQualifications(){
		Worker shouldNotWork = testCompanyWithWorkersAndNoQualifications.createWorker("Darth Vader", validQualifications, 10000);
		assert(shouldNotWork == null);
		assert(testCompanyWithWorkersAndNoQualifications.getAvailableWorkers().size() == 0);
		assert(testCompanyWithWorkersAndNoQualifications.getEmployedWorkers().size() == 0);
	}

	@Test
	public void createWorker_CompanyWithOneQualification(){
		testCompanyWithWorkersAndNoQualifications.createQualification("Example of One Qualification");
		Worker shouldWork = testCompanyWithWorkersAndNoQualifications.createWorker("Darth Vader", new HashSet<Qualification>(Arrays.asList(new Qualification("Example of One Qualification"))), 10000);
		assert(shouldWork instanceof Worker);
		assert(testCompanyWithWorkersAndNoQualifications.getAvailableWorkers().size() == 1);
		assert(testCompanyWithWorkersAndNoQualifications.getEmployedWorkers().size() == 1);
	}

	@Test
	public void createWorker_WorkerQsAreSubSet(){
		Set<Qualification> subSet = new HashSet<Qualification>();
		for(int i = 0; i < 5; i++){
			subSet.add(new Qualification(String.format("Qualification #%d",i)));
		}
		Worker workerSubSet = testCompanyWithNoWorkersButHasQualifications.createWorker("Darth Vader", subSet, 10000);
		assert(workerSubSet instanceof Worker);
		assert(testCompanyWithNoWorkersButHasQualifications.getAvailableWorkers().size() == 1);
		assert(testCompanyWithNoWorkersButHasQualifications.getEmployedWorkers().size() == 1);
	}

	@Test
	public void createWorker_WorkerQsAreNotSubSet(){
		Set<Qualification> subSet = new HashSet<Qualification>();
		for(int i = 0; i < 5; i++){
			subSet.add(new Qualification(String.format("Qualification #%d",i)));
		}
		subSet.add(new Qualification(String.format("Qualification #%d",11)));
		Worker workerNotSubSet = testCompanyWithNoWorkersButHasQualifications.createWorker("Darth Vader", subSet, 10000);
		assert(workerNotSubSet==null);
		assert(testCompanyWithNoWorkersButHasQualifications.getAvailableWorkers().size() == 0);
		assert(testCompanyWithNoWorkersButHasQualifications.getEmployedWorkers().size() == 0);
	}
  
  @Test
  public void createWorker_EmployeeAndAvailableStartAtZero(){
		assert(testCompanyWithNoWorkersButHasQualifications.getAvailableWorkers().size() == 0);
		assert(testCompanyWithNoWorkersButHasQualifications.getEmployedWorkers().size() == 0);
		assert(testCompanyWithWorkersAndQualifications.getAvailableWorkers().size() == 10);
		assert(testCompanyWithWorkersAndQualifications.getEmployedWorkers().size() == 10);		
		Worker exampleOne = testCompanyWithNoWorkersButHasQualifications.createWorker("Darth Vader", validQualifications, 100000);
		Worker exampleTwo = testCompanyWithWorkersAndQualifications.createWorker("Darth Vader", validQualifications, 100000);
		assert(testCompanyWithNoWorkersButHasQualifications.getAvailableWorkers().size() == 1);
		assert(testCompanyWithNoWorkersButHasQualifications.getEmployedWorkers().size() == 1);
		assert(testCompanyWithWorkersAndQualifications.getAvailableWorkers().size() == 11);
		assert(testCompanyWithWorkersAndQualifications.getEmployedWorkers().size() == 11);		
	}

	@Test
	public void createWorker_WorkerAlreadyInCompany(){
		assert(testCompanyWithWorkersAndQualifications.getAvailableWorkers().size() == 10);
		assert(testCompanyWithWorkersAndQualifications.getEmployedWorkers().size() == 10);		
		Worker exampleTwo = testCompanyWithWorkersAndQualifications.createWorker("Worker #0", validQualifications, 100000);
		assert(testCompanyWithWorkersAndQualifications.getAvailableWorkers().size() == 10);
		assert(testCompanyWithWorkersAndQualifications.getEmployedWorkers().size() == 10);	
    }

	@Test
	public void createWorker_MightBeAssignedAndNotInAvailable(){
		assert(testCompanyWithWorkersAndQualifications.getAvailableWorkers().size() == 10);
		assert(testCompanyWithWorkersAndQualifications.getEmployedWorkers().size() == 10);	
		Project bigProject1 = testCompanyWithWorkersAndQualifications.createProject("Project 1", validQualifications, ProjectSize.BIG);
		Project bigProject2 = testCompanyWithWorkersAndQualifications.createProject("Project 2", validQualifications, ProjectSize.BIG);
		Project bigProject3 = testCompanyWithWorkersAndQualifications.createProject("Project 3", validQualifications, ProjectSize.BIG);
		Project bigProject4 = testCompanyWithWorkersAndQualifications.createProject("Project 4", validQualifications, ProjectSize.BIG);
		Worker assigned = new Worker("Worker #0", validQualifications, 100000);
		testCompanyWithWorkersAndQualifications.assign(assigned,bigProject1);
		testCompanyWithWorkersAndQualifications.assign(assigned,bigProject2);
		testCompanyWithWorkersAndQualifications.assign(assigned,bigProject3);
		testCompanyWithWorkersAndQualifications.assign(assigned,bigProject4);
		assert(testCompanyWithWorkersAndQualifications.getAssignedWorkers().size() == 1);
		assert(testCompanyWithWorkersAndQualifications.getAssignedWorkers().contains(assigned));
		assert(testCompanyWithWorkersAndQualifications.getAvailableWorkers().size() == 9);
		assert(testCompanyWithWorkersAndQualifications.getEmployedWorkers().size() == 10);	
		testCompanyWithWorkersAndQualifications.createWorker("Worker #0",validQualifications,100000);
		assert(testCompanyWithWorkersAndQualifications.getAvailableWorkers().size() == 9);
		assert(testCompanyWithWorkersAndQualifications.getEmployedWorkers().size() == 10);	
	}

	@Test
	public void testCreateWorker_ValidWorkersInCompanyQualifications(){
		Company testCompany = new Company("Test");
		Set<Qualification> validSetOfQualifications = new HashSet<Qualification>();
		for(int i = 0; i < 10; i++) {
			validSetOfQualifications.add(new Qualification(String.format("Qualification #%d",i)));
			testCompany.createQualification(String.format("Qualification #%d",i));
		}
		Worker control = new Worker("Darth Vader", validSetOfQualifications, 100000);
		testCompany.createWorker("Darth Vader", validSetOfQualifications,100000);
		for(Qualification testCompanyQualifications : testCompany.getQualifications()){
			assert(testCompanyQualifications.getWorkers().size() == 1);
		}
	}

	@Test
	public void testCreateWorker_ValidWorkersInCompanyWithNoQualifications(){
		Company testCompany = new Company("Test");
		Set<Qualification> validSetOfQualifications = new HashSet<Qualification>();
		for(int i = 0; i < 10; i++) {
			validSetOfQualifications.add(new Qualification(String.format("Qualification #%d",i)));
		}
		Worker control = new Worker("Darth Vader", validSetOfQualifications, 100000);
		assert(testCompany.createWorker("Darth Vader", validSetOfQualifications,100000) == null);
	}

	@Test
	public void testCreateWorker_InvalidWorkers(){
		Company testCompany = new Company("Test");
		Set<Qualification> validSetOfQualifications = new HashSet<Qualification>();
		for(int i = 0; i < 10; i++) {
			validSetOfQualifications.add(new Qualification(String.format("Qualification #%d",i)));
			testCompany.createQualification(String.format("Qualification #%d",i));
		}
		assert(testCompany.createWorker("Darth Vader", new HashSet<>(),100000) == null);
		assert(testCompany.createWorker("Darth Vader", validSetOfQualifications,-100000) == null);
		assert(testCompany.createWorker("           ", validSetOfQualifications,100000) == null);
	}
	//	*******************************************************************************************************************************/

	@Test
	public void testgetQualifications_NoQualifications(){
		Company testCompany = new Company("With no qualifications");
		assert(testCompany.getQualifications().size() == 0);
	}

	@Test
	public void testCreateQualification_OneQualifications(){
		Company testCompany = new Company("With one qualifications");
		Qualification control = new Qualification("Control");
		assert(control.equals(testCompany.createQualification("Control")));
		assert(testCompany.getQualifications().size() == 1);
	}

	public void testCreateQualification_NullString(){
		Company testCompany = new Company("With one qualifications");
		Qualification test = testCompany.createQualification(null);
		assert (test == null);
	}

	public void testCreateQualification_EmptyString(){
		Company testCompany = new Company("With empty qualification name");
		Qualification test = testCompany.createQualification("");
		assert (test == null);
	}

	public void testCreateQualification_StringWithSpaces(){
		Company testCompany = new Company("With qualification name with only spaces");
		Qualification test = testCompany.createQualification("               ");
		assert (test == null);
	}

	@Test
	public void testCreateQualification_DuplicateQualifications(){
		Company testCompany = new Company("With one qualifications");
		for(int i = 0; i < 10; i++){
			testCompany.createQualification(String.format("Qualification #%d",i));
			if (i % 2 == 0) testCompany.createQualification(String.format("Qualification #%d",i));
		}
		assert(testCompany.getQualifications().size() == 10);
	}
	//	*******************************************************************************************************************************/
	//testGetAvailableWorkers()
	@Test
	public void testGetAvailableWorkers_WithWorkersAvailable(){
		assert(testCompanyWithAllThree.getAvailableWorkers().size() == 10);
		for (int i = 0; i < 10; i++) {
			Worker worker = new Worker(String.format("Worker #%d",i),validQualifications,100000);
			assert(testCompanyWithAllThree.getAvailableWorkers().contains(worker));
		}
	}

	@Test
	public void testGetAvailableWorkers_WithWorkersButSomeAvailable(){
		assert(testCompanyWithAllThree.getAvailableWorkers().size() == 10);
		Worker worker = new Worker("Worker #1",validQualifications,100000);

		for (int i = 0; i < 10; i++) {
			Project project = new Project(String.format("Project #%d", i), validQualifications, ProjectSize.SMALL);
			testCompanyWithAllThree.assign(worker, project);
		}
		
		Project project_medium = testCompanyWithAllThree.createProject(String.format("Project BIG"),validQualifications,ProjectSize.MEDIUM);
		testCompanyWithAllThree.assign(worker,project_medium);

		assert(testCompanyWithAllThree.getAvailableWorkers().size() == 9);
	}

	@Test
	public void testGetAvailableWorkers_WithWorkersButNoneAvailable() {
		Worker worker = testCompanyWithNoWorkersButHasQualifications.createWorker("Worker #1",validQualifications,100000);
		assert(testCompanyWithNoWorkersButHasQualifications.getAvailableWorkers().size() == 1);

		for (int i = 0; i < 12; i++) {
			Project project_medium = testCompanyWithNoWorkersButHasQualifications.createProject(String.format("Project #%d",i),validQualifications,ProjectSize.SMALL);
			testCompanyWithNoWorkersButHasQualifications.assign(worker,project_medium);
		}

		assert(testCompanyWithNoWorkersButHasQualifications.getAvailableWorkers().size() == 0);
		assert(testCompanyWithNoWorkersButHasQualifications.getAvailableWorkers().equals(Collections.emptySet()));
	}

	@Test
	public void testGetAvailableWorkers_WithNoWorkers(){
		assert (testCompanyWithNoWorkers.getAvailableWorkers().size() == 0);
		assert (testCompanyWithNoWorkers.getAvailableWorkers().equals(Collections.emptySet()));
	}
	//	*******************************************************************************************************************************/
// testCreateProject
	@Test
	public void testCreateProject() {
		Project testProject = new Project("Valid Project", validQualifications, ProjectSize.SMALL);
		Project returnProject = testCompanyWithWorkersAndQualifications.createProject(testProject.getName(), testProject.getRequiredQualifications(), testProject.getSize());
		assert (testCompanyWithWorkersAndQualifications.getProjects().contains(testProject));
		assert(returnProject!=null);
	}

	@Test
	public void testCreateProject_nullQualificationSet() {
		Project returnProject = testCompanyWithWorkersAndQualifications.createProject("Valid Project", null, ProjectSize.SMALL);
		assert (returnProject==null);
	}

	@Test
	public void testCreateProject_emptyQualificationSet() {
		Project returnProject = testCompanyWithWorkersAndQualifications.createProject("Valid Project", new HashSet<>(), ProjectSize.SMALL);
		assert (returnProject==null);
	}

	@Test
    public void testCreateProject_checkProjectSize() {
        Company testCompany = new Company("Company");
        Set<Qualification> qSet = new HashSet<>();
        Qualification py = new Qualification("Python");
        qSet.add(py);
		testCompany.createQualification("Python");

        for (int i = 0; i < 10; i++) {
            testCompany.createProject(String.format("Project #%d", i), qSet, ProjectSize.BIG);
        }

        assert (testCompany.getProjects().size() == 10);
    }

    @Test
    public void testCreateProject_EmptyQualificationSet() {
        Company testCompany = new Company("Company");
        Project project = testCompany.createProject("Project with no qualifications", new HashSet<>(), ProjectSize.BIG);
		assert(project == null);
    }

	@Test
	public void testCreateProject_companyAlreadyHasProject() {
		int projectSize = testCompanyWithQualificationsAndProject.getProjects().size();
		testCompanyWithQualificationsAndProject.createProject("Project 1",validQualifications,ProjectSize.SMALL);
		//The size shouldn't increase with a duplicate project added
		assert (testCompanyWithQualificationsAndProject.getProjects().size()!= projectSize);
	}
	//*******************************************************************************************************************************/

	@Test
	public void testGetEmployedWorkers_WithEmployedWorkers(){
		assert(testCompanyWithWorkersAndQualifications.getEmployedWorkers().size() == 10);
		assert(testCompanyWithWorkersAndNoQualifications.getEmployedWorkers().size() == 0);
	}

	@Test
	public void testGetEmployedWorkers_WithNoEmployedWorkers(){
		assert(testCompanyWithNoWorkers.getEmployedWorkers().size() == 0);
		assert(testCompanyWithWorkersAndNoQualifications.getAvailableWorkers().size() == 0);

	}
	@Test
	public void testCreateProject_addNullProject() {
        Company testCompany = new Company("Company");
		Set<Qualification> qSet = new HashSet<>();
        Qualification py = new Qualification("Python");
		qSet.add(py);

		testCompany.createProject(null, qSet, ProjectSize.BIG);
	}

	//*******************************************************************************************************************************/
	//checkAttributeStaysTheSame
	@Test
	public void checkAttributeStaysTheSame_GetName(){
		Company testCompany = new Company("Company");
		testCompany.getName().toUpperCase();
		assert (testCompany.getName().equals("Company"));
	}

	@Test
	public void checkAttributeStaysTheSame_getEmployedWorkers(){
		Worker worker = testCompanyWithWorkersAndQualifications.createWorker("Worker 1",validQualifications,100000);
		assert(testCompanyWithWorkersAndQualifications.getEmployedWorkers().size() == 11);
		//This operation shouldn't affect the class attributes
		testCompanyWithWorkersAndQualifications.getEmployedWorkers().remove(worker);
		assert(testCompanyWithWorkersAndQualifications.getEmployedWorkers().size() == 11);
	}

	@Test
	public void checkAttributeStaysTheSame_getAvailableWorkers(){
		Worker worker = testCompanyWithWorkersAndQualifications.createWorker("Worker 1",validQualifications,100000);
		assert(testCompanyWithWorkersAndQualifications.getAvailableWorkers().size() == 11);
		testCompanyWithWorkersAndQualifications.getAvailableWorkers().remove(worker);
		assert(testCompanyWithWorkersAndQualifications.getAvailableWorkers().size() == 11);
	}

	@Test
	public void checkAttributeStaysTheSame_getUnavailableWorkers() {
		testCompanyWithWorkersAndQualifications.createWorker("Worker 1", validQualifications, 100000);
		assert (testCompanyWithWorkersAndQualifications.getUnavailableWorkers().size() == 0);

		testCompanyWithWorkersAndQualifications.getAvailableWorkers().add(new Worker("Worker", validQualifications, 100));
		assert (testCompanyWithWorkersAndQualifications.getUnavailableWorkers().size() == 0);
	}

	@Test
	public void checkAttributeStaysTheSame_getAssignedWorkers() {
		Worker worker = testCompanyWithWorkersAndQualifications.createWorker("Worker 1",validQualifications,100000);
		Project project = testCompanyWithWorkersAndQualifications.createProject("Project",validQualifications, ProjectSize.SMALL);
		testCompanyWithWorkersAndQualifications.assign(worker,project);

		assert (testCompanyWithWorkersAndQualifications.getAssignedWorkers().size() == 1);
		testCompanyWithWorkersAndQualifications.getAssignedWorkers().remove(worker);
		assert (testCompanyWithWorkersAndQualifications.getAssignedWorkers().size() == 1);
	}

	@Test
	public void checkAttributeStaysTheSame_getProjects() {
		Project project = testCompanyWithQualificationsAndProject.createProject("Project",validQualifications, ProjectSize.SMALL);
		assert(testCompanyWithQualificationsAndProject.getProjects().size()==11);
		testCompanyWithQualificationsAndProject.getProjects().remove(project);
		assert(testCompanyWithQualificationsAndProject.getProjects().size()==11);
	}

	@Test
	public void checkAttributeStaysTheSame_getQualifications() {
		Qualification qualification = testCompanyWithQualificationsAndProject.createQualification("Qualification");
		assert(testCompanyWithQualificationsAndProject.getQualifications().size()==11);
		testCompanyWithQualificationsAndProject.getQualifications().remove(qualification);
		assert(testCompanyWithQualificationsAndProject.getQualifications().size()==11);
	}

/*******************************************************************************************************************************/
	//test for start()
	@Test
	public void testStart_BaseTest(){
		Project testProject = testCompanyWithWorkersAndQualifications.createProject("Test Project", validQualifications,ProjectSize.SMALL);
		assert (testProject.getStatus()==ProjectStatus.PLANNED);

		assert(testCompanyWithWorkersAndQualifications.getProjects().contains(testProject));

		assert (testProject.getStatus()==ProjectStatus.PLANNED);
		
		Worker testWorker = new Worker("Worker #1", validQualifications, 100000);
		testCompanyWithWorkersAndQualifications.assign(testWorker,testProject);
		testCompanyWithWorkersAndQualifications.start(testProject);
		assert (testProject.getStatus()==ProjectStatus.ACTIVE);

		
	}


	public void testStart_FinishedProject(){
		Project testProject = testCompanyWithWorkersAndQualifications.createProject("Test Project", validQualifications,ProjectSize.SMALL);
		testProject.setStatus(ProjectStatus.FINISHED);
		testCompanyWithWorkersAndQualifications.start(testProject);

		//No changes
		assert(testProject.getStatus() == ProjectStatus.FINISHED);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testStart_CompanyEmptyProjectSet(){
		Project testProject =new Project("Company Project Set Empty", validQualifications,ProjectSize.SMALL);
		testCompanyWithWorkersAndQualifications.start(testProject);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testStart_ProjectNotInCompanyProjectSet() {
		Project testProject =new Project("Project not in Company", validQualifications,ProjectSize.SMALL);

		for (int i = 0; i <3; i++) {
			testCompanyWithWorkersAndQualifications.createProject(String.format("Project #%d",i),validQualifications,ProjectSize.MEDIUM);
		}
		assert (testCompanyWithWorkersAndQualifications.getProjects().size()==3);

		testCompanyWithWorkersAndQualifications.start(testProject);
	}

	@Test(expected = NullPointerException.class)
	public void testStart_NullProject(){
		testCompanyWithWorkersAndQualifications.start(null);
	}


	@Test(expected = IllegalArgumentException.class)
	public void testStart_CompanyHasMissingQualifications(){
		Project missingQs = testCompanyWithQualificationsAndProject.createProject("Missing Qs", validQualifications, ProjectSize.SMALL);
		Set<Qualification> partialQualifications = new HashSet<Qualification>();
		for(int i = 0; i < 5; i++) partialQualifications.add(new Qualification((String.format("Qualification #%d",i))));
		Worker somewhatHelpful = testCompanyWithQualificationsAndProject.createWorker("Partial", partialQualifications, 10000);
		testCompanyWithQualificationsAndProject.assign(somewhatHelpful,missingQs);
		testCompanyWithQualificationsAndProject.start(missingQs);
	}
/**********************************************************************************************************/


/******************************************************************************************* */
	//testingGetUnavailableWorkers
	
	@Test
	public void GetUnavailableWorkers_AllEmployeesAvailableNoProjectsAssigned() {
		testCompanyWithNoWorkersButHasQualifications.createWorker("Dave", validQualifications, 80000);
		testCompanyWithNoWorkersButHasQualifications.createWorker("John", validQualifications, 80000);
		assert(testCompanyWithNoWorkersButHasQualifications.getUnavailableWorkers().isEmpty());
	}

	@Test 
	public void getUnavailableWorkers_EmployeeNotAvailableDueToLoadLimit(){
		Worker workerToBeUnavailable = new Worker("Worker", validQualifications, 1010);
		testCompanyWithNoWorkersButHasQualifications.createWorker("Worker", validQualifications, 1010);
		//Enough projects to overload a worker
		Project bigProject1 = new Project("Project1", validQualifications, ProjectSize.BIG);
		Project bigProject2 = new Project("Project2", validQualifications, ProjectSize.BIG);
		Project bigProject3 = new Project("Project3", validQualifications, ProjectSize.BIG);
		Project bigProject4 = new Project("Project4", validQualifications, ProjectSize.BIG);
		testCompanyWithNoWorkersButHasQualifications.createProject("Project1", validQualifications, ProjectSize.BIG);
		testCompanyWithNoWorkersButHasQualifications.createProject("Project2", validQualifications, ProjectSize.BIG);
		testCompanyWithNoWorkersButHasQualifications.createProject("Project3", validQualifications, ProjectSize.BIG);
		testCompanyWithNoWorkersButHasQualifications.createProject("Project4", validQualifications, ProjectSize.BIG);

		assert(testCompanyWithNoWorkersButHasQualifications.getEmployedWorkers().size() == 1);
		assert(testCompanyWithNoWorkersButHasQualifications.getAvailableWorkers().size() == 1);
		assert(testCompanyWithNoWorkersButHasQualifications.getUnavailableWorkers().isEmpty());
		assert(testCompanyWithNoWorkersButHasQualifications.getAssignedWorkers().isEmpty());

		testCompanyWithNoWorkersButHasQualifications.assign(workerToBeUnavailable, bigProject1);
		testCompanyWithNoWorkersButHasQualifications.assign(workerToBeUnavailable, bigProject2);
		testCompanyWithNoWorkersButHasQualifications.assign(workerToBeUnavailable, bigProject3);
		testCompanyWithNoWorkersButHasQualifications.assign(workerToBeUnavailable, bigProject4);

		assert(testCompanyWithNoWorkersButHasQualifications.getUnavailableWorkers().size() == 1);
	}

/****************************************************************************************************** */

	// Tests for assign()
	@Test
	public void checkTestingObjects(){
		assert(testCompanyWithWorkersAndQualifications.getQualifications().size() == 10);
		assert(testCompanyWithWorkersAndQualifications.getAvailableWorkers().size() == 10);
		assert(testCompanyWithWorkersAndQualifications.getEmployedWorkers().size() == 10);
		assert(testCompanyWithWorkersAndQualifications.getProjects().size() == 0);
		assert(testCompanyWithWorkersAndQualifications.getAssignedWorkers().size() == 0);
		Worker controlWorker = new Worker("Worker #1",validQualifications, 100000);
		assert(testCompanyWithWorkersAndQualifications.getEmployedWorkers().contains(controlWorker));

	}

	@Test
	public void Assign_WithValidProjectandWorker(){
		checkTestingObjects();
		Project controlProject = new Project("Testing Project", validQualifications, ProjectSize.SMALL);
		Worker controlWorker = new Worker("Worker #1",validQualifications, 100000);
		testCompanyWithWorkersAndQualifications.createProject("Testing Project", validQualifications, ProjectSize.SMALL);
		assert(testCompanyWithWorkersAndQualifications.getProjects().size() == 1);
		testCompanyWithWorkersAndQualifications.assign(controlWorker, controlProject);
		assert(testCompanyWithWorkersAndQualifications.getAvailableWorkers().size() == 10);
		assert(testCompanyWithWorkersAndQualifications.getEmployedWorkers().size() == 10);
		assert(testCompanyWithWorkersAndQualifications.getAssignedWorkers().size() == 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void Assign_WithNullWorker(){
		checkTestingObjects();
		Project controlProject = new Project("Testing Project", validQualifications, ProjectSize.SMALL);
		testCompanyWithWorkersAndQualifications.createProject("Testing Project", validQualifications, ProjectSize.SMALL);
		assert(testCompanyWithWorkersAndQualifications.getProjects().size() == 1);
		testCompanyWithWorkersAndQualifications.assign(null, controlProject);
	}

	@Test(expected = IllegalArgumentException.class)
	public void Assign_WorkerhasProject(){
		checkTestingObjects();
		Project controlProject = new Project("Testing Project", validQualifications, ProjectSize.SMALL);
		Worker controlWorker = new Worker("Worker #1",validQualifications, 100000);
		controlWorker.addProject(controlProject);
		assert(testCompanyWithWorkersAndQualifications.getEmployedWorkers().contains(controlWorker));
		testCompanyWithWorkersAndQualifications.createProject("Testing Project", validQualifications, ProjectSize.SMALL);
		assert(testCompanyWithWorkersAndQualifications.getProjects().size() == 1);
		testCompanyWithWorkersAndQualifications.assign(controlWorker, controlProject);
	}

	@Test(expected = IllegalArgumentException.class)
	public void Assign_NullProject(){
		checkTestingObjects();
		Worker controlWorker = new Worker("Worker #1",validQualifications, 100000);
		testCompanyWithWorkersAndQualifications.createProject("Testing Project", validQualifications, ProjectSize.SMALL);
		testCompanyWithWorkersAndQualifications.assign(controlWorker, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void Assign_ProjecthasWorker(){
		checkTestingObjects();
		Project controlProject = new Project("Testing Project", validQualifications, ProjectSize.SMALL);
		Worker controlWorker = new Worker("Worker #1",validQualifications, 100000);
		controlProject.addWorker(controlWorker);
		assert(testCompanyWithWorkersAndQualifications.getEmployedWorkers().contains(controlWorker));
		testCompanyWithWorkersAndQualifications.createProject("Testing Project", validQualifications, ProjectSize.SMALL);
		assert(testCompanyWithWorkersAndQualifications.getProjects().size() == 1);
		testCompanyWithWorkersAndQualifications.assign(controlWorker, controlProject);
	}

	@Test(expected = IllegalArgumentException.class)
	public void Assign_WithValidProjectandInvalidWorker(){
		checkTestingObjects();
		Set<Qualification> opposite = new HashSet<Qualification>();
		opposite.add(new Qualification("Not in project"));
		Project controlProject = new Project("Testing Project", validQualifications, ProjectSize.SMALL);
		Worker controlWorker = new Worker("Worker #1",opposite, 100000);
		assert(testCompanyWithWorkersAndQualifications.getEmployedWorkers().contains(controlWorker));
		testCompanyWithWorkersAndQualifications.createProject("Testing Project", validQualifications, ProjectSize.SMALL);
		assert(testCompanyWithWorkersAndQualifications.getProjects().size() == 1);
		testCompanyWithWorkersAndQualifications.assign(controlWorker, controlProject);
	}

	@Test
	public void Assign_WithValidProjectandWorkerWhenWorkerGetsOverLoaded(){
		checkTestingObjects();
		Project controlProjectOne = new Project("Testing Project 1: ", validQualifications, ProjectSize.BIG);
		Project controlProjectTwo = new Project("Testing Project 2: ", validQualifications, ProjectSize.BIG);
		Project controlProjectThree = new Project("Testing Project 3: ", validQualifications, ProjectSize.BIG);
		Project controlProjectFour = new Project("Testing Project 4: ", validQualifications, ProjectSize.BIG);
		testCompanyWithNoWorkersButHasQualifications.createProject("Testing Project 1: ", validQualifications, ProjectSize.BIG);
		testCompanyWithNoWorkersButHasQualifications.createProject("Testing Project 2: ", validQualifications, ProjectSize.BIG);
		testCompanyWithNoWorkersButHasQualifications.createProject("Testing Project 3: ", validQualifications, ProjectSize.BIG);
		testCompanyWithNoWorkersButHasQualifications.createProject("Testing Project 4: ", validQualifications, ProjectSize.BIG);
		assert(testCompanyWithNoWorkersButHasQualifications.getProjects().size() == 4);
		Worker controlWorker = new Worker("Control Worker",validQualifications, 10000);
		testCompanyWithNoWorkersButHasQualifications.createWorker("Control Worker", validQualifications, 10000);
		assert(testCompanyWithNoWorkersButHasQualifications.getEmployedWorkers().size() == 1);
		testCompanyWithNoWorkersButHasQualifications.assign(controlWorker, controlProjectOne);
		testCompanyWithNoWorkersButHasQualifications.assign(controlWorker, controlProjectTwo);
		testCompanyWithNoWorkersButHasQualifications.assign(controlWorker, controlProjectThree);
		testCompanyWithNoWorkersButHasQualifications.assign(controlWorker, controlProjectFour);
		//Worker should be overloaded and removed from assigned set
		assert(testCompanyWithNoWorkersButHasQualifications.getAssignedWorkers().size() == 1);
		assert(testCompanyWithNoWorkersButHasQualifications.getAvailableWorkers().size() == 0);
	}
  
	//Assign_WorkerOrProjectQualsAreNotSubSets split up into next 3
	@Test (expected = IllegalArgumentException.class)
	public void Assign_WorkerQualsIsNotSubSet(){
		Worker workerWithInvalidQualifications = new Worker("[Worker] Has invalid qualifications", notValidQualifications, 10000);
		Project validProject = new Project("Valid Project", validQualifications, ProjectSize.BIG);
		//Test if worker quals is not a subset of company quals
		assert(!testCompanyWithQualificationsAndProject.getQualifications().containsAll(workerWithInvalidQualifications.getQualifications()));
		//Test if assign throws error
		testCompanyWithQualificationsAndProject.assign(workerWithInvalidQualifications, validProject);
	}

	@Test (expected = IllegalArgumentException.class)
	public void Assign_ProjectQualsIsNotSubset(){
		Worker workerWithValidQualifications = new Worker("[Worker] has valid qualifications", validQualifications, 10000);
		Project projectWithInvalidQualifications = new Project("[Project] has valid qualifications", notValidQualifications, ProjectSize.SMALL);
		//Test if project quals is not a subset of company quals
		assert(!testCompanyWithQualificationsAndProject.getQualifications().containsAll(projectWithInvalidQualifications.getRequiredQualifications()));
		//Test if assign throws error
		testCompanyWithQualificationsAndProject.assign(workerWithValidQualifications, projectWithInvalidQualifications);
	}

	@Test (expected = IllegalArgumentException.class)
	public void Assign_ProjectQualsAndWorkerQualsIsNotSubset_WorkerAndProjectHaveSameWrongQualifications(){
		Worker workerWithInvalidQualifications = new Worker("[Worker] Has invalid qualifications", notValidQualifications, 10000);
		Project projectWithInvalidQualifications = new Project("[Project] has valid qualifications", notValidQualifications, ProjectSize.SMALL);
		
		//Test if worker quals AND project quals is not a subset of company quals
		assert(!testCompanyWithQualificationsAndProject.getQualifications().containsAll(workerWithInvalidQualifications.getQualifications()));
		assert(!testCompanyWithQualificationsAndProject.getQualifications().containsAll(projectWithInvalidQualifications.getRequiredQualifications()));

		testCompanyWithQualificationsAndProject.assign(workerWithInvalidQualifications, projectWithInvalidQualifications);
	}
  
	@Test
	public void Assign_WorkerBecomesOverloaded(){
		Worker toBeOverloaded = new Worker("Worker #1",validQualifications, 100000);
		Project controlProjectOne = testCompanyWithWorkersAndQualifications.createProject("Testing Project 1: ", validQualifications, ProjectSize.BIG);
		Project controlProjectTwo = testCompanyWithWorkersAndQualifications.createProject("Testing Project 2: ", validQualifications, ProjectSize.BIG);
		Project controlProjectThree = testCompanyWithWorkersAndQualifications.createProject("Testing Project 3: ", validQualifications, ProjectSize.BIG);
		Project controlProjectFour = testCompanyWithWorkersAndQualifications.createProject("Testing Project 4: ", validQualifications, ProjectSize.BIG);

		testCompanyWithWorkersAndQualifications.assign(toBeOverloaded,controlProjectOne);
		testCompanyWithWorkersAndQualifications.assign(toBeOverloaded,controlProjectTwo);
		testCompanyWithWorkersAndQualifications.assign(toBeOverloaded,controlProjectThree);
		testCompanyWithWorkersAndQualifications.assign(toBeOverloaded,controlProjectFour);
		assert(toBeOverloaded.isAvailable() == false);
		assert(testCompanyWithWorkersAndQualifications.getAssignedWorkers().size() == 1);
		assert(testCompanyWithWorkersAndQualifications.getAvailableWorkers().size() == 9);
		}
    
	@Test(expected = IllegalArgumentException.class)
	public void Assign_WorkerNotEmployeed(){
		Worker fake = new Worker("Free Loader", validQualifications, 1000);
		Project controlProject = testCompanyWithNoWorkersButHasQualifications.createProject("Testing Project 1: ", validQualifications, ProjectSize.BIG);
		testCompanyWithNoWorkersButHasQualifications.assign(fake, controlProject);
    }
    
  @Test(expected = IllegalArgumentException.class)
	public void Assign_NoAvailableWorker(){
		Project bigProject1 = testCompanyWithNoWorkersButHasQualifications.createProject("Project 1", validQualifications, ProjectSize.BIG);
		Project bigProject2 = testCompanyWithNoWorkersButHasQualifications.createProject("Project 2", validQualifications, ProjectSize.BIG);
		Project bigProject3 = testCompanyWithNoWorkersButHasQualifications.createProject("Project 3", validQualifications, ProjectSize.BIG);
		Project bigProject4 = testCompanyWithNoWorkersButHasQualifications.createProject("Project 4", validQualifications, ProjectSize.BIG);
		Project bigProject5 = testCompanyWithNoWorkersButHasQualifications.createProject("Project 5", validQualifications, ProjectSize.BIG);

		Worker becomeUnavailableToAssign = testCompanyWithNoWorkersButHasQualifications.createWorker("OverLoad", validQualifications, 10000);
		testCompanyWithNoWorkersButHasQualifications.assign(becomeUnavailableToAssign,bigProject1);
		testCompanyWithNoWorkersButHasQualifications.assign(becomeUnavailableToAssign,bigProject2);
		testCompanyWithNoWorkersButHasQualifications.assign(becomeUnavailableToAssign,bigProject3);
		testCompanyWithNoWorkersButHasQualifications.assign(becomeUnavailableToAssign,bigProject4);
		//Failure, worker is not available --> became overloaded
		testCompanyWithNoWorkersButHasQualifications.assign(becomeUnavailableToAssign,bigProject5);
  }
  
	public void Assign_WorkerNotInEmployeeSet(){
		Worker notInSet = new Worker("Not Employeed", validQualifications, 100000);
		Project testProject = testCompanyWithWorkersAndNoQualifications.createProject("Test Project", validQualifications, ProjectSize.SMALL);
		testCompanyWithWorkersAndNoQualifications.assign(notInSet,testProject);
	}
  

	@Test
	public void Assign_WorkerCanBeAssignedToMoreThanOneProject(){
		Worker workAholic = testCompanyWithNoWorkersButHasQualifications.createWorker("Worker", validQualifications, 1000000);
		for(int i = 0; i < 10; i++){
			Project controlProject = testCompanyWithNoWorkersButHasQualifications.createProject(String.format("Testing Project: %d",i), validQualifications, ProjectSize.SMALL);
			testCompanyWithNoWorkersButHasQualifications.assign(workAholic, controlProject);
			assert(testCompanyWithNoWorkersButHasQualifications.getAssignedWorkers().size() == 1);
		}
		assert(workAholic.getProjects().size() == 10);
		assert(testCompanyWithNoWorkersButHasQualifications.getEmployedWorkers().size() == 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void Assign_ProjectNotInCompany(){
		Project project = new Project("New Project",validQualifications, ProjectSize.SMALL);
		Worker worker = testCompanyWithQualificationsAndProject.createWorker("Worker", validQualifications, 1000000);
		assert (testCompanyWithQualificationsAndProject.getProjects().size()==10);
		testCompanyWithQualificationsAndProject.assign(worker,	project);
	}

	@Test(expected = IllegalArgumentException.class)
	public void Assign_CompanyProjectSetIsZero(){
		Project project = new Project("New Project",validQualifications, ProjectSize.SMALL);
		Worker worker = testCompanyWithNoWorkersButHasQualifications.createWorker("Worker", validQualifications, 1000000);
		testCompanyWithNoWorkersButHasQualifications.assign(worker,	project);
	}
	/************************************************************************************* */
	// Test Unassign()

	@Test (expected = IllegalArgumentException.class)
	public void testUnassign_illegalValues() {
		Worker null_worker = null;
		Project null_project = null;
		Company company = new Company("Company");

		company.unassign(null_worker, null_project);
	}

	//Worker is assigned to project
	//Worker is only assigned to this one project, and project only has this one worker
	@Test 
	public void testUnassign_successfulUnassign_OneWorkerWhoFulfillsQualifications() {
		Company company = new Company("Company");

		for (int i = 0; i < 30; i++) {
			company.createQualification(String.format("Qualification #%d", i));
		}

		assert(company.getQualifications().size() == 30);	

		Project project = new Project("Project", validQualifications, ProjectSize.SMALL);
		Worker worker = new Worker("Worker", validQualifications, 1000);

		company.createProject("Project", validQualifications, ProjectSize.SMALL);
		company.createWorker("Worker", validQualifications, 1000);
	

		assert(company.getEmployedWorkers().contains(worker));
		assert(company.getEmployedWorkers().size() == 1);
		assert(company.getProjects().contains(project));
		
		company.assign(worker, project);
		company.start(project);
		
		assert(worker.getProjects().contains(project));
		assert(worker.getProjects().size() == 1);
		assert(project.getStatus().equals(ProjectStatus.ACTIVE));
		assert(project.getWorkers().contains(worker));
		assert(project.getWorkers().size() == 1);
		assert(company.getAssignedWorkers().contains(worker));
		assert(company.getAvailableWorkers().contains(worker));

		//project is ACTIVE with one worker assigned to it
		//worker fulfills all of projects requirements, so unassigning worker should change project status to SUSPENDED
		//and remove worker from assigned

		company.unassign(worker, project);
		assert(!company.getAssignedWorkers().contains(worker));
		assert(project.getStatus().equals(ProjectStatus.SUSPENDED));
		assert(company.getAvailableWorkers().contains(worker));
		assert(project.getWorkers().size() == 0);
		assert(worker.getProjects().size() == 0);
	}

	//2 workers are assigned to ACTIVE project as their only project, both workers together fulfill all of projects requirements but not individually
	//unassigning 1 worker should  change project status from ACTIVE to SUSPENDED
	//unassigned worker should be removed from assigned
	@Test
	public void testUnassign_successfulUnassign_TwoWorkersWhoFulfillsQualifications() {
		Set<Qualification> w1_qSet = new HashSet<>();
		Set<Qualification> w2_qSet = new HashSet<>();

		//each worker has 5 qualifications each that complete the project's qualification requirement
		for (int i = 0; i < 10; i++) {
			if (i <= 4) {
				w1_qSet.add(new Qualification(String.format("Qualification #%d", i)));
			} else if (i > 4) {
				w2_qSet.add(new Qualification(String.format("Qualification #%d", i)));
			}
		}

		Company company = new Company("Company");

		for (int i = 0; i < 30; i++) {
			company.createQualification(String.format("Qualification #%d", i));
		}

		Project project = new Project("Project", validQualifications, ProjectSize.SMALL);
		Worker worker1 = new Worker("Worker 1", w1_qSet, 1000);
		Worker worker2 = new Worker("Worker 2", w2_qSet, 1000);

		company.createProject("Project", validQualifications, ProjectSize.SMALL);
		company.createWorker("Worker 1", w1_qSet, 1000);
		company.createWorker("Worker 2", w2_qSet, 1000);

		assert(worker1.getQualifications().size() == 5 && worker2.getQualifications().size() == 5);

		assert(company.getEmployedWorkers().size() == 2);
		assert(company.getProjects().size() == 1);
		assert(company.getAvailableWorkers().size() ==2);

		//2 workers are assigned to the project successfully
		company.assign(worker1, project);
		company.assign(worker2, project);
		assert(worker1.getProjects().contains(project) && worker2.getProjects().contains(project));
		assert(project.getWorkers().contains(worker1) && project.getWorkers().contains(worker2));
		assert(company.getAssignedWorkers().size() == 2);
		
		//set project to ACTIVE
		company.start(project);
		assert(project.getStatus().equals(ProjectStatus.ACTIVE));

		//worker 1 should still be working on project
		//worker 2 should be removed from assigned pool, and from project
		company.unassign(worker2, project);
		assert(project.getStatus().equals(ProjectStatus.SUSPENDED));
		assert(project.getWorkers().size() == 1);
		assert(company.getAssignedWorkers().size() == 1);
		assert(company.getAvailableWorkers().size() == 2);
		assert(worker2.getProjects().size() == 0);
		assert(worker1.getProjects().size() == 1);	
	}

	@Test (expected = IllegalArgumentException.class)
	public void testUnassign_workerNotPreviouslyAssignedToProject() {
		Company company = new Company("Company");

		for (int i = 0; i < 30; i++) {
			company.createQualification(String.format("Qualification #%d", i));
		}

		Project project = new Project("Project", validQualifications, ProjectSize.SMALL);
		Worker worker1 = new Worker("Worker 1", validQualifications, 1000);

		company.unassign(worker1, project);
	}

	//worker who fulfills all project requirements is assigned to multiple projects should not be removed from assigned pool
	//all worker's projects are active
	//thee project the worker is being removed from should turn to suspended
	@Test
	public void testUnassign_workerHasMoreThanOneProjectsAssigned() {
		Company company = new Company("Company");

		for (int i = 0; i < 30; i++) {
			company.createQualification(String.format("Qualification #%d", i));
		}

		Project p1 = new Project("P1", validQualifications, ProjectSize.BIG);
		Project p2 = new Project("P2", validQualifications, ProjectSize.BIG);
		Project p3 = new Project("P3", validQualifications, ProjectSize.BIG);

		Worker w = new Worker("W", validQualifications, 1000);

		company.createProject("P1", validQualifications, ProjectSize.BIG);
		company.createProject("P2", validQualifications, ProjectSize.BIG);
		company.createProject("P3", validQualifications, ProjectSize.BIG);
		company.createWorker("W", validQualifications, 1000);

		company.assign(w, p1);
		company.assign(w, p2);
		company.assign(w, p3);

		company.start(p1);
		company.start(p2);
		company.start(p3);

		assert(w.getProjects().size() == 3);

		company.unassign(w, p3);

		//worker should still be in assigned pool
		//worker should still have 2 more projects assigned to them
		//p3 status should be SUSPENDED
		assert(p3.getStatus().equals(ProjectStatus.SUSPENDED));
		assert(company.getAssignedWorkers().size() == 1);
		assert(company.getAvailableWorkers().size() == 1);
		assert(w.getProjects().size() == 2);
		assert(w.getProjects().contains(p1) && w.getProjects().contains(p2));	
	}

	//Unassigning worker from SUSPENDED project doest not change ProjectStatus
	@Test
	public void testUnassign_projectStatusIsSuspended() {
		Company company = new Company("Company");

		for (int i = 0; i < 30; i++) {
			company.createQualification(String.format("Qualification #%d", i));
		}

		Project p1 = new Project("P1", validQualifications, ProjectSize.BIG);
		Worker w = new Worker("W", validQualifications, 1000);

		company.createProject("P1", validQualifications, ProjectSize.BIG);
		company.createWorker("W", validQualifications, 1000);

		p1.setStatus(ProjectStatus.SUSPENDED);

		company.assign(w, p1);
		company.unassign(w, p1);

		assert(p1.getStatus().equals(ProjectStatus.SUSPENDED));
		assert(company.getAssignedWorkers().size() == 0);
	}

	@Test
	public void testUnassign_projectStatusIsPlanned() {
		Company company = new Company("Company");

		for (int i = 0; i < 30; i++) {
			company.createQualification(String.format("Qualification #%d", i));
		}

		Project p1 = new Project("P1", validQualifications, ProjectSize.BIG);
		Worker w = new Worker("W", validQualifications, 1000);

		company.createProject("P1", validQualifications, ProjectSize.BIG);
		company.createWorker("W", validQualifications, 1000);

		p1.setStatus(ProjectStatus.PLANNED);

		company.assign(w, p1);
		company.unassign(w, p1);

		assert(p1.getStatus().equals(ProjectStatus.PLANNED));
		assert(company.getAssignedWorkers().size() == 0);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testUnassign_ProjectNotInCompany() {

		Company company = new Company("Company");

		for (int i = 0; i < 30; i++) {
			company.createQualification(String.format("Qualification #%d", i));
		}

		Project p1 = company.createProject("Project in company", validQualifications, ProjectSize.BIG);
		Project p2 = new Project("Project not in company", validQualifications, ProjectSize.BIG);
		Worker worker = company.createWorker("W", validQualifications, 1000);

		//NOTE: this way the worker is in the assigned pool for company
		company.assign(worker, p1);

		worker.addProject(p2);
		p2.addWorker(worker);

		company.unassign(worker, p2);
	}


	@Test (expected = IllegalArgumentException.class)
	public void testUnassign_unassignFinishedProject() {
		Company company = new Company("Company");

		for (int i = 0; i < 30; i++) {
			company.createQualification(String.format("Qualification #%d", i));
		}

		Project p1 = new Project("P1", validQualifications, ProjectSize.BIG);
		Worker w = new Worker("W", validQualifications, 1000);

		company.createProject("P1", validQualifications, ProjectSize.BIG);
		company.createWorker("W", validQualifications, 1000);
		p1.setStatus(ProjectStatus.FINISHED);
		company.unassign(w, p1);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testUnassign_workerNotInCompanyAssignedSet() {
		Company company = new Company("Company");

		for (int i = 0; i < 30; i++) {
			company.createQualification(String.format("Qualification #%d", i));
		}

		Project p1 = new Project("P1", validQualifications, ProjectSize.BIG);
		Worker w = new Worker("W", validQualifications, 1000);

		company.createProject("P1", validQualifications, ProjectSize.BIG);
		company.createWorker("W", validQualifications, 1000);

		assert(company.getAssignedWorkers().size() == 0);
		company.unassign(w, p1);

	}


	/************************************************************************************* */
	//Test getAssignedWorkers()

	@Test
	public void GetAssigned_WithOneWorkerAssigned(){
		assert(testCompanyWithWorkersAndQualifications.getAssignedWorkers().size() == 0);
		Worker one = new Worker(String.format("Worker #%d",1),validQualifications,100000);
		Project controlProject = testCompanyWithWorkersAndQualifications.createProject("Testing Project 1: ", validQualifications, ProjectSize.BIG);
		testCompanyWithWorkersAndQualifications.assign(one, controlProject);
		assert(testCompanyWithWorkersAndQualifications.getAssignedWorkers().size() == 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void GetAssigned_WorkersWithWorkerAssigned(){
		assert(testCompanyWithWorkersAndQualifications.getAssignedWorkers().size() == 0);
		Worker one = new Worker(String.format("Worker #%d",1),validQualifications,100000);
		Worker two = new Worker(String.format("Worker #%d",2),validQualifications,100000);
		Worker three = new Worker(String.format("Worker #%d",3),validQualifications,100000);
		Worker four = new Worker(String.format("Worker #%d",4),validQualifications,100000);
		Project controlProject = testCompanyWithWorkersAndQualifications.createProject("Testing Project 1: ", validQualifications, ProjectSize.BIG);
		testCompanyWithWorkersAndQualifications.assign(one, controlProject);
		//Should fail @ two, three, and four because there are no missing Qualifications
		testCompanyWithWorkersAndQualifications.assign(two, controlProject);
		testCompanyWithWorkersAndQualifications.assign(three, controlProject);
		testCompanyWithWorkersAndQualifications.assign(four, controlProject);
		assert(testCompanyWithWorkersAndQualifications.getAssignedWorkers().size() == 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void GetAssigned_WorkersWithNoWorkerAssigned(){
		assert(testCompanyWithWorkersAndQualifications.getAssignedWorkers().size() == 0);
		Set<Qualification> opposite = new HashSet<Qualification>();
		opposite.add(new Qualification("Not in project"));
		Worker one = new Worker(String.format("Worker #%d",1),opposite,100000);
		Worker two = new Worker(String.format("Worker #%d",2),opposite,100000);
		Worker three = new Worker(String.format("Worker #%d",3),opposite,100000);
		Worker four = new Worker(String.format("Worker #%d",4),opposite,100000);
		Project controlProject = testCompanyWithWorkersAndQualifications.createProject("Testing Project 1: ", validQualifications, ProjectSize.BIG);
		//Should fail @ one, two, three, and four since no worker can be assigned to a project (no valid quialifications)
		testCompanyWithWorkersAndQualifications.assign(one, controlProject);
		testCompanyWithWorkersAndQualifications.assign(two, controlProject);
		testCompanyWithWorkersAndQualifications.assign(three, controlProject);
		testCompanyWithWorkersAndQualifications.assign(four, controlProject);
		assert(testCompanyWithWorkersAndQualifications.getAssignedWorkers().size() == 0);
	}


	/************************************************************************************* */
	//Test getUnassignedWorkers()

	@Test
	public void getUnassignedWorkers_noWorkersAssigned() {
		Company company = new Company("Company");

		for (int i = 0; i < 30; i++) {
			company.createQualification(String.format("Qualification #%d", i));
		}	

		Worker w1 = new Worker("Worker 1", validQualifications, 1000);
		Worker w2 = new Worker("Worker 2", validQualifications, 1000);
		Worker w3 = new Worker("Worker 3", validQualifications, 1000);


		company.createWorker("Worker 1", validQualifications, 1000);
		company.createWorker("Worker 2", validQualifications, 1000);
		company.createWorker("Worker 3", validQualifications, 1000);

		Set<Worker> expected = new HashSet<>();
		expected.add(w1);
		expected.add(w2);
		expected.add(w3);

		assert(company.getUnassignedWorkers().equals(expected));
	}

	@Test
	public void getUnassignedWorkers_emptySet() {
		Company company = new Company("Company");

		for (int i = 0; i < 30; i++) {
			company.createQualification(String.format("Qualification #%d", i));
		}	

		assert(company.getUnassignedWorkers().isEmpty());
	}

	@Test
	public void getUnassignedWorkers_allWorkersAssigned() {
		Set<Qualification> w1_qSet = new HashSet<>();
		Set<Qualification> w2_qSet = new HashSet<>();
		Set<Qualification> w3_qSet = new HashSet<>();

		for (int i = 0; i < 10; i++) {
			if (i < 3) {
				w1_qSet.add(new Qualification(String.format("Qualification #%d", i)));
			} else if (i >= 3 && i < 6) {
				w2_qSet.add(new Qualification(String.format("Qualification #%d", i)));
			} else {
				w3_qSet.add(new Qualification(String.format("Qualification #%d", i)));
			}
		}

		assert(w1_qSet.size() == 3);
		assert(w2_qSet.size() == 3);
		assert(w3_qSet.size() == 4);

		Company company = new Company("Company");

		for (int i = 0; i < 30; i++) {
			company.createQualification(String.format("Qualification #%d", i));
		}	

		Worker w1 = new Worker("Worker 1", w1_qSet, 1000);
		Worker w2 = new Worker("Worker 2", w2_qSet, 1000);
		Worker w3 = new Worker("Worker 3", w3_qSet, 1000);
		Project project = new Project("Project 1", validQualifications, ProjectSize.BIG);

		company.createWorker("Worker 1", w1_qSet, 1000);
		company.createWorker("Worker 2", w2_qSet, 1000);
		company.createWorker("Worker 3", w3_qSet, 1000);
		company.createProject("Project 1", validQualifications, ProjectSize.BIG);

		assert(company.getEmployedWorkers().size() == 3);
		assert(company.getProjects().size() == 1);

		project.setStatus(ProjectStatus.SUSPENDED);
		assert(project.getStatus() == ProjectStatus.SUSPENDED);

		company.assign(w1, project);
		company.assign(w2, project);
		company.assign(w3, project);

		assert(company.getUnassignedWorkers().isEmpty());
	}

	@Test
	public void getUnassignedWorkers_hasUnassignedWorkers() {
		Set<Qualification> w1_qSet = new HashSet<>();
		Set<Qualification> w2_qSet = new HashSet<>();
		Set<Qualification> w3_qSet = new HashSet<>();

		for (int i = 0; i < 10; i++) {
			if (i < 3) {
				w1_qSet.add(new Qualification(String.format("Qualification #%d", i)));
			} else if (i >= 3 && i < 6) {
				w2_qSet.add(new Qualification(String.format("Qualification #%d", i)));
			} else {
				w3_qSet.add(new Qualification(String.format("Qualification #%d", i)));
			}
		}

		assert(w1_qSet.size() == 3);
		assert(w2_qSet.size() == 3);
		assert(w3_qSet.size() == 4);

		Company company = new Company("Company");

		for (int i = 0; i < 30; i++) {
			company.createQualification(String.format("Qualification #%d", i));
		}	

		Worker w1 = new Worker("Worker 1", w1_qSet, 1000);
		Worker w2 = new Worker("Worker 2", w2_qSet, 1000);
		Worker w3 = new Worker("Worker 3", w3_qSet, 1000);
		Project project = new Project("Project 1", validQualifications, ProjectSize.BIG);

		company.createWorker("Worker 1", w1_qSet, 1000);
		company.createWorker("Worker 2", w2_qSet, 1000);
		company.createWorker("Worker 3", w3_qSet, 1000);
		company.createProject("Project 1", validQualifications, ProjectSize.BIG);

		assert(company.getEmployedWorkers().size() == 3);
		assert(company.getProjects().size() == 1);

		project.setStatus(ProjectStatus.SUSPENDED);
		assert(project.getStatus() == ProjectStatus.SUSPENDED);

		company.assign(w1, project);
		company.assign(w2, project);

		assert(company.getUnassignedWorkers().size() == 1);
		assert(company.getUnassignedWorkers().contains(w3));
	}



	/************************************************************************************* */
	//Test finish()

	@Test
	public void finish_ActiveProject() {
		Worker Jeff = new Worker("Worker #1", validQualifications, 100000);
		Project test = testCompanyWithWorkersAndQualifications.createProject("test", validQualifications, ProjectSize.BIG);
		testCompanyWithWorkersAndQualifications.assign(Jeff, test);
		test.setStatus(ProjectStatus.ACTIVE);
		testCompanyWithWorkersAndQualifications.finish(test);
		assert(test.getStatus().equals(ProjectStatus.FINISHED));
		assert(test.getWorkers().equals(Collections.emptySet()));
	}

	@Test
	public void finish_PlannedProject() {
		Worker Jeff = new Worker("Worker #1", validQualifications, 100000);
		Project test = testCompanyWithWorkersAndQualifications.createProject("test", validQualifications, ProjectSize.BIG);
		testCompanyWithWorkersAndQualifications.assign(Jeff, test);
		test.setStatus(ProjectStatus.PLANNED);
		testCompanyWithWorkersAndQualifications.finish(test);
		assert(test.getStatus().equals(ProjectStatus.PLANNED));
	}

	@Test
	public void finish_SuspendedProject() {
		Worker Jeff = new Worker("Worker #1", validQualifications, 100000);
		Project test = testCompanyWithWorkersAndQualifications.createProject("test", validQualifications, ProjectSize.BIG);
		testCompanyWithWorkersAndQualifications.assign(Jeff, test);
		test.setStatus(ProjectStatus.SUSPENDED);
		testCompanyWithWorkersAndQualifications.finish(test);
		assert(test.getStatus().equals(ProjectStatus.SUSPENDED));
	}

	@Test(expected = NullPointerException.class)
	public void finish_nullProjectPassed() {
		testCompanyWithWorkersAndQualifications.finish(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void finish_CompanyProjectSetIsEmpty(){
		Project test =new Project("Project Set is Empty", validQualifications, ProjectSize.MEDIUM);
		testCompanyWithWorkersAndQualifications.finish(test);
	}

	@Test(expected = IllegalArgumentException.class)
	public void finish_ProjectNotInCompanyProjectSet() {
		Project test =new Project("Project not in the company", validQualifications,ProjectSize.MEDIUM);

		for (int i = 0; i < 5; i++) {
			testCompanyWithWorkersAndQualifications.createProject("Project " + i, validQualifications, ProjectSize.MEDIUM);
		}
		assert(testCompanyWithWorkersAndQualifications.getProjects().size() == 5);

		testCompanyWithWorkersAndQualifications.start(test);
	}

	/************************************************************************************* */
	//test UnassignAll

	@Test (expected = IllegalArgumentException.class)
	public void unassignAll_nullWorker() {
		Company company = new Company("Company");

		for (int i = 0; i < 30; i++) {
			company.createQualification(String.format("Qualification #%d", i));
		}	

		Worker null_worker = null;
		company.unassignAll(null_worker);
	}

	@Test
	public void testUnassignAll_unassignWorkerFromThreeProjects() {
		Company company = new Company("Company");

		for (int i = 0; i < 30; i++) {
			company.createQualification(String.format("Qualification #%d", i));
		}	

		Worker w1 = new Worker("Worker 1", validQualifications, 1000);
		Project p1 = new Project("Project 1", validQualifications, ProjectSize.BIG);
		Project p2 = new Project("Project 2", validQualifications, ProjectSize.BIG);
		Project p3 = new Project("Project 3", validQualifications, ProjectSize.BIG);

		company.createProject("Project 1", validQualifications, ProjectSize.BIG);
		company.createProject("Project 2", validQualifications, ProjectSize.BIG);
		company.createProject("Project 3", validQualifications, ProjectSize.BIG);
		company.createWorker("Worker 1", validQualifications, 1000);

		company.assign(w1, p1);
		company.start(p1);
		company.assign(w1, p2);
		company.assign(w1, p3);

		company.unassignAll(w1);

		assert(!company.getAssignedWorkers().contains(w1));
		assert(w1.getProjects().size() == 0);
		assert(p1.getStatus() == ProjectStatus.SUSPENDED);
	}

	@Test
	public void testUnassignAll_workerHasNoProjects_doNothing() {
		Company company = new Company("Company");

		for (int i = 0; i < 30; i++) {
			company.createQualification(String.format("Qualification #%d", i));
		}	

		Worker w1 = new Worker("Worker 1", validQualifications, 1000);
		company.createWorker("Worker1", validQualifications, 1000);

		company.unassignAll(w1);
		assert(company.getAssignedWorkers().size() == 0);
		assert(w1.getProjects().size() == 0);
		assert(company.getEmployedWorkers().size() == 1);
	}
}
