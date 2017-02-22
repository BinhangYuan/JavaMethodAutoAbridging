import React, { Component, PropTypes } from 'react'
import { connect } from 'react-redux'

export function computeTime(secondCount){
    let mins = Math.floor(secondCount/60);
    let seconds = secondCount%60;
    let string = "";
    if(mins>=60){
        //throw "Too long for this task! Invalid case!"
    }
    if(mins<10){
        string += '0';
    }    
    string += mins;
    string+=':';
    if(seconds<10){
        string+='0';
    }
    string+=seconds;
    return string;
}


class Timer extends React.Component{
    constructor(props){
        super(props);
        this.state = { clock: 0, time: '' }
    }

    render(){
        return(
            <div>{this.count}</div>
        )
    }
    
};

export default connect()(Timer)