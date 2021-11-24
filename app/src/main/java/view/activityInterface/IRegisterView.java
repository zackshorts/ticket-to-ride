package view.activityInterface;

public interface IRegisterView {
    void updateServerStatus(String status);
    void sendResponse(String message);
    void notifyRegisterStarted();
}
