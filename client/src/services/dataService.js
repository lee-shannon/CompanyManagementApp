import axios from 'axios'

const SERVER_ADDRESS = 'http://localhost:4567/api/'

export function getQualification(name) {
    return axios.get(SERVER_ADDRESS + 'qualifications/' + name).then((res) => JSON.parse(res.request.response))
}

export function getQualifications() {
    return axios.get(SERVER_ADDRESS + 'qualifications').then((res) => JSON.parse(res.request.response).sort((a, b) => a.description.localeCompare(b.description)))
}

export function createQualification(description) {
    return axios.post(SERVER_ADDRESS + 'qualifications/' + description, { description: description })
}
export function getProjects() {
    return axios.get(SERVER_ADDRESS + 'projects').then((res) => JSON.parse(res.request.response).sort((a, b) => a.name.localeCompare(b.name)))
}   

export function getProject(name) {
    return axios.get(SERVER_ADDRESS + 'projects/' + name).then((res) => JSON.parse(res.request.response))
}

export function startProject(name) {
    return axios.put(SERVER_ADDRESS + 'start', {name})
}

export function finishProject(name) {
    return axios.put(SERVER_ADDRESS + 'finish', {name})
}

export function createProject(name, qualifications, size) {
    return axios.post(SERVER_ADDRESS + 'projects/' + name, {name, qualifications, size})
}

export function getWorker(name) {
    return axios.get(SERVER_ADDRESS + 'workers/' + name).then((res) => JSON.parse(res.request.response))
}

export function getWorkers() {
    return axios.get(SERVER_ADDRESS + 'workers').then((res) => JSON.parse(res.request.response).sort((a, b) => a.name.localeCompare(b.name)))
}

export function createWorker(name, salary, qualifications) {
    return axios.post(SERVER_ADDRESS + 'workers/' + name, { name, salary, qualifications })
}

export function unassign(projectName,workerName) {
    return axios.put(SERVER_ADDRESS + 'unassign',{project: projectName,worker: workerName})
}

export function assign(workerName,projectName) {
    return axios.put(SERVER_ADDRESS + 'assign',{worker:workerName,project:projectName})
}
