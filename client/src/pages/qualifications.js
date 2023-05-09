import React, { useEffect, useState } from 'react'
import ClickList from '../components/ClickList'
import { createQualification, getQualifications } from '../services/dataService'
import LocationID from '../utils/location'
import {darkGrayContainerStyle, grayContainerStyle, inputTextStyle, pageStyle} from '../utils/styles'

const CreateQualificationForm = (props) => {

    const [name, SetName] = useState("")
    const [errorMessage, setErrorMessage] = useState("")
    // handlers

    const handleSubmit = () => {
        if(name.trim() == 0) {
            setErrorMessage("Input is empty, please enter a qualification")
        } else {
        createQualification(name)
        window.location.reload();
        }
    }

    const handleNameChange = (e) => {
        SetName(e.target.value)
    } 


    // functions for rendering


    const displayForm = () => {
        return (
            <div id="formBackground">
                <form id="createProject" >
                    <label id="nameLabel">Qualification Name: </label>
                    <input type="text" id="projectName" onChange={handleNameChange} style={inputTextStyle} ></input>
                    <br/>
                    <br/>
                    <br/>
                    <div id="create"> <input type="button" value="Create" onClick={handleSubmit}/> 
                    {errorMessage && <div className="error"> {errorMessage} </div>} </div>
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

const Qualification = (qualification, active) => {
    return (
        <div>
            <div>{qualification.description}</div>
            {active === true ? QualificationBody(qualification) : null}
        </div>
    )
}

const QualificationBody = (qualification) => {
    return (
        <div style={grayContainerStyle}>
            Workers: <ClickList list={qualification.workers} styles={darkGrayContainerStyle} path="/workers" />
        </div>
    )
}

const Qualifications = () => {
    const [qualifications, setQualifications] = useState([])
    useEffect(() => { getQualifications().then(setQualifications) }, [])
    const active = LocationID('qualifications', qualifications, 'description')
    return (
        <div style={pageStyle}>
            <h2>
                Create new qualification:
            </h2>
            <CreateQualificationForm/>
            <h2>
                Table containing all the qualifications:
            </h2>
            <ClickList active={active} list={qualifications} item={Qualification} path='/qualifications' id='description' />
        </div>
    )
}

export default Qualifications
