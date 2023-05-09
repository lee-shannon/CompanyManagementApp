package edu.colostate.cs415.model;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.Assert;


import edu.colostate.cs415.dto.QualificationDTO;

import org.junit.Before;
import org.junit.runner.RunWith;

import java.util.*;

@RunWith(JUnitParamsRunner.class)
public class QualificationTest {

	private Set<Qualification> createQSet() {
        Qualification q1 = new Qualification("Public Speaking");
        Set<Qualification> qset = new HashSet<>();
        qset.add(q1);
        return qset;
    }   

    private void clearQSet(Set<Qualification> qset) {
        qset.clear();
    }
	
	Set<String> descriptions = new HashSet<String>();

	@Before
	public void establishDescriptionSet(){
		// All qualification descriptions ripped from provided db
		String angular = "Angular";
        String cyberSecurity = "Cyber Security";
        String java = "Java";
        String javaScript = "JavaScript";
        String mongoDB = "MongoDB";
        String python = "Python";
        String react = "React";
        String spark = "Spark";
        String spring = "Spring";
        String sql = "Sql";
        String tensorflow = "Tensorflow";
        String typeScript = "TypeScript";
		
		descriptions.add(angular);
		descriptions.add(cyberSecurity);
		descriptions.add(java);
		descriptions.add(javaScript);
		descriptions.add(mongoDB);
		descriptions.add(python);
		descriptions.add(react);
		descriptions.add(spark);
		descriptions.add(spring);
		descriptions.add(sql);
		descriptions.add(tensorflow);
		descriptions.add(typeScript);
	}

	@Test (expected = NullPointerException.class)
	public void testConstructor_nullDescription() {
		Qualification null_test = new Qualification(null);
	}

	@Test (expected = IllegalArgumentException.class)
	public void testConstructor_illegalArgumentTest() {
		Qualification illegal_test = new Qualification("     ");
	}
  
  	@Test
	public void testConstructor1() {
		Qualification testQualification = new Qualification("This is the discription");
		assert(testQualification != null);
	}

	@Test
	public void testSimpleToString(){
		Qualification testToString = new Qualification("Has Midi-chlorians");
		assert(testToString.toString().equals("Has Midi-chlorians"));
	}
  
  	@Test
	public void testEquals1(){
		Qualification control = new Qualification("Control case");
		Qualification testcase = new Qualification("Test case");
		Qualification fakeControl = new Qualification("Control case");
		String imposter = "Control case";
		assert(control.equals(fakeControl));
		assert(!control.equals(testcase));
		assert(control.equals(control));
		assert(!control.equals(imposter));
	}

	@Test
	public void testHashCodeForSameValue() {
		int q1_hashcode;
		int q2_hashcode;

		Qualification q1 = new Qualification("Engineer");
		Qualification q2 = new Qualification("Engineer");

		q1_hashcode = q1.hashCode();
		q2_hashcode = q2.hashCode();

		assert(q1_hashcode == q2_hashcode);
	}

	@Test
	public void testHashCodeForDiffValue() {
		int q1_hashcode;
		int q2_hashcode;

		Qualification q1 = new Qualification("Engineer");
		Qualification q2 = new Qualification("Marketing");

		q1_hashcode = q1.hashCode();
		q2_hashcode = q2.hashCode();

		assert(q1_hashcode != q2_hashcode);
	}

	@Test
	public void testAddWorker() {
		Qualification q1 = new Qualification("Marketing");
        Worker w1 = new Worker("Sarah",createQSet(),90000);
		Worker w2 = new Worker("Beth",createQSet(),800);
		Worker w3 = new Worker("Hannah",createQSet(),50);
		
		q1.addWorker(w1);
		q1.addWorker(w2);
		q1.addWorker(w3);

		assert(q1.getWorkers().contains(w1));
		assert(q1.getWorkers().contains(w2));
		assert(q1.getWorkers().contains(w3));
	}

	@Test(expected = NullPointerException.class)
	public void testAddWorkerNull(){
		Qualification qualification = new Qualification("Null Worker");
		qualification.addWorker(null);
	}

	@Test
	public void testRemoveWorker() {
		Qualification q1 = new Qualification("Marketing");
		Worker w1 = new Worker("Sarah",createQSet(),90000);
		Worker w2 = new Worker("Beth",createQSet(),800);
		Worker w3 = new Worker("Hannah",createQSet(),50);

		q1.addWorker(w1);
		q1.addWorker(w2);
		q1.addWorker(w3);

		q1.removeWorker(w1);
		q1.removeWorker(w2);

		assert(q1.getWorkers().size() == 1);
		assert(!q1.getWorkers().contains(w1));
		assert(!q1.getWorkers().contains(w2));
		assert(q1.getWorkers().contains(w3));
	}

