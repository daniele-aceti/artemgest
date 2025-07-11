package artemgest.artemgest.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private double prezzo;

    @Column(nullable = false)
    private String UPC;

    @OneToMany(mappedBy = "prodotto")
    @JsonBackReference
    private List<DettaglioOrdine> dettaglioOrdine;

    @OneToOne(mappedBy = "prodotto")
    private Magazzino magazzino;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public String getUPC() {
        return UPC;
    }

    public void setUPC(String uPC) {
        UPC = uPC;
    }

    public List<DettaglioOrdine> getDettaglioOrdine() {
        return dettaglioOrdine;
    }

    public void setDettaglioOrdine(List<DettaglioOrdine> dettaglioOrdine) {
        this.dettaglioOrdine = dettaglioOrdine;
    }

}
