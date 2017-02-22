import {displaySuccessMsg, displayErrorMsg, resource} from '../../actions'


export function submitStudy(visitor, questions, survey) {
    let answers = {};
    Object.keys(questions).forEach((key)=>{
        if(questions[key].type === "T1" || questions[key].type === "T2"){
            answers[key] = { type:questions[key].type,
                            method:questions[key].method, 
                            answer:questions[key].answer, 
                            correctSolution:questions[key].correctSolution,
                            time:questions[key].time
                        };
        }
        else if(questions[key].type==="T3"){
            answers[key] = { type:questions[key].type,
                            answer:questions[key].answer, 
                            time:questions[key].time
                        };
        }
    }) 
    return (dispatch) => {
        return resource('POST', 'submit', visitor.email, {visitor, answers, survey})
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