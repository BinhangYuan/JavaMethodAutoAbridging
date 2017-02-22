import React, { Component, PropTypes } from 'react'
import { connect } from 'react-redux'

import {surveyConfig} from './descriptionConst'

class Survey extends Component{
	constructor(props){
    super(props);
	}

  handleOption(e){
    //console.log(e);
    //console.log(e.target);
    this.props.survey.answers[e.target.name] = e.target.value;
    this.forceUpdate();
  }

 	render(){
 		return(
 			<div>
        <div className="col-md-2"></div>
   			<div className="col-md-8 alert alert-success">
        {
          surveyConfig.map((item,num)=>{
            return (
              <div key={"num "+ num}>
                <p className="text-justify">{item.title}</p>
                <div>
                  {
                    item.options.map((option,index)=>{
                      return (
                        <div className="radio" key={"num "+num+" option "+index}>
                          <label><input type="radio" name={"suveryQ"+num} value={"suveryQ"+num+"Option"+index} onChange={(e)=>{this.handleOption(e)}}/>{option}</label>
                        </div>
                      )
                    })
                  }
                </div>
                <br/>
              </div>
            )
        	})	 
        }
        </div>
        <div className="col-md-2"></div>
      </div>
    )
 	}
}

export default connect((state) => {
	//console.log(state);
	return {
		survey:state.survey
	}
})(Survey)