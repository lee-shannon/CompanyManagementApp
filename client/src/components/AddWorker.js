import React, { useState, useEffect } from 'react';
import {createWorker, getQualifications} from '../services/dataService';
import {inputTextStyle} from "../utils/styles";

const AddWorker = ( props ) => {
  const [name, setName] = useState("");
  const [wage, setWage] = useState("");

  const [isValidName, SetIsValidName] = useState(true);
  const [isValidSalary, SetIsValidSalary] = useState(true);
  const [isValidQualifications, SetIsValidQualifications] = useState(true);

  const [qualificationsSelected, setQualificationsSelected] = useState([]);
  const [qualificationsOptions, setQualificationsOptions] = useState([]);

  useEffect(() => { getQualifications().then((qualifications) => {
    const qualificationsOptions = qualifications.map((item) => item.description);
    setQualificationsOptions(qualificationsOptions);
  });
  }, []);

  const handleSubmit = (event) => {
      event.preventDefault();

      let validSubmission = true
      if (name.trim().length === 0) {
          SetIsValidName(false)
          validSubmission = false
      }

      if(!wage.match(/\d/) || wage.trim().length === 0 || wage < 0) {
          SetIsValidSalary(false)
          validSubmission = false
      }

      if(qualificationsSelected.length===0){
          SetIsValidQualifications(false)
          validSubmission = false
      }

      if(validSubmission){
          createWorker(name, wage, qualificationsSelected).catch(function (error){
              if(error.response){
                  alert("Could not add worker");
              }
          });
          window.location.reload();
      }
  };

  const handleNameChange = (event) => {
      setName(event.target.value);
      // if (event.target.value.toString().trim().length !==0) {
          SetIsValidName(true)
      // }
  };

  const handleWageChange = (event) => {
        setWage(event.target.value);
      SetIsValidSalary(true)

  };

  const handleQualificationsChange = (event) => {
    const option = event.target.value;
    SetIsValidQualifications(true)
    if (qualificationsSelected.includes(option)) {
      setQualificationsSelected(qualificationsSelected.filter((q) => q !== option));
    } else {
      setQualificationsSelected([...qualificationsSelected, option]);
    }
  };

  return (
      <div id="formBackground">
        <form>
            <label><b>Name:</b></label>
            <input type="text" onChange={handleNameChange} style={inputTextStyle} />
            <label id ="validationTextError" style={{visibility: isValidName ? 'hidden':'visible'}}> Please enter a valid name </label>
            <br/>
            <br/>
            <label>
            <b>Salary:</b>
            <input type="text" value={wage} onChange={handleWageChange} style={inputTextStyle}/>
            <label id ="validationTextError" style={{visibility: isValidSalary ? 'hidden':'visible'}}> Please enter a valid salary </label>
            </label>
            <br/>
            <br/>
            <label>
              <span><b>Qualifications:</b></span>
                <label id ="validationTextError" style={{visibility: isValidQualifications ? 'hidden':'visible'}}> Please enter a valid qualifications </label>
                <br/>
              <br/>
              <select multiple={true} value={qualificationsSelected} onChange={(e)=>handleQualificationsChange(e)} style={inputTextStyle}>
                {qualificationsOptions.map((qualification) => (
                  <option key={qualification} value={qualification}>
                    {qualification}
                  </option>
                ))}
              </select>
            </label>
            <br/>
            <br/>
          <div id ="create"><button type="submit" onClick={handleSubmit}>Create</button></div>
        </form>
      </div>
  );
};

export default AddWorker;
