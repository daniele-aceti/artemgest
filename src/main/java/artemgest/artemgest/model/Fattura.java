package artemgest.artemgest.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Fattura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "yyy-MM-dd")
    @Column(nullable = false)
    private LocalDate dataInizioFattura;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataScadenzaFattura;

    @Column(nullable = false)
    private BigDecimal iva;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    private StatoIva statoIva;

    @Enumerated(EnumType.STRING)
    private StatoFattura statoFattura;

    @Column(nullable = false)
    private BigDecimal importoTotale;

    @OneToOne
    @JoinColumn(name = "ordine_id", nullable = false)
    private Ordine ordine;

    public StatoIva getStatoIva() {
        return statoIva;
    }

    public void setStatoIva(StatoIva statoIva) {
        this.statoIva = statoIva;
    }

    public StatoFattura getStatoFattura() {
        return statoFattura;
    }

    public void setStatoFattura(StatoFattura statoFattura) {
        this.statoFattura = statoFattura;
    }

    public LocalDate getDataInizioFattura() {
        return dataInizioFattura;
    }

    public void setDataInizioFattura(LocalDate dataInizioFattura) {
        this.dataInizioFattura = dataInizioFattura;
    }

    public LocalDate getDataScadenzaFattura() {
        return dataScadenzaFattura;
    }

    public void setDataScadenzaFattura(LocalDate dataScadenzaFattura) {
        this.dataScadenzaFattura = dataScadenzaFattura;
    }

    public Long getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getImportoTotale() {
        return importoTotale;
    }

    public void setImportoTotale(BigDecimal importoTotale) {
        this.importoTotale = importoTotale;
    }

    public Ordine getOrdine() {
        return ordine;
    }

    public void setOrdine(Ordine ordine) {
        this.ordine = ordine;
    }

}
