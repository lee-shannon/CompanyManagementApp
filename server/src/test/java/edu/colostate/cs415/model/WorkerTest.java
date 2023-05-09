package edu.colostate.cs415.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import edu.colostate.cs415.dto.WorkerDTO;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.util.*;

import java.util.Collections;
import java.util.HashSet;

@RunWith(JUnitParamsRunner.class)
public class WorkerTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    //Ready to be used variables for testing
    private String[] workerNames, qualificationDescriptions, projectNames, emptyStrings;
    
    private double salary, zeroSalary, negativeSalary;
    private Double nullSalary;
    
    private Qualification[] multipleQualificationsArray;
    private Qualification qualification;
    private Set<Qualification> qualificationSetWithOneQualification;
    private Set<Qualification> qualificationSetWithMultipleQualifications;
    private Set<Qualification> emptyQualificationSet;
    private Set<Qualification> nullQualificationSet;
    
    private Qualification qualificationWithWorker;
    private Qualification qualificationWithMultipleWorkers; //All workers in this qualifications only have the one qualification
    
    private Worker WorkerWithOneQualification;
    private Worker WorkerWithMultipleQualifications;
    
    //Project Size is .SMALL for all projects
    private Project projectWithQualification;
    private Project projectWithMultipleQualifications;
    private Project[] differentProjectsSameQualifications; // Same qualifications, different names as above project vars

    @Before
    public void populateTestWorkerBeforeSectionWithVariablesAndObjects(){
        /*Initialized Variables and Arrays of objects from above*/
        String[] workerNamesInitialization = {"Test", "Test 2", "Test 3", "David", "Marie"};
        String[] qualificationDescriptionsInitialization = {"Angular", "Cyber Security", "Java", "Javascript", "MongoDB"};
        String[] projectNamesInitialization = {"Test", "Test 2", "Test 3", "Website", "Application"};
        String[] emptyStringsInitialization = {"", " ", "      "};
        workerNames = workerNamesInitialization;
        qualificationDescriptions = qualificationDescriptionsInitialization;
        projectNames = projectNamesInitialization;
        emptyStrings = emptyStringsInitialization;
        salary = 100000;
        zeroSalary = 0;
        negativeSalary = -100000;
        nullSalary = null;
        Qualification[] multipleQualificationsArrayInitialization = {new Qualification(qualificationDescriptions[0]), new Qualification(qualificationDescriptions[1]), new Qualification(qualificationDescriptions[2])};
        multipleQualificationsArray = multipleQualificationsArrayInitialization;
         
        //Objects **variables/objects with "multiple" in the name are referring to exactly three for consistency**
        qualification = multipleQualificationsArrayInitialization[0];
        
        qualificationSetWithOneQualification = new HashSet<>();
            qualificationSetWithOneQualification.add(qualification);

        qualificationSetWithMultipleQualifications = new HashSet<>();
            qualificationSetWithMultipleQualifications.add(multipleQualificationsArrayInitialization[0]);
            qualificationSetWithMultipleQualifications.add(multipleQualificationsArrayInitialization[1]);
            qualificationSetWithMultipleQualifications.add(multipleQualificationsArrayInitialization[2]);

        emptyQualificationSet = new HashSet<>();

        nullQualificationSet = null;

        qualificationWithWorker = new Qualification(qualificationDescriptions[0]);
            qualificationWithWorker.addWorker(new Worker(workerNames[0], qualificationSetWithOneQualification, salary));

        qualificationWithMultipleWorkers = new Qualification(qualificationDescriptions[0]);
            qualificationWithMultipleWorkers.addWorker(new Worker(workerNames[0], qualificationSetWithOneQualification, salary));
            qualificationWithMultipleWorkers.addWorker(new Worker(workerNames[1], qualificationSetWithOneQualification, salary));
            qualificationWithMultipleWorkers.addWorker(new Worker(workerNames[2], qualificationSetWithOneQualification, salary));


        WorkerWithOneQualification = new Worker(workerNames[0], qualificationSetWithOneQualification, salary);
        WorkerWithMultipleQualifications = new Worker(workerNames[0], qualificationSetWithMultipleQualifications, salary);
            
        //ProjectSize.SMALL for all projects
        projectWithQualification = new Project(projectNames[0], qualificationSetWithOneQualification, ProjectSize.SMALL );
        projectWithMultipleQualifications = new Project(projectNames[0], qualificationSetWithMultipleQualifications, ProjectSize.SMALL);
        Project[] differentProjectsInitialization = {new Project(projectNames[1], qualificationSetWithMultipleQualifications, ProjectSize.SMALL), new Project(projectNames[2], qualificationSetWithMultipleQualifications, ProjectSize.SMALL), new Project(projectNames[3], qualificationSetWithMultipleQualifications, ProjectSize.SMALL)};
        differentProjectsSameQualifications = differentProjectsInitialization;
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

/****************************************************************************************************************/
    // testingConstructor
    @Test
    public void testConstructor_OneQualificationAndGoodData(){
        assert(WorkerWithOneQualification.getName().equals(workerNames[0]));
        assert(WorkerWithOneQualification.getSalary() == salary);
        assert(WorkerWithOneQualification.getQualifications().size() == 1);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullQualifications(){
        Worker newWorker = new Worker("NO Qs", null, 10000);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testingConstructor_illegalQs() {
        Worker illegalQs = new Worker("Bad Q", emptyQualificationSet, 1000);
    }

    @Test
    public void testConstructor_MultipleQualificationsAndGoodData(){
        assert(WorkerWithMultipleQualifications.getName().equals(workerNames[0]));
        assert(WorkerWithMultipleQualifications.getSalary() == salary);
        assert(WorkerWithMultipleQualifications.getQualifications().size() == 3);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testConstructor_emptyStringNames1() {
        Worker illegalName = new Worker(emptyStrings[0], qualificationSetWithMultipleQualifications, salary);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testConstructor_emptyStringNames2() {
        Worker illegalName = new Worker(emptyStrings[1], qualificationSetWithMultipleQualifications, salary);

    }

    @Test (expected = IllegalArgumentException.class) 
    public void testConstructor_emptyStringNames3() {
        Worker illegalName = new Worker(emptyStrings[2], qualificationSetWithMultipleQualifications, salary);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testConstructor_noQualifications(){
        Worker workerWithNoQualifications = new Worker(workerNames[0], emptyQualificationSet, salary);
    }

    @Test
    public void testConstructor_QualificationSetShouldNotChange(){
        Set<Qualification> qtestsetcopy = createQSet();
        Worker testWorker = new Worker("Valid Qs",qtestsetcopy,10000);
        clearQSet(qtestsetcopy);
        assert(testWorker.getQualifications().size() == 1);
    }

/****************************************************************************************************************/

/****************************************************************************************************************/
    //testingGetName

    @Test // worker constructor handles incorrect names
    public void getName_returnsProperName(){
        for(String workerName : workerNames){
            Worker workerWithMultipleQualifications = new Worker(workerName, qualificationSetWithMultipleQualifications, salary);
            Worker workerWithOneQualification = new Worker(workerName, qualificationSetWithOneQualification,salary);
            assert(workerWithMultipleQualifications.getName() == workerName);
            assert(workerWithOneQualification.getName() == workerName);
        }
    }

/****************************************************************************************************************/

/****************************************************************************************************************/
    //testingGetSalary

    @Test
    public void GetSalary_standardSalaryAndOneQualification(){
        Worker workerGetSalaryTest = new Worker(workerNames[0], qualificationSetWithOneQualification, salary);
        assert(workerGetSalaryTest.getSalary() == salary);
    }

    @Test
    public void GetSalary_standardSalaryAndMultipleQualifications(){
        Worker workerGetSalaryTest = new Worker(workerNames[0], qualificationSetWithMultipleQualifications, salary);
        assert(workerGetSalaryTest.getSalary() == salary);
    }

/****************************************************************************************************************/
 
/****************************************************************************************************************/
    //testingSetSalary

    @Test
    public void SetSalary_workerWithOneQualification_TestValidSalary(){
        WorkerWithOneQualification.setSalary(10);
        assert(WorkerWithOneQualification.getSalary() == 10);
    }

    @Test
    public void SetSalary_workerWithMultipleQualifications_TestValidSalary(){
        WorkerWithMultipleQualifications.setSalary(10);
        assert(WorkerWithMultipleQualifications.getSalary() == 10);
    }

    @Test (expected = IllegalArgumentException.class)
    public void SetSalary_workerWithOneQualification_TestNegativeSalary(){
        WorkerWithOneQualification.setSalary(negativeSalary);
    }

    @Test (expected = IllegalArgumentException.class)
    public void SetSalary_workerWithMultipleQualifications_TestNegativeSalary(){
        WorkerWithMultipleQualifications.setSalary(negativeSalary);
    }

    //TODO: determine whether or not a worker can have a zero salary. For now, they can.
    @Test
    public void SetSalary_workerWithOneQualification_TestZeroSalary(){
        WorkerWithOneQualification.setSalary(zeroSalary);
        assert(WorkerWithOneQualification.getSalary() == zeroSalary);
    }

    @Test
    public void SetSalary_workerWithMultipleQualifications_TestZeroSalary(){
        WorkerWithMultipleQualifications.setSalary(zeroSalary);
        assert(WorkerWithMultipleQualifications.getSalary() == zeroSalary);
    }

/****************************************************************************************************************/

/****************************************************************************************************************/
    //testingGetQualifications
    @Test
    public void GetQualifications_workerWithOneQualification(){
        assert(WorkerWithOneQualification.getQualifications().equals(qualificationSetWithOneQualification));
        assert(WorkerWithOneQualification.getQualifications().size() == 1);
    }

    @Test
    public void GetQualifications_multipleQualifications(){
        assert(WorkerWithMultipleQualifications.getQualifications().equals(qualificationSetWithMultipleQualifications));
        assert(WorkerWithMultipleQualifications.getQualifications().size() == 3);
    }
   
    @Test(expected = IllegalArgumentException.class)
    public void GetQualifications_EmptySet(){
        Worker workerWithEmptyQualificationsSet = new Worker("Worker with empty qualifications set", new HashSet<Qualification>(), salary);
        assert(workerWithEmptyQualificationsSet.getQualifications().isEmpty()) ;
    }

    @Test(expected = NullPointerException.class)
    public void GetQualifications_QualificationsSetIsNull(){
        Worker workerWithNullQualificationSet = new Worker("Worker with null qualification set", nullQualificationSet, salary);
        workerWithNullQualificationSet.getQualifications();
        Assert.assertFalse("Worker is holding a null Qualification set", true);
    }

/****************************************************************************************************************/
    //test getQualifications doesnt access original set or modiy it

    @Test(expected = Exception.class)
    public void getQualifications_QualificationAttributeNotModified() {
        Set<Qualification> qualifications = createQSet();
        Worker worker = new Worker("Worker", qualifications, 100);
        assert(worker.getQualifications().size() == 1);
        //worker.getQualifications() returns a new set unrelated to the original set associated with worker
        worker.getQualifications().remove(new Qualification("Public Speaking"));
        assert(worker.getQualifications().size() == 1);
    }
/****************************************************************************************************************/
    //testingAddQualification
    @Test
    public void testingAddQualification(){
        Worker worker = CreateAndPass10QualificationsToWorker();
        add10AdditionalDuplicateCopyQualifications(worker);
        assert(worker.getQualifications().size() == 10);

        add10DifferentQualificationsToWorker(worker);
        assert(worker.getQualifications().size() == 20);        
    }

    public Worker CreateAndPass10QualificationsToWorker(){
        for(int testNumber = 0; testNumber < 10; testNumber++){
            Qualification newQualification = new Qualification(String.format("Test number: %d", testNumber));
            emptyQualificationSet.add(newQualification);           
        }
        return new Worker(workerNames[0], emptyQualificationSet, salary);
    }
    public void add10AdditionalDuplicateCopyQualifications(Worker worker){
        for(int testNumber = 0; testNumber < 10; testNumber++){
            Qualification copyQualification = new Qualification(String.format("Test number: %d", testNumber));
            worker.addQualification(copyQualification);           
        } 
    }
    public void add10DifferentQualificationsToWorker(Worker worker){
        for(int testNumber = 0; testNumber < 10; testNumber++){
            Qualification differnetQualification = new Qualification(String.format("Test copy number: %d", testNumber));
            worker.addQualification(differnetQualification);           
        }  
    }
    
    @Test(expected = NullPointerException.class)
    public void testingAddQualifications_nullQualificationDescription(){
        Qualification qualificationWithNullDescription = new Qualification(null);
        
        WorkerWithOneQualification.addQualification(qualificationWithNullDescription);
        WorkerWithMultipleQualifications.addQualification(qualificationWithNullDescription);
    }

    @Test(expected = NullPointerException.class)
    public void testingAddQualifications_nullQualification(){
        WorkerWithOneQualification.addQualification(null);
        WorkerWithMultipleQualifications.addQualification(null);
    }
    
/****************************************************************************************************************/
    
/****************************************************************************************************************/
    //TestingHashCode

    @Test //same name, diff qualifications, diff salaries
    public void testHashCodeForSameValue() {
        Worker worker1 = new Worker("Emily", qualificationSetWithMultipleQualifications, salary);
        Worker worker2 = new Worker("Emily", qualificationSetWithOneQualification, zeroSalary);

        assert(worker1.hashCode() == worker2.hashCode());
    }

    @Test //different names, same qualifications, same salaries
    public void testHashCodeForDiffValue() {
        Worker worker1 = new Worker("Joe", createQSet(), salary);
        Worker worker2 = new Worker("Emily", createQSet(), salary);

        assert(worker1.hashCode() != worker2.hashCode());
    }

/****************************************************************************************************************/

/****************************************************************************************************************/
    //testingEquals
    @Test
    @Parameters(method = "VariableCharacteristics_WorkerObject")
    public void Equals_WorkerObjectVariable(Object testValue, boolean expectedValue) {
        Worker workerTest= new Worker("John", createQSet(),40000);
        assert (workerTest.equals(testValue)== expectedValue);
    }

    public Object[] VariableCharacteristics_WorkerObject() {
        return new Object[] {
                new Object[] { new Worker("John", createQSet(),40000), true},
                new Object[] { null, false },
                new Object[] { new Qualification("description"), false}
        };
    }

    @Test
    public void Equals_againstSameObject() {
        assert(WorkerWithOneQualification.equals(WorkerWithOneQualification));
        assert(WorkerWithMultipleQualifications.equals(WorkerWithMultipleQualifications));
    }

    @Test
    @Parameters(method = "VariableCharacteristics_CompareName")
    public void Equals_NameVariable(Object testValue, boolean expectedValue) {
        Worker workerTest= new Worker("John", createQSet(),40000);
        assert (workerTest.equals(testValue)== expectedValue);
    }

    private Object[] VariableCharacteristics_CompareName() {
        return new Object[] {
                new Object[] { new Worker("John", createQSet(),40000), true},
                new Object[] { new Worker("Sam", createQSet(),30), false},
        };
    }

/****************************************************************************************************************/ 
	
/****************************************************************************************************************/
    //testGetProjects

    @Test
	public void getProjects_AddTwoProjectsToWorker(){
        assert(WorkerWithOneQualification.getProjects().isEmpty());
        assert(WorkerWithMultipleQualifications.getProjects().isEmpty());
		
        WorkerWithOneQualification.addProject(projectWithQualification);
        WorkerWithOneQualification.addProject(projectWithMultipleQualifications);
        WorkerWithMultipleQualifications.addProject(projectWithQualification);
        WorkerWithMultipleQualifications.addProject(projectWithMultipleQualifications);
        //Same name, so they should be the same project
        assert(WorkerWithOneQualification.getProjects().size() == 1);
        assert(WorkerWithMultipleQualifications.getProjects().size() == 1);

        //Adding project with different name but same qualifications (so different projects, technically)
        WorkerWithOneQualification.addProject(differentProjectsSameQualifications[0]);
        WorkerWithMultipleQualifications.addProject(differentProjectsSameQualifications[0]);
        assert(WorkerWithOneQualification.getProjects().size() == 2);
        assert(WorkerWithMultipleQualifications.getProjects().size() == 2);
	}
/****************************************************************************************************************/ 

/****************************************************************************************************************/ 
    //testingAddProject

    @Test(expected = NullPointerException.class)
    public void addProject_TestAddNullProjectObject() {
        WorkerWithOneQualification.addProject(null);
    }

    @Test
    @Parameters(method = "addProject_ValidProjectObjects")
    public void AddProject_ValidProjectObjects(Object testVal, boolean expectedVal) {
        Worker testWorker = new Worker("Shaun", createQSet(), 10000);
        Project CONTROL = new Project("Project 1", createQSet(), ProjectSize.MEDIUM);
        testWorker.addProject(CONTROL);

        if (testVal.getClass() == Project.class) {
            Project testProject = (Project)testVal;
            //test with duplicate project object 
            if (testProject.getName().equals("Project 1")) {
                testWorker.addProject(testProject);
                assert (testWorker.getProjects().size() == 1);
            }
            //test with unique project objects
            testWorker.addProject(testProject);
            assert(testWorker.getProjects().contains(testProject) == expectedVal);
        }
    }
   
    private Object[] addProject_ValidProjectObjects() {
        return new Object[] {
            new Object[] { new Project("Project 1", createQSet(), ProjectSize.SMALL), true },
            new Object[] { new Project("Project 2", createQSet(), ProjectSize.MEDIUM), true },
            new Object[] { new Project("PROJECT 3", createQSet(), ProjectSize.MEDIUM), true },
        };
    }

/****************************************************************************************************************/

/****************************************************************************************************************/
    //testingRemoveProject
    @Test
	public void RemoveProject_removeProjects(){
		WorkerWithOneQualification.addProject(projectWithQualification);
        WorkerWithMultipleQualifications.addProject(projectWithMultipleQualifications);
        assert(WorkerWithOneQualification.getProjects().size() == 1);
        assert(WorkerWithMultipleQualifications.getProjects().size() == 1);

        WorkerWithOneQualification.removeProject(projectWithQualification);
        WorkerWithMultipleQualifications.removeProject(projectWithMultipleQualifications);
        assert(WorkerWithOneQualification.getProjects().isEmpty());
        assert(WorkerWithMultipleQualifications.getProjects().isEmpty());
	}
    @Test
    public void removeProjects_ProjectNotInWorkerSet(){
        Project projectInWorker = projectWithQualification;
        Project projectNotInWorker = differentProjectsSameQualifications[0];
        
        WorkerWithOneQualification.addProject(projectInWorker);
        WorkerWithOneQualification.removeProject(projectNotInWorker);
        assert(WorkerWithOneQualification.getProjects().size() == 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeProjects_ProjectIsNull(){
        WorkerWithOneQualification.addProject(projectWithQualification);
        WorkerWithMultipleQualifications.addProject(projectWithMultipleQualifications);

        WorkerWithOneQualification.removeProject(null);
        WorkerWithMultipleQualifications.removeProject(null);
        assert(WorkerWithOneQualification.getProjects().size() == 1);
        assert(WorkerWithMultipleQualifications.getProjects().size() == 1);
    }

/****************************************************************************************************************/

/****************************************************************************************************************/
    //testingGetWorkLoad

    @Test
	public void WorkloadTest(){
		Worker testWorker = new Worker("John", createQSet(),40000);
		assert (testWorker.getWorkload() == 0);

		populateProjectsHelper(ProjectSize.BIG, 4, testWorker, ProjectStatus.FINISHED);
		assert (testWorker.getWorkload() == 0);

		populateProjectsHelper(ProjectSize.SMALL, 4, testWorker, ProjectStatus.ACTIVE);
		assert (testWorker.getWorkload() == 4);

		populateProjectsHelper(ProjectSize.MEDIUM, 2, testWorker,ProjectStatus.ACTIVE);
		assert (testWorker.getWorkload() == 8);

		populateProjectsHelper(ProjectSize.BIG,1, testWorker,ProjectStatus.ACTIVE);
		assert (testWorker.getWorkload() == 11);

        testWorker = new Worker("John", createQSet(),40000);
        populateProjectsHelper(ProjectSize.SMALL,6, testWorker,ProjectStatus.ACTIVE);
        assert (testWorker.getWorkload() == 6);
	}

/****************************************************************************************************************/

private void populateProjectsHelper(ProjectSize size, int numOfProjects, Worker worker, ProjectStatus status){
    for (int i = 0; i <numOfProjects; i++) {
        Project project = new Project(String.valueOf(Math.random()), createQSet(), size);
        project.setStatus(status);
        worker.addProject(project);
    }
}

/****************************************************************************************************************/
    //testingIsAvailable

    @Parameters({
            "0, true",
            "6, true",
            "12, false",
            "31, false" })
	@Test
	public void isAvailableTest(int workload, boolean expectedIsAvailable){
		Worker testWorker = new Worker("John", createQSet(),40000);
		populateProjectsHelper(ProjectSize.SMALL, workload, testWorker, ProjectStatus.ACTIVE);
		assert (testWorker.isAvailable() == expectedIsAvailable);
	}

/****************************************************************************************************************/    
    
/****************************************************************************************************************/
    // testingtoDTO 
    @Test
    public void toDTO_WorkerHasNoProjects(){
        int workLoad = 10;
        String[] qualificationsAsStringArray = Arrays.copyOfRange(qualificationDescriptions, 0, 3);
       
        edu.colostate.cs415.dto.WorkerDTO testWorkerDTO_noProjects = new WorkerDTO(workerNames[0], salary, workLoad, new String[1] , qualificationsAsStringArray);

        //Checking object equality (by name)
        assert(WorkerWithMultipleQualifications.toDTO().equals(testWorkerDTO_noProjects));
       
        //Checking set-to-string-Array conversion in DTO class
        String[] qualificationAsStringArray_COPY = qualificationsAsStringArray.clone();
        Arrays.sort(qualificationAsStringArray_COPY);
        String[] workerDTOQualificationsStringArray_COPY = testWorkerDTO_noProjects.getQualifications().clone();
        Arrays.sort(workerDTOQualificationsStringArray_COPY);        
        Assert.assertArrayEquals(qualificationAsStringArray_COPY, workerDTOQualificationsStringArray_COPY);

    }
    
    @Test 
    public void toDTO_Both_Projects_And_Qualifications(){
        int workLoad = 10;
        Set<Project> projects = new HashSet<Project>();
        projects.add(projectWithQualification);
        projects.add(projectWithMultipleQualifications);

        String[] qualificationsAsStringArray = Arrays.copyOfRange(qualificationDescriptions, 0, 3);
        String[] projectsAsStringArray = {projectNames[0], projectNames[1]};

        edu.colostate.cs415.dto.WorkerDTO testWorkerDTO = new WorkerDTO(workerNames[0], salary, workLoad, projectsAsStringArray, qualificationsAsStringArray);
        
        assert(WorkerWithMultipleQualifications.toDTO().equals(testWorkerDTO));
        //Checking set-to-stringArray conversion in DTO
        String[] qualificationAsStringArray_COPY = qualificationsAsStringArray.clone();
        Arrays.sort(qualificationAsStringArray_COPY);
        String[] workerDTOQualificationsStringArray_COPY = testWorkerDTO.getQualifications().clone();
        Arrays.sort(workerDTOQualificationsStringArray_COPY);        
        Assert.assertArrayEquals(qualificationAsStringArray_COPY, workerDTOQualificationsStringArray_COPY);
    }

    //Test incorrect data
    @Test
    public void toDTO_IncorrectData(){
        int workLoad = 10;
    
        Set<Project> projects = new HashSet<Project>();
        projects.add(projectWithQualification); 
        projects.add(projectWithQualification);

        String[] dtoProjects = {"FakeProject", "FakeProject2"};
        String[] dtoQualifications = {"FakeQualification1", "FakeQualification2"};
        WorkerDTO workerDTO_WithDifferentData = new WorkerDTO(workerNames[1], salary, workLoad, dtoProjects, dtoQualifications);
    
        Assert.assertNotEquals("Worker: toDTO() creating same object regardless of values", WorkerWithMultipleQualifications.toDTO(), workerDTO_WithDifferentData);
    }

    @Test
    public void toDTO_testNotNullReturn(){
        Set<Project> projects = new HashSet<Project>();
        projects.add(projectWithQualification); 
        projects.add(projectWithMultipleQualifications);

        Assert.assertNotNull(WorkerWithOneQualification.toDTO());
    }

/****************************************************************************************************************/

/****************************************************************************************************************/
    //testingToString
    @Test
    public void ToString() {
        WorkerWithOneQualification.addProject(differentProjectsSameQualifications[0]);
        WorkerWithOneQualification.addProject(differentProjectsSameQualifications[1]);

        //create qualifications + add
        Qualification newQualification1 = new Qualification("Second Qualification");
        Qualification newQualification2 = new Qualification("Third Qualification");
        Qualification newQualification3 = new Qualification("Fourth Qualificaition");

        WorkerWithOneQualification.addQualification(newQualification1);
        WorkerWithOneQualification.addQualification(newQualification2);
        WorkerWithOneQualification.addQualification(newQualification3);

        String correctResult = "Test:2:4:100000";
        //assert(WorkerWithOneQualification.toString().equals(correctResult));
    }

    @Test
    public void ToString_exampleInWriteUp() {
        Set<Qualification> testQSet = new HashSet<Qualification>();
        for(int i = 0; i < 10; i++) {
            testQSet.add(new Qualification("test" + i));
        }
        Worker nick = new Worker("Nick", testQSet,10000.20);
        Project testProject1 = new Project("TEST", createQSet(), ProjectSize.SMALL);
        Project testProject2 = new Project("TEST2", createQSet(), ProjectSize.SMALL);
        nick.addProject(testProject1);
        nick.addProject(testProject2);
        String correctResult = "Nick:2:10:10000";
        assert(nick.toString().equals(correctResult));
    }

    @Test
    public void testToStringZeroSalary() {
        Worker testWorker = new Worker("John", createQSet(),0);

        String correctResult = "John:0:1:0";
        assert(testWorker.toString().equals(correctResult));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testToStringNegativeSalary() {
        Worker testWorker = new Worker("John", createQSet(),-4000);
        String result = testWorker.toString();
    }

/****************************************************************************************************************/

/****************************************************************************************************************/
    //testingWillOverload
    @Test
    public void testWillOverload_NonUniqueProject() {
        Worker testWorker = new Worker("John", createQSet(),40000);
        populateProjectsHelper(ProjectSize.SMALL, 12, testWorker, ProjectStatus.PLANNED);

        Project project = new Project("Test Project", createQSet(), ProjectSize.SMALL);
        project.setStatus(ProjectStatus.ACTIVE);
        testWorker.addProject(project);

        assert (testWorker.willOverload(project) == false);
    }

    @Test
    public void testWillOverload_UniqueProject() {
        Worker testWorker = new Worker("John",createQSet(),40000);
        populateProjectsHelper(ProjectSize.SMALL, 7, testWorker, ProjectStatus.PLANNED);

        Project project = new Project("Test Project", createQSet(), ProjectSize.SMALL);
        project.setStatus(ProjectStatus.ACTIVE);
        assert (testWorker.willOverload(project) == false);

        //Total workload adds up to 11
        populateProjectsHelper(ProjectSize.SMALL, 5, testWorker, ProjectStatus.PLANNED);
        assert (testWorker.willOverload(project) == true);
    }

    @Test(expected = NullPointerException.class)
    public void testWillOverload_NullProject() {
        Worker testWorker = new Worker("John", createQSet(), 40000);
        testWorker.willOverload(null);
    }

    @Test
    public void testWillOverload_ProjectStatus() {
        Worker testWorker = new Worker("John", createQSet(), 40000);
        populateProjectsHelper(ProjectSize.SMALL, 12, testWorker, ProjectStatus.PLANNED);

        Project project = new Project("Test Project", createQSet(), ProjectSize.SMALL);
        project.setStatus(ProjectStatus.ACTIVE);
        assert (testWorker.willOverload(project) == true);

        project.setStatus(ProjectStatus.FINISHED);
        assert (testWorker.willOverload(project) == false);
    }

    @Parameters({
            "0, false",
            "6, false",
            "12, true",
            "31, true" })
    @Test
    public void testWillOverload_ProjectWorkload(int projectWorkload, boolean expectedResult) {
        Worker testWorker = new Worker("John", createQSet(), 40000);
        populateProjectsHelper(ProjectSize.SMALL, projectWorkload, testWorker, ProjectStatus.PLANNED);

        Project project = new Project("Test Project", createQSet(), ProjectSize.SMALL);
        project.setStatus(ProjectStatus.ACTIVE);
        assert (testWorker.willOverload(project) == expectedResult);
    }
}
