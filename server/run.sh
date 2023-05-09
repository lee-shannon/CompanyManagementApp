module load courses/cs415
mvn clean test
mvn test-compile org.pitest:pitest-maven:mutationCoverage
mvn pmd:pmd pmd:cpd
mvn ekstazi:ekstazi
mvn clover:setup clover:optimize test clover:snapshot
python3 -m http.server 8000
