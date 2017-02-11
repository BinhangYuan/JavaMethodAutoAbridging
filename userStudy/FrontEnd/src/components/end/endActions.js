import {displaySuccessMsg, displayErrorMsg, resource} from '../../actions'


export function submitStudy(visitor, questions, survey) {
    return (dispatch) => {
        return resource('POST', 'submit', {visitor, questions, survey})
        .then((response) => {
            if(response.result === "success"){
                dispatch(displaySuccessMsg("Your answer was successfully submitted to our server! Thanks again!"));
            }
            else{
                dispatch(displayErrorMsg("Submission failed!"));
            }
        }).catch((err) => {
            dispatch(displayErrorMsg("Submission failed!"));
        })
    }
}