import React from 'react'

import Vistor from './visitor'


//The landing page JSX

const Landing = () => (
	<div>
		<div className="jumbotron text-center">
  			<h1>Welcome to the User Study</h1>
		</div>
		<div className="container">
			<Vistor/>
		</div>
		<nav className="navbar navbar-default navbar-fixed-bottom">
			<div className="container">
          		<p className="text-center"> &copy; Computer Science Department, Rice University</p>
        	</div>
        </nav>
	</div>
)

export default Landing