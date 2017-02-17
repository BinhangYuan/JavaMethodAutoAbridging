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

  handleOptionReduction(e){
    console.log(e)
    this.props.questions.reduction = e.target.value;
    this.forceUpdate();
  }

 	render(){
 		return(
 			<div>
        <div className="row">
          <div className="col-md-9">
            <h4 className="text-center">Please choose the code based on this description:</h4>
          </div>
          <div className="col-md-2 text-center">
            <div className="form-group">
              <label className="control-label">Choose Reduction Option:</label>
              <div className="selectContainer">
                <select name="language" className="form-control" onChange={(e)=>{this.handleOptionReduction(e)}}>
                  <option value="r10" key="r10">Reduce to 10 lines</option>
                  <option value="r20" key="r20">Reduce to 20 lines</option>
                  <option value="r50%" key="r50%">Reduce by 50%</option>
                </select>
              </div>
            </div>
          </div>
          <div className="col-md-1"></div>
        </div>
        <div className="row">
   			  <div className="col-md-7 text-center">
        	 <div id="editor"><AceEditor mode="java" theme="chrome" name="editor" editorProps={{$blockScrolling:true}} value={this.props.questions.questions[this.props.questions.index.toString()].codes[this.codeId][this.props.questions.reduction]}/></div>
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