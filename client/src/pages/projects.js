import ClickList from '../components/ClickList'
import { finishProject, getProjects, startProject, getQualifications, createProject } from '../services/dataService'
import {
    darkGrayContainerStyle,
    grayContainerStyle,
    pageStyle,
    greenText,
    redText,
    inputTextStyle
} from '../utils/styles'
import LocationID from '../utils/location'
import React, { useEffect, useState } from 'react'
import './createForm.css'

const CreateProjectForm = (props) => {

    let qualificationsProp = props.qualifications

    const [qualifications, SetQualifications] = useState([])
    const [name, SetName] = useState("")
    const [size, SetSize] = useState("")

    const [isValidName, SetIsValidName] = useState(true);
    const [isValidQualifications, SetIsValidQualifications] = useState(true);

    const [chosenQualifications, SetChosenQualifications] = useState([])
    

    useEffect(() => {
        qualificationsProp.then(SetQualifications)
    })

    // handlers

    const handleSelectQualification = (e) => {
        if (e.target.checked === true) {
            SetChosenQualifications([...chosenQualifications, e.target.name])
            SetIsValidQualifications(true)
        } else if (e.target.checked === false) {
            let remove = e.target.name
            SetChosenQualifications(chosenQualifications.filter(quals => quals !== remove))
        }
    }


    const handleSelectSize = (e) => {
        SetSize(e.target.id)   
    }

    const handleSubmit = () => {
        let validSubmission = true
        if (name.length === 0 || name.trim().length === 0) {
            SetIsValidName(false)
            validSubmission = false
        }

        if (chosenQualifications.length === 0) {
            SetIsValidQualifications(false)
            validSubmission = false
        }

        if(validSubmission){
            createProject(name, chosenQualifications, size)
            window.location.reload();
        }
    }

    const handleNameChange = (e) => {
        SetName(e.target.value)
        if (e.target.value.toString().trim().length !==0) {
            SetIsValidName(true)
        }
    }


    // functions for rendering
    const qualificationsList = []
    for (let i = 0; i < qualifications.length; i++) {
        const qualificationElement = qualifications[i]
        qualificationsList.push(
            <>
            <br/>
            <input type="checkbox" id={qualificationElement.description} name={qualificationElement.description} onClick={handleSelectQualification}/>
            <label for={qualificationElement.description}>{qualificationElement.description}</label>
            </>
        )
    }

    const projectSizes = ['BIG', 'MEDIUM', 'SMALL']
    const projectSizesCheckList = []
    for (let i = 0; i < projectSizes.length; i++) {
        projectSizesCheckList.push(
            <>
            <br/>
            <input type="radio" id={projectSizes[i]} name="size" onChange={handleSelectSize} defaultChecked={i===0}/>
            <label for={projectSizes[i]}>{projectSizes[i]}</label>
            </>
        )
    }


    const displayForm = () => {
        return (
            <div id="formBackground">
                <form id="createProject" >
                    <label id="nameLabel">Project Name: </label>
                    <input type="text" id="projectName" onChange={handleNameChange} style={inputTextStyle} ></input>
                    <label id ="validationTextError" style={{visibility: isValidName ? 'hidden':'visible'}}> Please enter a valid name </label>
                    <br/>
                    <br/>
                    <label id="qualificationsLabel">Project Qualifications: </label>
                    <label id ="validationTextError" style={{visibility: isValidQualifications ? 'hidden':'visible'}}> Please select at least one qualification</label>
                    <br/>
                    {qualificationsList}
                    <br/>
                    <br/>
                    <label id="size">Project Size: </label>
                    <br/>
                        {projectSizesCheckList}
                        <br/>
                    <div id="create"> <input type="button" value="Create" onClick={handleSubmit}/> </div>
                </form>
            </div>
        )
    }

    //the bones of the createProjectButton component
    return (
        <>
        <br/>
        {displayForm()}
        <br/>
        </>
    )
}


const Project = (project, active) => {
    return (
        <div>
            <div>{project.name}</div>
            {active === true ? ProjectBody(project) : null}
        </div>
    )
}

const ProjectBody = (project) => {
    let qSet = project.qualifications
    let mSet = project.missingQualifications
    const diff = qSet.filter(x => !mSet.includes(x)) 


    const handleStart = () => {
        startProject(project.name)
        .then(() => project.status = 'ACTIVE')
        .catch((err) => console.log(err))
    }

    const handleFinish = () => {
        finishProject(project.name)
        .then(() => project.status = 'FINISHED')
        .catch((err) => console.log(err))
        window.location.reload();
    }
    
    return (
        <div style={grayContainerStyle}>
            <div>
                Size: 
                <div style={darkGrayContainerStyle}> {project.size} </div>
            </div>
            <br/>
            <div> Status:
                <div style={darkGrayContainerStyle}> {project.status} </div>
            </div>
            <br/>
            <div>
                Assigned Workers: { project.workers.length === 0 ? <div style={darkGrayContainerStyle}> None </div> : <ClickList list={project.workers} styles={darkGrayContainerStyle} path='/workers' /> }
            </div>
            <br/>
            <div>
                Qualifications: <div style={greenText}> <ClickList list={diff} styles={darkGrayContainerStyle} path='/qualifications'/> </div>
                                <div style={redText}> <ClickList list={project.missingQualifications} styles={darkGrayContainerStyle} path='/qualifications'/> </div>
            </div>
            { ((project.status === 'PLANNED' || project.status === 'SUSPENDED') && project.missingQualifications.length === 0) ? <> <br/> <button onClick={handleStart}>Start Project</button> </> : null }
            { project.status === 'ACTIVE' && project.missingQualifications.length === 0 ? <> <br/> <button onClick={handleFinish}>Finish Project</button> </> : null }
        </div>
    )
}

const Projects = () => {
    const [projects, setProjects] = useState([])
    useEffect(() => { getProjects().then(setProjects) }, [])
    const active = LocationID('projects', projects, 'name')

    return (
        <div style={pageStyle}>
            <h2>
                Create new project:
            </h2>
            <CreateProjectForm qualifications={getQualifications()}/>
            <h2>
                Table containing all the projects:
            </h2>
            <ClickList active={active} list={projects} item={Project} path='/projects' id='name'/>
        </div>
    )
}

export default Projects
