from flask import Flask, jsonify, request, redirect, url_for
from flask_cors import CORS
import json


app = Flask(__name__)
CORS(app, supports_credentials=True)

@app.route("/")
def hello():
    return "Hello from Flask backend!"

@app.route("/visitor", methods=['POST'])
def visitor():
	print dict(request.get_json())
	return jsonify(request.get_json())
	#return redirect(url_for(frontURL+"question.html"),email=request.form.email, level=request.form.level)



@app.route("/questions")
def questions():
	with open('questionStub.json') as file:
		questions = json.load(file)
    	return jsonify(questions)




if __name__ == "__main__":
    app.run(host = '127.0.0.1', port=3000)