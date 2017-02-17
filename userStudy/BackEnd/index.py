from flask import Flask, jsonify, request, redirect, url_for
from flask_cors import CORS
import json
import time


app = Flask(__name__)
CORS(app, supports_credentials=True)

@app.route("/")
def hello():
    return "Hello from Flask backend!"


@app.route("/visitor", methods=['POST'])
def visitor():
	#print dict(request.get_json())
	return jsonify(request.get_json())


@app.route("/submit", methods=['POST'])
def submit():
	#print dict(request.get_json())
	jsonObject = request.get_json();
	if 'visitor' in jsonObject:
		email = jsonObject['visitor']['email']
		timestamp = int(time.time())
		fileName = "./survey/"+email+"_"+str(timestamp)+".json"
		with open(fileName, "w") as output:
			json.dump(jsonObject, output)
			return jsonify({"result":"success"})
	else:
		return jsonify({"result":"failure"})


@app.route("/questions")
def questions():
	with open('questionAll.json') as file:
		questions = json.load(file)
    	return jsonify(questions)



if __name__ == "__main__":
    app.run(host = '127.0.0.1', port=3000)