package mg.itu.dto;

public class LoginRequest {
    private String telephone;
    private String password;
    private String userType; 

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getUserType() {
        return userType;
    }
    
    public void setUserType(String userType) {
        this.userType = userType;
    }

    public LoginRequest(String telephone, String password) {
        this.telephone = telephone;
        this.password = password;
    }

    public LoginRequest(String telephone, String password, String userType) {
        this.telephone = telephone;
        this.password = password;
        this.userType = userType;
    }
    public LoginRequest() {
    }
    
}