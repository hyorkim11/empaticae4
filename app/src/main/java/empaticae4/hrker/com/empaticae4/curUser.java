package empaticae4.hrker.com.empaticae4;


public class curUser {

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
