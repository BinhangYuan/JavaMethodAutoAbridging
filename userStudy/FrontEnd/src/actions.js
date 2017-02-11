import Promise from 'bluebird'
import fetch from 'isomorphic-fetch'

const local = true;
export const url = local? 'http://127.0.0.1:3000' : 'UNKNOWN_YET'


const Action = {
    ERRORMSG:'error message',
    SUCCESSMSG: 'success message',

    NAV2QUESTION: 'navigate to question page',
    NAV2END: 'navigate to end page',
    NAV2INDEX: 'navigate to index page',

    VISIT: 'visit',

    UPDATEQUESTION: 'update questions',
    ADDANSWER: 'add an answer',

    UPDATESURVEY: 'update survey question answers'
}

export default Action

export function displayErrorMsg(msg){
    return {type: Action.ERRORMSG, errorMsg: msg};
}

export function displaySuccessMsg(msg){
    return {type: Action.SUCCESSMSG, successMsg: msg};
}

export function nav2Question(){
    return {type: Action.NAV2QUESTION};
}

export function nav2End(){
    return {type: Action.NAV2END};
}

export function nav2Index(){
    return {type: Action.NAV2INDEX};
}

export function resource(method, endpoint, payload){
    const options =  {
        method,
        credentials: 'include',
    };
    
    options.headers = {'Content-Type': 'application/json'};

    if (payload) options.body = JSON.stringify(payload);

    return fetch(`${url}/${endpoint}`, options)
    .then(response => {
        if (response.status === 200) {
            if (response.headers.get('Content-Type').indexOf('json') > 0) {
                return response.json()
            }else {
                return response.text()
            }
        } else {
            throw new Error(response.statusText)
        }
    }).catch(error=>{
        console.log(error);
    })
}