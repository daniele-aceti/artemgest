package artemgest.artemgest.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ragioneSociale;

    @Size(min = 11, max = 16)
    private String pIvaCFiscale;

    @NotBlank(message = "Inserire email corretta")
    @Column(nullable = false)
    private String email;

    @NotBlank(message = "Inserire telefono corretto")
    @Column(nullable = false)
    private String telefono;

    @NotBlank(message = "Inserire indirizzo corretto")
    @Column(nullable = false)
    private String indirizzo;

    @NotBlank(message = "Inserire città corretta")
    @Column(nullable = false)
    private String citta;

    @NotBlank(message = "Inserire cap corretto")
    @Column(nullable = false)
    private String cap;

    @NotBlank(message = "Inserire provincia corretta")
    @Column(nullable = false)
    private String provincia;

    @OneToMany(mappedBy = "cliente")
    @JsonBackReference
    private List<Fattura> fatture;

    public List<Fattura> getFatture() {
        return fatture;
    }

    public void setFatture(List<Fattura> fatture) {
        this.fatture = fatture;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRagioneSociale() {
        return ragioneSociale;
    }

    public void setRagioneSociale(String ragioneSociale) {
        this.ragioneSociale = ragioneSociale;
    }
    

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getpIvaCFiscale() {
        return pIvaCFiscale;
    }

    public void setpIvaCFiscale(String pIvaCFiscale) {
        this.pIvaCFiscale = pIvaCFiscale;
    }

}
