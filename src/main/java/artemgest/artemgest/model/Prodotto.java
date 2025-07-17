package artemgest.artemgest.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;

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
    private String upc;

    @NotNull
    @Column(nullable = false)
    private int quantitaDisponibile;

    @OneToMany(mappedBy = "prodotto")
    @JsonBackReference
    private List<DettaglioOrdine> dettaglioOrdine;

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

    public List<DettaglioOrdine> getDettaglioOrdine() {
        return dettaglioOrdine;
    }

    public void setDettaglioOrdine(List<DettaglioOrdine> dettaglioOrdine) {
        this.dettaglioOrdine = dettaglioOrdine;
    }

    public int getQuantitaDisponibile() {
        return quantitaDisponibile;
    }

    public void setQuantitaDisponibile(int quantitaDisponibile) {
        this.quantitaDisponibile = quantitaDisponibile;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

}
