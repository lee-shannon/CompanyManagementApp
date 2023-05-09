import { useEffect, useState } from 'react'
import ClickList from '../components/ClickList'
import {getWorkers, unassign} from '../services/dataService'
import AddWorker from '../components/AddWorker'
import LocationID from '../utils/location'
import {darkGrayContainerStyle, grayContainerStyle, inlineButton, pageStyle} from '../utils/styles'

const Worker = (worker, active) => {
    return (
        <div>
            <div>{worker.name}</div>
            {active === true ? WorkerBody(worker) : null}
        </div>
    )
}

const WorkerBody = (worker) => {

    const ProjectListItem = (project) => {
        return (
            <div style={inlineButton}>
                <div style = {{margin: 5}}>{project}</div>
                <div style = {{margin: 5}}>
                    <button onClick={(e)=>handleUnassignProject(e, project, worker.name)}> Unassign </button>
                </div>
            </div>
        )
    }

    const handleUnassignProject = (e, projectName) => {
        //Prevents the event from ClickList to be called
        e.stopPropagation();

        unassign(projectName, worker.name)
            .catch((err) => console.log(err));
        //refreshing page to show the most updated project list
        window.location.reload()
    }

    return (
        <div style={grayContainerStyle}>
            <div> Current Workload: <div style={darkGrayContainerStyle}> {worker.workload}</div></div>
            <br/>
            <div> Current Salary: <div style={darkGrayContainerStyle}>${worker.salary}</div></div>
            <br/>
            <div> Qualifications: <ClickList list={worker.qualifications} styles={darkGrayContainerStyle} path="/qualifications" /></div>
            <br/>
            <div> Assigned Projects: <ClickList list={worker.projects} item = {ProjectListItem} styles={darkGrayContainerStyle} path="/projects"/> </div>
            </div>
    )
}
const Workers = () => {
    const [workers, setWorkers] = useState([])
    useEffect(() => { getWorkers().then(setWorkers) }, [])
    const active = LocationID('workers', workers, 'name')
   

    return (
        <div style={pageStyle}>
            <h2>
                Create new worker:
            </h2>
            <AddWorker setWorkers = {setWorkers} workers = {workers}></AddWorker>
            <h2>
                Table containing all the workers:
            </h2>
            <ClickList active={active} list={workers} item={Worker} path='/workers' id='name' />
        </div>
    )
}

export default Workers
