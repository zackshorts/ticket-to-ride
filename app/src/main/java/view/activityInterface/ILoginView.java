package view.activityInterface;

public interface ILoginView {
    void updateServerStatus(String status);
    void sendResponse(String message);
    void notifyLoginStarted();
}