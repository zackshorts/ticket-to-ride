package view.presenterInterface;

import models.data.Result;

public interface ILoginPresenter {
	public Result loginUser(String username, String password);
	public boolean onCreate();
}