package view.presenter;

import com.example.cs340.tickettoride.GameBoardActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import client.ClientModel;
import client.PlayerStates.YourTurnDefault;
import models.TTR_Constants;
import models.data.Result;
import models.data.Route;
import view.presenterInterface.IRoutePresenter;

public class RoutePresenter implements IRoutePresenter, Observer {
    private final GameBoardActivity activity;
    private Map<Route, Boolean> hasBeenDrawn;

    public RoutePresenter(GameBoardActivity activity) {
        this.activity = activity;
        hasBeenDrawn = new HashMap<>();
        for (Route r: TTR_Constants.getInstance().getStartingRouteSet()) {
            hasBeenDrawn.put(r, false);
        }
        ClientModel model = ClientModel.create();
        update(model, null);
        model.setRoutePresenter(this);
    }

    @Override
    public Boolean purchase(Route route) {
        System.out.println("Trying to purchase a route!");
        Boolean routeDrawn = hasBeenDrawn.get(route);
        Route pair = TTR_Constants.getInstance().getRouteToPair().get(route);

        ClientModel model = ClientModel.create();
        if (model.state.getClass() != YourTurnDefault.class) {
            activity.popToast("Current State Is " + model.state.getClass().toString() +
                    "\nYou Cannot Purchase A Route At This Time", false);
            return false;
        }

        if (routeDrawn == null) {
            System.out.println("ERROR: routeDrawn in purchase method is null...");
            return false;
        }
        else if (routeDrawn){
            String output = "route has already been bought";
            System.out.println(output);
            activity.popToast(output, false);
            return false;
        }
        else if (pair != null) {
            Boolean pairDrawn = hasBeenDrawn.get(pair);
            if (pairDrawn == null) {
                System.out.println("ERROR: pair is not null, but pairDrawn is null...");
            }
            else if (pairDrawn && (model.getPlayer().getRoutesOwned().contains(pair) || model.getActiveGame().getPlayers().size() < 5)) {
                String output = "You Cannot Purchase Parallel Routes";
                System.out.println(output);
                activity.popToast(output, false);
                return false;
            }
        }
        System.out.println("routeDrawn is false. Proceeding");

        Integer purchaseCardColor = route.getCardColor();
        if (purchaseCardColor == TTR_Constants.getInstance().WILD) {
            if (activity.getSelectedTicketColor() == TTR_Constants.getInstance().EMPTY) {
                activity.popToast("Please Tap a Ticket Card Color \n" +
                        "to Select a Card Color in order \n" +
                        "to Purchase this Gray Route", true);
                return false;
            }
            purchaseCardColor = activity.getSelectedTicketColor();
        }
        Map<Integer, Integer> ticketHand = model.getPlayer().getTickets();

        Integer ownedWilds = ticketHand.get(TTR_Constants.getInstance().WILD);
        System.out.println(ownedWilds);
//        if (ownedWilds != null && ownedWilds > 0) {
//            numWilds = activity.getPurchaseNumberWilds();
//        }
        Integer sumOfTicketCards;
        if (activity.getSelectedTicketColor() == TTR_Constants.getInstance().WILD) {
            sumOfTicketCards = ownedWilds;
        }
        else {
            sumOfTicketCards = ticketHand.get(purchaseCardColor) + ownedWilds;
        }


        // check if the player has the necessary cards for the purchase
        if (sumOfTicketCards >= route.findLength() &&
                model.getPlayer().getTrainsRemaining() >= route.findLength()) {
            //FIXME: add the purchasing color to the purchaseRoute method
            Result result;
            int numOfWildsNeeded;
            if (activity.getSelectedTicketColor() == TTR_Constants.getInstance().WILD) {
                numOfWildsNeeded = 0;
            }
            else {
                numOfWildsNeeded = route.findLength() - ticketHand.get(purchaseCardColor);
            }

            if (numOfWildsNeeded > 0) {
                String output = "Purchasing route using " + route.findLength() +
                        " cards of the selected color";
                System.out.println(output);
                activity.popToast(output, true);
                result = model.purchaseRoute(route, numOfWildsNeeded,activity.getSelectedTicketColor());
            }
            else {
                String output = "Purchasing route using " + route.findLength() +
                        " cards of the selected color and 0 WILD cards";
                System.out.println(output);
                activity.popToast(output, true);
                result = model.purchaseRoute(route, 0,activity.getSelectedTicketColor());
            }

            if (result != null && result.isSuccessful()) {
                System.out.println("Server Command Call Successful...");
                return true; //This should probably reflect the success of the method call...
            }
            else {
                System.out.println("ERROR: Server Command Call Failed...");
                return false;
            }
        }
        if (!(sumOfTicketCards >= route.findLength())) {
            String output = "Sum of Selected Color Cards and Wild Cards: " +
                    sumOfTicketCards + " (" + ownedWilds + " WILD cards)" +
                    "\n is less than the cost of the route: " + route.findLength();
            System.out.println(output);
            activity.popToast(output, true);
        }

        if (!(model.getPlayer().getTrainsRemaining() >= route.findLength())) {
            String output = "You don't have enough trains left for that";
            System.out.println(output);
            activity.popToast(output, false);
        }
        System.out.println("Didn't purchase route...");

        return false;
    }


    @Override
    public void update(Observable o, Object arg) {
        System.out.println("RoutePresenter.update");
        activity.new updateRouteAsyncTask(activity, hasBeenDrawn).execute();
    }


}
