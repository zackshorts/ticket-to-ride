package view.facade;

import java.util.ArrayList;

import client.ClientModel;
import models.data.Game;
import models.data.User;
import models.data.Result;
import client.ServerProxy;

public class ViewFacade {
	private ServerProxy serverCommunicator;
	
	public ViewFacade() {
		serverCommunicator = new ServerProxy();
	}
	
	public Result register(User newUser) {
		return serverCommunicator.register(newUser);
	}

	public Result login(User returnUser) {
		Result Result = serverCommunicator.login(returnUser);
		return Result;
	}

//	public ArrayList<Game> getGameList() {
//        ClientModel model = new ClientModel();
//	    return model.get();
//	}

	public Result loginUser(User returningUser) {
		return null;
	}
}
