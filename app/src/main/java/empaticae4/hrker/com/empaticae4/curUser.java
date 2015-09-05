package empaticae4.hrker.com.empaticae4;


import android.app.Application;

public class curUser extends Application{

    private String userName;

    public String getName() {

        if (userName.isEmpty()) {
            return "none";
        } else {
            return userName;
        }
    }

    public void setName(String inputName) {
        userName = inputName;
    }
}
