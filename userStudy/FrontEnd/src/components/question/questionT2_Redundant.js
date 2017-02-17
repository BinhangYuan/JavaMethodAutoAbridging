import React, { Component, PropTypes } from 'react'
import { connect } from 'react-redux'
import brace from 'brace'
import AceEditor from 'react-ace'
import 'brace/mode/java'
import 'brace/theme/chrome'

import {T2description} from './descriptionConst'

class QuestionT2 extends Component{
	constructor(props){
		super(props);
	}

	handleOptionAnswer(e){
  	console.log(e)
  	this.props.questions.answer=e.target.value;
  	console.log(this.props.questions.answer)
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
            <h4 className="text-center">Please match the best description for the following Java methods:</h4>
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
   				<div className="col-md-6 text-center">
            <h5>Method A:</h5>
        		<div id="editor"><AceEditor mode="java" theme="chrome" name="editor" editorProps={{$blockScrolling:false}} value={this.props.questions.questions[this.props.questions.index.toString()].codeA[this.props.questions.reduction]}/></div>
    			</div>
          <div className="col-md-6 text-center">  
            <h5>Method B:</h5>  
            <div id="editor"><AceEditor mode="java" theme="chrome" name="editor" editorProps={{$blockScrolling:false}} value={this.props.questions.questions[this.props.questions.index.toString()].codeB[this.props.questions.reduction]}/></div>
          </div>
        </div>
        <div className="row">
          <div className="col-md-2"></div>
    			<div className="col-md-8">
        		<div className="alert alert-success" id="alternatives">
            <p className="text-justify">{T2description}</p>
            <br/>
            <p className="text-justify"><b>Description A: </b>{this.props.questions.questions[this.props.questions.index.toString()].docA}</p>
        		<p className="text-justify"><b>Description B: </b>{this.props.questions.questions[this.props.questions.index.toString()].docB}</p>
            {
        			this.props.questions.questions[this.props.questions.index.toString()].Alternatives.map((option,index)=>{
        				return (
        					<div className="radio" key={"option "+index}>
									 <label><input type="radio" name="optradio" checked={this.props.questions.answer===("option "+index)} value={"option "+index} onChange={(e)=>{this.handleOptionAnswer(e)}}/>{option}</label>
								  </div>
							  )
        			})
        		}
        		</div>
          </div>
          <div className="col-md-2"></div>
    	 </div>
      </div>
    )
 	}
}

export default connect((state) => {
	return {
		questions:state.questions
	}
})(QuestionT2)