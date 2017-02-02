from flask import Flask, jsonify, request, redirect, url_for
from flask_cors import CORS
import json

frontURL = "http://127.0.0.1:10008/"

app = Flask(__name__)
CORS(app)

@app.route("/")
def hello():
    return "Hello from Flask backend!"

@app.route("/visitor", methods=['POST'])
def visitor():
	print dict(request.form)
	return redirect(frontURL+"question.html")
	#return redirect(url_for(frontURL+"question.html"),email=request.form.email, level=request.form.level)



@app.route("/questions")
def questions():
	with open('questionStub.json') as file:
		questions = json.load(file)
    	return jsonify(questions)




if __name__ == "__main__":
    app.run(host = '127.0.0.1', port=10006)