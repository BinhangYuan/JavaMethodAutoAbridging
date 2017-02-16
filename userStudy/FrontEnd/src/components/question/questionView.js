import React, {Component, PropTypes} from 'react'
import {connect} from 'react-redux'


import {nav2End, displayErrorMsg} from '../../actions'
import Message from '../message'
import ListItem from './listItem'
import Timer, {computeTime} from './timer'
import {checkOneQuestion, checkSurvey} from './questionActions'
import QuestionT1 from './questionT1'
import QuestionT2 from './questionT2'
import QuestionT3 from './questionT3'
import Survey from './survey'
import {surveyConfig} from './descriptionConst'

let interval = null;

export class QuestionView extends Component{

	constructor(props){
		super(props);
		this.questionCount = Object.keys(this.props.questions.questions).length;
		this.state = { clock: 0, time: ""};
	}	

	componentDidMount() {
    if (!interval) {
      	interval = setInterval(this.update.bind(this), 1000)
    }
  }

  componentWillUnmount() {
  	clearInterval(interval);
  	interval = null;
  }

  update() {
    let clock = this.state.clock;
    clock += 1;
    this.setState({clock: clock });
    let time = computeTime(clock);
    this.setState({time: time });
  }

	render(){
		return (
			<div>
				<div className="jumbotron text-center">
  					<h3>User Study</h3>
				</div>
				<div className="row">
    				<div className="col-md-2 text-center">
        				<ul className="nav nav-pills nav-stacked">
                {
        				  Object.keys(this.props.questions.questions).map((key)=>{
        						if(parseInt(key)===this.props.questions.index){
        							return <ListItem key={parseInt(key)} title={"Question "+parseInt(key)} active={true}/>
        						}
        						else{
        							return <ListItem key={parseInt(key)} title={"Question "+parseInt(key)} active={false}/>
        						}
        					}).concat(<ListItem key={this.questionCount+1} title={"Survey"} active={this.props.questions.index === (this.questionCount+1)}/>)
        				}
        				</ul>
   					</div>
            <div className="col-md-10">
              <div className="row">
                <div className="alert alert-info text-center" id="timer">{this.state.time}</div>
              </div>
              <div className="row">
              {
                this.props.questions.index === (this.questionCount + 1) ? <Survey/>:
                (this.props.questions.questions[this.props.questions.index].type === 'T1'? <QuestionT1/>:
                (this.props.questions.questions[this.props.questions.index].type === "T2"? <QuestionT2/>:
                (this.props.questions.questions[this.props.questions.index].type === "T3"? <QuestionT3/>: <div>Unexpected Type</div>)))
              }
              </div>
              <br/>
              <div className="row"><Message/></div>
            </div>
    			</div>

    			<div className="row">
    				<ul className="pager">
        			<li className="disabled"><a href="#"><span aria-hidden="true">&larr;</span> Previous</a></li>
       				<li><a href="#" id="nextButton" onClick={()=>{
       					if(this.props.questions.index === this.questionCount+1){//in the survey
                  this.props.dispatch(checkSurvey(this.props.survey.answers));
                  if(Object.keys(this.props.survey.answers).length===surveyConfig.length){
                    this.props.dispatch(nav2End());
                  }
                }
                else if(this.props.questions.answer==="Not done!" || this.props.questions.answer===""){
       						this.props.dispatch(displayErrorMsg("Please finish this question first!"));
       					}
       					else if(this.props.questions.index <= this.questionCount){

								  this.props.dispatch(checkOneQuestion(this.props.questions.index, this.props.questions.answer, this.state.clock, this.props.questions.reduction));
								  this.props.questions.index += 1;
       						this.state.clock = 0;
       						this.props.questions.answer = "Not done!";
								  this.forceUpdate();
							  }
							  else{
								  throw "This is an invalid branch!"
							  }
       					}}>Next <span aria-hidden="true">&rarr;</span></a></li>
    				</ul>
            <br/>
            <br/>
            <br/>
				</div>
        <nav className="navbar navbar-default navbar-fixed-bottom">
          <div className="container">
            <br/>
            <p className="text-center"> &copy; Computer Science Department, Rice University</p>
            <br/>
          </div>
        </nav>
    	</div>
		)
	}
}


export default connect((state) => {
	return {
		questions:state.questions,
    survey:state.survey
	}
})(QuestionView)