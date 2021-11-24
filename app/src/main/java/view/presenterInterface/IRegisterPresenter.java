package view.presenterInterface;

import models.data.Result;

public interface IRegisterPresenter {
    public Result registerUser(String username, String password, String repeatedPassword);
}