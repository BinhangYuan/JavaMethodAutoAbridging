import React, { Component, PropTypes } from 'react'
import { connect } from 'react-redux'
import brace from 'brace'
import AceEditor from 'react-ace'
import 'brace/mode/java'
import 'brace/theme/chrome'

import {T1description} from './descriptionConst'

class QuestionT1 extends Component{
	constructor(props){
		super(props);
    this.codeId = 0;
	}

	handleOptionAnswer(e){
    console.log(e)
    this.codeId = e.target.value;
    this.props.questions.answer=e.target.value;
    console.log(this.props.questions.answer);
    this.forceUpdate();
  }

 	render(){
 		return(
 			<div>
        <div className="row">
          <div className="col-md-1"></div>
          <div className="col-md-10">
            <h4 className="text-center">Please choose the code based on this description:</h4>
          </div>
          <div className="col-md-1"></div>
        </div>
        <div className="row">
   			  <div className="col-md-7 text-center">
        	 <div id="editor"><AceEditor mode="java" theme="chrome" name="editor" width="100%" height="600px" editorProps={{$blockScrolling:true}} value={this.props.questions.questions[this.props.questions.index.toString()].codes[this.codeId]}/></div>
    		  </div>

    		  <div className="col-md-5">
        	  <div className="alert alert-success" id="alternatives">
              <p className="text-justify">{T1description}</p>
              <br/>
              <p><b>Method Description:</b></p>
              <p>{this.props.questions.questions[this.props.questions.index.toString()].description}</p>
              <br/>
              <div className="form-group">
                <label className="col-md-6 control-label">Choose Method:</label>
                <div className="col-md-6 selectContainer">
                  <select name="language" className="form-control" defaultValue={"Not done!"} onChange={(e)=>{this.handleOptionAnswer(e)}}>
        			    {
                    [<option value="Not done!" key="dropdownCodeDefault" disabled="true">- choose a method -</option>].concat(
        				    this.props.questions.questions[this.props.questions.index.toString()].codes.map((code,id)=>{
        					   return(<option value={id} key={"dropdownCode"+id}>{"Method "+(id+1)}</option>);
        				    }))
        			   }
                  </select>
                </div>
    			   </div>
             <br/>
            </div>
          </div>
        </div>
      </div>
    )
 	}
}

export default connect((state) => {
	console.log(state);
	return {
		questions:state.questions
	}
})(QuestionT1)