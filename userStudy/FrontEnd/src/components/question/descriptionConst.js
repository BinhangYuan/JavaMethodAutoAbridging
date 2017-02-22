export const T1description = "Try to determine the correct code quickly as possible, while also ensuring that you have determined the correct code with \'reasonable\' accurately, however you choose to define \'reasonable\'.  If at some point you determine that it is impossible to locate the correct code, then simply choose the code that is your best guess as to the answer.";
export const T2description = "Try to complete each matching as quickly as possible, while also ensuring that you have labeled each code with \'reasonable\' accurately, however you choose to define \'reasonable\'.  If you determine that it is impossible to decide with more than 50/50 (coin-flip) accuracy which documentation goes with which method, then choose \'impossible to decide\'.";
export const T3description = "Try to write a one-sentence description of the method. This description should inform someone at a high level what the method does. Try to write your description quickly as possible, while also ensuring that your description is of \'reasonable\' quality, however you define \'reasonable\'.";


export const surveyConfig = [
	{
		title:"Which version of the code tended to make it easier to write your description?",
		options: ["Non-reduced code","Code Reduced by method A","Code reduced by method B"]
	},
	{
		title:"Which reducation level is easiest to use for reduction method A?",
		options: ["To around 15 lines", "To around 30 lines", "To 50% of the original lines"]
	},
	{
		title:"Which reducation level is easiest to use for reduction method B?",
		options: ["To around 15 lines", "To around 30 lines", "To 50% of the original lines"]
	},
	{
		title:"Which compressed code misses more necessary information for you to know the high level functionality of this method?",
		options: ["Code reduced by method A", "Code reduced by method B"]
	},
	{
		title:"Which compressed code contains more unnecessary statements for you to know the high level functionality of this method?",
		options: ["Code reduced by method A", "Code reduced by method B"]
	}
]