package models.command;

public class CommandResult {
        private boolean success;
        public Object data;
        private String errorInfo;

    public void setSuccesful(boolean b) {
        success = b;
    }

    public void setErrorMessage(String toString) {
        errorInfo = toString;
    }

    public boolean isSuccessful() {
        return success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return errorInfo;
    }

}