	@Test(expected = NullPointerException.class)
	public void testRemoveWorkerNull(){
		Qualification qualification = new Qualification("Null Worker");
		qualification.removeWorker(null);
	}

	@Test
	public void testConstructor(){
		for(String description : descriptions){
			Qualification newQualification = new Qualification(description);
			//Qualification objects exist
			assert(!(newQualification == null));
		}
	}
	@Test
	public void testHashCode(){
		for(String description : descriptions){
			Qualification newQualification = new Qualification(description);
			//You can't really test the hash function
			assert(newQualification.hashCode() == (int) newQualification.hashCode());
		}
	}

	@Test
	@Parameters(method = "VariableCharacteristics_QualificationObject")
	public void testEquals_WorkerObjectVariable(Object testValue, boolean expectedValue) {
		Qualification testQualification = new Qualification("Test Qualifications");
		assert (testQualification.equals(testValue)== expectedValue);
	}

	private Object[] VariableCharacteristics_QualificationObject() {
		return new Object[] {
				new Object[] { new Qualification("Test Qualifications"),true},
				new Object[] { null, false },
				new Object[] { new Worker("John", createQSet(),40000), false}
		};
	}

	@Test
	@Parameters(method = "VariableCharacteristics_CompareDescription")
	public void testEquals_DescriptionVariable(Object testValue, boolean expectedValue) {
		Qualification testQualification = new Qualification("Test Qualifications");
		assert (testQualification.equals(testValue)== expectedValue);
	}

	private Object[] VariableCharacteristics_CompareDescription() {
		return new Object[] {
				new Object[] {  new Qualification("Test Qualifications"), true},
				new Object[] {  new Qualification("Different Qualifications"), false},
		};
	}

	public void testToString(){
		for(String description : descriptions){
			Qualification newQualification = new Qualification(description);
			assert(newQualification.toString() == description);
		}
	}
  
	@Test
	//TODO: Add tests for workers with *multiple* qualifications
	//I am not 100% sure that this works as it should please look over
	public void testGetWorkersAndAddWorkers(){
		for(String description : descriptions){
			Qualification newQualification = new Qualification(description);
			
			//Create worker
			Set<Qualification> workersQualifications = new HashSet<Qualification>();
			workersQualifications.add(newQualification);
			double salary = 80000;
			Worker worker = new Worker("TestWorker", workersQualifications, salary);
			
			newQualification.addWorker(worker);
			assert(newQualification.getWorkers().contains(worker));
		}
	}

	@Test
	public void testGetWorkers_EmptySet(){
		Qualification newQualification = new Qualification("Qualification Description");
		assert(newQualification.getWorkers().isEmpty());
	}

	@Test
	public void testRemoveWorker1(){
		for(String description : descriptions){
			Qualification newQualification = new Qualification(description);
			
			//Create worker
			Set<Qualification> workersQualifications = new HashSet<Qualification>();
			workersQualifications.add(newQualification);
			double salary = 80000;
			Worker worker = new Worker("TestWorker", workersQualifications, salary);
			
			newQualification.addWorker(worker);
			newQualification.removeWorker(worker);
			assert(newQualification.getWorkers().isEmpty());
		}
	}
	@Test
	public void testQualificationDTO_noWorkers(){
		Qualification NO_WORKERS = new Qualification("JavaScript");
		QualificationDTO TEST_NO_WORKERS = new QualificationDTO("JavaScript", new String[0]);

		assert(NO_WORKERS.toDTO().equals(TEST_NO_WORKERS));
		assert (NO_WORKERS.toDTO().getWorkers().length ==0);
	}

