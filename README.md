# rei
A command line example for robotic slot placements

# to run
- clone this repository to your local drive
- on cmd line cd to the cloned folder.  Then to : /out/production/rei
- run java com.rei.main 

# synopsis
-This main class was written to show how I approach this problem: 
Design a command-line program that controls a robotic arm by taking commands that move blocks stacked in a series of slots. After each command, output the state of the slots, with each line of output corresponding to a slot and each X corresponding to a block.

Commands:

- size [n] - Adjusts the number of slots, resizing if necessary. Program must start with this to be valid.
- add [slot] - Adds a block to the specified slot.
- mv [slot1] [slot2] - Moves a block from slot1 to slot2.
- rm [slot] - Removes a block from the slot.
- replay [n] - Replays the last n commands.
- undo [n] - Undo the last n commands.

# assumptions
- Some of the commands (undo and replay) that rely on looping through previous commands should not go outside the parameters of the grid size (ie- undo 4 and there are only three prev commands).  
- Some commands to replay and undo cannot be done (for example undoing SIZE) and a user friendly error should be presented 
- Setting SIZE at any time resets the commandArray list and essentially the application

# comments

 This is a quick and basic example of how I would approach this problem.   There are several things, if given the time, i would change:

- error handling would be in it's own class and more robust
- constants would be in their own class
- the grid would have its own class and most of the methods called in this main class dealing with grid actions would be handled in that object
- validation would have its own class 
- Some of the functionality in the main class is a bit rigid. If given the time they would be combined or minimized for code size efficiency


