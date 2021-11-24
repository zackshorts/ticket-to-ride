package view.presenter;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Pattern;

import client.ClientModel;
import client.ServerProxy;
import models.data.Result;
import models.data.User;
import view.facade.client.ClientFacade;
import view.facade.client.out.RegisterFacadeOut;
import view.presenterInterface.IRegisterPresenter;
import view.activityInterface.IRegisterView;

public class RegisterPresenter implements IRegisterPresenter, Observer {
    private final String REGISTER_SUCCESSFUL = "Username and Password Registered. Logging In...";
    private final String NO_PASSWORD_MATCH = "Passwords Do Not Match. Please Input Carefully.";
    private final String BAD_PASSWORD = "Password Not Accepted. Password Must Be \n"
            + "- At least 5 Characters \n "
            + "- At least 1 Number \n "
            + "- No Non-Alphanumeric Symbols \n";

    private final String BAD_USERNAME = "Username Not Accepted. Username Must be \n "
            + "- Atleast 5 Charachers \n "
            + "- No Non-Alphanumeric Characters";
    private final String REGISTER_FAILED = "Register Failed Unexpectedly";

    private final String PASSWORD_CRITERIA = "[a-zA-Z1-9]{5,}+";
    private final String USERNAME_CRITERIA = "[a-zA-Z1-9]{5,}+";

    private IRegisterView registerView = null;

    /**
     * desciption - the registerPresenter is the class that calls the correct methods in the back-end
     *              and performs simple logic on the information that the registerActivity sends it
     *
     * exceptions - None
     *
     * pre-conditions - There must be an existing IRegisterPresenter class correctly written that
     *                  this class can implement
     *
     * post-conditions - There are no post-conditions for the constructor
     *
     */
    public RegisterPresenter() {}

    /**
     *
     * description - the registerUser method takes in three strings - a username, and password, and
     *              the repeated password.  It then evaluates the two password strings to make sure
     *              they match each other, that they match the pre-defined reg-ex pattern we've set
     *              as a contraint, and then calls the registerUser function in the server.  It returns
     *              a result that says if everything was performed successfully or not
     *
     * @param username - This is the username string that the server will check to see if it exists
     *                 already in the database.  If all is good, that user will use this
     * @param password - The function will check if this string password was inputted correctly and
     *                 assign it to be the user's password
     * @param repeatedPassword - This is the second string that the function compares the the first
     *                         password to to make should that the user entered it in correctly
     * @return Result - the result will contain the message if the command was completed successfully
     *                          or not
     *
     * exceptions - There are no exceptions that could occur in this method, so no exceptions are defined
     *
     * pre-conditions - The only pre-conditions for this is to make sure that what's passed through
     *                  is a string.  Besides that, all of the logic of making sure that
     *                  what was inputted was correct is performed in this method.  Any type of
     *                  string can be sent to this method, and it would still be able to work
     *
     * post-conditions - When the method returns, a Result object will be created and returned
     *                  If it's a positive response, then the result will not have an errorCode,
     *                  and if it was a negative response, then the result will have an errorCode.
     *                  This is what the post-condition is guaranteed to be
     */
    @Override
    public Result registerUser(String username, String password, String repeatedPassword) {
        Result result = new Result();
        //Compare Passwords
        if (password.compareTo(repeatedPassword) != 0) {
            result.setErrorMessage(NO_PASSWORD_MATCH);
            result.setSuccessful(false);
            return result;
        }

        //Match Password to Reg-ex
        if (!checkRegex(username, this.USERNAME_CRITERIA)) {
            result.setErrorMessage(BAD_USERNAME);
            result.setSuccessful(false);
            return result; //If password characters are unacceptable
        }

        //Match Username to Reg-ex
        if (!checkRegex(password, this.PASSWORD_CRITERIA)) {
            result.setSuccessful(false);
            result.setErrorMessage(BAD_PASSWORD);
            return result; //If username characters are unacceptable
        }

        //RegisterActivity.NotifyRegisterStarted()

        User newUser = new User(username, password);
        RegisterFacadeOut registerFacadeOut = new RegisterFacadeOut();

        Result registerResult = registerFacadeOut.register(newUser);
        //Transistion to next view: gameLobby

        if (registerResult == null) {
            result.setErrorMessage(REGISTER_FAILED);
            result.setSuccessful(false);
            return result;
        }

        if (registerResult.isSuccessful()) {
            ClientFacade client = new ClientFacade();
            client.setUser(newUser);
            result.setErrorMessage(REGISTER_SUCCESSFUL);
            result.setSuccessful(true);
            return result;
        }
        else {
            return registerResult;
        }


    }

    /**
     * description - this method is an override for the Observer class.  It's purpose is to
     *              push updated and changed information to the poller that our game uses,
     *              which then pushes all that information to the update functions, which individually
     *              update their client models with up-to-date information
     *
     * @param o - this is an object used by the poller to be able to update everyone's screens
     *          simultaneously and make sure everything is in sync with each other
     * @param obj - this is a generic object that is passed in as an optional parameter.
     *            Turns out that only the Observable was needed in order to make
     *            the update function work for us, so Object is not used
     *
     * Exceptions - none
     *
     *  pre-conditions - The preconditions are making sure that the object that we end up casting
     *            (in our case, the client model) is what is being passed as the Observable
     *  post-conditions - The post-conditions are an updated client model that is set from the
     *            Observable passed in
     */
    @Override
    public void update(Observable o, Object obj) {
        ClientModel client = (ClientModel) o;
    }

    private boolean checkRegex(String input, String criteria) {

        return Pattern.matches(criteria, input);
    }

}