	@Test
	public void testQualificationsDTO_withOneWorker(){
		Qualification eng = new Qualification("Engineer");
		Qualification it = new Qualification("IT");
		Qualification pr = new Qualification("Public Relations");
		Set<Qualification> qSet1 = new HashSet<>();
		qSet1.add(eng);
		qSet1.add(it);
		qSet1.add(pr);
		Worker sally = new Worker("Sally", qSet1, 100);

		String sallyAsString = sally.getName();
		String[] workers = {sallyAsString};

		Qualification python = new Qualification("Python");
		python.addWorker(sally);

		QualificationDTO expected = new QualificationDTO("Python", workers);
		QualificationDTO pythonDTO = python.toDTO();

		//equals only checks for description
		assert(pythonDTO.equals(expected));//equals only checks for description

		assert(Arrays.equals(pythonDTO.getWorkers(),workers));
	}
	@Test
	public void testQualificationToDTO_withWorkersNoProject() {

		//create worker 1
		Qualification eng = new Qualification("Engineer");
		Qualification it = new Qualification("IT");
		Qualification pr = new Qualification("Public Relations");
		Set<Qualification> qSet1 = new HashSet<>();
		qSet1.add(eng);
		qSet1.add(it);
		qSet1.add(pr);
		Worker sally = new Worker("Sally", qSet1, 100);

		//create worker 2
		Set<Qualification> qSet2 = new HashSet<>();
		Qualification py = new Qualification("Python");
		Qualification js = new Qualification("JavaScript");
		qSet2.add(py);
		qSet2.add(js);
		Worker bob = new Worker("Bob", qSet2, 100);

		//create test object & add worker 1 and worker 2
		Qualification marketing = new Qualification("Marketing");

		marketing.addWorker(bob); //2 qualifications 
		marketing.addWorker(sally); // 3 qualifications

		//marketing worker array to string arr
		String[] workersArray = {"Bob", "Sally"};

		//create marketingDTO with workers string arr
		QualificationDTO expectedMarketingDTO = new QualificationDTO("Marketing", workersArray);
		QualificationDTO testMarketingDTO = marketing.toDTO();
		assert(testMarketingDTO.equals(expectedMarketingDTO));

		assert(Arrays.equals(testMarketingDTO.getWorkers(),workersArray));
	}
  
	public Object[] toStringObjectsToTest(){
		return new Object[]{
			new Object[]{new Qualification("Test 1"), "Test 1", true},
			new Object[]{new Qualification("Test 2"),  "Test 2", true},
			new Object[]{new Qualification(" Test 1 "),  "Test 1", false},
			new Object[]{new Qualification("Test 3"),  "Test 1", false}
};
}

	@Test(expected = NullPointerException.class)
	public void testQualificationtoDTO_withNullWorkers(){
		String[] workers = null;
		QualificationDTO incorrectDTO = new QualificationDTO("Python", workers);

		Assert.assertFalse("QualificationDTO was constructed with null workers and no exception was thrown", true);
	}


	@Test(expected = NullPointerException.class)
	public void testQualificationtoDTO_withNullDescription(){
		Qualification eng = new Qualification("Engineer");
		Qualification it = new Qualification("IT");
		Qualification pr = new Qualification("Public Relations");
		Set<Qualification> qSet1 = new HashSet<>();
		qSet1.add(eng);
		qSet1.add(it);
		qSet1.add(pr);
		Worker sally = new Worker("Sally", qSet1, 100);
		
		String[] workers = {sally.toString()};
		String nullDescription = null;
		QualificationDTO incorrectDTO = new QualificationDTO(nullDescription, workers);

		Assert.assertFalse("QualificationDTO constructed with a null description", true);

	}

	@Test(expected = IllegalArgumentException.class)
	public void testQualificationDTO_emptyDescriptionString(){
		Qualification eng = new Qualification("Engineer");
		Qualification it = new Qualification("IT");
		Qualification pr = new Qualification("Public Relations");
		Set<Qualification> qSet1 = new HashSet<>();
		qSet1.add(eng);
		qSet1.add(it);
		qSet1.add(pr);
		Worker sally = new Worker("Sally", qSet1, 100);

		String[] workers = {sally.toString()};
		String description = "";

		new QualificationDTO(description, workers);
		Assert.assertFalse("QualificationDTO was able to construct with an empty string description", true);

	}

	public Object[] testingGetWorkerObjects(){
		Qualification noWorkers = new Qualification("No Workers");
		Qualification oneWorker = new Qualification("One Workers");
		Qualification manyWorkers = new Qualification("Many Workers");
		for(int worker = 0; worker < 100; worker++){
			if(worker == 1) oneWorker.addWorker(new Worker(String.format("Worker Number %d", worker),createQSet(), 100));
			manyWorkers.addWorker(new Worker(String.format("Worker Number %d", worker),createQSet(), 100));
		}
		return new Object[]{
			new Object[] {noWorkers, 0, true},
			new Object[] {oneWorker, 1, true},
			new Object[] {manyWorkers,100,true}
		};
	}

		@Test
		@Parameters(method = "testingGetWorkerObjects")
		public void testingSizeOfWorkers(Qualification qualification, int size, boolean result){
			assert((qualification.getWorkers().size() == size) == result);
		}

