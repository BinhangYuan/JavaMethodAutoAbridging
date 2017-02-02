import React, { Component, PropTypes } from 'react'
import { connect } from 'react-redux'

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