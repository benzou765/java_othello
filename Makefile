class/othello/Game.class: src/othello/Game.java class/othello/Othello.class
	javac -encoding utf-8 -cp class src/othello/Game.java -d class
class/othello/Othello.class: src/othello/Othello.java class/othello/Panel.class
	javac -encoding utf-8 -cp class src/othello/Othello.java -d class
class/othello/Panel.class: src/othello/Panel.java
	javac -encoding utf-8 -cp class src/othello/Panel.java -d class