	public Object[] testingGetWorkerObjectToCompare(){
		Qualification noWorkers = new Qualification("No Workers");
		Qualification oneWorker = new Qualification("One Workers");
		Qualification manyWorkers = new Qualification("Many Workers");
		Set<Worker> oneWorkerObject = new HashSet<Worker>();
		Set<Worker> manyWorkerObjects = new HashSet<Worker>();

		for(int worker = 0; worker < 100; worker++){
			Worker singleWorker = new Worker(String.format("Worker Number %d", worker), createQSet(), 100);
			if(worker == 1) {
				oneWorker.addWorker(singleWorker);
				oneWorkerObject.add(singleWorker);
			}
			manyWorkers.addWorker(singleWorker);
			manyWorkerObjects.add(singleWorker);
		}
		return new Object[]{
			new Object[] {noWorkers, Collections.emptySet(), true},
			new Object[] {oneWorker, oneWorkerObject, true},
			new Object[] {manyWorkers,manyWorkerObjects,true},
			new Object[] {oneWorker, manyWorkerObjects, false},
			new Object[] {manyWorkers,oneWorkerObject,false},
			new Object[] {noWorkers, oneWorkerObject, false},
			new Object[] {noWorkers, manyWorkerObjects, false}
			};
	}

	@Test
	@Parameters(method = "testingGetWorkerObjectToCompare")
	public void testingSetsOfWorkers(Qualification control, Set<Worker> test, boolean result){
		assert(control.getWorkers().equals(test) == result);
	}


	public Object[] validQualifications(){
		return new Object[]{
			"Test 1",
			"  Test 2  ",
			"    Test 3      "
		};
	}

	@Test
	@Parameters(method = "toStringObjectsToTest")
	public void testingQualificationtoString(Object testCase, Object expected, Boolean expect){
		assert(testCase.toString().equals(expected) == expect);
}
	@Parameters(method = "validQualifications")
	public void validTestsForConstructor(String descriptions){
		Qualification testing = new Qualification(descriptions);
		assert(testing.getWorkers().size() == 0);
	}

	public Object[] invalidQualifications(){
		return new Object[]{
			null
		};
	}

	@Test(expected = NullPointerException.class)
	@Parameters(method = "invalidQualifications")
	public void invalidTestsForConstructorNull(String descriptions){
		Qualification invalidTests = new Qualification(descriptions);
	}
	public Object[] invalidQualificationsIllegal(){
		return new Object[]{
			"",
			" ",
			"   ",
			"      ",
			"                                      "
		};
	}

	@Test(expected = IllegalArgumentException.class)
	@Parameters(method = "invalidQualificationsIllegal")
	public void invalidTestsForConstructorArg(String descriptions){
		Qualification invalidTests = new Qualification(descriptions);
	}


	public Object[] removingWorkers(){
		Qualification tenWorkers = new Qualification("Ten Workers");
		Qualification fiftyWorkers = new Qualification("Fifty Workers");
		Qualification hundredWorkers = new Qualification("One Hundred Workers");
		for(int worker = 0; worker < 100; worker++){
			Worker oneWorker = new Worker(String.format("Worker #%d",worker), createQSet(), 100);
			if(worker < 10) tenWorkers.addWorker(oneWorker);
			if(worker < 50) fiftyWorkers.addWorker(oneWorker);
			if(worker < 100) hundredWorkers.addWorker(oneWorker);
		}
		return new Object[]{
			new Object[]{tenWorkers, 10, 5, true},
			new Object[]{fiftyWorkers, 50, 25, true},
			new Object[]{hundredWorkers,100,50,true}
		};
	}

	public Object[] addingWorkers(){
		Set<Worker> noWorker = new HashSet<Worker>();
		Set<Worker> oneWorker = new HashSet<Worker>();
		Set<Worker> tenWorker = new HashSet<Worker>();
		Set<Worker> fiftyWorker = new HashSet<Worker>();
		for(int i = 0; i < 50; i++){
			Worker worker = new Worker(String.format("Woreker #%d", i),new HashSet<Qualification>(),100);
			if(i == 1) oneWorker.add(worker);
			if(i < 10) tenWorker.add(worker);
			fiftyWorker.add(worker);
		}
		return new Object[]{
			new Object[]{new Qualification("No Workers"), noWorker, 0},
			new Object[]{new Qualification("One Worker"), oneWorker, 1},
			new Object[]{new Qualification("Ten Workers"), tenWorker, 10},
			new Object[]{new Qualification("Fifty Workers"), fiftyWorker, 50}
		};
	}

	@Test
	@Parameters(method = "removingWorkers")
	public void testingRemovingHalfWorkers(Qualification qual, int initialsize, int halfsize, boolean result){
		assert(qual.getWorkers().size() == initialsize);
		for(int start = 0; start < halfsize; start++){
			qual.removeWorker(new Worker(String.format("Worker #%d", start), createQSet(), 100));
		}
		assert(qual.getWorkers().size() == halfsize);
	}

	@Parameters(method = "addingWorkers")
	public void addingWorkersForTesting(Qualification qual, Set<Worker> workers, int expectedSize){
		for(Worker worker:workers) qual.addWorker(worker);
		assert(qual.getWorkers().size() == expectedSize);

	}
}
