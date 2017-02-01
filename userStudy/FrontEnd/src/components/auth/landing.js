import React from 'react'

import Vistor from './vistor'


//The landing page JSX

const Landing = () => (
	<div>
		<div className="jumbotron text-center">
  			<h1>Welcome to the User Study</h1>
		</div>
		<div className="container">
			<Vistor/>
		</div>
	</div>
)

export default Landing