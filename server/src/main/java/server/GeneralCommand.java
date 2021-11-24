package server;

import java.io.Serializable;
import java.lang.reflect.Method;

import models.command.CommandResult;
import models.command.ICommandExecuter;
import models.data.Result;


public class GeneralCommand implements ICommandExecuter, Serializable {
/*
    ---README---
    This code is very good, exactly what we need. The TAs seemed to be
    uncertain about whether this needs to its own class or whether this
    can be done withing the Server class. Please consult a TA about this
    specific class.

*/

    public void set_className(String _className) {
        this._className = _className;
    }

    public void set_methodName(String _methodName) {
        this._methodName = _methodName;
    }

    public void set_paramTypes(Class<?>[] _paramTypes) {
        this._paramTypes = _paramTypes;
    }

    public void set_paramValues(Object[] _paramValues) {
        this._paramValues = _paramValues;
    }

    public String get_className() {
        return _className;
    }

    public String get_methodName() {
        return _methodName;
    }

    public Class<?>[] get_paramTypes() {
        return _paramTypes;
    }

    public Object[] get_paramValues() {
        return _paramValues;
    }

    public GeneralCommand() {
    }

    public String _className;
    public String _methodName;
    public Class<?>[] _paramTypes;
    public Object[] _paramValues;


    public GeneralCommand(String methodName,
                          Class<?>[] paramTypes, Object[] paramValues) {
        _methodName = methodName;
        _paramTypes = paramTypes;
        _paramValues = paramValues;
    }

    @Override
    public Result exec() {
        CommandResult commandResult = new CommandResult();
        Result result = new Result();
        try {

            Class<?> receiver = Class.forName(ServerCommands.class.getName());
            for (int i = 0; i < _paramTypes.length; ++i) {
                _paramValues[i] = _paramTypes[i].cast(_paramValues[i]);
            }
            Method method = receiver.getMethod(_methodName, _paramTypes);
            Object o = method.invoke(receiver.newInstance(), _paramValues);
            commandResult.setData(o);
            result = (Result)commandResult.getData();
        }
        catch (Exception e) {
            e.printStackTrace();
            result.setErrorMessage(e.toString());
            result.setSuccessful(false);
        }
        return result;
    }

}
