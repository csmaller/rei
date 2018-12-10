package com.rei;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class main {

    private static Scanner sc = new Scanner(System.in);
    private static String userInput = "";
    private static String userCommand = "";
    private static String userInputString = "";
    private static int commandInt = -1;
    private static int moveFromCommand = -1;
    private static ArrayList<String> commandArray = new ArrayList<String>();
    private static List<List<String>> grid;
    private static String error = "";
    private static boolean isValid = false;
    private static boolean isReplaying = false;
    //constants
    public static final String BAD_COMMAND_ERROR = "Sorry this command is unreadable to our robot. Please try again";
    public static final String BAD_MV_ERROR = "Sorry , for our robot to move it needs a destination slot inside our slot params. Please try again";
    public static final String BAD_INT_ERROR = "Sorry integer values must be greater than 0. Please try again";
    public static final String SIZE = "size";
    public static final String ADD = "add";
    public static final String MV = "mv";
    public static final String RM = "rm";
    public static final String REPLAY = "replay";
    public static final String UNDO = "undo";
    public static String[] constantsArray = new String[]{SIZE, ADD, MV, REPLAY, UNDO, RM};
    public static List<String> constantsList = Arrays.asList(constantsArray);

    /**
     * main. start up with a prompt for size
     * we only call this once to ensure SIZE is called
     *
     * @param args
     */
    public static void main(String args[]) {
        //only move on once size is set
        String input = "";
        while (!userCommand.equals(SIZE)) {
            promptUser();
        }
        setGrid();
    }

    /**
     * init. prompt and wait for user commands
     */
    private static void init() {
        error = ""; //reset error
        while (!isValid) {
            promptUser();
        }
        doCommand();
    }

    /**
     * run the command through the switch
     */
    private static void doCommand() {
        switch (userCommand) {
            case SIZE:
                setGrid();
                break;
            case MV:
                doMove();
                break;
            case RM:
                doRemove();
                break;
            case REPLAY:
                doReplay();
                break;
            case UNDO:
                doUndo();
                break;
            case ADD:
                doAdd();
                break;
            default:
                break;
        }
        //if replay or undo is looping through commands
        //do not prompt until done
        if (!isReplaying) {
            showGrid();
        }
    }

    /**
     * run the command through the switch statement for their
     * opposite command and clear the item in saved commands
     */
    private static void doUndoCommand() {
        switch (userCommand) {
            case MV:
                //swap the to/from commands and doMove()
                int tempMoveToCommand = moveFromCommand;
                moveFromCommand = commandInt;
                commandInt = tempMoveToCommand;
                doMove();
                break;
            case RM:
                doAdd();
                break;
            case ADD:
                doRemove();
                break;
            default:
                System.out.println(BAD_COMMAND_ERROR);
                break;
        }

        if (!isReplaying) {
            showGrid();
        }
    }


    /**
     * when all processes end reset the prompt
     * - store the last command
     * - reset validity checks
     */
    private static void resetUserPrompt() {
        isValid = false;
        storeLastCommand();
        init();
    }

    /**
     * redo n number of previous commands
     */
    private static void doReplay() {
        checkUndoRedoArraySize();
        isReplaying = true;
        for (int i = commandInt; i > 0; i--) {
            loadPrevCommand(i);
            doCommand();
        }
        isReplaying = false;
        showGrid();
    }

    /**
     * UNDO n number of previous commands
     */
    private static void doUndo() {
        checkUndoRedoArraySize();
        isReplaying = true;
        for (int i = commandInt; i > 0; i--) {
            loadPrevCommand(i);
            doUndoCommand();
        }
        isReplaying = false;
        showGrid();
    }

    /**
     * on UNDO an REDO make sure the command to go back is not
     * bigger than the size of the command array
     */
    private static void checkUndoRedoArraySize() {
        if (commandInt > commandArray.size()) {
            System.out.println(BAD_COMMAND_ERROR);
            isValid = false;
            init();
        }
    }

    private static void doAdd() {
        if (commandInt <= grid.size()) {
            List input = grid.get(commandInt - 1);
            input.add("X ");
        } else {
            throwError();
        }

    }

    /**
     * show a standard error
     * TODO: add more concise error messages through the function
     * TODO: create Error handling class
     */
    private static void throwError() {
        System.out.println(BAD_COMMAND_ERROR);
        resetUserPrompt();
    }

    private static void doRemove() {
        if (commandInt <= grid.size()) {
            List input = grid.get(commandInt - 1);
            if (input.size() > 0) {
                input.remove(input.size() - 1);
            }
        }
    }

    private static void doMove() {
        //make sure both params are inside the grid slot size
        if (moveFromCommand <= grid.size() && commandInt <= grid.size()) {
            List input = grid.get(moveFromCommand - 1);
            if (input.size() > 0) {
                input.remove(input.size() - 1);
                doAdd();
            } else {
                throwError();
            }
        } else {
            throwError();
        }
    }

    /**
     * for each input show the resulting grid to the user command
     * this is the last of the command chain so go back to init
     * and wait for next user input when complete
     */
    private static void showGrid() {
        int gridSize;
        if (userCommand == SIZE) {
            gridSize = commandInt;
        } else {
            gridSize = grid.size();
        }

        for (int i = 0; i < gridSize; i++) {
            String gridShowItem = "";
            List<String> gridItem = grid.get(i);
            for (int t = 0; t < gridItem.size(); t++) {
                gridShowItem += gridItem.get(t);
            }
            System.out.println((i + 1) + ":" + gridShowItem);
        }
        resetUserPrompt();
    }

    /**
     * set up the initial grid with the size of items based on the
     * user commandInt then (re)start the app
     */
    private static void setGrid() {
        grid = new ArrayList<List<String>>();
        for (int i = 0; i < commandInt; i++) {
            ArrayList<String> gridItem = new ArrayList<String>();
            grid.add(gridItem);
        }
        commandArray = new ArrayList<String>();
        showGrid();
    }

    /**
     * prompt the user for input
     *
     * @param prompts
     * @return
     */
    private static void promptUser() {
        if (error != "") {
            System.out.println(error);
        }
        String prompt = "> ";
        System.out.print(prompt);
        parseUserInput(sc.nextLine());
    }

    /**
     * store our commands for re-call if needed
     *
     * @param command
     */
    private static void storeLastCommand() {
        commandArray.add(userInputString);
    }

    /**
     * load a prev command based on how many back the user wants to go
     *
     * @param countBack
     */
    private static void loadPrevCommand(int countBack) {
        int commandPos = commandArray.size() - countBack;
        String command = commandArray.get(commandPos);
        parseUserInput(command);
    }

    /**
     * parse the user input
     * and count the split array
     *
     * @param input
     */
    private static void parseUserInput(String input) {
        //use for storing later if command is valid
        userInputString = input;
        String[] parsedArray = input.split("\\s+");

        if (checkForValidityAndAssignToCommand(parsedArray)) {
            error = "";
            isValid = true;
        } else {
            error = BAD_COMMAND_ERROR;
            isValid = false;
        }
    }

    /**
     * try to parse the second and optional third param and catch it
     * if its not a number without throwing exception
     *
     * @param text
     * @return
     */
    private static Integer tryParse(String text) {
        try {
            int t = Integer.parseInt(text);
            if (t > 0) {
                return t;
            } else {
                return -1;
            }
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * check the user input for validity and assign to command if true
     *
     * @param parsedArray
     * @return boolean
     */
    private static boolean checkForValidityAndAssignToCommand(String[] parsedArray) {
        //first see if the length is correct
        if (checkForValidCommandArrayLength(parsedArray)) {
            //assign the command to a temp string
            String command = parsedArray[0].toLowerCase().trim();
            //see if it is a valid command
            if (constantsList.contains(command)) {
                //assign the string command
                userCommand = command;
                //try to parse the second and optional third
                commandInt = tryParse(parsedArray[1]);
                //if its a number > 0, assign and continue
                if (commandInt != -1) {
                    //if its a MV command make sure there is a third param and its a #
                    if (userCommand.equals(MV)) {
                        if (checkForValidMoveCommand(parsedArray)) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return true;
                    }
                } else {
                    error = BAD_INT_ERROR;
                }
            }
        }
        return false;
    }

    /**
     * make sure the command array length is valid
     * we need at least 2 commands no more that 3. if not provided then re-prompt
     */
    private static boolean checkForValidCommandArrayLength(String[] parsedArray) {
        if (parsedArray.length <= 1 || parsedArray.length > 3) {
            error = BAD_COMMAND_ERROR;
            return false;
        }
        return true;
    }

    /**
     * check for a valid third parameter for MV command
     * and assign moveFromCommand to commandInt and commandInt to be
     *
     * @param parsedArray
     * @return
     */
    private static boolean checkForValidMoveCommand(String[] parsedArray) {

        if (parsedArray.length != 3) {
            error = BAD_MV_ERROR;
            return false;
        } else {

            int tempMoveToCommand = tryParse(parsedArray[2]);
            if (tempMoveToCommand == -1) {
                return false;
            }
            //if all is well then assign moveFromCommand to commandInt for easier
            //adding functionality
            moveFromCommand = commandInt;
            commandInt = tempMoveToCommand;
        }
        return true;
    }
}
