import {inlineButton, pageStyle} from '../utils/styles'
import { useEffect, useState } from 'react'
import { getProjects, getWorkers, assign } from '../services/dataService'

const Assign = () => {
    const[workers,setWorkers] = useState([]);
    const [selectedOption, setSelectedOption] = useState("");
    useEffect(() => { getWorkers().then(setWorkers) }, [])
    const [projects, setProjects] = useState([])    
    useEffect(() => { getProjects().then(setProjects) }, [])

    
    function handleChange(event) {
        setSelectedOption(event.target.value);
    }
    
    const options = [];
    for (let i = 0; i < workers.length; i++) {
      const option = workers[i];
      options.push(
        <option key={option.name} value={option.name}>
          {option.name}
        </option>
      );
    }

    return (
        <div style={pageStyle}>
            <h2>
                Select a worker to assign a project to:
            </h2>
            <div>
                <select id="my-dropdown" 
                value={selectedOption} 
                onChange={handleChange}
                style={{ width: '50%', height: '20%', border: "2px solid #ccc", backgroundColor:'#fff', fontSize : '20px'}} >
                <option value="">Select a Worker</option>
                {options}
                </select>
            </div>
            <ShowProjects worker = {selectedOption} allWorkers = {workers} allProjects = {projects}/>            
        </div>
    )
}

const ShowProjects = (props) =>{
    let availableProjects = []
    let worker = props.worker
    let allWorkers = props.allWorkers
    let allProjects = props.allProjects
    if(worker.length !== 0)
        worker = findWorker(allWorkers, worker)
        availableProjects = getProjectsNotFinishedOrActive(allProjects)
        availableProjects = getProjectsFromWorkerName(worker.name, availableProjects)
        availableProjects = getProjectsFromWorkerQualifications(worker.qualifications, availableProjects)
        availableProjects = getProjectsFromWorkerWorkload(worker.workload, availableProjects)
        availableProjects = convertToNameArray(availableProjects)
        if(availableProjects.length > 0){
            return(
                <>
                <h3>Assignable projects:</h3>
                    <ul>
                        {availableProjects.map(name => (
                            <li style={{fontSize:'1vw'}} key={name}>
                                <div style={inlineButton}>
                                    <div style = {{margin: 5}}>{name}</div>
                                    <div style = {{margin: 5}}>
                                        <button size = 'lg' onClick={() => assignClick(worker.name,name)} >Assign</button>
                                    </div>
                                </div>
                            </li>
                        ))}
                    </ul>
                </>
            )    
        }
        if(availableProjects.length === 0 && worker.length !== 0){
            return <div style={{fontSize: '1vw', margin: 5, color:'#D27373'}}>There are no projects that can be assigned to this worker.</div>
        }
}

function findWorker(allWorkers, worker){
    for(let index = 0; index < allWorkers.length; index++){
        if(allWorkers[index].name === worker) return allWorkers[index]
    }
}
 
function assignClick(worker,project){
    assign(worker,project).catch((err) => alertUser());
    window.location.reload();
}

function getProjectsNotFinishedOrActive(projects){
    let available = []
    for(let i = 0; i < projects.length; i++){
        if(projects[i].status !== "FINISHED" && projects[i].status !== "ACTIVE") available.push(projects[i])
    }
    return available
}

function getProjectsFromWorkerName(workerName, projects){
    let workerNotInProject = []
    for(let i = 0; i < projects.length; i++){
        if (!projects[i].workers.includes(workerName)) workerNotInProject.push(projects[i])
    }
    return workerNotInProject
}

function getProjectsFromWorkerQualifications(qualifications, projects){
    let workerNotInProject = []
    if(qualifications !== undefined)
        for(let i = 0; i < qualifications.length; i++){
            for(let j = 0; j < projects.length; j++){
                if(projects[j].missingQualifications.includes(qualifications[i])&& !workerNotInProject.includes(projects[j])) 
                    workerNotInProject.push(projects[j])
            }
        }
    return workerNotInProject
}

function getProjectsFromWorkerWorkload(workload, projects){
    let overloaded = 12
    let workerNotInProject = []
    for(let i = 0; i < projects.length; i++){
        if (projects[i].size === "BIG"){
            if (3 + workload <= overloaded) workerNotInProject.push(projects[i])
        }
        if (projects[i].size === "MEDIUM"){
            if (2 + workload <= overloaded) workerNotInProject.push(projects[i])
        }
        if (projects[i].size === "SMALL"){
            if (1 + workload <= overloaded) workerNotInProject.push(projects[i])
        }
    }

    return workerNotInProject
}

function convertToNameArray(projects){
    let names = []
    for(let i = 0; i < projects.length; i++){
        names.push(projects[i].name)
    }
    return names
}

function alertUser(){
    alert("Cannot assign worker to selected Project\nPlease consider the workers...\nQualifications\nWorkload\nand Project Status");
    window.location.reload();
}
export default Assign
