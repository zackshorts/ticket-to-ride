package view.facade.client;

import java.util.ArrayList;

import client.ClientModel;
import models.data.User;
import models.data.Game;

public class ClientFacade {
	private ClientModel client;

	public ClientFacade () {
		this.client = ClientModel.create();
	}

	public ArrayList<Game> getGames() {
		return client.getLobbyGamesList();
	}
	public void setUser(User newUser) {
		client.setUserPlayer(newUser);
	}

	public User getPlayer() {
		return this.client.getUser();
	}
}
