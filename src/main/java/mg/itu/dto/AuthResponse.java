package mg.itu.dto;

public class AuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private String idUtilisateur;
    private String telephone;
    private String role;

    public AuthResponse(String accessToken, String idUtilisateur, String telephone, String role) {
        this.accessToken = accessToken;
        this.idUtilisateur = idUtilisateur;
        this.telephone = telephone;
        this.role = role;
    }

    

    public AuthResponse(String accessToken, String telephone) {
        this.accessToken = accessToken;
        this.telephone = telephone;
    }



    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(String idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
